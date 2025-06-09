/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: domaci2RMT
-- ------------------------------------------------------
-- Server version	11.7.2-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `Population`
--

DROP TABLE IF EXISTS `Population`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Population` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ime` varchar(30) NOT NULL,
  `prezime` varchar(30) NOT NULL,
  `jmbg` varchar(13) NOT NULL,
  `broj_pasosa` varchar(9) NOT NULL,
  `datum_rodjenja` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Population`
--

LOCK TABLES `Population` WRITE;
/*!40000 ALTER TABLE `Population` DISABLE KEYS */;
INSERT INTO `Population` VALUES
(1,'Marko','Jovanović','1234567890123','987654321','1985-03-12'),
(2,'Milica','Petrović','2345678901234','123456789','1992-07-05'),
(3,'Nikola','Stanković','3456789012345','234567891','1978-11-23'),
(4,'Jelena','Nikolić','4567890123456','345678912','1969-05-14'),
(5,'Stefan','Đorđević','5678901234567','456789123','1990-02-27'),
(6,'Ana','Ilić','6789012345678','567891234','1983-08-30'),
(7,'Ivana','Pavlović','7890123456789','678912345','2000-12-01'),
(8,'Aleksandar','Lukić','8901234567890','789123456','1972-09-16'),
(9,'Katarina','Janković','9012345678901','891234567','1995-06-21'),
(10,'Miloš','Ristić','0123456789012','912345678','1987-01-09'),
(11,'Luka','Popović','1112233445566','123987654','1991-04-17'),
(12,'Sara','Savić','2223344556677','234198765','1989-10-02'),
(13,'Petar','Kovačević','3334455667788','345219876','1975-06-06'),
(14,'Teodora','Todorović','4445566778899','456321987','1980-03-28'),
(15,'Nenad','Simović','5556677889900','567432198','1996-09-13'),
(16,'Marija','Vučić','6667788990011','678543219','1968-12-25'),
(17,'Filip','Bošković','7778899001122','789654321','1982-07-08'),
(18,'Tijana','Lazarević','8889900112233','891765432','2001-11-19'),
(19,'Vladimir','Stojanović','9990011223344','912876543','1970-02-14'),
(20,'Nina','Radovanović','0001122334455','123456780','1993-08-11'),
(21,'Ivan','Mihajlović','1012233445566','234567801','1986-05-22'),
(22,'Andjela','Stevanović','2123344556677','345678912','1998-03-30'),
(23,'Bojan','Mitrović','3234455667788','456789123','1974-10-04'),
(24,'Tamara','Vasić','4345566778899','567891234','1990-01-18'),
(25,'Đorđe','Marinković','5456677889900','678912345','1965-07-25'),
(26,'Jovana','Perić','6567788990011','789123456','1999-11-07'),
(27,'Uroš','Živković','7678899001122','891234567','1981-06-03'),
(28,'Milena','Antić','8789900112233','912345678','1984-09-15'),
(29,'Vuk','Mladenović','9890011223344','123456781','1977-12-29'),
(30,'Sanja','Rakić','0901122334455','234567892','1994-02-20'),
(31,'Ognjen','Pantić','1012233445567','345678903','1988-10-10'),
(32,'Isidora','Stanisavljević','2123344556678','456789014','1971-08-14'),
(33,'Željko','Lazović','3234455667789','567890125','2000-03-03'),
(34,'Nataša','Blagojević','4345566778900','678901236','1969-04-01'),
(35,'Milan','Arsić','5456677889011','789012347','1976-05-06'),
(36,'Aleksandra','Obradović','6567788990122','890123458','1983-12-20'),
(37,'Dragan','Ignjatović','7678899001233','901234569','1997-06-28'),
(38,'Tatjana','Mitić','8789900112344','012345670','1966-11-30'),
(39,'Radovan','Milinković','9890011223455','123456781','1985-09-09'),
(40,'Ksenija','Đurić','0901122334566','234567892','1992-02-17'),
(41,'Boris','Damjanović','1012233445677','345678903','1973-07-12'),
(42,'Ljiljana','Simić','2123344556788','456789014','1980-01-23'),
(43,'Nikolina','Živković','3234455667899','567890125','2002-04-04'),
(44,'Goran','Cvetković','4345566778901','678901236','1967-08-18'),
(45,'Vesna','Tasić','5456677889012','789012347','1979-09-29'),
(46,'Nebojša','Lepojević','6567788990123','890123458','1987-03-06'),
(47,'Slavica','Avramović','7678899001234','901234569','1991-10-16'),
(48,'Damir','Vuković','8789900112345','012345670','1996-12-08'),
(49,'Nevena','Jakšić','9890011223456','123456781','1989-11-13'),
(50,'test','testerovic','1234567890123','123456789','1900-01-01');
/*!40000 ALTER TABLE `Population` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Putovanje`
--

DROP TABLE IF EXISTS `Putovanje`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Putovanje` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `zemlja_id` bigint(20) NOT NULL,
  `datum_prijave` date NOT NULL,
  `datum_ulaska` date NOT NULL,
  `datum_izlaska` date NOT NULL,
  `transport_id` bigint(20) NOT NULL,
  `placa_se` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`,`user_id`,`zemlja_id`),
  KEY `Putovanje_Transport_FK` (`transport_id`),
  KEY `Putovanje_User_FK` (`user_id`),
  KEY `Putovanje_Zemlja_FK` (`zemlja_id`),
  CONSTRAINT `Putovanje_Transport_FK` FOREIGN KEY (`transport_id`) REFERENCES `Transport` (`id`),
  CONSTRAINT `Putovanje_User_FK` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
  CONSTRAINT `Putovanje_Zemlja_FK` FOREIGN KEY (`zemlja_id`) REFERENCES `Zemlja` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Putovanje`
--

LOCK TABLES `Putovanje` WRITE;
/*!40000 ALTER TABLE `Putovanje` DISABLE KEYS */;
/*!40000 ALTER TABLE `Putovanje` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Transport`
--

DROP TABLE IF EXISTS `Transport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Transport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tip` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Transport`
--

LOCK TABLES `Transport` WRITE;
/*!40000 ALTER TABLE `Transport` DISABLE KEYS */;
INSERT INTO `Transport` VALUES
(1,'CAR'),
(2,'TRAIN'),
(3,'MOTORCYCLE'),
(4,'BUS'),
(5,'PLANE');
/*!40000 ALTER TABLE `Transport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `ime` varchar(30) NOT NULL,
  `prezime` varchar(30) NOT NULL,
  `email` varchar(100) NOT NULL,
  `jmbg` varchar(13) NOT NULL,
  `broj_pasosa` varchar(9) NOT NULL,
  `datum_rodjenja` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES
(1,'tester','tester','test','testerovic','test@gmail.com','1234567890123','123456789','1900-01-01');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Zemlja`
--

DROP TABLE IF EXISTS `Zemlja`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Zemlja` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `naziv` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Zemlja`
--

LOCK TABLES `Zemlja` WRITE;
/*!40000 ALTER TABLE `Zemlja` DISABLE KEYS */;
INSERT INTO `Zemlja` VALUES
(1,'FRANCE'),
(2,'GERMANY'),
(3,'ITALY'),
(4,'POLAND'),
(5,'DENMARK');
/*!40000 ALTER TABLE `Zemlja` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'domaci2RMT'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-06-09 15:32:17
