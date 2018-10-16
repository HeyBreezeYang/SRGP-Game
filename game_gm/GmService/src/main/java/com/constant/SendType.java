package com.constant;

/**
 * Created by DJL on 2016/11/28.
 *
 * @ClassName SendType
 * @Description 发送类型
 */
public interface SendType {
    int SUCCESS=0;

    int NOTICE_GET_FAIL=80101;//公告获取失败
    int PRIZE_CODE_ERROR=80201;//兑换码错误
    int PRIZE_CODE_UNAVAILABLE=80202;//兑换码不可使用
    int PRIZE_CODE_OUT_TIME=80203;//兑换码过期
    int PRIZE_CODE_USE=80204;//兑换码已经使用
    int PRIZE_CODE_CANNT_USE_ACHIVE=80205;//不能使用该兑换码（同批次情况）
    int PRIZE_CODE_CANNT_USE_BINDING=80206;//不能使用该兑换码（关联情况）
    int SEND_GOODS_ERROR=80207;//邮件发送失败
    int SERVER_ERROR=80208;//游戏服连接失败
    int MSG_ERROR=80301;

}
