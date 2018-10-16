package com.manager.service.impl.ws;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.manager.bean.AppBean;
import com.manager.bean.ParamBean;
import com.manager.bean.VersionBean;
import com.manager.exception.ManageException;
import com.manager.service.AddGameServiceIF;
import com.manager.service.impl.BaseService;
import com.manager.util.SendUtil;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/5/15.
 *
 * @ClassName AddGameService
 * @Description
 */
@IocBean
public class AddGameService extends BaseService implements AddGameServiceIF {


    @Override
    public String getGmConfig(int id) throws ManageException {
        return null;
    }

    @Override
    public List<VersionBean> queryAllVersion() throws ManageException {
       List<VersionBean> vls= this.dao.query(VersionBean.class,null);
       List<AppBean> als=this.dao.query(AppBean.class,null);
       if(als!=null&&!als.isEmpty()){
           for(AppBean bean :als){
               for(VersionBean vb:vls){
                   if(vb.getId()==bean.getVersionID()){
                       vls.remove(vb);
                       break;
                   }
               }
           }
       }
        return vls;
    }

    @Override
    public void setGameKey(AppBean bean) throws ManageException {
        if(this.dao.insert(bean).getId()<0){
            throw new ManageException("添加APP-ID失败!~");
        }
        ParamBean p =this.dao.fetch(ParamBean.class,1L);
        try {
            SendUtil.sendHttpMsg(p.getParam()+"?AID="+URLEncoder.encode(bean.sendJsonMsg(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ManageException(e.getMessage());
        }
    }
}
