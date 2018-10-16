package com.cellsgame.game.module.card.msg;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.MsgFactory;
import com.cellsgame.game.module.card.vo.CardVO;

import java.util.List;
import java.util.Map;

/**
 * Created by yfzhang on 2016/8/22.
 */
public class MsgFactoryCard extends MsgFactory {
    public static final String CARD = "card";

    public static final String CARD_LIST = "lst";
    public static final String CARD_UPDATE = "up";


    private static final MsgFactoryCard instance = new MsgFactoryCard();

    public static final MsgFactoryCard instance() {
        return instance;
    }

    @Override
    public String getModulePrefix() {
        return CARD;
    }


    public Map<String, Object> getCardPrizeMsg(Map<String, Object> parent, Map prizeMap) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        addPrizeMsg(op, prizeMap);
        return parent;
    }

    public Map<String, Object> getCardListMsg(Map<String, Object> parent, Map<Integer, CardVO> cardVOS) {
        if(cardVOS == null) return parent;
        parent = creIfNull(parent);
        Map op = gocInfoMap(parent);
        List<Map> list = gocLstIn(op, CARD_LIST);
        for (CardVO cardVO : cardVOS.values()) {
            list.add(getCardInfoMsg(cardVO));
        }
        return parent;
    }


    public Map<?, ?> getUpdateCardMsg(Map<?, ?> parent, CardVO cardVO) {
        parent = creIfNull(parent);
        Map op = gocOpMap(parent);
        List<Map> update = gocLstIn(op, CARD_UPDATE);
        update.add(getCardInfoMsg(cardVO));
        return parent;
    }


    public static final String END_DATE = "edDate";
    public static final String PRIZE_DATE = "przDate";

    private Map<?, ?> getCardInfoMsg(CardVO cardVO){
        Map<String, Object> result = GameUtil.createSimpleMap();
        result.put(CID, cardVO.getCid());
        result.put(END_DATE, cardVO.getEndDate());
        result.put(PRIZE_DATE, cardVO.getPrizeDate());
        return result;
    }

}
