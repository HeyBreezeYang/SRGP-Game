package com.cellsgame.game.module.shop.csv;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

import java.util.List;

/**
 * File Description.
 *
 * @author Yang
 */
public class SimpleShopItemConfig extends BaseCfg {
    // 出售道具ID
    private int goodsId;
    // 购买动作
    private FuncConfig purchase;
    // 商品消耗
    private List<FuncConfig> costs;
    // 购买等级要求
    private int purchaseLv;
    // 商店物品类型
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPurchaseLv() {
        return purchaseLv;
    }

    public void setPurchaseLv(int purchaseLv) {
        this.purchaseLv = purchaseLv;
    }

    public int getGoodsId() {
        return  ` `;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public FuncConfig getPurchase() {
        return purchase;
    }

    public void setPurchase(FuncConfig purchase) {
        this.purchase = purchase;
    }

    public List<FuncConfig> getCosts() {
        return costs;
    }

    public void setCosts(List<FuncConfig> costs) {
        this.costs = costs;
    }
}
