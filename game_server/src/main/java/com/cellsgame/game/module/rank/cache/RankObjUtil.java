package com.cellsgame.game.module.rank.cache;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ToLongFunction;

/**
 * @author Aly on 2017-04-05.
 */
public class RankObjUtil<T, Key extends Comparable<Key>> {
	private static AtomicLong indexCreator = new AtomicLong(System.currentTimeMillis()*1000);
    Comparator<RankObj<Key>> comparator;
    private Function<T, Key> keyGetter;
    private ToLongFunction<T> sort1Getter;
    private ToLongFunction<T> sort2Getter;
    private BiConsumer<T,Long> ixSetter;
    
    
    private RankObjUtil(Function<T, Key> keyGetter, ToLongFunction<T> sort1Getter, Comparator<RankObj<Key>> comparator) {
        this(keyGetter, sort1Getter, null, null, comparator);

    }
    

    private RankObjUtil(Function<T, Key> keyGetter, ToLongFunction<T> sort1Getter, ToLongFunction<T> sort2Getter, BiConsumer<T,Long> ixSetter, Comparator<RankObj<Key>> comparator) {
        this.keyGetter = keyGetter;
        this.sort1Getter = sort1Getter;
        this.sort2Getter = sort2Getter;
        // 确保 唯一
        this.comparator = comparator.thenComparing(Comparator.comparing(RankObj::getIx));
        this.ixSetter = ixSetter;
    }

    public static <T, Key extends Comparable<Key>> RankObjUtil<T, Key> of(Function<T, Key> keyGetter, ToLongFunction<T> sort1Getter, ToLongFunction<T> sort2Getter, Comparator<RankObj<Key>> comparator) {
        return new RankObjUtil<>(keyGetter, sort1Getter, sort2Getter, null, comparator);
    }
    
    public static <T, Key extends Comparable<Key>> RankObjUtil<T, Key> of(Function<T, Key> keyGetter, ToLongFunction<T> sort1Getter,  BiConsumer<T,Long> ixSetter, Comparator<RankObj<Key>> comparator) {
        return new RankObjUtil<>(keyGetter, sort1Getter, null, ixSetter, comparator);
    }

    void updateObj(T val, RankObj<Key> n) {
        if(update(val, n) && ixSetter != null)
        	ixSetter.accept(val, n.ix = indexCreator.incrementAndGet());
    }
 
   

    private boolean update(T val, RankObj<Key> n) {
    	n.key = getKey(val);
    	n.sort1 = sort1Getter.applyAsLong(val);
    	if (sort2Getter != null)
    		n.sort2 = sort2Getter.applyAsLong(val);
    	return true;
    }

    RankObj<Key> newObj(T val) {
        RankObj<Key> n = new RankObj<>();
        updateObj(val, n);
        return n;
    }
    
    boolean noChange(T val, RankObj<Key> n) {
    	long sort1 = sort1Getter.applyAsLong(val);
    	boolean noChange = sort1 == n.sort1;
    	if (noChange && sort2Getter != null){
    		noChange  &= sort2Getter.applyAsLong(val) == n.sort2;
    	}
    	return noChange;
    }

    RankObj<Key> loadObj(T val) {
    	RankObj<Key> n = new RankObj<>();
    	update(val, n);
          return n;
    }
    
    public Key getKey(T val) {
        return keyGetter.apply(val);
    }
}
