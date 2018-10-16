//package com.cellsgame.game.module.player.bo;
//
//import java.util.List;
//import java.util.Map;
//
//import com.cellsgame.game.core.excption.LogicException;
//import com.cellsgame.game.core.message.CMD;
//import com.cellsgame.game.module.IBuildData;
//import com.cellsgame.game.module.player.vo.CheckInVO;
//import com.cellsgame.game.module.player.vo.PlayerVO;
//import com.cellsgame.orm.DBObj;
//
///**
// * File Description.
// *
// * @author Yang
// */
//public class CheckInDataBuilder implements IBuildData {
//    @Override
//    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
//        // DB数据
//        List<DBObj> checkInData = (List<DBObj>) data.get(DATA_SIGN_CHECK_IN);
//        //
//        if (checkInData == null || checkInData.size() == 0) return;
//        // 签到数据
//        CheckInVO checkInVO = new CheckInVO();
//        // DB to Object
//        checkInVO.readFromDBObj(checkInData.get(0));
//        // 玩家
//        checkInVO.setPlayer(player.getId());
//        // 加入玩家数据
//        player.setCheckInVO(checkInVO);
//    }
//}
