package com.cellsgame.game.module.func.impl.checker;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.IRecChecker;
import com.cellsgame.game.module.func.impl.checkrec.CheckRecGuildMny;
import com.cellsgame.game.module.guild.msg.CodeGuild;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class GuildMnyCostChecker extends CurTypeChecker implements IRecChecker<CheckRecGuildMny> {


    @Override
    public void check(PlayerVO player, FuncParam param) throws LogicException {
        if (player.getGuild() == null) return;
        CodeGuild.GuildMoneyMinus.throwIfTrue(player.getGuild().getMny() + param.getValue() < 0);
    }


    @Override
    public void checkRec(PlayerVO player, CheckRecGuildMny rec) throws LogicException {
        if (player.getGuild() == null) return;
        CodeGuild.GuildMoneyMinus.throwIfTrue(player.getGuild().getMny() + rec.getNum() < 0);
    }

}
