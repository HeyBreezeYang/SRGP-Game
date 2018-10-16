package com.cellsgame.game.module.func.impl.func;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.func.cons.CheckRecType;
import com.cellsgame.game.module.func.cons.IRecCheckerType;
import com.cellsgame.game.module.func.cons.PrizeConstant;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecGoods;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.Table;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ChangeGoodsFunc extends SyncFunc implements WeightValueFunc {

    @Resource
    private DepotBO depotBO;


    @Override
    public Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        int cid = param.getParam();
        int num = 0;
        if(param.getValue() < 0){
            num = (int)param.getValue();
        }else {
            num = (int) getValue(param, execNum);
        }
        if (cid > 0) {
            parent = changeGoods(parent, player, cid, num, cmd);
        }
        if (param.getValue() > 0 && prizeMap != null)
            PrizeConstant.addGoods(prizeMap, param.getParam(), num);
        return parent;
    }


    private Map<?, ?> changeGoods(Map<?, ?> parent, PlayerVO player, int cid, int num, CMD cmd) throws LogicException {
        return depotBO.changeGoodsNum(parent, player, cid, num, cmd);
    }


    @Override
    public Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) {
        List<CheckRec<?>> ret = GameUtil.createList();
        CheckRecGoods rec = null;
        if (param.getValue() < 0) {
            rec = CheckRecType.Goods.getCheckRec();
            rec.add(param.getParam(), (int)Math.abs(param.getValue()));
        }
//		if (param.getExtraParams() != null && param.getExtraParams().size() > 0) {
//			Set<Entry<Object, Object>> es =  param.getExtraParams().entrySet();
//			for (Entry<Object, Object> e : es) {
//				Integer cid = (Integer) e.getKey();
//				Integer num = (Integer) e.getValue();
//				if(num<0){
//					if(rec == null)
//						rec =  CheckRecType.Goods.getCheckRec();
//					rec.add(cid,Math.abs(num));
//				}
//			}
//		}
        if (ret != null)
            ret.add(rec);
        return ret;
    }


    @Override
    public IChecker getParamChecker() {
        return IRecCheckerType.GoodsCost.getChecker();
    }

    @Override
    public String checkCfg(FuncParam config, Table<Class<? extends BaseCfg>, Integer, BaseCfg> allCfgData) {
        int cid = config.getParam();
        GoodsConfig goodsConfig = CacheGoods.getGoodsConfigById(cid);

        return goodsConfig != null ? null : "不存在的物品:" + cid;
    }


}
