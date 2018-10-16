package com.master.gm.service.manage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.master.bean.dispoly.CodeConfig;

/**
 * Created by DJL on 2017/12/11.
 *
 * @ClassName GM
 * @Description
 */
public interface CodeServiceIF {

    List<CodeConfig> queryBinding();
    List<CodeConfig> queryAll();

    File addPrizeCode(CodeConfig bean) throws IOException;

    void addBindCode(int aid,String bindingId);

    void deleteCode(int aid);

    Map exportCode(int aid);
}
