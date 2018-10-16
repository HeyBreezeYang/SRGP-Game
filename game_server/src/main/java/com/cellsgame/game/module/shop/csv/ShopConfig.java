package com.cellsgame.game.module.shop.csv;

import java.util.Arrays;
import java.util.List;

import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.cache.Enums;
import com.cellsgame.game.module.func.FuncConfig;

/**
 * 商店配置数据。
 *
 * @author Yang
 */
public class ShopConfig extends BaseCfg {
    // 商店类型
    private int type;
    // 商店数据存储类型(决定刷新方式-立即OR判断)
    private int placeHolder;
    // 商店手动刷新最大次数
    private int maxRefreshTimes;
    // 商店手动刷新消耗
    private int[] refreshCosts;
    // 商店刷新时间
    private int[] refreshHours;
    // 商店刷新规则
    private String refreshRule;
    // 商店刷新规则参数
    private String[] refreshParams;

    public int getMaxRefreshTimes() {
        return maxRefreshTimes;
    }

    public void setMaxRefreshTimes(int maxRefreshTimes) {
        this.maxRefreshTimes = maxRefreshTimes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public ShopType getShopType(){
    	 ShopType shopType = Enums.get(ShopType.class, type);
    	 if(shopType == null)
    		 shopType = ShopType.Unsign;
    	return shopType;
    }

    public int getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(int placeHolder) {
        this.placeHolder = placeHolder;
    }

    public int[] getRefreshHours() {
        return refreshHours;
    }

    public int[] getRefreshCosts() {
        return refreshCosts;
    }

    public void setRefreshCosts(int[] refreshCosts) {
        this.refreshCosts = refreshCosts;
    }

    public void setRefreshHours(int[] refreshHours) {
        this.refreshHours = refreshHours;
        Arrays.sort(this.refreshHours);
    }

    public String getRefreshRule() {
        return refreshRule;
    }

    public void setRefreshRule(String refreshRule) {
        this.refreshRule = refreshRule;
    }

    public String[] getRefreshParams() {
        return refreshParams;
    }

    public void setRefreshParams(String[] refreshParams) {
        this.refreshParams = refreshParams;
    }

    public boolean isSystem() {
        return getPlaceHolder() == 1;
    }

    public boolean isGuild() {
        return this.type == ShopType.Guild.getType();
    }

   
}
