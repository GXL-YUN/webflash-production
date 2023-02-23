/*
 Navicat Premium Data Transfer

 Source Server         : myLocalhost
 Source Server Type    : MySQL
 Source Server Version : 50537
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50537
 File Encoding         : 65001

 Date: 28/08/2021 11:05:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ut_tst_user
-- ----------------------------
DROP TABLE IF EXISTS `ut_tst_user`;
CREATE TABLE `ut_tst_user`  (
  `id` int(11) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hobbies` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `appendix` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of ut_tst_user
-- ----------------------------
INSERT INTO `ut_tst_user` VALUES (1, '张三', '男', 23, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (2, '李四', '男', 23, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (3, '王一', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (4, '王二', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (5, '王三', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (6, '王四', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (7, '王五', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (8, '王六', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (9, '王七', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (10, '王八', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (11, '王九', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (12, '王十', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (13, '赵五', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (14, '钱四', '女', 23, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (15, '张一', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (16, '张二', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (17, '张三', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (18, '孙四', '男', 23, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (19, '里一', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (20, '李二', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (21, '李三', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (22, '李四', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (23, '李五', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (24, '李六', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (25, '李七', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (26, '李八', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (27, '王李九', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (28, '李十', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (29, '周四', '男', 23, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (30, '周一', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (31, '周二', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (32, '周王三', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (33, '武四', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (34, '周五', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (35, '周六', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (36, '王七', '女', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (37, '王八', '男', 24, '开发', NULL, NULL, NULL);
INSERT INTO `ut_tst_user` VALUES (38, '王九', '男', 24, '开发', NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
