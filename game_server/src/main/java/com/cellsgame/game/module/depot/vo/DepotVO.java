package com.cellsgame.game.module.depot.vo;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cellsgame.common.util.GameUtil;
import com.cellsgame.common.util.csv.BaseCfg;
import com.cellsgame.game.module.depot.cons.DepotType;
import com.cellsgame.game.module.goods.csv.QuestCollectible;
import com.cellsgame.game.module.goods.vo.GoodsVO;
import com.cellsgame.game.util.IntegerMap;
import com.cellsgame.orm.DBObj;
import com.cellsgame.orm.DBVO;
import com.cellsgame.orm.enhanced.annotation.Save;
import com.google.common.base.Supplier;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

public class DepotVO extends DBVO {

    private int id;

    private int pid;

    /**
     * 计数物品数据  ID，数量
     */
    @Save(ix = 0)
    private Map<Integer, Integer> goodsCounts;

    /**
     * 货币数据
     */
    @Save(ix = 1)
    private Map<Integer, Long> currencyMap;
    /**
     * 实体物品数据 表格（背包类型，ID，道具）
     */
    //Table<R,C,V> 各个参数的意义是(行、列、值)，即一个行号和一个列号对应一个值。就像一个表格一样；类似于Map<Key,Map<key,value>>
    private Table<DepotType, Integer, GoodsVO> goodsEntities;

    @Save(ix = 2)
    private Map<Integer, DepotGoodsRefVO> goodsRef;

    //最大道具索引标识
    @Save(ix = 3)
    private int maxGoodsIx;

    @Save(ix = 4)
    private Map<Integer, Long> refDates;

    /**
     * 加载的时候 不在背包引用的物品列表
     */
    private Table<DepotType, Integer, GoodsVO> notInDepotGoods;
    // 物品计数, 包括所有物品
    private IntegerMap<Integer> goodsCounter;

    public DepotVO() {
    }

    @Override
    public DBObj writeToDBObj() {
        goodsRef.clear();
        Set<Entry<DepotType, Map<Integer, GoodsVO>>> es = goodsEntities.rowMap().entrySet();
        for (Entry<DepotType, Map<Integer, GoodsVO>> e : es) {
            Integer type = e.getKey().getDepotTypeCode();
            Set<Integer> gixSet = e.getValue().keySet();
            DepotGoodsRefVO refvo = new DepotGoodsRefVO();
            List<Integer> gixLst = refvo.getGoodsRefs();
            for (Integer gix : gixSet) {
                gixLst.add(gix);
            }
            goodsRef.put(type, refvo);
        }
        return super.writeToDBObj();
    }

    @Override
    protected Object getPrimaryKey() {
        return id;
    }

    @Override
    protected void setPrimaryKey(Object pk) {
        id = (int) pk;
    }

    @Override
    protected Object[] getRelationKeys() {
        return new Object[]{pid};
    }

    @Override
    protected void setRelationKeys(Object[] relationKeys) {
        if (relationKeys.length > 0)
            pid = (int) relationKeys[0];
    }

    public int getMaxGoodsIx() {
        return maxGoodsIx;
    }

    public void setMaxGoodsIx(int maxGoodsIx) {
        this.maxGoodsIx = maxGoodsIx;
    }

    @Override
    protected void init() {
        id = 0;
        pid = 0;
        goodsEntities = Tables.newCustomTable(new EnumMap<>(DepotType.class), new Supplier<Map<Integer, GoodsVO>>() {
            @Override
            public Map<Integer, GoodsVO> get() {
                return GameUtil.createSimpleMap();
            }
        });
        goodsCounts = GameUtil.createSimpleMap();
        currencyMap = GameUtil.createSimpleMap();
        goodsCounts = GameUtil.createSimpleMap();
        goodsRef = GameUtil.createSimpleMap();
        goodsCounter = IntegerMap.create();
        refDates = GameUtil.createSimpleMap();
    }

    @Override
    public Integer getCid() {
        return null;
    }

    @Override
    public void setCid(Integer cid) {
    }

    /**
     * 按ID计数。
     * <p>
     *     按物品收集类型计数
     * </p>
     * @param cfg   需要计数的配置
     * @param delta 数量偏移量，如果需要减去，则为负数
     */
    public final void count(BaseCfg cfg, int delta) {
        if (cfg == null) return;
        //
        goodsCounter.addAndGet(cfg.getId(), delta);
        // 如果物品是任务可收集的
        if (cfg instanceof QuestCollectible) goodsCounter.addAndGet(((QuestCollectible) cfg).getCollectType(), delta);
    }

    /**
     * 物品计数
     *
     * @param goodsVO 物品数据
     * @param delta   数量偏移量，如果需要减去，则为负数
     */
    public final void count(GoodsVO goodsVO, int delta) {
        if (goodsVO == null) return;
        // 计数物品
        count(goodsVO.getCfg(), delta);
    }

    /**
     * 获取某ID的物品数量。
     * <p>
     * 参数可以是物品的配置ID，也可以是物品的可收集类型ID
     *
     * @param idOrCollectType 物品配置ID或任何可收集类型
     * @return 数量
     */
    public final int getCount(int idOrCollectType) {
        return goodsCounter.get(idOrCollectType);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }


    public Map<Integer, Long> getCurrencyMap() {
        return currencyMap;
    }

    public void setCurrencyMap(Map<Integer, Long> currencyMap) {
        this.currencyMap = currencyMap;
    }


    public Table<DepotType, Integer, GoodsVO> getGoodsEntities() {
        return goodsEntities;
    }

    public void setGoodsEntities(Table<DepotType, Integer, GoodsVO> goodsEntities) {
        this.goodsEntities = goodsEntities;
    }

    public Map<Integer, Integer> getGoodsCounts() {
        return goodsCounts;
    }

    public void setGoodsCounts(Map<Integer, Integer> goodsCounts) {
        this.goodsCounts = goodsCounts;
    }

    public Map<Integer, DepotGoodsRefVO> getGoodsRef() {
        return goodsRef;
    }

    public void setGoodsRef(Map<Integer, DepotGoodsRefVO> goodsRef) {
        this.goodsRef = goodsRef;
    }

    public Table<DepotType, Integer, GoodsVO> getNotInDepotGoods() {
        return notInDepotGoods;
    }
    // 设置不在仓库的道具（背包类型， 道具ID ，道具）
    public void setNotInDepotGoods(Table<DepotType, Integer, GoodsVO> notInDepotGoods) {
        this.notInDepotGoods = notInDepotGoods;
    }

    public Map<Integer, Long> getRefDates() {
        return refDates;
    }

    public void setRefDates(Map<Integer, Long> refDates) {
        this.refDates = refDates;
    }
}
