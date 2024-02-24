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
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_role` (
  `id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_xc_user_role_user_id` (`user_id`) USING BTREE,
  KEY `fk_xc_user_role_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES ('1','46','8','2017-09-11 13:02:45','超级管理员'),('143e0b1e-3de4-47e7-9db1-1390763f6bec','1','8','2024-02-10 14:26:04','超级管理员'),('19','50','6','2017-09-12 14:20:20','超级管理员'),('2','48','6','2017-09-11 13:02:56','超级管理员'),('20','50','17','2017-09-12 14:20:20','超级管理员'),('2034706c-7d15-43fb-8b0d-bd581c7ace02','e63fb882-3226-4365-9711-d3fd467a4813','17',NULL,NULL),('21','52','20',NULL,NULL),('3','49','20','2017-09-11 13:03:12',NULL),('48d759c1-a597-4220-8ddf-91696638d91d','17b9d8be-cef3-400c-bec5-1478a6ef7ddc','17',NULL,NULL),('88f646ec-7257-4987-816e-92adfb68e0c1','e47ade0d-377d-4e58-99fc-6d5e2af5c148','17',NULL,NULL),('b5dcefda-d942-407b-ac4d-fc9c4b3c1b97','b5dcefda-d942-407b-ac4d-fc9c4b3c1b97','17','2023-03-12 22:52:33',NULL),('cab93878-c357-4d40-ac07-79d19b8f7232','cab93878-c357-4d40-ac07-79d19b8f7232','17','2023-03-12 23:08:14',NULL),('dbcd12d9-2231-4a8b-ad10-6a73b326c334','dbcd12d9-2231-4a8b-ad10-6a73b326c334','17','2023-03-14 18:39:00',NULL);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-24 16:23:36
