package com.cellsgame.game.cmd.console;

import java.lang.reflect.Method;

import com.cellsgame.common.util.cmd.console.AConsoleCmd;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

/**
 * @author Aly on  2016-09-13.
 */
public class LogLevel extends AConsoleCmd {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(LogLevel.class);

    @Override
    public Object getName() {
        return "logLevel";
    }

    @Override
    public Object exe(String cmd, String[] param) throws Exception {

        String loggerName = param[1];
        String level = param[2];
        Level lv = Level.valueOf(level);
        Logger logger = LogManager.getLogger(loggerName);
        Level oldLevl = logger.getLevel();
        Method setLevel = ReflectionUtils.findMethod(logger.getClass(), "setLevel", Level.class);
        setLevel.setAccessible(true);
        setLevel.invoke(logger, lv);

        log.warn(" reset logger  -->[{}]  Level  --> old:{} --> new:{}", logger.getName(), oldLevl, logger.getLevel());
        return null;
    }
}
