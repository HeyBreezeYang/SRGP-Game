package com.cellsgame.game.module.guild.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.collection.Trie;
import com.cellsgame.game.module.guild.vo.GuildMemberVO;
import com.cellsgame.game.module.guild.vo.GuildReqVO;
import com.cellsgame.game.module.guild.vo.GuildVO;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

/**
 * @author Aly on  2016-08-15.
 */
public class CacheGuild {
    public static final int maxSearchGuildSize = 50;

    // 已经满的家族 不应该放入推荐的家族列表中
    public static final TreeSet<GuildVO> recommendGuild = new TreeSet<>(
            // 战力 降序 ，如果战力相同那么就选择创建时间更早的
            Comparator.comparing(GuildVO::getFightForce).reversed()
                    .thenComparing(Comparator.comparing(GuildVO::getCreateDate)));
    public static final List<GuildVO> NoReqGuild = GameUtil.createList();
    public static final Set<GuildVO> inDissolutionGuild = new HashSet<>();
    private static final Map<Integer, GuildMemberVO> allGuildMemberByPID = new ConcurrentHashMap<>();
    private static final int recommentGuildSize = 50;
    private static final Map<Integer, GuildVO> allGuildByID = new ConcurrentHashMap<>();
    private static final BiMap<String, Integer> guildNameCache = HashBiMap.create();
    // 忽略大小写 全半角
    private static final Trie<Integer> nameCache = new Trie<>(true, true, null);
    // 所有 玩家对家族的请求      可能在quartz 线程调用,
    private static final Table<Integer, Integer, GuildReqVO> allReqsCache = Tables.newCustomTable(GameUtil.createMap(), GameUtil::createMap);

    public static boolean hashName(String name) {
        return guildNameCache.containsKey(name) || nameCache.containsKey(name);
    }

    public static GuildVO getGuildByID(int guildID) {
        return allGuildByID.get(guildID);
    }

    public static void addCache(GuildVO guildVO) {
        int guildID = guildVO.getId();
        allGuildByID.put(guildID, guildVO);
        nameCache.put(guildVO.getName(), guildID);
        guildNameCache.forcePut(guildVO.getName(), guildID);
        recommendGuild.add(guildVO);
        if (recommendGuild.size() >= recommentGuildSize) {
            recommendGuild.pollLast();
        }
    }

    public static void removeCache(GuildVO guildVO) {
        allGuildByID.remove(guildVO.getId());
        recommendGuild.remove(guildVO);
    }

    public static void resetNameCache(String oldName, GuildVO guildVO) {
        nameCache.remove(oldName);
        int guildID = guildVO.getId();
        nameCache.put(guildVO.getName(), guildID);
        guildNameCache.remove(oldName);
        guildNameCache.put(guildVO.getName(), guildID);
    }


    public static List<GuildVO> fuzzyQueryByName(String name) {
        List<Integer> ids = nameCache.keyStartWith(name, maxSearchGuildSize, null);
        if (ids != null) {
            List<GuildVO> vos = new ArrayList<>(ids.size());
            for (Integer id : ids) {
                GuildVO guildVO = allGuildByID.get(id);
                if (guildVO != null) {
                    vos.add(guildVO);
                }
            }
            return vos;
        } else {
            return Collections.emptyList();
        }
    }

    public static void cacheReq(GuildReqVO req) {
        allReqsCache.put(req.getPid(), req.getGuildID(), req);
    }

    public static int getPlayerReqSize(int pid) {
        Map<Integer, GuildReqVO> row = allReqsCache.row(pid);
        return null == row ? 0 : row.size();
    }

    public static int getGuildReqedSize(Integer guildID) {
        Map<Integer, GuildReqVO> column = allReqsCache.column(guildID);
        return null == column ? 0 : column.size();
    }

    public static Collection<GuildReqVO> removeReq(int pid) {
        Map<Integer, GuildReqVO> remove = allReqsCache.rowMap().remove(pid);

        if (null == remove) return Collections.emptyList();
        return remove.values();
    }

    public static void removeReq(GuildReqVO vo) {
        allReqsCache.remove(vo.getPid(), vo.getGuildID());
    }

    public static GuildReqVO getReq(int pid, int guildID) {
        return allReqsCache.get(pid, guildID);
    }

    public static Map<Integer, GuildReqVO> getReqByPid(int pid) {
        return allReqsCache.row(pid);
    }

    public static Map<Integer, GuildReqVO> getReqByGuildId(int guildID) {
        return allReqsCache.column(guildID);
    }

    public static ArrayList<GuildReqVO> getAllReqs() {
        return new ArrayList<>(allReqsCache.values());
    }

    public static Collection<GuildVO> getAllGuild() {
        return allGuildByID.values();
    }

    public static GuildMemberVO getGuildMemberVO(int pid) {
        return allGuildMemberByPID.get(pid);
    }

    public static GuildMemberVO cacheMember(GuildMemberVO vo) {
        return allGuildMemberByPID.put(vo.getPid(), vo);
    }
}
