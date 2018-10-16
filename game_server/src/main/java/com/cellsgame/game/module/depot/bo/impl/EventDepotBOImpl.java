package com.cellsgame.game.module.depot.bo.impl;

import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.depot.cons.EventTypeDepot;
import com.cellsgame.game.module.depot.msg.CodeDepot;
import com.cellsgame.game.module.depot.vo.DepotVO;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.csv.QuestCollectible;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
/**
 * File Description.  背包事件业务实现类
 *
 * @author Yang
 */
public class EventDepotBOImpl implements DepotBO {
    private DepotBO depotBO;

    public EventDepotBOImpl(DepotBO depotBO) {
        this.depotBO = depotBO;
    }



    /**
     * 查看可堆叠物品数量
     *  <p>
     *      此方法根据收集类型编号
     *  </p>
     * @param playerVO 玩家数据
     * @param idOrCollectType  物品ID
     * @return 物品数量
     */

    //TODO
    @Override
    public int getGoodsQuantity(PlayerVO playerVO, int idOrCollectType) {
        return depotBO.getGoodsQuantity(playerVO, idOrCollectType);
    }

    /**
     * @param parent
     * @param pvo
     * @param cid
     * @param num
     * @param cmd
     * @return
     * @throws LogicException
     */
    @Override
    public Map changeGoodsNum(Map parent, PlayerVO pvo, int cid, int num, CMD cmd) throws LogicException {
        GoodsConfig goodsConfig = CacheGoods.getGoodsConfigById(cid);
        CodeDepot.Depot_Goods_Not_Found.throwIfTrue(goodsConfig == null);
        parent = depotBO.changeGoodsNum(parent, pvo, cid, num, cmd);
        // 物品变化
        return onGoodsChange(parent, pvo, goodsConfig, num, cmd);
    }

//    /**
//     * 玩家立即消耗物品组
//     *
//     * @param parent
//     * @param pvo
//     * @param goodsMap
//     */
//    @Override
//    public Map costGoodsNum(Map parent, PlayerVO pvo, Map goodsMap) throws LogicException {
//        return depotBO.costGoodsNum(parent, pvo, goodsMap);
//    }

    @Override
    public Map changeCurByType(Map parent, PlayerVO pvo, CurrencyType type, long cur, boolean save, boolean overMax, CMD cmd, boolean pay) throws LogicException {
        // 改变之前
        long before = getCurByType(pvo, type);
        // 调整货币
        Map result = depotBO.changeCurByType(parent, pvo, type, cur, save, overMax, cmd, pay);
        // 货币相关事件
        EventTypeDepot.Currency.happen(parent, cmd, pvo,
                EvtParamType.CURRENCY_TYPE.val(type.getType()),
                EvtParamType.BEFORE.val(before),
                EvtParamType.AFTER.val(before + cur),
                EvtParamType.CHANGE.val(cur),
                EvtParamType.PAY.val(pay));
        // 改变结果
        return result;
    }

    @Override
    public long getCurByType(PlayerVO pvo, CurrencyType type) {
        return depotBO.getCurByType(pvo, type);
    }

    /**
     * 查看货币数量
     *
     * @param pvo  玩家数据
     * @param type 货币类型
     * @return 货币数量
     */
    @Override
    public long getCurByType(PlayerVO pvo, int type) {
        return depotBO.getCurByType(pvo, type);
    }

    /**
     * 创建默认背包
     *
     * @param player
     * @param cmd
     */
    @Override
    public void createDefaultDepot(PlayerVO player, CMD cmd) {
        depotBO.createDefaultDepot(player, cmd);
    }

    @Override
    public void checkGoodsEnough(PlayerVO pvo, int gid, int num) throws LogicException {
        depotBO.checkGoodsEnough(pvo, gid, num);
    }

    @Override
    public void checkGoodsEnough(PlayerVO pvo, Map goodsMap) throws LogicException {
        depotBO.checkGoodsEnough(pvo, goodsMap);
    }

    @Override
    public GoodsVO getGoodsEntity(PlayerVO pvo, int gix) {
        return depotBO.getGoodsEntity(pvo, gix);
    }

    @Override
    public <T extends GoodsVO> T getGoodsEntity(PlayerVO pvo, Class<T> cls, int gix) {
        return depotBO.getGoodsEntity(pvo, cls, gix);
    }

    /**
     * 查看背包是否有足够的空间存放物品
     *
     * @param cls     物品类型
     * @param depotVO 背包
     * @param offset  偏移量
     * @return 是否还有足够空间
     * @throws LogicException
     */
    @Override
    public <T extends GoodsVO> boolean isCapacityEnough(Class<T> cls, DepotVO depotVO, int offset) throws LogicException {
        return depotBO.isCapacityEnough(cls, depotVO, offset);
    }

    @Override
    public boolean checkSpace(Class cls, DepotVO depotVO) throws LogicException {
        return depotBO.checkSpace(cls, depotVO);
    }

    /**
     * 添加不可堆叠物品
     *
     * @param parent 消息体
     * @param pvo    玩家数据
     * @param gvo    物品数量
     * @param cmd
     * @return 添加结果
     * @throws LogicException
     */
    @Override
    public Map addGoodsEntity(Map parent, PlayerVO pvo, GoodsVO gvo, CMD cmd) throws LogicException {
        //
        long before = getGoodsQuantity(pvo, gvo.getCid());
        // 先执行背包操作
        parent = depotBO.addGoodsEntity(parent, pvo, gvo, cmd);
        // after
        long after = getGoodsQuantity(pvo, gvo.getCid());
        // 如果有变化
        if (before != after) onGoodsChange(parent, pvo, gvo.getCfg(), (int) (after - before), cmd);
        //
        return parent;
    }

    /**
     * 删除背包物品数据，不会销毁物品数据
     * <p>
     * 具体数据的销毁必须通过 {@link DepotBO#removeGoodsEntity(Map, PlayerVO, GoodsVO, boolean, CMD)}, 否则将会造成不可堆叠物品计数异常
     * <p>
     * 物品数据类型:
     * <p>
     *
     * @param parent     消息
     * @param pvo        玩家数据
     * @param entityType 物品背包类型
     * @param gix        物品唯一ID
     * @return 被删除的物品数据
     * @throws LogicException
     */
    @Override
    public <T extends GoodsVO> T removeGoodsEntity(Map parent, PlayerVO pvo, Class<T> entityType, int gix) throws LogicException {
        // 先执行背包操作
        return depotBO.removeGoodsEntity(parent, pvo, entityType, gix);
    }

    /**
     * 只删除背包引用. 不删除物品数据
     * <p>
     * 具体数据的删除还依赖于各自模块
     *
     * @param parent       消息
     * @param pvo          玩家数据
     * @param gvo          物品数据
     * @param destroyGoods 是否需要修改计数器
     * @param cmd
     * @return 删除消息
     * @throws LogicException
     */
    @Override
    public Map removeGoodsEntity(Map parent, PlayerVO pvo, GoodsVO gvo, boolean destroyGoods, CMD cmd) {
        // 先执行背包操作
        parent = depotBO.removeGoodsEntity(parent, pvo, gvo, destroyGoods, cmd);
        // 如果需要计数, 则一定会有物品相关事件
        return destroyGoods ? onGoodsChange(parent, pvo, gvo.getCfg(), -1, cmd) : parent;
    }

    /**
     * 物品销毁处理。
     * <p>
     * 物品销毁是指物品的数据将会删除
     *
     * @param parent      消息
     * @param playerVO    玩家数据
     * @param goodsConfig 销毁的物品
     * @param changeDelta 变化的数量
     * @param cmd
     * @return 事件处理结果
     */
    private Map onGoodsChange(Map parent, PlayerVO playerVO, BaseCfg goodsConfig, int changeDelta, CMD cmd) {
        // 当前数量
        long after = getGoodsQuantity(playerVO, goodsConfig.getId());// 物品收集类型
        // 物品数量事件
        parent = EventTypeDepot.Goods.happen(parent, cmd, playerVO,
                EvtParamType.GOODS_CID.val(goodsConfig.getId()),
                EvtParamType.BEFORE.val(after - changeDelta),
                EvtParamType.AFTER.val(after),
                EvtParamType.CHANGE.val((long) changeDelta),
                EvtParamType.CONFIG.val(goodsConfig)
        );
        // 如果物品可收集
        if (goodsConfig instanceof QuestCollectible) {
            // 物品收集类型
            int collectType = ((QuestCollectible) goodsConfig).getCollectType();
            // 类型最终数量
            after = getGoodsQuantity(playerVO, collectType);
            // 物品收集类型事件
            parent = EventTypeDepot.GoodsCollectType.happen(parent, cmd, playerVO, EvtParamType.COLLECT_TYPE.val(collectType), EvtParamType.BEFORE.val(after - changeDelta), EvtParamType.AFTER.val(after));
        }
        //
        return parent;
    }

    @Override
    public void checkCurEnough(PlayerVO pvo, CurrencyType type, long cur) throws LogicException {
        depotBO.checkCurEnough(pvo, type, cur);
    }

    @Override
    public EvtType[] getListenTypes() {
        return depotBO.getListenTypes();
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        return depotBO.listen(parent, cmd, holder, event);
    }

    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        depotBO.buildAsLoad(cmd, player, data);
    }
    
	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		 depotBO.buildAsCreate(cmd, pvo);
	}
}
