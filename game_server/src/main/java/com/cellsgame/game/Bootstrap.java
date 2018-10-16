package com.cellsgame.game;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import java.util.Date;
import java.text.SimpleDateFormat;

import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.common.util.cmd.ConsoleCmdRunner;
import com.cellsgame.common.util.http.HttpServer;
import com.cellsgame.conc.thread.ESManager;
import com.cellsgame.game.cache.CacheIP;
import com.cellsgame.game.cache.CachePlayerDBID;
import com.cellsgame.game.cache.CacheServerState;
import com.cellsgame.game.cons.ModuleID;
import com.cellsgame.game.context.BootstrapConfig;
import com.cellsgame.game.context.GameConfig;
import com.cellsgame.game.core.config.GameSysConfig;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.module.load.ModuleLoader;
import com.cellsgame.game.core.mq.MQClient;
import com.cellsgame.game.core.socket.SocketServer;
import com.cellsgame.game.module.DailyResetable;
import com.cellsgame.game.module.chat.cache.CacheChat;
import com.cellsgame.game.module.chat.thread.TaskManager;
import com.cellsgame.game.module.pay.bo.impl.OrderBOImpl;
import com.cellsgame.game.quartz.DataQuartzJob;
import com.cellsgame.game.servlet.*;

import com.cellsgame.game.util.AllowLoginIpUtil;
import com.cellsgame.game.util.LoadBootstrapConfigUtil;
import com.cellsgame.pay.PayServer;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.LoggerFactory;


public class Bootstrap {
    public static final AtomicBoolean allowPlayerIn = new AtomicBoolean();
    private static org.slf4j.Logger log = LoggerFactory.getLogger(Bootstrap.class);
    private static Bootstrap bootstrap = new Bootstrap();

    private final AtomicBoolean running = new AtomicBoolean(false);
    private BootstrapConfig bootstrapConfig;
    private HttpServer http;
    private PayServer pay;

    public static Bootstrap getInstance() {
        return bootstrap;
    }

    public static void main(String[] args) throws Exception {
        log.info("main start -------------");
        // 在Spring初始化之前
        ModuleLoader.loadAllModule(GameSysConfig.getString("game.core.module.pkg", "com.cellsgame"));
        bootstrap.loadBootstrapConfig();
//        bootstrap.loadAllowLoginIp();
        // TODO http server module init commented by luojf945
        bootstrap.httpServerStart();
        bootstrap.init();
        
        //充值服务启动必须在配置档初始化完成以后再启动
        // TODO pay module init commented by luojf945
//        try{
//            bootstrap.payStart();
//        }catch(Exception e){
//        	e.printStackTrace();
//        }
        bootstrap.start();
        bootstrap.running.set(true);
        allowPlayerIn.set(true);
        // 控制台命令解析
        String consoleCmdPkg = GameSysConfig.getString("game.core.console.cmd.pkg", "");
        ConsoleCmdRunner runner = new ConsoleCmdRunner(bootstrap.running, consoleCmdPkg, bootstrap::stop);
        runner.run();
    }

    private void loadAllowLoginIp() {
        try {
            List<Map> lst = AllowLoginIpUtil.doPost(GameConfig.getConfig().getAllowLoginIp());
            for (Map map : lst) {
                String allowIp = map.get("ip").toString();
                CacheIP.allow.add(allowIp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void payStart() throws Exception {
        OrderBOImpl orderBO = SpringBeanFactory.getBean(OrderBOImpl.class);
        String host = bootstrap.bootstrapConfig.getPayHost() == null || bootstrap.bootstrapConfig.getPayHost().isEmpty() ? "0.0.0.0" : bootstrap.bootstrapConfig.getPayHost();
        int port = bootstrap.bootstrapConfig.getPayPort() <= 0 ? bootstrap.bootstrapConfig.getHttpPort() + 1000 : bootstrap.bootstrapConfig.getPayPort();
         pay = PayServer.start(GameConfig.getConfig().getPayServerUrl(),
                 host,
                 port,
                String.valueOf(GameConfig.getConfig().getGameServerId()),
                GameConfig.getConfig().getAppId(),
                GameConfig.getConfig().getAppCharacter(),
                GameConfig.getConfig().getPayPrivateKey(),
                orderBO);
    }

    private void httpServerStart() {
        log.warn("-->http server host : {}, port : {}", bootstrap.bootstrapConfig.getHttpHost(), bootstrap.bootstrapConfig.getHttpPort());
        http = new HttpServer("game", new InetSocketAddress(bootstrap.bootstrapConfig.getHttpPort()));
        http.addServlet("loadPlayer", new LoadPlayerServlet());
        http.addServlet("getPlayerBaseInfo", new GetPlayerBaseInfoServlet());
        http.addServlet("notify", new NotifyServlet());
        http.addServlet("releaseActivity", new ReleaseActivityServlet());
        http.addServlet("deleteActivity", new DeleteActivityServlet());
        http.addServlet("queryActivity", new QueryActivityServlet());
        http.addServlet("mail", new MailServlet());
        http.addServlet("stateChange", new GameStateChangeServlet());
        http.addServlet("getState", new GetGameStateServlet());
        http.addServlet("onlineSize", new GetOnlineSizeServlet());
        http.addServlet("setPlayerState", new ChangePlayerStateServlet(false));
        http.addServlet("resetPlayerState", new ChangePlayerStateServlet(true));
        http.addServlet("reissueOrder", new ReissuePayFailOrderServlet());
        http.addServlet("createStore", new CreateStoreServlet());
        http.addServlet("deleteStore", new DeleteStoreServlet());
        http.addServlet("queryStore", new QueryStoreServlet());
        http.addServlet("updateStore", new UpdateStoreServlet());
        http.addServlet("getRank", new GetRankServlet());
        http.addServlet("changeCurrency", new ChangeCurrencyServlet());
        http.addServlet("changeGoods", new ChangeGoodsServlet());
        http.addServlet("updateAllowLoginIp", new UpdateAllowLoginIpServlet());
        http.start();
        log.warn("-->http server已启动");
    }
//	TODO commented by luojf945
//    private void loadBootstrapConfig() throws Exception {
//        LoadBootstrapConfigUtil.LoadConfigResponse response = LoadBootstrapConfigUtil.doPost(GameConfig.getConfig().getBootstrapConfigUrl(), String.valueOf(GameConfig.getConfig().getGameServerId()));
//        if(response == null) throw  new RuntimeException("loadBootstrapConfig error ==> " + " serverId : " + GameConfig.getConfig().getGameServerId());
//        bootstrapConfig = new BootstrapConfig();
//        bootstrapConfig.setGameLogicID(response.getLogicID());
//        bootstrapConfig.setHttpHost(response.getServerIP());
//        bootstrapConfig.setHttpPort(response.getHttpPort());
//        bootstrapConfig.setGameHost(response.getServerIP());
//        bootstrapConfig.setGamePort(response.getServerPort());
//        bootstrapConfig.setPayHost(response.getExtranetIP());
//        bootstrapConfig.setPayPort(response.getDeliverPort());
//        bootstrapConfig.setServerOpenTime(response.getOpenTime());
//        CacheServerState.STATE = response.getState();
//        //初始化LogicID
//        GameConfig.getConfig().setLogicID(bootstrapConfig.getGameLogicID());
//    }

// 	TODO added by luojf945 临时
    private void loadBootstrapConfig() throws Exception {
        bootstrapConfig = new BootstrapConfig();
        bootstrapConfig.setGameLogicID(1);
        bootstrapConfig.setHttpHost("192.168.10.193");
        bootstrapConfig.setHttpPort(7001);
        bootstrapConfig.setGameHost("192.168.10.193");
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

        // 初始化 GM命令
        CacheChat.initCMD(GameSysConfig.getString("game.module.chat.cmd.pkg", ""));

        // 初始化数据存储
        DataQuartzJob.init();

        // Disruptor 初始化
        Dispatch.init();
        // 初始化数据线城池
        SpringBeanFactory.getBean(ESManager.class).init();
        
        // TODO mq module init commented by luojf945
//        //初始化MQ连接
//        MQClient.start();
        
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
            
            Map<String, DailyResetable> resetables = SpringBeanFactory.getBeanByType(DailyResetable.class);
            Collection<DailyResetable> values = resetables.values();
            for (DailyResetable a : values) {
				System.out.println(a.getClass());
			}
            log.warn("-->quartz已启动");
        });
       
    }


    private void start() {
        SocketServer.start(String.valueOf(GameConfig.getConfig().getGameServerId()),bootstrapConfig.getGameHost(), bootstrapConfig.getGamePort());
        log.warn("-->socket已启动");
    }

    private void stop() {
        SocketServer.stop();
        if(http != null) http.shutdown();
        if(pay != null) pay.stop();
        // 系统关闭 模块
        ModuleLoader.moduleOnShutdown();

        Scheduler scheduler = SpringBeanFactory.getBean("scheduler");
        try {
            scheduler.shutdown(true);
        } catch (SchedulerException e) {
            log.error("", e);
        }

        // 关闭逻辑
        Dispatch.destroy();
//        MQClient.stop();
        TaskManager.shutdown();
        DataQuartzJob dataQuartzJob = SpringBeanFactory.getBean("dataQuartzJob");
        dataQuartzJob.execute();

        SpringBeanFactory.getBean(ESManager.class).stop();
    }

    public BootstrapConfig getBootstrapConfig() {
        return bootstrapConfig;
    }
}
