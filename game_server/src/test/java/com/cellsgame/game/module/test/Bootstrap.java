package com.cellsgame.game.module.test;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.conc.thread.ESManager;
import com.cellsgame.game.cache.CachePlayerDBID;
import com.cellsgame.game.cache.CacheServerState;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.context.BootstrapConfig;
import com.cellsgame.game.context.GameConfig;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.module.load.ModuleLoader;
import com.cellsgame.game.core.socket.SocketServer;
import com.cellsgame.game.module.chat.thread.TaskManager;
import com.cellsgame.game.quartz.DataQuartzJob;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class Bootstrap {
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    private final AtomicBoolean running = new AtomicBoolean(false);

    private BootstrapConfig bootstrapConfig;

    private void loadBootstrapConfig() throws Exception {
        bootstrapConfig = new BootstrapConfig();
        bootstrapConfig.setGameLogicID(1);
        bootstrapConfig.setHttpHost("192.168.10.165");
        bootstrapConfig.setHttpPort(7001);
        bootstrapConfig.setGameHost("192.168.10.165");
        bootstrapConfig.setGamePort(7002);
//        bootstrapConfig.setPayHost(response.getExtranetIP());
//        bootstrapConfig.setPayPort(response.getDeliverPort());

        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(d);

        bootstrapConfig.setServerOpenTime(dateNowStr);
        CacheServerState.STATE = 1;
        //初始化LogicID
        GameConfig.getConfig().setLogicID(bootstrapConfig.getGameLogicID());
    }

    private void init() throws Exception {

        // 加载所有配置档
        ModuleLoader.loadConfigs(ModuleID.System);

        //加载玩家ID
        CachePlayerDBID.init();

        // 初始化数据存储
        DataQuartzJob.init();

        // Disruptor 初始化
        Dispatch.init();
        // 初始化数据线城池
        SpringBeanFactory.getBean(ESManager.class).init();

        TaskManager.init();
        Dispatch.dispatchGameLogic(()->{
            //
            // 系统启动初始化 模块
            ModuleLoader.initModuleOnStartup();
            // 更新排行榜 显示
            log.warn("-->dispatch已启动");
            Scheduler scheduler = SpringBeanFactory.getBean("scheduler");
            try {
                //
                scheduler.start();
            } catch (SchedulerException e) {
                log.error("", e);
            }
            log.warn("-->quartz已启动");
        });

    }

    private void start() {
        SocketServer.start(String.valueOf(GameConfig.getConfig().getGameServerId()),bootstrapConfig.getGameHost(), bootstrapConfig.getGamePort());
        log.warn("-->socket已启动");
    }

    public Bootstrap() throws Exception {
        ModuleLoader.loadAllModule("com.cellsgame.game.module");

        loadBootstrapConfig();

        init();

        start();

        running.set(true);
    }
}
