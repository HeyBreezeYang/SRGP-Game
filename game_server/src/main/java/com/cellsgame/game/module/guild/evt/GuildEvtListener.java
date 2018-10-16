package com.cellsgame.game.module.guild.evt;

import java.util.Map;
import javax.annotation.Resource;

import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.guild.bo.GuildBO;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.vo.GuildMemberVO;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;

/**
 * @author Aly on 2017-03-23.
 */
public class GuildEvtListener implements StaticEvtListener {
    @Resource
    private BaseDAO<PlayerVO> playerDAO;
    private GuildBO guildBO;

    @Override
    public EvtType[] getListenTypes() {
        return new EvtType[]{EvtTypePlayer.EnterGame};
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        PlayerVO player = ((PlayerVO) holder);
        GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(player.getId());
        if (null != memberVO) {
            if (memberVO.getGuildID() >= 0) {
                GuildVO guildVO = CacheGuild.getGuildByID(memberVO.getGuildID());
                if (null == guildVO || !guildVO.getMemberRights().containsKey(memberVO.getPid())) {
                    memberVO.setGuildID(-1);
                }
            }
            if (memberVO.getGuildID() != player.getGuildID()) {
                player.setGuildID(memberVO.getGuildID());
                playerDAO.save(player);
            }
        } else {
            player.setGuildID(-1);
            playerDAO.save(player);
        }
        return parent;
    }

    public void setGuildBO(GuildBO guildBO) {
        this.guildBO = guildBO;
    }
}
