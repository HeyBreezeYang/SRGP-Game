package com.cellsgame.game.module;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.conc.thread.ESManager;
import com.cellsgame.game.context.MessageController;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.CatchRunnable;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.ChainLoadFinisher;
import com.cellsgame.orm.DBObj;
import com.cellsgame.orm.LoadDBObjChainJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoadPlayerJobFactory {
    private static final Logger log = LoggerFactory.getLogger(LoadPlayerJobFactory.class);
    private int playerId;

    private int serverId;

    private String accountId;

    private LoadDBObjChainJob playerLoadJob;

    private Map<LoadDBObjChainJob, Integer> playerDataLoadJob;

    private ChainLoadFinisher finisher;

    private ESManager threadManager;

    public LoadPlayerJobFactory() {
    }

    public LoadPlayerJobFactory(int playerId) {
        this.playerId = playerId;
    }

    public LoadPlayerJobFactory(int serverId, String accountId) {
        this.accountId = accountId;
        this.serverId = serverId;
    }

    @SuppressWarnings("unchecked")
    public static void loadByServerIdAndAccountId(String accountId, Integer serverId, ChainLoadFinisher finisherJob) {
        LoadPlayerJobFactory factory = (LoadPlayerJobFactory) SpringBeanFactory
                .getBean("loadPlayerJobFactory", serverId, accountId);
        factory.setFinisher(finisherJob);
        LoadDBObjChainJob cljob = factory.createLoadByServerIdAndUIDJob();
        cljob.load((ConcurrentHashMap) GameUtil.createMap());
    }

    @SuppressWarnings("unchecked")
    public static void loadByPlayerId(int playerId, ChainLoadFinisher finisherJob) {
        LoadPlayerJobFactory factory = (LoadPlayerJobFactory) SpringBeanFactory
                .getBean("loadPlayerJobFactory", playerId);
        factory.setFinisher(finisherJob);
        LoadDBObjChainJob cljob = factory.createLoadByPidJob();
        cljob.load((ConcurrentHashMap) GameUtil.createMap());
    }
    
  

    public LoadDBObjChainJob createLoadByPidJob() {
        playerLoadJob.setPrimaryKey(playerId);
        LoadDBObjChainJob parentJob = playerLoadJob;
        Set<Entry<LoadDBObjChainJob, Integer>> otherDataLoaders = playerDataLoadJob.entrySet();
        for (Iterator<Entry<LoadDBObjChainJob, Integer>> i = otherDataLoaders.iterator(); i.hasNext(); ) {
            Entry<LoadDBObjChainJob, Integer> e = i.next();
            LoadDBObjChainJob job = e.getKey();
            Integer relkeyIx = e.getValue();
            job.setRelKeyIx(relkeyIx);
            job.setRelKey(playerId);
            if (parentJob != null) {
                parentJob.setNext(job);
            }
            parentJob = job;
            if (!i.hasNext()) {
                job.setFinisher(finisher);
            }
        }
        return playerLoadJob;
    }

    @SuppressWarnings("unchecked")
    public LoadDBObjChainJob createLoadByServerIdAndUIDJob() {
        playerLoadJob.setRelKeyIx(0);
        playerLoadJob.setRelKey(accountId);
        final LoadDBObjChainJob first = playerLoadJob;
        first.setFinisher(new ChainLoadFinisher() {
			
			@Override
			public void finishLoad(Map<String, Object> data) {
				// TODO Auto-generated method stub
				List<DBObj> playerData = (List<DBObj>) data.get(IBuildData.DATA_SIGN_PLAYER);
	            log.debug("loadPlayerData ------------- > {}", data);
	            DBObj tgt = null;
	            if (playerData != null) {
	                for (DBObj player : playerData) {
	                    if (serverId == (Integer)player.getRelationKeys()[1]) {
	                        tgt = player;
	                        break;
	                    }
	                }
	            }
	            if (tgt != null) {
	                data.put(first.getDataSign(), tgt);
                    int pid = (int) tgt.getPrimaryKey();
	                LoadDBObjChainJob head = null;
	                LoadDBObjChainJob parent = null;
	                Set<Entry<LoadDBObjChainJob, Integer>> otherDataLoaders = playerDataLoadJob.entrySet();
	                for (Iterator<Entry<LoadDBObjChainJob, Integer>> i = otherDataLoaders.iterator(); i.hasNext(); ) {
	                    Entry<LoadDBObjChainJob, Integer> e = i.next();
	                    LoadDBObjChainJob job = e.getKey();
	                    Integer relkeyIx = e.getValue();
	                    job.setRelKeyIx(relkeyIx);
	                    job.setRelKey(pid);
	                    if (head == null)
	                        head = job;
	                    if (parent != null) {
	                        parent.setNext(job);
	                    }
	                    parent = job;
	                    if (!i.hasNext()) {
	                        job.setFinisher(finisher);
	                    }
	                }
	                if (head != null) {
	                    head.load((ConcurrentHashMap) data);
	                }
	            } else {
	                data.remove(first.getDataSign());
	                finisher.finishLoad(data);
	            }
			}
        });
        return first;
    }

    public LoadDBObjChainJob getPlayerLoadJob() {
        return playerLoadJob;
    }

    public void setPlayerLoadJob(LoadDBObjChainJob playerLoadJob) {
        this.playerLoadJob = playerLoadJob;
    }

    public Map<LoadDBObjChainJob, Integer> getPlayerDataLoadJob() {
        return playerDataLoadJob;
    }

    public void setPlayerDataLoadJob(
            Map<LoadDBObjChainJob, Integer> playerDataLoadJob) {
        this.playerDataLoadJob = playerDataLoadJob;
    }

    public ChainLoadFinisher getFinisher() {
        return finisher;
    }

    public void setFinisher(ChainLoadFinisher finisher) {
        this.finisher = finisher;
    }

    public ESManager getThreadManager() {
        return threadManager;
    }

    public void setThreadManager(ESManager threadManager) {
        this.threadManager = threadManager;
    }

}
