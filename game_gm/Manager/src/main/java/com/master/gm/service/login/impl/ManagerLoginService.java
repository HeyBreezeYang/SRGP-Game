package com.master.gm.service.login.impl;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.GroupBean;
import com.gmdesign.bean.MenuBean;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.DateUtil;
import com.gmdesign.util.StringUtil;
import com.master.bean.ComputerTableBean;
import com.master.gm.bean.GmSql;
import com.master.gm.BaseService;
import com.master.gm.service.login.ManagerLoginServiceIF;
import com.master.util.SqlUtil;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;


/**
 * 
* @ClassName ManagerLoginService
* @Description GM登录系统跳转过来的信息验证
* @author DJL
* @date 2015-12-10 下午7:40:26
*
 */
@IocBean
public class ManagerLoginService extends BaseService implements ManagerLoginServiceIF  {

	@Override
	public String judgeUserData(String menuData, UserForService user)
			throws GmException {
		if(user==null||user.getName()==null||user.getUserKey()==null){
			throw new GmException("用户信息有误~~~");
		}
		String key=new String(StringUtil.hex2byte(user.getUserKey().getBytes()));
		if(!key.equals(user.getName())){
			throw new GmException("登录方式有误~~~");
		}
		if(menuData.length()<1){
			throw new GmException("菜单数据缺失~~~");
		}
		List<GroupBean> Menus;
		try{
			Menus = JSON.parseArray(menuData,GroupBean.class);
		}catch (Exception e) {
			throw new GmException("菜单数据有误~~~");
		}
		StringBuilder sb =new StringBuilder(1024);
		return convertMenu(Menus,sb);
	}
	private String dataIsNull(Object obj){
		return obj==null?"":obj.toString();
	}

	@Override
	public List<String[]> initWindowComprehensive() throws GmException {
		List<String[]> result=new ArrayList<>();
		Date now =new Date();
		Date start=DateUtil.getDaytoDay(now,-1);
		Date end=DateUtil.getDaytoDay(now,-7);
		Sql sql = SqlUtil.getSql(GmSql.GET_COMPREHENSIVE,DateUtil.getDateString(end),DateUtil.getDateString(start));
		this.dao.execute(sql);
		List<GmHashMap> mapList=sql.getList(GmHashMap.class);
		for(GmHashMap obj:mapList){
			String[] res=new String[13];
			res[11]=dataIsNull(obj.get("newPlayer"));
			res[12]=dataIsNull(obj.get("dayPayNum"));
			res[0]=dataIsNull(obj.get("logTime"));
			res[1]=dataIsNull(obj.get("player"));
			res[2]=dataIsNull(obj.get("register"));
			res[3]=dataIsNull(obj.get("login"));
			res[4]=dataIsNull(obj.get("pay"));
			res[5]=dataIsNull(obj.get("dayPay"));
			res[6]=dataIsNull(obj.get("payNum"));
			Object o1=obj.get("arppu")!=null?obj.get("arppu"):0;
			Object o2=obj.get("permeate")!=null?obj.get("permeate"):0;
			Object o3=obj.get("convert")!=null?obj.get("convert"):0;
			Object o4=obj.get("arpu")!=null?obj.get("arpu"):0;
			res[7]= ComputerTableBean.convertThree(o1);
			res[8]=ComputerTableBean.convertThree(o4);
			res[9]=ComputerTableBean.convertTwo(Double.parseDouble(o2.toString()));
			res[10]=ComputerTableBean.convertTwo(Double.parseDouble(o3.toString()));
			result.add(res);
		}
		return result;
	}

	@Override
	public List<String[]> initWindowRate() throws GmException {
		Date now =new Date();
		Date start= DateUtil.getDaytoDay(now,-1);
		Date end=DateUtil.getDaytoDay(now,-7);
		Sql sql = SqlUtil.getSql(GmSql.QUERY_7_LOSS,DateUtil.getDateString(end),DateUtil.getDateString(start));
		this.dao.execute(sql);
		List<GmHashMap> mapList=sql.getList(GmHashMap.class);
		Map<String,String[]> reMap=new HashMap<>();
		String key;
		for(GmHashMap re:mapList){
			key=re.get("logTime").toString()+re.get("createNum").toString();
			String[] r=reMap.get(key);
			if (r==null){
				r=new String[9];
				r[0]=re.get("logTime").toString();
				r[1]=re.get("createNum").toString();
			}
			r[(int) re.get("tDay")]=ComputerTableBean.convertProportion(re.get("loss"));
			reMap.put(key,r);
		}
		return new ArrayList<>(reMap.values());
	}

	private static String convertMenu(Collection<GroupBean> Menus, StringBuilder sb){
		String k="";
		for(GroupBean gb:Menus){
			if(gb.getGroupIcon()!=null&&gb.getGroupIcon().length()>1){
				k="<i class='"+gb.getGroupIcon()+"'></i>";
			}
			sb.append("<li><a href='#'>").append(k).append("<span class='title'>").append(gb.getGroupName()).append("</span></a>");
			sb.append("<ul>");
			if(gb.getMenu()!=null&&!gb.getMenu().isEmpty()){
				for(MenuBean mb:gb.getMenu()){
					sb.append("<li><a href='#' onclick=\"forwardPage('").append(mb.getUrl()).append("','").
							append(mb.getId()).append("');\"><span class='title'>").append(mb.getTitle()).append("</span></a></li>");
				}
			}
			if(gb.getGroups()!=null&&!gb.getGroups().isEmpty()){
				convertMenu(gb.getGroups(),sb);
			}
			sb.append("</ul>");
			sb.append("</li>");
		}
		return sb.toString();
	}
}
