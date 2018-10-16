/*
Navicat MySQL Data Transfer

Source Server         : 189
Source Server Version : 50636
Source Host           : 106.75.142.189:3306
Source Database       : platform_deliver

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2018-08-13 16:19:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for deliver_goods
-- ----------------------------
DROP TABLE IF EXISTS `deliver_goods`;
CREATE TABLE `deliver_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app` varchar(20) NOT NULL,
  `payType` int(11) NOT NULL COMMENT '充值渠道',
  `sendState` int(11) NOT NULL COMMENT '发货状态,1未发货，0发货成功，3根据发货服返回填入',
  `orderNumber` varchar(70) NOT NULL COMMENT '订单号',
  `orderUuid` varchar(70) NOT NULL COMMENT '订单UUID',
  `goodsId` varchar(50) NOT NULL COMMENT '物品ID',
  `serverId` varchar(30) NOT NULL COMMENT '服务器ID',
  `pid` varchar(50) NOT NULL COMMENT '玩家PID',
  `price` int(11) NOT NULL COMMENT '金额 人民币 最小分',
  `channel` varchar(10) NOT NULL COMMENT '角色渠道',
  `logTime` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for price_list
-- ----------------------------
DROP TABLE IF EXISTS `price_list`;
CREATE TABLE `price_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app` varchar(20) NOT NULL COMMENT '游戏',
  `goodsId` varchar(30) NOT NULL COMMENT '道具',
  `money` int(11) NOT NULL COMMENT '价格 RMB 最小',
  `name` varchar(64) DEFAULT NULL COMMENT '套餐名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for send_interface
-- ----------------------------
DROP TABLE IF EXISTS `send_interface`;
CREATE TABLE `send_interface` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app` varchar(20) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `sendUrl` varchar(100) NOT NULL COMMENT '发货地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
