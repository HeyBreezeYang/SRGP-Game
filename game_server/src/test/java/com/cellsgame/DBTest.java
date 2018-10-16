package com.cellsgame;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.http.HttpServer;
import com.cellsgame.conc.thread.ESManager;
import com.cellsgame.game.cache.CachePlayerDBID;
import com.cellsgame.game.cache.CacheServerState;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.context.BootstrapConfig;
import com.cellsgame.game.context.GameConfig;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.module.load.ModuleLoader;
import com.cellsgame.game.core.socket.SocketServer;
import com.cellsgame.game.module.BuildPlayerProcess;
import com.cellsgame.game.module.chat.thread.TaskManager;
import com.cellsgame.game.module.hero.vo.HeroVO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.game.quartz.DataQuartzJob;
import com.cellsgame.orm.BaseDAO;
import org.junit.Assert;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class DBTest {
    private static DBTest bootstrap = new DBTest();

    private static final Logger log = LoggerFactory.getLogger(DBTest.class);

    private BootstrapConfig bootstrapConfig;

    public PlayerVO createPlayer(String playerName, int img) {
        BaseDAO<PlayerVO> playerDAO = SpringBeanFactory.getBean("playerDAO");
        BuildPlayerProcess playerBuilder = SpringBeanFactory.getBean("playerBuilder");
        PlayerVO player = new PlayerVO();
        player.setAccountId("accountId");
        player.setServerId(20180924);
        player.setName(playerName);
        player.writeToDBObj();

        playerDAO.save(player);
        playerBuilder.buildAsCreate(player, null);

        CachePlayer.addPlayer(player);
        CachePlayerBase.addBaseInfo(player);

        return player;
    }

    public void removePlayer(Integer pid) {
        BaseDAO<PlayerVO> playerDAO = SpringBeanFactory.getBean("playerDAO");
        PlayerVO vo = CachePlayer.getPlayerByPid(pid);
        if(null != vo) playerDAO.delete(vo);
    }

    public HeroVO createHero() {
        return null;
    }

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

    public static void main(String[] args) throws Exception {
        ModuleLoader.loadAllModule("com.cellsgame.game.module");

        bootstrap.loadBootstrapConfig();
//        bootstrap.loadAllowLoginIp();
        // TODO http server module init commented by luojf945
//        bootstrap.httpServerStart();
        bootstrap.init();

        bootstrap.start();

        PlayerVO player = bootstrap.createPlayer("player-01-" + System.currentTimeMillis(), 0);

        bootstrap.removePlayer(player.getId());

        bootstrap.removePlayer(1);
        bootstrap.removePlayer(2);
        bootstrap.removePlayer(3);
        bootstrap.removePlayer(4);
        bootstrap.removePlayer(5);
        bootstrap.removePlayer(6);
        bootstrap.removePlayer(7);
        bootstrap.removePlayer(8);
        bootstrap.removePlayer(9);

        PlayerVO cachePlayer = CachePlayer.getPlayerByPid(player.getId());

        Assert.assertTrue(player == cachePlayer);

        PlayerVO firstPlayer = CachePlayer.getPlayerByPid(1);


        Assert.assertTrue(null == firstPlayer);

        System.out.println("测试完成");
    }
}
