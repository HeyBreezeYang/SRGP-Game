package com.cellsgame.game.module.chat;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.chat.cache.CacheDisDataChat;
import com.cellsgame.game.module.guild.bo.GuildBO;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(ChatBO.class)
                .jsonHead()
                .loadCSVFile()
                .loadDisData("DscrDataChat.csv", CacheDisDataChat.class)
                .adjustConfig()
                .checker()
                .listener()
                .onStartup(ChatBO::init)
                .build();
    }
}