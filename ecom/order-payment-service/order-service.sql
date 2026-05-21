-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: order_service
-- ------------------------------------------------------
-- Server version	8.0.45

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
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `order_id` char(36) DEFAULT NULL,
  `product_id` varchar(10) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES ('7e592dbb-19fc-4944-bd8d-070daf0a7a09','94f9fb1b-0a58-4eea-91a9-05fc1d791788','p04','iPhone 17',1000.00,3),('a7198ed8-461d-4d50-9713-6b17e8de0e26','6a4594a5-a3b1-4ed8-829e-664fca8c419f','p02','AirPods Pro',250.00,2),('bfb675f5-ab5b-4310-af89-80132e851f10','6a4594a5-a3b1-4ed8-829e-664fca8c419f','p01','iPhone 15',1000.00,1),('c4d28d4a-450b-4abd-9f40-9659b7b4ed2a','029ce983-bf97-46e9-b27f-267f09582f99','p07','iPhone 25',1000.00,3),('item_001','order_001','p1','Keyboard',100.00,2),('item_002','order_001','p2','Mouse',50.00,1);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `user_id` char(36) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `address_line` varchar(255) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `orders_chk_1` CHECK ((`status` in (_utf8mb4'PENDING',_utf8mb4'PAID',_utf8mb4'CANCELLED')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES ('029ce983-bf97-46e9-b27f-267f09582f99','user010','0909000000','PENDING',3000.00,'123 ABC','HCM','Vietnam','2026-04-27 15:46:43'),('6a4594a5-a3b1-4ed8-829e-664fca8c419f','user002','0988777669',NULL,1500.00,'456 Le Loi Updated','Da Nang','Vietnam','2026-04-21 22:39:00'),('94f9fb1b-0a58-4eea-91a9-05fc1d791788','user006','0909598789','PENDING',3000.00,'12356 Nguyen Hue Vien Posted','HCM1','Vietnam1','2026-04-26 21:39:19'),('order_001','user001','0909123456','PENDING',250.00,'123 Nguyen Trai','HCM','Vietnam','2026-04-21 22:03:48');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `order_id` char(36) DEFAULT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `paid_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `payments_chk_1` CHECK ((`status` in (_utf8mb4'SUCCESS',_utf8mb4'FAILED')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES ('27a3d155-8b9d-4c11-971d-da1773ea6647','029ce983-bf97-46e9-b27f-267f09582f99','COD','SUCCESS','2026-04-27 15:46:44'),('410116aa-420c-11f1-832e-74d4dd06a867','6a4594a5-a3b1-4ed8-829e-664fca8c419f','BANK','FAILED','2026-04-27 14:39:42'),('4101178d-420c-11f1-832e-74d4dd06a867','6a4594a5-a3b1-4ed8-829e-664fca8c419f','MOMO','SUCCESS','2026-04-27 14:39:42'),('9b93e646-2fd0-4dac-98a3-de2fcd8a8bb0','94f9fb1b-0a58-4eea-91a9-05fc1d791788','COD','SUCCESS','2026-04-26 21:39:20'),('ae4ab585-da78-487a-aeaf-5e2b76a45c2b','94f9fb1b-0a58-4eea-91a9-05fc1d791788','COD','SUCCESS','2026-04-27 16:17:53'),('e6ce74bc-afa5-4920-82ea-30971911b45f','6a4594a5-a3b1-4ed8-829e-664fca8c419f','COD','SUCCESS','2026-04-21 22:39:00');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-27 16:34:39
