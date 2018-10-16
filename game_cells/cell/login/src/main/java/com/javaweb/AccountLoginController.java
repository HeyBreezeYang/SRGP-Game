package com.javaweb;

import com.design.exception.PlatformException;
import com.design.util.URLTool;
import com.service.AccountLoginServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by HP on 2018/6/13.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/loginInfo")
public class AccountLoginController {

    @Autowired
    private AccountLoginServiceIF loginService;

    @RequestMapping(value = "/accountIsLogin")
    public String accountIsLogin(@Param("msg") String msg){
        int i = loginService.getAccountIsLogin(URLTool.Dncode(msg));
        return "{code:"+i+"}";
    }
}
