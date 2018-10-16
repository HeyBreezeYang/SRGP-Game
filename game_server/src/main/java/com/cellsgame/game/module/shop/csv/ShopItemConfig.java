package com.cellsgame.game.module.shop.csv;

import java.util.Date;

import com.cellsgame.game.module.RandomObj;

/**
 * 商品配置数据。
 * <p>
 * 包括刷新规则所需要的参数配置。
 *
 * @author Yang
 */
public class ShopItemConfig extends SimpleShopItemConfig implements Comparable<ShopItemConfig>, RandomObj {
    // 每个玩家的限购数量
    private int[] limit;
    // 标签
    private int label;
    // 所属商品组
    private int group;
    // 所在商品组权重
    private int weight;
    // 可见等级要求
    private int visibleLv;
    // 购买公会等级要求
    private int purchaseGuildLevel;
    // 购买VIP要求
    private int purchaseVip;
    // 刷新规则匹配的VIP要求
    private int refreshRequireVip;
    // 刷新规则匹配的等级要求{refreshRequireLv,ints}
    private int[] refreshRequireLv;
    // 刷新规则匹配的公会等级要求{refreshRequireGuildLv,ints}
    private int[] refreshRequireGuildLv;

    public int[] getRefreshRequireGuildLv() {
        return refreshRequireGuildLv;
    }

    public void setRefreshRequireGuildLv(int[] refreshRequireGuildLv) {
        this.refreshRequireGuildLv = refreshRequireGuildLv;
    }

    public int[] getRefreshRequireLv() {
        return refreshRequireLv;
    }

    public void setRefreshRequireLv(int[] refreshRequireLv) {
        this.refreshRequireLv = refreshRequireLv;
    }

    public int[] getLimit() {
        return limit;
    }

    public void setLimit(int[] limit) {
        this.limit = limit;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVisibleLv() {
        return visibleLv;
    }

    public void setVisibleLv(int visibleLv) {
        this.visibleLv = visibleLv;
    }

    public int getPurchaseVip() {
        return purchaseVip;
    }

    public void setPurchaseVip(int purchaseVip) {
        this.purchaseVip = purchaseVip;
    }

    public int getRefreshRequireVip() {
        return refreshRequireVip;
    }

    public void setRefreshRequireVip(int refreshRequireVip) {
        this.refreshRequireVip = refreshRequireVip;
    }

    public int getPurchaseGuildLevel() {
        return purchaseGuildLevel;
    }

    public void setPurchaseGuildLevel(int purchaseGuildLevel) {
        this.purchaseGuildLevel = purchaseGuildLevel;
    }

    @Override
    public int compareTo(ShopItemConfig o) {
        if (o == null || this == o) return 0;
        int diff = o.weight - this.weight;
        if(diff == 0) return o.getId() - this.getId();
        return diff;
    }
}
