-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: educationalmanagementsystem
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `student_course`
--

DROP TABLE IF EXISTS `student_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_course` (
  `sid` int DEFAULT NULL,
  `ccode` int DEFAULT NULL,
  `yearmark` int DEFAULT '-1',
  `midmark` int DEFAULT '-1',
  `finalmark` int DEFAULT '-1',
  `bonus` int DEFAULT '0',
  `totalmark` int DEFAULT '-1',
  KEY `student_course_student` (`sid`),
  KEY `student_course_course` (`ccode`),
  CONSTRAINT `student_course_course` FOREIGN KEY (`ccode`) REFERENCES `course` (`code`),
  CONSTRAINT `student_course_student` FOREIGN KEY (`sid`) REFERENCES `student` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_course`
--

LOCK TABLES `student_course` WRITE;
/*!40000 ALTER TABLE `student_course` DISABLE KEYS */;
INSERT INTO `student_course` VALUES (9,8,-1,-1,-1,2,-1),(10,8,-1,-1,-1,2,-1),(12,8,-1,-1,-1,2,-1),(28,8,-1,-1,-1,2,-1),(29,6,-1,-1,-1,-1,-1),(28,6,-1,-1,-1,-1,-1),(27,6,-1,-1,-1,-1,-1),(26,6,-1,-1,-1,-1,-1),(25,6,-1,-1,-1,-1,-1),(24,12,-1,-1,-1,-1,-1),(23,12,-1,-1,-1,-1,-1),(22,12,-1,-1,-1,-1,-1),(25,12,-1,-1,-1,-1,-1),(21,12,-1,-1,-1,-1,-1),(13,9,-1,-1,-1,-1,-1),(14,9,-1,-1,-1,-1,-1),(15,9,-1,-1,-1,-1,-1),(16,9,-1,-1,-1,-1,-1),(17,9,-1,-1,-1,-1,-1),(19,8,-1,-1,-1,2,-1),(11,14,-1,-1,-1,-1,-1),(12,14,-1,-1,-1,-1,-1),(13,14,-1,-1,-1,-1,-1),(14,14,-1,-1,-1,-1,-1),(17,14,-1,-1,-1,-1,-1),(18,14,-1,-1,-1,-1,-1),(20,15,-1,-1,-1,-1,-1),(19,15,-1,-1,-1,-1,-1),(18,15,-1,-1,-1,-1,-1),(21,15,-1,-1,-1,-1,-1),(22,15,-1,-1,-1,-1,-1),(18,6,-1,-1,-1,0,-1),(18,7,-1,-1,-1,0,-1),(18,12,-1,-1,-1,0,-1),(18,13,-1,-1,-1,0,-1),(18,9,-1,-1,-1,0,-1);
/*!40000 ALTER TABLE `student_course` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-30 19:35:20
