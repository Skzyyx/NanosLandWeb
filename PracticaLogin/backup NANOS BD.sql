-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: nanos_land
-- ------------------------------------------------------
-- Server version	9.2.0

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
-- Table structure for table `eventos`
--

DROP TABLE IF EXISTS `eventos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `eventos` (
  `id_evento` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `nombre_evento` varchar(150) NOT NULL,
  `fecha_evento` datetime NOT NULL,
  `paquete` varchar(100) NOT NULL,
  `costo` decimal(10,2) NOT NULL,
  `estado` enum('agendado','terminado') NOT NULL DEFAULT 'agendado',
  PRIMARY KEY (`id_evento`),
  KEY `id_cliente` (`id_cliente`),
  CONSTRAINT `eventos_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `usuarios` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `eventos`
--

LOCK TABLES `eventos` WRITE;
/*!40000 ALTER TABLE `eventos` DISABLE KEYS */;
INSERT INTO `eventos` VALUES (1,1,'Fiesta de Juan','2025-11-25 15:00:00','Paquete Completo',5000.00,'terminado'),(2,3,'Fiesta','2025-11-18 09:12:00','Basico|1500.00',1500.00,'terminado'),(3,3,'fieston2','2025-11-26 01:21:00','Basico|1500.00',1500.00,'terminado'),(4,8,'cumple yooooo','2025-11-12 11:02:00','Premium',5000.00,'terminado'),(6,10,'Cumple 23','2025-11-18 18:19:00','Premium',5000.00,'terminado'),(7,8,'Fiesta','2025-11-22 18:18:00','Completo',3000.00,'terminado'),(8,8,'fieston','2025-11-28 23:28:00','Basico',1500.00,'agendado'),(9,8,'111','2025-11-18 21:35:00','Premium',5000.00,'terminado'),(10,8,'Sofia cumple','2025-11-29 16:40:00','Premium',5000.00,'agendado'),(11,8,'Fiesta rodrigo','2025-11-22 16:40:00','Completo',3000.00,'agendado'),(12,8,'Jose fiesta','2025-11-25 16:41:00','Completo',3000.00,'agendado');
/*!40000 ALTER TABLE `eventos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `rol` enum('admin','trabajador','cliente') NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'Admin Nanos','admin@nanosland.com','$2a$10$f/A.6..BnaGqL8gXf2.MmuoXvYqB.s.1J.S.S1.s1.S.S.1.s1.','admin','555-1111'),(2,'Trabajador Nanos','worker@nanosland.com','$2a$10$f/A.6..BnaGqL8gXf2.MmuoXvYqB.s.1J.S.S1.s1.S.S.1.s1.','trabajador','555-2222'),(3,'Jose roberto','joseeaguilarg05@gmail.com','11111','cliente','12345678'),(5,'Admin Nanos','admin2@nanosland.com','1111','admin','555-1111'),(6,'Admin Nanos trabajador','trabajador2@nanosland.com','1111','trabajador','555-1111'),(7,'juan','juan@gmail.com','1111','admin','12345678'),(8,'HUGO','hugo@gmail.com','11111','cliente','123456789'),(10,'EDUARDO','EDUARDO@nanosland.com','11111','cliente','1234567899'),(11,'Paco Ruiz','paco@gmail.com','11111','cliente','6666666666666'),(14,'Jose','jose1@gmail.com','123','cliente','1111');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'nanos_land'
--

--
-- Dumping routines for database 'nanos_land'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-20 16:47:18
