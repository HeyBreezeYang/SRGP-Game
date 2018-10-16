package com.cellsgame.game.cmd.console;

import java.util.List;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.StringUtil;
import com.cellsgame.common.util.cmd.console.AConsoleCmd;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.mail.MailFactory;
import com.cellsgame.game.module.mail.MailType;
import com.cellsgame.game.module.mail.cons.MailConstant;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddMail extends AConsoleCmd {
    private static final Logger log = LoggerFactory.getLogger(AddMail.class);

    @Override
    public Object getName() {
        return "addMail";
    }

    @Override
    public Object exe(String cmd, String[] param) throws Exception {
        Dispatch.dispatchGameLogic(() -> {
            String pname = param[1];
            Integer playerId = CachePlayerBase.getPIDByPname(pname);
            if (StringUtil.isEmpty(pname)) {
                log.warn("param is null....");
                return;
            }
            if (playerId == null) {
                log.warn("playerId is null....");
                return;
            }
            try {
                List<FuncConfig> prizes = GameUtil.createList();
                prizes.add(new FuncConfig(SyncFuncType.ChangeCur.getType(), CurrencyType.MONEY.getType(), 99));
                prizes.add(new FuncConfig(SyncFuncType.ChangeGoods.getType(), 30030016, 1));
                prizes.add(new FuncConfig(SyncFuncType.ChangeGoods.getType(), 30030017, 1));
                prizes.add(new FuncConfig(SyncFuncType.ChangeGoods.getType(), 30030018, 1));
                prizes.add(new FuncConfig(SyncFuncType.ChangeGoods.getType(), 30030019, 1));

                MailFactory.newMail(MailType.Guild, playerId, "公会邮件", "FightFightFightFightFight1")
                        .setSenderName(MailConstant.MAIL_SENDER_NAME_SYS)
                        .setAccessors(prizes)
                        .send();
            } catch (Throwable e) {
                log.error("", e);
            }
        });
        return null;
    }
}
