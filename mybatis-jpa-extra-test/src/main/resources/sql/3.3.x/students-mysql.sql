-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: test
-- ------------------------------------------------------
-- Server version	8.4.0

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
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `STDNO` varchar(100) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL,
  `STDNAME` varchar(100) DEFAULT NULL,
  `STDGENDER` varchar(100) DEFAULT NULL,
  `STDAGE` int DEFAULT NULL,
  `STDMAJOR` varchar(100) DEFAULT NULL,
  `STDCLASS` varchar(100) DEFAULT NULL,
  `ID` varchar(100) NOT NULL,
  `images` blob,
  `status` int DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `deleted` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('10005',NULL,'黄忠','M',68,'化学','1','05084723-77a8-425a-870e-e33d00e53fd2',NULL,NULL,NULL,'n'),('10024','{aesec}9bec20a5e9895fcc37ef43c56187730e','司马昭','M',20,'政治','4','1004539842878504960',NULL,NULL,NULL,'n'),('10024','{aesec}9bec20a5e9895fcc37ef43c56187730e','司马昭','M',20,'政治','4','1007058902925180928',_binary 'ssss',NULL,'2024-07-06 20:29:11','n'),('10024','{aesec}9bec20a5e9895fcc37ef43c56187730e','司马昭','M',20,'政治','4','1007062907189460992',NULL,NULL,NULL,'n'),('10024',NULL,'司马昭','M',20,'政治','4','1007302109701341184',NULL,NULL,NULL,'n'),('10024','{aesec}5284216d85ea34f45a225595a24c8e7a','司马昭','M',20,'政治','4','1007302206942085120',NULL,NULL,NULL,'n'),('10003',NULL,'张飞','M',38,'政治','1','1297510b-5f47-4425-b1cf-778425254142',NULL,NULL,NULL,'n'),('10014',NULL,'陆逊','M',18,'数学','2','2120f7a5-00d9-44ff-92ee-ae1e2bc212e2',NULL,NULL,NULL,'n'),('10022',NULL,'孙尚香','f',26,'历史','4','22f2914c-37bc-4e05-b0da-3d02f682008a',NULL,NULL,NULL,'n'),('10016',NULL,'典韦','M',28,'历史','3','293745a9-c33e-47d2-9844-8668574d3b60',NULL,NULL,NULL,'n'),('10019',NULL,'邓艾','M',17,'军事','3','317d5eda-927c-4871-a916-472a8062df23',_binary 'ssss',NULL,'2023-07-16 17:28:47','n'),('10006',NULL,'魏延','M',35,'化学','1','37e4b3b4-3c76-4bf3-8165-f514d14ff54f',NULL,NULL,NULL,'n'),('10020',NULL,'大乔','F',25,'艺术','4','3edd3ce2-1b13-46c9-8f39-37522052f2cc',NULL,NULL,NULL,'n'),('10013',NULL,'孙权','M',20,'政治','2','3f1e0276-8104-44a0-9231-2759777b08ee',NULL,NULL,NULL,'n'),('10015',NULL,'曹操','M',46,'政治','3','715c53ac-c85d-4049-9ef9-abf4e3d91d15',NULL,NULL,NULL,'n'),('10011',NULL,'鲁肃','M',34,'物理','2','71aaa4dd-9720-4f76-a825-357842de3c88',NULL,NULL,NULL,'n'),('10018',NULL,'司马懿','M',38,'政治','3','8793f911-fc0a-49fa-9ca7-2ab4e6a19454',NULL,NULL,NULL,'n'),('10001',NULL,'刘备','M',40,'政治','1','8c34448b-c65b-4a4e-a0da-83284d05f909',NULL,NULL,NULL,'n'),('10004',NULL,'赵云','M',29,'历史','1','940908e2-7c58-429a-bcef-8b566befed00',NULL,NULL,NULL,'n'),('10023',NULL,'貂蝉','F',20,'艺术','4','af04d610-6092-481e-9558-30bd63ef783c',NULL,NULL,NULL,'n'),('10002',NULL,'关羽','M',37,'物理','1','b9111f83-d338-461d-8d46-f331087d5a42',NULL,NULL,NULL,'n'),('10010',NULL,'周瑜','M',39,'数学','2','c38f8afd-a6bc-458d-9f9a-fb22f30c7e39',NULL,NULL,NULL,'n'),('10008',NULL,'姜维','M',20,'政治','1','d252f326-c0ac-46cb-82fc-cb2597edaf41',NULL,NULL,NULL,'n'),('10021',NULL,'小乔','F',23,'艺术','4','d353a108-657d-47c3-8aef-e6936fd9a58e',NULL,NULL,NULL,'n'),('10012',NULL,'吕蒙','M',20,'数学','2','dba0b7b9-97bf-41ac-8ac1-652b1fa05c9a',NULL,NULL,NULL,'n'),('10009',NULL,'孙策','M',43,'政治','2','e4b748e6-ff3c-4727-9fc2-458b97cb318d',NULL,NULL,NULL,'n'),('10017',NULL,'曹仁','M',40,'数学','3','f29ca87a-7b85-4ed6-b57b-8ab75128487b',NULL,NULL,NULL,'n'),('10007',NULL,'马超','M',29,'历史','1','f8d34aac-93d6-47ab-915e-fcd169007f62',NULL,NULL,NULL,'n');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-28 12:43:34
