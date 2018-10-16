package com.cellsgame.game.module.log;

import java.util.Map;

import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

public abstract class LogProcess {

    protected static final String Cmd = "cmd";
    protected static final String Shop_Id = "spid";
    protected static final String CID = "cid";
    protected static final String PLAYER_ID = "pid";
    protected static final String PLAYER_NAME = "pname";
    protected static final String GUILD_ID = "gid";
    protected static final String GUILD_NAME = "gname";
    protected static final String Shop_TYPE = "spType";
    protected static final String ATTS = "atts";
    protected static final String TYPE = "type";
    protected static final String VALUE = "value";
    protected static final String Date = "dt";
    protected static final String PARAMS = "pms";
    protected static final String CHANNEL = "ptf";

    public Map<String, Object> getJsonInfo(CMD cmd, EvtHolder holder, GameEvent e) {
        Map<String, Object> ret = builderInfo(holder, e);
        cmd = cmd.now();
        ret.put(CHANNEL, 1006);
        ret.put(Cmd, cmd.getCmd());
        if(holder != null) {
            if (holder instanceof PlayerVO) {
                PlayerVO player = (PlayerVO) holder;
                ret.put(PLAYER_ID, player.getId());
                ret.put(PLAYER_NAME, player.getName());
            }
            if (holder instanceof GuildVO) {
                GuildVO guildVO = (GuildVO) holder;
                ret.put(GUILD_ID, guildVO.getId());
                ret.put(GUILD_NAME, guildVO.getName());
            }
        }
        ret.put(Date, cmd.getTime());
        return ret;
    }

    protected abstract Map<String, Object> builderInfo(EvtHolder holder, GameEvent e);

}
