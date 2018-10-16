package com.cellsgame.game.cmd.chat;

import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.depot.bo.DepotBO;
import com.cellsgame.game.module.depot.vo.DepotVO;
import com.cellsgame.game.module.goods.cache.CacheGoods;
import com.cellsgame.game.module.goods.csv.GoodsConfig;
import com.cellsgame.game.module.player.vo.PlayerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly @2016-12-02.
 */
public class AddTestGoosds extends AChatCMD {
    private static final Logger log = LoggerFactory.getLogger(AddTestGoosds.class);

    @Resource
    private DepotBO depotBO;

    @Override
    public Object getName() {
        return "testg";
    }

    @Override
    public Map execute(PlayerVO player, CMD cmd, String command, String[] parm) throws Exception {
        Map parent = GameUtil.createSimpleMap();
        DepotVO depotVO = player.getDepotVO();
        if (depotVO == null) {
            log.warn("背包怎么是空的？");
            return null;
        }
        Set<Map.Entry<Integer, GoodsConfig>> es = CacheGoods.getGoodsMap().entrySet();
        for (Map.Entry<Integer, GoodsConfig> e : es) {
            int cid = e.getKey();
            int num = 999;
            depotBO.changeGoodsNum(parent, player, cid, num, cmd);
            log.debug("添加物品  cid = {}, num = {}", cid, num);
        }
        return parent;
    }
}
