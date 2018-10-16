package com.master.gm.service.manage.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.gmdesign.util.DateUtil;
import com.master.bean.dispoly.CodeBindingConfig;
import com.master.bean.dispoly.CodeConfig;
import com.master.bean.dispoly.GameCode;
import com.master.gm.BaseService;
import com.master.gm.service.manage.CodeServiceIF;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.IocBean;

/**
 * Created by DJL on 2017/12/11.
 *
 * @ClassName GM
 * @Description
 */
@IocBean
public class CodeService extends BaseService implements CodeServiceIF{

    @Override
    public List<CodeConfig> queryBinding() {
        return this.dao.query(CodeConfig.class,Cnd.where("specialType","=",2)
                .and("endTime",">=",DateUtil.getFullDateString(new Date())));
    }

    @Override
    public List<CodeConfig> queryAll() {
        return this.dao.query(CodeConfig.class,null);
    }

    private void addCode(List<GameCode> prams,List<String> judge,int aid,String prize,FileWriter writer) throws IOException {
        if(!judge.contains(prize)){
            GameCode code=new GameCode();
            code.setAid(aid);
            code.setPrizeCode(prize);
            prams.add(code);
            writer.write(prize+"\n");
            writer.flush();
            judge.add(prize);
        }
    }
    @Override
    public File addPrizeCode(CodeConfig bean) throws IOException {
        File word =File.createTempFile("tst","txt");
        FileWriter writer=new FileWriter(word);
        bean.setCreateTime(System.currentTimeMillis());
        List<GameCode> prams=new ArrayList<>();
        List<String> judge=new ArrayList<>();
        if(bean.getPrizeCode()!=null&&bean.getPrizeCode().length()>1){
            String[] prizes=bean.getPrizeCode().split(",");
            bean.setNum(prizes.length);
            bean=this.dao.insert(bean);
            for (String prize : prizes) {
                addCode(prams,judge,bean.getId(),prize,writer);
            }
        }else{
            bean=this.dao.insert(bean);
            for(int i=0;i<bean.getNum();i++){
                String prize=UUID.randomUUID().toString().replaceAll("-","").substring(3,17);
                addCode(prams,judge,bean.getId(),prize,writer);
            }
        }
        judge.clear();
        this.dao.fastInsert(prams);
        writer.close();
        return word;
    }

    @Override
    public void addBindCode(int aid,String bindingId) {
        if(bindingId==null||bindingId.length()<1){
            return;
        }
        String[] bid=bindingId.split(",");
        List<CodeBindingConfig> list=new ArrayList<>();
        for(String b:bid){
            CodeBindingConfig bind=new CodeBindingConfig();
            bind.setAid(aid);
            bind.setBid(Integer.parseInt(b));;
            list.add(bind);
        }
        this.dao.fastInsert(list);
    }

    @Override
    public void deleteCode(int aid) {
        this.dao.delete(CodeConfig.class,aid);
        this.dao.clear(GameCode.class,Cnd.where("aid","=",aid));
        this.dao.clear(CodeBindingConfig.class,Cnd.where("bid","=",aid).or("aid","=",aid));
    }

    @Override
    public Map exportCode(int aid) {
        Map map = new HashMap();
        List<String> list = new ArrayList<>();
        List<GameCode> gameCodes = this.dao.query(GameCode.class,Cnd.where("aid","=",aid));
        for (GameCode code:gameCodes) {
            list.add(code.getPrizeCode());
        }
        map.put("code",list);
        List<CodeConfig> codeConfigs = this.dao.query(CodeConfig.class,Cnd.where("id","=",aid));
        if (codeConfigs !=null && codeConfigs.size()>0){
            map.put("fileName",codeConfigs.get(0).getDsp());
        }else {
            map.put("fileName","未命名");
        }
        return map;
    }


}
