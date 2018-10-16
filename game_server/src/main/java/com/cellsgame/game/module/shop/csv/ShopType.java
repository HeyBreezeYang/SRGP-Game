/**
 * 
 */
package com.cellsgame.game.module.shop.csv;

/**
 * @author peterveron
 *
 */
public enum ShopType{
	Unsign(-1),
    Special(0),         //0: 特殊商城
    System(1),          //1: 系统商城，
    Guild(2),           //2: 公会，
    Party(3),          //3: 宴会
    Boss(4),          //4: BOSS
    Score_WB(5),          //5  挖宝积分商店
    Goods_WB(6),          //6  挖宝道具商店
    Score_FS(7),          //7  大丰收积分商店
    Goods_FS(8),          //8  大丰收道具商店
    Score_JF(9),          //9  剿土匪积分商店
    Goods_JF(10),          //10  剿土匪道具商店
    Score_ZD(11),          //11  抓壮丁积分商店
    Goods_ZD(12),          //12  抓壮丁道具商店
    Score_YH(13),          //13  铲除妖后积分商店
    Goods_YH(14),          //14  铲除妖后道具商店
    ;
    private int type;

    ShopType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}