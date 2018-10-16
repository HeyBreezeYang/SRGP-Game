package com.cellsgame.game.module.shop;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.Pair;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 商品组数据.
 *
 * @author Yang
 */
public class GroupShopItem {
    // 商品组
    private int group;
    // 总权重
    private int weight;
    // 商品列表
    private SortedSet<ShopItemConfig> items;
    // 是否有多个匹配等级的限制
    private boolean moreRangeRestrict = false;
    //
    private int[] lastRangeRestrict;
    // 玩家等级限制的商品库
    private Map<Integer, Pair<Integer, SortedSet<ShopItemConfig>>> cache = GameUtil.createSimpleMap();
    // 公会等级限制的商品库
    private Map<Integer, Pair<Integer, SortedSet<ShopItemConfig>>> GuildLevelRelateCache = GameUtil.createSimpleMap();

    public GroupShopItem(int group) {
        this.group = group;
        this.weight = 0;
        this.items = Sets.newTreeSet();
    }

    public int getGroup() {
        return group;
    }

    final Pair<Integer, SortedSet<ShopItemConfig>> match(final int level) {
        // 如果有多个区间限制
        if (moreRangeRestrict) {
            // 查看是否已存在筛选结果
            Pair<Integer, SortedSet<ShopItemConfig>> result = cache.get(level);
            // 如果没有
            if (result == null) {
                // 满足条件的累计权重
                int w = 0;
                // 结果
                SortedSet<ShopItemConfig> data = new TreeSet<>();
                // 遍历
                for (ShopItemConfig item : items) {
                    // 如果满足等级限制
                    if (ArrayUtils.isEmpty(item.getRefreshRequireLv()) || (level >= item.getRefreshRequireLv()[0] && level <= item.getRefreshRequireLv()[1])) {
                        // 统计权重
                        w += item.getWeight();
                        // 添加结果
                        data.add(item);
                    }
                }
                //
                cache.put(level, result = Pair.valueOf(w, data));
            }
            // 返回
            return result;
        }
        //
        return Pair.valueOf(weight, items);
    }

    public void addShopItem(ShopItemConfig shopItemConfig) {
        if (shopItemConfig == null || shopItemConfig.getWeight() <= 0) return;
        // 统计权重
        weight += shopItemConfig.getWeight();
        items.add(shopItemConfig);
        // 查看是否有等级限制
        if (lastRangeRestrict == null) lastRangeRestrict = shopItemConfig.getRefreshRequireLv();
        // 可看当前限制等级与最后一个是否相同
        moreRangeRestrict = moreRangeRestrict || !ArrayUtils.isEquals(lastRangeRestrict, shopItemConfig.getRefreshRequireLv());
        // 改变最后一个区间限制
        lastRangeRestrict = shopItemConfig.getRefreshRequireLv();
    }

    final Pair<Integer, SortedSet<ShopItemConfig>> matchByGuildLevel(final int level) {
        // 查看是否已存在筛选结果
        Pair<Integer, SortedSet<ShopItemConfig>> result = GuildLevelRelateCache.get(level);
        // 如果没有
        if (result == null) {
            // 满足条件的累计权重
            int w = 0;
            // 结果
            SortedSet<ShopItemConfig> data = new TreeSet<>();
            // 遍历
            for (ShopItemConfig item : items) {
                // 如果满足等级限制
                if (ArrayUtils.isEmpty(item.getRefreshRequireGuildLv()) || (level >= item.getRefreshRequireGuildLv()[0] && level <= item.getRefreshRequireGuildLv()[1])) {
                    // 统计权重
                    w += item.getWeight();
                    // 添加结果
                    data.add(item);
                }
            }
            //
            GuildLevelRelateCache.put(level, result = Pair.valueOf(w, data));
        }
        // 返回
        return result;
    }
}
