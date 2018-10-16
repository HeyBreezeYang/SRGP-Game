package com.master.gm.service.manage;

import com.gmdesign.bean.other.UserForService;
import com.gmdesign.exception.GmException;
import com.master.bean.dispoly.GmMail;
import org.nutz.dao.entity.Record;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by HP on 2018/6/4.
 */
public interface MailServiceIF {
    List<Record> getAllMail(String mailStatus,String startDate,String endDate) throws ParseException;

    List<Record> getAllServer();

    List<Record> getAllChannel();

    void saveOrUpdateMail(String mailMsg, GmMail mail,UserForService user,String operation) throws GmException;

    List<Record> getMail(String mailId);

    String delMail(String mailId) throws GmException;

    void toExamineMailStatus(Integer id, Integer type,UserForService user) throws GmException;
}
