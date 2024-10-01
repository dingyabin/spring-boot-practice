/*
Navicat MySQL Data Transfer

Source Server         : 本机
Source Server Version : 50729
Source Host           : localhost:3306
Source Database       : sys_common

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2024-10-01 22:35:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for distribute_ids_range
-- ----------------------------
DROP TABLE IF EXISTS `distribute_ids_range`;
CREATE TABLE `distribute_ids_range` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `max_id` bigint(20) unsigned NOT NULL,
  `step` int(11) NOT NULL,
  `biz_type` varchar(100) NOT NULL,
  `version` bigint(20) unsigned NOT NULL,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_biz_type` (`biz_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=100002 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of distribute_ids_range
-- ----------------------------
INSERT INTO `distribute_ids_range` VALUES ('100000', '18220', '200', 'ORDER', '81', '2024-10-01 22:33:35');
INSERT INTO `distribute_ids_range` VALUES ('100001', '5949460', '200', 'DEFAULT', '29581', '2024-10-01 22:35:00');

-- ----------------------------
-- Table structure for ip_worker_config
-- ----------------------------
DROP TABLE IF EXISTS `ip_worker_config`;
CREATE TABLE `ip_worker_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) NOT NULL,
  `work_id` int(10) NOT NULL,
  `data_center_id` int(10) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_ip` (`ip`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of ip_worker_config
-- ----------------------------
INSERT INTO `ip_worker_config` VALUES ('1', '192.168.175.1', '1', '1', '2024-09-30 17:06:37');
