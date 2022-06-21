-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: shopmedb
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Electronics','electronics','electronics.png',_binary '',NULL,NULL),(2,'Camera & Photo','camera','camera.jpg',_binary '',1,'-1-'),(3,'Computers','computers','computers.png',_binary '',NULL,NULL),(4,'Cell Phones & Accessories','cellphones','cellphones.png',_binary '',1,'-1-'),(5,'Desktops','desktop_computers','desktop computers.png',_binary '',3,'-3-'),(6,'Laptops','laptop_computers','laptop computers.png',_binary '',3,'-3-'),(7,'Tablets','tablet_computers','tablets.png',_binary '',3,'-3-'),(8,'Computer Components','computer_components','computer components.png',_binary '',3,'-3-'),(9,'Bags & Cases','camera_bags_cases','bags_cases.png',_binary '',2,'-1-2-'),(10,'Digital Cameras','digital_cameras','digital cameras.png',_binary '',2,'-1-2-'),(11,'Flashes','camera_flashes','flashes.png',_binary '',2,'-1-2-'),(12,'Lenses','camera_lenses','lenses.png',_binary '',2,'-1-2-'),(13,'Tripods & Monopods','camera_tripods_monopods','tripods_monopods.png',_binary '',2,'-1-2-'),(14,'Carrier Cell Phones','carrier_cellphones','carrier cellphones.png',_binary '',4,'-1-4-'),(15,'Unlocked Cell Phones','unlocked_cellphones','unlocked cellphones.png',_binary '',4,'-1-4-'),(16,'Accessories','cellphone_accessories','cellphone accessories.png',_binary '',4,'-1-4-'),(17,'Cables & Adapters','cellphone_cables_adapters','cables and adapters.png',_binary '',16,'-1-4-16-'),(18,'MicroSD Cards','microsd_cards','microsd cards.png',_binary '',16,'-1-4-16-'),(19,'Stands','cellphone_stands','cellphone_stands.png',_binary '',16,'-1-4-16-'),(20,'Cases','cellphone_cases','cellphone cases.png',_binary '',16,'-1-4-16-'),(21,'Headphones','headphones','headphones.png',_binary '',16,'-1-4-16-'),(22,'CPU Processors Unit','computer_processors','computer processors.png',_binary '',8,'-3-8-'),(23,'Graphic Cards','computer_graphic_cards','graphic cards.png',_binary '',8,'-3-8-'),(24,'Internal Hard Drives','hard_drive','internal hard drive.png',_binary '',8,'-3-8-'),(25,'Internal Optical Drives','computer_optical_drives','internal optical drives.png',_binary '',8,'-3-8-'),(26,'Power Supplies','computer_power_supplies','power supplies.png',_binary '',8,'-3-8-'),(27,'Solid State Drives','solid_state_drives','solid state drives.png',_binary '',8,'-3-8-'),(28,'Sound Cards','computer_sound_cards','sound cards.png',_binary '',8,'-3-8-'),(29,'Memory','computer_memory','computer memory.png',_binary '',8,'-3-8-'),(30,'Motherboard','computer_motherboard','motherboards.png',_binary '',8,'-3-8-'),(31,'Network Cards','computer_network_cards','network cards.png',_binary '',8,'-3-8-');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-21  7:16:30
