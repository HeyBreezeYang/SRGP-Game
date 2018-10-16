package com.cellsgame.game.module.goods.bo.impl;

import java.util.List;
import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.cons.DepotType;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.goods.bo.GoodsBO;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.FuncGoodsConfig;
import com.cellsgame.game.module.goods.csv.ItemGoodsConfig;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.goods.evt.EvtTypeGoods;
import com.cellsgame.game.module.goods.msg.CodeGoods;
import com.cellsgame.game.module.goods.msg.MsgFactoryGoods;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.DBObj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *  道具业务类
 */

public class GoodsBOImpl implements GoodsBO{

    private static Logger logger = LoggerFactory.getLogger(GoodsBOImpl.class);



    @SuppressWarnings("unchecked")//Goods数据库对象转为值对象
    private void goodsDBObj2VO(PlayerVO player, Map data, DepotType depotType) {
        List<DBObj> objs = (List<DBObj>) data.get(depotType.getDataSign());
        if (objs != null) {
            Map<Integer, GoodsVO> vos = GameUtil.createSimpleMap();
            for (DBObj dbObj : objs) {
                GoodsVO goodsVO;
                try {
                    // 获取goodsVO 对象
                    goodsVO = depotType.getGvoClass().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("", e);
                    break;
                }
                // 从数据库读取
                goodsVO.readFromDBObj(dbObj);
                goodsVO.setPid(player.getId());
                // 将获取的道具唯一ID，放到集合里面，并与道具匹配
                vos.put(goodsVO.getGoodsIx(), goodsVO);
            }
            data.put(depotType.getDataSign(), vos);
        }
    }

    
	@Override
	public Map playerUseGoods(PlayerVO pvo, int gid, int num, CMD cmd) throws LogicException {
		  Map ret = useGoods(pvo, gid, num, null, cmd);
		  return MsgUtil.brmAll(ret, Command.Goods_UseGoods);
	}


    
    @Override   // 玩家带参数的使用道具
    public Map playerUseGoodsWithParam(PlayerVO pvo, int gid, int num, Integer[] chosen, CMD cmd) throws LogicException {
        Map ret = useGoods(pvo, gid, num, chosen, cmd);
        // 返回一个使用物品的消息
        return MsgUtil.brmAll(ret, Command.Goods_PlayerUseGoodsWithParam);
    }

	private Map useGoods(PlayerVO pvo, int gid, int num, Integer[] chosen, CMD cmd) {
		GoodsConfig goodsCfg = CacheGoods.getGoodsConfigById(gid);
        Map ret = GameUtil.createSimpleMap();
       
        if (null != goodsCfg && goodsCfg instanceof ItemGoodsConfig) {
            FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
            num = Math.abs(num);
            exec.acceptFunc(SyncFuncType.ChangeGoods.createFunc(new FuncParam(gid, -num)));
            Object[] chosenParam = new Object[0];
            if(chosen!=null){
            	chosenParam = new Object[chosen.length];
                for (int i = 0; i < chosenParam.length; i++) {
                	chosenParam[i] = chosen[i];
    			}
                chosen = null;
            }
            for (int i = 0; i < num; i++) {
                exec.addExecutor(((ItemGoodsConfig) goodsCfg).getFuncs(cmd),chosenParam);
            }
            Map prize = GameUtil.createSimpleMap();
            exec.exec(ret, prize, pvo);
            MsgFactoryGoods.instance().getGoodsPrizeMsg(ret, prize);
            EvtTypeGoods.USE_BOX.happen(ret, cmd, pvo, EvtParamType.GOODS_CID.val(gid), EvtParamType.NUM.val(num));
        }
        if (null != goodsCfg && goodsCfg instanceof FuncGoodsConfig) {
            FuncGoodsConfig funcGoodsConfig = (FuncGoodsConfig) goodsCfg;
            CodeGoods.Goods_Type_Error.throwIfTrue(funcGoodsConfig.getFuncType() != FuncGoodsConfig.FuncType_Player);
            FuncsExecutor executor = FuncsExecutorsType.Base.getExecutor(cmd);
            executor.addSyncFunc(new FuncConfig(SyncFuncType.ChangeGoods.getType(), gid, -num));
            executor.addSyncFunc(funcGoodsConfig.getFunc());
            Map prize = GameUtil.createSimpleMap();
            executor.exec(ret, prize, pvo, true, num);
            MsgFactoryGoods.instance().getGoodsPrizeMsg(ret, prize);
        }
		return ret;
	}


    @Override
    public FuncsExecutor addGoodsFuncs(FuncsExecutor<?> executor, PlayerVO pvo, int gid) {
        GoodsConfig goodsCfg = CacheGoods.getGoodsConfigById(gid);
        if (null != goodsCfg && goodsCfg instanceof ItemGoodsConfig) {
            if (((ItemGoodsConfig) goodsCfg).isAutoOpen()) {
                executor.addExecutor(((ItemGoodsConfig) goodsCfg).getFuncs(executor.getCmd()));
            } else {
                executor.addSyncFunc(SyncFuncType.ChangeGoods, gid, 1);
            }
        }
        return executor;
    }


    @Override
    public Map execGoodsFuncs(Map parent, Map prizeMap, PlayerVO pvo, int gid, CMD cmd) throws LogicException {
        GoodsConfig goodsCfg = CacheGoods.getGoodsConfigById(gid);
        if (goodsCfg != null && goodsCfg instanceof ItemGoodsConfig) {
            return execGoodsFuncs(parent, prizeMap, pvo, (ItemGoodsConfig) goodsCfg, cmd);
        }
        return parent;
    }
    @Override
    public Map<?, ?> execGoodsFuncs(Map<?, ?> parent, Map<?, ?> prizeMap, PlayerVO pvo, ItemGoodsConfig cfg, CMD cmd) throws LogicException {
        if (cfg == null)
            return parent;
        if (parent == null)
            parent = GameUtil.createSimpleMap();
        if (cfg.isAutoOpen()) {
            cfg.getFuncs(cmd).exec(parent, prizeMap, pvo);
        } else {
            FuncsExecutorsType.Base.getExecutor(cmd).addSyncFunc(SyncFuncType.ChangeGoods, cfg.getId(), 1).exec(parent, prizeMap, pvo);
        }
        return parent;
    }
    
    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map data) throws LogicException {
        for (DepotType depotType : DepotType.values()) {
            goodsDBObj2VO(player, data, depotType);
        }
    }


	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
	}



}
