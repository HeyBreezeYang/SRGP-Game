	package com.cellsgame.game.module.depot.cons;

import javax.annotation.Resource;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.module.depot.msg.CodeDepot;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.msg.CodeGuild;
import com.cellsgame.game.module.guild.vo.GuildMemberVO;
import com.cellsgame.game.module.player.msg.CodePlayer;
import com.cellsgame.orm.BaseDAO;

/**
 * User: 阚庆忠
 * Date: 2016/9/22 15:45
 * Desc:
 */
public enum CurrencyType {
    /**
     *
     */
    NULL(0, CodeGeneral.General_InvokeParamError),
    /**
     * 金币
     */
    MONEY(1, CodePlayer.Player_MoneyMinus),
    /**
     * 元宝
     */
    TRE(2, CodeDepot.Depot_Treasure_Minus),
    /**
     * 人脉
     */
    CONTACTS(3, CodeDepot.Depot_Contacts_Minus),
    /**
     * 机会
     */
    CHANCE(4, CodeDepot.Depot_Chance_Minus),
    /**
     * 工会贡献
     */
    GUILD_COIN(5, CodeGuild.GuildCoinMinus) {
        @Resource
        BaseDAO<GuildMemberVO> guildMemberDAO;

        @Override
        public void afterAdd(int pid, long delta) {
            if (delta > 0) {
                GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(pid);
                if(memberVO == null){
                    return;
                }
                memberVO.setHistoryGuildCoin(memberVO.getHistoryGuildCoin() + delta);
                guildMemberDAO.save(memberVO);
            }
        }
    },
    SOCIETY_EXP(7, CodeDepot.Depot_Society_Exp_Minus),

    ;
    public static final int typeFix = 80400000;
    int type;
    ICode errorCode;

    CurrencyType(int id, ICode errorCode) {
        this.type = typeFix + id;
        this.errorCode = errorCode;
        SpringBeanFactory.autowireBean(this);
    }


    public int getType() {
        return type;
    }

    public ICode getErrorCode() {
        return errorCode;
    }

    public boolean haveUpperLmt() {
        return getUpperLmt() >= 0;
    }

    public long getUpperLmt() {
        return - 1;
    }

    public void afterAdd(int pid, long delta) {

    }
}
