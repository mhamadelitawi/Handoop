-- MySQL dump 10.13  Distrib 5.6.28, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: handoop
-- ------------------------------------------------------
-- Server version	5.6.28-0ubuntu0.15.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `fcmid`
--

DROP TABLE IF EXISTS `fcmid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fcmid` (
  `id` varchar(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fcmid`
--

LOCK TABLES `fcmid` WRITE;
/*!40000 ALTER TABLE `fcmid` DISABLE KEYS */;
/*!40000 ALTER TABLE `fcmid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `guid` varchar(100) NOT NULL,
  `task` varchar(50) NOT NULL,
  `title` varchar(100) NOT NULL,
  `preview` text NOT NULL,
  `pathresult` varchar(100) NOT NULL,
  `time` varchar(50) NOT NULL,
  PRIMARY KEY (`guid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `name` varchar(100) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL,
  `passsword` varchar(100) NOT NULL,
  `idpush` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('a','a','a','a','fUg9hKwzYbw:APA91bGCCQ_jHLi8htJqe3pvKXF2MpQWXzZ8B1Y4ILwdmkZh52AiIagxdj4bivmnLtDJPo6rTs1YinSBwIXhCIgh6fhj6fLU6iWv3k24VH77G3wHJecc6duC7NqKMogTILxOdMd_4Llt'),('a','a','aa','a','fUg9hKwzYbw:APA91bGCCQ_jHLi8htJqe3pvKXF2MpQWXzZ8B1Y4ILwdmkZh52AiIagxdj4bivmnLtDJPo6rTs1YinSBwIXhCIgh6fhj6fLU6iWv3k24VH77G3wHJecc6duC7NqKMogTILxOdMd_4Llt'),('a','a','aace','a','fUg9hKwzYbw:APA91bGCCQ_jHLi8htJqe3pvKXF2MpQWXzZ8B1Y4ILwdmkZh52AiIagxdj4bivmnLtDJPo6rTs1YinSBwIXhCIgh6fhj6fLU6iWv3k24VH77G3wHJecc6duC7NqKMogTILxOdMd_4Llt'),('a','a','z','a','fUg9hKwzYbw:APA91bGCCQ_jHLi8htJqe3pvKXF2MpQWXzZ8B1Y4ILwdmkZh52AiIagxdj4bivmnLtDJPo6rTs1YinSBwIXhCIgh6fhj6fLU6iWv3k24VH77G3wHJecc6duC7NqKMogTILxOdMd_4Llt');
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

-- Dump completed on 2017-03-07 14:07:07
