package com.cellsgame.game.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.Pair;
import com.cellsgame.game.module.RandomObj;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * File Description.
 *
 * @author Yang
 */
public class RandomUtils {
    private static final Map<Map<Integer, Integer>, Integer> CACHE = new ConcurrentHashMap<>(10000);

    private static final Map<String, Pair<Integer, List<Pair<Integer, Integer>>>> randomCache = new ConcurrentHashMap<>(10000);


    /**
     * 按机率随机。
     * <p/>
     * 配置格式=id:weight,id:weight。 如果不包括任何分隔符，直接返回文本的整数表示
     *
     * @param str 机率配置
     * @return 配置ID
     */
    public static int random(String str) {
        //
        if (!StringUtils.contains(str, ',') && !StringUtils.contains(str, ':')) {
            return Converter.toInteger(str);
        }
        // 查找是否已有记录
        Pair<Integer, List<Pair<Integer, Integer>>> reference = randomCache.get(str);
        // 如果没有记录
        if (reference == null) {
            // 所有配置ID和权重
            String[] idsInfo = str.split(",");
            // 总权重值
            int allWeight = 0;
            // 列表
            List<Pair<Integer, Integer>> config = new ArrayList<>(idsInfo.length);

            for (String idInfo : idsInfo) {
                String[] info = idInfo.split(":");
                int id = Converter.toInteger(info[0]);
                int weight = Converter.toInteger(info[1]);
                // 权重
                config.add(Pair.valueOf(id, weight));
                allWeight += weight;
            }
            // 解析结果
            reference = Pair.valueOf(allWeight, config);
            // 添加
            randomCache.putIfAbsent(str, reference);
        }
        // 随机权重
        int randomAuth = ThreadLocalRandom.current().nextInt(reference.getLeft() + 1);
        // 查看
        for (Pair<Integer, Integer> config : reference.getRight()) {
            // 当前权重
            if (randomAuth <= config.getRight()) {
                return config.getLeft();
            } else {
                randomAuth -= config.getRight();
            }
        }
        return 0;
    }

    /**
     * 按机率随机。
     * <p/>
     * 配置格式=id:weight,id:weight。 如果不包括任何分隔符，直接返回文本的整数表示
     * <p>
     * 默认使用缓存，用于避免重复计算累计权重
     *
     * @param config 机率配置
     * @return 配置ID
     */
    public static int random(Map<Integer, Integer> config) {
        //
        return random(config, true);
    }

    /**
     * 按机率随机。
     * <p/>
     * 配置格式=id:weight,id:weight。 如果不包括任何分隔符，直接返回文本的整数表示
     *
     * @param config   机率配置
     * @param useCache 是否使用缓存
     * @return 配置ID
     */
    public static int random(Map<Integer, Integer> config, boolean useCache) {
        //
        if (CollectionUtils.isEmpty(config)) return 0;
        // 查找缓存
        Integer totalWeight = useCache ? CACHE.get(config) : null;
        // 如果没找到
        if (totalWeight == null) {
            // 计算总权重值
            totalWeight = 0;
            // 遍历配置
            for (Integer weight : config.values()) {
                // 累计
                totalWeight += weight;
            }
            // 保存
            if (useCache) CACHE.putIfAbsent(config, totalWeight);
        }
        // 随机权重
        int randomAuth = ThreadLocalRandom.current().nextInt(totalWeight) + 1;
        // 查看
        for (Map.Entry<Integer, Integer> entry : config.entrySet()) {
            // 当前权重
            if (randomAuth <= entry.getValue()) {
                return entry.getKey();
            } else {
                randomAuth -= entry.getValue();
            }
        }
        return 0;
    }

    /**
     * 从指定随机数据库，按照权重随机抽取不重复的数据数量size
     *
     * @param size        数量
     * @param totalWeight 总权重
     * @param data        随机数据
     * @return 随机结果
     */
    public static Set<Integer> random(int size, int totalWeight, Map<Integer, Integer> data) {
        //
        Random random = ThreadLocalRandom.current();
        // 累计权重
        int weight = 0;
        // 随机权重，
        LinkedList<Integer> randomWeight = new LinkedList<>();
        // 随机
        for (int i = 0; i < size; i++) randomWeight.push(random.nextInt(totalWeight) + 1);
        // 随机数排序
        if (randomWeight.size() > 0) Collections.sort(randomWeight);
        // 结果
        Set<Integer> result = Sets.newHashSetWithExpectedSize(randomWeight.size());
        // 如果有数量
        if (randomWeight.size() > 0) {
            // 随机英雄
            for (Map.Entry<Integer, Integer> entry : data.entrySet()) {
                //
                weight += entry.getValue();
                // 如果满足
                if (weight >= randomWeight.peek()) {
                    // 随机权重失效
                    randomWeight.poll();
                    //
                    result.add(entry.getKey());
                    // 如果所有权重使用完成
                    if (randomWeight.size() <= 0) break;
                }
            }
        }
        // 如果还有权重没有使用
        if (randomWeight.size() > 0) {
            List<Integer> fill = new ArrayList<>(data.keySet());
            // 遍历所有
            for (int i = fill.size() - 1; i >= 0; i--) {
                // 如果已在结果列表
                if (result.contains(fill.get(i))) continue;
                // 加入结果列表
                result.add(fill.get(i));
                // 权重失效
                randomWeight.poll();
                // 如果没有权重, 不再进行填充
                if (randomWeight.size() <= 0) break;
            }
        }
        return result;
    }

    /**
     * 随机 给出数组下标
     *
     * @param attRate 概率数组  填小于零的概率表示必出
     * @param usedIX  已经使用过的下标
     * @return -1 表示随机失败
     */
    public static int randomIX(int[] attRate, int usedIX) {
        int sum = 0;
        int length = attRate.length;
        int[] tmp = new int[length];
        for (int i = 0; i < length; i++) {
            if (!GameUtil.checkIntFlag(usedIX, i)) {
                // 小于0 必出
                int rate = attRate[i];
                if (rate < 0) return i;
                else {
                    sum += rate;
                    tmp[i] = rate;
                }
            }
        }
        if (sum > 0) {
            int random = GameUtil.r.nextInt(sum);
            sum = 0;
            for (int i = 0; i < length; i++) {
                int rate = tmp[i];
                if (rate > 0) {
                    sum += rate;
                    if (sum > random) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 从指定随机池随机指定数量的元素
     *
     * @param pool 随机元素池
     * @param size 数量
     * @return 随机结果，不改变原随机池
     */
    public static <T> Set<T> random(List<T> pool, int size) {
        if (pool == null) return Collections.emptySet();
        //
        size = Math.max(1, size);
        // 如果目标数量超过随机池大小
        if (size >= pool.size()) return new HashSet<>(pool);
        //
        Collections.shuffle(pool);
        //
        int begin = nextInt(pool.size() - size + 1);
        //
        return Sets.newHashSet(pool.subList(begin, begin + size));
    }

    /**
     * 从指定随机池随机指定数量的可重复的元素
     *
     * @param pool 随机元素池
     * @param size 数量
     * @return 随机结果，不改变原随机池
     */
    public static <T> List<T> random(T[] pool, int size) {
        if (pool == null || size <= 0) return Collections.emptyList();
        //
        List<T> data = Lists.newArrayList(pool);
        //
        size = Math.max(1, size);
        // 查看是否需要补数据
        int offset = size - data.size();
        // 如果需要补数据
        while (offset > 0) {
            // 补数据
            data.addAll(Lists.newArrayList(pool));
            //
            offset = size - data.size();
        }
        //
        Collections.shuffle(data);
        //
        int begin = nextInt(data.size() - size + 1);
        //
        return Lists.newArrayList(data.subList(begin, begin + size));
    }

    /**
     * 从随机池随机结果
     *
     * @param totalWeight 总权重
     * @param pool        随机池
     * @param <T>         随机结果类型
     * @return 随机结果
     */
    public static <T extends RandomObj> T random(int totalWeight, Collection<T> pool) {
        //
        if (CollectionUtils.isEmpty(pool)) return null;
        // 如果只有一个
        if (totalWeight > 0 && pool.size() == 1) return pool.iterator().next();
        // 随机权重
        int randomAuth = ThreadLocalRandom.current().nextInt(totalWeight) + 1;
        // 查看
        for (T t : pool) {
            // 当前权重
            if (randomAuth <= t.getWeight()) {
                return t;
            } else {
                randomAuth -= t.getWeight();
            }
        }
        return null;
    }

    /**
     * 随机数字
     *
     * @param excludeMax 最大值，不包括
     * @return [0, excludeMax)
     */
    public static int nextInt(int excludeMax) {
        return ThreadLocalRandom.current().nextInt(excludeMax);
    }

    /**
     * 随机最大最小值
     *
     * @param includeMin 最小值
     * @param includeMax 最大值
     * @return [min, max)
     */
    public static int nextInt(int includeMin, int includeMax) {
        return ThreadLocalRandom.current().nextInt(Math.min(includeMax, includeMin), Math.max(includeMax, includeMin) + 1);
    }

    /**
     * @param range [min,max]
     * @return [min, max)
     */
    public static int nextInt(int[] range) {
        return ArrayUtils.isEmpty(range) ? 0 : (range.length == 1 ? range[0] : nextInt(range[0], range[1]));
    }
}
