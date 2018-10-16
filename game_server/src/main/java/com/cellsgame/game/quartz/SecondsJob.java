package com.cellsgame.game.quartz;


import com.cellsgame.game.core.dispatch.Dispatch;
import com.cellsgame.game.module.player.cache.CacheScene;
import com.cellsgame.game.module.player.cons.SceneType;

/**
 * User: 阚庆忠
 * Date: 2016/9/12 19:49
 * Desc:
 */
public class SecondsJob {



	public void execute() {
		Dispatch.dispatchGameLogic(secondsExecJob);
	}



	private Runnable secondsExecJob = new Runnable() {
		@Override
		public void run() {
			long ms = System.currentTimeMillis();
		
			for (SceneType sceneType : SceneType.values()) {
				CacheScene.getScene(sceneType).sendCacheMsg();
			}
		}
	};

}
