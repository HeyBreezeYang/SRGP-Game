package com.master.gm.service.Rank;

import com.gmdesign.exception.GmException;


/**
 * Created by HP on 2018/7/5.
 */
public interface RankTimeServiceIF {
    void getGameRankOutFile(String server,Integer type) throws GmException;
}
