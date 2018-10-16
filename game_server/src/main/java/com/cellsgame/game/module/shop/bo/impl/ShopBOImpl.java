package com.cellsgame.game.module.shop.bo.impl;


import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Resource;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EvtHolder;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.event.EvtType;
import com.cellsgame.game.core.event.GameEvent;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.msgproc.annotation.CParam;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.func.FuncConfig;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.func.cons.SyncFuncType;
import com.cellsgame.game.module.guild.cache.CacheGuild;
import com.cellsgame.game.module.guild.msg.CodeGuild;
import com.cellsgame.game.module.guild.vo.GuildMemberVO;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cons.EvtTypePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.module.shop.ShopRefreshRule;
import com.cellsgame.game.module.shop.bo.ShopBO;
import com.cellsgame.game.module.shop.bo.ShopItemRecordBO;
import com.cellsgame.game.module.shop.csv.ShopConfig;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;
import com.cellsgame.game.module.shop.csv.ShopType;
import com.cellsgame.game.module.shop.csv.SimpleShopItemConfig;
import com.cellsgame.game.module.shop.evt.EvtTypeShop;
import com.cellsgame.game.module.shop.msg.CodeShop;
import com.cellsgame.game.module.shop.msg.MsgFactoryShop;
import com.cellsgame.game.module.shop.vo.ShopItemRecordVO;
import com.cellsgame.game.module.shop.vo.ShopItemVO;
import com.cellsgame.game.module.shop.vo.ShopVO;
import com.cellsgame.game.module.sys.vo.SystemVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商店逻辑.
 *
 * @author Yang
 */
public class ShopBOImpl implements ShopBO{
    private static final Logger log = LoggerFactory.getLogger(ShopBOImpl.class);

    private static final EvtType[] CONCERN_EVENTS = new EvtType[]{EvtTypePlayer.LeaveGuild, EvtTypePlayer.EnterGame};
    @Resource
    private BaseDAO<ShopVO> shopDAO;
    @Resource
    private BaseDAO<ShopItemRecordVO> shopItemRecordDAO;
    //
    private ShopItemRecordBO shopItemRecordBO;
    //
    private PlayerBO playerBO;

    /**
     * 打开商店
     *
     * @param player 玩家数据
     * @param shopId 商店ID
     * @return 打开结棍
     */
    @Override
    public Map<?, ?> open(PlayerVO player, int shopId) throws LogicException {
        // 商店配置
        ShopConfig config = CacheConfig.getCfg(ShopConfig.class, shopId);
        // 如果商店不存在
        CodeShop.SHOP_NOT_FOUND.throwIfTrue(config == null);
        // 如果是家族商店, 但玩家不属于任何家族
        if (config.isGuild()) {
            // 查看玩家所在公会
            GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(player.getId());
            // 如果不在公会
            CodeGuild.NotJoinGuild.throwIfTrue(memberVO == null || memberVO.getGuildID() <= 0);
        }
        //
        Map ret = GameUtil.createSimpleMap();
        // 商店数据
        ShopVO shopVO = getShopVO(player, config);
        // 如果商店不存在
        if (shopVO == null) shopVO = createShopVO(player, config);
        // 刷新商店
        systemRefresh(player, shopVO);
        // 如果是系统商店
        if (shopVO.isSystem() && shopVO.getVersion() != player.getSystemShopVersion()) {
            // 需要清除个人限购记录
//            shopItemRecordDAO.delete(player.getShopItemRecordVOMap().values());
//            // 清除个人限购记录
//            player.getShopItemRecordVOMap().clear();
            // 更新玩家当前系统商店版本号
            player.setSystemShopVersion(shopVO.getVersion());
            // 保存玩家数据
            playerBO.save(player);
        }
        // 返回商店数据
        return MsgUtil.brmAll(MsgFactoryShop.instance().getShopInfoMsg(ret, player, shopVO), Command.Shop_Open);
    }

    /**
     * 购买商店商品
     *
     * @param player   玩家数据
     * @param shopId   商店ID
     * @param index    商店商品下标
     * @param quantity 购买数量
     * @return 购买结果
     * @throws LogicException
     */
    @Override
    public Map<?, ?> buy(PlayerVO player, int shopId, int index, int quantity, CMD cmd) throws LogicException {
        quantity = Math.max(1, quantity);
        // 商店配置
        ShopConfig config = CacheConfig.getCfg(ShopConfig.class, shopId);
        // 如果商店不存在
        CodeShop.SHOP_NOT_FOUND.throwIfTrue(config == null);
        // 商店数据
        ShopVO shopVO = getShopVO(player, config);
        // 如果商店不存在
        CodeShop.SHOP_NOT_FOUND.throwIfTrue(shopVO == null);
        // 如果数据异常
        CodeShop.SHOP_DATA_EXCEPTION.throwIfTrue(index < 0 || index >= shopVO.getItemVOs().size());
        Map ret = GameUtil.createSimpleMap();
        // 需要购买的商品
        ShopItemVO shopItemVO = shopVO.getItemVOs().get(index);
        // 检测商品是否达到限购
        checkLimit(player, shopVO, shopItemVO, quantity);
        // 商品配置数据
        ShopItemConfig shopItemConfig = shopItemVO.getCfg();

        ////////////////////////////////////////////////////////////////////////
        // 检测物品是否可以添加成功
        FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
        exec.addSyncFunc(shopItemConfig.getPurchase(), quantity);
        ////////////////////////////////////////////////////////////////////////

        exec = FuncsExecutorsType.Base.getExecutor(cmd);
        // 購買消耗
        buildBuyCost(player, exec, shopItemConfig, quantity);
        exec.selectAndCheck(player); // 檢測消耗
        exec.addSyncFunc(shopItemConfig.getPurchase(), quantity);
        // 奖励消息数据
        Map prizeMap = GameUtil.createSimpleMap();
        // 购买及消耗结果
        exec.exec(ret, prizeMap, player);
        // 购买成功之后, 需要记录数据
        shopItemRecordBO.afterShopBuy(player, shopVO, shopItemVO, quantity);
        // 保存商店数据
        shopDAO.save(shopVO);
        // 事件
        ret = EvtTypeShop.BUY.happen(ret, cmd, player, EvtParamType.SHOP_CID.val(shopId), EvtParamType.SHOP_Type.val(config.getShopType()), EvtParamType.GOODS_CID.val(shopItemConfig.getId()), EvtParamType.NUM.val(quantity), EvtParamType.COLLECT_TYPE.val(shopItemConfig.getType()));
        // 返回购买结果
        return MsgUtil.brmAll(MsgFactoryShop.instance().getShopBuyMessage(ret, player, shopVO, shopItemVO, prizeMap), Command.Shop_Buy);
    }

    /**
     * 购买特殊商店商品
     *
     * @param player   玩家数据
     * @param itemId   商店商品ID
     * @param quantity 购买数量
     * @param cmd
     * @return 购买结果
     * @throws LogicException
     */
    @Override
    public Map<?, ?> buySpecial(PlayerVO player, @CParam("itemId") int itemId, @CParam("quantity") int quantity, CMD cmd) throws LogicException {
        quantity = Math.max(1, quantity);
        // 商品配置
        SimpleShopItemConfig shopItemConfig = CacheConfig.getCfg(SimpleShopItemConfig.class, itemId);
        // 如果商品不存在
        CodeShop.SHOP_ITEM_NOT_FOUND.throwIfTrue(shopItemConfig == null);
        // 如果等级不够
//        CodeShop.SHOP_REQUIRE_LEVEL.throwIfTrue(shopItemConfig.getPurchaseLv() > player.getLevel());
        Map ret = GameUtil.createSimpleMap();
        FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
        buildBuyCost(player, exec, shopItemConfig, quantity);
        exec.addSyncFunc(shopItemConfig.getPurchase(), quantity);
        exec.exec(ret, player);
        // 事件
        ret = EvtTypeShop.BUY.happen(ret, cmd, player, EvtParamType.SHOP_CID.val(-1), EvtParamType.SHOP_Type.val(ShopType.Special), EvtParamType.GOODS_CID.val(shopItemConfig.getId()), EvtParamType.NUM.val(quantity), EvtParamType.COLLECT_TYPE.val(shopItemConfig.getType()));
        // 返回购买结果
        return MsgUtil.brmAll(ret, Command.Shop_Buy_Special);
    }

    private void buildBuyCost(PlayerVO player, FuncsExecutor exec, SimpleShopItemConfig shopItemConfig, int quantity) {
//        List<FuncConfig> costs = shopItemConfig.getCosts();
//        if(!costs.isEmpty()) {
//            // 查看个人限购数据
//            ShopItemRecordVO recordVO = player.getShopItemRecordVOMap().get(shopItemConfig.getId());
//            int buyTimes = recordVO != null ? recordVO.getSold() : 0;
//            for (int i = 0; i < quantity; i++) {
//                if ((buyTimes + i) >= costs.size()) {
//                    FuncConfig funcConfig = costs.get(costs.size() - 1);
//                    exec.addSyncFunc(funcConfig);
//                }else{
//                    FuncConfig funcConfig = costs.get(buyTimes + i);
//                    exec.addSyncFunc(costs.get(buyTimes + i));
//                }
//            }
//        }
    }

    /**
     * 检测目标商品是否达到限购
     *
     * @param playerVO   玩家数据
     * @param shopVO     商店数据
     * @param shopItemVO 商品数据
     * @param quantity   购买数量
     * @throws LogicException
     */
    private void checkLimit(PlayerVO playerVO, ShopVO shopVO, ShopItemVO shopItemVO, int quantity) throws LogicException {
        // 商品配置数据
        ShopItemConfig shopItemConfig = shopItemVO.getCfg();
        // 等级
//        CodeShop.SHOP_REQUIRE_LEVEL.throwIfTrue(playerVO.getLevel() < shopItemConfig.getVisibleLv() || playerVO.getLevel() < shopItemConfig.getPurchaseLv());
        // VIP
//        CodeShop.SHOP_REQUIRE_VIP.throwIfTrue(playerVO.getVip() < shopItemConfig.getPurchaseVip());

        //获取商品限购数量
        int limit = getShopItemLimit(playerVO, shopVO, shopItemConfig);
        // 系统商店个人限购
        if (shopVO.isSystem()) {
            // 查看个人限购数据
//            ShopItemRecordVO recordVO = playerVO.getShopItemRecordVOMap().get(shopItemConfig.getId());
            // 如果达到限购
//            CodeShop.SHOP_LIMIT.throwIfTrue(recordVO != null && limit >= 0 && recordVO.getSold() + quantity > limit);
        }
        // 商店限购
        if (limit > 0) {
            // 如果达到限购
            CodeShop.SHOP_LIMIT.throwIfTrue(shopItemVO.getSold() + quantity > limit);
        }
    }

    private int getShopItemLimit(PlayerVO playerVO, ShopVO shopVO, ShopItemConfig shopItemConfig) {
        if(shopVO.isGuild()){
            GuildVO guildVO = playerVO.getGuild();
            if(guildVO != null){
                int[] limit = shopItemConfig.getLimit();
                if(limit == null || limit.length <= 0) return -1;
                if(limit.length <= guildVO.getLevel()){
                    return limit[limit.length - 1];
                }else{
                    return limit[guildVO.getLevel()];
                }
            }
            CodeShop.SHOP_REQUIRE_GUILD_VIP.throwException();
            return 0;
        }
        int[] limit = shopItemConfig.getLimit();
        if(limit == null || limit.length <= 0) return -1;
//        if(limit.length <= playerVO.getVip()){
//            return limit[limit.length - 1];
//        }else{
//            return limit[playerVO.getVip()];
//        }
        return 0;
    }

    /**
     * 查看商店数据
     *
     * @param player 玩家数据
     * @param config 商店配置
     * @return 商店数据
     * @throws LogicException
     */
    private ShopVO getShopVO(PlayerVO player, ShopConfig config) throws LogicException {
        // 查看商店数据存储类型
        if (config.getPlaceHolder() == 1) { // 系统商店
            // 查找商店
            return SystemVO.getInstance().getShopVOMap().get(config.getId());
        } else if (config.getPlaceHolder() == 2) {// 玩家商店
            // 查找商店
            return player.getShopVOMap().get(config.getId());
        } else {
            throw new LogicException(CodeShop.SHOP_UNKNOWN_PLACE_HOLDER.getCode());
        }
    }

  

    /**
     * 创建并添加商店数据
     *
     * @param playerVO 玩家数据
     * @param config   商店配置
     * @return 商店数据
     */
    private ShopVO createShopVO(PlayerVO playerVO, ShopConfig config) {
        //
        ShopVO shopVO = create(SystemVO.GAME_DB_UNIQUE_ID, config);
        // 查看商店数据存储类型
        if (config.getPlaceHolder() == 1) {
            // 系统商店
            SystemVO.getInstance().getShopVOMap().put(config.getId(), shopVO);
        } else if (config.getPlaceHolder() == 2) {
            // 玩家商店
            playerVO.getShopVOMap().put(config.getId(), shopVO);
        } else {
            throw new IllegalArgumentException("unknown shop save place : " + config.getPlaceHolder());
        }
        // 返回
        return shopVO;
    }

    /**
     * 刷新商店。
     * <p>
     * 需要检测是否达到刷新条件。
     * <p>
     * <span style="color:red;">注意:</span>
     * <p>
     * 如果逻辑处理线程是多线程, 系统商店的刷新可能会造成并发异常。
     *
     * @param playerVO 玩家数据
     * @param shopVO   商店数据
     */
    @Override
    public boolean systemRefresh(PlayerVO playerVO, ShopVO shopVO) throws LogicException {
        // 如果商店满足刷新条件
        // 还没有刷新过
        // 达到下次刷新时间
        if (shopVO.getRefreshTime() <= 0 || checkRefreshTime(shopVO.getRefreshTime(), shopVO.getCfg().getRefreshHours())) {
            // 刷新商品
            shopVO.setItemVOs(Enums.get(ShopRefreshRule.class, shopVO.getCfg().getRefreshRule()).refresh(playerVO, shopVO));
            // 重置刷新时间
            shopVO.setRefreshTime(System.currentTimeMillis());
            // 重置手动刷新次数
            shopVO.setManualRefreshTimes(0);
            // 更新当前版本号
            shopVO.setVersion(shopVO.getVersion() + 1);
            // 保存商店数据
            shopDAO.save(shopVO);
            // 如果是系统商店
            if (shopVO.isSystem()) {
                // 需要清除全服限购
                shopItemRecordDAO.delete(SystemVO.getInstance().getShopItemRecordVOMap().values());
                //
                SystemVO.getInstance().getShopItemRecordVOMap().clear();
            }
            // 数据刷新
            return true;
        }
        // 没有刷新
        return false;
    }

    /**
     * 手动刷新商店
     *
     * @param player 玩家数据
     * @param shopId 商店ID
     * @param cmd
     * @return 刷新结果
     * @throws LogicException
     */
    @Override
    public Map<?, ?> manualRefresh(PlayerVO player, @CParam("shop") int shopId, CMD cmd) throws LogicException {
        // 商店配置
        ShopConfig config = CacheConfig.getCfg(ShopConfig.class, shopId);
        // 如果商店不存在
        CodeShop.SHOP_NOT_FOUND.throwIfTrue(config == null);
        // 如果不能手动刷新
        CodeShop.SHOP_NOT_ALLOW_MANUAL_REFRESH.throwIfTrue(config.getMaxRefreshTimes() <= 0 || config.isSystem());
        // 商店数据
        ShopVO shopVO = getShopVO(player, config);
        // 如果商店不存在
        if (shopVO == null) shopVO = createShopVO(player, config);
        // 如果商店不存在
        CodeShop.SHOP_NOT_FOUND.throwIfTrue(shopVO == null);
        // 如果刷新次数超限
        CodeShop.SHOP_MAX_MANUAL_REFRESH.throwIfTrue(shopVO.getManualRefreshTimes() >= config.getMaxRefreshTimes());
        // 刷新消耗
        int refCost = getRefreshShopCost(shopVO, config);
        // 扣除消耗
        Map ret = GameUtil.createSimpleMap();
        SyncFuncType.ChangeCur.getFunc().checkAndExec(ret, cmd, player, new FuncParam(CurrencyType.TRE.getType(), -refCost));
        // 可以刷新
        shopVO.setItemVOs(Enums.get(ShopRefreshRule.class, shopVO.getCfg().getRefreshRule()).refresh(player, shopVO));
        shopVO.setManualRefreshTimes(shopVO.getManualRefreshTimes() + 1);
        // 保存商店数据
        shopDAO.save(shopVO);
        //
        EvtTypeShop.Refresh.happen(ret, cmd, player, EvtParamType.SHOP_CID.val(shopId), EvtParamType.SHOP_Type.val(config.getShopType()), EvtParamType.NUM.val(shopVO.getManualRefreshTimes() - shopVO.getManualRefreshTimes()));
        // 返回商店数据
        return MsgUtil.brmAll(MsgFactoryShop.instance().getShopInfoMsg(ret, player, shopVO), Command.Shop_Manual_Refresh);
    }

    private int getRefreshShopCost(ShopVO shopVO, ShopConfig config) {
        int refTimes = shopVO.getManualRefreshTimes();
        int[] refCost = config.getRefreshCosts();
        if(refTimes >= refCost.length){
            return refCost[refCost.length];
        }else{
            return refCost[refTimes];
        }
    }

    /**
     * 检测是否达到刷新时间
     *
     * @param lastRefreshTime 上一次刷新时间
     * @param refreshTime     需要刷新的时间点
     * @return 是否可以刷新
     */
    private boolean checkRefreshTime(long lastRefreshTime, int[] refreshTime) {
        // 如果没有刷新时间,不刷新
        if (ArrayUtils.isEmpty(refreshTime)) return false;
        // 当前日期
        Clock system = Clock.systemDefaultZone();
        // 未来时间直接返回false
        if (lastRefreshTime > system.millis())
            return false;

        LocalDateTime current = LocalDateTime.now(system);
        // 上一次刷新日期
        LocalDateTime last = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastRefreshTime), ZoneId.systemDefault());
        int compareTo = current.toLocalDate().compareTo(last.toLocalDate());
        if (compareTo > 0) {
            // 当前时间超过了刷新时间 直接刷新
            return current.getHour() >= refreshTime[0] ||
                    // 如果刷新时间间隔越过1天  直接刷新
                    current.minusDays(1).toLocalDate().isAfter(last.toLocalDate()) ||
                    //是昨天
                    last.getHour() < refreshTime[refreshTime.length - 1];
            // 如果是同一天
        } else if (compareTo == 0) {
            // 相等 同一天
            // 下一个刷新时间点
            int nextRefreshTime = -1;
            // 检测所有时间点是否已刷新
            for (int time : refreshTime) {
                // 刷新时间
                LocalTime localTime = LocalTime.of(time, 0, 0, 0);
                // 查找下一个刷新时间点
                if (last.toLocalTime().isBefore(localTime)) {
                    // 下个刷新点
                    nextRefreshTime = time;
                    // 查找结束
                    break;
                }
            }
            // 如果有下一个刷新时间点
            return nextRefreshTime > -1 && current.getHour() >= nextRefreshTime;
        } else {
            // 未来时间 不刷新
            return false;
        }
    }

    private ShopVO create(int player, ShopConfig config) {
        ShopVO shopVO = new ShopVO();
        shopVO.setPlayer(player);
        shopVO.setCid(config.getId());
        return shopVO;
    }

  

    /**
     * 销毁商店数据
     *
     * @param parent   消息
     * @param playerVO 玩家数据
     * @param type     需要销毁的商店类型
     * @return 删除结果
     */
    private Map destroyShop(Map parent, PlayerVO playerVO, ShopType type) {
        // 需要删除的商店数据
        ShopVO delete = null;
        // 查看玩家所有商店
        for (ShopVO shopVO : playerVO.getShopVOMap().values()) {
            // 如果满足条件
            if (shopVO.getCfg().getType() == type.getType()) {
                delete = shopVO;
                break;
            }
        }
        // 如果有商店需要删除
        if (delete != null) {
            // 消息
            parent = MsgFactoryShop.instance().getShopDeleteMessage(parent, delete);
            // 删除内存
            playerVO.getShopVOMap().remove(delete.getCid());
            // 删除DB
            shopDAO.delete(delete);
        }
        return parent;
    }

    public void setShopItemRecordBO(ShopItemRecordBO shopItemRecordBO) {
        this.shopItemRecordBO = shopItemRecordBO;
    }

    public void setPlayerBO(PlayerBO playerBO) {
        this.playerBO = playerBO;
    }
    
    
    @Override
    public EvtType[] getListenTypes() {
        return CONCERN_EVENTS;
    }

    @Override
    public Map listen(Map parent, CMD cmd, EvtHolder holder, GameEvent event) {
        Enum<?> o = event.getType();
        PlayerVO playerVO = (PlayerVO) holder;
        if (o instanceof EvtTypePlayer) {
            switch ((EvtTypePlayer) o) {
                case LeaveGuild:
                    // 如果玩家身上有公会任务
                    parent = destroyShop(parent, playerVO, ShopType.Guild);
                    break;
                case EnterGame:
                    // 查看玩家所在公会
                    GuildMemberVO memberVO = CacheGuild.getGuildMemberVO(playerVO.getId());
                    // 如果不在公会
                    if (memberVO == null || memberVO.getGuildID() <= 0)
                        parent = destroyShop(parent, playerVO, ShopType.Guild);
                    break;
            }
        }
        return parent;
    }

    
    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        // 玩家所有商店数据
        List<DBObj> shopDB = (List<DBObj>) data.get(DATA_SIGN_SHOP);
        //
        if (shopDB == null)
            return;
        for (DBObj dbObj : shopDB) {
            // 商店数据
            ShopVO shopVO = new ShopVO();
            // DB to Object
            shopVO.readFromDBObj(dbObj);
            //
            int before = shopVO.getItemVOs().size();
            // 查看商店所有商品
            shopVO.getItemVOs().removeIf(new Predicate<ShopItemVO>() {
                @Override
                public boolean test(ShopItemVO shopItemVO) {
                    return shopItemVO.getCfg() == null;
                }
            });
            // 玩家商店
            shopVO.setPlayer(player.getId());
            // 加入玩家数据
            player.getShopVOMap().put(shopVO.getCid(), shopVO);
            //
            if (before != shopVO.getItemVOs().size()) shopDAO.save(shopVO);
        }
    }
    
	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
	}
}
