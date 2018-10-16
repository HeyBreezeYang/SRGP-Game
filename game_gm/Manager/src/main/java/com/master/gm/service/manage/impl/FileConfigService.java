package com.master.gm.service.manage.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import com.gmdesign.util.SendUtil;
import com.master.bean.dispoly.GameServer;
import com.master.gm.BaseService;
import com.master.gm.service.manage.FileConfigServiceIF;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/8/1.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
public class FileConfigService extends BaseService implements FileConfigServiceIF{

    private static final String ACTIVITY ="activity";
    private static final String ACTIVITY_BATCH="activityBatch";


    private String convertActivity(Properties pro) throws GmException {
        Map<String,String> config=new HashMap<>();
        config.put("id",UUID.randomUUID().toString());
        config.put("autoBev",pro.getProperty("是否自动执行行为"));
        config.put("clientAtts",pro.getProperty("客户端参数"));
        config.put("endDate",pro.getProperty("结束时间"));
        config.put("refInterval",pro.getProperty("刷新间隔(m)"));
        config.put("startDate",pro.getProperty("开始时间"));
        config.put("execBevStartDate",pro.getProperty("行为执行开始时间"));
        config.put("bevListenEndDate",pro.getProperty("冲榜结束时间"));
        config.put("workMode",pro.getProperty("工作方式"));
        config.put("conds",pro.getProperty("条件"));
        config.put("bevs",pro.getProperty("行为"));
        return JSON.toJSONString(config);
    }

    private List<String> convertActivityBatch(File file) throws IOException, GmException {
        ZipFile zf =new ZipFile(file);
        Enumeration<? extends ZipEntry> entryEnum = zf.entries();
        ZipEntry entry ;
        List<String> allConfig=new ArrayList<>();
        while (entryEnum.hasMoreElements()) {
            entry = entryEnum.nextElement();
            Properties pro=new Properties();
            pro.load(new InputStreamReader(zf.getInputStream(entry)));
            allConfig.add(convertActivity(pro));
        }
        return allConfig;
    }


    private GmHashMap sendCfg(String[]sid, String cfg, String mouth) throws GmException, IOException {
        String p=mouth.concat(java.net.URLEncoder.encode(cfg, "UTF-8"));
        int index = p.indexOf("?");
        String p1 = p.substring(index+1);
        String path = p.substring(0,index+1);

        GmHashMap map =new GmHashMap();
        for(String s :sid){
            String url=this.dao.fetch(GameServer.class,Cnd.where("serverID","=",sid)).getServerUrl()+path;
            System.out.println(url);
            map.put(s, SendUtil.sendHttpMsg(url,p1));
        }
        return map;
    }

    @Override
    public String obtainGameConfig(File file, String type, String[] sid) throws GmException {
        String cfg;
        try {
            switch (type){
                case ACTIVITY:
                    Properties pro=new Properties();
                    pro.load(new FileReader(file));
                    cfg=convertActivity(pro);
                    return JSON.toJSONString(sendCfg(sid,cfg,"/releaseActivity?activity="));
                case ACTIVITY_BATCH:
                    List<String> allConfig=convertActivityBatch(file);
                    Map res =new HashMap();
                    for(String con:allConfig){
                        res.putAll(sendCfg(sid,con,"/releaseActivity?activity="));
                    }
                    return JSON.toJSONString(res);
                default:
                    throw new GmException("未知类型文件~~！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
