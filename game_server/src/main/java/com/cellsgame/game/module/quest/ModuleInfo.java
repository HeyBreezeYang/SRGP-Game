package com.cellsgame.game.module.quest;

import java.util.ArrayList;
import java.util.List;
import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.quest.bo.QuestBO;
import com.cellsgame.game.module.quest.csv.BehavCfg;
import com.cellsgame.game.module.quest.csv.CditCfg;
import com.cellsgame.game.module.quest.csv.QuestAchieveCfg;
import com.cellsgame.game.module.quest.csv.QuestDailyCfg;
import com.cellsgame.game.module.quest.csv.QuestMainCfg;
import com.cellsgame.game.module.quest.csv.adjust.QuestAchieveCfgAdjuster;
import com.cellsgame.game.module.quest.csv.adjust.QuestDailyCfgAdjuster;
import com.cellsgame.game.module.quest.csv.adjust.QuestMainCfgAdjuster;
import com.google.gson.reflect.TypeToken;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(QuestBO.class)
                .jsonHead()
                .regCusJsonType("_cdits", List.class, new TypeToken<ArrayList<CditCfg>>(){})
                .regCusJsonType("_behavs", List.class, new TypeToken<ArrayList<BehavCfg>>(){})
                
                .loadCSVFile()
                .load("QuestMain.csv", QuestMainCfg.class)
               	.load("QuestAchieve.csv", QuestAchieveCfg.class)
                .load("QuestDaily.csv", QuestDailyCfg.class)
                //
                .adjustConfig()
                .regCusAdjuster(new QuestMainCfgAdjuster())
                .regCusAdjuster(new QuestAchieveCfgAdjuster())
                .regCusAdjuster(new QuestDailyCfgAdjuster())

                .checker()
                .listener()
                
                .build();
    }
}