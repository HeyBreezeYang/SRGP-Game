/*
Navicat MySQL Data Transfer

Source Server         : 88
Source Server Version : 50636
Source Host           : 140.143.244.88:3306
Source Database       : gm_config

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2018-08-13 16:16:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for channel
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
  `id` int(11) NOT NULL,
  `cname` varchar(20) NOT NULL,
  `dsp` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for db_config
-- ----------------------------
DROP TABLE IF EXISTS `db_config`;
CREATE TABLE `db_config` (
  `id` varchar(20) NOT NULL,
  `ip` varchar(40) NOT NULL,
  `port` int(11) NOT NULL,
  `account` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `dsp` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for game_server
-- ----------------------------
DROP TABLE IF EXISTS `game_server`;
CREATE TABLE `game_server` (
  `logicID` int(11) NOT NULL AUTO_INCREMENT,
  `serverID` varchar(30) NOT NULL,
  `serverName` varchar(30) NOT NULL,
  `state` int(11) NOT NULL,
  `recommend` tinyint(4) NOT NULL,
  `serverIP` varchar(50) NOT NULL COMMENT '内网',
  `extranetIP` varchar(50) NOT NULL COMMENT '外网',
  `serverPort` int(11) NOT NULL,
  `httpPort` int(11) NOT NULL,
  `deliverPort` int(11) NOT NULL,
  `openTime` varchar(20) NOT NULL,
  `resourceURL` int(11) NOT NULL,
  `other` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`logicID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for gm_mail_data
-- ----------------------------
DROP TABLE IF EXISTS `gm_mail_data`;
CREATE TABLE `gm_mail_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(20) NOT NULL COMMENT '服务器id',
  `channel` varchar(20) NOT NULL COMMENT '渠道id',
  `email` varchar(255) NOT NULL COMMENT '邮件信息',
  `userName` varchar(255) NOT NULL COMMENT '发送的玩家',
  `createBy` varchar(25) NOT NULL COMMENT '创建人',
  `createTime` bigint(20) NOT NULL COMMENT '创建时间',
  `status` int(11) NOT NULL COMMENT '邮件状态,1:未审核，2:审核通过，3:审核失败',
  `toexamineBy` varchar(25) DEFAULT NULL COMMENT '审核人',
  `toexamineTime` bigint(20) DEFAULT NULL COMMENT '审核时间，也就是发送时间',
  `updateBy` varchar(25) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for individual_parameter
-- ----------------------------
DROP TABLE IF EXISTS `individual_parameter`;
CREATE TABLE `individual_parameter` (
  `id` varchar(10) NOT NULL,
  `prams` varchar(200) NOT NULL,
  `dsp` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for inside_account_log
-- ----------------------------
DROP TABLE IF EXISTS `inside_account_log`;
CREATE TABLE `inside_account_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(20) DEFAULT NULL,
  `channel` varchar(20) DEFAULT NULL,
  `rolename` varchar(32) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `operatorBy` varchar(32) DEFAULT NULL,
  `logTime` bigint(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel` varchar(10) NOT NULL,
  `type` varchar(10) NOT NULL,
  `msg` text COMMENT '公告信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for notice_set
-- ----------------------------
DROP TABLE IF EXISTS `notice_set`;
CREATE TABLE `notice_set` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `setting` varchar(255) NOT NULL COMMENT '公告设置项',
  `value` int(20) NOT NULL DEFAULT '0' COMMENT '设置公告弹出限制0:不限制.1:限制只弹出一次',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for prize_binding
-- ----------------------------
DROP TABLE IF EXISTS `prize_binding`;
CREATE TABLE `prize_binding` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `aid` int(11) NOT NULL,
  `bid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for prize_code
-- ----------------------------
DROP TABLE IF EXISTS `prize_code`;
CREATE TABLE `prize_code` (
  `aid` int(11) NOT NULL,
  `prizeCode` varchar(50) NOT NULL,
  PRIMARY KEY (`prizeCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for prize_code_attr
-- ----------------------------
DROP TABLE IF EXISTS `prize_code_attr`;
CREATE TABLE `prize_code_attr` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serverId` varchar(200) DEFAULT NULL,
  `platform` varchar(200) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '1，一次使用；-1每个人都可以使用；',
  `specialType` tinyint(4) DEFAULT '0',
  `cretime` bigint(20) DEFAULT NULL,
  `endTime` varchar(20) DEFAULT NULL,
  `dsp` varchar(30) DEFAULT NULL,
  `num` int(11) NOT NULL DEFAULT '0',
  `mailTitle` varchar(30) DEFAULT NULL,
  `mailText` text,
  `func` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for resource_url
-- ----------------------------
DROP TABLE IF EXISTS `resource_url`;
CREATE TABLE `resource_url` (
  `id` int(11) NOT NULL,
  `resource` varchar(255) NOT NULL,
  `dsp` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `type` varchar(128) DEFAULT NULL,
  `userName` varchar(128) DEFAULT NULL,
  `context` text,
  `logTime` bigint(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_timer
-- ----------------------------
DROP TABLE IF EXISTS `t_timer`;
CREATE TABLE `t_timer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `timeName` varchar(50) NOT NULL,
  `groupName` varchar(20) NOT NULL,
  `startTime` bigint(20) NOT NULL,
  `endTime` bigint(20) NOT NULL,
  `config` varchar(25) NOT NULL,
  `clazz` varchar(100) NOT NULL,
  `statues` tinyint(3) NOT NULL,
  `dsp` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
