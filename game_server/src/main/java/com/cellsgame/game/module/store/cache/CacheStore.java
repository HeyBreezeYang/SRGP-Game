package com.cellsgame.game.module.store.cache;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.store.vo.StoreVO;

import java.util.Collection;
import java.util.Map;

public class CacheStore {

    private static final Map<Integer, StoreVO> Stores = GameUtil.createSimpleMap();

    public static void cache(StoreVO shopVO){
        Stores.put(shopVO.getId(), shopVO);
    }

    public static StoreVO get(int id){
        return Stores.get(id);
    }

    public static Collection<StoreVO> getAll(){
        return Stores.values();
    }

    public static void remove(int storeId) {
        Stores.remove(storeId);
    }
}
