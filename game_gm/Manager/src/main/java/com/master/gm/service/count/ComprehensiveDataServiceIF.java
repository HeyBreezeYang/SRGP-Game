package com.master.gm.service.count;

import java.util.List;

import com.gmdesign.exception.GmException;

/**
 * 
* @ClassName ComprehensiveDataServiceIF
* @Description 综合数据查询业务接口
* @author DJL
* @date 2015-12-10 下午4:25:24
*
 */
public interface ComprehensiveDataServiceIF {

     List<Object[]> Comprehensive(String sid, String start, String end, String platform)throws GmException;
     List<Object[]> LTVData(String sid, String start, String end, String platform)throws GmException;
     List<Object[]> LTVDataForMoney(String sid, String start, String end, String platform)throws GmException;
     List<Object[]> LRRate(String sid, String start, String end, String platform, boolean lr)throws GmException;
}
