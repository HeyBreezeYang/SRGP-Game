package com.cellsgame.game.cons;

import java.util.Arrays;

import com.google.common.base.MoreObjects;

public class Command {
    /**
     * ModuleID.General
     */
    public static final int General_System_Time = ModuleID.General + 1;
    public static final int General_System_Refresh = ModuleID.General + 2;
    public static final int General_ServerClose = ModuleID.General + 3;

    /**
     * ModuleID.Player
     */
    public static final int Player_VerifySign = ModuleID.Player + 1;  //验证签名
    public static final int Player_EnterGame = ModuleID.Player + 2;//进入游戏
    public static final int Player_Create = ModuleID.Player + 3;//创建玩家
    public static final int Player_Query_byName = ModuleID.Player + 5;                  // 玩家模糊查询
    public static final int Player_Query_By_Pid = ModuleID.Player + 6;                  // 玩家信息根据PID查询
    public static final int Player_Record_Story = ModuleID.Player + 9;                  // 记录玩家剧情点
    public static final int Player_Random_Name = ModuleID.Player + 11;                  // 随机名字
    public static final int Player_CHG_IMG2 = ModuleID.Player + 12;                     // 改变头像img2
    public static final int Player_CHECK_IN = ModuleID.Player + 14;                     // 签到
    public static final int Player_GET_TOTAL_CHECK_IN_PRIZE = ModuleID.Player + 15;              // 领取累计签到奖励
    public static final int Player_Buy_Money = ModuleID.Player + 16;                    // 购买游戏币
    public static final int Player_ReplaceLogin = ModuleID.Player + 17;                   //替换登录
    public static final int Player_PI = ModuleID.Player + 18;                           //心跳
    public static final int Player_ModifyName = ModuleID.Player + 19;                   // 修改玩家名
    public static final int Player_Adventure_Update = ModuleID.Player + 20;             //奇遇数据更新
    public static final int Player_RevFirstPayPrize = ModuleID.Player + 21;             //领取第一次充值奖励
    public static final int Player_ServerTime = ModuleID.Player + 22;                   //服务端时间
    public static final int Player_UpLevel = ModuleID.Player + 23;                      //升级
    public static final int Player_FuncTimes = ModuleID.Player + 24;                      //获取功能次数
    public static final int Player_Worship = ModuleID.Player + 25;                      //膜拜
    public static final int Player_TitleDec = ModuleID.Player + 26;                      //设置称号宣言
    public static final int Player_VipLevelPrize = ModuleID.Player + 27;                 //VIP等级奖励


    /**
     * ModuleID.Worker
     */
    public static final int Worker_UpLevel = ModuleID.Worker + 1;  //升级
    public static final int Worker_Breach = ModuleID.Worker + 2;  //突破
    public static final int Worker_ExpUpLevelBook = ModuleID.Worker + 3;  //经验升级书籍
    public static final int Worker_RadomUpLevelBook = ModuleID.Worker + 4;  //几率升级书籍
    public static final int Worker_GoodsUpLevelBook = ModuleID.Worker + 5;  //w物品升级书籍
    public static final int Worker_UpLevelWorkerSkill = ModuleID.Worker + 6; //升级技能
    public static final int Worker_ResetUseStatus = ModuleID.Worker+7;
    public static final int Worker_UseFuncGoods = ModuleID.Worker + 8;//使用功能道具
    public static final int Worker_Exchange = ModuleID.Worker + 9;//员工兑换


    /**
     * ModuleID.Goods
     */
    public static final int Goods_UseGoods = ModuleID.Goods + 1; //使用物品
    public static final int Goods_PlayerUseGoodsWithParam = ModuleID.Goods + 2; //使用物品



    /**
     * ModuleID.Depot
     */
    public static final int Depot_Ref = ModuleID.Depot + 1; //背包刷新



    /**
     * @see ModuleID#Beauty
     */
    public static final int Beauty_UseFuncGoods = ModuleID.Beauty + 1; //赏赐
    public static final int Beauty_Pet = ModuleID.Beauty + 2; //宠幸
    public static final int Beauty_RandomPet = ModuleID.Beauty + 3; //随机宠幸
    public static final int Beauty_UpLevelWorkerSkill = ModuleID.Beauty + 4; //升级技能
    public static final int Beauty_BatchRandomPet = ModuleID.Beauty + 5; //批量随机宠幸
    public static final int Beauty_RevPetNum = ModuleID.Beauty + 6;//恢复次数

    /**
     * @see ModuleID#Child
     */
    public static final int Child_Train = ModuleID.Child + 1; //培养
    public static final int Child_BatchTrain = ModuleID.Child + 2; //批量培养
    public static final int Child_Exam = ModuleID.Child + 3; //考试
    public static final int Child_RevChildEnergy = ModuleID.Child + 4; //恢复活力
    public static final int Child_BatchRevChildEnergy = ModuleID.Child + 5; //批量恢复活力
    public static final int Child_SetName = ModuleID.Child + 6; // 子嗣名字设定
    public static final int Child_AddLen = ModuleID.Child + 7; // 增加子嗣栏位
    public static final int Child_MarryEnd = ModuleID.Child + 8;//联姻结束
    public static final int Child_NotifyMarry = ModuleID.Child + 9;//发起联姻
    public static final int Child_Marry = ModuleID.Child + 10;//联姻
    public static final int Child_DealMarryEnd = ModuleID.Child + 11;//处理联姻结束列表
    public static final int Child_GetMarryList = ModuleID.Child + 12;//获取联姻列表
    public static final int Child_RefMarryList = ModuleID.Child + 13;//重置联姻列表
    public static final int Child_CancelMarry = ModuleID.Child + 14;//取消联姻
    public static final int getChild_MarryEnd = ModuleID.Child + 15;//子嗣联姻结束
    public static final int Child_GetMarryTimes = ModuleID.Child + 16;//获取联姻次数
    public static final int Child_GetDirectMarryList = ModuleID.Child + 17;//获取定向联姻列表
    public static final int Child_DirectMarryNotify = ModuleID.Child + 18;//定向联姻通知

    /**
     * @see ModuleID#Affairs
     */
    public static final int Affairs_Prize = ModuleID.Affairs + 1; //领取奖励
    public static final int Affairs_RevNum = ModuleID.Affairs + 2; //恢复次数


    /**
     * @see ModuleID#Operate
     */

    public static final int Operate_Money = ModuleID.Operate + 1; //经营金币
    public static final int Operate_Chance = ModuleID.Operate + 2; //经营机会
    public static final int Operate_Contacts = ModuleID.Operate + 3; //经营人脉
    public static final int Operate_BatchOperate = ModuleID.Operate + 4; //一键经营
    public static final int Operate_RevMoneyNum = ModuleID.Operate + 5; //恢复经营金币次数
    public static final int Operate_RevChanceNum = ModuleID.Operate + 6; //复经营机会次数
    public static final int Operate_RevContactsNum = ModuleID.Operate + 7; //复经营人脉次数

    /**
     * @see ModuleID#Guild
     */
    public static final int Guild_Create = ModuleID.Guild + 1;                // 创建军团
    public static final int Guild_Join = ModuleID.Guild + 2;                  // 加入军团
    public static final int Guild_Query = ModuleID.Guild + 3;                 // 查询军团
    public static final int Guild_AcceptJoin = ModuleID.Guild + 4;           // 同意加入军团
    public static final int Guild_ModifyNotice = ModuleID.Guild + 5;         // 修改公告
    public static final int Guild_ModifyDesc = ModuleID.Guild + 6;           // 修改宣言
    public static final int Guild_ModifyQQ = ModuleID.Guild + 7;           // 修改LOGO
    public static final int Guild_ModifyName = ModuleID.Guild + 8;           // 修改名字
    public static final int Guild_ModifyRight = ModuleID.Guild + 9;          // 修改权限
    public static final int Guild_Out = ModuleID.Guild + 10;                  // 离开军团
    public static final int Guild_ChgOwner = ModuleID.Guild + 11;            // 转让军团
    public static final int Guild_Dissolution = ModuleID.Guild + 12;          // 解散军团
    public static final int Guild_CancelDissolution = ModuleID.Guild + 13;   // 取消解散军团
    public static final int Guild_Donate = ModuleID.Guild + 14;               // 捐献
    public static final int Guild_Enter = ModuleID.Guild + 15;                // 进入军团
    public static final int Guild_ShowMemberList = ModuleID.Guild + 16;      // 查询军团成员
    public static final int Guild_ShowReqList = ModuleID.Guild + 17;         // 查询请求列表
    public static final int Guild_InvitePlayer = ModuleID.Guild + 18;        // 邀请玩家加入
    public static final int Guild_ModifyVX = ModuleID.Guild + 19;           // 修改微信
    public static final int Guild_ModifyNeedReqStatus = ModuleID.Guild + 21;  // 修改公会是否需要请求
    public static final int Guild_GetGuildLog = ModuleID.Guild + 22;        // 获取操作日志
    public static final int Guild_GetBoss = ModuleID.Guild + 23;        // 获取boss列表
    public static final int Guild_OpenBoss = ModuleID.Guild + 24;        // 开启boss
    public static final int Guild_FightBoss = ModuleID.Guild + 25;        // 攻击boss
    public static final int Guild_GetRank = ModuleID.Guild + 26;        // 获取排名
    public static final int Guild_NotAllJoinReq = ModuleID.Guild + 27;           // 否决所有加入请求
    public static final int Guild_BossRef = ModuleID.Guild + 28;        // boss刷新

    /**
     * @see ModuleID#Shop
     */
    public static final int Shop_Open = ModuleID.Shop + 1;//商店打开
    public static final int Shop_Buy = ModuleID.Shop + 2;//购买物品
    public static final int Shop_Buy_Special = ModuleID.Shop + 3;//购买特殊物品
    public static final int Shop_Manual_Refresh = ModuleID.Shop + 4;//手动刷新商店
    public static final int SHOP_SELL_GOODS = ModuleID.Shop + 5;//出售物品


    /**
     * @see ModuleID#Mail
     */
    public static final int Mail_Add = ModuleID.Mail + 1;//增加邮件
    public static final int Mail_Get = ModuleID.Mail + 2;//获取邮件信息
    public static final int Mail_Delete = ModuleID.Mail + 3;//删除邮件
    public static final int Mail_Pick = ModuleID.Mail + 4;//领取附件
    public static final int Mail_Send = ModuleID.Mail + 5;//发送邮件
    public static final int Mail_List = ModuleID.Mail + 6;//邮件列表
    public static final int Mail_Picks = ModuleID.Mail + 7;//批量领取附件


    /**
     * @see ModuleID#Business
     */
    public static final int Business_Drink = ModuleID.Business + 1;//喝酒


    /**
     * @see ModuleID#Search
     */
    public static final int Search_Search = ModuleID.Search + 1;//寻访
    public static final int Search_MoneyRevLuck = ModuleID.Search + 2;//金币恢复运气
    public static final int Search_ContactsRevLuck = ModuleID.Search + 3;//人脉恢复运气
    public static final int Search_TreRevLuck = ModuleID.Search + 4;//元宝恢复运气
    public static final int Search_RevSearchNum = ModuleID.Search + 5;//恢复寻访次数
    public static final int Search_Get = ModuleID.Search + 6;// 获取寻访信息
    public static final int Search_SetAutoRevLuck = ModuleID.Search + 7;// 设置自动恢复运势
    public static final int Search_BatchSearch = ModuleID.Search + 8;//一键寻访


    /**
     * @see ModuleID#Chat
     */
    public static final int CHAT_CHAT = ModuleID.Chat + 1;                 // 聊天
    public static final int CHAT_MSG = ModuleID.Chat + 2; //聊天信息
    public static final int CHAT_PRIVATE_MSG = ModuleID.Chat + 3;
    public static final int CHAT_REV_PRIVATE_MSG = ModuleID.Chat + 4;
    public static final int CHAT_GET_ALL_PRIVATE_MSG = ModuleID.Chat + 5;
    public static final int CHAT_NOTIFY_MSG = ModuleID.Chat + 6;
    public static final int CHAT_GET_CACHE_CHAT = ModuleID.Chat + 7;                 //获取缓存聊天


    /**
     * @see ModuleID#Store
     */
    public static final int Store_Open = ModuleID.Store + 1;                 // 打开商城
    public static final int Store_Buy = ModuleID.Store + 2;                 // 购买
    public static final int Store_AddStore = ModuleID.Store + 3;     //新增商城
    public static final int Store_UpdateStore = ModuleID.Store + 4;     //修改商城
    public static final int Store_DeleteStore = ModuleID.Store + 5;     //删除商城

    /**
     * @see ModuleID#Boss
     */
    public static final int Boss_Enter = ModuleID.Boss + 1; // 进入BOSS系统
    public static final int Boss_Out = ModuleID.Boss + 2; // 退出BOSS系统
    public static final int Boss_Fight = ModuleID.Boss + 3; // 攻击BOSS
    public static final int Boss_RevRankPrize = ModuleID.Boss + 4; // 领取排行榜奖励
    public static final int Boss_StateChange = ModuleID.Boss + 5; // 状态变更
    public static final int Boss_SyncLf = ModuleID.Boss + 6; // 血量同步
    public static final int Boss_GetRank = ModuleID.Boss + 7;// 获取排行榜
    public static final int Boss_KillRecord = ModuleID.Boss + 8;// 获取共享BOSS的击杀记录

    /**
     * @see ModuleID#Trade
     */
    public static final int Trade_Fight = ModuleID.Trade + 1;//战斗
    public static final int Trade_Sweep = ModuleID.Trade + 2;//扫荡

    /**
     * @see ModuleID#Party
     */
    public static final int Party_List = ModuleID.Party + 1;//获取宴会列表
    public static final int Party_Get = ModuleID.Party + 2;//获取宴会详细信息
    public static final int Party_Create = ModuleID.Party + 3;//创建宴会
    public static final int Party_Join = ModuleID.Party + 4;//参加
    public static final int Party_Prize = ModuleID.Party + 5;//领取宴会奖励
    public static final int Party_End = ModuleID.Party + 6;//宴会结束
    public static final int Party_GetRec = ModuleID.Party + 7;//获取宴会记录
    public static final int Party_RevPartyTimes = ModuleID.Party + 8;//恢复参加宴会次数

    /**
     * @see ModuleID#Plunder
     */
    public static final int Plunder_Fight = ModuleID.Plunder + 1;//战斗
    public static final int Plunder_Sweep = ModuleID.Plunder + 2;//扫荡

    /**
     * @see ModuleID#Title
     */
    public static final int Title_Use = ModuleID.Title + 1;//使用称号
    public static final int Title_UnUse = ModuleID.Title + 2;//取消使用
    public static final int Title_GetHoldPlayers = ModuleID.Title + 3;//获取称号的获得者
    public static final int Title_End = ModuleID.Title + 4;//称号到期


    /**
     * @see ModuleID#Make
     * */
    public static final int Make_Make = ModuleID.Make + 1;//制作
    public static final int Make_BatchMake = ModuleID.Make + 2;//批量制作
    
    /**
     * 好友
     *
     * @see ModuleID#Friends
     */
    public static final int FRIEND_GET_MY_FRIEND = ModuleID.Friends + 1;        // 获取好友列表
    public static final int FRIEND_QUERY = ModuleID.Friends + 2;                // 查询好友
    public static final int FRIEND_ADD_NEW = ModuleID.Friends + 3;              // 添加新好友
    public static final int FRIEND_ADD_NEW_FEED_BACK = ModuleID.Friends + 4;    // 好友添加反馈
    public static final int FRIEND_DELETE = ModuleID.Friends + 5;               // 删除好友
    public static final int FRIEND_ADD_2_BLACK_LIST = ModuleID.Friends + 6;     // 添加黑名单
    public static final int FRIEND_REMOVE_FROM_BLACK_LIST = ModuleID.Friends + 7;// 黑名单中删除
    public static final int FRIEND_RECOMMENDED = ModuleID.Friends + 8;          // 推荐好友
    public static final int FRIEND_FIGHT = ModuleID.Friends + 9;                // 战斗
    public static final int FRIEND_QUERY_NEAR = ModuleID.Friends + 10;          // 查询最近联系
    public static final int FRIEND_GET_BLESS_DATA = ModuleID.Friends + 11;      // 获取祝福数据
    public static final int FRIEND_BLESS_FRIEND = ModuleID.Friends + 12;        // 祝福好友
    public static final int FRIEND_BLESS_GET_PRIZE = ModuleID.Friends + 13;        // 祝福收获奖励


    /**
     * 兑换
     * @see ModuleID#Convert
     */
    public static final int Convert_Get = ModuleID.Convert + 1;        // 获取信息
    public static final int Convert_Convert = ModuleID.Convert + 2;        // 兑换
    public static final int Convert_Ref = ModuleID.Convert + 3;        // 刷新
    public static final int Convert_GetRank = ModuleID.Convert + 4; // 获取排行
    public static final int Convert_GetRankPrize = ModuleID.Convert + 5; // 获取排行奖励
    public static final int Convert_Enter = ModuleID.Convert + 6;        // 进入活动
    public static final int Convert_Out = ModuleID.Convert + 7;        // 退出活动
    public static final int Convert_Prize = ModuleID.Convert + 8;    //其他玩家获得奖励


    /**
     * 探险
     * @see ModuleID#Explore
     */
    public static final int Explore_Get = ModuleID.Explore + 1;        // 获取信息
    public static final int Explore_Find = ModuleID.Explore + 2;        // 探险
    public static final int Explore_Fight = ModuleID.Explore + 3;        // 攻击怪物
    public static final int Explore_GetRank = ModuleID.Explore + 4;        // 获取排行
    public static final int Explore_GetRankPrize = ModuleID.Explore + 5;        // 获取排行奖励
    public static final int Explore_Enter = ModuleID.Explore + 6;        // 进入活动
    public static final int Explore_Out = ModuleID.Explore + 7;        // 退出活动
    public static final int Explore_BuyTimes = ModuleID.Explore + 8;        // 购买次数
    public static final int Explore_Ref = ModuleID.Explore + 9;        // 刷新
    public static final int Explore_Prize = ModuleID.Explore + 10;    //其他玩家获得奖励
    public static final int Explore_GetItems = ModuleID.Explore + 11;    //获取探索到的项目
    public static final int Explore_Inspire = ModuleID.Explore + 12;    //鼓舞
    public static final int Explore_GetRec = ModuleID.Explore + 13;    //获取记录
    public static final int Explore_ResetWorker = ModuleID.Explore + 14;//重置员工出战
    public static final int Explore_GetPlayerPrizeRec = ModuleID.Explore + 15;//获取玩家奖励记录


    /**
     * @see ModuleID#BeautyPlot
     */
    public static final int BeautyPlot_Commit = ModuleID.BeautyPlot + 1;                // 提交剧情

    /**
     * @see ModuleID#RANK
     */
    public static final int RANK_GET_RANK_LIST = ModuleID.RANK + 1;                // 获取排行榜列表
    public static final int RANK_ADD_LIKE = ModuleID.RANK + 2;                     // 点赞


    /**
     * @see ModuleID#Halo
     */
    public static final int Halo_UpLevel = ModuleID.Halo + 1;                // 升级


    /**
     * {@link ModuleID#ACHIEVE}
     */
    public static final int ACHIEVE_GET_INFO = ModuleID.ACHIEVE + 1;                // 获取信息
    public static final int ACHIEVE_GET_PRIZE = ModuleID.ACHIEVE + 2;               // 获取成就奖励
    public static final int ACHIEVE_GET_POINT_PRIZE = ModuleID.ACHIEVE + 3;         // 获取成就积分奖励
    /**
     * {@link ModuleID#GroupChess}
     */
    public static final int GroupChess_Group_List = ModuleID.GroupChess + 1;        // 列表
    public static final int GroupChess_Join = ModuleID.GroupChess + 2;              // 加入队伍
    public static final int GroupChess_Leave = ModuleID.GroupChess + 3;             // 离开队伍
    public static final int GroupChess_Create = ModuleID.GroupChess + 4;            // 创建队伍
    public static final int GroupChess_Kick = ModuleID.GroupChess + 5;              // T人
    public static final int GroupChess_start = ModuleID.GroupChess + 6;             // 开始战斗
    public static final int GroupChess_roll = ModuleID.GroupChess + 7;              // 投掷骰子
    public static final int GroupChess_move = ModuleID.GroupChess + 8;              // 投掷骰子
    public static final int GroupChess_startWhenFull = ModuleID.GroupChess + 9;     // 设置自动开始
    public static final int GroupChess_Invite = ModuleID.GroupChess + 10;           // 邀请
    public static final int GroupChess_World_Word = ModuleID.GroupChess + 11;       // 世界喊话
    public static final int GroupChess_SHOW_FIGHT = ModuleID.GroupChess + 12;       // 查看当前位置的战斗
    public static final int GroupChess_ready = ModuleID.GroupChess + 13;            // 设置准备状态
    public static final int GroupChess_chg_goods = ModuleID.GroupChess + 14;        // 改变加成道具
    public static final int GroupChess_re_enter = ModuleID.GroupChess + 15;         // 重新加入 chess
    public static final int GroupChess_out_chess = ModuleID.GroupChess + 16;        // 离开chess
    public static final int GroupChess_chat = ModuleID.GroupChess + 17;             // 聊天
    public static final int GroupChess_buy_life = ModuleID.GroupChess + 18;         // 直接购买复活
    public static final int GroupChess_other_move = ModuleID.GroupChess + 19;         // 其他玩家移动 广播消息
    public static final int GroupChess_AutoMatch = ModuleID.GroupChess + 20;         // 自动匹配
    public static final int GroupChess_ExitMatch = ModuleID.GroupChess + 21;         // 退出匹配
    public static final int GroupChess_MatchJoin = ModuleID.GroupChess + 22;         // 匹配加入队伍



    /**
     * {@link ModuleID#VxDialog}
     */
    public static final int VxDialog_Start = ModuleID.VxDialog + 1;        // 开始聊天
    public static final int VxDialog_Prize = ModuleID.VxDialog + 2;         // 奖励选择
    public static final int VxDialog_Record = ModuleID.VxDialog + 3;         //记录选择
    public static final int VxDialog_Enter = ModuleID.VxDialog + 4;         //进入微信系统
    public static final int VxDialog_AutoDialog = ModuleID.VxDialog + 5;         //自动聊天


    /**
     * {@link ModuleID#Activity}
     */
    public static final int Activity_SysRef = ModuleID.Activity + 1;               // 活动刷新
    public static final int Activity_GetInfo = ModuleID.Activity + 2;   //获取活动数据
    public static final int Activity_ExecBevs = ModuleID.Activity + 3;  //执行行为
    public static final int Activity_BatchExecBevs = ModuleID.Activity + 4;  //批量执行行为
    public static final int Activity_GetRank = ModuleID.Activity + 5;  //获取活动排行榜

    /**
     * {@link ModuleID#Quest}}
     */
    public static final int Quest_commit = ModuleID.Quest + 1;

    
    /**
     * {@link ModuleID#Story}
     */
    public static final int Story_forward = ModuleID.Story + 1;
    public static final int Story_batch_forward = ModuleID.Story + 2;
    
    /**
     * {@link ModuleID#Pay}
     * */
    public static final int Pay_Prize = ModuleID.Pay + 1; // 充值成功


    /**
     * {@link ModuleID#Card}
     * */
    public static final int Card_Prize = ModuleID.Card + 1; // 领取奖励
    
    /**
     * {@link ModuleID#ACADEMY}
     */
    public static final int ACADEMY_EXTEND = ModuleID.ACADEMY+1;
    public static final int ACADEMY_IN_SEAT = ModuleID.ACADEMY+2;
    public static final int ACADEMY_FIN = ModuleID.ACADEMY+3;
    public static final int ACADEMY_ONE_KEY_FIN = ModuleID.ACADEMY+4;
    
	public static final int SOCIETY_CREATE = ModuleID.SOCIETY+1;
	public static final int SOCIETY_ENTER = ModuleID.SOCIETY+2;
	public static final int SOCIETY_GET_PRZ = ModuleID.SOCIETY+3;
	public static final int SOCIETY_PROTECT = ModuleID.SOCIETY+4;
	public static final int SOCIETY_LEVEL_UP = ModuleID.SOCIETY+5;
	public static final int SOCIETY_QUERY = ModuleID.SOCIETY+6;
	public static final int SOCIETY_QUERY_ONE = ModuleID.SOCIETY+7;
    
	public static final int DEBATE_GET_ENEMIES = ModuleID.DEBATE+1;
	public static final int DEBATE_GET_DEF_INFOS = ModuleID.DEBATE+2;
    public static final int DEBATE_GET_LIST_INFO = ModuleID.DEBATE+3;
    public static final int DEBATE_REF_VOLUNTEER = ModuleID.DEBATE+4;
    public static final int DEBATE_START_VOLUNTEER = ModuleID.DEBATE+5;
    public static final int DEBATE_START_CHALLENGE = ModuleID.DEBATE+6;
    public static final int DEBATE_START_REVENGE = ModuleID.DEBATE+7;
    public static final int DEBATE_START_CHASE = ModuleID.DEBATE+8;
    public static final int DEBATE_DEAL = ModuleID.DEBATE+9;
    public static final int DEBATE_ONE_KEY_DEAL = ModuleID.DEBATE+10;
    public static final int DEBATE_SELECT_BUFF = ModuleID.DEBATE+11;
    public static final int DEBATE_SELECT_PRZ = ModuleID.DEBATE+12;
    public static final int DEBATE_TGT_QUERY = ModuleID.DEBATE+13;

    /**
     * {@link ModuleID#Hero}
     */
    public static final int HERO_SUMMON_IN  = ModuleID.Hero + 1;       // 进入召唤
    public static final int HERO_SUMMON_OUT = ModuleID.Hero + 2;       // 退出召唤
    public static final int HERO_SUMMON = ModuleID.Hero + 3;           // 召唤英雄
    public static final int HERO_LEVEL_UP = ModuleID.Hero + 4;         // 英雄升级
    public static final int HERO_STAR_UP = ModuleID.Hero + 5;          // 英雄升星
    public static final int HERO_STAGE_UP = ModuleID.Hero + 6;         // 英雄突破
    public static final int HERO_CHAREER_NEW = ModuleID.Hero + 7;      // 英雄转职

    /**
     * {@link ModuleID#Skill}
     */
    public static final int SKILL_LEARN = ModuleID.Skill + 1;           // 技能学习
    public static final int SKILL_INHERIT = ModuleID.Skill + 2;         // 技能传承
    public static final int SKILL_EQUIP_ON = ModuleID.Skill + 3;        // 装备技能
    public static final int SKILL_EQUIP_OFF = ModuleID.Skill + 4;       // 脱下技能
    public static final int SEAL_CREATE = ModuleID.Skill + 5;           // 圣印制作
    public static final int SEAL_UPGRADE = ModuleID.Skill + 6;          // 圣印升级
    public static final int SEAL_EQUIP_ON = ModuleID.Skill + 7;         // 装备圣印
    public static final int SEAL_EQUIP_OFF = ModuleID.Skill + 8;        // 脱下圣印


    private static final int[] asynCommand;
    private static final int[] haveGameMessageCmd;


    static {
        asynCommand = new int[]{
                Player_PI,
                Player_VerifySign
        };
        haveGameMessageCmd = new int[]{
                Player_VerifySign,
                Player_EnterGame,
                Player_Create
        };
        Arrays.sort(haveGameMessageCmd);
    }

    // 在同步功能少的时候 直接使用int数组
    public static boolean isSyncCmd(int cmd) {
        for (int async : asynCommand) {
            if (cmd == async) return true;
        }
        return false;
    }

    public static boolean isCmdCanHaveGameMessage(int cmd) {
        return Arrays.binarySearch(haveGameMessageCmd, cmd) >= 0;
    }

}
