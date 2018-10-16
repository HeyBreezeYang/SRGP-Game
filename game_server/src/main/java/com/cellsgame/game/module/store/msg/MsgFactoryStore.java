package com.cellsgame.game.module.store.msg;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.shop.vo.ShopItemVO;
import com.cellsgame.game.module.shop.vo.ShopVO;
import com.cellsgame.game.module.store.cache.CacheStore;
import com.cellsgame.game.module.store.vo.StoreItemRecordVO;
import com.cellsgame.game.module.store.vo.StoreItemVO;
import com.cellsgame.game.module.store.vo.StoreVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * File Description.
 *
 * @author Yang
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MsgFactoryStore extends MsgFactory {

    public static final String STORE = "store";
    public static final String STORE_LIST = "storeLst";
    public static final String STORE_UPDATE = "storeUpd";
    public static final String STORE_ADD = "storeAdd";
    public static final String STORE_DELETE = "storeDel";
    public static final String STORE_SOLD = "sold";
    public static final String STORE_ITEMS = "items";

    private static final MsgFactoryStore instance = new MsgFactoryStore();

    public static MsgFactoryStore instance() {
        return instance;
    }

    @Override
    public String getModulePrefix() {
        return STORE;
    }


    public Map<String, Object> getStoreInfoMsg(Map parent) {
        parent = creIfNull(parent);
        Map info = gocInfoMap(parent);
        List<Map> itemUpdate = gocLstIn(info, STORE_LIST);
        for (StoreVO storeVO : CacheStore.getAll()) {
            itemUpdate.add(getStoreInfo(storeVO));
        }
        return parent;
    }

    public Map<String, Object> getStoreUpdateMsg(Map parent, StoreVO storeVO) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        List<Map> itemUpdate = gocLstIn(op, STORE_UPDATE);
        itemUpdate.add(getStoreInfo(storeVO));
        return parent;
    }

    public Map<String, Object> getStoreAddMsg(Map parent, StoreVO storeVO) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        List<Map> itemUpdate = gocLstIn(op, STORE_ADD);
        itemUpdate.add(getStoreInfo(storeVO));
        return parent;
    }

    public Map<String, Object> getStoreDeleteMsg(Map parent, StoreVO storeVO) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        List<Integer> itemUpdate = gocLstIn(op, STORE_DELETE);
        itemUpdate.add(storeVO.getId());
        return parent;
    }

    private static final String START_TIME = "strTime";
    private static final String END_TIME = "endTime";

    private Map getStoreInfo(StoreVO storeVO) {
        Map result = GameUtil.createSimpleMap();
        result.put(ID, storeVO.getId());
        result.put(TYPE, storeVO.getType());
        result.put(START_TIME, storeVO.getStartTime());
        result.put(END_TIME, storeVO.getEndTime());
        return result;
    }

    public Map getStoreItemsMsg(Map parent, PlayerVO playerVO, StoreVO storeVO) {
//        Map<Integer, StoreItemRecordVO> recordVOMap = playerVO.getStoreItemRecordVOMap().row(storeVO.getId());
        Map<Integer, StoreItemRecordVO> recordVOMap =null;
        parent = creIfNull(parent);
        Map info = gocInfoMap(parent);
        List<Map> itemUpdate = gocLstIn(info, STORE_ITEMS);
        for (StoreItemVO itemVO : storeVO.getItemVOs()) {
            Map itemMap = getStoreItemMsg(itemVO);
            if(recordVOMap != null && recordVOMap.containsKey(itemVO.getCid())){
                StoreItemRecordVO recordVO = recordVOMap.get(itemVO.getCid());
                itemMap.put(STORE_SOLD, recordVO.getSold());
            }else{
                itemMap.put(STORE_SOLD, 0);
            }
            itemUpdate.add(itemMap);
        }
        return parent;
    }

    private Map getStoreItemMsg(StoreItemVO itemVO) {
        Map result = GameUtil.createSimpleMap();
        result.put(INDEX, itemVO.getIndex());
        result.put(CID, itemVO.getCid());
        return result;
    }

    public Map getStoreFullMsg(Map parent, Collection<StoreVO> all) {
        List<Map> list = gocLstIn(parent, STORE_LIST);
        for (StoreVO storeVO : all) {
            Map m = getStoreInfo(storeVO);
            List<Map> itemUpdate = gocLstIn(m, STORE_ITEMS);
            for (StoreItemVO itemVO : storeVO.getItemVOs()) {
                Map itemMap = getStoreItemMsg(itemVO);
                itemUpdate.add(itemMap);
            }
            list.add(m);
        }
        return parent;
    }
}