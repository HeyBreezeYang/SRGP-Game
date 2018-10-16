package com.master.gm;

import java.util.Date;
import java.util.List;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.context.DataType;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.ReadFile;
import com.master.bean.back.Player;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;

/**
 * Created by DJL on 2017/6/20.
 *
 * @ClassName BaseService
 * @Description
 */
public abstract class BaseService {
    @Inject
    protected Dao dao;

    public Dao getDao(){
        return this.dao;
    }
    protected String getPid(String server,String pName) throws GmException {
        String pid="";
        Player p= this.dao.fetch(Player.class, Cnd.where("name","=",pName).and("sid","=",server));
        if(p==null){
            ReadFile rf =new ReadFile(server, DataType.PLAYER, DateUtil.getDateString(new Date()));
            List<GmHashMap> ls= rf.getMsg();
            for(GmHashMap m:ls){
                if(m.get("pname").toString().equals(pName)){
                    pid=m.get("pid").toString();
                    break;
                }
            }
        }else{
            pid=p.getPid();
        }
        return pid;
    }

}
