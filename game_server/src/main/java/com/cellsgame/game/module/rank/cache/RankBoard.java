package com.cellsgame.game.module.rank.cache;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.game.cache.CacheDisData;
import com.cellsgame.game.module.rank.cons.CacheDisRank;
import com.cellsgame.game.module.rank.msg.MsgFactoryRank;
import com.cellsgame.game.util.TriConsumer;

/**
 * @author Aly on 2017-03-14.
 * * @apiNote
 * 默认情况下排序的时候按 排序按照 用户自定义比较器+ Key 进行排序 {@link RankObjUtil#comparator}
 * 确保{@link RankBoard#sortedVal} 中没有任何两个元素相同的情况
 */
public class RankBoard<T, Key extends Comparable<Key>>{
  
    private int type;
    private RankObjUtil<T, Key> utils;
    private List<RankObj<Key>> sortedVal = new ArrayList<>();
    private Map<Key, RankObj<Key>> allObj = new HashMap<>();
    private BiFunction<MsgFactoryRank, List<RankObj<Key>>,  List<Map<String, Object>>> viewFunction;

    public static <T, Key extends Comparable<Key>> RankBoard<T, Key> of(int type, RankObjUtil<T, Key> maker, BiFunction<MsgFactoryRank, List<RankObj<Key>>,  List<Map<String, Object>>> viewFunction){
    	return new RankBoard<>(type, maker, viewFunction);
    }
    
    private RankBoard(int type, RankObjUtil<T, Key> maker,
    		BiFunction<MsgFactoryRank, List<RankObj<Key>>, List<Map<String, Object>>> viewFunction) {
        this.type = type;
        this.viewFunction = viewFunction;
        this.utils = maker;
    }
    
   

    public int getType() {
        return type;
    }

    public boolean inRank(Key key) {
        return allObj.containsKey(key);
    }
    
    public int getRank(T val){
    	if(val == null)
    		return -1;
    	Key key = utils.getKey(val);
        RankObj<Key> obj = allObj.get(key);
        if(obj == null)
        	return -1;
        return Collections.binarySearch(sortedVal, obj, utils.comparator);
    }
    
    public List<Key> getKeysAround(T tgt, int lowerRange, int upperRange){
    	Key key = utils.getKey(tgt);
    	return getKeysAround(key, lowerRange, upperRange);
    }
    
    public List<Key> getKeysAround(T tgt, int range){
    	Key key = utils.getKey(tgt);
    	return getKeysAround(key, range);
    }
    
    public List<Key> getKeysAround(Key key, int range){
    	return getKeysAround(key, range/2, range-range/2);
    }
    
    public List<Key> getKeysAround(Key key, int lowerRange, int upperRange){
    	List<Key> ret = GameUtil.createList();
        if(key == null)
        	return ret;
    	RankObj<Key> obj = allObj.get(key);
    	if(obj == null)
    		return ret;
    	int ix = Collections.binarySearch(sortedVal, obj, utils.comparator);
    	if(ix<0)
    		return ret;
    	int size = sortedVal.size();
    	
    	for (int i = Math.max(0, ix-lowerRange); i < ix; i++) {
			ret.add(sortedVal.get(i).key);
		}
    	int delta = lowerRange - ret.size();
    	upperRange += delta;
    	int upper = Math.min(upperRange+ix, size-1);
    	for (int i = ix+1; i <=upper; i++) {
			ret.add(sortedVal.get(i).key);
		}
    	return ret;
    }

    /**
     * 根据排名 [0,RankMax) 获取对应的Key
     */
    public Key getKeyByRank(int rank) {
        if (rank < 0 || rank >= sortedVal.size()) {
            return null;
        }
        RankObj<Key> rankObj = sortedVal.get(rank);
        return null == rankObj ? null : rankObj.key;
    }

    public void load(T val){
    	Key key = utils.getKey(val);
        RankObj<Key> obj = allObj.get(key);
        if (null == obj) {
            obj = utils.loadObj(val);
            allObj.put(key, obj);
            add(obj);
        }
    }
    
    
    /**
     * 更新数据
     */
    public void update(T val) {
        update(val, null);
    }

    /**
     * 更新数据 带监听
     */
    public long update(T val, Consumer<T> onChange) {
    	long ix = -1;
    	if ((ix = innerUpdate(val)) >= 0 && null != onChange) {
            onChange.accept(val);
        }
    	return ix;
    }

    /**
     * 与逻辑在同一线程运行
     * 依靠二分查找快速将元素更新到位
     * @return
     */
    private long innerUpdate(T val) {
        Key key = utils.getKey(val);
        RankObj<Key> obj = allObj.get(key);
        if (null == obj) {
            obj = utils.newObj(val);
            allObj.put(key, obj);
            add(obj);
            return obj.getIx();
        } else {
        	if(utils.noChange(val, obj))
        		return obj.getIx();
            int idx = Collections.binarySearch(sortedVal, obj, utils.comparator);
            if (idx >= 0) {
                sortedVal.remove(idx);//确保
                utils.updateObj(val, obj);
                add(obj);
                return obj.getIx();
            }
            return -1;
        }
    }

    private void add(RankObj<Key> obj) {
        int search = Collections.binarySearch(sortedVal, obj, utils.comparator);
        if (search < 0) {
            sortedVal.add(-search - 1, obj);//插入
        } else {//不允许
           throw new RuntimeException("add duplicated element into sortedLst");
        }
    }



	public BiFunction<MsgFactoryRank, List<RankObj<Key>>,  List<Map<String, Object>>> getViewFunction() {
		return viewFunction;
	}



	public void setViewFunction(BiFunction<MsgFactoryRank, List<RankObj<Key>>,  List<Map<String, Object>>> viewFunction) {
		this.viewFunction = viewFunction;
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Map<String, Object>> getRankInfo(int start, int end) {
		int maxNum = Math.min(size(), CacheDisRank.MAX_RANK_NUM.first());
		if(end<0)
			end = maxNum;
		
		List<RankObj<Key>> subList = sortedVal.subList(Math.max(0, start), Math.min(maxNum, end));
		List<Map<String, Object>> msg = viewFunction.apply(MsgFactoryRank.instance(), subList);
		return msg;
	}
	
	
	public  Map<String, Object> getRankInfo(int index) {
		if(index<0 || index>=sortedVal.size())
			return null;
		List<RankObj<Key>> temp = GameUtil.createList();
		temp.add(sortedVal.get(index));
		List<Map<String, Object>> msg = viewFunction.apply(MsgFactoryRank.instance(), temp);
		return msg.get(0);
	}

	/**
	 * @return
	 */
	public Integer size() {
		return sortedVal.size();
	}

}
