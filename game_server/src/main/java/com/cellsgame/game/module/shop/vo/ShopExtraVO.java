package com.cellsgame.game.module.shop.vo;

import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 商店额外数据。
 * <p>
 * 比如:
 * <p>
 * 商店序列刷新规则数据
 *
 * @author Yang
 */
public class ShopExtraVO extends DBVO {
    //************ 固定序列刷新规则额外数据 ***********/
    // 商店刷新商品组下一个序列下标
    @Save(ix = 1)
    private int nextGroupIndex;
    // 商店刷新商品组序列
    @Save(ix = 2)
    private int[] groupSequence;
    // 已使用的商品组序列
    @Save(ix = 3)
    private Set<String> usedGroupSequence;
    //************ 固定序列刷新规则额外数据 ***********/


    @Override
    protected Object initPrimaryKey() {
        return 1;
    }

    @Override
    protected Object getPrimaryKey() {
        return 1;
    }

    @Override
    protected void setPrimaryKey(Object pk) {

    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[0];
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {

    }

    @Override
    protected void init() {
        nextGroupIndex = 0;
        groupSequence = ArrayUtils.EMPTY_INT_ARRAY;
        usedGroupSequence = GameUtil.createSet();
    }

    @Override
    public Integer getCid() {
        return 0;
    }

    @Override
    public void setCid(Integer cid) {

    }

    public Set<String> getUsedGroupSequence() {
        return usedGroupSequence;
    }

    public void setUsedGroupSequence(Set<String> usedGroupSequence) {
        this.usedGroupSequence = usedGroupSequence;
    }

    public int getNextGroupIndex() {
        return nextGroupIndex;
    }

    public void setNextGroupIndex(int nextGroupIndex) {
        this.nextGroupIndex = nextGroupIndex;
    }

    public int[] getGroupSequence() {
        return groupSequence;
    }

    public void setGroupSequence(int[] groupSequence) {
        this.groupSequence = groupSequence;
    }
}
