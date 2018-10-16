package com.cellsgame.game.module.goods.cache;

import java.util.Map;

import com.cellsgame.game.module.goods.csv.*;
import io.netty.util.collection.IntObjectHashMap;

/**
 * User: 阚庆忠
 * Date: 2016/9/18 17:22
 * Desc:
 */
public class CacheGoods {

    private static final Map<Integer, GoodsConfig> goodsMap = new IntObjectHashMap<>();

    private static final Map<Integer, ItemGoodsConfig> itemGoodsMap = new IntObjectHashMap<>();

    private static final Map<Integer, QuestCollectible> QuestCollectibleMap = new IntObjectHashMap<>();

    public static Map<Integer, GoodsConfig> getGoodsMap() {
        return goodsMap;
    }

    public static Map<Integer, ItemGoodsConfig> getItemGoodsMap() {
        return itemGoodsMap;
    }

    public static GoodsConfig getGoodsConfigById(int gid) {
        return goodsMap.get(gid);
    }

    public static ItemGoodsConfig getItemConfigById(int id) {
        return itemGoodsMap.get(id);
    }

    public static Map<Integer, QuestCollectible> getQuestCollectibleMap() {
        return QuestCollectibleMap;
    }
}
