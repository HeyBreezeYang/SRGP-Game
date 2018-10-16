package com.cellsgame.game.module.player.cache;


import java.util.*;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.collection.Filter;
import com.cellsgame.common.util.collection.Trie;
import com.cellsgame.game.cache.CacheName;
import com.cellsgame.game.module.friend.cache.CacheFriendNames;
import com.cellsgame.game.module.player.cons.PlayerInfoVOUpdateType;
import com.cellsgame.game.module.player.vo.PlayerInfoVO;
import com.cellsgame.game.module.player.vo.PlayerVO;
import com.google.common.collect.*;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Aly on 2015-09-06.
 * 玩家基础信息缓存
 */
public class CachePlayerBase {
    public static final EventBus EVENT_BUS = new EventBus("PLAYER_BASE_INFO");
    private static final Logger log = LoggerFactory.getLogger(CachePlayerBase.class);

    // 关心大小写 全半角
    private static final Trie<Integer> nameTrie = new Trie<>();
    private static final BiMap<Integer, String> PID_PNAME = Maps.synchronizedBiMap(HashBiMap.<Integer, String>create());
    private static final Map<Integer, PlayerInfoVO> PID_BaseInfo = GameUtil.createMap();
    private static final Multimap<Integer, PlayerInfoVO> DISTINCT_BY_LV = HashMultimap.create();
    private static final Listener LISTENER = new Listener();
    private static final Table<String, Integer, Integer> UID_SID_PID = HashBasedTable.create();
    private static int maxLevel = 0, minLevel = Integer.MAX_VALUE;

    private static Set<String> CUR_ROBOT_NAME = Sets.newConcurrentHashSet();

    static {
        EVENT_BUS.register(LISTENER);
    }

    public static void addRobotName(String name) {
        CUR_ROBOT_NAME.add(name);
    }

    public static Collection<PlayerInfoVO> getAllBaseInfo() {
        return Collections.unmodifiableCollection(PID_BaseInfo.values());
    }

    public static void updateBasInfo(PlayerVO pvo, PlayerInfoVOUpdateType type) {
        // 数据更新类型
        if (null == type) type = PlayerInfoVOUpdateType.ALL;
        // 查找玩家基础数据
        PlayerInfoVO baseInfo = PID_BaseInfo.get(pvo.getId());
        // 如果玩家基础数据不存在
        if (baseInfo == null) {
            // 基础数据
            baseInfo = new PlayerInfoVO(pvo.getId());
            baseInfo.setUid(pvo.getAccountId());
            // 所有玩家基础数据
            PID_BaseInfo.put(pvo.getId(), baseInfo);
            // 按等级区分玩家
//            DISTINCT_BY_LV.put(pvo.getLevel(), baseInfo);
            // 最大等级
//            maxLevel = Math.max(maxLevel, pvo.getLevel());
            // 最小等级
//            minLevel = Math.min(minLevel, pvo.getLevel());
        }
        // 更新数据
        type.update(pvo, baseInfo);
    }

    public static void addBaseInfo(PlayerVO pvo) {
        UID_SID_PID.put(pvo.getAccountId(), pvo.getServerId(), pvo.getId());
        if (null != getPIDByPname(pvo.getName())) {
            log.warn("pname:[{}] is not unique! pid[{}]", new Object[]{pvo.getName(), pvo.getId()});
        } else {
            PID_PNAME.forcePut(pvo.getId(), pvo.getName());
        }
        updateBasInfo(pvo, PlayerInfoVOUpdateType.ALL);
        CacheName.addUsedName(pvo.getName());
    }

    public static Multimap<Integer, PlayerInfoVO> levels() {
        return Multimaps.unmodifiableMultimap(DISTINCT_BY_LV);
    }

    public static PlayerInfoVO getBaseInfo(int pid) {
        return PID_BaseInfo.get(pid);
    }

    public static BiMap<Integer, String> getPName() {
        return PID_PNAME;
    }

    public static void resetPlayerName(PlayerVO pvo, String oldName) {
        if (null == oldName) {
            return;
        }
        PID_PNAME.forcePut(pvo.getId(), pvo.getName());
        nameTrie.put(pvo.getName(), pvo.getId());
        updateBasInfo(pvo, PlayerInfoVOUpdateType.Name);
    }

    public static Integer getPIDByPname(String pname) {
        return PID_PNAME.inverse().get(pname);
    }

    public static String getPnameByPid(int pid) {
        return PID_PNAME.get(pid);
    }

//    public static void cacheRobotName(Integer id, String name) {
//        PID_RobotName.put(id, name);
//        nameTrie.put(name, DUMP);
//    }

    public static boolean isNameInCache(String name) {
        return PID_PNAME.containsValue(name) ||/* PID_RobotName.containsValue(name) ||*/ nameTrie.containsKey(name);

    }

    public static List<PlayerInfoVO> getQueryPlayerByName(String name, Filter<PlayerInfoVO> filter) {
        Filter<Integer> pidFilter = null;
        if (filter != null) {
            pidFilter = new Filter<Integer>() {
                @Override
                public boolean isFilter(Integer pid) {
                    PlayerInfoVO baseInfo = PID_BaseInfo.get(pid);
                    return baseInfo == null || filter.isFilter(baseInfo);
                }
            };
        }
        List<Integer> pids = nameTrie.keyStartWith(name, 50, pidFilter);

        if (null == pids || pids.size() == 0) {
            return Collections.emptyList();
        } else {
            List<PlayerInfoVO> infos = new ArrayList<>(pids.size());
            for (Integer pid : pids) {
                PlayerInfoVO info = PID_BaseInfo.get(pid);
                if (info != null) {
                    infos.add(info);
                }
            }
            return infos;
        }

    }


    private static class Listener {
        @Subscribe
        public void on(final UpdateEvent event) {
            switch (event.type) {
                case PLV:
                    int before = ((Number) event.before).intValue();
                    int after = ((Number) event.after).intValue();
                    if (before > 0 && before != after) {
                        // 移除旧等级数据
                        DISTINCT_BY_LV.remove(before, event.source);
                        // 添加新等级
                        DISTINCT_BY_LV.put(after, event.source);
                        // 最大等级
                        maxLevel = Math.max(maxLevel, after);
                        // 如果是最小等级发生变化
                        if (minLevel == before) minLevel = after;
                    }
                    break;
                case Name: {
                    if (event.before != null)
                        CacheFriendNames.friendNameCache.remove(((String) event.before));
                    CacheFriendNames.friendNameCache.put(((String) event.after), event.source);
                    break;
                }
                default:
                    log.error("unknown update type[{}]", event.type);
                    break;
            }
        }
    }

    public static class UpdateEvent {
        private PlayerInfoVOUpdateType type;
        private Object before;
        private Object after;
        private PlayerInfoVO source;

        public UpdateEvent(PlayerInfoVOUpdateType type, Object before, Object after, PlayerInfoVO source) {
            this.type = type;
            this.before = before;
            this.after = after;
            this.source = source;
        }
    }
}
