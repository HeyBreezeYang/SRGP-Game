package com.master.gm.service.manage;

import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.master.bean.back.InsideAccountLog;
import org.nutz.dao.entity.Record;
import org.nutz.mvc.annotation.Param;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/23.
 */
public interface InsideAccountServiceIF {

    List<Record> getAllServer();

    List<Record> getAllChannel();

    void saveOrUpdate(String id, String sid, String channel, String rolename, String pid, String paymoney, String username, String userphone, String ascription, String applyuser, String applyreason, String remark);

    List<Map<String,Object>> queryInsideAccountList();

    List<Map<String,Object>> queryInsideAccountById(Integer id);

    void delInsideAccount(String id) throws GmException;

    void insideAccountTE(String id, String type, String name);

    List<Map<String,Object>> queryInsideAccountListCondition(String sid, String channel, String rolename, String pid, String money, String status);

    void grantResource(String paymoney,String rolename,UserForService user) throws GmException;

    List<InsideAccountLog> getInsideAccountLog(String startDate, String endDate) throws ParseException;
}
