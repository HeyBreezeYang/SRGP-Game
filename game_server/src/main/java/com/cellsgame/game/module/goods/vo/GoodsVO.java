package com.cellsgame.game.module.goods.vo;

import java.util.Map;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.core.cfg.base.CfgSaveVO;
import com.cellsgame.orm.enhanced.annotation.Save;

/**
 * @apiNote 需要自己手动设置goodIX
 * @see GoodsVO#toInfoMap()        请查看注意事项
 * @see GoodsVO#goodsIx
 */
public abstract class GoodsVO<T extends BaseCfg> extends CfgSaveVO<T> {

    /**
     * 配置档ID CSV文件
     */
    private int cid;
    
    /**
     * DBID
     */
    private int id;

    /**
     * PID 丛属ID： 当前道具属于哪个背包
     */
    private int pid;
    /**
     * 物品IX （索引index)
     */
    @Save(ix = 1)
    private int goodsIx;

    public GoodsVO(Class<T> cfgClass) {
        super(cfgClass);
    }

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{pid};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys.length > 0)
            pid = (int) relationKeys[0];
    }

    @Override
    protected void init() {
        id = 0;
        pid = 0;
    }

    @Override
    public Integer getCid() {
        return cid;
    }

    @Override
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsIx() {
        return goodsIx;
    }

    public void setGoodsIx(int goodsIx) {
        this.goodsIx = goodsIx;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }


    /**
     * 必须包含两个字段  方便客户端统一处理
     *
     * @see com.cellsgame.game.constant.ToClientResultKey#TO_CLIENT_CID                 物品CID
     * @see com.cellsgame.game.constant.ToClientResultKey#TO_CLIENT_PARAM_GOODS_IX      物品IX
     */
    public abstract Map toInfoMap();

}
