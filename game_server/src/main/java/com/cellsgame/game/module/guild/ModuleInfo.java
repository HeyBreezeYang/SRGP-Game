package com.cellsgame.game.module.guild;

import java.util.Map;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.guild.bo.GuildBO;
import com.cellsgame.game.module.guild.cache.CacheDisGuild;
import com.cellsgame.game.module.guild.cons.GuildBossState;
import com.cellsgame.game.module.guild.csv.*;
import com.cellsgame.game.module.sys.funOpen.FunType;
import com.cellsgame.game.util.DebugTool;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(GuildBO.class)
                .jsonHead()
                .loadCSVFile()
                .load("GuildLV.csv", GuildLVCfg.class)
                .loadDisData("DscrDataGuild.csv", CacheDisGuild.class)
//                .load("GuildDonate.csv", DonateConfig.class)
                .load("GuildBoss.csv", GuildBossConfig.class)

                .adjustConfig()
                .regCusAdjuster(GuildLVCfg.class, false, (Map<Integer, GuildLVCfg> map) -> {
                    for (GuildLVCfg cfg : map.values()) {
                        int lv = cfg.getLv();
                        GuildLVCfg put = GuildLVCfg.GUILD_LV_CFG_MAP.put(lv, cfg);
                        if (null != put) {
                            DebugTool.throwException(" 家族配置错误: 重复的家族等级  value:-->" + lv, null);
                        }
                    }
                })
                .checker()
                .listener()
                .onStartup(GuildBO::init)
                .listenModuleOpen(FunType.GUILD)
                .build();
    }
}