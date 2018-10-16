package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bean.InsideAccount;
import com.design.exception.PlatformException;
import com.service.AccountInsideServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/23.
 */
@Service
public class AccountInsideService implements AccountInsideServiceIF {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_INSIDE="insert into account_inside (sid,channel,rolename,pid,paymoney,username,userphone,ascription," +
            "applyuser,applyreason,status,remark,createtime) value (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String UPDATE_INSIDE = "update account_inside set paymoney=?,username=?,userphone=?,ascription=?,applyuser=?," +
            "applyreason=?,remark=? where id=?";
    private static final String QUERY_INSIDE = "select id,sid,channel,rolename,pid,paymoney,username,userphone,ascription,applyuser," +
            "applyreason,status,remark,createtime  from account_inside ";
    private static final String QUERY_INSIDE_ID = "select id,sid,channel,rolename,pid,paymoney,username,userphone,ascription,applyuser," +
            "applyreason,status,remark,createtime  from account_inside where id=? ";
    private static final String DEL_INSIDE="delete from account_inside where id = ?";
    private static final String DEL_ACCOUNT_ADMIN="delete from account_admin where adminAccount=?";
    //private static final String CONTACT_DEL_INSIDE="update account_inside set status = 0 where id = ?";
    private static final String TE_INSIDE="update account_inside set status=?,toexamineby=?,toexaminetime=? where id=?";
    private String QUERY_INSIDE_CON="select id,sid,channel,rolename,pid,paymoney,username,userphone,ascription,applyuser," +
            "applyreason,status,remark,createtime  from account_inside where 1=1 ";
    private static final String INSERT_ACCOUNT_ADMIN = "insert into account_admin (adminAccount) value (?)";
    private static final String QUERY_ACCOUNT_ADMIN_ROLE = "select a.id,a.adminAccount,ai.sid from account_admin a left join account_inside ai on a.adminAccount=ai.pid where ai.rolename=?";
    private static final String QUERY_INSIDE_PID="select id,sid,channel,rolename,pid,paymoney,username,userphone,ascription,applyuser," +
            "applyreason,status,remark,createtime  from account_inside where pid=? ";

    @Override
    public int saveOrUpdate(String msg) throws PlatformException {
        Map<String,Object> insideAccount = JSONObject.parseObject(msg,Map.class);
        if (insideAccount.get("id") == null || insideAccount.get("id").equals("")){
            //添加
            try{
                return jdbcTemplate.update(INSERT_INSIDE,insideAccount.get("sid"),insideAccount.get("channel"),insideAccount.get("rolename"),
                        insideAccount.get("pid"),insideAccount.get("paymoney"),insideAccount.get("username"),insideAccount.get("userphone"),
                        insideAccount.get("ascription"),insideAccount.get("applyuser"),insideAccount.get("applyreason"),
                        insideAccount.get("status"),insideAccount.get("remark"),System.currentTimeMillis());
            }catch (Exception e){
                e.printStackTrace();
                new PlatformException("添加内部号异常");
            }
        }else {
            try{
                return jdbcTemplate.update(UPDATE_INSIDE,insideAccount.get("paymoney"),insideAccount.get("username"),insideAccount.get("userphone"),
                        insideAccount.get("ascription"),insideAccount.get("applyuser"),insideAccount.get("applyreason"),insideAccount.get("remark"),insideAccount.get("id"));
            }catch (Exception e){
                e.printStackTrace();
                new PlatformException("添加内部号异常");

            }
            //修改
        }
        return 1;
    }

    @Override
    public List<Map<String, Object>> queryInsideAccount() {
        List<Map<String,Object>> insides = jdbcTemplate.queryForList(QUERY_INSIDE);
        return insides;
    }

    @Override
    public List<Map<String, Object>> queryInsideAccountById(String id) {
        List<Map<String,Object>> insides = jdbcTemplate.queryForList(QUERY_INSIDE_ID,id);
        return insides;
    }

    @Override
    public int delInsideAccount(String id) {
        List<Map<String,Object>> insides = jdbcTemplate.queryForList(QUERY_INSIDE_ID,id);
        int i = 0;
        if (insides != null && insides.size()>0){
            i=jdbcTemplate.update(DEL_INSIDE,id);
            if (Integer.parseInt(insides.get(0).get("status").toString()) == 1){
                i=jdbcTemplate.update(DEL_ACCOUNT_ADMIN,insides.get(0).get("pid"));
            }
        }
       /* if (insides != null && insides.size()>0){
            Map map = insides.get(0);
            if (Integer.parseInt(map.get("status").toString())==3){
                i=jdbcTemplate.update(CONTACT_DEL_INSIDE,id);
            }else {
                i = jdbcTemplate.update(DEL_INSIDE,id);
            }
        }else {
            return 0;
        }*/
        return  i;
    }

    @Override
    public int insideAccountTE(String id, String type,String loginName) {
        List<Map<String,Object>> insides = jdbcTemplate.queryForList(QUERY_INSIDE_ID,id);
        int i = 0;
        if (insides != null && insides.size()>0){
            Map map = insides.get(0);
            if (Integer.parseInt(map.get("status").toString())==0){
                i=jdbcTemplate.update(TE_INSIDE,type,loginName,System.currentTimeMillis(),id);
                if (Integer.parseInt(type) == 1){
                    jdbcTemplate.update(INSERT_ACCOUNT_ADMIN,map.get("pid"));
                }
            }
        }else {
            return 0;
        }

        return i;
    }

    @Override
    public List<Map<String, Object>> queryInsideAccountCondition(String msg) {
        Map msgMap = JSONObject.parseObject(msg,Map.class);
        List params = new ArrayList();
        StringBuffer sql = new StringBuffer(QUERY_INSIDE_CON);
        if (msgMap.get("sid") != null && !msgMap.get("sid").equals("") && !msgMap.get("sid").equals("all")){
            sql .append(" and sid=?");
            params.add(msgMap.get("sid").toString());
        }
        if (msgMap.get("channel") != null && !msgMap.get("channel").equals("") && !msgMap.get("channel").equals("all")){
            sql .append(" and channel=?");
            params.add(msgMap.get("channel").toString());
        }
        if (msgMap.get("rolename") != null && !msgMap.get("rolename").equals("")){
            sql .append(" and rolename=?");
            params.add(msgMap.get("rolename").toString());
        }
        if (msgMap.get("pid") != null && !msgMap.get("pid").equals("")){
            sql .append(" and pid=?");
            params.add(msgMap.get("pid").toString());
        }
        if(msgMap.get("status") != null && !msgMap.get("status").equals("") && !msgMap.get("status").equals("all")){
            sql .append(" and status=?");
            params.add(msgMap.get("status").toString());
        }
        if (msgMap.get("money") != null && !msgMap.get("money").equals("")){
            sql .append(" and paymoney=?");
            params.add(msgMap.get("money").toString());
        }
        List<Map<String,Object>> insides = jdbcTemplate.queryForList(sql.toString(),params.toArray());
        return insides;
    }

    @Override
    public Map queryAccountAdmin(String rolename) throws PlatformException{
        Map accountAdmins = jdbcTemplate.queryForMap(QUERY_ACCOUNT_ADMIN_ROLE,rolename);
        return accountAdmins;
    }

    @Override
    public Map queryAccountInsideByPid(String pid) throws PlatformException{
        Map map=jdbcTemplate.queryForMap(QUERY_INSIDE_PID,Integer.parseInt(pid));
        return map;
    }

}
