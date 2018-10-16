package com.master.gm.service.manage;

import java.util.List;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.GameServer;
import com.master.bean.web.ShopDesign;

/**
 * Created by DJL on 2017/8/2.
 *
 * @ClassName GameConfigServiceIF
 * @Description 游戏配置
 */
public interface GameConfigServiceIF {
    List<GmHashMap> getAllActivity()throws GmException;

    void deleteActivity(String sid,String id)throws GmException;

    List<GameServer> queryAllConfig()throws GmException;

//    Map deleteServer(int logic)throws GmException;

    GmHashMap changeServerState(String sid,int state)throws GmException;

    void changeServerRecommend(String sid)throws GmException;

    List<ShopDesign> getAllShop()throws GmException ;

    void updateShop(ShopDesign bean)throws GmException ;

    void delShop(int id,String sid)throws GmException ;

    void updateExc(String sid,String data)throws GmException ;

    void updateExp(String sid,String data)throws GmException ;

    List<GmHashMap> getAllExc()throws GmException ;

    List<GmHashMap> getAllExp()throws GmException ;

    Object getAllServer();
}
