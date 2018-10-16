package com.manager.service;

import java.util.List;

import com.manager.bean.AppBean;
import com.manager.bean.VersionBean;
import com.manager.exception.ManageException;

/**
 * Created by DJL on 2017/5/15.
 *
 * @ClassName AddGameServiceIF
 * @Description
 */
public interface AddGameServiceIF {
     String getGmConfig(int id)throws ManageException;
     List<VersionBean> queryAllVersion() throws ManageException;
     void setGameKey(AppBean bean)throws ManageException;
}
