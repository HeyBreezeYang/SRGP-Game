package com.cellsgame.game.module.activity.csv.adjuster;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.core.cfg.core.ConfigCache;
import com.cellsgame.game.core.cfg.core.proc.CfgAdjuster;
import com.cellsgame.game.module.activity.csv.ActivityRankPrizeConfig;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ActRankPrizeAdjuster implements CfgAdjuster<ActivityRankPrizeConfig> {
    @Override
    public boolean needAdd2Cache() {
        return false;
    }

    @Override
    public Class<ActivityRankPrizeConfig> getProType() {
        return ActivityRankPrizeConfig.class;
    }

    @Override
    public void doAdjustData(ConfigCache allCfg, Map<Integer, ActivityRankPrizeConfig> map) {

        for (Map.Entry<Integer, ActivityRankPrizeConfig> entry : map.entrySet()) {
            ActivityRankPrizeConfig config = entry.getValue();
            List<ActivityRankPrizeConfig> configs = ActivityRankPrizeConfig.configs.get(config.getGroupId());
            if(configs == null){
                ActivityRankPrizeConfig.configs.put(config.getGroupId(), configs = GameUtil.createList());
            }
            configs.add(config);
        }

        for (Map.Entry<Integer, List<ActivityRankPrizeConfig>> entry : ActivityRankPrizeConfig.configs.entrySet()) {
            List<ActivityRankPrizeConfig> configs = entry.getValue();
            configs.sort(new Comparator<ActivityRankPrizeConfig>() {
                @Override
                public int compare(ActivityRankPrizeConfig o1, ActivityRankPrizeConfig o2) {
                    return o1.getRank() - o2.getRank();
                }
            });
        }
    }
}
