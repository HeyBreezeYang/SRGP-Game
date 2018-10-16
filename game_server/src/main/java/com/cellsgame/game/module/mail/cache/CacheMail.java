package com.cellsgame.game.module.mail.cache;

import java.util.Map;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.module.mail.vo.MailVO;
import com.cellsgame.game.module.player.vo.PlayerVO;

/**
 * Created by yfzhang on 2016/8/22.
 */
public class CacheMail {

    private static Map<Integer, Map<Integer, MailVO>> cacheNewMail = GameUtil.createMap();

    public static void cacheNewMail(PlayerVO playerVO, MailVO newMailVO){
        cacheNewMail(playerVO.getId(), newMailVO);
    }

    public static void cacheNewMail(int playerId, MailVO newMailVO){
        Map<Integer, MailVO> mailVOMap = cacheNewMail.get(playerId);
        if(mailVOMap == null){
            mailVOMap = GameUtil.createMap();
            cacheNewMail.put(playerId, mailVOMap);
        }
        mailVOMap.put(newMailVO.getId(), newMailVO);
    }

    public static Map<Integer, MailVO> getCacheNewMail(PlayerVO playerVO){
        return cacheNewMail.get(playerVO.getId());
    }

}
