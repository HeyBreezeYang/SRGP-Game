package com.master.gm.service.manage;

import java.io.File;

import com.gmdesign.exception.GmException;

/**
 * Created by DJL on 2016/11/24.
 *
 * @ClassName FileConfigerServiceIF
 * @Description 上传的文件业务
 */
public interface FileConfigServiceIF {
    String obtainGameConfig(File file, String type, String[] sid)throws GmException;
}
