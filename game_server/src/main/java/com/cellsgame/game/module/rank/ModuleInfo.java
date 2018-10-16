package com.cellsgame.game.module.rank;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.rank.bo.RankBO;
import com.cellsgame.game.module.rank.cons.CacheDisRank;

/**
 * @author Aly on 2017-03-14.
 *         排行榜
 */
public class ModuleInfo implements IModuleInfo<RankBO> {
    @Override
    public Module<RankBO> getModuleInfo() {
        return Module.createModule(RankBO.class)
                .jsonHead()
                .loadCSVFile()
                .loadDisData("DscrDataRank.csv", CacheDisRank.class)
                .adjustConfig()
                .checker()
                .listener()
                .onStartup(RankBO::init)
                .onShutdown(RankBO::onShutDown)
                .build();
    }
}
