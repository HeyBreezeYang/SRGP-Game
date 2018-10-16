package com.master.gm.service.login;


import java.util.List;

import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;

/**
 * 
* @ClassName GM 从登录系统到管理系统的验证
* @Description ManagerLoginServiceIF
* @author DJL
* @date 2015-12-9 下午6:32:53
*
 */
public interface ManagerLoginServiceIF {
	String judgeUserData(String menuData, UserForService user)throws GmException;

	List<String[]> initWindowComprehensive()throws GmException;
	List<String[]> initWindowRate()throws GmException;
}
