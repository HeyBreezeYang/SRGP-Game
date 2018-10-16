/*
Navicat MySQL Data Transfer

Source Server         : 88
Source Server Version : 50636
Source Host           : 140.143.244.88:3306
Source Database       : analysis_log

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2018-08-13 16:16:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ban_back
-- ----------------------------
DROP TABLE IF EXISTS `ban_back`;
CREATE TABLE `ban_back` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `sid` varchar(128) DEFAULT NULL,
  `playerName` varchar(128) DEFAULT NULL,
  `type` int(32) DEFAULT NULL,
  `logTime` bigint(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for base_data
-- ----------------------------
DROP TABLE IF EXISTS `base_data`;
CREATE TABLE `base_data` (
  `id` int(11) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `platform` varchar(20) NOT NULL,
  `logTime` varchar(12) NOT NULL,
  `createNum` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for boss_night
-- ----------------------------
DROP TABLE IF EXISTS `boss_night`;
CREATE TABLE `boss_night` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `sid` varchar(128) DEFAULT NULL,
  `logTime` varchar(128) DEFAULT NULL,
  `channel` varchar(128) DEFAULT NULL,
  `fre` varchar(128) DEFAULT NULL,
  `num` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for boss_noon
-- ----------------------------
DROP TABLE IF EXISTS `boss_noon`;
CREATE TABLE `boss_noon` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `sid` varchar(128) DEFAULT NULL,
  `logTime` varchar(128) DEFAULT NULL,
  `nop` varchar(128) DEFAULT NULL,
  `num` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for checkpoint
-- ----------------------------
DROP TABLE IF EXISTS `checkpoint`;
CREATE TABLE `checkpoint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(20) DEFAULT NULL,
  `logTime` varchar(20) DEFAULT NULL,
  `channel` varchar(20) DEFAULT NULL,
  `task` varchar(10) NOT NULL,
  `passNum` int(11) NOT NULL,
  `passFre` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cyd_cl
-- ----------------------------
DROP TABLE IF EXISTS `cyd_cl`;
CREATE TABLE `cyd_cl` (
  `id` int(11) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `logTime` varchar(20) NOT NULL,
  `channel` varchar(20) NOT NULL,
  `challenge` int(11) NOT NULL COMMENT '挑战次数',
  `challengeNum` int(11) NOT NULL COMMENT '挑战人数',
  `vengeance` int(11) NOT NULL COMMENT '复仇次数',
  `negotiate` int(11) NOT NULL COMMENT '谈判次数',
  `fight` int(11) NOT NULL COMMENT '出战令',
  `integral` int(11) NOT NULL COMMENT '积分涨幅',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cyd_disport
-- ----------------------------
DROP TABLE IF EXISTS `cyd_disport`;
CREATE TABLE `cyd_disport` (
  `id` int(11) NOT NULL,
  `channel` varchar(20) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `type` int(11) NOT NULL,
  `logTime` varchar(20) NOT NULL,
  `recType` int(11) NOT NULL COMMENT '资源类型',
  `recNum` int(11) NOT NULL COMMENT '消耗资源',
  `actor` int(11) NOT NULL COMMENT '参与人数',
  `maxLevel` int(11) NOT NULL COMMENT '最高层数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cyd_jj
-- ----------------------------
DROP TABLE IF EXISTS `cyd_jj`;
CREATE TABLE `cyd_jj` (
  `id` int(11) NOT NULL,
  `logTime` varchar(20) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `channel` varchar(20) NOT NULL,
  `seat` int(11) NOT NULL COMMENT '格子位',
  `openNum` int(11) NOT NULL COMMENT '开启人数',
  `actor` int(11) NOT NULL COMMENT '参与人数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cyd_lianyin
-- ----------------------------
DROP TABLE IF EXISTS `cyd_lianyin`;
CREATE TABLE `cyd_lianyin` (
  `id` int(11) NOT NULL,
  `logTime` varchar(20) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `channel` varchar(20) NOT NULL,
  `sonLv` int(11) NOT NULL COMMENT '子嗣品级',
  `marriage` int(11) NOT NULL COMMENT '联姻子嗣',
  `unMarriage` int(11) NOT NULL COMMENT '未联姻子嗣',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cyd_xb
-- ----------------------------
DROP TABLE IF EXISTS `cyd_xb`;
CREATE TABLE `cyd_xb` (
  `id` int(11) NOT NULL,
  `logTime` varchar(20) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `channel` varchar(20) NOT NULL,
  `createTeam` int(11) NOT NULL COMMENT '创建队伍次数',
  `createPlayer` int(11) NOT NULL COMMENT '创建队伍人数',
  `actor` int(11) NOT NULL COMMENT '参加人数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cyd_zlz
-- ----------------------------
DROP TABLE IF EXISTS `cyd_zlz`;
CREATE TABLE `cyd_zlz` (
  `id` int(11) NOT NULL,
  `logTime` varchar(20) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `channel` varchar(20) NOT NULL,
  `stall` int(11) NOT NULL COMMENT '档位',
  `roomNum` int(11) NOT NULL COMMENT '开包间数',
  `roomPlayer` int(11) NOT NULL COMMENT '开包间人数',
  `actor` int(11) NOT NULL COMMENT '参加人数',
  `integral` int(11) NOT NULL COMMENT '积分涨幅',
  `refresh` int(11) NOT NULL COMMENT '刷新次数',
  `treNum` int(11) NOT NULL COMMENT '钻石消耗',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for data_snapshot
-- ----------------------------
DROP TABLE IF EXISTS `data_snapshot`;
CREATE TABLE `data_snapshot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(20) NOT NULL,
  `createNum` int(11) NOT NULL,
  `loginCount` int(11) NOT NULL,
  `money` int(11) NOT NULL,
  `payNum` int(11) NOT NULL,
  `olNum` int(11) NOT NULL,
  `logTime` varchar(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dup_count
-- ----------------------------
DROP TABLE IF EXISTS `dup_count`;
CREATE TABLE `dup_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `baseId` int(11) NOT NULL,
  `logTime` varchar(12) NOT NULL,
  `dup` varchar(20) NOT NULL,
  `firstPass` int(11) NOT NULL,
  `passNum` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `sid` varchar(20) DEFAULT NULL,
  `logTime` varchar(12) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `gCid` varchar(20) DEFAULT NULL,
  `numC` int(32) DEFAULT NULL,
  `numP` int(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for login
-- ----------------------------
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `pid` varchar(30) DEFAULT NULL,
  `loginTime` varchar(12) DEFAULT NULL,
  `sid` varchar(20) DEFAULT NULL,
  `platform` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for loss_rate
-- ----------------------------
DROP TABLE IF EXISTS `loss_rate`;
CREATE TABLE `loss_rate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `baseId` int(11) DEFAULT NULL,
  `tDay` int(11) DEFAULT NULL,
  `lossNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ltv
-- ----------------------------
DROP TABLE IF EXISTS `ltv`;
CREATE TABLE `ltv` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `baseID` int(11) NOT NULL,
  `tDay` int(11) NOT NULL,
  `num` int(11) NOT NULL,
  `money` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for monetary_statistics
-- ----------------------------
DROP TABLE IF EXISTS `monetary_statistics`;
CREATE TABLE `monetary_statistics` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `sid` varchar(128) DEFAULT NULL,
  `channel` varchar(20) DEFAULT NULL,
  `logTime` varchar(128) DEFAULT NULL,
  `typeC` varchar(128) DEFAULT NULL,
  `typeU` varchar(128) DEFAULT NULL,
  `numC` int(32) DEFAULT NULL,
  `numP` int(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for online_num
-- ----------------------------
DROP TABLE IF EXISTS `online_num`;
CREATE TABLE `online_num` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `logTime` varchar(30) DEFAULT NULL,
  `sid` varchar(30) DEFAULT NULL,
  `hour` int(3) DEFAULT NULL,
  `maxNum` int(11) DEFAULT NULL,
  `minNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay
-- ----------------------------
DROP TABLE IF EXISTS `pay`;
CREATE TABLE `pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(20) NOT NULL,
  `channel` varchar(20) NOT NULL,
  `logTime` varchar(30) NOT NULL,
  `pid` varchar(50) NOT NULL,
  `price` int(11) NOT NULL,
  `gid` varchar(20) NOT NULL,
  `lv` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for player
-- ----------------------------
DROP TABLE IF EXISTS `player`;
CREATE TABLE `player` (
  `pid` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `uid` varchar(50) NOT NULL,
  `createTime` varchar(30) NOT NULL,
  `platform` varchar(20) NOT NULL,
  `sid` varchar(20) NOT NULL,
  `payment` int(11) DEFAULT NULL COMMENT '累计充值',
  `treasure` int(11) DEFAULT NULL COMMENT '当前钻石',
  `lv` int(11) DEFAULT NULL,
  `vip` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL COMMENT '当前金币',
  `fight` int(11) DEFAULT NULL,
  `lastPayTime` varchar(20) DEFAULT NULL,
  `lastLoginTime` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for prize_code_use
-- ----------------------------
DROP TABLE IF EXISTS `prize_code_use`;
CREATE TABLE `prize_code_use` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prizeCode` varchar(100) NOT NULL,
  `pid` varchar(100) DEFAULT NULL,
  `serverId` varchar(50) DEFAULT NULL,
  `platform` varchar(30) DEFAULT NULL,
  `modifyTime` bigint(20) DEFAULT NULL,
  `achiveId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(20) NOT NULL,
  `channel` varchar(20) NOT NULL,
  `logTime` varchar(20) NOT NULL,
  `goods` varchar(11) NOT NULL,
  `num` int(11) NOT NULL,
  `fre` int(11) NOT NULL,
  `payNum` int(11) NOT NULL,
  `shopType` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for synthetical
-- ----------------------------
DROP TABLE IF EXISTS `synthetical`;
CREATE TABLE `synthetical` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` varchar(20) DEFAULT NULL,
  `platform` varchar(20) DEFAULT NULL,
  `logTime` varchar(20) DEFAULT NULL,
  `newPlayer` int(11) DEFAULT NULL,
  `player` int(11) DEFAULT NULL,
  `register` int(11) DEFAULT NULL,
  `login` int(11) DEFAULT NULL,
  `pay` int(11) DEFAULT NULL,
  `dayPay` int(11) DEFAULT NULL,
  `newPay` int(11) DEFAULT NULL,
  `payNum` int(11) DEFAULT NULL,
  `dayPayNum` int(11) DEFAULT NULL,
  `newPayNum` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_taskobj
-- ----------------------------
DROP TABLE IF EXISTS `t_taskobj`;
CREATE TABLE `t_taskobj` (
  `taskName` varchar(40) NOT NULL,
  `obj` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vip
-- ----------------------------
DROP TABLE IF EXISTS `vip`;
CREATE TABLE `vip` (
  `id` int(11) NOT NULL,
  `baseId` int(11) NOT NULL,
  `vip` int(11) NOT NULL,
  `num` int(11) NOT NULL,
  `login` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
