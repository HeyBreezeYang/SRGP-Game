package com.manager.service.impl.ws;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manager.bean.*;
import com.manager.cache.PlatFormSql;
import com.manager.exception.ManageException;
import com.manager.service.MenuServiceIF;
import com.manager.service.impl.BaseService;
import com.manager.util.SqlUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class MenuService extends BaseService implements MenuServiceIF {


	@Override
	public Map<String, Map<String, List<MenuBean>>> getAllMenu() throws ManageException {
		Sql sql = SqlUtil.getSql(PlatFormSql.QUERY_ALL_MENU);
		this.dao.execute(sql);
		List<PlatFormHashMap> menuMap=sql.getList(PlatFormHashMap.class);
		return convertMenu(menuMap);
	}

	private Map<String,Map<String,List<MenuBean>>> convertMenu(List<PlatFormHashMap> menuMap) {
		Map<String,Map<String,List<MenuBean>>> all =new HashMap<>();
		for(PlatFormHashMap map :menuMap){
			Map<String,List<MenuBean>> a=all.get(map.get("name").toString());
			if(a==null){
				a=new HashMap<>();
			}
			List<MenuBean> b =a.get(map.get("version").toString());
			if(b==null){
				b=new ArrayList<>();
			}
			MenuBean bean=new MenuBean();
			bean.setId(Integer.parseInt(map.get("id").toString()));
			bean.setTitle(map.get("title").toString());
			b.add(bean);
			a.put(map.get("version").toString(),b);
			all.put(map.get("name").toString(),a);
		}
		return all;
	}

	@Override
	public void createGameMenu(GameBean game) throws ManageException {
		this.dao.insert(game);
	}

	@Override
	public void updateGameMenu(GameBean game) throws ManageException {
		this.dao.update(game);
	}

	private void delGroupOfMenu(GroupBean gb) throws ManageException {
		List<MenuBean> menuBeans=this.queryMenu(gb.getId());
		Sql[] allSql=new Sql[menuBeans.size()];
		for (int i=0;i<menuBeans.size();i++){
			this.dao.delete(menuBeans.get(i));
			allSql[i] = Sqls.create("delete from uid_mid where menuId=@mid;");
			allSql[i].setParam("mid",menuBeans.get(i).getId());
		}
		this.dao.execute(allSql);
		List<GroupBean> groupBeans=this.queryGroupMenuForGroup(gb.getId());
		for (GroupBean gb2:groupBeans){
			delGroupOfMenu(gb2);
		}
		this.dao.delete(gb);
	}

	@Override
	public void deleteGameMenu(int game) throws ManageException {
		List<VersionBean> version=this.queryVersionMenu(game);
		for(VersionBean vb:version){
			List<GroupBean> group=this.queryGroupMenuForVersion(vb.getId());
			for(GroupBean gb:group){
				delGroupOfMenu(gb);
			}
			this.dao.delete(vb);
		}
		this.dao.delete(GameBean.class,game);
	}

	@Override
	public List<GameBean> queryGameMenu() throws ManageException {
		return this.dao.query(GameBean.class,null);
	}

	@Override
	public void createVersionMenu(VersionBean version) throws ManageException {
		this.dao.insert(version);
	}

	@Override
	public void updateVersionMenu(VersionBean version) throws ManageException {
		this.dao.update(version);
	}

	@Override
	public void deleteVersionMenu(int version) throws ManageException {
		List<GroupBean> group=this.queryGroupMenuForVersion(version);
		for(GroupBean gb:group){
			delGroupOfMenu(gb);
		}
		this.dao.delete(VersionBean.class,version);
	}

	@Override
	public List<VersionBean> queryVersionMenu(int game) throws ManageException {
		return this.dao.query(VersionBean.class,Cnd.where("gameId","=",game));
	}

	@Override
	public VersionBean queryVersion(int version) throws ManageException {
		return this.dao.fetch(VersionBean.class,version);
	}

	@Override
	public void createGroupMenu(GroupBean group) throws ManageException {
		group=this.dao.insert(group);
		if(group.getGid()==null){
			group.setGid(group.getId());
			this.dao.update(group);
		}
	}

	@Override
	public void updateGroupMenu(GroupBean group) throws ManageException {
		this.dao.updateIgnoreNull(group);
	}

	@Override
	public void deleteGroupMenu(int group) throws ManageException {
		delGroupOfMenu(this.dao.fetch(GroupBean.class,group));
	}

	@Override
	public List<GroupBean> queryGroupMenuForVersion(int version) throws ManageException {

		return this.dao.query(GroupBean.class,Cnd.where("versionId","=",version).and("icon","!=",""));
	}

	@Override
	public List<GroupBean> queryGroupMenuForGroup(int group) throws ManageException {
		return this.dao.query(GroupBean.class,Cnd.where("gid","=",group).and("id","!=",group));
	}

	@Override
	public GroupBean queryGroup(int id) throws ManageException {
		return this.dao.fetch(GroupBean.class,id);
	}

	@Override
	public void createMenu(MenuBean menu) throws ManageException {
		menu=this.dao.insert(menu);

		Sql sql = Sqls.create("insert into uid_mid values(1,@mid);");
		sql.setParam("mid",menu.getId());
		this.dao.execute(sql);
	}

	@Override
	public void updateMenu(MenuBean menu) throws ManageException {
		this.dao.update(menu);
	}

	@Override
	public void deleteMenu(int menu) throws ManageException {
		this.dao.delete(MenuBean.class,menu);

		Sql sql = Sqls.create("delete from uid_mid where menuId=@mid;");
		sql.setParam("mid",menu);
		this.dao.execute(sql);

	}

	@Override
	public List<MenuBean> queryMenu(int group) throws ManageException {
		return this.dao.query(MenuBean.class,Cnd.where("groupId","=",group));
	}

	@Override
	public void updatePower(String user, int[] menuId) throws ManageException {
		UserBean bean =this.dao.fetch(UserBean.class, Cnd.where("name","=",user));

		Sql s = Sqls.create("delete from uid_mid where userId=@uid;");
		s.setParam("uid",bean.getId());
		this.dao.execute(s);

		Sql[] allSql=new Sql[menuId.length];
		String sql="insert into uid_mid values(@uid,@mid);";
		for(int i=0;i<allSql.length;i++){
			allSql[i]=Sqls.create(sql);
			allSql[i].setParam("uid",bean.getId()).setParam("mid",menuId[i]);
		}
		this.dao.execute(allSql);
	}

	@Override
	public boolean addPower(int uid, int mid) throws ManageException {
		return false;
	}
}
