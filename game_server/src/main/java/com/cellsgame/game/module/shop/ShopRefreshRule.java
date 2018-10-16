package com.cellsgame.game.module.shop;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.Pair;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.bo.ShopBO;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;
import com.cellsgame.game.module.shop.msg.CodeShop;
import com.cellsgame.game.module.shop.vo.ShopExtraVO;
import com.cellsgame.game.module.shop.vo.ShopItemVO;
import com.cellsgame.game.module.shop.vo.ShopVO;
import com.cellsgame.game.util.Converter;
import com.cellsgame.game.util.RandomUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * 商店商品刷新规则.
 * <p>
 * fix: 按照固定商品组刷新，参数(商品组列表)
 * weight : 按照商品权重来刷新，参数(商品组规则, 数量)
 * vip : 按VIP等级来执行刷新，参数（商品组规则, 等级偏移量, 数量）
 * sequence : 按固定配置序列刷新，参数（序列参数【商品组排列】）
 * bl: 按照公会建筑等级刷新，参数（商品组规则，数量）
 *
 * @author Yang
 */
public enum ShopRefreshRule {

    Fix("fix") {
        // 按照固定商品组顺序来刷新，参数(商品组列表)
        @Override
        public List<ShopItemVO> refresh(PlayerVO playerVO, ShopVO shopVO) throws LogicException {
            String[] params = shopVO.getCfg().getRefreshParams();
            // 参数【商品组列表】
            CodeShop.SHOP_REFRESH_RULE_PARAM_ERROR.throwIfTrue(ArrayUtils.isEmpty(params));
            // 随机结果
            List<ShopItemVO> results = GameUtil.createList();
            // 查看所有参数
            for (int i = 0; i < params.length; i++) {
                //
                int groupId = Converter.toInteger(params[i]);
                // 查看当前商品组数据
                GroupShopItem groupShopItem = ShopBO.ShopItemGroup.get(groupId);
                //
                if (groupShopItem == null) {
                    log.error("[ShopRefreshRule] group [string={}], [int={}] not found for shop [{}]", params[i], groupId, shopVO.getCid());
//                    continue;
                }
                // 查看能从当前组选取的结果
//                Pair<Integer, SortedSet<ShopItemConfig>> match = groupShopItem.match(playerVO.getLevel());
                // 添加结果
//                results.add(create(i, RandomUtils.random(match.getLeft(), match.getRight())));
            }
            return results;
        }
    },
    Weight("weight") {
        // 按照商品权重来刷新，参数(商品组规则, 数量)
        @Override
        public List<ShopItemVO> refresh(PlayerVO playerVO, ShopVO shopVO) throws LogicException {
            String[] params = shopVO.getCfg().getRefreshParams();
            // 参数
            CodeShop.SHOP_REFRESH_RULE_PARAM_ERROR.throwIfTrue(ArrayUtils.getLength(params) != 2);
            // 随机商品组
            int destGroup = RandomUtils.random(params[0]);
            // 目标数量
            int quantity = Converter.toInteger(params[1]);
            // 查看当前商品组数据
            GroupShopItem groupShopItem = ShopBO.ShopItemGroup.get(destGroup);
            // 查看能从当前组选取的结果
//            Pair<Integer, SortedSet<ShopItemConfig>> match = groupShopItem == null ? null : groupShopItem.match(playerVO.getLevel());
            // 如果不存在商品组数据
//            CodeShop.SHOP_GROUP_NOT_FOUND.throwIfTrue(groupShopItem == null || match == null || CollectionUtils.isEmpty(match.getRight()));
            // 随机结果
            List<ShopItemVO> results = GameUtil.createList();
            // 次数
            for (int i = 0; i < quantity; i++) {
                // 添加结果
//                results.add(create(i, RandomUtils.random(match.getLeft(), match.getRight())));
            }
            return results;
        }
    },
    GuildLevel("guildLv") {
        // 按照公会建筑等级，参数(商品组规则, 数量), 不会出现重复结果
        @Override
        public List<ShopItemVO> refresh(PlayerVO playerVO, ShopVO shopVO) throws LogicException {
            // 如果没有公会
            if (playerVO.getGuild() == null) return Collections.emptyList();
            // 规则参数
            String[] params = shopVO.getCfg().getRefreshParams();
            // 参数
            CodeShop.SHOP_REFRESH_RULE_PARAM_ERROR.throwIfTrue(ArrayUtils.getLength(params) != 2);
            // 随机商品组
            int destGroup = RandomUtils.random(params[0]);
            // 目标数量
            int quantity = Converter.toInteger(params[1]);
            // 查看当前商品组数据
            GroupShopItem groupShopItem = ShopBO.ShopItemGroup.get(destGroup);
            // 公会等级 查看能从当前组选取的结果
            Pair<Integer, SortedSet<ShopItemConfig>> match = groupShopItem == null ? null : groupShopItem.matchByGuildLevel(playerVO.getGuild().getLevel());
            // 如果不存在商品组数据
            CodeShop.SHOP_GROUP_NOT_FOUND.throwIfTrue(groupShopItem == null || match == null || CollectionUtils.isEmpty(match.getRight()));
            // 随机结果
            List<ShopItemVO> results = GameUtil.createList();
            //
            int copyWeight = match.getLeft();
            int initCopyWeight = copyWeight;
            // 不能重复出现，需要拷贝随机库
            SortedSet<ShopItemConfig> copy = new TreeSet<>(match.getRight());
            // 次数
            for (int i = 0; i < quantity; i++) {
                // 随机结果
                ShopItemConfig randomConfig = RandomUtils.random(copyWeight, copy);
                // 如果没有结果, 进行下一次
                if (randomConfig == null) continue;
                // 添加结果
                results.add(create(i, randomConfig));
                // 从随机库删除
                copy.remove(randomConfig);
                // 调整累计权重
                copyWeight -= randomConfig.getWeight();
                // 如果随机库没有任何内容可进行随机操作
                if (copy.size() <= 0) break;
                if(copyWeight <= 0){
                    log.error("pname : {}, guildLevel : {} , initCopyWeight : {}, match : {}" , playerVO.getName(), playerVO.getGuild().getLevel(), initCopyWeight, match.getRight());
                    log.error("pname : {}, i : {}, copyWeight : {}, copy : {}" , playerVO.getName(), i, copyWeight, copy);
                    return results;
                }
            }
            return results;
        }
    },
    Vip("vip") {
        // 参数（商品组规则, 等级偏移量, 数量）
        @Override
        public List<ShopItemVO> refresh(PlayerVO playerVO, ShopVO shopVO) throws LogicException {
            String[] params = shopVO.getCfg().getRefreshParams();
            // 参数(只能有3个参数)
            CodeShop.SHOP_REFRESH_RULE_PARAM_ERROR.throwIfTrue(ArrayUtils.getLength(params) != 3);
            // 商品组
            String groupRule = params[0];
            // 等级
            int levelOffset = Converter.toInteger(params[1]);
            // 数量
            int quantity = Converter.toInteger(params[2]);
            // 随机商品组
            int destGroup = RandomUtils.random(groupRule);
            // 查看当前商品组数据
            GroupShopItem groupShopItem = ShopBO.ShopItemGroup.get(destGroup);
            // 查看能从当前组选取的结果
//            Pair<Integer, SortedSet<ShopItemConfig>> match = groupShopItem == null ? null : groupShopItem.match(playerVO.getLevel());
            // 如果不存在商品组数据
//            CodeShop.SHOP_GROUP_NOT_FOUND.throwIfTrue(groupShopItem == null || match == null || CollectionUtils.isEmpty(match.getRight()));
            //
//            int cacheKey = playerVO.getVip() * 100 + playerVO.getLevel();
            // 查看是否已有筛选结果
//            Pair<Integer, TreeSet<ShopItemConfig>> exists = CACHE.get(cacheKey);
            // 如果没有结果
//            if (exists == null) {
//                // 结果总权重
//                int totalWeight = 0;
//                // 筛选结果
//                TreeSet<ShopItemConfig> vipMatchData = new TreeSet<>();
//                // 筛选
//                for (ShopItemConfig config : match.getRight()) {
//                    // 如果满足条件
//                    if (config.getRefreshRequireVip() == playerVO.getVip() && config.getVisibleLv() >= playerVO.getLevel() - levelOffset && config.getVisibleLv() <= playerVO.getLevel()) {
//                        // 结果总权重
//                        totalWeight += config.getWeight();
//                        //
//                        vipMatchData.add(config);
//                    }
//                }
//                // 保存结果
//                CACHE.putIfAbsent(cacheKey, exists = Pair.valueOf(totalWeight, vipMatchData));
//            }
            //
//            CodeShop.SHOP_REFRESH_RULE_PARAM_ERROR.throwIfTrue(CollectionUtils.isEmpty(exists.getRight()));
            // 随机结果
            List<ShopItemVO> results = GameUtil.createList();
            // 次数
            for (int i = 0; i < quantity; i++) {
                // 添加结果
//                results.add(create(i, RandomUtils.random(exists.getLeft(), exists.getRight())));
            }
            return results;
        }
    },
    Sequence("sequence") {
        @Override
        public List<ShopItemVO> refresh(PlayerVO playerVO, ShopVO shopVO) throws LogicException {
            String[] params = shopVO.getCfg().getRefreshParams();
            // 参数
            CodeShop.SHOP_REFRESH_RULE_PARAM_ERROR.throwIfTrue(ArrayUtils.isEmpty(params));
            // 商店额外数据
            ShopExtraVO extraVO = shopVO.getExtraData();
            // 查看当前商店已按当前规则刷新
            if (ArrayUtils.isNotEmpty(extraVO.getGroupSequence())) {
                // 当前序列已完成
                if (extraVO.getNextGroupIndex() >= extraVO.getGroupSequence().length) {
                    // 如果序列有变化或者序列已全部使用
                    if (StringUtils.join(extraVO.getGroupSequence(), Sequence_Separator).length() != params[0].length() || extraVO.getUsedGroupSequence().size() >= params.length) {
                        // 删除旧数据
                        extraVO.getUsedGroupSequence().clear();
                    }
                    // 序列池
                    ArrayList<String> pool = Lists.newArrayList(params);
                    // 已使用的序列不再参与
                    pool.removeAll(extraVO.getUsedGroupSequence());
                    // 随机序列
                    randomSequence(pool, extraVO);
                }
            } else {// 第一次
                // 随机序列
                randomSequence(Lists.newArrayList(params), extraVO);
            }
            // 下一商品组
            int destGroup = extraVO.getGroupSequence()[extraVO.getNextGroupIndex()];
            // 查看当前商品组数据
            GroupShopItem groupShopItem = ShopBO.ShopItemGroup.get(destGroup);
            // 查看能从当前组选取的结果
//            Pair<Integer, SortedSet<ShopItemConfig>> match = groupShopItem == null ? null : groupShopItem.match(playerVO.getLevel());
            // 如果不存在商品组数据
//            CodeShop.SHOP_GROUP_NOT_FOUND.throwIfTrue(groupShopItem == null || CollectionUtils.isEmpty(match.getRight()));
            // 移动到下一商品组
            extraVO.setNextGroupIndex(extraVO.getNextGroupIndex() + 1);
            // 随机结果
            List<ShopItemVO> results = GameUtil.createList();
            // 下标
            int index = 0;
            // 查看组所有商品
//            for (ShopItemConfig config : match.getRight()) {
//                // 添加
//                results.add(create(index++, config));
//            }
            //
            return results;
        }
    };
    private static final String Sequence_Separator = ":";
    private static final Logger log = LoggerFactory.getLogger(ShopRefreshRule.class);
    private static final Map<Integer, Pair<Integer, TreeSet<ShopItemConfig>>> CACHE = new ConcurrentHashMap<>();
    private String type;

    ShopRefreshRule(String type) {
        this.type = type;
    }

    private static void randomSequence(List<String> pool, ShopExtraVO extraVO) {
        // 随机序列
        Collections.shuffle(pool, ThreadLocalRandom.current());
        // 随机结果
        String newSequence = pool.get(ThreadLocalRandom.current().nextInt(0, pool.size()));
        //
        extraVO.setNextGroupIndex(0);
        //
        extraVO.setGroupSequence(toIntArray(newSequence));
        //
        extraVO.getUsedGroupSequence().add(newSequence);
    }

    private static ShopItemVO create(int index, ShopItemConfig config) {
        ShopItemVO vo = new ShopItemVO();
        vo.setSold(0);
        vo.setCid(config.getId());
        vo.setIndex(index);
        return vo;
    }

    /**
     * 将用:分隔的字符串转换成整数数组
     *
     * @param data
     * @return
     */
    private static int[] toIntArray(String data) {
        if (StringUtils.isEmpty(data)) return ArrayUtils.EMPTY_INT_ARRAY;
        if (!data.contains(Sequence_Separator)) return new int[]{Converter.toInteger(data)};
        String[] elements = StringUtils.split(data, Sequence_Separator);
        int[] ints = new int[elements.length];
        for (int i = elements.length - 1; i >= 0; i--) ints[i] = Converter.toInteger(elements[i]);
        return ints;
    }

    public String getType() {
        return this.type;
    }

    public List<ShopItemVO> refresh(PlayerVO playerVO, ShopVO shopVO) throws LogicException {
        return Collections.emptyList();
    }
}
