package com.cellsgame.game.module.store;


import com.cellsgame.game.core.module.IModuleInfo;
import com.cellsgame.game.core.module.config.Module;
import com.cellsgame.game.module.store.bo.StoreBO;

/**
 * @author Aly on  2016-10-24.
 */
public class ModuleInfo implements IModuleInfo {
    @Override
    public Module getModuleInfo() {
        return Module.createModule(StoreBO.class)
                .jsonHead()
                .loadCSVFile()

                .adjustConfig()
                .checker()
                .listener()
                .onStartup(StoreBO::init)
                .build();
    }
}