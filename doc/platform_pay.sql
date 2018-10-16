/*
Navicat MySQL Data Transfer

Source Server         : 189
Source Server Version : 50636
Source Host           : 106.75.142.189:3306
Source Database       : platform_pay

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2018-08-13 16:22:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for details_fun
-- ----------------------------
DROP TABLE IF EXISTS `details_fun`;
CREATE TABLE `details_fun` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `basicId` int(11) NOT NULL,
  `orderId` varchar(100) NOT NULL,
  `money` double(11,2) NOT NULL,
  `currency` varchar(10) NOT NULL,
  `orderMsg` text NOT NULL,
  `createTime` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for details_yinghun
-- ----------------------------
DROP TABLE IF EXISTS `details_yinghun`;
CREATE TABLE `details_yinghun` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `basicId` int(11) NOT NULL,
  `orderId` varchar(100) NOT NULL,
  `money` double(11,2) NOT NULL,
  `currency` varchar(10) NOT NULL,
  `orderMsg` text NOT NULL,
  `createTime` varchar(25) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for order_basic
-- ----------------------------
DROP TABLE IF EXISTS `order_basic`;
CREATE TABLE `order_basic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(40) NOT NULL COMMENT '账号ID',
  `orderUuid` varchar(40) NOT NULL COMMENT '唯一订单',
  `money` int(11) NOT NULL COMMENT '金额人民币分',
  `state` int(3) NOT NULL COMMENT '订单状态，1:未支付，2:支付成功，3:支付失败，4:已发货，5:发货成功，6:发货失败',
  `app` varchar(20) NOT NULL COMMENT '游戏编号',
  `createTime` bigint(20) NOT NULL COMMENT '时间',
  `payType` int(3) NOT NULL COMMENT '充值方式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=441 DEFAULT CHARSET=utf8;
