package com.cellsgame.game.quartz;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.cons.Command;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.core.push.Push;
import com.cellsgame.game.module.DailyResetable;
import com.cellsgame.game.module.friend.bo.FriendsBO;
import com.cellsgame.game.module.guild.bo.GuildBO;
import com.cellsgame.game.module.player.bo.PlayerBO;
import com.cellsgame.game.module.player.cache.CachePlayer;
import com.cellsgame.game.module.player.vo.PlayerVO;

import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

/**
 * @author Aly on  2016-08-16.
 */
public class DayOffset4Job {

	private GuildBO guildBO;
	private FriendsBO friendsBO;
	private PlayerBO playerBO;

	private Collection<DailyResetable> systemResetables;

    public void execute() {
        Calendar c = Calendar.getInstance();
//        c.setTime(new Date());        默认当前时间 无用代码
        boolean resetByWeek5 = c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;

        if (resetByWeek5) {
           
        }
        Dispatch.dispatchGameLogic(new Runnable() {
            @Override
            public void run() {

                long cur = System.currentTimeMillis();
                // 刷新公会数据
                guildBO.systemResetGuildData();
                // 检测公会长是否该替换
                guildBO.replaceGuildLeader();
                // 刷新好友祝福
                friendsBO.sysResetData(cur);
                // 系统刷新在线玩家数据
                resetOnlinePlayerData();
            }
        });

    }

    private void resetOnlinePlayerData() {
        long ms = System.currentTimeMillis();
    	if(systemResetables == null){
    	    Map<String, DailyResetable> resetables = SpringBeanFactory.getBeanByType(DailyResetable.class);
    	    if(resetables!=null){
    	    	systemResetables = resetables.values();
    	    }
    	}
    	if(systemResetables!=null){
    		 for (PlayerVO playerVO : CachePlayer.getOnlinePlayers()) {
    	            Map<String, Object> ret = GameUtil.createSimpleMap();
    	            // 重置
    	            for (DailyResetable r : systemResetables) {
    					r.reset(CMD.system, ret, playerVO);
    				}
    	            // 保存玩家数据
    	            playerBO.save(playerVO);
    	            // 给玩家推送系统重置信息
    	            Push.multiThreadSend(playerVO.getMessageController(), MsgUtil.brmAll(ret, Command.General_System_Refresh));
    	     }
    	}
        
        
       
    }

    public void setGuildBO(GuildBO guildBO) {
        this.guildBO = guildBO;
    }


    public void setPlayerBO(PlayerBO playerBO) {
        this.playerBO = playerBO;
    }

    public void setFriendsBO(FriendsBO friendsBO) {
        this.friendsBO = friendsBO;
    }

}
