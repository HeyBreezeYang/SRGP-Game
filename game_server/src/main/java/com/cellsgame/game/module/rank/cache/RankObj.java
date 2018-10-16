package com.cellsgame.game.module.rank.cache;

/**
 * @author Aly on 2017-03-28.
 */
public class RankObj<Key> implements Cloneable {
    Key key;
    long sort1;
    long sort2;
    long ix;

    RankObj() {
    }

    @Override
    public String toString() {
        return "{" + sort1 + ',' + sort2 + '}';
    }

    public Key getKey() {
        return key;
    }

    public long getSort1() {
        return sort1;
    }

    public long getSort2() {
        return sort2;
    }
    
    public long getIx(){
    	return ix;
    }
    
    public void setIx(long ix){
    	this.ix = ix;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected RankObj<Key> clone() throws CloneNotSupportedException {
        return (RankObj) super.clone();
    }
}
