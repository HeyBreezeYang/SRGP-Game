package com.cellsgame.game.cmd.console;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.cmd.console.AConsoleCmd;
import com.cellsgame.game.quartz.DataQuartzJob;

/**
 * @author Aly on  2016-09-01.
 */
public class SaveAll extends AConsoleCmd {
    @Override
    public Object getName() {
        return "saveAll";
    }

    @Override
    public Object exe(String cmd, String[] param) throws Exception {
        SpringBeanFactory.getBean(DataQuartzJob.class).execute();
        return null;
    }
}
