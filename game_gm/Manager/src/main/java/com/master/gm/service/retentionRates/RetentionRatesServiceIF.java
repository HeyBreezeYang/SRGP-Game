package com.master.gm.service.retentionRates;

import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.Channel;
import com.master.bean.dispoly.GameServer;

import java.text.ParseException;
import java.util.List;

/**
 * Created by HP on 2018/7/17.
 */
public interface RetentionRatesServiceIF {
    List<GameServer> getAllServer();

    List<Channel> getAllChannel();

    List queryRetentionRatesData(String sid, String channel, String startDate, String endDate) throws GmException, ParseException;
}
