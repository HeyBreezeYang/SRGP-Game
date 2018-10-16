package com.master.gm.service.Rank.impl;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.ReadFile;
import com.master.bean.back.VipRank;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import com.master.gm.service.Rank.VipRankServiceIF;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.ioc.loader.annotation.IocBean;

import java.util.List;

/**
 * Created by HP on 2018/7/19.
 */
@IocBean
public class VipRankService extends BaseService implements VipRankServiceIF {

    @Override
    public void toDBVipRankInfo() throws GmException {
        String toDate =  DateUtil.getCurrentTimeMillisToString(System.currentTimeMillis(),"yyyy-MM-dd");
        List<GameServer> gameServers = this.dao.query(GameServer.class,null);
        for (GameServer gs:gameServers) {
            List<GmHashMap> res=new ReadFile(gs.getServerID(),"3",toDate,toDate).getMsg();
            for (GmHashMap gmap:res) {
                List<VipRank> vipRanks = this.dao.query(VipRank.class, Cnd.where("pid","=",gmap.get("pid").toString()).and("pname","=",gmap.get("pname").toString()));
                if (vipRanks != null && vipRanks.size()>0){
                    VipRank vr = vipRanks.get(0);
                    vr.setLv(Integer.parseInt(gmap.get("vip").toString()));
                    if (gmap.get("vipExp ") != null){
                        vr.setExp(Long.parseLong(gmap.get("vipExp ").toString()));
                    }else {
                        vr.setExp(0);
                    }
                    vr.setMoney(0);
                    this.dao.update(vr);
                }else {
                    VipRank vr = new VipRank();
                    vr.setId(0);
                    vr.setSid(gs.getServerID());
                    vr.setChannel(gmap.get("ptf").toString());
                    vr.setPid(gmap.get("pid").toString());
                    vr.setPname(gmap.get("pname").toString());
                    vr.setLv(Integer.parseInt(gmap.get("vip").toString()));
                    if (gmap.get("vipExp ") != null){
                        vr.setExp(Long.parseLong(gmap.get("vipExp ").toString()));
                    }else {
                        vr.setExp(0);
                    }
                    vr.setMoney(0);
                    this.dao.insert(vr);
                }
            }
        }
    }
}
