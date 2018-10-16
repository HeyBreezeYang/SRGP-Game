package com.manager.service.impl.ws;


import java.util.*;

import com.manager.bean.*;
import com.manager.cache.PlatFormSql;
import com.manager.exception.ManageException;
import com.manager.service.UserServiceIF;
import com.manager.service.impl.BaseService;
import com.manager.util.SqlUtil;
import com.manager.util.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class UserService extends BaseService implements UserServiceIF {

	@Override
	public UserBean judgePassword(UserBean user) throws ManageException {
		UserBean bean =this.dao.fetch(UserBean.class, Cnd.where("name","=",user.getName()));
		if(bean==null){
			throw new ManageException("账号不存在！~");
		}
		if(!bean.getPassword().equals(user.getPassword())){
			throw new ManageException("密码输入有误！~");
		}
		return queryUser(bean.getId());
	}

	@Override
	public void resetPassword(UserBean user, String newPsd) throws ManageException {
		if(user.getPassword().equals(newPsd)){
			throw new ManageException("密码不能一样~");
		}
		user.setPassword(StringUtil.changePassword(user.getPassword()));
		UserBean bean =this.dao.fetch(UserBean.class, Cnd.where("id","=",user.getId()).and("password","=",user.getPassword()));
		if(bean==null){
			throw new ManageException("原始密码错误~");
		}
		bean.setPassword(StringUtil.changePassword(newPsd));
		this.dao.update(bean);
	}

	@Override
	public boolean judgeExist(String name) throws ManageException {
		return false;
	}

	@Override
	public void createUser(UserBean user) throws ManageException {
		user.setCreateTime(System.currentTimeMillis());
		user.setPassword(StringUtil.changePassword(user.getPassword()));
		UserBean bean =this.dao.fetch(UserBean.class, Cnd.where("name","=",user.getName()));
		if(bean!=null){
			throw new ManageException("账号已经存在！~");
		}
		if(this.dao.insert(user).getId()<0){
			throw new ManageException("添加账号错误~~");
		}
	}

	private Map<Integer, Map<Integer,GroupBean>> convertGroup(List<PlatFormHashMap> groupMap){
		Map<Integer, Map<Integer, GroupBean>> group=new HashMap<>();
		for (PlatFormHashMap aGroupMap : groupMap) {
			Integer vid = Integer.parseInt(aGroupMap.get("versionId").toString());
			Map<Integer, GroupBean> gmap = group.get(vid);
			if (gmap == null) {
				gmap = new HashMap<>();
			}
			GroupBean gb = new GroupBean(aGroupMap);
			gmap.put(gb.getId(), gb);
			group.put(vid, gmap);
		}
		return group;
	}

	private GroupBean getGroupList(Map<Integer,GroupBean> mg,GroupBean gba){
		if(gba.getId()!=gba.getGid()){
			GroupBean gbat=mg.get(gba.getGid());
			gbat.getGroups().add(gba);
			return getGroupList(mg,gbat);
		}else{
			return gba;
		}
	}

	@Override
	public UserBean queryUser(int id) throws ManageException {
		UserBean bean =this.dao.fetch(UserBean.class,id);
		bean.setFather(this.dao.fetch(UserBean.class,bean.getUid()));
		Sql sql = SqlUtil.getSql(PlatFormSql.MENU_QUERY_USER,id);
		this.dao.execute(sql);
		Sql sql2=SqlUtil.getSql(PlatFormSql.GROUP_QUERY,id);
		this.dao.execute(sql2);
		List<PlatFormHashMap> menuMap=sql.getList(PlatFormHashMap.class);
		List<PlatFormHashMap> groupMap=sql2.getList(PlatFormHashMap.class);
		Map<Integer, Map<Integer,GroupBean>> group=convertGroup(groupMap);
		Map<GameBean,Set<VersionBean>> gv=new HashMap<>();
		Set<Integer> gids=new HashSet<>();
		for (PlatFormHashMap aMenuMap : menuMap) {
			MenuBean mBean = new MenuBean();
			mBean.setId(Integer.parseInt(aMenuMap.get("mid").toString()));
			mBean.setTitle(aMenuMap.get("title").toString());
			mBean.setUrl(aMenuMap.get("murl").toString());
			Map<Integer, GroupBean> gmap = group.get(Integer.parseInt(aMenuMap.get("versionId").toString()));
			int g = Integer.parseInt(aMenuMap.get("groupid").toString());
			gmap.get(g).getMenu().add(mBean);
			gids.add(g);
			GameBean gb = new GameBean();
			gb.setId(Integer.parseInt(aMenuMap.get("id").toString()));
			gb.setName(aMenuMap.get("gname").toString());
			Set<VersionBean> verset = gv.get(gb);
			if (verset == null) {
				verset = new HashSet<>();
			}
			VersionBean vb = new VersionBean();
			vb.setId(Integer.parseInt(aMenuMap.get("vsnid").toString()));
			vb.setUrl(aMenuMap.get("vurl").toString());
			vb.setVersionName(aMenuMap.get("version").toString());
			verset.add(vb);
			gv.put(gb, verset);
		}

		List<GameBean> games=new ArrayList<>();
		int h=0;
		for(GameBean b:gv.keySet()){
			Set<VersionBean> vers=gv.get(b);
			for(VersionBean v:vers){
				Map<Integer,GroupBean> mg=group.get(v.getId());
				for(int j :gids){
					GroupBean gba=mg.get(j);
					if(gba!=null){
						v.getGroup().add(getGroupList(mg,gba));
					}
				}
			}
			b.setVersion(new ArrayList<>(vers));
			b.setIndex(h++);
			games.add(b);
		}
		bean.setGame(games);
		return bean;
	}

	@Override
	public UserBean queryUser(String name) throws ManageException {
		return null;
	}

	@Override
	public List<UserBean> allUser() throws ManageException {
		return this.dao.query(UserBean.class,null);
	}

	@Override
	public boolean userDeregister(int id) throws ManageException {
		return false;
	}

	@Override
	public List<Integer> userPower(String name) throws ManageException {
		List<Integer> res =new ArrayList<>();
		UserBean bean =this.dao.fetch(UserBean.class, Cnd.where("name","=",name));
		if(bean==null){
			throw new ManageException("账号不存在！~");
		}
		List<Record> list=this.dao.query("uid_mid",Cnd.where("userId","=",bean.getId()));
		for(Record r:list){
			res.add(r.getInt("menuId"));
		}
		return res;
	}
}
