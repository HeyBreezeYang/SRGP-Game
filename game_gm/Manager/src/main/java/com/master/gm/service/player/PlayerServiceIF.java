package com.master.gm.service.player;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.bean.back.BanBack;
import org.nutz.dao.entity.Record;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * Created by DJL on 2017/7/8.
 *
 * @ClassName PlayerServiceIF
 * @Description 角色相关业务接口
 */
public interface PlayerServiceIF {

    GmHashMap queryPlayerMsg(String sid , String name, int type)throws GmException;

    GmHashMap queryBanState(String sid,String name)throws GmException;

    GmHashMap setBan(String sid,String name,int cid,long time) throws GmException, SchedulerException;
    GmHashMap sendOrder(String sid,String order)throws GmException;

    List<Record> getAllServer();

    List<BanBack> queryPlayerList();
}
