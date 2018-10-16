package com.design.context;

/**
 * Created by DJL on 2017/5/25.
 *
 * @ClassName PlatformCode
 * @Description 业务异常码
 */
public interface PlatformCode {
    int SUCCESS=0;
    int FAIL=1;

    int PARAMETER_ERROR=10001;//参数错误
    int PARAMETER_FORMAT_ERROR=10002;//参数格式错误
    int PARAMETER_NULL=10003;//参数为空
    int SERVICE_ERROR=10004;//业务错误
    int ACCOUNT_IS_NOT_MAIL=10005;//不是邮箱
    int PARAMETER_OUT_TIME=10006;//参数失效
    int PENETRATION_ERROR=10007;//透传参数错误

    int PAYMENT_AUTOGRAPH_ERROR=20001;//充值签名错误
    int PAYMENT_QUERY_ERROR=20002;//充值查询错误
    int PAYMENT_NO_AUTOGRAPH=20003;//订单未签名
    int PAYMENT_AUTOGRAPH=20005;//订单已存在
    int PAYMENT_USE=20004;//该订单已使用
    int PAYMENT_USE_2=20007;//该订单已使用或未验证
    int PAYMENT_IN_VALIDATION=20006;//订单还没处理完
    int PAYMENT_EXCEPTION=20007;//订单处理异常
    int PAYMENT_ERROR=20008;//订单处理异常
    int DELIVER_ING=20009;//订单处理异常
    int PRICE_ERROR=20010;//物品不对应
    int DELIVER_AUTOGRAPH=20011;//订单已存在
    int GOODS_NOT_EXISTENCE=20012;//物品不存在
    int PAYMENT_OPERATE_FAST=20013;//充值操作太快
    int TRANSACTION_DOES_NOT_EXIST=20014;//订单不存在

    int ACCOUNT_NOT_EXISTENCE=30001;//账号不存在
    int ACCOUNT_NOT_NAME_OR_PASSWORD=30002;//没账号或者密码
    int ACCOUNT_PASSWORD_ERROR=30003;//密码错误
    int ACCOUNT_NOT_APP_ID=30004;//无APP-ID
    int ACCOUNT_SIGN_ERROR=30005;//签名数据错误
    int ACCOUNT_TOKEN_ERROR=30006;//token错误
    int ACCOUNT_IS_BINDING =30007;//该账号已经注册
    int ACCOUNT_NOT_CERTIFIED =30009;//该账号没认证
    int ACCOUNT_PASSWORD_ALIKE =30010;//两次密码相同
    int ACCOUNT_FREEZE =30011;//此账号冻结
    int ACCOUNT_LOGIN_INVALID =30012;//此账号冻结

    int DATA_ADD_ERROR=40004;//数据添加

    int CALLBACK_ERROR=50001;

    int SMS_SEND_ERROR=60001;//短信发送错误
    int SMS_OPERATE_FAST=60002;//短信操作太快
    int COD_ERROR =60003;//验证码错误

    int DELIVER_ERROR=70001;//发货错误;

}
