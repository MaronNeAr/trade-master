-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: localhost    Database: trade_transaction
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
-- Table structure for table `transaction_task`
--

DROP TABLE IF EXISTS `transaction_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_task` (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) DEFAULT NULL,
  `security_code` varchar(45) DEFAULT NULL,
  `transaction_date` timestamp NULL DEFAULT NULL,
  `transaction_type` varchar(45) DEFAULT NULL,
  `transaction_price` decimal(10,2) DEFAULT NULL,
  `transaction_volume` int DEFAULT NULL,
  `transaction_status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `task_id_UNIQUE` (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_task`
--

LOCK TABLES `transaction_task` WRITE;
/*!40000 ALTER TABLE `transaction_task` DISABLE KEYS */;
INSERT INTO `transaction_task` VALUES (1,'1','sh600000','2024-01-27 03:05:28','buy',6.90,100,'filled'),(2,'1','sh600004','2024-01-28 04:09:05','buy',10.05,100,'filled'),(3,'1','sh600004','2024-01-28 04:12:38','sell',9.73,100,'cancelled'),(4,'1','sh600004','2024-01-28 04:24:03','sell',9.73,100,'cancelled'),(5,'1','sh600000','2024-02-18 08:35:41','buy',6.90,500,'cancelled'),(6,'1','sh600004','2024-02-18 08:36:07','buy',10.36,500,'filled'),(7,'1','sh600006','2024-02-18 08:36:37','buy',5.55,500,'filled'),(8,'1','sh600007','2024-02-18 08:36:44','buy',19.13,500,'filled'),(9,'1','sh600008','2024-02-18 08:36:53','buy',2.89,500,'filled'),(10,'1','sh600009','2024-02-18 08:37:04','buy',35.19,200,'filled'),(11,'1','sh600010','2024-02-18 08:37:12','buy',1.52,200,'filled'),(12,'1','sh600011','2024-02-18 08:37:21','buy',8.50,200,'cancelled'),(13,'1','sh600012','2024-02-18 08:37:27','buy',13.18,200,'filled'),(14,'1','sh600015','2024-02-18 08:37:36','buy',5.95,200,'cancelled'),(15,'1','sh600016','2024-02-18 08:37:45','buy',3.91,200,'cancelled'),(16,'1','sh600017','2024-02-18 08:37:53','buy',2.75,100,'filled'),(17,'1','sh600018','2024-02-18 08:38:02','buy',5.27,100,'cancelled'),(18,'1','sh600019','2024-02-18 08:38:11','buy',6.24,100,'cancelled'),(19,'1','sh600020','2024-02-18 08:38:17','buy',3.38,100,'filled'),(20,'1','sh600021','2024-02-18 08:38:23','buy',8.40,100,'filled'),(21,'1','sh600022','2024-02-18 08:38:29','buy',1.44,100,'filled'),(22,'1','sh600023','2024-02-18 08:38:35','buy',5.29,100,'active'),(23,'1','sh600025','2024-02-18 08:38:43','buy',8.83,100,'cancelled'),(24,'1','sh600026','2024-02-18 08:38:51','buy',12.91,100,'filled'),(25,'1','sh600027','2024-02-18 08:38:57','buy',5.86,100,'cancelled'),(26,'1','sh600028','2024-02-18 08:39:04','buy',5.98,100,'cancelled'),(27,'1','sh600029','2024-02-18 08:39:09','buy',5.72,100,'active'),(28,'1','sh600030','2024-02-18 08:39:15','buy',21.31,100,'filled'),(30,'1','sh600000','2024-02-18 09:05:16','buy',6.96,200,'filled'),(32,'1','sh600004','2024-02-18 11:12:02','buy',10.20,100,'filled'),(33,'1','sh600004','2024-02-18 12:04:08','buy',10.20,100,'filled'),(34,'1','sh600000','2024-02-19 06:20:00','sell',7.11,100,'filled'),(35,'1','sh600000','2024-02-19 06:31:25','sell',7.12,100,'filled'),(36,'1','sh600007','2024-02-19 06:38:29','sell',18.88,100,'filled'),(37,'1','sh600006','2024-02-19 06:41:24','sell',5.57,100,'filled'),(38,'1','sh600004','2024-02-19 06:43:12','buy',10.06,100,'filled'),(39,'1','sh600000','2024-02-19 07:34:38','sell',7.12,100,'active'),(40,'1','sh600004','2024-02-19 07:34:45','sell',10.11,100,'active'),(41,'1','sh600006','2024-02-19 07:34:53','sell',5.58,100,'active'),(42,'1','sh600004','2024-02-19 08:23:59','buy',10.11,100,'cancelled'),(43,'1','sh600004','2024-02-19 08:24:07','buy',10.11,100,'active'),(44,'1','sh600006','2024-02-19 08:24:47','buy',5.58,100,'active'),(45,'1','sh600007','2024-02-19 08:24:59','buy',18.96,100,'active'),(46,'1','sh600006','2024-02-19 07:34:53','sell',5.58,100,'active'),(47,'1','sh600006','2024-02-19 07:34:53','sell',5.58,100,'active'),(48,'1','sh600679','2024-02-19 08:27:05','buy',8.94,100,'active');
/*!40000 ALTER TABLE `transaction_task` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-02-24 16:21:27
