package com.manager.service;


import java.util.List;

import com.manager.bean.LogBean;
import com.manager.bean.UserBean;
import com.manager.exception.ManageException;

/**
 * 
* @ClassName GmLoginLogServiceIF
* @Description 关于登录日志业务
* @author DJL
* @date 2015-11-5 下午4:45:13
*
 */
public interface GmLoginLogServiceIF {
	/**
	 * 日志保存
	 */
	boolean saveLoginLog(LogBean log)throws ManageException;
	/**
	 * 日志查询
	 */
	List<LogBean> queryLoginLog(UserBean user, String start, String end)throws ManageException;
}
