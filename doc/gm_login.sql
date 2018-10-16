/*
Navicat MySQL Data Transfer

Source Server         : 189
Source Server Version : 50636
Source Host           : 106.75.142.189:3306
Source Database       : gm_login

Target Server Type    : MYSQL
Target Server Version : 50636
File Encoding         : 65001

Date: 2018-08-13 16:18:07
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for open_key
-- ----------------------------
DROP TABLE IF EXISTS `open_key`;
CREATE TABLE `open_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appID` varchar(20) NOT NULL,
  `versionID` int(11) NOT NULL COMMENT '关联t_version.id',
  `openID` varchar(50) NOT NULL,
  PRIMARY KEY (`id`,`appID`,`openID`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_game
-- ----------------------------
DROP TABLE IF EXISTS `t_game`;
CREATE TABLE `t_game` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(15) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_group
-- ----------------------------
DROP TABLE IF EXISTS `t_group`;
CREATE TABLE `t_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `versionId` int(11) NOT NULL,
  `gid` int(11) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `icon` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `type` varchar(10) NOT NULL,
  `message` varchar(150) NOT NULL,
  `modifyDate` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5716 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupId` int(11) NOT NULL,
  `title` varchar(20) NOT NULL,
  `url` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=381 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_param
-- ----------------------------
DROP TABLE IF EXISTS `t_param`;
CREATE TABLE `t_param` (
  `id` int(11) NOT NULL,
  `param` varchar(100) NOT NULL,
  `dsp` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `password` varchar(100) NOT NULL,
  `createTime` bigint(20) NOT NULL,
  `uid` int(11) NOT NULL DEFAULT '0',
  `root` tinyint(3) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_version
-- ----------------------------
DROP TABLE IF EXISTS `t_version`;
CREATE TABLE `t_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gameId` int(11) NOT NULL,
  `version` varchar(50) NOT NULL,
  `url` varchar(70) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for uid_mid
-- ----------------------------
DROP TABLE IF EXISTS `uid_mid`;
CREATE TABLE `uid_mid` (
  `userId` int(11) NOT NULL,
  `menuId` int(11) NOT NULL,
  KEY `A` (`userId`),
  KEY `B` (`menuId`),
  CONSTRAINT `A` FOREIGN KEY (`userId`) REFERENCES `t_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `B` FOREIGN KEY (`menuId`) REFERENCES `t_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
