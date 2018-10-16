package com.cellsgame.game.core.event;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.activity.cons.SysType;
import com.cellsgame.game.module.activity.vo.ActivityVO;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.guild.cons.RightLevel;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.mail.vo.MailVO;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.csv.ShopType;

import java.util.Collection;
import java.util.Map;

/**
 * @author Aly on  2016-10-26.
 */
public class EvtParamType<T> {
    public static final EvtParamType<Integer> PID = valueOf("pid");
    public static final EvtParamType<Integer> CID = valueOf("cid");
    public static final EvtParamType<Integer> HERO_ID = valueOf("hid");
    public static final EvtParamType<Integer> HERO_CID = valueOf("hcid");
    public static final EvtParamType<Integer> GOODS_CID = valueOf("goodsCid");
    public static final EvtParamType<Integer> CURRENCY_TYPE = valueOf("curType");
    public static final EvtParamType<Integer> DUP_CID = valueOf("dup_cid");
    public static final EvtParamType<Integer> DUP_Type = valueOf("dup_type");
    public static final EvtParamType<Integer> QUEST_CID = valueOf("quest_cid");
    public static final EvtParamType<Integer> Skill_CID = valueOf("skill_cid");
    public static final EvtParamType<Integer> MONSTER_TEAM_CID = valueOf("monster_team");
    public static final EvtParamType<Integer> GUILD_ID = valueOf("gid");
    public static final EvtParamType<String> GUILD_NAME = valueOf("gName");
    public static final EvtParamType<Integer> PARTY_SCORE = valueOf("ptyScr");
    public static final EvtParamType<Integer> BOSS_CID = valueOf("bossCid");
    public static final EvtParamType<Integer> LEVEL_TYPE = valueOf("lvType");
    public static final EvtParamType<Integer> LEVEL = valueOf("lv");
    public static final EvtParamType<Integer> BREACH_LEVEL = valueOf("brlv");
    public static final EvtParamType<Integer> STAR = valueOf("star");
    public static final EvtParamType<Integer> OLD_LEVEL = valueOf("oldLv");
    public static final EvtParamType<Integer> EXP = valueOf("exp");
    public static final EvtParamType<Long> TOTAL_NUM = valueOf("tNum");
    public static final EvtParamType<Integer> EVT_CID = valueOf("eCid");
    public static final EvtParamType<Integer> PLOT_ID = valueOf("plotId");
    public static final EvtParamType<Boolean> FIRST_PAY = valueOf("frstPay");
    public static final EvtParamType<String> ORDER_ITEM_ID = valueOf("itemId");
    public static final EvtParamType<GuildVO> GUILD = valueOf("guild");
    public static final EvtParamType<PlayerVO> PLAYER = valueOf("player");
    public static final EvtParamType<Integer> Chapter = valueOf("Chapter");
    public static final EvtParamType<Integer> Part = valueOf("Part");
    public static final EvtParamType<Integer> Index = valueOf("Index");
    public static final EvtParamType<Integer> BEAUTY_CID = valueOf("beautyCid");

    public static final EvtParamType<BaseCfg> CONFIG = valueOf("config");

    public static final EvtParamType<Integer> COLLECT_TYPE = valueOf("collectType");
    public static final EvtParamType<Integer> SHOP_CID = valueOf("SHOP_CID");
    public static final EvtParamType<ShopType> SHOP_Type = valueOf("SHOP_Type");

    public static final EvtParamType<RightLevel> Right_Level = valueOf("RIGHT_LEVEL");
    public static final EvtParamType<PlayerInfoVO> PlayerInfoVO = valueOf("PlayerInfoVO");

    public static final EvtParamType<Integer> RANK = valueOf("rank");
    public static final EvtParamType<Integer> OLD_GRADE = valueOf("oldGrade");
    public static final EvtParamType<Integer> GRADE = valueOf("grade");
    public static final EvtParamType<Integer> NUM = valueOf("num");
    public static final EvtParamType<Integer> HURT = valueOf("hurt");
    public static final EvtParamType<Boolean> WIN = valueOf("win");
    public static final EvtParamType<Boolean> PAY = valueOf("pay");
    public static final EvtParamType<Long> LONG_VAL = valueOf("longVal");
    public static final EvtParamType<Long> LF = valueOf("lf");

    public static final EvtParamType<Collection<ActivityVO>> ACTIVITY_LIST = valueOf("act_list");
    public static final EvtParamType<ActivityVO> ACTIVITY = valueOf("act");
    public static final EvtParamType<Integer> TYPE = valueOf("type");
    public static final EvtParamType<Map<?, ?>> PRIZE = valueOf("prize");
    public static final EvtParamType<SysType> SYS_TYPE = valueOf("sysType");
    public static final EvtParamType<FuncConfig> PRICE = valueOf("price");

    public static final EvtParamType<Long> CHANGE = valueOf("change");
    public static final EvtParamType<Long> BEFORE = valueOf("before");
    public static final EvtParamType<Long> AFTER = valueOf("after");
    public static final EvtParamType<MailVO> MAIL_VO = valueOf("mail_vo");
    public static final EvtParamType<Collection<MailVO>> MAIL_VO_S = valueOf("mail_vo_s");
    
    public static final EvtParamType<Integer> WORKER_CID = valueOf("wCid");

    public static final EvtParamType<String> QUEST_LOOP_TYPE = valueOf("qlType");
    
	public static final EvtParamType<Boolean> SAME_DATE = valueOf("smDte");
    public static final EvtParamType<Boolean> BATCH = valueOf("batch");
    
    private String name;

    private EvtParamType(String name) {
        this.name = name;
    }

    private static <T> EvtParamType<T> valueOf(String name) {
        return new EvtParamType<>(name);
    }

    public EvtParam<T> val(T val) {
        return new EvtParam<>(this, val);
    }


    // ---------------------------------------------constructor---------------------------------------------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvtParamType<?> evtParamType = (EvtParamType<?>) o;
        return name.equals(evtParamType.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
