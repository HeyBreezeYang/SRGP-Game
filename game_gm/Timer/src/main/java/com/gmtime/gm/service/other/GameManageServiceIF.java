package com.gmtime.gm.service.other;

import java.util.Map;

import com.gmdesign.exception.GmException;

/**
 * Created by DJL on 2016/11/23.
 *
 * @ClassName gm
 * @Description
 */
public interface GameManageServiceIF {
    Map relieve(String name,String sid,int method)throws GmException;
}
