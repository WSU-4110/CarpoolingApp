-- MySQL dump 10.13  Distrib 8.0.18, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: warriors_on_wheels
-- ------------------------------------------------------
-- Server version	5.6.47

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS `warriors_on_wheels`;
USE `warriors_on_wheels`;

--
-- Table structure for table `driver`
--

DROP TABLE IF EXISTS `driver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `driver` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `car` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `passenger_id` (`user_id`),
  CONSTRAINT `driver_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `driver`
--

LOCK TABLES `driver` WRITE;
/*!40000 ALTER TABLE `driver` DISABLE KEYS */;
INSERT INTO `driver` VALUES (2,2,'2010 ford fusion'),(3,24,'2045 Chevy Corvette');
/*!40000 ALTER TABLE `driver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating`
--

DROP TABLE IF EXISTS `rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rating` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` float NOT NULL,
  `is_driver` tinyint(1) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating`
--

LOCK TABLES `rating` WRITE;
/*!40000 ALTER TABLE `rating` DISABLE KEYS */;
INSERT INTO `rating` VALUES (8,2,0,2),(9,2,0,2),(10,2,0,2),(11,2,0,2),(12,2,0,2),(13,2,0,2),(14,2,0,2),(15,2,0,2),(16,2,0,2),(17,2,0,2),(18,2,0,2),(19,2,0,2),(20,2,0,2),(21,2,0,2);
/*!40000 ALTER TABLE `rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ride`
--

DROP TABLE IF EXISTS `ride`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ride` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) NOT NULL,
  `time` datetime DEFAULT CURRENT_TIMESTAMP,
  `departure_location` varchar(255) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  `passenger_count` int(11) NOT NULL DEFAULT '3',
  `arrival_location` varchar(255) DEFAULT 'wayne',
  PRIMARY KEY (`id`),
  KEY `driver_id` (`driver_id`),
  CONSTRAINT `ride_ibfk_1` FOREIGN KEY (`driver_id`) REFERENCES `driver` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride`
--

LOCK TABLES `ride` WRITE;
/*!40000 ALTER TABLE `ride` DISABLE KEYS */;
INSERT INTO `ride` VALUES (10,2,'2020-04-20 12:00:00','troy',1,1,'wayne'),(11,2,'2020-05-20 00:00:00','troy',1,1,'wayne'),(12,2,'2020-05-20 00:00:00','troy',1,5,'wayne'),(19,2,'2020-04-15 08:00:00','13018 Brierstone Dr. Sterling Heights, MI 48312',1,4,'wayne'),(20,2,'2020-01-01 08:00:00','13018 Brierstone Dr. Sterling Heights, MI 48312',1,4,'wayne'),(21,2,'2020-06-20 08:00:00','13018 Brierstone Dr. Sterling Heights, MI 48312',1,4,'wayne'),(22,2,'2020-08-20 08:00:00','13018 Brierstone Dr. Sterling Heights, MI 48312',1,4,'wayne'),(23,2,'2020-08-20 08:00:00','13018 Brierstone Dr. Sterling Heights, MI 48312',1,4,NULL);
/*!40000 ALTER TABLE `ride` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ride_user_join`
--

DROP TABLE IF EXISTS `ride_user_join`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ride_user_join` (
  `ride_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ride_id`,`user_id`),
  KEY `passenger_id` (`user_id`),
  CONSTRAINT `ride_user_join_ibfk_1` FOREIGN KEY (`ride_id`) REFERENCES `ride` (`id`),
  CONSTRAINT `ride_user_join_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ride_user_join`
--

LOCK TABLES `ride_user_join` WRITE;
/*!40000 ALTER TABLE `ride_user_join` DISABLE KEYS */;
INSERT INTO `ride_user_join` VALUES (10,8,1);
/*!40000 ALTER TABLE `ride_user_join` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(11) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `access_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `access_id` (`access_id`),
  UNIQUE KEY `access_id_2` (`access_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2,'darpan','1412122234','vegas baby','ab1234'),(7,'wayne','911','atlantis','cd1111'),(8,'alyssa','1112222233','nyc','aa5555'),(21,'Evan is Jesus',NULL,'Detroit Heights','cj5100'),(22,'Evan de Jesus',NULL,'bernietown','cj5101'),(23,'Batman','3138794561','Gotham','bm0313'),(24,'Darpan Shah','5864197630','Sterling Heights','gg2002');
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

-- Dump completed on 2020-03-23 20:09:27
