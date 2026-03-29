-- MySQL dump 10.13  Distrib 9.6.0, for macos26.3 (arm64)
--
-- Host: localhost    Database: myadvicedatabase
-- ------------------------------------------------------
-- Server version	9.6.0

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

--
-- Table structure for table `appointment`
--

DROP TABLE IF EXISTS `appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appointment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `faculty_id` bigint NOT NULL,
  `date_time` datetime NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `student_id` (`student_id`),
  KEY `faculty_id` (`faculty_id`),
  CONSTRAINT `fk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `fk_2` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointment`
--

LOCK TABLES `appointment` WRITE;
/*!40000 ALTER TABLE `appointment` DISABLE KEYS */;
INSERT INTO `appointment` VALUES (2,2,2,'2026-04-02 14:00:00','CONFIRMED'),(3,3,2,'2026-04-03 09:00:00','CONFIRMED'),(5,5,1,'2026-04-05 14:00:00','CONFIRMED'),(6,6,4,'2026-04-07 10:00:00','PENDING'),(7,7,2,'2026-04-08 15:00:00','CONFIRMED'),(8,3,3,'2026-04-09 09:30:00','PENDING'),(16,1,1,'2026-04-10 10:00:00','scheduled'),(17,4,4,'2026-03-30 22:00:00','scheduled'),(20,7,4,'2026-03-30 10:00:00','scheduled');
/*!40000 ALTER TABLE `appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `course_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `credits` int NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `course_code` (`course_code`),
  UNIQUE KEY `course_name` (`course_name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course`
--

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES (1,'COMP1000','Intro to CS',3,'Introduction to computer science fundamentals'),(2,'COMP1047','Computer Concepts',3,'Basic computer systems and applications'),(3,'COMP2120','Object Oriented Programming',3,'Introduction to programming in Java'),(4,'COMP2800','Software Development',3,'Software development principles and practices'),(12,'COMP1400','Introduction to Algorithms and Prog',3,'Introduction to systems programming in C'),(13,'COMP3220','Object Oriented Design',3,'OOD principles and design patterns'),(14,'COMP3540','Theory of Computation',3,'Automata, formal languages, computability'),(15,'COMP3670','Computer Networks',3,'Network protocols and architecture'),(16,'COMP4400','Principles of Programming Languages',3,'Language design and implementation'),(17,'COMP4960','Research Project',3,'Independent research under faculty supervision'),(18,'Comp 2310','Theoritical Foundation of Computer Science',3,'Introduction to the theories of computer science');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `course_prerequisites`
--

DROP TABLE IF EXISTS `course_prerequisites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_prerequisites` (
  `course_id` bigint NOT NULL,
  `prerequisite_id` bigint NOT NULL,
  KEY `FK5n69pkr3cv08biukt7qhi4dcf` (`prerequisite_id`),
  KEY `FKemh7nrkoatej939evaclhippn` (`course_id`),
  CONSTRAINT `FK5n69pkr3cv08biukt7qhi4dcf` FOREIGN KEY (`prerequisite_id`) REFERENCES `course` (`id`),
  CONSTRAINT `FKemh7nrkoatej939evaclhippn` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_prerequisites`
--

LOCK TABLES `course_prerequisites` WRITE;
/*!40000 ALTER TABLE `course_prerequisites` DISABLE KEYS */;
INSERT INTO `course_prerequisites` VALUES (3,2);
/*!40000 ALTER TABLE `course_prerequisites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrollment`
--

DROP TABLE IF EXISTS `enrollment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `lab_day_of_week` varchar(255) DEFAULT NULL,
  `lab_time` varchar(255) DEFAULT NULL,
  `course_id` bigint NOT NULL,
  `section_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbhhcqkw1px6yljqg92m0sh2gt` (`course_id`),
  KEY `FKamt2st6hpbqje7p6jwqd6cm5e` (`section_id`),
  KEY `FKio7fsy3vhvfgv7c0gjk15nyk4` (`student_id`),
  CONSTRAINT `FKamt2st6hpbqje7p6jwqd6cm5e` FOREIGN KEY (`section_id`) REFERENCES `section` (`id`),
  CONSTRAINT `FKbhhcqkw1px6yljqg92m0sh2gt` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `FKio7fsy3vhvfgv7c0gjk15nyk4` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrollment`
--

LOCK TABLES `enrollment` WRITE;
/*!40000 ALTER TABLE `enrollment` DISABLE KEYS */;
INSERT INTO `enrollment` VALUES (2,'Tuesday','10:00-11:00',3,3,7),(5,NULL,NULL,12,4,7),(8,'Wednesday','10:00-11:00',12,12,4),(9,NULL,NULL,1,11,4);
/*!40000 ALTER TABLE `enrollment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `faculty`
--

DROP TABLE IF EXISTS `faculty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculty` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `department` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faculty`
--

LOCK TABLES `faculty` WRITE;
/*!40000 ALTER TABLE `faculty` DISABLE KEYS */;
INSERT INTO `faculty` VALUES (1,'Saeed','Samet','sametS67@uwindsor.ca','Faculty of Science'),(2,'Robin','Gras','grasR29@uwindsor.ca','Faculty of Science'),(3,'Andreas','Maniatis','Andreas.Maniatis@uwindsor.ca','Faculty of Science'),(4,'Imarn','Ahmad','ahmadI@uwindsor.ca','Faculty of Science'),(5,'Ziad','Kobti','zkobti@uwindsor.ca','Faculty of Science'),(6,'Ahmad','Biniaz','biniazA@uwindsor.ca','Faculty of Science'),(7,'Dan','Wu','danw123@uwindsor.ca','Faculty of Science'),(8,'Luis','Rueda','ruedaL69@uwindsor.ca','Faculty of Science'),(9,'Ikjot','Saini','sainiI12@uwindsor.ca','Faculty of Science');
/*!40000 ALTER TABLE `faculty` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prerequisite`
--

DROP TABLE IF EXISTS `prerequisite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prerequisite` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `prerequisite_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_prereq_course` (`course_id`),
  KEY `fk_prereq_required` (`prerequisite_id`),
  CONSTRAINT `fk_prereq_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_prereq_required` FOREIGN KEY (`prerequisite_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prerequisite`
--

LOCK TABLES `prerequisite` WRITE;
/*!40000 ALTER TABLE `prerequisite` DISABLE KEYS */;
INSERT INTO `prerequisite` VALUES (1,12,1),(2,3,12),(3,4,12),(4,13,3),(5,14,12),(6,15,4),(7,16,13),(8,17,13),(9,17,14);
/*!40000 ALTER TABLE `prerequisite` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program`
--

DROP TABLE IF EXISTS `program`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `program` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `department` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `total_credits` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program`
--

LOCK TABLES `program` WRITE;
/*!40000 ALTER TABLE `program` DISABLE KEYS */;
/*!40000 ALTER TABLE `program` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `day_of_week` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `room_number` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `term` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `fk_3` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (1,1,'Monday','09:00:00','10:30:00','ERB 1024','Winter 2026'),(2,2,'Wednesday','13:00:00','14:30:00','LT 1','Winter 2026'),(3,3,'Friday','10:00:00','11:30:00','CHN 104','Winter 2026'),(4,12,'Tuesday','10:00:00','11:30:00','ERB 1045','Winter 2026'),(5,13,'Thursday','14:00:00','15:30:00','LT 2','Winter 2026'),(6,14,'Monday','13:00:00','14:30:00','CHN 201','Winter 2026'),(7,15,'Wednesday','09:00:00','10:30:00','ERB 1024','Winter 2026'),(8,16,'Friday','11:00:00','12:30:00','LT 1','Winter 2026');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `section`
--

DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `section` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `faculty_id` bigint NOT NULL,
  `section_number` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `capacity` int NOT NULL,
  `enrolled_count` int NOT NULL,
  `day_of_week` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `instructor_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lab_day_of_week` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lab_time` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `room` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`),
  KEY `faculty_id` (`faculty_id`),
  CONSTRAINT `fk_4` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `fk_5` FOREIGN KEY (`faculty_id`) REFERENCES `faculty` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `section`
--

LOCK TABLES `section` WRITE;
/*!40000 ALTER TABLE `section` DISABLE KEYS */;
INSERT INTO `section` VALUES (1,1,3,'01',30,25,'Monday','Dr. Maniatis',NULL,NULL,NULL),(2,2,1,'02',30,20,'Wednesday','Dr. Garabon',NULL,NULL,NULL),(3,3,2,'01',30,16,'Friday','Dr. Guarini','Tuesday','10:00-11:00',NULL),(4,12,4,'01',35,31,'Tuesday','Dr. Ahmad',NULL,NULL,NULL),(5,13,5,'01',30,25,'Thursday','Dr. Kobti',NULL,NULL,NULL),(6,14,3,'02',40,35,'Monday','Dr. Maniatis',NULL,NULL,NULL),(7,15,7,'01',30,20,'Wednesday','Dr. Wu',NULL,NULL,NULL),(8,16,9,'01',25,22,'Friday','Dr. Saini',NULL,NULL,NULL),(9,4,3,'12',25,1,'Wednesday','Andreas Maniatis','Tuesday','10:00-11:00','LT 102'),(10,1,1,'2',23,0,'Wednesday','Dr. Saeed','Tuesday','10:00-11:00','Lt 102'),(11,1,4,'5',50,24,'Monday','Dr. Ahmad','Friday','10:00-11:00','LT 102'),(12,12,4,'2',50,24,'Monday','Dr. Ahmad','Wednesday','10:00-11:00','LT 102');
/*!40000 ALTER TABLE `section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `student_number` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `faculty_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `program_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `student_number` (`student_number`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student`
--

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES (1,'Andy','Li','liA1289@uwindsor.ca','110792368','Faculty of Science','Computer Science Honors'),(2,'Omar','Khan','khanO45@uwindsor.ca','110556789','Faculty of Science','Computer Science Honors'),(3,'James','Wilson','wilsonj@uwindsor.ca','110182213','Faculty of Science','Computer Science Honors'),(4,'Ali Al Fattouhi','Al Jundi','jundiA12@uwindsor.ca','110892388','Faculty of Science','Computer Science with Applied Computing'),(5,'Ife','Adegbite','adegbiteI02@uwindsor.ca','110673127','Faculty of Science','Software Engineering'),(6,'Hady Darwiche','Figueredo','figueredoH@uwindsor.ca','110784299','Faculty of Science','Software Engineering'),(7,'Sadat','Tanzim','tanzims@uwindsor.ca','110168812','Faculty of Science','Computer Science Honors'),(8,'Tega','Otubu','otubuT23@uwindsor.ca','110989677','Faculty of Science','Computer Science General');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transcript`
--

DROP TABLE IF EXISTS `transcript`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transcript` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL,
  `course_id` bigint NOT NULL,
  `grade` double NOT NULL,
  `term` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `student_id` (`student_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `fk_6` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `fk_7` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transcript`
--

LOCK TABLES `transcript` WRITE;
/*!40000 ALTER TABLE `transcript` DISABLE KEYS */;
INSERT INTO `transcript` VALUES (1,1,1,85,'Fall 2024'),(2,1,2,78,'Fall 2024'),(3,1,3,91,'Winter 2025'),(4,2,1,72,'Fall 2024'),(5,2,2,88,'Winter 2025'),(6,3,1,95,'Fall 2024'),(7,3,3,82,'Winter 2025'),(8,4,2,76,'Fall 2024'),(9,5,1,89,'Winter 2025'),(10,1,1,85,'Fall 2024'),(11,1,3,78,'Fall 2024'),(12,1,12,91,'Winter 2025'),(13,2,1,72,'Fall 2024'),(14,2,3,88,'Winter 2025'),(15,3,1,95,'Fall 2024'),(16,3,12,82,'Winter 2025'),(17,4,3,76,'Fall 2024'),(18,5,1,89,'Winter 2025');
/*!40000 ALTER TABLE `transcript` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-29 18:52:14
