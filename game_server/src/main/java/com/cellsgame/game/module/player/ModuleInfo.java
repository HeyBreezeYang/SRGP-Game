package com.cellsgame.game.module.player;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cons.CacheDisDataPlayer;
import com.cellsgame.game.module.player.csv.*;
import com.cellsgame.game.module.player.csv.adjust.*;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(PlayerBO.class)
                .jsonHead()
                .loadCSVFile()
                // 玩家
                .loadDisData("DscrDataPlayer.csv", CacheDisDataPlayer.class)
                .load("FixedExec.csv", FixedExecConfig.class)
                .load("PlayerLevel.csv", PlayerLevelConfig.class)
                .load("PlayerName.csv", PlayerNameConfig.class)
                .load("CheckInPrizes.csv", CheckInPrizeConfig.class)
                .load("LoginPrizes.csv", LoginPrizeConfig.class)
                .load("VipLevel.csv", VipLevelConfig.class)
                //
                .adjustConfig()
                .regCusAdjuster(new PlayerNameAdjuster())
                .regCusAdjuster(new PlayerLevelAdjuster())
                .regCusAdjuster(new LoginPrizeAdjuster())
                .regCusAdjuster(new CheckInPrizeAdjuster())
                .regCusAdjuster(new VipLevelAdjuster())
                .checker()
                .listener()
                .onStartup(PlayerBO::init)
                .onShutdown(PlayerBO::onShutDown)
                .build();
    }
}