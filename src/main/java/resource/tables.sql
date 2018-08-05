/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50545
Source Host           : localhost:3306
Source Database       : recommenderdb

Target Server Type    : MYSQL
Target Server Version : 50545
File Encoding         : 65001

Date: 2018-08-05 08:57:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for items
-- ----------------------------
DROP TABLE IF EXISTS `items`;
CREATE TABLE `items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `published_year` varchar(12) NOT NULL,
  `tags` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=164980 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for item_similarities
-- ----------------------------
DROP TABLE IF EXISTS `item_similarities`;
CREATE TABLE `item_similarities` (
  `itemID1` int(11) NOT NULL,
  `itemID2` int(11) NOT NULL,
  `similarity` double NOT NULL DEFAULT '0',
  KEY `itemID1` (`itemID1`),
  KEY `itemID2` (`itemID2`),
  CONSTRAINT `item_similarities_ibfk_1` FOREIGN KEY (`itemID1`) REFERENCES `items` (`id`) ON DELETE CASCADE,
  CONSTRAINT `item_similarities_ibfk_2` FOREIGN KEY (`itemID2`) REFERENCES `items` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for rates
-- ----------------------------
DROP TABLE IF EXISTS `rates`;
CREATE TABLE `rates` (
  `userID` int(11) NOT NULL,
  `itemID` int(11) NOT NULL,
  `preference` float NOT NULL DEFAULT '0',
  `timestamp` int(11) NOT NULL DEFAULT '0',
  KEY `rates_index1` (`userID`,`itemID`),
  KEY `rates_index2` (`userID`),
  KEY `rates_index3` (`itemID`),
  CONSTRAINT `rates_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `rates_ibfk_2` FOREIGN KEY (`itemID`) REFERENCES `items` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone` varchar(36) NOT NULL,
  `tags` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=673 DEFAULT CHARSET=utf8;
