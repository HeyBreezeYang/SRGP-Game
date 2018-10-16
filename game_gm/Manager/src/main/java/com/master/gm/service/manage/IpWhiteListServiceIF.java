package com.master.gm.service.manage;

import com.gmdesign.exception.GmException;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/7/6.
 */
public interface IpWhiteListServiceIF {
    List<Map> getAllIpWhiteList();

    void addIpWhiteList(String ip, String remark) throws GmException;

    void delIpWhiteList(String id) throws GmException;
}
