package com.cellsgame.game.module.func.impl.func;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.func.CheckRec;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IChecker;
import com.cellsgame.game.module.func.SyncFunc;
import com.cellsgame.game.module.func.cons.CheckRecType;
import com.cellsgame.game.module.func.cons.IRecCheckerType;
import com.cellsgame.game.module.func.cons.PrizeConstant;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecGuildMny;
import com.cellsgame.game.module.guild.bo.GuildBO;
import com.cellsgame.game.module.guild.msg.MsgFactoryGuild;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * User: 阚庆忠
 * Date: 2016/9/23 16:09
 * Desc:
 * FuncParam 参数：
 * param：      指定公会  小于等于0表示不指定
 * value：      资金变化量
 */
public class ChangeGuildMnyFunc extends SyncFunc {

    @Resource
    private GuildBO guildBO;

    @Override
    public Object exec(Map<?, ?> parent, Map<?, ?> prizeMap, CMD cmd, PlayerVO player, FuncParam param,int execNum) throws LogicException {
        GuildVO guildVO = checkAndGetGuildVO(player, param);
        if (guildVO == null) return parent;
        guildBO.changeGuildMny(cmd, parent, player, guildVO, param.getValue());
        if (prizeMap != null) {
            PrizeConstant.addGuildMny(prizeMap, param.getValue());
        }
        return parent;
    }

    private GuildVO checkAndGetGuildVO(PlayerVO player, FuncParam param) {
        GuildVO guildVO = player.getGuild();
        if (null != guildVO && (param.getParam() <= 0 || param.getParam() == guildVO.getId())) {
            // 限定参数 指定公会生效
            return guildVO;
        }
        return null;
    }

    @Override
    public Collection<CheckRec<?>> record(PlayerVO player, FuncParam param) {
        if (param.getValue() < 0) {
            List<CheckRec<?>> ret = GameUtil.createList();
            CheckRecGuildMny curRec = CheckRecType.GuildMny.getCheckRec();
            curRec.addNum(param.getValue());
            ret.add(curRec);
            return ret;
        }
        return Collections.emptyList();
    }


    @Override
    public IChecker getParamChecker() {
        return IRecCheckerType.GuildMnyCost.getChecker();
    }


}
