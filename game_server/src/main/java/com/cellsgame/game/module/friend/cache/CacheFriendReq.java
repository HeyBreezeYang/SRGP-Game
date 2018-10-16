package com.cellsgame.game.module.friend.cache;

import java.util.*;
import java.util.function.Function;

import com.cellsgame.game.module.friend.vo.FriendReqVO;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Aly @2017-02-07.
 */
public class CacheFriendReq {

    private static Map<Integer, FriendReqVO> allReqs = new HashMap<>();
    //SRC TGT
    private static Map<Integer, Table<Integer, Integer, Integer>> reqsByStatus = new HashMap<>();

    public static boolean statusChg(FriendReqVO vo, int expect, int update) {
        return statusChg(vo, expect, update, false);
    }

    public static boolean statusChg(FriendReqVO vo, int expect, int update, boolean removeOnly) {
        if (vo.casStatus(expect, update)) {
            Table<Integer, Integer, Integer> table = reqsByStatus.get(expect);
            table.remove(vo.getSrc(), vo.getTgt());
            if (!removeOnly)
                add2Cache(vo);
            return true;
        }
        return false;
    }

    public static void add2Cache(FriendReqVO vo) {
        if (null == vo) return;
        allReqs.put(vo.getId(), vo);
        Table<Integer, Integer, Integer> table = reqsByStatus.computeIfAbsent(vo.getStatus(), new Function<Integer, Table<Integer, Integer, Integer>>() {
            @Override
            public Table<Integer, Integer, Integer> apply(Integer integer) {
                return HashBasedTable.create();
            }
        });
        table.put(vo.getSrc(), vo.getTgt(), vo.getId());
    }


    public static FriendReqVO getSrcOrTgt(int src, int tgt) {
        return get(src, tgt, true);
    }

    public static FriendReqVO get(int src, int tgt) {
        return get(src, tgt, false);
    }

    private static FriendReqVO get(int src, int tgt, boolean unkonwn) {
        if ( src <= 0 || tgt <= 0) return null;
        for (Map.Entry<Integer, Table<Integer, Integer, Integer>> entry : reqsByStatus.entrySet()) {
            FriendReqVO vo = get(src, tgt, entry.getKey(), entry.getValue());
            if (null == vo && unkonwn) {
                vo = get(tgt, src, entry.getKey(), entry.getValue());
            }
            if (null != vo) return vo;
        }
        return null;
    }

    private static FriendReqVO get(int src, int tgt, int expectStatus, Table<Integer, Integer, Integer> dataTable) {
        if ( src <= 0 || tgt <= 0) return null;
        Integer voID = dataTable.get(src, tgt);
        if (null != voID) {
            FriendReqVO vo = allReqs.get(voID);
            if (vo.getStatus() != expectStatus) {
                // 容错  状态变更后 数据没有及时的更换缓存列表
                dataTable.remove(src, tgt);
            } else
                return vo;
        }
        return null;
    }

    // 不知到是我请求还是请求我
    public static Collection<Integer> getFriendByStatusSrcOrTgt(int status, int srcOrTgt) {
        Table<Integer, Integer, Integer> table = reqsByStatus.get(status);
        Set<Integer> tgtPids = new HashSet<>();
        if (null != table) {
            Map<Integer, Integer> row = table.row(srcOrTgt);
            if (null != row && row.size() > 0) {
                tgtPids.addAll(row.keySet());
            }
            Map<Integer, Integer> column = table.column(srcOrTgt);
            if (null != column && column.size() > 0) {
                tgtPids.addAll(column.keySet());
            }
        }
        return tgtPids;
    }

    // 我请求的
    public static Collection<Integer> getFriendByStatusSrc(int status, int src) {
        Table<Integer, Integer, Integer> table = reqsByStatus.get(status);
        Set<Integer> tgtPids = new HashSet<>();
        if (null != table) {
            Map<Integer, Integer> row = table.row(src);
            if (null != row && row.size() > 0) {
                tgtPids.addAll(row.keySet());
            }
        }
        return tgtPids;
    }

    //请求我的
    public static Collection<Integer> getFriendByStatusTgt(int status, int tgt) {
        Table<Integer, Integer, Integer> table = reqsByStatus.get(status);
        Set<Integer> tgtPids = new HashSet<>();
        if (null != table) {
            Map<Integer, Integer> row = table.column(tgt);
            if (null != row && row.size() > 0) {
                tgtPids.addAll(row.keySet());
            }
        }
        return tgtPids;
    }
}
