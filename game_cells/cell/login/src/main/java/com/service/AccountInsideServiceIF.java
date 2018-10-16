package com.service;

import com.design.exception.PlatformException;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/23.
 */
public interface AccountInsideServiceIF {
    int saveOrUpdate(String msg) throws PlatformException;

    List<Map<String,Object>> queryInsideAccount();

    List<Map<String,Object>> queryInsideAccountById(String id);

    int delInsideAccount(String id);

    int insideAccountTE(String id, String type,String loginName);

    List<Map<String,Object>> queryInsideAccountCondition(String msg);

    Map queryAccountAdmin(String rolename) throws PlatformException;

    Map queryAccountInsideByPid(String pid) throws PlatformException;

}
