package com.gmdesign.util;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.gmdesign.bean.other.GmHashMap;
import com.gmdesign.exception.GmException;
import org.apache.commons.io.FileUtils;

/**
 * Created by DJL on 2017/6/9.
 *
 * @ClassName ReadFile
 * @Description 获取日志文件
 */
public class ReadFile {
    //外网地址
//    private static final String ROOT_PATH="/data/GM/gameLog";
    //内网地址
    private static final String ROOT_PATH="/root/GM/logstash/gameLog";
    private List<File> fileName;
    private String type;

    public String getType() {
        return type;
    }
    public ReadFile(String serverIP,String logType,String time){
        this(serverIP,logType,time,time);
    }

    public static List<GmHashMap> readFileOfOne(String serverIP,String logType,String start,String hour) throws IOException {
        String url=ROOT_PATH + "/" + serverIP + "/" + logType + "/" + start + "/" + hour+".log";
        File f =new File(url);
        if(!f.exists()){
            return Collections.emptyList();
        }
        List<String> data= FileUtils.readLines(f);
        if(data==null||data.isEmpty()){
            return Collections.emptyList();
        }
        List<GmHashMap> msg=new ArrayList<>();
        for(String m:data){
            msg.add(JSON.parseObject(m,GmHashMap.class));
        }
        return msg;
    }

    public ReadFile(String serverIP,String logType,String start,String end){
        this.type=logType;
        String filePath = ROOT_PATH + "/" + serverIP + "/" + logType + "/";
        fileName=new ArrayList<>();
        List<String> names= DateUtil.getEveryDayNoDurge(start,end);
        for (String name : names) {
            File f=new File(filePath.concat(name));
            File[] files=f.listFiles();
            if(files!=null&&files.length>0){
                fileName.addAll(Arrays.asList(files));
            }
        }
    }

    private List<String> convertMsg() throws GmException {
        List<String> msg=new ArrayList<>();
        for (File fn : fileName) {
            try {
                msg.addAll(FileUtils.readLines(fn));
            } catch (IOException e) {
                throw new GmException(e.getMessage());
            }
        }
        return msg;
    }

    private Map<String,List<String>> convertMsgMap() throws GmException {
        Map<String,List<String>> msg=new HashMap<>();
        for (File fn : fileName) {
            try {
                String key=fn.getName().substring(0,2);
                List<String> res=msg.get(key);
                if(res==null){
                    res=new ArrayList<>();
                }
                res.addAll(FileUtils.readLines(fn));
                msg.put(key,res);
            } catch (IOException e) {
                throw new GmException(e.getMessage());
            }
        }
        return msg;
    }

    public Map<String,List<GmHashMap>> getMsgMap() throws GmException {
        Map<String,List<GmHashMap>> msg=new HashMap<>();
        Map<String,List<String>> data=this.convertMsgMap();

        for(String m:data.keySet()){
            List<String> d=data.get(m);
            List<GmHashMap> mList=new ArrayList<>();
            for(String dk:d){
                mList.add(JSON.parseObject(dk,GmHashMap.class));
            }
            msg.put(m,mList);
        }
        return msg;
    }

    public List<GmHashMap> getMsgList() throws GmException {
        List<GmHashMap> msg=new ArrayList<>();
        List<String> data=this.convertMsg();
        for(String m:data){
            msg.add(JSON.parseObject(m,GmHashMap.class));
        }
        return msg;
    }

    public  List<GmHashMap> getMsg() throws GmException {
        List<GmHashMap> msg=getMsgList();
        dataSort(msg);
        return msg;
    }
    private void dataSort(List<GmHashMap> msg){
        msg.sort((o1, o2) -> {
            if(!(o1.get("dt") instanceof Long)){
                o1.put("dt",0L);
            }
            if(!(o2.get("dt") instanceof Long)){
                o2.put("dt",0L);
            }
            Long a = (Long) o1.get("dt");
            Long b = (Long) o2.get("dt");
            return a.compareTo(b);
        });
    }

    public  List<GmHashMap> getMsg(GmHashMap exclude) throws GmException {

        List<GmHashMap> msg=new ArrayList<>();
        List<String> data=this.convertMsg();
        boolean  flag=true;
        for(String m:data){
            GmHashMap map= JSON.parseObject(m,GmHashMap.class);
            if(exclude!=null && exclude.size()>0){
                for (String key:map.keySet()){
                    List<Object> pram= (List<Object>) exclude.get(key);
                    if(pram==null){
                        continue;
                    }
                    for (Object o:pram){
                        if(!map.get(key).toString().equals(o.toString())){
                            flag=false;
                            break;
                        }
                    }
                    if(!flag){
                        break;
                    }
                }
            }
            if(flag){
                msg.add(map);
            }else{
                flag=true;
            }
        }
        dataSort(msg);
        return msg;
    }

}
