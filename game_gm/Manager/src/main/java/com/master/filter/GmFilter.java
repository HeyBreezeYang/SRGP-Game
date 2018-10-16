package com.master.filter;

import javax.servlet.http.HttpServletRequest;

import com.gmdesign.bean.other.UserForService;
import com.gmdesign.util.StringUtil;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ServerRedirectView;
import org.nutz.mvc.view.VoidView;

/**
 * Created by DJL on 2017/8/29.
 *
 * @ClassName GM
 * @Description
 */
public class GmFilter implements ActionFilter{
    private static Log log = Logs.get();
    @Override
    public View match(ActionContext actionContext) {
        HttpServletRequest request=actionContext.getRequest();
        UserForService user= (UserForService) request.getSession().getAttribute("GM");
        if (user == null) {
            log.error("user is null!~~");
            return new ServerRedirectView("http://192.168.1.46:8109/manage/platform");
        }else{
            String k= StringUtil.byte2hex(user.getName().getBytes());
            if(!k.equals(user.getUserKey())){
                log.error("key is error!~~");
                return new VoidView();
            }
        }
        return null;
    }
}
