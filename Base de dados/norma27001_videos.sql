-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: norma27001
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `videos`
--

DROP TABLE IF EXISTS `videos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `videos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) DEFAULT NULL,
  `duracao` varchar(255) DEFAULT NULL,
  `thumbnail` varchar(255) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `videourl` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videos`
--

LOCK TABLES `videos` WRITE;
/*!40000 ALTER TABLE `videos` DISABLE KEYS */;
INSERT INTO `videos` VALUES (1,'Este vídeo oferece um resumo da norma ISO 27001, explicando os principais conceitos e benefícios da implementação de um Sistema de Gestão de Segurança da Informação (SGSI).Fonte:IT Governance Ltd','4:45','https://i.ytimg.com/vi/x792wXSeAhA/hqdefault.jpg','What is ISO 27001? | A Brief Summary of the Standard','https://www.youtube.com/watch?v=x792wXSeAhA'),(2,'Este vídeo aborda as principais perguntas sobre a norma ISO/IEC 27001:2022, incluindo os requisitos que um SGSI deve atender.Fonte: ISO - International Organization for Standardization','10:12','https://i.ytimg.com/vi/jPA6gbsT2IQ/hqdefault.jpg','What is ISO/IEC 27001? Guide to Information Security','https://www.youtube.com/watch?v=jPA6gbsT2IQ'),(3,'Este vídeo aborda as etapas finais de preparação para a certificação ISO 27001:2022, incluindo documentos essenciais e auditorias. Fonte: Prabh Nair','15:30','https://i.ytimg.com/vi/IRnIy-c5_6k/hqdefault.jpg','Your Last-Minute ISO 27001 Prep: Documents, Audits, and Must-Haves','https://www.youtube.com/watch?v=IRnIy-c5_6k'),(4,'Este vídeo cobre os fundamentos das auditorias internas da ISO 27001, incluindo como conduzi-las e o que esperar. Fonte: Dejan Kosutic','20:45','https://i.ytimg.com/vi/Rk1dnXoIPbM/hqdefault.jpg','ISO 27001 Internal Audit Essentials: Everything You Need to Know','https://www.youtube.com/watch?v=Rk1dnXoIPbM'),(5,'Este vídeo explica de forma curta o que é uma auditoria interna da ISO 27001.','0:58','https://i.ytimg.com/vi/sdtgNI31iJY/hqdefault.jpg','What is an ISO 27001 Internal Audit?','https://www.youtube.com/watch?v=sdtgNI31iJY'),(6,'Este vídeo oferece uma visão geral dos controlos da ISO 27001, incluindo as atualizações introduzidas na versão 2022.','5:47','https://i.ytimg.com/vi/9Pb5JrR4TKE/hqdefault.jpg','ISO 27001 Controls Overview | ControlCase','https://www.youtube.com/watch?v=9Pb5JrR4TKE');
/*!40000 ALTER TABLE `videos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-20 14:07:27
