package com.gmtime.gm.service.task;

import java.util.List;
import java.util.Set;

import com.gmdesign.exception.GmException;
import com.gmtime.gm.service.task.bean.TaskString;
import com.gmtime.rmi.service.bean.TimerTaskBean;

/**
 * 
* @ClassName TimerTaskServiceIF
* @Description 定时任务操作接口
* @author DJL
* @date 2015-12-23 上午11:22:50
*
 */
public interface TimerTaskServiceIF {
	/**
	 * 添加任务
	 * @param bean
	 * @throws GmException
	 */
	void addTask(TimerTaskBean bean)throws GmException;
	/**
	 * 修改任务信息
	 * @param bean
	 * @throws GmException
	 */
	void updateTask(TimerTaskBean bean)throws GmException;
	/**
	 * 暂停/开启状态
	 * @param name
	 * @param state
	 * @throws GmException
	 */
	void changeState(String name, int state)throws GmException;
	/**
	 * 删除任务
	 * @param name
	 * @throws GmException
	 */
	void deleteTask(String name)throws GmException;
	/**
	 * 查看全部任务
	 * @return
	 * @throws GmException
	 */
	List<TimerTaskBean> queryTaskAll()throws GmException;
	/**
	 * 保存任务使用对象缓存
	 * @param beans
	 * @throws GmException
	 */
	void saveCache(List<TaskString> beans)throws GmException;
	/**
	 * 查询任务使用对象
	 * @return
	 * @throws GmException
	 */
	List<TaskString> queryCacheObj()throws GmException;
	/**
	 * 删除保存记录
	 * @throws GmException
	 */
	void deleteCacheObj()throws GmException;
	/**
	 * 备份执行未完成的任务
	 * @param beans
	 * @throws GmException
	 */
	void backTask(Set<TimerTaskBean> beans)throws GmException;
}
