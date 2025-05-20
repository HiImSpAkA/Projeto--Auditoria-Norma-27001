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
-- Table structure for table `cursos`
--

DROP TABLE IF EXISTS `cursos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cursos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) DEFAULT NULL,
  `duracao` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `modalidade` varchar(255) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cursos`
--

LOCK TABLES `cursos` WRITE;
/*!40000 ALTER TABLE `cursos` DISABLE KEYS */;
INSERT INTO `cursos` VALUES (1,'Formação certificada internacionalmente que desenvolve as competências necessárias para realizar auditorias de Sistemas de Gestão de Segurança da Informação (SGSI) aplicando princípios, procedimentos e técnicas reconhecidas.','40 horas','graduation-cap','Online','ISO/IEC 27001 Lead Auditor - PECB','https://pecb.com/en/education-and-certification-for-individuals/iso-iec-27001/iso-iec-27001-lead-auditor'),(2,'Programa essencial para profissionais que procuram excelência em Sistemas de Gestão de Segurança da Informação, fornecendo curso completo em princípios e práticas de auditoria com certificação reconhecida internacionalmente.','40 horas','graduation-cap','Self-paced','ISO 27001 Lead Auditor - The Knowledge Academy Portugal','https://www.theknowledgeacademy.com/pt/courses/iso-27001-training/iso-27001-lead-auditor/'),(3,'Curso com certificação ISMS Implementer Foundation da TÜV-SÜD que apresenta introdução completa à segurança da informação baseada na norma ISO/IEC 27001:2022, com exemplos e casos práticos.','16 horas','graduation-cap','Online','ISO 27001 Implementer Foundation - Técnico+','https://tecnicomais.pt/cursos/iso-27001-implementer-foundation/'),(4,'Curso de Auditor Líder acreditado pela Exemplar Global para auditoria em Sistemas de Gestão de Segurança da Informação, com instrutores experientes e exercícios práticos baseados em casos reais.','32 horas','graduation-cap','Presencial','Auditor Líder ISO/IEC 27001:2022 - BSI','https://bsi.learncentral.com/shop/Course.aspx?id=26568&name=Connected+Learning+Live+ISO+27001:2022+Auditor+L%C3%ADder+(TPECS)'),(5,'Formação baseada em estudo de caso real que desafia os participantes a auditar um Sistema de Gestão durante a formação, com metodologia prática desenvolvida para o mercado português.','35 horas','graduation-cap','B-learning','Information Security 27001 Lead Auditor - Behaviour Group','https://behaviour-group.com/PT/curso-information-security-27001-lead-auditor/'),(6,'Percurso completo de formação em ISO 27001 disponível em português e inglês, com módulos adaptados para diferentes níveis de experiência, do nível básico até auditor líder.','Variável conforme módulo','graduation-cap','B-learning','ISO/IEC 27001 Foundation, Lead Implementer and Auditor - B2B Learning','https://www.b2blearning.eu/en/54-iso27001-training-certification'),(7,'Formação certificada PECB para auditores de sistemas de gestão de segurança da informação, fornecida por empresa com presença local em Portugal e reconhecimento internacional.','5 dias','graduation-cap','Presencial','ISO 27001 Lead Auditor - NobleProg Portugal','https://www.nobleprog.pt/capacitacao-iso-27001');
/*!40000 ALTER TABLE `cursos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-20 14:07:26
