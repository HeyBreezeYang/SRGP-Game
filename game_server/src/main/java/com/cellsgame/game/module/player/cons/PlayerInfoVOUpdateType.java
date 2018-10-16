package com.cellsgame.game.module.player.cons;

import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Author: yangwei
 * <p/>
 * Email: ywengineer@gmail.com
 * <p/>
 * Date: 2015/9/10
 */
public enum PlayerInfoVOUpdateType {
    ALL {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
            for (PlayerInfoVOUpdateType type : values()) {
                if (ALL != type) {
                    type.update(pvo, info);
                }
            }
        }
    }, PLV {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
            int before = info.getPlv();
//            info.setPlv(pvo.getLevel());
            CachePlayerBase.EVENT_BUS.post(new CachePlayerBase.UpdateEvent(PLV, before, info.getPlv(), info));
        }
    }, IMAGE {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
//            info.setImage(pvo.getImage());
        }
    }, lastLogOutTime {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
            info.setLastLogOutTime(pvo.getLogoutDate());
        }
    }, lastLoginTime {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
            info.setLastLoginTime(pvo.getLoginDate());
        }
    }, fightForce {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
//            info.setFightForce(pvo.getFightForce());
        }
    }, PID {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
            info.setPid(pvo.getId());
        }
    }, SERVERID {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
            info.setPid(pvo.getId());
        }
    }, EXP {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
//            info.setServerId(pvo.getExp());
        }
    }, Name {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
            String before = info.getName();
            info.setName(pvo.getName());
            CachePlayerBase.EVENT_BUS.post(new CachePlayerBase.UpdateEvent(Name, before, info.getName(), info));
        }
    }, VIP {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
//            info.setVip(pvo.getVip());
        }
    }, TITLE_DEC {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
//            info.setTitleDec(pvo.getTitleDec());
        }
    }, GuildInfo {
        @Override
        public void update(PlayerVO pvo, PlayerInfoVO info) {
            GuildVO guildVO = pvo.getGuild();
            if (guildVO != null) {
                info.setGuildId(guildVO.getId());
                info.setGuildName(guildVO.getName());
            } else {
                info.setGuildId(-1);
                info.setGuildName("");
            }
        }
    };

    public abstract void update(PlayerVO pvo, PlayerInfoVO info);
}
