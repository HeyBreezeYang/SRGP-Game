package com.cellsgame.game.cmd.console;

import com.cellsgame.common.util.cmd.console.AConsoleCmd;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.core.cfg.core.CfgService;
import com.cellsgame.game.core.module.load.ModuleLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Aly on  2016-07-16.
 */
public class ReloadCfg extends AConsoleCmd {
    private static final Logger log = LoggerFactory.getLogger(ReloadCfg.class);


    @Override
    public Object getName() {
        return "loadcfg";
    }

    @Override
    public Object exe(String cmd, String[] param) throws Exception {
        try {
            CfgService.loadAllCfg(ModuleID.System, true, ModuleLoader.moduleMap);
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }
}
