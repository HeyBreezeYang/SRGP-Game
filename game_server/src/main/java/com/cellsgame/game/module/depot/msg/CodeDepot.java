package com.cellsgame.game.module.depot.msg;

import com.cellsgame.game.cons.ICode;
import com.cellsgame.game.cons.ModuleID;

/**
 * @see ModuleID#Depot
 */
public enum CodeDepot implements ICode {
        Depot_GoodsNumNotEnough(1),             // 物品数量不足
    Depot_Contacts_Minus(2),                // 人脉不足
    Depot_UseLevelNotEnough(3),             // 使用等级不足
    Depot_UseVipLevelNotEnough(4),          // 使用等级不足
    Depot_Treasure_Minus(5),                // 元宝不足
    Depot_Goods_Type_Error(6),              // 物品类型错误
    Depot_Money_Minus(7),                   // 金币不足
    Depot_Chance_Minus(8),                  // 机会不足
    Depot_Cny_Type_Error(9),                // 货币类型错误
    Depot_Cant_Sell(10),                    // 物品奖励为空, 不能出售
    Depot_Capacity_max(11),                 // 达到背包上限
    Depot_Already_In_Depot(12),             // 已经放入背包
    Depot_Goods_Not_Found(13),              // 未找到物品配置
    Depot_Society_Exp_Minus(14)             // 经验不足
    
    ;

    private int code;

    CodeDepot(int code) {
        this.code = code;
    }

    @Override
    public int getModule() {
        return ModuleID.Depot;
    }

    @Override
    public int getCode() {
        return code;
    }


}	
