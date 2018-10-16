package com.master.gm.service.manage;

import java.util.List;

import com.gmdesign.exception.GmException;
import com.master.gm.bean.UserQuestion;

/**
 * Created by DJL on 2017/7/27.
 *
 * @ClassName CustomerServiceIF
 * @Description 客户
 */
public interface CustomerServiceIF {
    UserQuestion getUserQuestion(String sid,String pid,long time)throws GmException;
    List<UserQuestion> queryAllQuestion()throws GmException;
    void saveAnswer(String sid,String pid,long time,int type,String context)throws GmException;
}
