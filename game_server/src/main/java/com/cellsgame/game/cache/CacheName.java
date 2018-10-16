package com.cellsgame.game.cache;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.cellsgame.game.module.player.cache.CachePlayerBase;
import com.cellsgame.game.module.player.csv.PlayerNameConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File Description.
 *
 * @author Yang
 */
public class CacheName {
    private static final Logger log = LoggerFactory.getLogger(CacheName.class);
    private static Set<String> usedName = Sets.newHashSet();
    // 临时使用的名字
    private static Set<String> tempUsedName = Sets.newHashSet();
    // 名字库
    private static List<String> maleNamePool = Collections.emptyList();
    // 名字库
    private static List<String> femaleNamePool = Collections.emptyList();

    public static void loadFromConfig(Map<Integer, PlayerNameConfig> nameConfigMap) {
        final List<String> firstName = nameConfigMap.values().stream().filter(v -> StringUtils.isNotEmpty(v.getFirstName())).map(PlayerNameConfig::getFirstName).collect(Collectors.toList());
        final List<String> femaleLastName = nameConfigMap.values().stream().filter(v -> StringUtils.isNotEmpty(v.getFemalelastName())).map(PlayerNameConfig::getFemalelastName).collect(Collectors.toList());
        final List<String> maleLastName = nameConfigMap.values().stream().filter(v -> StringUtils.isNotEmpty(v.getMalelastName())).map(PlayerNameConfig::getMalelastName).collect(Collectors.toList());
        maleNamePool = Lists.newArrayListWithCapacity(firstName.size() * maleLastName.size());
        femaleNamePool = Lists.newArrayListWithCapacity(firstName.size() * femaleLastName.size());
        firstName.forEach(f -> {
            maleLastName.forEach(ml -> maleNamePool.add(f + ml));
            femaleLastName.forEach(ml -> femaleNamePool.add(f + ml));
        });
    }

    public static void afterLoadAll() {
        maleNamePool.removeIf(CachePlayerBase::isNameInCache);
        femaleNamePool.removeIf(CachePlayerBase::isNameInCache);
    }

    public static String random(boolean male) {
        String name = doRandom(male);
        while (usedName.contains(name) || tempUsedName.contains(name)) name = doRandom(male);
        tempUsedName.add(name);
        return name;
    }

    private static String doRandom(boolean male) {
        Random random = ThreadLocalRandom.current();
        return male ? maleNamePool.get(random.nextInt(maleNamePool.size())) : femaleNamePool.get(random.nextInt(femaleNamePool.size()));
    }

    public static boolean isUsed(String name) {
        return usedName.contains(name);
    }

    public static void release(String name) {
        tempUsedName.remove(name);
    }


    public static void addUsedName(String name){
        usedName.add(name);
    }


}
