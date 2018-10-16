package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bean.OrderInfo;
import com.config.PaymentConfig;
import com.design.context.PlatformCode;
import com.design.context.SdkKey;
import com.design.exception.PlatformException;
import com.design.util.*;
import com.service.PaymentServiceIF;
import org.apache.commons.codec.digest.DigestUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by HP on 2018/5/31.
 */
@Service("yhPaymentService")
public class YhPaymentService implements PaymentServiceIF {

    private final JdbcTemplate jdbcTemplate;

    private static final String QUERY_GOODS_MONEY="select id,goodsId,money from platform_deliver.price_list where app=? and money=?";

    @Autowired
    public YhPaymentService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @NotNull
    @Override
    public String saveTrade(@NotNull Map<?, ?> base, @NotNull Map<?, ?> attach) throws PlatformException {
        Map<String,Object> data = new HashMap<>();
        data.put("accountId",base.get(SdkKey.ACCOUNT_ID));
        data.put("money",base.get("money"));
        data.put("app",base.get(SdkKey.APP_ID).toString()+base.get(SdkKey.OPEN_ID).toString());
        data.put("time",System.currentTimeMillis());
        data.put("payType",base.get("payType"));

        saveOrder(data, jdbcTemplate);
        String uuid = data.get(SdkKey.PAY_UUID).toString();
        return uuid;
    }

    @Override
    public void payVerification(@NotNull Map<?, ?> msg) throws PlatformException {
        int status = varificationOrderStatus(msg);
        if (status == 5){
            throw new PlatformException("订单已完成发货");
        }else if (msg.get("orderStatus").equals("0")){
            jdbcTemplate.update(getUPDATE_BASIC(),3,msg.get("cpOrderId"));
            throw new PlatformException("Failure of order payment");
        }else {
            verificationOrder(msg);
            verificationOrderInfo(msg);
            verificationSign((Map<String,Object>)msg);

            jdbcTemplate.update(getUPDATE_BASIC(),2,msg.get("cpOrderId"));
            
            List<Map<String,Object>> detailsYH = jdbcTemplate.queryForList(getDetailsYH(),msg.get("cpOrderId"));
           if (detailsYH == null && detailsYH.size() == 0){
               Map<String,Object> orderBasicMap = getOrderBasic(msg);
               int basicId = Integer.parseInt(orderBasicMap.get("basicId").toString());
               String orderId = orderBasicMap.get("orderId").toString();
               int money = Integer.parseInt(orderBasicMap.get("money").toString());
               String currency =PaymentConfig.CURRENCY;
               String orderMsg = orderBasicMap.get("orderMsg").toString();
               String createTime = DateUtil.getCurrentTimeMillisToString(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss");
               jdbcTemplate.update(setDetailsYingHun(),basicId,orderId,money,currency,orderMsg,createTime);
           }

            Map<String,Object> callbackInfoMap = JSONObject.parseObject(msg.get("callbackInfo1").toString(),Map.class);
            Integer serverId =Integer.parseInt(callbackInfoMap.get("sid").toString());
            String serverPath = getServerPath(serverId);
            OrderInfo deliverJson = getDeliverJson(msg,callbackInfoMap);
            try {
                System.out.println("数据存入deliver_goods");
                jdbcTemplate.update(setGoddsDeliver(),msg.get("appId"),PaymentConfig.YING_HUN_TYPE,1,msg.get("cpOrderId"),msg.get("bfOrderId"),callbackInfoMap.get("goodsId"),"99",callbackInfoMap.get("pid"),Integer.parseInt(msg.get("money").toString()),msg.get("channelId"),System.currentTimeMillis());
                String param = "orderInfo=" + JSONObject.toJSONString(deliverJson);
                System.out.println("向发货服发送发货信息，参数："+param);
                String ret = HttpUtil.doPost(serverPath,param);
                System.out.println("发货服返回的数据："+ret);

                if (!ret.equals("0")) {
                    throw new PlatformException("发货服返回失败");
                }else {
                    jdbcTemplate.update(getUPDATE_BASIC(),4,msg.get("cpOrderId"));
                }
            }catch (PlatformException p){
                throw new PlatformException("游戏服调用异常");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private String setGoddsDeliver() {
        return "INSERT INTO platform_deliver.deliver_goods(app,payType,sendState,orderNumber,orderUuid,goodsId,serverId,pid,price,channel,logTime)VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    }


    private String getDetailsYH() {
        return "SELECT id FROM platform_pay.details_yinghun WHERE orderId=?;";
    }

    private Map<String,Object> getOrderBasic(Map<?, ?> msg) {
        List<Map<String,Object>> orderBasic = jdbcTemplate.queryForList(getGET_BASIC(),msg.get("cpOrderId"));
        Map<String,Object> detailsYingHun = new HashMap<>();
        detailsYingHun.put("basicId",Integer.parseInt(orderBasic.get(0).get("id").toString()));
        detailsYingHun.put("orderId",msg.get("cpOrderId"));
        detailsYingHun.put("money",msg.get("money"));
        detailsYingHun.put("orderMsg",msg.toString());
        return detailsYingHun;
    }

    private String setDetailsYingHun() {

        return "insert into platform_pay.details_yinghun(id,basicId,orderId,money,currency,orderMsg,createTime) values (null,?,?,?,?,?,?);";
    }

    private int varificationOrderStatus(Map<?, ?> msg) throws PlatformException {

        List<Map<String,Object>> orders = jdbcTemplate.queryForList(getGET_STATE1(),msg.get("cpOrderId"));
        if (orders == null || orders.size()==0){
            throw new PlatformException("订单不存在");
        }

        return  Integer.parseInt(orders.get(0).get("state").toString());
    }

    private OrderInfo getDeliverJson(Map<?, ?> msg, Map<String,Object> callbackInfoMap) {
        Map<String,Object> map = new HashMap<>();
        map.put("pid",callbackInfoMap.get("pid"));
        map.put("orderNumber",msg.get("cpOrderId"));
        map.put("goodsId",callbackInfoMap.get("goodsId"));
        map.put("price",msg.get("money"));

//        String code = MD5Sign.getSign(map,PaymentConfig.PRIVATEKEY);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPid(callbackInfoMap.get("pid").toString());
        orderInfo.setOrderNumber(msg.get("cpOrderId").toString());
        orderInfo.setGoodsId(callbackInfoMap.get("goodsId").toString());
        orderInfo.setPrice(Integer.parseInt(msg.get("money").toString()));
        String code = orderInfo.getPid() + "&" + orderInfo.getOrderNumber() + "&" + orderInfo.getGoodsId() + "&" + orderInfo.getPrice()+"&" + PaymentConfig.PRIVATEKEY;
        String sign = DigestUtils.md5Hex(code);
        map.put("code",sign);
        orderInfo.setCode(sign);

        return orderInfo;

    }

    public String getServerPath(Integer serverId){
        String sql = getServerById(serverId);
//        Map<String,Object> serverMap = jdbcTemplate.queryForMap(sql,serverId);
        String serverPath = "http://192.168.1.46:10999/pay/order";
        return  serverPath;
    }

    private void verificationOrder(Map<?, ?> msg) throws PlatformException {
        String orderUuid = msg.get("cpOrderId").toString();
        List<Map<String,Object>> baseOrder = jdbcTemplate.queryForList(getGET_BASIC(),orderUuid);
        if (baseOrder == null && baseOrder.size() == 0){
            throw  new PlatformException("orderUuid Non-existent ");
        }
    }

    private void verificationOrderInfo(Map<?, ?> msg) throws PlatformException {
        String orderUuid = msg.get("cpOrderId").toString();
        Map<String,Object> orderMap = jdbcTemplate.queryForMap(getGET_BASIC(),orderUuid);

        if (orderMap != null && orderMap.size() > 0){
            Map<String,Object> callbackInfoMap = JSONObject.parseObject(msg.get("callbackInfo1").toString().replaceAll("=",":"));
            String uid = callbackInfoMap.get("uid").toString();
            if (msg.get("userId").equals(uid)){}else {
                throw  new PlatformException("accountId verification error");
            }
            if (Integer.parseInt(msg.get("money").toString())==Integer.parseInt(orderMap.get("money").toString())){
                System.out.println("appId:"+msg.get("appId")+"==goodsId"+callbackInfoMap.get("goodsId").toString());
                List<Map<String,Object>> GoodsInfo = jdbcTemplate.queryForList(getGoodsInfo(),callbackInfoMap.get("goodsId").toString(),msg.get("appId").toString());
                if (GoodsInfo == null || GoodsInfo.size() == 0 ){
                    throw new PlatformException("商品不存在");
                }else {
                    if (Integer.parseInt(GoodsInfo.get(0).get("money").toString())!=Integer.parseInt(msg.get("money").toString())){
                        throw new PlatformException("支付金额与商品价格不符");
                    }
                }
            }else {
                throw  new PlatformException("money verification error");
            }
            long createTime = Long.parseLong(orderMap.get("createTime").toString());
            long msgCreateTime = Long.parseLong(msg.get("time").toString()) * 1000;
            if (createTime<msgCreateTime){}else {
                throw  new PlatformException("orderTime verification error");
            }
        }else {
            throw new PlatformException("orderUuid Non-existent");
        }
    }

    private String getGoodsInfo() {
        return "SELECT * FROM platform_deliver.price_list WHERE goodsId=? and app=?";
    }

    private void verificationSign(Map<String, Object> msg) throws PlatformException {
        String sign = msg.get("sign").toString();
        String callbackInfo1=msg.get("callbackInfo1").toString();
        msg.remove("sign");
        msg.remove("callbackInfo1");
//        msg.put("callbackInfo","");
        String MD5Str = MD5Sign.getSign(msg,PaymentConfig.APP_SECRET);
        msg.put("callbackInfo1",callbackInfo1);
        if (sign.equals(MD5Str)){}else {
            throw new PlatformException("sign verification error");
        }
        }

    @Override
    public void updateSuccessOrder(@NotNull String uuid) {

    }
    @Override
    public int saveOrder(@NotNull Map<String, Object> saveMsg, @NotNull JdbcTemplate template) throws PlatformException {
        String judge = accountVerification(saveMsg.get(SdkKey.ACCOUNT_ID).toString());
        if (!judge.equals("0")) {
            throw new PlatformException("accountId is NOT EXISTENCE", PlatformCode.ACCOUNT_NOT_EXISTENCE);
        }
        saveMsg.put(SdkKey.PAY_UUID ,StringUtil.createUUID());
        saveMsg.put("state",getNO_PAY());
        return saveBasicMsg(template, saveMsg);
    }
    @Override
    public int saveOrder(@NotNull Map<String, Object> msg, int priceType, int payType, @NotNull JdbcTemplate template) throws PlatformException {
        return 0;
    }
    @Override
    public int saveBasicMsg(@NotNull JdbcTemplate template, @NotNull Map<String, ?> data) {

        String sql = getADD_BASIC().toString();
        template.update(sql,data.get(SdkKey.ACCOUNT_ID),data.get(SdkKey.PAY_UUID),data.get("money"),data.get("state"),data.get("app"),data.get("time"),data.get("payType"));
        return 0;
    }
    @Override
    public int updateBasicMsg(@NotNull JdbcTemplate template, @NotNull String uuid, int t) {
        return 0;
    }
    @Override
    public int updateBasicMsg(@NotNull JdbcTemplate template, int id, int t, int money) {
        return 0;
    }
    @Override
    public int getIndex(@NotNull JdbcTemplate template, @NotNull String uuid) {
        return 0;
    }
    @NotNull
    @Override
    public Map<String, Object> getState(@NotNull JdbcTemplate template, int id) {
        return null;
    }

    @NotNull
    @Override
    public String accountVerification(@NotNull String accountId) throws PlatformException {
        String p = URLTool.Encode(accountId);
        if (p != null) {
            return URLTool.sendMsg("http://192.168.1.46:9080/certified/isAccount?id=" + p);
        } else {
            return String.valueOf(PlatformCode.ACCOUNT_NOT_EXISTENCE);
        }
    }

    @NotNull
    @Override
    public Map<String, Object> setDeliver(@NotNull Map<?, ?> player, @NotNull String order, int payType) {
        return null;
    }

    @Override
    public int getTWD() {
        return 1;
    }

    @Override
    public int getCNY() {
        return 0;
    }

    @Override
    public int getHDK() {
        return 2;
    }

    @Override
    public int getUSD() {
        return 3;
    }

    @Override
    public int getPAY() {
        return 2;
    }

    @Override
    public int getNO_PAY() {
        return 1;
    }
    @Override
    public int getPAY_FALSE(){
        return 3;
    }
    @Override
    public int getDELIVER(){
        return 4;
    }
    @Override
    public int getDELIVER_SUCCESS(){
        return 5;
    }
    @Override
    public int getDELIVER_FALSE(){
        return 6;
    }

    @Override
    public int getUSE() {
        return 3;
    }

    @NotNull
    @Override
    public String getADD_BASIC() {
        return "insert INTO platform_pay.order_basic (id,uid,orderUuid,money,state,app,createTime,payType) VALUES (null,?,?,?,?,?,?,?);";
    }

    @NotNull
    @Override
    public String getUPDATE_BASIC() {
        return "UPDATE platform_pay.order_basic SET state=? WHERE orderUuid=?;";
    }

    @NotNull
    @Override
    public String getUPDATE_BASIC_2() {
        return "UPDATE platform_pay.order_basic SET state=?,money=? WHERE id=?;";
    }

    @NotNull
    @Override
    public String getGET_BASIC() {
        return "SELECT id,money,createTime FROM platform_pay.order_basic WHERE orderUuid=?;";
    }

    @NotNull
    @Override
    public String getGET_STATE() {
        return "SELECT state,orderUuid FROM platform_pay.order_basic WHERE id=?;";
    }

    public String getGET_STATE1() {
        return "SELECT state FROM platform_pay.order_basic WHERE orderUuid=?;";
    }

    public String gerBaseOrder(){return  "SELECT id,uid,orderUuid,money,state,app,createTime,payType FROM platform_pay.order_basic WHERE orderUuid=?";}

    public  String getServerById(Integer id){return "";}

    public static final String INSERT_INSIDE_ORDER="insert into platform_deliver.deliver_goods (app,payType,sendState,orderNumber," +
            "orderUuid,goodsId,serverId,pid,price,channel,logTime) value (?,?,?,?,?,?,?,?,?,?,?)";

    @Override
    public void insideAccountPay(@NotNull String msg) throws PlatformException{
        Map msgMap = JSONObject.parseObject(msg,Map.class);
        String orderNumber = StringUtil.createUUID();
        String orderUuid = "inside"+DateUtil.getDateTime().replaceAll("-","").replaceAll(":","").replaceAll(" ","");
        jdbcTemplate.update(INSERT_INSIDE_ORDER,msgMap.get("appid"),msgMap.get("payType"),0,orderNumber,orderUuid,
               msgMap.get("goodsId"),msgMap.get("sid"),msgMap.get("pid"), msgMap.get("money"),msgMap.get("channel"),System.currentTimeMillis());

        try {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setOrderNumber(orderNumber);
            orderInfo.setGoodsId(msgMap.get("goodsId").toString());
            orderInfo.setPid(msgMap.get("pid").toString());
            orderInfo.setPrice(Integer.parseInt(msgMap.get("money").toString()));
            String code = orderInfo.getPid() + "&" + orderInfo.getOrderNumber() + "&" + orderInfo.getGoodsId() + "&" + orderInfo.getPrice()+"&" + PaymentConfig.PRIVATEKEY;
            String sign = DigestUtils.md5Hex(code);
            orderInfo.setCode(sign);
            String param = "orderInfo=" + JSONObject.toJSONString(orderInfo);
            String serverPath = getServerPath(Integer.parseInt(msgMap.get("sid").toString()));
            System.out.println("向发货服发送发货信息，参数："+param);
            String ret = HttpUtil.doPost(serverPath,param);
            System.out.println("发货服返回的数据："+ret);

            if (!ret.equals("0")) {
                throw new PlatformException("发货服返回失败");
            }
        }catch (PlatformException p){
            throw new PlatformException("游戏服调用异常");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> queryGoodsByMoney(String money,String appid) {
        Map goods = jdbcTemplate.queryForMap(QUERY_GOODS_MONEY,appid,money);
        return goods;
    }
}
