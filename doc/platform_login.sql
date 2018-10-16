/*
Navicat MySQL Data Transfer

Source Server         : 189
Source Server Version : 50636
Source Host           : 106.75.142.189:3306
Source Database       : platform_login

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2018-08-13 16:23:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account_admin
-- ----------------------------
DROP TABLE IF EXISTS `account_admin`;
CREATE TABLE `account_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `adminAccount` varchar(80) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for account_attach
-- ----------------------------
DROP TABLE IF EXISTS `account_attach`;
CREATE TABLE `account_attach` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) NOT NULL,
  `device` varchar(30) DEFAULT NULL,
  `sysName` varchar(30) DEFAULT NULL,
  `sysVersion` varchar(30) DEFAULT NULL,
  `operators` varchar(30) DEFAULT NULL,
  `appVersion` varchar(30) DEFAULT NULL,
  `budleId` varchar(30) DEFAULT NULL,
  `cpuType` varchar(30) DEFAULT NULL,
  `cpuCore` varchar(30) DEFAULT NULL,
  `cpuGhz` varchar(30) DEFAULT NULL,
  `machine` varchar(70) DEFAULT NULL,
  `wifiMac` varchar(70) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `channel` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for account_data
-- ----------------------------
DROP TABLE IF EXISTS `account_data`;
CREATE TABLE `account_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) NOT NULL,
  `createTime` bigint(20) NOT NULL,
  PRIMARY KEY (`id`,`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3842 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for account_freeze
-- ----------------------------
DROP TABLE IF EXISTS `account_freeze`;
CREATE TABLE `account_freeze` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(60) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for account_inside
-- ----------------------------
DROP TABLE IF EXISTS `account_inside`;
CREATE TABLE `account_inside` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(32) NOT NULL COMMENT '服务器',
  `channel` varchar(32) NOT NULL COMMENT '渠道',
  `rolename` varchar(64) NOT NULL COMMENT '角色名',
  `pid` int(11) NOT NULL COMMENT '玩家id',
  `paymoney` int(11) DEFAULT '0' COMMENT '充值金额',
  `username` varchar(32) DEFAULT NULL COMMENT '使用人',
  `userphone` varchar(32) DEFAULT NULL COMMENT '使用人电话',
  `ascription` varchar(64) DEFAULT NULL COMMENT '使用人归属',
  `applyuser` varchar(32) NOT NULL COMMENT '申请运营',
  `applyreason` varchar(64) DEFAULT NULL COMMENT '申请原因',
  `status` int(1) DEFAULT NULL COMMENT '审核状态，0-待审核，1-审核通过，2-审核不通过，3-封禁',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `createtime` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `toexamineby` varchar(32) DEFAULT NULL COMMENT '审核人',
  `toexaminetime` bigint(20) DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for account_other
-- ----------------------------
DROP TABLE IF EXISTS `account_other`;
CREATE TABLE `account_other` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aId` int(11) NOT NULL COMMENT '关联account_data中id',
  `type` int(11) NOT NULL,
  `account` varchar(180) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `bindingTime` bigint(20) NOT NULL,
  `restTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`,`aId`)
) ENGINE=InnoDB AUTO_INCREMENT=3713 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for app_id
-- ----------------------------
DROP TABLE IF EXISTS `app_id`;
CREATE TABLE `app_id` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appId` varchar(30) NOT NULL,
  `openId` varchar(30) NOT NULL,
  `privateKey` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for first_login
-- ----------------------------
DROP TABLE IF EXISTS `first_login`;
CREATE TABLE `first_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountId` varchar(50) NOT NULL,
  `logTime` bigint(20) NOT NULL,
  `appId` varchar(20) NOT NULL,
  `platform` varchar(20) NOT NULL,
  PRIMARY KEY (`id`,`appId`,`accountId`)
) ENGINE=InnoDB AUTO_INCREMENT=3817 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ip_white_list
-- ----------------------------
DROP TABLE IF EXISTS `ip_white_list`;
CREATE TABLE `ip_white_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(32) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for login_day_log
-- ----------------------------
DROP TABLE IF EXISTS `login_day_log`;
CREATE TABLE `login_day_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) NOT NULL,
  `loginTime` bigint(20) NOT NULL COMMENT '每天的第一次登陆时间',
  `appId` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for login_error
-- ----------------------------
DROP TABLE IF EXISTS `login_error`;
CREATE TABLE `login_error` (
  `id` int(11) NOT NULL,
  `logTime` datetime NOT NULL,
  `network` varchar(30) DEFAULT NULL,
  `country` varchar(30) DEFAULT NULL,
  `appBuild` varchar(30) DEFAULT NULL,
  `language` varchar(30) DEFAULT NULL,
  `ramRem` varchar(30) DEFAULT NULL,
  `ramTotal` varchar(30) DEFAULT NULL,
  `romRem` varchar(30) DEFAULT NULL,
  `romTotal` varchar(30) DEFAULT NULL,
  `cardRem` varchar(30) DEFAULT NULL,
  `cardTotal` varchar(30) DEFAULT NULL,
  `resolution` varchar(30) DEFAULT NULL,
  `appVsnCode` varchar(30) DEFAULT NULL,
  `wifiBssId` varchar(70) DEFAULT NULL,
  `imei` varchar(50) DEFAULT NULL,
  `appVsnName` varchar(30) DEFAULT NULL,
  `packageName` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) NOT NULL,
  `loginTime` bigint(20) NOT NULL,
  `app` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20574 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for not_certified
-- ----------------------------
DROP TABLE IF EXISTS `not_certified`;
CREATE TABLE `not_certified` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(50) NOT NULL COMMENT '账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
