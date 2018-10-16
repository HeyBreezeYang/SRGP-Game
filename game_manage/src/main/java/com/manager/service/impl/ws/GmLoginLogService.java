package com.manager.service.impl.ws;

import java.util.List;

import com.manager.bean.LogBean;
import com.manager.bean.UserBean;
import com.manager.exception.ManageException;
import com.manager.service.GmLoginLogServiceIF;
import com.manager.service.impl.BaseService;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class GmLoginLogService extends BaseService implements GmLoginLogServiceIF {

	@Override
	public boolean saveLoginLog(LogBean log) throws ManageException {
		int i=this.dao.insert(log).getId();
		return i>0;
	}

	@Override
	public List<LogBean> queryLoginLog(UserBean user, String start, String end) throws ManageException {
		return null;
	}
}
