/**
 * 
 */
package com.cellsgame.game.module.stats.cons;

/**
 * @author peterveron
 *
 */
public class StatsCons {
	public static final int	BIZ_TYPE_OP_MONEY_TIMES		=	1;//		1	使用销售产品功能次数
	public static final int	BIZ_TYPE_OP_CONTANCT_TIMES	=	2;//		2	使用拓展人脉功能次数
	public static final int	BIZ_TYPE_OP_CHANCE_TIMES	=	3;//		3	使用发现机遇功能次数
	public static final int	BIZ_TYPE_AFFAIR_PRZ_TIMES	=	4;//		4	使用处理公务功能次数
	public static final int	BIZ_TYPE_PASS_STORY_TIMES	=	5;//		5	通关关卡次数
	public static final int	BIZ_TYPE_FIGHT_FORCE_SUM	=	6;//		6	势力值（4属性总和）达到给定数值
	public static final int	BIZ_TYPE_PLAYER_LV			=	7;//		7	用户等级达到指定数值（富豪等级）
	public static final int	BIZ_TYPE_BEAUTY_R_PET_TIMES	=	8;//		8	使用随机约会功能次数
	public static final int	BIZ_TYPE_BEAUTY_PET_TIMES	=	9;//		9	使用指定美女约会功能次数
	public static final int	BIZ_TYPE_CHILD_NUM			=	10;//		10	子嗣数量达到指定数量
	public static final int	BIZ_TYPE_CHILD_TRAIN_TIMES	=	11;//		11	培养子嗣次数
	public static final int	BIZ_TYPE_RANK_ADD_LIKE_TIMES=	12;//		12	使用膜拜功能次数（排行帮功能中）
	public static final int	BIZ_TYPE_CHECK_IN_TIMES		=	13;//		13	使用签到功能次数
	public static final int	BIZ_TYPE_LVUP_WBOOK	=	14;//		14	使用强化小弟秘籍功能次数（如果使用强化卷，无论成功或失败，均记录1次）
	public static final int	BIZ_TYPE_SEARCH_TIMES		=	15;//		15	去散心（寻访）次数
	public static final int	BIZ_TYPE_BUSINESS_TIMES		=	16;//		16	和老板谈生意（牢房）测试
	public static final int	BIZ_TYPE_ACADEMY_TIMES		=	17;//		17	使用家教功能次数（1个小弟参与1次记录1次）
	public static final int	BIZ_TYPE_BEAUTY_GIFT_TIMES	=	18;//		18	给美女送礼次数（每送1个道具记录1次）
	public static final int	BIZ_TYPE_DEBATE_TIMES		=	19;//		19	讲道理（衙门）小弟出战次数
	public static final int	BIZ_TYPE_CHILDS_EXTDS_TIMES	=	20;//		20	扩展子嗣栏位到指定个数
	public static final int	BIZ_TYPE_ACDMY_EXTDS_TIMES	=	21;//		21	扩展家教栏位到指定个数
	public static final int	BIZ_TYPE_SHOPPING_TIMES		=	22;//		22	商城中购买道具
	public static final int	BIZ_TYPE_LVUP_WSKILL_TIMES	=	23;//		23	提升小弟技能等级次数
	public static final int	BIZ_TYPE_GUILD_JOIN_TIMES	=	24;//		24	加入商盟
	public static final int	BIZ_TYPE_GUILD_BUILD_TIMES	=	25;//		25	使用商盟建设功能次数
	public static final int	BIZ_TYPE_GUILD_EXCHG_TIMES	=	26;//		26	使用商盟兑换功能次数
	public static final int	BIZ_TYPE_LOGIN_DATES		=	27;//		27	登录天数达到指定数值
	public static final int	BIZ_TYPE_WORKER_NUM			=	28;//		28	小弟数量
	public static final int	BIZ_TYPE_VIP_LV				=	29;//		29	VIP等级达到指定数值
	public static final int	BIZ_TYPE_BEAUTY_NUM			=	30;//		30	获取的美女数量达到指定数值
	public static final int	BIZ_TYPE_CHILD_MARRIAGE_NUM		=	31;//		31	子嗣联姻数量达到指定次数
	public static final int	BIZ_TYPE_WORLD_BOSS_DEEATS	=	32;//		32	击败狗大户（世界BOSS）达到指定次数
	public static final int	BIZ_TYPE_PLUNDER_WINS		=	33;//		33	抢地盘（讨伐）胜利达到指定次数
	public static final int	BIZ_TYPE_PARTY_TIMES		=	34;//		34	找乐子（酒楼）参与次数达到指定数值（赴宴过程，不包括自己发起）
//	public static final int	BIZ_TYPE_;//		35	剁手节（通商）胜利达到指定次数
	public static final int	BIZ_TYPE_MONTH_CARD_TIMES	=	36;//		36	领取月卡福利次数达到指定数值
	public static final int	BIZ_TYPE_YEAR_CARD_TIMES	=	37;//		37	领取年卡福利次数达到指定数值
	public static final int	BIZ_TYPE_WORSHIP_TIMES		=	38;//		38	领取电视台（皇宫）福利次数达到指定数值
	public static final int	BIZ_TYPE_ACTIVENESS			=	39;//		39	活跃度达到指定数值
	public static final int	BIZ_TYPE_LVUP_WORKER_TIMES	=	40;//		40	小弟升级次数达到指定数值
	public static final int	BIZ_TYPE_LVUP_BSKILL_TIMES	=	42;//		42	升级美女技能次数达到指定数值
	public static final int	BIZ_TYPE_LVUP_WBOOK_BY_ITEM	=	43;//		43	使用强化卷轴强化小弟秘籍次数
	public static final int	BIZ_TYPE_GUILD_LV 			=	44;//		44	所在联盟等级到达指定数值

	
	
	public static final int	COMPLEX_TYPE_3 = 1000;
	public static final int	COMPLEX_TYPE_4 = 10_000;
	public static final int	COMPLEX_TYPE_5 = 100_000;
	public static final int	COMPLEX_TYPE_6 = 1_000_000;
	public static final int	COMPLEX_TYPE_7 = 10_000_000;
	public static final int	COMPLEX_TYPE_8 = 100_000_000;
	
	public static final int	COMPLEX_TYPE_START = COMPLEX_TYPE_3;
	public static final int	COMPLEX_TYPE_END = COMPLEX_TYPE_8;
}
