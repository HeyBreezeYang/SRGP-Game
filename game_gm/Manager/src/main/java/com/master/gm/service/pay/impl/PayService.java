package com.master.gm.service.pay.impl;

import java.io.UnsupportedEncodingException;

import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.context.GmConfiger;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.SendUtil;
import com.gmdesign.util.StringUtil;
import com.master.bean.dispoly.Parameter;
import com.master.gm.BaseService;
import com.master.gm.service.pay.PayServiceIF;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/9/30.
 *
 * @ClassName PayService
 * @Description
 */
@IocBean
public class PayService extends BaseService implements PayServiceIF{
    @Override
    public String getPlayerFirstPay(String sid,String pid) throws GmException {
        GmHashMap p=new GmHashMap();
        p.put("sid",sid);
        p.put("pid",pid);
        p.put("app",GmConfiger.APP);
        String url= null;
        try {
            url = this.dao.fetch(Parameter.class,"deliverUrl").getPrams().concat("/firstPay?param="+ StringUtil.encodeUrlForJson(p));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String res=SendUtil.sendHttpMsg(url,null);
        if(res.equals("0")){
            return "no payment";
        }else {
            return DateUtil.formatDateTime(Long.parseLong(res));
        }
    }
}
