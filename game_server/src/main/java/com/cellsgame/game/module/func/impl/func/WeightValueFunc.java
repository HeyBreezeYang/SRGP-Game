package com.cellsgame.game.module.func.impl.func;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.module.func.FuncParam;
import com.cellsgame.game.module.sys.csv.WeightConfig;

import java.util.List;
import java.util.Map;

public interface WeightValueFunc {

    default int randomWeightValue(FuncParam funcParam, int execNum){
        WeightConfig config = CacheConfig.getCfg(WeightConfig.class, funcParam.getParam2());
        if(config == null) return 0;
        if(execNum >= 100){
            List<Integer> nums = GameUtil.createList();
            for (WeightConfig.Item item : config.getItems()) {
                int num = item.getWeight() * execNum/config.getSumWeight();
                nums.add(num);
            }
            List<Integer> nums2 = GameUtil.createList();
            for (int i = 0; i < nums.size(); i++) {
                int num = nums.get(i);
                int n = ((num * 8) + GameUtil.r.nextInt(num * 4))/10;
                if(n >= execNum) {
                    nums2.add(execNum);
                    execNum = 0;
                }else{
                    nums2.add(n);
                    execNum -= n;
                }
            }
            if(execNum > 0){
                int index = GameUtil.r.nextInt(nums2.size()/2);
                int old = nums2.get(index);
                nums2.set(index, old + execNum);
            }
            int sumValue = 0;
            for (int i = 0; i < nums2.size(); i++) {
                int num = nums2.get(i);
                WeightConfig.Item item = config.getItems().get(i);
                sumValue += item.getValue() * num;
            }
            return sumValue;
        }else {
            int sumValue = 0;
            for (int i = 0; i < 100; i++) {
                int random = GameUtil.r.nextInt(config.getSumWeight());
                for (WeightConfig.Item item : config.getItems()) {
                    if (random > item.getWeight()) {
                        random -= item.getWeight();
                        continue;
                    } else {
                        sumValue += item.getValue();
                    }
                }
            }
            return sumValue;
        }
    }

    default  long getValue(FuncParam funcParam, int execNum){
        if(funcParam.getParam2() > 0)
            return randomWeightValue(funcParam, execNum);
        else {
            return funcParam.getValue() * execNum;
        }
    }

}
