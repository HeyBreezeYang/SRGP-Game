package com.master.bean.web;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.gmdesign.bean.other.GmHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by DJL on 2018/4/2.
 *
 * @ClassName gm
 * @Description
 */
@Getter
@Setter
@ToString
public class ShopDesign {
    private String type;
    private String start;
    private String end;
    private String itemIds;
    private String itemNum;
    private String refHours;
    private String id;
    private String server;
    private GmHashMap res=null;
    public ShopDesign(){}

    public ShopDesign(Map data,String sid){
        this.server=sid;
        this.id= data.get("id").toString();
        this.type=data.get("type").toString();
        this.start=data.get("sTime").toString();
        this.end=data.get("eTime").toString();
        this.itemIds=setStringValue((JSONArray) data.get("items"));
        this.itemNum=setStringValue((JSONArray) data.get("itemsNum"));
        this.refHours=setStringValue((JSONArray) data.get("ref"));
    }
    private String setStringValue(JSONArray v){
        StringBuilder kBuilder = new StringBuilder(String.valueOf(v.get(0)));
        for (int i = 1; i<v.size(); i++){
            kBuilder.append(",").append(v.get(i));
        }
        return kBuilder.toString();
    }

    public GmHashMap getMapData(){
        if (res==null){
            res=new GmHashMap();
        }
        if(!this.id.equals("-1")){
            res.put("id",this.id);
        }
        res.put("type",this.type);
        res.put("sTime",this.start);
        res.put("eTime",this.end);
        res.put("items",this.itemIds.split(","));
        res.put("itemsNum",this.itemNum.split(","));
        res.put("ref",this.refHours.split(","));
        return res;
    }
}
