package com.javaweb;

import com.alibaba.fastjson.JSONObject;
import com.design.exception.PlatformException;
import com.design.util.ResultUtil;
import com.design.util.URLTool;
import com.service.AccountInsideServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/23.
 */
@RestController
@EnableAutoConfiguration
@RequestMapping("/insidea")
public class AccountInsideController {

    @Autowired
    private AccountInsideServiceIF accountInsideService;

    @RequestMapping(value = "/saveOrUpdate")
    public String saveOrUpdateInsideAccount(@Param("msg") String msg){

        ResultUtil result = new ResultUtil();
        try {
            int i = accountInsideService.saveOrUpdate(URLTool.Dncode(msg));
            result.setCode(i);
            if (i == 1){
                result.setMsg("操作成功");
            }else {
                result.setMsg("操作异常");
            }
        }catch (PlatformException p){
            result.setCode(10000);
            result.setMsg(p.getMessage());
        }

        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/queryInsideAccount")
    public String queryInsideAccount(){
        ResultUtil result = new ResultUtil();
        List<Map<String,Object>> insides = accountInsideService.queryInsideAccount();
        if (insides == null && insides.size()==0){
            result.setCode(0);
            result.setMsg("没有查询到任何数据");
        }else {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(insides);
        }

        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/queryInsideAccountById")
    public String queryInsideAccountById(@Param("id") String id){
        ResultUtil result = new ResultUtil();
        if (id==null || id.equals("")){
            result.setCode(0);
            result.setMsg("id值为空");
            return JSONObject.toJSONString(result);
        }
        List<Map<String,Object>> insides = accountInsideService.queryInsideAccountById(id);
        if (insides == null && insides.size()==0){
            result.setCode(0);
            result.setMsg("没有查询到任何数据");
        }else {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(insides);
        }

        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/delInsideAccount")
    public String delInsideAccount(@Param("id") String id){
        ResultUtil result = new ResultUtil();
        if (id==null || id.equals("")){
            result.setCode(0);
            result.setMsg("id值为空");
            return JSONObject.toJSONString(result);
        }
        int i = accountInsideService.delInsideAccount(id);
        if (i==0){
            result.setCode(0);
            result.setMsg("没有查询到任何数据");
        }else {
            result.setCode(1);
            result.setMsg("删除成功");
        }

        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/insideAccountTE")
    public String insideAccountTE(@Param("id") String id,@Param("type")String type,@Param("loginName") String loginName){

        ResultUtil result = new ResultUtil();
        if (id==null || id.equals("")){
            result.setCode(0);
            result.setMsg("id值为空");
            return JSONObject.toJSONString(result);
        }
        if (type==null || type.equals("")){
            result.setCode(0);
            result.setMsg("type值为空");
            return JSONObject.toJSONString(result);
        }
        int i = accountInsideService.insideAccountTE(id,type,loginName);
        if (i==0){
            result.setCode(0);
            result.setMsg("没有查询到任何数据");
        }else {
            result.setCode(1);
            result.setMsg("审核完成");
        }

        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/queryInsideAccountListCondition")
    public String queryInsideAccountListCondition(@Param("msg") String msg, HttpServletRequest request){
        ResultUtil result = new ResultUtil();
        List<Map<String,Object>> insides = accountInsideService.queryInsideAccountCondition(msg);
        if (insides == null && insides.size()==0){
            result.setCode(0);
            result.setMsg("没有查询到任何数据");
        }else {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(insides);
        }
        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/queryAccountAdmin")
    public String queryAccountAdminList(@Param("rolename") String rolename){
        ResultUtil result = new ResultUtil();
        Map<String,Object> accountAdmin = null;
        try{
            accountAdmin = accountInsideService.queryAccountAdmin(rolename);
            if (accountAdmin == null && accountAdmin.size()==0){
                result.setCode(0);
                result.setMsg(rolename+"不是内部号，请添加为内部再操作");
            }else {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(accountAdmin);
            }
        }catch (Exception e){
            result.setCode(0);
            result.setMsg("不是内部号，请添加为内部号再操作");
        }

        return JSONObject.toJSONString(result);
    }

    @RequestMapping(value = "/queryAccountInsideByPid")
    public String queryAccountInsideByPid(@Param("pid") String pid){
        ResultUtil result = new ResultUtil();
        Map accountInside = null;
        try{
            accountInside = accountInsideService.queryAccountInsideByPid(pid);
            if (accountInside == null && accountInside.size()==0){
                result.setCode(0);
                result.setMsg("没有查询到任何数据");
            }else {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(accountInside);
            }
        }catch (Exception e){
            result.setCode(0);
            result.setMsg("货品匹配出错，请确认金额是否正确");
        }
        return JSONObject.toJSONString(result);
    }

}
