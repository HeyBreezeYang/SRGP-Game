package com.cellsgame.game.module.store.bo.impl;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;
import com.cellsgame.game.module.shop.csv.SimpleShopItemConfig;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.store.StoreUtil;
import com.cellsgame.game.module.store.bo.StoreBO;
import com.cellsgame.game.module.store.cache.CacheStore;
import com.cellsgame.game.module.store.msg.CodeStore;
import com.cellsgame.game.module.store.msg.MsgFactoryStore;
import com.cellsgame.game.module.store.vo.StoreItemRecordVO;
import com.cellsgame.game.module.store.vo.StoreItemVO;
import com.cellsgame.game.module.store.vo.StoreVO;
import com.cellsgame.game.module.sys.msg.CodeSystem;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Resource;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class StoreBOImpl implements StoreBO, IBuildData {

    @Resource
    private BaseDAO<StoreVO> storeDAO;

    @Resource
    private BaseDAO<StoreItemRecordVO> storeItemRecordDAO;


    @Override
    public void init() {
        List<DBObj> dbObjs = storeDAO.getAll();
        if(dbObjs != null){
            for (DBObj dbObj : dbObjs) {
                StoreVO storeVO = new StoreVO();
                storeVO.readFromDBObj(dbObj);
                CacheStore.cache(storeVO);
                int before = storeVO.getItemVOs().size();
                // 查看商店所有商品
                storeVO.getItemVOs().removeIf(new Predicate<StoreItemVO>() {
                    @Override
                    public boolean test(StoreItemVO itemVO) {
                        return itemVO.getCfg() == null;
                    }
                });
                if (before != storeVO.getItemVOs().size()) storeDAO.save(storeVO);
                refStore(storeVO);
            }
        }
    }

    private void refStore(StoreVO storeVO) {
        // 如果没有刷新时间,不刷新
        if (ArrayUtils.isEmpty(storeVO.getRefreshHours())) return;
        // 当前日期
        Clock system = Clock.systemDefaultZone();
        // 未来时间直接返回false
        if (storeVO.getRefreshTime() > system.millis())
            return;
        LocalDateTime current = LocalDateTime.now(system);
        // 上一次刷新日期
        LocalDateTime last = LocalDateTime.ofInstant(Instant.ofEpochMilli(storeVO.getRefreshTime()), ZoneId.systemDefault());
        int compareTo = current.toLocalDate().compareTo(last.toLocalDate());
        if (compareTo > 0) {
            // 当前时间超过了刷新时间 直接刷新
            if(current.getHour() >= storeVO.getRefreshHours()[0] ||
                    // 如果刷新时间间隔越过1天  直接刷新
                    current.minusDays(1).toLocalDate().isAfter(last.toLocalDate()) ||
                    //是昨天
                    last.getHour() < storeVO.getRefreshHours()[storeVO.getRefreshHours().length - 1]){
                addStoreVersion(storeVO);
            }
            // 如果是同一天
        } else if (compareTo == 0) {
            // 相等 同一天
            // 下一个刷新时间点
            int nextRefreshTime = -1;
            // 检测所有时间点是否已刷新
            for (int time : storeVO.getRefreshHours()) {
                // 刷新时间
                LocalTime localTime = LocalTime.of(time, 0, 0, 0);
                // 查找下一个刷新时间点
                if (last.toLocalTime().isBefore(localTime)) {
                    // 下个刷新点
                    nextRefreshTime = time;
                    // 查找结束
                    break;
                }
            }
            // 如果有下一个刷新时间点
            if(nextRefreshTime > -1 && current.getHour() >= nextRefreshTime){
                addStoreVersion(storeVO);
            }
        }
    }

    private void addStoreVersion(StoreVO storeVO) {
        // 更新当前版本号
        storeVO.setVersion(storeVO.getVersion() + 1);
        // 保存商店数据
        storeDAO.save(storeVO);
    }

    @Override
    public Map getAllStore() throws LogicException {
        Map result = GameUtil.createSimpleMap();
        result = MsgFactoryStore.instance().getStoreFullMsg(result, CacheStore.getAll());
        return result;
    }

    @Override
    public ICode createStore(Map params) throws LogicException {
        System.out.println("params : " + params);
        StoreVO storeVO = StoreUtil.trans(params);
        storeDAO.save(storeVO);
        CacheStore.cache(storeVO);
        Map result = GameUtil.createSimpleMap();
        result = MsgFactoryStore.instance().getStoreAddMsg(result,storeVO);
        broadcastMsg(MsgUtil.brmAll(result, Command.Store_AddStore));
        return CodeSystem.Suc;
    }

    private void broadcastMsg(Map<String, Object> result) {
        for (PlayerVO playerVO : CachePlayer.getOnlinePlayers()) {
            Push.multiThreadSend(playerVO, result);
        }
    }

    @Override
    public ICode updateStore(Map params) throws LogicException {
        StoreVO storeVO = StoreUtil.trans(params);
        StoreVO oldStoreVO = CacheStore.get(storeVO.getId());
        if(oldStoreVO == null)
            return CodeStore.NotFindStore;
        oldStoreVO.setType(storeVO.getType());
        oldStoreVO.setRefreshHours(storeVO.getRefreshHours());
        oldStoreVO.setItemVOs(storeVO.getItemVOs());
        storeDAO.save(oldStoreVO);
        Map result = GameUtil.createSimpleMap();
        result = MsgFactoryStore.instance().getStoreUpdateMsg(result,oldStoreVO);
        broadcastMsg(MsgUtil.brmAll(result, Command.Store_UpdateStore));
        return CodeSystem.Suc;
    }

    @Override
    public ICode deleteStore(int storeId) throws LogicException {
        StoreVO oldStoreVO = CacheStore.get(storeId);
        if(oldStoreVO == null)
            return CodeStore.NotFindStore;
        CacheStore.remove(storeId);
        storeDAO.delete(oldStoreVO);
        Map result = GameUtil.createSimpleMap();
        result = MsgFactoryStore.instance().getStoreDeleteMsg(result,oldStoreVO);
        broadcastMsg(MsgUtil.brmAll(result, Command.Store_DeleteStore));
        return CodeSystem.Suc;
    }

    @Override
    public Map<?, ?> open(PlayerVO player, int storeId, CMD cmd) throws LogicException {
        StoreVO storeVO = CacheStore.get(storeId);
        CodeStore.NotFindStore.throwIfTrue(storeVO == null);
        Map result = GameUtil.createSimpleMap();
//        Map<Integer, StoreItemRecordVO> recordVOMap = player.getStoreItemRecordVOMap().row(storeId);
        Map<Integer, StoreItemRecordVO> recordVOMap=null;
        if(recordVOMap != null){
            for (StoreItemRecordVO recordVO : recordVOMap.values()) {
                if(recordVO.getVersion() != storeVO.getVersion()){
                    resetStoreItemRecord(storeVO, recordVO);
                }
            }
        }
        result = MsgFactoryStore.instance().getStoreItemsMsg(result, player, storeVO);
        return MsgUtil.brmAll(result, cmd);
    }

    @Override
    public Map<?, ?> buy(PlayerVO player, int storeId, int index, int quantity, CMD cmd) throws LogicException {
        quantity = Math.max(1, quantity);
        StoreVO storeVO = CacheStore.get(storeId);
        CodeStore.NotFindStore.throwIfTrue(storeVO == null);
        long currTime = System.currentTimeMillis();
        CodeStore.NotOpen.throwIfTrue(storeVO.getStartTime() > currTime);
        CodeStore.NotOpen.throwIfTrue(storeVO.getEndTime() < currTime);
        CodeStore.ItemIndexError.throwIfTrue(index <= 0 || index > storeVO.getItemVOs().size());
        // 需要购买的商品
        StoreItemVO itemVO = storeVO.getItemVOs().get(index - 1);
        // 检测商品是否达到限购
        checkLimit(player, storeVO, itemVO, quantity);
        // 商品配置数据
        ShopItemConfig shopItemConfig = itemVO.getCfg();
        // 检测物品是否可以添加成功
        FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
        buildBuyCost(player, exec, shopItemConfig, quantity);
        exec.selectAndCheck(player); // 檢測消耗
        exec.addSyncFunc(shopItemConfig.getPurchase(), quantity);
        // 奖励消息数据
        Map prizeMap = GameUtil.createSimpleMap();
        Map result = GameUtil.createSimpleMap();
        // 购买及消耗结果
        exec.exec(result, prizeMap, player);
        recordBuyQuantity(player, storeVO, shopItemConfig, quantity);
        result = MsgFactoryStore.instance().getPrizeMsg(result, prizeMap);
        result = MsgFactoryStore.instance().getStoreItemsMsg(result, player, storeVO);
        return MsgUtil.brmAll(result, cmd);
    }

    private void buildBuyCost(PlayerVO player, FuncsExecutor exec, SimpleShopItemConfig shopItemConfig, int quantity) {
//        List<FuncConfig> costs = shopItemConfig.getCosts();
//        if(!costs.isEmpty()) {
//            // 查看个人限购数据
//            StoreItemRecordVO recordVO = player.getStoreItemRecordVOMap().get(player.getId(), shopItemConfig.getId());
//            int buyTimes = recordVO != null ? recordVO.getSold() : 0;
//            for (int i = 1; i <= quantity; i++) {
//                if (buyTimes + i >= costs.size()) {
//                    exec.addSyncFunc(costs.get(costs.size() - 1));
//                }else{
//                    exec.addSyncFunc(costs.get(buyTimes + i));
//                }
//            }
//        }
    }

    @Override
    public void systemRef() {
        long hour = DateUtil.getCurrentHour();
        for (StoreVO storeVO : CacheStore.getAll()) {
            if (ArrayUtils.isEmpty(storeVO.getRefreshHours())) continue;
            for (int refHour : storeVO.getRefreshHours()) {
                if(hour == refHour){
                    addStoreVersion(storeVO);
                }
            }
        }
    }

    private void recordBuyQuantity(PlayerVO playerVO, StoreVO storeVO, ShopItemConfig shopItemConfig, int quantity){
//        StoreItemRecordVO recordVO = playerVO.getStoreItemRecordVOMap().get(storeVO.getId(), shopItemConfig.getId());
//        // 如果不存在记录
//        if (recordVO == null)
//            playerVO.getStoreItemRecordVOMap().put(storeVO.getId(), shopItemConfig.getId(), recordVO = new StoreItemRecordVO());
//        //配置档ID
//        recordVO.setCid(shopItemConfig.getId());
//        recordVO.setStoreId(storeVO.getId());
//        // 商店所属玩家ID(当记录不属于任何玩家时,表示此记录为全服限购)
//        recordVO.setPlayer(playerVO.getId());
//        // 已售数量
//        recordVO.setSold(recordVO.getSold() + quantity);
//        recordVO.setVersion(storeVO.getVersion());
//        //
//        storeItemRecordDAO.save(recordVO);
    }

    private void checkLimit(PlayerVO playerVO, StoreVO storeVO, StoreItemVO itemVO, int quantity) throws LogicException {
        // 商品配置数据
        ShopItemConfig shopItemConfig = itemVO.getCfg();
        // 等级
//        CodeStore.ReqLevel.throwIfTrue(playerVO.getLevel() < shopItemConfig.getVisibleLv() || playerVO.getLevel() < shopItemConfig.getPurchaseLv());
        // VIP
//        CodeStore.ReqVip.throwIfTrue(playerVO.getVip() < shopItemConfig.getPurchaseVip());
        //获取商品限购数量
        int limit = getShopItemLimit(playerVO, shopItemConfig);
        // 查看个人限购数据
//        StoreItemRecordVO recordVO = playerVO.getStoreItemRecordVOMap().get(storeVO.getId(), shopItemConfig.getId());
        // 如果达到限购
//        CodeStore.Limit.throwIfTrue(recordVO != null && limit >= 0 && recordVO.getSold() + quantity > limit);
    }

    private int getShopItemLimit(PlayerVO playerVO, ShopItemConfig shopItemConfig) {
        int[] limit = shopItemConfig.getLimit();
        if(limit == null || limit.length <= 0) return -1;
//        if(limit.length <= playerVO.getVip()){
//            return limit[limit.length - 1];
//        }else{
//            return limit[playerVO.getVip()];
//        }
        return  0;
    }


    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        // 玩家所有商店数据
        List<DBObj> shopDB = (List<DBObj>) data.get(DATA_SIGN_STORE_ITEM_RECORD);
        if (shopDB == null)
            return;
        //
        for (DBObj dbObj : shopDB) {
            // 限购数据
            StoreItemRecordVO recordVO = new StoreItemRecordVO();
            // DB TO OBJECT
            recordVO.readFromDBObj(dbObj);

            StoreVO storeVO = CacheStore.get(recordVO.getStoreId());
            if(storeVO != null) {
                // 限购数据所属性玩家
                recordVO.setPlayer(player.getId());
                // 加入玩家数据
//                player.getStoreItemRecordVOMap().put(recordVO.getStoreId(), recordVO.getCid(), recordVO);
                if(recordVO.getVersion() != storeVO.getVersion()){
                    resetStoreItemRecord(storeVO, recordVO);
                }
            }else{
                storeItemRecordDAO.delete(recordVO);
            }
        }
    }

    private void resetStoreItemRecord(StoreVO storeVO, StoreItemRecordVO recordVO) {
        recordVO.setSold(0);
        recordVO.setVersion(storeVO.getVersion());
        storeItemRecordDAO.save(recordVO);

    }

    @Override
    public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {

    }
}
