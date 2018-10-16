package com.cellsgame.game.module.card.bo.impl;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.core.MsgUtil;
import com.cellsgame.game.core.event.EvtParamType;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.core.message.CMD;
import com.cellsgame.game.module.IBuildData;
import com.cellsgame.game.module.card.bo.CardBO;
import com.cellsgame.game.module.card.csv.CardConfig;
import com.cellsgame.game.module.card.evt.EvtTypeCard;
import com.cellsgame.game.module.card.msg.CodeCard;
import com.cellsgame.game.module.card.msg.MsgFactoryCard;
import com.cellsgame.game.module.card.vo.CardVO;
import com.cellsgame.game.module.func.FuncsExecutor;
import com.cellsgame.game.module.func.cons.FuncsExecutorsType;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.cellsgame.orm.BaseDAO;
import com.cellsgame.orm.DBObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CardBOImpl implements CardBO, IBuildData {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardBOImpl.class);

    @Resource
    private BaseDAO<CardVO> cardDAO;
    
	/**
	 * @see com.cellsgame.game.module.IBuildData#buildAsCreate(com.cellsgame.game.core.message.CMD, com.cellsgame.game.module.player.vo.PlayerVO)
	 */
	@Override
	public void buildAsCreate(CMD cmd, PlayerVO pvo) throws LogicException {
	}

    @Override
    public void buildAsLoad(CMD cmd, PlayerVO player, Map<String, ?> data) throws LogicException {
        List<DBObj> dbObjs = (List<DBObj>) data.get(DATA_SIGN_CARD);
        if (dbObjs == null) return;
        Map<Integer, CardVO> cardVOMap = GameUtil.createSimpleMap();
        for (DBObj dbObj : dbObjs) {
            CardVO cardVO = new CardVO();
            cardVO.readFromDBObj(dbObj);
            cardVO.setPlayerId(player.getId());
            cardVOMap.put(cardVO.getCid(), cardVO);
        }
//        player.setCardVOMap(cardVOMap);
    }


    @Override
    public Map<?, ?> openCard(Map<?, ?> parent, PlayerVO playerVO, int cardId) throws LogicException {
//        CardConfig cardConfig = CacheConfig.getCfg(CardConfig.class, cardId);
//        if(cardConfig == null) {
//            LOGGER.error("open card, not find card config , cardId : {}", cardId);
//            CodeCard.NotFindConfig.throwException();
//            return parent;
//        }
//        Map<Integer, CardVO> cardVOMap = playerVO.getCardVOMap();
//        if(cardVOMap == null) playerVO.setCardVOMap(cardVOMap = GameUtil.createSimpleMap());
//        CardVO cardVO = cardVOMap.get(cardId);
//        //创建对象数据
//        if(cardVO == null){
//            cardVO = new CardVO();
//            cardVO.setCid(cardId);
//            cardVO.setPlayerId(playerVO.getId());
//        }
//        try {
//            int currDate = Integer.parseInt(getShortDate());
//            //月卡结束时间在今天之后，则处理月卡增加时间操作
//            if (cardVO.getEndDate() > 0 && cardVO.getEndDate() > currDate) {
//                int endDate = Integer.parseInt(getNewDateForDay(String.valueOf(cardVO.getEndDate()), cardConfig.getExpiryDate()));
//                cardVO.setEndDate(endDate);
//            } else {
//                int endDate = Integer.parseInt(getNewDateForDay(cardConfig.getExpiryDate()));
//                cardVO.setEndDate(endDate);
//            }
//        }catch (Exception e){
//            LOGGER.error("open card, error , cardId : {}, exception : {}", cardId, e);
//            return parent;
//        }
//        cardVOMap.put(cardId, cardVO);
//        cardDAO.save(cardVO);
//        return MsgFactoryCard.instance().getUpdateCardMsg(parent, cardVO);
        return null;
    }


    @Override
    public Map<String, Object> revCardDayPrize(CMD cmd, PlayerVO playerVO, int cardId) throws LogicException {
//        CardConfig cardConfig = CacheConfig.getCfg(CardConfig.class, cardId);
//        if(cardConfig == null) {
//            LOGGER.error("rev card day prize, not find card config , cardId : {}", cardId);
//            CodeCard.NotFindConfig.throwException();
//            return null;
//        }
//        Map<Integer, CardVO> cardVOMap = playerVO.getCardVOMap();
//        if(cardVOMap == null) playerVO.setCardVOMap(cardVOMap = GameUtil.createSimpleMap());
//        CardVO cardVO = cardVOMap.get(cardId);
//        if(cardVO == null){
//            LOGGER.error("rev card day prize, not open card , cardId : {}", cardId);
//            CodeCard.CardNotOpen.throwException();
//            return null;
//        }
//        int currDate = Integer.parseInt(getShortDate());
//        if(currDate > cardVO.getEndDate()){
//            LOGGER.error("rev card day prize, card is end , cardId : {}", cardId);
//            CodeCard.CardEnd.throwException();
//            return null;
//        }
//        if(currDate <= cardVO.getPrizeDate()){
//            LOGGER.error("rev card day prize, already rev prize , cardId : {}, prizeDate : {}", cardId, cardVO.getPrizeDate());
//            CodeCard.AlreadyRevPrize.throwException();
//            return null;
//        }
//        FuncsExecutor exec = FuncsExecutorsType.Base.getExecutor(cmd);
//        exec.addSyncFunc(cardConfig.getDayPrize());
//        Map<String, Object> result = GameUtil.createSimpleMap();
//        Map<String, Object> prizeMap = GameUtil.createSimpleMap();
//        exec.exec(result, prizeMap, playerVO, true, 1);
//        MsgFactoryCard.instance().getCardPrizeMsg(result, prizeMap);
//        cardVO.setPrizeDate(currDate);
//        cardDAO.save(cardVO);
//        EvtTypeCard.GET_CARD_PRIZE.happen(result, cmd, playerVO, EvtParamType.TYPE.val(cardVO.getCfg().getType()));
//        return MsgUtil.brmAll(MsgFactoryCard.instance().getUpdateCardMsg(result, cardVO), cmd);
        return  null;
    }

    private String getNewDateForDay(int day) throws ParseException {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DAY_OF_YEAR, day);
        return dateToString(cal.getTime());
    }


    private String getNewDateForDay(String oldDateStr, int day) throws ParseException {
        Calendar cal = new GregorianCalendar();
        Date oldDate = stringToDate(oldDateStr);
        cal.setTime(oldDate);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return dateToString(cal.getTime());
    }

    public static ThreadLocal<SimpleDateFormat> sdf = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd"));

    private Date stringToDate(String dateStr) throws ParseException {
        return sdf.get().parse(dateStr);
    }

    private String dateToString(Date date) throws ParseException {
        return sdf.get().format(date);
    }

    private String getShortDate() {
        return sdf.get().format(new Date().getTime());
    }
}
