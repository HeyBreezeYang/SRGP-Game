package com.cellsgame.game.module.depot.msg;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.depot.cons.DepotType;
import com.cellsgame.game.module.depot.vo.DepotVO;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class MsgFactoryDepot extends MsgFactory{
	public static final String DEPOT = "dpt";
    public static final String DEPOT_CURRENCY = "cur";
    public static final String DEPOT_GOODS_ID = "gid";
    public static final String DEPOT_GOODS_LST = "gl";            // 物品列表
    public static final String DEPOT_GOODS_ENTITY = "ge";     // 物品列表

    private static final MsgFactoryDepot instance  = new MsgFactoryDepot();

    public static MsgFactoryDepot instance(){
    	return instance;
    }

	@Override
	public String getModulePrefix() {
		return DEPOT;
	}


	public  Map getDepotInfoMsg(Map parent, PlayerVO pvo) {
        parent = creIfNull(parent);
        DepotVO depotVO = pvo.getDepotVO();
        Map dptInfo = gocInfoMap(parent);
        Map<Integer, Object> cnyInfo = gocMapIn(dptInfo, DEPOT_CURRENCY);
        for (Entry<Integer, Long> cnyEntry : depotVO.getCurrencyMap().entrySet()) {
            cnyInfo.put(cnyEntry.getKey(), cnyEntry.getValue().toString());
        }
        List<Map> goodsList = gocLstIn(dptInfo, DEPOT_GOODS_LST);
        for (Entry<Integer, Integer> entry : depotVO.getGoodsCounts().entrySet()) {
            Map<String, Object> goodsInfo = GameUtil.createSimpleMap();
            goodsInfo.put(DEPOT_GOODS_ID, entry.getKey());
            goodsInfo.put(NUM, entry.getValue());
            goodsList.add(goodsInfo);
        }
        Map<Integer, List<Map>> goodsEntities = gocMapIn(dptInfo, DEPOT_GOODS_ENTITY);
        Set<Entry<DepotType, Map<Integer, GoodsVO>>> gees = depotVO.getGoodsEntities().rowMap().entrySet();
        for (Entry<DepotType, Map<Integer, GoodsVO>> gee : gees) {
            DepotType depotType = gee.getKey();
            List<Map> infoMapLst = GameUtil.createList();
            Collection<GoodsVO> gvos = gee.getValue().values();
            for (GoodsVO gvo : gvos) {
                infoMapLst.add(gvo.toInfoMap());
            }
            goodsEntities.put(depotType.getDepotTypeCode(), infoMapLst);
        }
        return parent;
    }

    public  Map<?, ?> getDepotGoodsCountsUpdateMsg(Map parent, int gid, int num) {
        Map<Integer, Integer> goodsMap = GameUtil.createSimpleMap();
        goodsMap.put(gid, num);
        return getDepotGoodsCountsUpdateMsg(parent, goodsMap);
    }

    /**
     * 获取物品更新操作封装
     *
     * @param goodsMap Map里的内容为 <cid,num>
     */
    private Map<?, ?> getDepotGoodsCountsUpdateMsg(Map parent, Map<Integer, Integer> goodsMap) {
        parent = creIfNull(parent);
        Map depotOp = gocOpMap(parent);
        List<Map> updateList = gocLstIn(depotOp, DEPOT_GOODS_LST);
        for (Entry<Integer, Integer> entry : goodsMap.entrySet()) {
            Map<String, Object> opMap = GameUtil.createSimpleMap();
            opMap.put(DEPOT_GOODS_ID, entry.getKey());
            opMap.put(NUM, entry.getValue());
            updateList.add(opMap);
        }
        return parent;
    }

    public  Map getDepotCurUpdateMsg(Map parent, DepotVO depotVO, int type) {
    	parent = creIfNull(parent);
        Map depotOp = gocOpMap(parent);
        Map<Integer, Object> cnyOp = gocMapIn(depotOp, DEPOT_CURRENCY);
        Long currValue = depotVO.getCurrencyMap().getOrDefault(type,0L);
        if (currValue == null) currValue = 0L;
        cnyOp.put(type, currValue);
        return parent;
    }


    public  Map getDepotGoodsEntityUpdateMsg(Map parent, GoodsVO gvo) {
    	parent = creIfNull(parent);
    	DepotType depotType = Enums.get(DepotType.class, gvo.getClass());
        if(depotType == null)
        	return parent;
        Set<Integer> deleteByDepotType = gocGoodsEntitiesDeleteMsg(parent, depotType);
        if(deleteByDepotType.contains(gvo.getGoodsIx()))
        	return parent;
        Map<Integer, Map> updateByDepotType = gocGoodsEntitiesUpdateMsg(parent, depotType);
        updateByDepotType.put(gvo.getGoodsIx(), gvo.toInfoMap());
        return parent;
    }


    public  Map getDepotGoodsEntityUpdateMsg(Map parent, List<GoodsVO> gvos) {
    	parent = creIfNull(parent);
    	for (GoodsVO gvo : gvos) {
    		DepotType depotType = Enums.get(DepotType.class, gvo.getClass());
            if(depotType == null)
            	continue;
            Set<Integer> deleteByDepotType = gocGoodsEntitiesDeleteMsg(parent, depotType);
            if(deleteByDepotType.contains(gvo.getGoodsIx()))
            	continue;
            Map<Integer, Map> updateByDepotType = gocGoodsEntitiesUpdateMsg(parent, depotType);
            updateByDepotType.put(gvo.getGoodsIx(), gvo.toInfoMap());
		}
        return parent;
    }

    public  Map getDepotGoodsEntityDeleteMsg(Map parent, GoodsVO gvo) {
    	parent = creIfNull(parent);
    	DepotType depotType = Enums.get(DepotType.class, gvo.getClass());
        if(depotType == null)
        	return parent;
        Map<Integer, Map> updateByDepotType = gocGoodsEntitiesUpdateMsg(parent, depotType);
        updateByDepotType.remove(gvo.getGoodsIx());
        Set<Integer> deleteByDepotType = gocGoodsEntitiesDeleteMsg(parent, depotType);
        deleteByDepotType.add(gvo.getGoodsIx());
        return parent;
    }


    public  Map getDepotGoodsEntitiesDeleteMsg(Map parent, List<GoodsVO> gvos) {
    	parent = creIfNull(parent);
    	for (GoodsVO gvo : gvos) {
    		DepotType depotType = Enums.get(DepotType.class, gvo.getClass());
            if(depotType == null)
            	continue;
            Map<Integer, Map> updateByDepotType = gocGoodsEntitiesUpdateMsg(parent, depotType);
            updateByDepotType.remove(gvo.getGoodsIx());
            Set<Integer> deleteByDepotType = gocGoodsEntitiesDeleteMsg(parent, depotType);
            deleteByDepotType.add(gvo.getGoodsIx());
		}
        return parent;
    }


    private Map<Integer, Map> gocGoodsEntitiesUpdateMsg(Map parent, DepotType depotType) {
        Map depotOp = gocOpMap(parent);
        Map<String, Object> ge = gocMapIn(depotOp, DEPOT_GOODS_ENTITY);
        Map<Integer,Map> update  =	gocMapIn(ge, UPDATE);
        return gocMapIn(update, depotType.getDepotTypeCode());
    }


    private Set<Integer> gocGoodsEntitiesDeleteMsg(Map parent, DepotType depotType) {
        Map depotOp = gocOpMap(parent);
        Map<String, Object> ge = gocMapIn(depotOp, DEPOT_GOODS_ENTITY);
        Map<Integer,Set<Integer>> delete  =	gocMapIn(ge, DELETE);
        return gocSetIn(delete, depotType.getDepotTypeCode());
    }


//    public  Map getDepotGoodsEntitiesUpdateMsg(Map parent, DepotVO depotVO, DepotType depotType) {
//    	parent = creIfNull(parent);
//        Map depotOp = gocOpMap(parent);
//        Map<Integer, List<Map>> updateInfos = gocMapIn(depotOp, DEPOT_GOODS_ENTITY);
//        List<Map> infoMapLst = updateInfos.get(depotType.getDepotTypeCode());
//        if (infoMapLst == null)
//            infoMapLst = GameUtil.createList();
//        else
//            infoMapLst.clear();
//        updateInfos.put(depotType.getDepotTypeCode(), infoMapLst);
//        Set<Entry<Integer, GoodsVO>> es = depotVO.getGoodsEntities().row(depotType).entrySet();
//        for (Entry<Integer, GoodsVO> e : es) {
//            infoMapLst.add(e.getValue().toInfoMap());
//        }
//        return parent;
//    }



}
