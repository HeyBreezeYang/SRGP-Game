package com.master.gm.service.manage;

import com.gmdesign.exception.GmException;

import java.util.Map;

/**
 * Created by HP on 2018/6/30.
 */
public interface ItemsManageServiceIF {
    Map queryRoleItems(String rolename);

    void changeCurrency(String pid, String goodsId, String changeNum) throws GmException;

    void changeGoods(String pid, String goodsId, String changeNum) throws GmException;
}
