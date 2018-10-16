package com.cellsgame.game.module.func.cons;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.csv.QuestCollectible;

@SuppressWarnings({"rawtypes", "unchecked", "WeakerAccess"})
public class PrizeConstant {

    public static final String SIGN_GOODS_LIST = "gl";
    public static final String SIGN_GOODS_ENTITY = "ge";
    public static final String SIGN_CURRENCY = "cur";
    public static final String SIGN_PLAYER_EXP = "pexp";
    public static final String SIGN_PLAYER_VIP_EXP = "pvexp";
    public static final String SIGN_PLAYER_Glory_EXP = "ggexp";
    public static final String SIGN_PLAYER_ACTIVENESS = "an";
    public static final String SIGN_PLAYER_ENERGY = "en";
    public static final String SIGN_IMG_2 = "img2";
    public static final String SIGN_GUILD_MNY = "gmny";
    public static final String SIGN_LIQUOR_CAPACITY = "lcap";
    public static final String SIGN_PLUNDER_INTEGRAL = "pldInt";
    public static final String SIGN_TRADE_INTEGRAL = "trdInt";
    public static final String SIGN_VX_FEELING = "feeling";
    public static final String PRIZE_VX_FEELING = "feeling";

    public static final String SIGN_WORKER_LIST = "wl";
    public static final String SIGN_WORKER_SKILLEXP = "wsexp";
    public static final String SIGN_WORKER_BOOKEXP = "wbexp";
    public static final String SIGN_WORKER_DRGG = "wdrug";
    public static final String PRIZE_WORKER_CID = "wcid";
    public static final String PRIZE_EXP = "exp";
    public static final String PRIZE_TYPE = "type";
    public static final String PRIZE_VALUE = "val";

    public static final String SIGN_BEAUTY_LIST = "bl";
    public static final String SIGN_BEAUTY_INTIMACY = "bitmcy";
    public static final String SIGN_BEAUTY_SKILLEXP = "bsexp";
    public static final String SIGN_BEAUTY_CHARM = "bchrm";
    public static final String PRIZE_BEAUTY_CID = "bcid";
    public static final String PRIZE_BEAUTY_INTIMACY = "itmcy";
    public static final String PRIZE_BEAUTY_CHARM = "chrm";
    public static final String SIGN_BOSS_SCORE = "bscore";


    public static Map<Integer, Integer> addGoods(Map prizeMap, int gid, int num) {
        return add2IntMap(prizeMap, SIGN_GOODS_LIST, gid, num);
    }
    
    public static Map<Integer, Integer> getGoods(Map prizeMap, int goodsType, int goodsId){
    	Map<Integer, Integer> goodsPrize = (Map<Integer, Integer>)prizeMap.get(SIGN_GOODS_LIST);
    	Map ret = GameUtil.createSimpleMap();
    	for(Entry<Integer, Integer> entry : goodsPrize.entrySet()){
    		int goodsCid = entry.getKey();
    		if(goodsId > 0 && goodsId != goodsCid) continue;
    		GoodsConfig config = CacheGoods.getGoodsMap().get(goodsCid);
    		if(config == null) continue;
    		if(goodsType > 0 && goodsType != config.getGoodsType()) continue;
    		ret.put(entry.getKey(), entry.getValue());
    	}
    	return ret;
    }

    public static Map<Integer, Integer> getCollectibleGoodsByID(Map prizeMap, int goodsId){
        Map<Integer, Integer> goodsPrize = (Map<Integer, Integer>)prizeMap.get(SIGN_GOODS_LIST);
        Map ret = GameUtil.createSimpleMap();
        for(Entry<Integer, Integer> entry : goodsPrize.entrySet()){
            int goodsCid = entry.getKey();
            if(goodsId > 0 && goodsId != goodsCid) continue;
            ret.put(entry.getKey(), entry.getValue());
        }
        return ret;
    }

    public static Map<Integer, Integer> getCollectibleGoodsByType(Map prizeMap, int collectibleType){
        Map<Integer, Integer> goodsPrize = (Map<Integer, Integer>)prizeMap.get(SIGN_GOODS_LIST);
        Map ret = GameUtil.createSimpleMap();
        for(Entry<Integer, Integer> entry : goodsPrize.entrySet()){
            int goodsCid = entry.getKey();
            QuestCollectible config = CacheGoods.getQuestCollectibleMap().get(goodsCid);
            if(config == null) continue;
            if(collectibleType > 0 && collectibleType != config.getCollectType()) continue;
            ret.put(entry.getKey(), entry.getValue());
        }
        return ret;
    }
    
    public static Map<Integer, Integer> getCur(Map prizeMap, int curType){
    	Map<Integer, Integer> curPrize = (Map<Integer, Integer>)prizeMap.get(SIGN_CURRENCY);
    	Map ret = GameUtil.createSimpleMap();
    	for(Entry<Integer, Integer> entry : curPrize.entrySet()){
    		int type = entry.getKey();
    		if(type != curType) continue;
    		ret.put(entry.getKey(), entry.getValue());
    	}
    	return ret;
    }


    public static Map<Integer, Integer> addGoods(Map prizeMap, Map<Integer, Integer> goods) {
        return add2IntMap(prizeMap, SIGN_GOODS_LIST, goods);
    }

    public static Map<Integer, Long> addCur(Map prizeMap, int curType, long num) {
        return add2LongMap(prizeMap, SIGN_CURRENCY, curType, num);
    }

    public static int addLiquorCapacity(Map prizeMap, int num) {
        return addIntValue(prizeMap, SIGN_LIQUOR_CAPACITY, num);
    }

    public static Map<Integer, Integer> addCur(Map prizeMap, Map<Integer, Integer> curs) {
        return add2IntMap(prizeMap, SIGN_CURRENCY, curs);
    }

    public static int addPlunderIntegral(Map prizeMap, int num) {
        return addIntValue(prizeMap, SIGN_PLUNDER_INTEGRAL, num);
    }

    public static int addTradeIntegral(Map prizeMap, int num) {
        return addIntValue(prizeMap, SIGN_TRADE_INTEGRAL, num);
    }

    public static Map addVxContactsFeeling(Map prizeMap, int beautyCid, int num) {
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_VX_FEELING);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_BEAUTY_CID, beautyCid);
        map.put(PRIZE_VX_FEELING, num);
        list.add(map);
        return prizeMap;
    }

    public static List<Map> addWorker(Map prizeMap, int wcid) {
        List<Map> tgtList = GameUtil.getOrCreateListInMap(prizeMap, SIGN_WORKER_LIST);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_WORKER_CID, wcid);
        tgtList.add(map);
        return tgtList;
    }

    public static List<Map> addBeauty(Map prizeMap, int bcid) {
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_BEAUTY_LIST);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_BEAUTY_CID, bcid);
        list.add(map);
        return list;
    }

    public static Map addBeautyIntimacy(Map prizeMap, int bcid, int num){
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_BEAUTY_INTIMACY);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_BEAUTY_CID, bcid);
        map.put(PRIZE_BEAUTY_INTIMACY, num);
        list.add(map);
        return prizeMap;
    }

    public static Map addBeautyCharm(Map prizeMap, int bcid, int num){
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_BEAUTY_CHARM);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_BEAUTY_CID, bcid);
        map.put(PRIZE_BEAUTY_CHARM, num);
        list.add(map);
        return prizeMap;
    }


    public static void addWorkerBookExp(Map prizeMap, int workerCid, int num) {
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_WORKER_BOOKEXP);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_WORKER_CID, workerCid);
        map.put(PRIZE_EXP, num);
        list.add(map);
    }

    public static void addWorkerDrugAtt(Map prizeMap, int workerCid, int attType, int num){
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_WORKER_DRGG);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_WORKER_CID, workerCid);
        map.put(PRIZE_TYPE, attType);
        map.put(PRIZE_VALUE, num);
        list.add(map);
    }


    public static void addWorkerDrugAtt(Map<?, ?> prizeMap, Integer workerCid, Map<Integer, Integer> values) {
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_WORKER_DRGG);
        for (Entry<Integer, Integer> entry : values.entrySet()) {
            int attType = entry.getKey();
            int value = entry.getValue();
            Map<String, Object> map = GameUtil.createSimpleMap();
            map.put(PRIZE_WORKER_CID, workerCid);
            map.put(PRIZE_TYPE, attType);
            map.put(PRIZE_VALUE, value);
            list.add(map);
        }
    }

    public static void addWorkerSkillExp(Map prizeMap, int workerCid, int num) {
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_WORKER_SKILLEXP);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_WORKER_CID, workerCid);
        map.put(PRIZE_EXP, num);
        list.add(map);
    }

    public static void addBeautySkillExp(Map prizeMap, int beautyCid, int num) {
        List<Map> list = GameUtil.getOrCreateListInMap(prizeMap, SIGN_BEAUTY_SKILLEXP);
        Map<String, Object> map = GameUtil.createSimpleMap();
        map.put(PRIZE_BEAUTY_CID, beautyCid);
        map.put(PRIZE_EXP, num);
        list.add(map);
    }

    public static List<Integer> addImg2(Map prizeMap, int img2) {
        return add2IntLst(prizeMap, SIGN_IMG_2, img2);
    }


    public static long addPlayerExp(Map prizeMap, long pExp) {
        return addLongValue(prizeMap, SIGN_PLAYER_EXP, pExp);
    }

    public static long addPlayerVipExp(Map prizeMap, long pExp) {
        return addLongValue(prizeMap, SIGN_PLAYER_VIP_EXP, pExp);
    }

    public static int addGuildGloryExp(Map prizeMap, int pExp) {
        return addIntValue(prizeMap, SIGN_PLAYER_Glory_EXP, pExp);
    }

    public static int addBossScorePrize(Map prizeMap, int score) {
        return addIntValue(prizeMap, SIGN_BOSS_SCORE, score);
    }

    public static int addPlayerActiveness(Map prizeMap, int activeness) {
        return addIntValue(prizeMap, SIGN_PLAYER_ACTIVENESS, activeness);
    }

    public static int addPlayerEnergy(Map prizeMap, int energy) {
        return addIntValue(prizeMap, SIGN_PLAYER_ENERGY, energy);
    }

    public static void addGuildMny(Map prizeMap, long gmny) {
        addLongValue(prizeMap, SIGN_GUILD_MNY, gmny);
    }


    private static int addIntValue(Map prizeMap, Object sign, int value) {
        Integer num = (Integer) prizeMap.get(sign);
        int ret;
        if (num == null)
            prizeMap.put(sign, ret = value);
        else
            prizeMap.put(sign, ret = value + num);
        return ret;
    }

    private static long addLongValue(Map prizeMap, Object sign, long value) {
        Long num = (Long) prizeMap.get(sign);
        long ret;
        if (num == null)
            prizeMap.put(sign, ret = value);
        else
            prizeMap.put(sign, ret = value + num);
        return ret;
    }


    private static Map<Integer, Integer> add2IntMap(Map prizeMap, Object sign, Integer key, Integer value) {
        Map<Integer, Integer> tgtMap = GameUtil.getOrCreateMapInMap(prizeMap, sign);
        Integer num = tgtMap.get(key);
        if (num == null)
            tgtMap.put(key, value);
        else
            tgtMap.put(key, value + num);
        return tgtMap;
    }


    private static Map<Integer, Integer> add2IntMap(Map prizeMap, Object sign, Map<Integer, Integer> dataMap) {
        Set<Entry<Integer, Integer>> es = dataMap.entrySet();
        for (Entry<Integer, Integer> e : es) {
            add2IntMap(prizeMap, sign, e.getKey(), e.getValue());
        }
        return GameUtil.getOrCreateMapInMap(prizeMap, sign);
    }

    private static Map<Integer, Long> add2LongMap(Map prizeMap, Object sign, Map<Integer, Long> dataMap) {
        Set<Entry<Integer, Long>> es = dataMap.entrySet();
        for (Entry<Integer, Long> e : es) {
            add2LongMap(prizeMap, sign, e.getKey(), e.getValue());
        }
        return GameUtil.getOrCreateMapInMap(prizeMap, sign);
    }

    private static Map<Integer, Long> add2LongMap(Map prizeMap, Object sign, Integer key, Long value) {
        Map<Integer, Long> tgtMap = GameUtil.getOrCreateMapInMap(prizeMap, sign);
        Long num = tgtMap.get(key);
        if (num == null)
            tgtMap.put(key, value);
        else
            tgtMap.put(key, value + num);
        return tgtMap;
    }

    private static List<Integer> add2IntLst(Map prizeMap, Object sign, Integer value) {
        List<Integer> tgtList = GameUtil.getOrCreateListInMap(prizeMap, sign);
        tgtList.add(value);
        return tgtList;
    }


    private static List<Integer> add2IntLst(Map prizeMap, Object sign, List<Integer> values) {
        List<Integer> tgtList = GameUtil.getOrCreateListInMap(prizeMap, sign);
        tgtList.addAll(values);
        return tgtList;
    }
}
