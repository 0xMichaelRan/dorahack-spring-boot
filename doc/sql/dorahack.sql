/*
 Navicat Premium Data Transfer

 Source Server         : mysql-5.7.27
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : 127.0.0.1:3306
 Source Schema         : dorahack

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 05/03/2021 18:41:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for artwork
-- ----------------------------
DROP TABLE IF EXISTS `artwork`;
CREATE TABLE `artwork` (
  `id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `eth_address` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `properties` json DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `thumbnail_url` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `listing_round` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='藏品表';

-- ----------------------------
-- Records of artwork
-- ----------------------------
BEGIN;
INSERT INTO `artwork` VALUES ('1367772815805349889', '232322', 'finished', 'https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1638079650,2146947483&fm=26&gp=0.jpg', '232', '232323', '2021-03-05 09:43:46', NULL, '1', '23232323', '2323');
INSERT INTO `artwork` VALUES ('1367773169213112321', '232322', 'finished', 'https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1963304009,2816364381&fm=26&gp=0.jpg', '232', '232323', '2021-03-05 09:45:10', NULL, '2', '23232323', '2323');
INSERT INTO `artwork` VALUES ('1367773191933771778', '232322', 'on_auction', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1089874897,1268118658&fm=26&gp=0.jpg', '232', '232323', '2021-03-05 09:45:16', '2021-03-05 09:58:29', '3', '23232323', '2323');
INSERT INTO `artwork` VALUES ('1367773202192994305', '232322', 'finished', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1603365312,3218205429&fm=26&gp=0.jpg', '232', '232323', '2021-03-05 09:45:18', NULL, '4', '23232323', '2323');
COMMIT;

-- ----------------------------
-- Table structure for auction
-- ----------------------------
DROP TABLE IF EXISTS `auction`;
CREATE TABLE `auction` (
  `id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `art_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auction_round` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '第几轮拍卖',
  `start_time` datetime DEFAULT NULL COMMENT '拍卖开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '拍卖结束时间',
  `status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '竞拍已结束(finished)，竞拍进行中(happening)',
  `start_bid_price` decimal(10,2) DEFAULT NULL,
  `bid_cap_price` decimal(10,2) DEFAULT NULL,
  `bid_uesr_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bid_price` decimal(10,2) DEFAULT NULL COMMENT '出价价格',
  `bid_time` datetime DEFAULT NULL COMMENT '出价时间',
  `is_highest_bid` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '被超过(false)，当前最高价(true)',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='竞拍出价表';

-- ----------------------------
-- Records of auction
-- ----------------------------
BEGIN;
INSERT INTO `auction` VALUES ('3528323f50ed186b23af309c9361fe97', '1367773191933771778', '1', '2021-03-04 17:58:29', '2021-03-05 01:58:29', 'happening', 100.00, 120.00, '2343', 11.00, '2021-03-05 10:02:38', 'true', '2021-03-04 17:58:29', NULL);
INSERT INTO `auction` VALUES ('7fb83b1984c19c38ea3f74542ae3ed91', '1367773191933771778', '1', '2021-03-05 01:58:29', '2021-03-05 09:58:29', 'happening', 100.00, 120.00, '0', 0.00, NULL, 'false', '2021-03-05 01:58:29', NULL);
INSERT INTO `auction` VALUES ('924a4de7fc234488ba0bf7d5eb929dfc', '1367773191933771778', '1', '2021-03-04 17:58:29', '2021-03-05 01:58:29', 'happening', 100.00, 120.00, '2343', 10.00, '2021-03-05 02:01:18', 'false', '2021-03-04 17:58:29', NULL);
COMMIT;

-- ----------------------------
-- Table structure for trans_history
-- ----------------------------
DROP TABLE IF EXISTS `trans_history`;
CREATE TABLE `trans_history` (
  `id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `art_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `auction_round` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '第几轮拍卖',
  `seller_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '卖出者',
  `highest_bid_user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '出价人：0代表无人出价',
  `highest_bid_price` decimal(10,2) DEFAULT NULL COMMENT '最高出价：0代表无人出价',
  `payment_end_time` datetime DEFAULT NULL COMMENT '支付截止时间',
  `payment_status` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付状态：pending，failed，success',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of trans_history
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `matamask_address` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '以太坊地址',
  `profile_url` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像链接',
  `bio` varchar(4000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '个人描述',
  `email_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES ('123', '123123123', '3232', '323232', '32323', '23232', '2021-03-05 10:07:44', NULL);
INSERT INTO `user` VALUES ('1234', NULL, NULL, NULL, NULL, NULL, '2021-03-05 10:07:50', NULL);
INSERT INTO `user` VALUES ('12345', NULL, NULL, NULL, NULL, NULL, '2021-03-05 10:07:52', NULL);
INSERT INTO `user` VALUES ('123456', NULL, NULL, NULL, NULL, NULL, '2021-03-05 10:07:53', NULL);
INSERT INTO `user` VALUES ('1234567', NULL, NULL, NULL, NULL, NULL, '2021-03-05 10:07:55', NULL);
INSERT INTO `user` VALUES ('12345678', NULL, NULL, NULL, NULL, NULL, '2021-03-05 10:07:57', NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
