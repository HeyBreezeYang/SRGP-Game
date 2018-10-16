package com.cellsgame.game.module.pay;

import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.pay.bo.OrderBO;
import com.cellsgame.game.module.pay.csv.OrderItemConfig;
import com.cellsgame.game.module.pay.csv.adjuster.OrderItemAdjuster;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo<OrderBO> {
    @Override
    public Module<OrderBO> getModuleInfo() {
        return Module.createModule(OrderBO.class)
                .jsonHead()
                .loadCSVFile()
                .load("Recharge.csv", OrderItemConfig.class)
                .adjustConfig()
                .regCusAdjuster(new OrderItemAdjuster())
                .build();
    }
}