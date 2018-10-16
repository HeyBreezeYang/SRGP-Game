package com.master.gm.service.LTV;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;

import java.util.List;

/**
 * Created by HP on 2018/7/20.
 */
public interface LTVServiceIF {

    List<GameServer> getAllServer();

    List<Channel> getAllChannel();

    List<GmHashMap> queryLTV(String sid, String channel, Integer ltvType, String rstartDate, String rendDate,String qstartDate,String qendDate) throws GmException;
}
