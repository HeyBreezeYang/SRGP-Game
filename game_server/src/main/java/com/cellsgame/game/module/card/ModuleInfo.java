package com.cellsgame.game.module.card;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.card.bo.CardBO;
import com.cellsgame.game.module.card.csv.CardConfig;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(CardBO.class)
                .jsonHead()
                .loadCSVFile()
                // 玩家
                .load("Card.csv", CardConfig.class)
                //
                .adjustConfig()
                .checker()
                .listener()
                .build();
    }
}