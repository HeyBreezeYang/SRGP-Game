package com.cellsgame.game.module.player.cons;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.game.core.csv.IAttribute;
import com.cellsgame.game.module.player.csv.PlayerLevelConfig;
import com.cellsgame.game.module.player.vo.PlayerVO;

public enum FuncTimes {

   
    OperateMoney(4) {
        @Override
        public int getLimit(PlayerVO playerVO) {
//            PlayerLevelConfig config = PlayerLevelConfig.By_Level[playerVO.getLevel()];
            PlayerLevelConfig config = PlayerLevelConfig.By_Level[0];
            return config.getFuncTimeLimit();
        }
        @Override
        public int getCd(PlayerVO playerVO) {
            long iq = playerVO.getAtt().get().getValue()[IAttribute.TYPE_IQ];
            long cd = Math.min(18000, Math.max(60, iq/10000 * 60));
            return (int)cd;
        }

        @Override
        public int getRevNum(PlayerVO playerVO) {
//            PlayerLevelConfig config = PlayerLevelConfig.By_Level[playerVO.getLevel()];
            //无vip
            PlayerLevelConfig config = PlayerLevelConfig.By_Level[0];
            return config.getFuncTimeRevNum();
        }
    },
    OperateContacts(5) {
        @Override
        public int getLimit(PlayerVO playerVO) {
//            PlayerLevelConfig config = PlayerLevelConfig.By_Level[playerVO.getLevel()];
            PlayerLevelConfig config = PlayerLevelConfig.By_Level[0];
            return config.getFuncTimeLimit();
        }
        @Override
        public int getCd(PlayerVO playerVO) {
            long iq = playerVO.getAtt().get().getValue()[IAttribute.TYPE_IQ];
            long cd = Math.min(18000, Math.max(60, iq/10000 * 60));
            return (int)cd;
        }

        @Override
        public int getRevNum(PlayerVO playerVO) {
//            PlayerLevelConfig config = PlayerLevelConfig.By_Level[playerVO.getLevel()];
            PlayerLevelConfig config = PlayerLevelConfig.By_Level[0];
            return config.getFuncTimeRevNum();
        }
    },
    OperateChance(6) {
        @Override
        public int getLimit(PlayerVO playerVO) {
//            PlayerLevelConfig config = PlayerLevelConfig.By_Level[playerVO.getLevel()];
            PlayerLevelConfig config = PlayerLevelConfig.By_Level[0];
            return config.getFuncTimeLimit();
        }
        @Override
        public int getCd(PlayerVO playerVO) {
            long iq = playerVO.getAtt().get().getValue()[IAttribute.TYPE_IQ];
            long cd = Math.min(18000, Math.max(60, iq/10000 * 60));
            return (int)cd;
        }

        @Override
        public int getRevNum(PlayerVO playerVO) {
//            PlayerLevelConfig config = PlayerLevelConfig.By_Level[playerVO.getLevel()];
            PlayerLevelConfig config = PlayerLevelConfig.By_Level[0];
            return config.getFuncTimeRevNum();
        }
    }

    ;
    public static final int typeFix = 80500000;

    private int id;

    FuncTimes(int id) {
        this.id = typeFix + id;
    }

    public int getId(){
        return id;
    }

    public abstract int getLimit(PlayerVO playerVO);

    public abstract int getCd(PlayerVO playerVO);

    public abstract  int getRevNum(PlayerVO playerVO);

}
