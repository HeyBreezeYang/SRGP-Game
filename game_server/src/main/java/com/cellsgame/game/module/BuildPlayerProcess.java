package com.cellsgame.game.module;

import java.util.List;
import java.util.Map;

import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.player.vo.PlayerVO;

public class BuildPlayerProcess {

    private List<IBuildData> builds;

    public void setBuilds(List<IBuildData> builds) {
        this.builds = builds;
    }

    public PlayerVO build(PlayerVO player, CMD cmd, Map<String, ?> datas) throws LogicException {
        //
        for (IBuildData build : builds) {
            build.buildAsLoad(cmd, player, datas);
        }
        return player;
    }
    
    public PlayerVO buildAsCreate(PlayerVO player, CMD cmd){
    	  for (IBuildData build : builds) {
              build.buildAsCreate(cmd, player);
          }
          return player;
    }
}
