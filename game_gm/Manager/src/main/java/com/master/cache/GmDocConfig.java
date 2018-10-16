package com.master.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gmdesign.util.csv.CsvUtil;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import javax.annotation.PostConstruct;

/**
 * Created by DJL on 2017/7/15.
 *
 * @ClassName GmDocConfig
 * @Description
 */
public class GmDocConfig {
    private static Log log = Logs.get();
    private static final String url="/data/GM/server/gm_config/";
    public static final Map<String,String> DUP=new HashMap<>();
    public static final Map<String,String> SCIENCE=new HashMap<>();
    public static final Map<String,String> TASK=new HashMap<>();
    public static final Map<String,String> CMD=new HashMap<>();
    public static final Map<String,String> HERO=new HashMap<>();
    public static final Map<String,String> GOODS=new HashMap<>();
    public static final Map<String,String> SKILL=new HashMap<>();
    public static final Map<String,String[]> STEP=new HashMap<>();
    public static final Map<String,String> STORE=new HashMap<>();

    public static void clearAll(){
        HERO.clear();
        GOODS.clear();
        SKILL.clear();
        STEP.clear();
        CMD.clear();
        TASK.clear();
        SCIENCE.clear();
        DUP.clear();

    }

    public static void initFile(){
       /* HERO.putAll(convertCSV(url.concat("hero.csv"), 1));
        GOODS.putAll(convertCSV(url.concat("gem.csv"), 1));
        GOODS.putAll(convertCSV(url.concat("rune.csv"), 1));
        GOODS.putAll(convertCSV(url.concat("equip.csv"), 1));
        SKILL.putAll(convertCSV(url.concat("skill.csv"), 1));
        SKILL.putAll(convertCSV(url.concat("Pskill.csv"), 1));
        STEP.putAll(convertCSV(url.concat("step.csv"), 1,2));*/
        GOODS.putAll(convertCSV(url.concat("goods.csv"), 1));
        CMD.putAll(convertCSV(url.concat("cmd.csv"), 1));
        STORE.putAll(convertCSV(url.concat("store.csv"),1));
        /*DUP.putAll(convertCSV(url.concat("dup.csv"), 1));*/
//        System.out.println(GOODS);
    }
    private static Map convertCSV(String path, int... value){
        try {
            List<String[]> data = CsvUtil.readerCsv(path);
            if (!data.isEmpty()) {
                if(value.length>1){
                    Map<String, String[]> map =new HashMap<>();
                    for (String[] aData : data) {
                        String mapKey = aData[0].trim();
                        String[] mapValue = new String[value.length];
                        for (int i = 0; i < value.length; i++) {
                            mapValue[i] = aData[value[i]].trim();
                        }
                        map.put(mapKey, mapValue);
                    }
                    return map;
                }else{
                    Map<String, String> map=new HashMap<>();
                    for (String[] aData : data) {
                        String mapKey = aData[0].trim();
                        String mapValue = aData[value[0]].trim();
                        map.put(mapKey, mapValue);
                    }
                    return map;
                }
            }else {
                log.info("获取配置文件为空");
            }
        } catch (Exception e) {
            //log.error(e.getMessage());
            log.info("获取配置文件有错");
        }
        return null;
    }
}
