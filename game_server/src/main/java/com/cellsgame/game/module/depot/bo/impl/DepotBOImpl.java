package com.cellsgame.game.module.depot.bo.impl;

import java.util.*;
import java.util.Map.Entry;
import javax.annotation.Resource;
import com.cellsgame.common.util.DateUtil;
import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.context.DefaultPlayerConfig;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.depot.cons.DepotType;
import com.cellsgame.game.module.depot.msg.CodeDepot;
import com.cellsgame.game.module.depot.msg.MsgFactoryDepot;
import com.cellsgame.game.module.depot.vo.DepotGoodsRefVO;
import com.cellsgame.game.module.depot.vo.DepotVO;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.cons.GoodsConstant;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DepotBOImpl implements DepotBO {
    private static final Logger log = LoggerFactory.getLogger(DepotBOImpl.class);
    /**
     * 背包仓库数据操作类 业务对象BO
     */
    @Resource
    private BaseDAO<DepotVO> depotDAO;

    private DefaultPlayerConfig playerConfig;

    //DepotBO depotBO = new DepotBOImpl();

     //TODO  我们的刷新仓库界面功能
    //加载仓库等所有东西  data 从数据库中读取的数据
    private void loadGoodsVO(Map<String, ?> data, DepotVO depotVO, DepotType depotType) {
        // 在加载物品的时候重新处理数据    获取当前仓库类型码(从数据库中读取的)，对应的所有道具
        Map<Integer, GoodsVO> goodsVOMap = (Map<Integer,GoodsVO>) data.get(depotType.getDataSign());
        // 符文背包数据
        if (goodsVOMap == null) return;
        //通过仓库类型id  识别道具，然后通过道具get(Key) 来获取仓库道具的引用集合
        DepotGoodsRefVO refVO = depotVO.getGoodsRef().get(depotType.getDepotTypeCode());
        if (refVO != null) {
            List<Integer> goodsRefLst = refVO.getGoodsRefs();
            Table<DepotType, Integer, GoodsVO> goodsEntities = depotVO.getGoodsEntities();
            for (Integer ref : goodsRefLst) {
                // 数据库仓库中 存放了很多道具，有的有了引用，有的没有，遍历当前获取的仓库后，发现如果有了引用,则删除这个，留下没有引用的集合。
                GoodsVO goodsVO = goodsVOMap.remove(ref);
                if (goodsVO == null) continue;
                // 背包物品计数器
                depotVO.count(goodsVO, 1);
                // 将没有引用的物品加入对应类型列表
                if (goodsEntities.put(depotType, goodsVO.getGoodsIx(), goodsVO) != null) {
                    log.debug("[RuneDepot] Duplicate Goods IX : {}, type : {}：", goodsVO.getGoodsIx(), depotType);
                }
            }
        }
        if (goodsVOMap.size() > 0) {
            for (GoodsVO goodsVO : goodsVOMap.values()) {
                if (null == goodsVO) continue;
                // 背包物品计数器
                depotVO.count(goodsVO, 1);
                Table<DepotType, Integer, GoodsVO> notInDepots = depotVO.getNotInDepotGoods();
                if (null == notInDepots)
                    depotVO.setNotInDepotGoods(notInDepots = HashBasedTable.create());
                if (notInDepots.put(depotType, goodsVO.getGoodsIx(), goodsVO) != null) {
                    log.debug("[RuneDepot] Duplicate Goods IX : {}, type : {} ", goodsVO.getGoodsIx(), depotType);
                }
            }
        }
    }

    @Override //查看玩家某ID道具的数量
    public int getGoodsQuantity(PlayerVO playerVO, int idOrCollectType) {
        DepotVO depVO = playerVO.getDepotVO();
        return depVO.getCount(idOrCollectType);
    }


    @Override  // 检查使用的数量mun 小于可堆叠道具的数量currNum ，抛出仓库道具数量不足的状态码
    public void checkGoodsEnough(PlayerVO pvo, int gid, int num) throws LogicException {
        Integer currNum = getGoodsQuantity(pvo, gid);
        CodeDepot.Depot_GoodsNumNotEnough.throwIfTrue(currNum < Math.abs(num));
    }

    @Override // 检查所有道具是否足够 ，不够抛出状态码 ，调用上面的 checkGoodsEnough
    public void checkGoodsEnough(PlayerVO pvo, Map goodsMap) throws LogicException {
        Set<Entry<Object, Object>> es = goodsMap.entrySet();
        for (Entry<Object, Object> e : es) {
            checkGoodsEnough(pvo, (Integer) e.getKey(), (Integer) e.getValue());
        }
    }

    @Override // 从服务器缓存的道具列表 改变道具数量  int num有正负，+ 为增加 -为减少；
    public Map changeGoodsNum(Map parent, PlayerVO pvo, int cid, int num, CMD cmd) throws LogicException {
        GoodsConfig goodsConfig = CacheGoods.getGoodsConfigById(cid);
        DepotVO depVO = pvo.getDepotVO();
        // ID  数量  直接序列化给gCounts
        Map<Integer, Integer> gCounts = depVO.getGoodsCounts();
        // 从cid 获取当前id 道具的数量
        Integer currNum = gCounts.get(cid);
        if (num < 0) {//如果num<0 就是减少道具数量，取绝对值和拥有的数量currNum 对比 ，不够则抛出不够的异常状态码
            CodeDepot.Depot_GoodsNumNotEnough.throwIfTrue(currNum == null || currNum < Math.abs(num));
        }else {
            //如果当前道具数量currNum为空 则 num（此时num为+） 赋值给currNum（增加道具数量）否则 currNum+num 赋值给currNum
            currNum = currNum == null ? num : currNum + num;
            gCounts.put(cid, currNum);
            depVO.count(goodsConfig, num);
            depotDAO.save(depVO);
        }
        //返回改变的消息 parent
        return MsgFactoryDepot.instance().getDepotGoodsCountsUpdateMsg(parent, cid, currNum);
    }

//    @Override
//    public Map costGoodsNum(Map parent, PlayerVO pvo, Map goodsMap) throws LogicException {
//        checkGoodsEnough(pvo, goodsMap);
//        Set<Entry<Object, Object>> es = goodsMap.entrySet();
//        for (Entry<Object, Object> e : es)
//            parent = costGoodsNum(parent, pvo, (Integer) e.getKey(), (Integer) e.getValue());
//        return parent;
//    }


    @Override // 检查货币是否足够
    public void checkCurEnough(PlayerVO pvo, CurrencyType type, long cur) throws LogicException {
        CodeDepot.Depot_Cny_Type_Error.throwIfTrue(type == null);
        DepotVO depotVO = pvo.getDepotVO();
        //当Map集合中有这个type时，就使用这个type值，如果没有就使用默认值defaultValue
        Long currCur = depotVO.getCurrencyMap().getOrDefault(type.getType(),0L);
        type.getErrorCode().throwIfTrue(currCur < cur);
    }

    @Override
    public long getCurByType(PlayerVO pvo, CurrencyType type) {
        return getCurByType(pvo, type.getType());
    }

    /**
     * 根据货币类型查看货币数量
     *
     * @param pvo  玩家数据
     * @param type 货币类型
     * @return 货币数量
     */
    @Override
    public long getCurByType(PlayerVO pvo, int type) {
        DepotVO depotVO = pvo.getDepotVO();
        return depotVO.getCurrencyMap().getOrDefault(type, 0L);
    }

    @Override // 根据货币类型更改货币
    public Map changeCurByType(Map parent, PlayerVO pvo, CurrencyType type, long cur, boolean save, boolean overMax, CMD cmd, boolean pay) throws LogicException {
        DepotVO depotVO = pvo.getDepotVO();
        if (changeCurByType(depotVO, type, cur, save, overMax)){
        	 return MsgFactoryDepot.instance().getDepotCurUpdateMsg(parent, depotVO, type.getType());
        }
        return parent;
    }

    private boolean changeCurByType(DepotVO depotVO, CurrencyType type, long cur, boolean save, boolean overMax) throws LogicException {
        if (cur == 0) return false;
        Map<Integer, Long> currencyMap = depotVO.getCurrencyMap();
        Long currCny = currencyMap.getOrDefault(type.getType(),0L);
        long res = currCny + cur;
        type.getErrorCode().throwIfTrue(res < 0 && cur < 0);
        if (cur > 0 && type.haveUpperLmt() && !overMax)
            res = Math.min(type.getUpperLmt(), res);
        currencyMap.put(type.getType(), res);
        type.afterAdd(depotVO.getPid(), res - currCny);
        if (save) depotDAO.save(depotVO);
        return true;
    }
    @Override  // 获取某个ID道具不可堆叠物品
    public GoodsVO getGoodsEntity(PlayerVO pvo, int gix) {
        DepotVO depotVO = pvo.getDepotVO();
        Map<DepotType, GoodsVO> entity = depotVO.getGoodsEntities().column(gix);
        //
        return (CollectionUtils.isEmpty(entity)) ? null : entity.values().iterator().next();
    }

    @Override //获取所有不可堆叠的道具集合
    public <T extends GoodsVO> T getGoodsEntity(PlayerVO pvo, Class<T> cls, int gix) {
        DepotVO depotVO = pvo.getDepotVO();
        return (T) depotVO.getGoodsEntities().get(Enums.get(DepotType.class, cls), gix);
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
        DepotVO depotVO = pvo.getDepotVO();
        if (!checkSpace(gvo.getClass(), depotVO))
            return parent;
        DepotType depotType = Enums.get(DepotType.class, gvo.getClass());
        Map<Integer, GoodsVO> voMap = depotVO.getGoodsEntities().row(depotType);
        CodeDepot.Depot_Already_In_Depot.throwIfTrue(voMap.containsKey(gvo.getGoodsIx()));
         //将GoodVO 索引index 放到voMap中，作为gvo 的key ；
        voMap.put(gvo.getGoodsIx(), gvo);
        // 如果当前物品的index比历史最大index大，则物品是新增
        if (gvo.getGoodsIx() > depotVO.getMaxGoodsIx()) depotVO.count(gvo, 1);
        // 记录最大物品索引 index
        depotVO.setMaxGoodsIx(Math.max(gvo.getGoodsIx(), depotVO.getMaxGoodsIx()));
        // 保存背包数据
        depotDAO.save(depotVO);
        //从仓库信息工厂返回一个不可堆叠物品更新成功的信息
        return MsgFactoryDepot.instance().getDepotGoodsEntityUpdateMsg(parent, gvo);
    }


     @Override
    public boolean checkSpace(Class cls, DepotVO depotVO) {
        // 从Enums枚举缓存中获取仓库类型的缓存类 给仓库类型类  cls:物品类型
        DepotType depotType = Enums.get(DepotType.class, cls);
        Map<Integer, GoodsVO> voMap = depotVO.getGoodsEntities().row(depotType);
        return voMap.size() < depotType.getCapacity();
    }

    /**
     * 查看背包是否有足够的空间存放物品
     *
     * @param cls     物品类型
     * @param depotVO 背包
     * @param offset  偏移量 (要放的空间)
     * @return 是否还有足够空间
     */
    @Override
    public <T extends GoodsVO> boolean isCapacityEnough(Class<T> cls, DepotVO depotVO, int offset) {
        DepotType depotType = Enums.get(DepotType.class, cls);
        //从table中取出 道具的ID和道具对象，depotTpye 为背包类型码
        Map<Integer, GoodsVO> voMap = depotVO.getGoodsEntities().row(depotType);
        //  当前已有的容量+ 需要新增的容量是否小于等于整个背包容量
        return voMap.size() + offset <= depotType.getCapacity();
    }

    /**
     * 删除背包物品数据，不会销毁物品数据
     * <p>
     * 具体数据的销毁必须通过 {@link DepotBO#removeGoodsEntity(Map, PlayerVO, GoodsVO, boolean, CMD)}, 否则将会造成不可堆叠物品计数异常
     * <p>
     * 物品数据类型:
     * <p>
     * 符文：{@link }<br/>
     * 宝石：{@link }<br/>
     * 魔晶：{@link }<br/>
     *
     * @param parent     消息
     * @param pvo        玩家数据
     * @param entityType 物品背包类型
     * @param gix        物品唯一ID
     * @return 被删除的物品数据
     */
    @Override
    public <T extends GoodsVO> T removeGoodsEntity(Map parent, PlayerVO pvo, Class<T> entityType, int gix) throws LogicException {
        DepotVO depotVO = pvo.getDepotVO();
        DepotType depotType = Enums.get(DepotType.class, entityType);
        //通过 depotType和gix道具唯一id 删除道具（双Key 定位删除）
        GoodsVO remove = depotVO.getGoodsEntities().remove(depotType, gix);
        //删除道具之后 作为道具值对象。判定道具数量是否足够（如果道具不存在了仍然删除的情况）
        CodeDepot.Depot_GoodsNumNotEnough.throwIfTrue(null == remove);
        depotDAO.save(depotVO);
        MsgFactoryDepot.instance().getDepotGoodsEntityDeleteMsg(parent, remove);
        return (T) remove;
    }

    /**
     * 删除物品数据
     * <p>
     * 是否需要删除数据，由参数{needToCount}决定
     *
     * @param parent       消息
     * @param pvo          玩家数据
     * @param gvo          物品数据
     * @param destroyGoods 是否需要修改计数器
     * @param cmd  客户端对象
     * @return 删除消息
     */
    @Override
    public Map removeGoodsEntity(Map parent, PlayerVO pvo, GoodsVO gvo, boolean destroyGoods, CMD cmd) {
        DepotVO depotVO = pvo.getDepotVO();
        //调用上面的删除道具方法
        GoodsVO remove = this.removeGoodsEntity(parent, pvo, gvo.getClass(), gvo.getGoodsIx());
        // 如果需要删除
        if (destroyGoods) {
            // 记数器
            depotVO.count(remove, -1);
            // 删除 物品
            doDestroyGoods(gvo);
        }
        // 保存到数据库
        depotDAO.save(depotVO);
        return parent;
    }

    private void doDestroyGoods(GoodsVO goodsVO) {
        DepotType depotType = Enums.get(DepotType.class, goodsVO.getClass());
        switch (depotType) {
          	default:
                break;
        }
    }



    private void saveAll(PlayerVO playerVO) {
        depotDAO.save(playerVO.getDepotVO());
    }

    private void enterGame(PlayerVO pvo) {
        enterGameRevCurrcnyData(pvo);
    }

    @Override
    public void createDefaultDepot(PlayerVO player, CMD cmd) {
        DepotVO depotVO = player.getDepotVO();
        if (depotVO != null)
            return;
        depotVO = new DepotVO();
        depotVO.setPid(player.getId());
        player.setDepotVO(depotVO);
        depotDAO.save(depotVO);
        if (null != playerConfig && playerConfig.getDefaultPrize() != null) {
            FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
            exec.addSyncFunc(playerConfig.getDefaultPrize());
            try {
                exec.exec(GameUtil.createSimpleMap(), player);
            } catch (LogicException e) {
                log.info("创建默认物品错误  code:{}:", e.getCode());
            }
        }
    }

    public void setDefaultPlayerConfig(DefaultPlayerConfig defaultPlayerConfig) {
        playerConfig = defaultPlayerConfig;
    }
   

    private Map onlinePlayerRevCurrency(Map parent, PlayerVO pvo, CMD cmd, CurrencyType type, int refType, long time, int num, int maxNum) {
        DepotVO depotVO = pvo.getDepotVO();
        Map<Integer, Long> refDates = depotVO.getRefDates();
        try {
            long currCny = getCurByType(pvo, type);
            if (currCny >= maxNum)
                return parent;
            parent = changeCurByType(parent, pvo, type, num, true, false, cmd, false);
        } catch (LogicException e) {
            log.warn("", e);
        }
        refDates.put(refType, time);
        depotDAO.save(depotVO);
        return parent;
    }

    private void refDepotCurrcnyData(PlayerVO pvo, CurrencyType cnyType, int refType, int maxNum, int revCd, int revNum){
        DepotVO depotVO = pvo.getDepotVO();
        Map<Integer, Long> refDates = depotVO.getRefDates();
        if (refDates == null || refDates.isEmpty()) return;
        Long refTime = refDates.get(refType);
        if (refTime == null || refTime <= 0)
            return;
        long currCny = getCurByType(pvo, cnyType);
        if (currCny >= maxNum)
            return;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(refTime);
        int refMinute = c.get(Calendar.MINUTE);
        int v = refMinute % revCd;
        int minutes = DateUtil.getMinutes(refTime);
        minutes = minutes + v;
        //计算出产生效果的时间
        v = minutes / revCd;
        int revAp = v * revNum;
        if (currCny + revAp >= maxNum) revAp = (int) (maxNum - currCny);
        try {
            if (revAp > 0)
                changeCurByType(pvo.getDepotVO(), cnyType, revAp, true, false);
        } catch (LogicException e1) {
            log.warn("", e1);
        }
        refDates.put(refType, System.currentTimeMillis());
        depotDAO.save(depotVO);
    }
     // ?
    private void enterGameRevCurrcnyData(PlayerVO pvo) {}

    
    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[]{EvtTypePlayer.EnterGame, EvtTypePlayer.Offline};
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        Enum<?> o = event.getType();
        if (o instanceof EvtTypePlayer) {
            switch ((EvtTypePlayer) o) {
                case EnterGame:
                    enterGame(((PlayerVO) holder));
                    break;
                case Offline:
                    saveAll(((PlayerVO) holder));
                    break;
            }

        }
        return parent;
    }
    
    /**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override  // 创建环境的时候创建
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
		 createDefaultDepot(pvo, cmd);
	}
    
    @Override // 加载环境的时候创建
    public void buildAsLoad(CMD cmd, PlayerVO pvo, Map<String, ?> datas) throws LogicException {
        List<DBObj> dbObjs = (List<DBObj>) datas.get(DATA_SIGN_DEPOT);
        if (dbObjs != null && dbObjs.size() > 0) {
        	DepotVO depotVO = new DepotVO();
        	pvo.setDepotVO(depotVO);
            depotVO.readFromDBObj(dbObjs.get(0));
            depotVO.setPid(pvo.getId());
            // 初始化背包时，需要将计数物品加入计数器
            for (Entry<Integer, Integer> entry : depotVO.getGoodsCounts().entrySet())
                depotVO.count(CacheGoods.getGoodsConfigById(entry.getKey()), entry.getValue());
            //加载引用背包
            for (DepotType depotType : DepotType.values()) {
                loadGoodsVO(datas, depotVO, depotType);
            }
        } else {
        	createDefaultDepot(pvo, cmd);
        }
    }
}
