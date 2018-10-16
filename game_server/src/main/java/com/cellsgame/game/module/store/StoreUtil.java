package com.cellsgame.game.module.store;

import com.cellsgame.common.util.DateUtil;
import com.cellsgame.game.cache.CacheConfig;
import com.cellsgame.game.cons.CodeGeneral;
import com.cellsgame.game.core.excption.LogicException;
import com.cellsgame.game.module.shop.csv.ShopItemConfig;
import com.cellsgame.game.module.store.msg.CodeStore;
import com.cellsgame.game.module.store.vo.StoreItemVO;
import com.cellsgame.game.module.store.vo.StoreVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class StoreUtil {

    private static final String ID = "id";
    private static final String Type = "type";
    private static final String ItemIds = "items";
    private static final String ItemNums = "itemsNum";
    private static final String RefHours = "ref";
    private static final String StartDate = "sTime";
    private static final String EndDate = "eTime";


    public static StoreVO trans(Map params) throws LogicException{
        StoreVO storeVO = new StoreVO();
        try {
            String id = (String) params.getOrDefault(ID, null);
            if(id != null) storeVO.setId(Integer.parseInt(id));
            String type = (String) params.getOrDefault(Type, null);
            CodeStore.CreateStoreParamsError.throwIfTrue(type == null);
            List<String> itemIds = (List<String>) params.getOrDefault(ItemIds, null);
            CodeStore.CreateStoreParamsError.throwIfTrue(itemIds == null);
            List<String> refHours = (List<String>) params.getOrDefault(RefHours, null);
            CodeStore.CreateStoreParamsError.throwIfTrue(refHours == null);
            String startDate = (String) params.get(StartDate);
            CodeStore.CreateStoreParamsError.throwIfTrue(startDate == null);
            String endDate = (String) params.get(EndDate);
            CodeStore.CreateStoreParamsError.throwIfTrue(endDate == null);
            int[] refs = new int[refHours.size()];
            for (int i = 0; i < refs.length; i++) {
                refs[i] = Integer.parseInt(refHours.get(i));
            }
            storeVO.setRefreshHours(refs);
            storeVO.setType(Integer.parseInt(type));
            CodeStore.CreateStoreParamsError.throwIfTrue(storeVO.getType() <= 0);
            storeVO.setStartTime(DateUtil.stringToDate(startDate).getTime());
            storeVO.setEndTime(DateUtil.stringToDate(endDate).getTime());
            for (int i = 0; i < itemIds.size(); i++) {
                int itemId = Integer.parseInt(itemIds.get(i));
                ShopItemConfig config = CacheConfig.getCfg(ShopItemConfig.class, itemId);
                CodeStore.CreateStoreNotFindItemConfig.throwIfTrue(config == null);
                StoreItemVO itemVO = new StoreItemVO();
                itemVO.setCid(config.getId());
                itemVO.setIndex(i + 1);
                storeVO.getItemVOs().add(itemVO);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new LogicException(CodeGeneral.General_ServerException.get());
        }
        return storeVO;
    }

}
