package com.cellsgame.game.module.friend;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.friend.bo.FriendsBO;
import com.cellsgame.game.module.friend.cons.CacheDisDataFriend;
import com.cellsgame.game.module.friend.csv.FriendBlessCfg;
import com.cellsgame.game.module.sys.funOpen.FunType;

/**
 * @author Aly @2017-02-07.
 */
public class ModuleInfo implements IModuleInfo<FriendsBO> {
    @Override
    public Module<FriendsBO> getModuleInfo() {
        return Module.createModule(FriendsBO.class)
                .jsonHead()
                .loadCSVFile()
                .loadDisData("DscrDataFriend.csv", CacheDisDataFriend.class)
                .load("FriendBless.csv", FriendBlessCfg.class)
                .adjustConfig()
                .regCusAdjuster(FriendBlessCfg.class, false,
                        cfgMap -> cfgMap.values().forEach(FriendBlessCfg::addCache))
                .checker()
                .listener()
                .onStartup(FriendsBO::load)
                .listenModuleOpen(FunType.FRIEND)
                .build();
    }
}
