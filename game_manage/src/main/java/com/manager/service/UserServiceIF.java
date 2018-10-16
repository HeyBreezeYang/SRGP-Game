package com.manager.service;


import java.util.List;

import com.manager.bean.UserBean;
import com.manager.exception.ManageException;

public interface UserServiceIF {
	/**
	 * 密码验证
	 */
	UserBean judgePassword(UserBean user)throws ManageException;
	/**
	 * 修改密码
	 */
	void resetPassword(UserBean user, String newPsd)throws ManageException;
	/**
	 * 重复验证
	 */
	boolean judgeExist(String name)throws ManageException;
	/**
	 * 新增用户
	 */
	void createUser(UserBean user)throws ManageException;
	/**
	 * 用户查询
	 */
	UserBean queryUser(int id)throws ManageException;
	/**
	 * 用户查询
	 */
	UserBean queryUser(String name)throws ManageException;
	/**
	 * 所以用户
	 */
	List<UserBean> allUser()throws ManageException;
	/**
	 * 账号注销
	 */
	boolean userDeregister(int id)throws ManageException;
	/**
	 * 用户权限
	 */
	List<Integer> userPower(String name)throws ManageException;
}
