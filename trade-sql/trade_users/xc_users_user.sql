-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: xc_users
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `wx_unionid` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信unionid',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `userpic` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `company_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `utype` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `birthday` datetime DEFAULT NULL,
  `sex` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `cellphone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `qq` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户状态',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_user_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1','marlon1475','$2a$10$x5kFqlXUe4IDIZLqMaDTAeU4F17kOjmNfRw.8Q11W6gYSavD3/PQW',NULL,NULL,'Aurora_NeAr','阿巴阿巴',NULL,NULL,'101001',NULL,NULL,'marlon1475@foxmail.com','13299531475',NULL,'1','2024-02-10 14:26:04',NULL),('17b9d8be-cef3-400c-bec5-1478a6ef7ddc','o5PB1sytMXJGGv2x5OBP2uMzCpS8','o5PB1sytMXJGGv2x5OBP2uMzCpS8',NULL,'o5PB1sytMXJGGv2x5OBP2uMzCpS8','Mr.M','Mr.M','https://thirdwx.qlogo.cn/mmopen/vi_32/P8h3wxWDqCqfI0BmHxAYHEzppsaBp4xt7nscyiaQ2ZptEhzyKcEia7loT6pO6zExaM9FR7BnSguxqKYuIe5B3aEA/132',NULL,'101001',NULL,NULL,NULL,NULL,NULL,'1','2022-09-30 20:08:51',NULL),('46','super','$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe',NULL,NULL,NULL,'超级管理员',NULL,NULL,'101003',NULL,'1',NULL,NULL,NULL,'1','2022-09-28 08:32:03',NULL),('48','admin','$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe',NULL,NULL,NULL,'系统管理员',NULL,NULL,'101003',NULL,'1',NULL,NULL,NULL,'1','2022-09-28 08:32:03',NULL),('49','itcast','$2a$10$TJ4TmCdK.X4wv/tCqHW14.w70U3CC33CeVncD3SLmyMXMknstqKRe',NULL,NULL,NULL,'test02',NULL,NULL,'101002',NULL,'1','','12345',NULL,'1','2022-09-28 08:32:03',NULL),('50','stu1','$2a$10$0pt7WlfTbnPDTcWtp/.2Mu5CTXvohnNQhR628qq4RoKSc0dGAdEgm',NULL,NULL,'大水牛','学生1','http://file.xuecheng-plus.com/dddf',NULL,'101001','2022-09-28 19:28:46','1',NULL,NULL,NULL,'1','2022-09-28 08:32:03',NULL),('51','Lucy','$2a$10$lb1lY/J2hPYuIWeEkeDMm.dSvcdI4D2raikdlcxZNwRy4NHSRznEO',NULL,NULL,NULL,'学生2',NULL,NULL,'101001',NULL,'1',NULL,NULL,NULL,'1','2022-09-28 08:32:03',NULL),('52','Kyle','$2a$10$lb1lY/J2hPYuIWeEkeDMm.dSvcdI4D2raikdlcxZNwRy4NHSRznEO',NULL,NULL,NULL,'Kiki',NULL,'1232141425','101002',NULL,'1','6385296@qq.com',NULL,NULL,'','2022-09-28 08:32:03',NULL),('b5dcefda-d942-407b-ac4d-fc9c4b3c1b97','oWgGz1FO1I4wzgBGTMP6BOvdk1EY','oWgGz1FO1I4wzgBGTMP6BOvdk1EY',NULL,'oWgGz1FO1I4wzgBGTMP6BOvdk1EY','时崎狂三','时崎狂三','https://thirdwx.qlogo.cn/mmopen/vi_32/PiajxSqBRaEJbiaadgEJpDPx2GenPaUDngyYoyvibJDlEibsOeiauGr4kftXaSrssLmicgEhmQjdjQEhZHWoEib6WAQsg/132',NULL,'101001',NULL,NULL,NULL,NULL,NULL,'1','2023-03-12 22:52:33',NULL),('cab93878-c357-4d40-ac07-79d19b8f7232','oWgGz1L5soWx0gK7xemSMDQEU8ps','oWgGz1L5soWx0gK7xemSMDQEU8ps',NULL,'oWgGz1L5soWx0gK7xemSMDQEU8ps','ㅤ','ㅤ','https://thirdwx.qlogo.cn/mmopen/vi_32/uVpKJX5jQEicOHDibRtmzTILSCdmU6sWcLMD0DYlZylvibpRfZSSZm1ASDEqxeCHFM6ZWdZ3TkY1WsW8rmRjKIibBA/132',NULL,'101001',NULL,NULL,NULL,NULL,NULL,'1','2023-03-12 23:08:14',NULL),('dbcd12d9-2231-4a8b-ad10-6a73b326c334','123132','$2a$10$O43pyUc3ytdmHo68BdxjluDw2IlrTB0O2X50Dh0a6h/xpu5yerUCO',NULL,NULL,'123132','123132',NULL,NULL,'101001',NULL,NULL,'1586385296@qq.com','1586385296@qq.com',NULL,'1','2023-03-14 18:39:00',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-24 16:23:38
