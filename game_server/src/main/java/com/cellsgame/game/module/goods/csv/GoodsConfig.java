package com.cellsgame.game.module.goods.csv;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.func.FuncConfig;

/**
 * 物品配置档
 */
public class GoodsConfig extends BaseCfg implements QuestCollectible {
    /**
     * 物品类型
     */
    private int goodsType;
    /**
     * 收集类型{collectType,int}(num,collectType)
     */
    private int collectType;

    // 使用模式
    private int mode;

    // 绑定模式
    private int bind;

    // 价格类型
    private int current;

    // 价格
    private int price;

    // 道具类型
    private int goods_type;
    // 道具效果值
    private int goods_value;
    /**
     * 品质
     * */
    private int quality;

    /**
     * 兑换系统类型
     * */
    private int convertType;

    /**
     * 收集类型
     *
     * @return
     */
    @Override
    public int getCollectType() {
        return collectType;
    }

    public void setCollectType(int collectType) {
        this.collectType = collectType;
    }

    public int getGoodsType() {
        return goodsType;
    }

    private void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getConvertType() {
        return convertType;
    }

    public void setConvertType(int convertType) {
        this.convertType = convertType;
    }

    public static void main(String[] strs){
        System.out.println(Long.MAX_VALUE);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
    }

    public int getGoods_value() {
        return goods_value;
    }

    public void setGoods_value(int goods_value) {
        this.goods_value = goods_value;
    }
}
