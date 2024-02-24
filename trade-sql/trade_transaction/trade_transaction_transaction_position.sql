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
-- Table structure for table `transaction_position`
--

DROP TABLE IF EXISTS `transaction_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_position` (
  `position_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(64) DEFAULT NULL,
  `security_code` varchar(45) DEFAULT NULL,
  `position_quantity` int DEFAULT NULL,
  `position_cost` decimal(10,2) DEFAULT NULL,
  `position_status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`position_id`),
  UNIQUE KEY `position_id_UNIQUE` (`position_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_position`
--

LOCK TABLES `transaction_position` WRITE;
/*!40000 ALTER TABLE `transaction_position` DISABLE KEYS */;
INSERT INTO `transaction_position` VALUES (1,'1','sh600000',0,0.00,'unactive'),(2,'1','sh600004',700,7197.75,'active'),(3,'1','sh600007',400,7652.00,'active'),(4,'1','sh600008',500,1445.00,'active'),(5,'1','sh600010',200,304.00,'active'),(6,'1','sh600012',200,2636.00,'active'),(7,'1','sh600017',100,275.00,'active'),(8,'1','sh600021',100,840.00,'active'),(9,'1','sh600022',100,144.00,'active'),(10,'1','sh600030',100,2131.00,'active'),(11,'1','sh600009',200,7038.00,'active'),(12,'1','sh600006',500,2497.50,'active'),(13,'1','sh600020',100,338.00,'active'),(14,'1','sh600026',100,1291.00,'active');
/*!40000 ALTER TABLE `transaction_position` ENABLE KEYS */;
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
