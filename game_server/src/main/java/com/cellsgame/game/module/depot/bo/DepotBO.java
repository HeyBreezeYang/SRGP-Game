package com.cellsgame.game.module.depot.bo;

import java.util.Map;

import com.cellsgame.game.core.event.StaticEvtListener;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.depot.cons.CurrencyType;
import com.cellsgame.game.module.depot.vo.DepotVO;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

//@AModule(ModuleID.Depot)  业务对象BO
@SuppressWarnings("rawtypes")
public interface DepotBO extends StaticEvtListener, IBuildData {
    /**
     * @param parent
     * @param pvo
     * @param cid
     * @param num
     * @param cmd
     * @return
     * @throws LogicException
     */
    Map changeGoodsNum(Map parent, PlayerVO pvo, int cid, int num, CMD cmd) throws LogicException;

    /**
     * 根据货币类型修改货币值
     *
     * @param parent  消息
     * @param pvo     玩家数据
     * @param type    货币类型
     * @param cur     偏移量，可以为正负值
     * @param save    是否立即保险
     * @param overMax 是否可以超过上限
     * @param pay 是否充值
     * @param cmd
     * @return
     * @throws LogicException
     */
    Map changeCurByType(Map parent, PlayerVO pvo, CurrencyType type, long cur, boolean save, boolean overMax, CMD cmd, boolean pay) throws LogicException;

    /**
     * 创建默认背包
     */

    void createDefaultDepot(PlayerVO player, CMD cmd);

    /**
     * 查看可堆叠物品数量
     *  <p>
     *      此方法根据收集类型编号
     *  </p>
     * @param playerVO 玩家数据
     * @param idOrCollectType  物品ID
     * @return 物品数量
     */
    int getGoodsQuantity(PlayerVO playerVO, int idOrCollectType);

    void checkGoodsEnough(PlayerVO pvo, int gid, int num) throws LogicException;

    void checkGoodsEnough(PlayerVO pvo, Map goodsMap) throws LogicException;

    GoodsVO getGoodsEntity(PlayerVO pvo, int gix);

    <T extends GoodsVO> T getGoodsEntity(PlayerVO pvo, Class<T> cls, int gix);

    /**
     * 添加不可堆叠物品
     *
     * @param parent 消息体
     * @param pvo    玩家数据
     * @param gvo    物品数量
     * @param cmd
     * @return 添加结果
     * @throws LogicException
     */
    Map addGoodsEntity(Map parent, PlayerVO pvo, GoodsVO gvo, CMD cmd) throws LogicException;

    /**
     * 删除背包物品数据，不会销毁物品数据
     * <p>
     * 具体数据的销毁必须通过 {@link DepotBO#removeGoodsEntity(Map, PlayerVO, GoodsVO, boolean, CMD)}, 否则将会造成不可堆叠物品计数异常
     * <p>
     * 物品数据类型:
     * <p>
     *
     * @param parent     消息
     * @param pvo        玩家数据
     * @param entityType 物品背包类型
     * @param gix        物品唯一ID
     * @param <T>        物品数据类型
     * @return 被删除的物品数据
     * @throws LogicException
     */
    <T extends GoodsVO> T removeGoodsEntity(Map parent, PlayerVO pvo, Class<T> entityType, int gix) throws LogicException;

    /**
     * 删除物品数据
     * <p>
     * 是否需要删除数据，由参数{destroyGoods}决定
     *
     * @param parent       消息
     * @param pvo          玩家数据
     * @param gvo          物品数据
     * @param destroyGoods 是否需要销毁物品数据
     * @param cmd
     * @return 删除消息
     */
    Map removeGoodsEntity(Map parent, PlayerVO pvo, GoodsVO gvo, boolean destroyGoods, CMD cmd);


    void checkCurEnough(PlayerVO pvo, CurrencyType type, long cur) throws LogicException;

    /**
     * 查看货币数量
     *
     * @param pvo  玩家数据
     * @param type 货币类型
     * @return 货币数量
     */
    long getCurByType(PlayerVO pvo, CurrencyType type);

    /**
     * 查看货币数量
     *
     * @param pvo  玩家数据
     * @param type 货币类型
     * @return 货币数量
     */
    long getCurByType(PlayerVO pvo, int type);

    /**
     * 查看背包是否有足够的空间存放物品
     *
     * @param cls     物品类型
     * @param depotVO 背包
     * @param offset  偏移量
     * @return 是否还有足够空间
     */
    <T extends GoodsVO> boolean isCapacityEnough(Class<T> cls, DepotVO depotVO, int offset);


    <T extends GoodsVO> boolean checkSpace(Class<T> cls, DepotVO depotVO);
}
