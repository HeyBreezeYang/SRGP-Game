package com.cellsgame.game.context;

import java.util.List;
import com.cellsgame.common.util.SpringBeanFactory;
import com.cellsgame.game.module.func.FuncConfig;

public class DefaultPlayerConfig {

    private static DefaultPlayerConfig config;

    public static DefaultPlayerConfig getConfig() {
        if (config == null)
            config = SpringBeanFactory.getBean(DefaultPlayerConfig.class);
        return config;
    }
    
    // 其他 非队伍中的英雄
    private List<FuncConfig> defaultPrize;


  

    public List<FuncConfig> getDefaultPrize() {
        return defaultPrize;
    }

 
	public void setDefaultPrize(String defaultPrize) {
//        this.defaultPrize = JSONUtils.fromJson(defaultPrize, new TypeToken<List<FuncConfig>>() {
//        }.getType());
    }

}
