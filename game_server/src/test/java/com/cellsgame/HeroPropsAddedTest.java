package com.cellsgame;

import com.cellsgame.common.util.GameUtil;

/**
 * 测试英雄升级属性增加
 */

public class HeroPropsAddedTest {
    public static int getPropAdded(Integer totalTimes, Integer useTimes, Integer curValue, Integer maxValue) {
        if (useTimes > totalTimes) {
            return -1;
        }

        int addMode =  (maxValue - curValue) % totalTimes;
        int addFactor = (maxValue - curValue - addMode) / totalTimes;

        int added = 0;
        while (useTimes -- > 0) {
            int validValue = maxValue - (curValue + added);
            int randomValue = GameUtil.r.nextInt(totalTimes --);
            if (randomValue < validValue) {
                added += addFactor;
                if (randomValue < addMode) {
                    added += 1;
                }
            }
        }
        return added;
    }

    public static void main(String[] args) {
        int added = 0;

        int totalTimes = 65;
        int curValue = 3;
        int maxValue = 45;

        int ADDED_STEP = 1;
        int usedTimes = 0;
        while(usedTimes < totalTimes) {
            int useTimes = Math.min(totalTimes - usedTimes, ADDED_STEP);
            int r = HeroPropsAddedTest.getPropAdded(totalTimes - usedTimes , useTimes, curValue + added, maxValue);
            added += r;
            usedTimes += useTimes;
            System.out.println(usedTimes +",    added:" + r + ",curValue "+ (curValue + added));
        }
    }
}
