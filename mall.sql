-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mall
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `sys_avatar`
--

DROP TABLE IF EXISTS `sys_avatar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_avatar` (
  `user_id` int NOT NULL,
  `avatar_bytes` longblob,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_avatar`
--

LOCK TABLES `sys_avatar` WRITE;
/*!40000 ALTER TABLE `sys_avatar` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_avatar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_file_map`
--

DROP TABLE IF EXISTS `sys_file_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_file_map` (
  `uid` int DEFAULT NULL,
  `file_seq` varchar(64) NOT NULL,
  `extension` varchar(10) NOT NULL,
  `status` tinyint(1) DEFAULT NULL COMMENT '0禁用1可用',
  PRIMARY KEY (`file_seq`,`extension`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_file_map`
--

LOCK TABLES `sys_file_map` WRITE;
/*!40000 ALTER TABLE `sys_file_map` DISABLE KEYS */;
INSERT INTO `sys_file_map` VALUES (1,'1-1656658449757','.png',1);
/*!40000 ALTER TABLE `sys_file_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '权限名字?',
  `path` varchar(200) DEFAULT NULL,
  `component` varchar(255) DEFAULT NULL,
  `visible` char(1) DEFAULT '0' COMMENT ' 0不可1可?',
  `status` char(1) DEFAULT '1' COMMENT ' 0不可用1可用?',
  `perms` varchar(100) DEFAULT NULL,
  `icon` varchar(100) DEFAULT '#',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0',
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='menu?';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'sys.scan',NULL,NULL,'0','1','sys.scan','#',NULL,NULL,NULL,NULL,0,NULL),(2,'sys.update',NULL,NULL,'0','1','sys.update','#',NULL,NULL,NULL,NULL,0,NULL),(3,'sys.del',NULL,NULL,'0','1','sys.del','#',NULL,NULL,NULL,NULL,0,NULL),(4,'sys.mowner',NULL,NULL,'0','1','sys.mowner','#',NULL,NULL,NULL,NULL,0,NULL),(5,'浏览商店商品',NULL,NULL,'0','1','sys.mscan','#',NULL,NULL,NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_merchant`
--

DROP TABLE IF EXISTS `sys_merchant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_merchant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  `score` double DEFAULT NULL,
  `name` varchar(10) NOT NULL,
  `own_phone` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_merchant`
--

LOCK TABLES `sys_merchant` WRITE;
/*!40000 ALTER TABLE `sys_merchant` DISABLE KEYS */;
INSERT INTO `sys_merchant` VALUES (1,1,1,NULL,'ADMIN',NULL);
/*!40000 ALTER TABLE `sys_merchant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `role_key` varchar(100) DEFAULT NULL,
  `status` char(1) DEFAULT '0',
  `del_flag` int DEFAULT '0' COMMENT 'del_flag',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'admin','admin','0',0,NULL,NULL,NULL,NULL,NULL),(2,'user','user','0',0,NULL,NULL,NULL,NULL,NULL),(3,'merchant','merchant','0',0,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `menu_id` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(2,1),(3,1),(3,2),(3,4);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(64) NOT NULL DEFAULT 'NULL',
  `nick_name` varchar(64) DEFAULT 'NULL',
  `password` varchar(64) NOT NULL DEFAULT 'NULL',
  `status` char(1) DEFAULT '0',
  `email` varchar(64) DEFAULT NULL,
  `phonenumber` varchar(32) DEFAULT NULL,
  `sex` char(1) DEFAULT NULL,
  `avatar` varchar(128) DEFAULT NULL,
  `user_type` char(1) NOT NULL DEFAULT '1',
  `create_by` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` bigint DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','NULL','$2a$10$hvrXH3RbRl17.eqvbVnh7uBgw0pHfwIotYVUFMC7yUrMyxKcedBee','0',NULL,NULL,NULL,NULL,'2',NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
/*!50032 DROP TRIGGER IF EXISTS BingingRole */;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `BingingRole` AFTER INSERT ON `sys_user` FOR EACH ROW insert into sys_user_role(user_id, role_id)
    values (new.id, 2) */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_pd_class`
--

DROP TABLE IF EXISTS `t_pd_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_pd_class` (
  `cid` int NOT NULL AUTO_INCREMENT,
  `pid` int NOT NULL,
  `mask_sec` int NOT NULL,
  `class_name` varchar(30) NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_pd_class`
--

LOCK TABLES `t_pd_class` WRITE;
/*!40000 ALTER TABLE `t_pd_class` DISABLE KEYS */;
INSERT INTO `t_pd_class` VALUES (1,1,0,'分类1'),(2,1,1,'分类2');
/*!40000 ALTER TABLE `t_pd_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_pd_order`
--

DROP TABLE IF EXISTS `t_pd_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_pd_order` (
  `oid` int(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `num` int NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_payed` tinyint(1) NOT NULL,
  `sku_serialized` blob,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_pd_order`
--

LOCK TABLES `t_pd_order` WRITE;
/*!40000 ALTER TABLE `t_pd_order` DISABLE KEYS */;
INSERT INTO `t_pd_order` VALUES (00000000000000000001,1,1,'2022-07-04 02:25:10',0,_binary '{\"pid\":1,\"mask\":\"0.0\",\"img\":\"1\",\"stock\":100,\"price\":1}');
/*!40000 ALTER TABLE `t_pd_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_pd_sku`
--

DROP TABLE IF EXISTS `t_pd_sku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_pd_sku` (
  `pid` int NOT NULL,
  `mask` varchar(15) NOT NULL,
  `img` blob,
  `stock` int NOT NULL DEFAULT '0',
  `price` double DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_pd_sku`
--

LOCK TABLES `t_pd_sku` WRITE;
/*!40000 ALTER TABLE `t_pd_sku` DISABLE KEYS */;
INSERT INTO `t_pd_sku` VALUES (1,'0.0',_binary '1',100,1),(1,'0.1',NULL,0,0),(1,'1.0',_binary '1-1656658449757.png',1,20),(1,'1.1',_binary '1111',111,11111),(1,'2.0',NULL,0,0),(1,'2.1',NULL,0,0);
/*!40000 ALTER TABLE `t_pd_sku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_pd_tag`
--

DROP TABLE IF EXISTS `t_pd_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_pd_tag` (
  `cid` int unsigned NOT NULL,
  `tag_sec` int unsigned NOT NULL,
  `content` varchar(30) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_pd_tag`
--

LOCK TABLES `t_pd_tag` WRITE;
/*!40000 ALTER TABLE `t_pd_tag` DISABLE KEYS */;
INSERT INTO `t_pd_tag` VALUES (1,0,'标签1'),(1,1,'标签2'),(1,2,'标签3'),(2,0,'标签1'),(2,1,'标签2');
/*!40000 ALTER TABLE `t_pd_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_prod`
--

DROP TABLE IF EXISTS `t_prod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_prod` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mid` int DEFAULT NULL COMMENT 'pid',
  `prod_name` varchar(30) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `img` text,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `sec_flag` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_prod`
--

LOCK TABLES `t_prod` WRITE;
/*!40000 ALTER TABLE `t_prod` DISABLE KEYS */;
INSERT INTO `t_prod` VALUES (1,1,'测试',1,'1-1656658449757.png','2022-07-01 06:54:19',0);
/*!40000 ALTER TABLE `t_prod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `pass` varbinary(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-26 10:01:27
