package com.manager.service;


import java.util.List;
import java.util.Map;

import com.manager.bean.*;
import com.manager.exception.ManageException;

/**
 * 
* @ClassName MenuServiceIF
* @Description 菜单业务
* @author DJL
* @date 2015-11-9 下午8:00:31
*
 */
public interface MenuServiceIF {
	Map<String,Map<String,List<MenuBean>>> getAllMenu()throws ManageException;
	/*
	 * 添加游戏
	 */
	void createGameMenu(GameBean game)throws ManageException;
	void updateGameMenu(GameBean game)throws ManageException;
	void deleteGameMenu(int game)throws ManageException;
	List<GameBean> queryGameMenu()throws ManageException;
	/*
	 * 添加版本
	 */
	void createVersionMenu(VersionBean version)throws ManageException;
	void updateVersionMenu(VersionBean version)throws ManageException;
	void deleteVersionMenu(int version)throws ManageException;
	List<VersionBean> queryVersionMenu(int game)throws ManageException;
	VersionBean queryVersion(int version)throws ManageException;
	/*
	 * 增加分组
	 */
	void createGroupMenu(GroupBean group)throws ManageException;
	void updateGroupMenu(GroupBean group)throws ManageException;
	void deleteGroupMenu(int group)throws ManageException;
	List<GroupBean> queryGroupMenuForVersion(int version)throws ManageException;
	List<GroupBean> queryGroupMenuForGroup(int group)throws ManageException;
	GroupBean queryGroup(int id)throws ManageException;
	/*
	 * 增加菜单
	 */
	void createMenu(MenuBean menu)throws ManageException;
	void updateMenu(MenuBean menu)throws ManageException;
	void deleteMenu(int menu)throws ManageException;
	List<MenuBean> queryMenu(int group)throws ManageException;
	/*
	 * 增加某用户权限
	 */
	void updatePower(String user, int[] menuId)throws ManageException;
	boolean addPower(int uid, int mid)throws ManageException;
}
