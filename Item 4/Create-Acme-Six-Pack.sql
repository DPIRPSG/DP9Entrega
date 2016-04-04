start transaction;

create database `Acme-Six-Pack`;

use `Acme-Six-Pack`;

create user 'acme-user'@'%' identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete on `Acme-Six-Pack`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, create temporary tables, lock tables, create view, create routine, alter routine, execute, trigger, show view on `Acme-Six-Pack`.* to 'acme-manager'@'%';

-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Six-Pack
-- ------------------------------------------------------
-- Server version	5.5.29

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
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `duration` double NOT NULL,
  `numberOfSeatsAvailable` int(11) NOT NULL,
  `startingMoment` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `room_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `trainer_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_io3v968n09x6l1f9tlrwxm8np` (`deleted`),
  KEY `UK_k3nydm81d3wa878h6krulgcxv` (`startingMoment`),
  KEY `FK_jsa3u2vpmyqg71qwgdtlwkyw4` (`room_id`),
  KEY `FK_kc4mcsxkaop9myg5sv2debq6s` (`service_id`),
  KEY `FK_rpjabsus2mhx5fangkvlyav76` (`trainer_id`),
  CONSTRAINT `FK_rpjabsus2mhx5fangkvlyav76` FOREIGN KEY (`trainer_id`) REFERENCES `trainer` (`id`),
  CONSTRAINT `FK_jsa3u2vpmyqg71qwgdtlwkyw4` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FK_kc4mcsxkaop9myg5sv2debq6s` FOREIGN KEY (`service_id`) REFERENCES `servicetable` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_pictures`
--

DROP TABLE IF EXISTS `activity_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `activity_pictures` (
  `Activity_id` int(11) NOT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  KEY `FK_465ltkojh9ooei6h62da9lw3m` (`Activity_id`),
  CONSTRAINT `FK_465ltkojh9ooei6h62da9lw3m` FOREIGN KEY (`Activity_id`) REFERENCES `activity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_pictures`
--

LOCK TABLES `activity_pictures` WRITE;
/*!40000 ALTER TABLE `activity_pictures` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_idt4b4u259p6vs4pyr9lax4eg` (`userAccount_id`),
  CONSTRAINT `FK_idt4b4u259p6vs4pyr9lax4eg` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (12,0,'Miguel','666777888','Rodriguez',1);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bulletin`
--

DROP TABLE IF EXISTS `bulletin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bulletin` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `publishMoment` datetime DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `gym_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_fk8lhai31xrvtsqyjoil4jvff` (`title`),
  KEY `UK_hsspyx0inkftx2i4dsy9dlp21` (`description`),
  KEY `FK_i07pinmxm7wqwkmffe4c1xr9p` (`gym_id`),
  CONSTRAINT `FK_i07pinmxm7wqwkmffe4c1xr9p` FOREIGN KEY (`gym_id`) REFERENCES `gym` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bulletin`
--

LOCK TABLES `bulletin` WRITE;
/*!40000 ALTER TABLE `bulletin` DISABLE KEYS */;
/*!40000 ALTER TABLE `bulletin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `moment` datetime DEFAULT NULL,
  `starRating` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `actor_id` int(11) NOT NULL,
  `commentedEntity_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_e7nsvg8jf7nmm5gvim6t9b9a7` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `curriculum`
--

DROP TABLE IF EXISTS `curriculum`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `curriculum` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `dislikes` varchar(255) DEFAULT NULL,
  `likes` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `skills` varchar(255) DEFAULT NULL,
  `statement` varchar(255) DEFAULT NULL,
  `updateMoment` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_alf3pnkfq4nvkauexmck2rww4` (`statement`),
  KEY `UK_t47942i7fs3n4li8fj7jw79s8` (`skills`),
  KEY `UK_sj74d7x2g7mnift8xhsnqghs5` (`likes`),
  KEY `UK_yb2pybjnhwmlexa5yxehrpgd` (`dislikes`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `curriculum`
--

LOCK TABLES `curriculum` WRITE;
/*!40000 ALTER TABLE `curriculum` DISABLE KEYS */;
/*!40000 ALTER TABLE `curriculum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  `socialIdentity_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pwmktpkay2yx7v00mrwmuscl8` (`userAccount_id`),
  KEY `FK_ld1r3g5jtm4ktdbmtoju22gwt` (`socialIdentity_id`),
  CONSTRAINT `FK_pwmktpkay2yx7v00mrwmuscl8` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `FK_ld1r3g5jtm4ktdbmtoju22gwt` FOREIGN KEY (`socialIdentity_id`) REFERENCES `socialidentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_activity`
--

DROP TABLE IF EXISTS `customer_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_activity` (
  `customers_id` int(11) NOT NULL,
  `activities_id` int(11) NOT NULL,
  KEY `FK_7u1sf71oni51dlk4b937q5osf` (`activities_id`),
  KEY `FK_pyrntqkoukker2v8mkjnudhki` (`customers_id`),
  CONSTRAINT `FK_pyrntqkoukker2v8mkjnudhki` FOREIGN KEY (`customers_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_7u1sf71oni51dlk4b937q5osf` FOREIGN KEY (`activities_id`) REFERENCES `activity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_activity`
--

LOCK TABLES `customer_activity` WRITE;
/*!40000 ALTER TABLE `customer_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer_creditcards`
--

DROP TABLE IF EXISTS `customer_creditcards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_creditcards` (
  `Customer_id` int(11) NOT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `cvvCode` int(11) NOT NULL,
  `expirationMonth` int(11) NOT NULL,
  `expirationYear` int(11) NOT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  KEY `FK_o19gw4n4g1iyqwsuc71w63lgn` (`Customer_id`),
  CONSTRAINT `FK_o19gw4n4g1iyqwsuc71w63lgn` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer_creditcards`
--

LOCK TABLES `customer_creditcards` WRITE;
/*!40000 ALTER TABLE `customer_creditcards` DISABLE KEYS */;
/*!40000 ALTER TABLE `customer_creditcards` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exchangerate`
--

DROP TABLE IF EXISTS `exchangerate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exchangerate` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `rate` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_6mf6o3hgkiaxa35oc5faidheh` (`currency`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exchangerate`
--

LOCK TABLES `exchangerate` WRITE;
/*!40000 ALTER TABLE `exchangerate` DISABLE KEYS */;
INSERT INTO `exchangerate` VALUES (18,0,'EUR','Euros',1),(19,0,'ESP','Spanish Pesetas',166.386),(20,0,'FRF','French Francs',6.56),(21,0,'ITL','Italian Liras',1936.27),(22,0,'DEM','German Marks',1.96),(23,0,'PTE','Portuguese Escudos',200.48);
/*!40000 ALTER TABLE `exchangerate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feepayment`
--

DROP TABLE IF EXISTS `feepayment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feepayment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `activeMoment` datetime DEFAULT NULL,
  `amount` double NOT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `cvvCode` int(11) NOT NULL,
  `expirationMonth` int(11) NOT NULL,
  `expirationYear` int(11) NOT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `inactiveMoment` datetime DEFAULT NULL,
  `paymentMoment` datetime DEFAULT NULL,
  `customer_id` int(11) NOT NULL,
  `gym_id` int(11) NOT NULL,
  `invoice_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_foca6flwfbl5wit4c3qilm8w9` (`activeMoment`),
  KEY `UK_4wetev20sgxntdpb7goyb6deo` (`inactiveMoment`),
  KEY `UK_cgrpxibcbec8r9hraxhtlnpyr` (`paymentMoment`),
  KEY `FK_8pgjh62mj6aujjifjrthamwbf` (`customer_id`),
  KEY `FK_bgnr9f6puq2dvx5k0oqk9fw3g` (`gym_id`),
  KEY `FK_5ahqdk0wj02ninebuiiafyjd6` (`invoice_id`),
  CONSTRAINT `FK_5ahqdk0wj02ninebuiiafyjd6` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `FK_8pgjh62mj6aujjifjrthamwbf` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bgnr9f6puq2dvx5k0oqk9fw3g` FOREIGN KEY (`gym_id`) REFERENCES `gym` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feepayment`
--

LOCK TABLES `feepayment` WRITE;
/*!40000 ALTER TABLE `feepayment` DISABLE KEYS */;
/*!40000 ALTER TABLE `feepayment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `isSystem` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `actor_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_l1kp977466ddsv762wign7kdh` (`name`),
  KEY `UK_a5prtxr1c0vwoxbpixtnse2nq` (`isSystem`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder`
--

LOCK TABLES `folder` WRITE;
/*!40000 ALTER TABLE `folder` DISABLE KEYS */;
INSERT INTO `folder` VALUES (13,0,'','InBox',12),(14,0,'','OutBox',12),(15,0,'','TrashBox',12),(16,0,'','SpamBox',12);
/*!40000 ALTER TABLE `folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folder_message`
--

DROP TABLE IF EXISTS `folder_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folder_message` (
  `folders_id` int(11) NOT NULL,
  `messages_id` int(11) NOT NULL,
  KEY `FK_5nh3mwey9bw25ansh2thcbcdh` (`messages_id`),
  KEY `FK_o1e2m8i8khapsgpd6jg28dr7e` (`folders_id`),
  CONSTRAINT `FK_o1e2m8i8khapsgpd6jg28dr7e` FOREIGN KEY (`folders_id`) REFERENCES `folder` (`id`),
  CONSTRAINT `FK_5nh3mwey9bw25ansh2thcbcdh` FOREIGN KEY (`messages_id`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder_message`
--

LOCK TABLES `folder_message` WRITE;
/*!40000 ALTER TABLE `folder_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `folder_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gym`
--

DROP TABLE IF EXISTS `gym`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gym` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fee` double NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_m6u62a7fqw6e7wt8jlnghjgr7` (`name`),
  KEY `UK_8ql4gg5c7gcj3kswc0c4wgm64` (`description`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gym`
--

LOCK TABLES `gym` WRITE;
/*!40000 ALTER TABLE `gym` DISABLE KEYS */;
/*!40000 ALTER TABLE `gym` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gym_room`
--

DROP TABLE IF EXISTS `gym_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gym_room` (
  `Gym_id` int(11) NOT NULL,
  `rooms_id` int(11) NOT NULL,
  UNIQUE KEY `UK_r0bt08oiyrru0jnaukh45vpcb` (`rooms_id`),
  KEY `FK_aa24yahpjdssr14378atcopdp` (`Gym_id`),
  CONSTRAINT `FK_aa24yahpjdssr14378atcopdp` FOREIGN KEY (`Gym_id`) REFERENCES `gym` (`id`),
  CONSTRAINT `FK_r0bt08oiyrru0jnaukh45vpcb` FOREIGN KEY (`rooms_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gym_room`
--

LOCK TABLES `gym_room` WRITE;
/*!40000 ALTER TABLE `gym_room` DISABLE KEYS */;
/*!40000 ALTER TABLE `gym_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('DomainEntity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoice`
--

DROP TABLE IF EXISTS `invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `invoice` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `VAT` varchar(255) DEFAULT NULL,
  `creationMoment` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `invoiceesName` varchar(255) DEFAULT NULL,
  `totalCost` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoice`
--

LOCK TABLES `invoice` WRITE;
/*!40000 ALTER TABLE `invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `sentMoment` datetime DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `sender_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_actor`
--

DROP TABLE IF EXISTS `message_actor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_actor` (
  `received_id` int(11) NOT NULL,
  `recipients_id` int(11) NOT NULL,
  KEY `FK_s15b8cpmjbq3qqa55fool5tp7` (`received_id`),
  CONSTRAINT `FK_s15b8cpmjbq3qqa55fool5tp7` FOREIGN KEY (`received_id`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_actor`
--

LOCK TABLES `message_actor` WRITE;
/*!40000 ALTER TABLE `message_actor` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_actor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `numberOfSeats` int(11) NOT NULL,
  `gym_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n4nmms7h5i300trkr701ssh1c` (`gym_id`),
  CONSTRAINT `FK_n4nmms7h5i300trkr701ssh1c` FOREIGN KEY (`gym_id`) REFERENCES `gym` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_pictures`
--

DROP TABLE IF EXISTS `room_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `room_pictures` (
  `Room_id` int(11) NOT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  KEY `FK_cqkd838ebng1flco55ayp5q0` (`Room_id`),
  CONSTRAINT `FK_cqkd838ebng1flco55ayp5q0` FOREIGN KEY (`Room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_pictures`
--

LOCK TABLES `room_pictures` WRITE;
/*!40000 ALTER TABLE `room_pictures` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serviceentity_pictures`
--

DROP TABLE IF EXISTS `serviceentity_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serviceentity_pictures` (
  `ServiceEntity_id` int(11) NOT NULL,
  `pictures` varchar(255) DEFAULT NULL,
  KEY `FK_jy0v1kvmxy41tfajmjrnmbkf3` (`ServiceEntity_id`),
  CONSTRAINT `FK_jy0v1kvmxy41tfajmjrnmbkf3` FOREIGN KEY (`ServiceEntity_id`) REFERENCES `servicetable` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serviceentity_pictures`
--

LOCK TABLES `serviceentity_pictures` WRITE;
/*!40000 ALTER TABLE `serviceentity_pictures` DISABLE KEYS */;
INSERT INTO `serviceentity_pictures` VALUES (17,'https://www.clubfluviallugo.com/web/media/rokgallery/1/1902b722-85e5-4d2c-e3c5-d4b96e055fc1/clase_fitness_4.jpg'),(17,'https://www.clubfluviallugo.com/web/media/rokgallery/0/052c7c45-45e9-408e-ba87-3b29c88ca085/clase_fitness_1.jpg');
/*!40000 ALTER TABLE `serviceentity_pictures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicetable`
--

DROP TABLE IF EXISTS `servicetable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servicetable` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_lvjbgckt0iwlxjm1rl40ou5ud` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicetable`
--

LOCK TABLES `servicetable` WRITE;
/*!40000 ALTER TABLE `servicetable` DISABLE KEYS */;
INSERT INTO `servicetable` VALUES (17,0,'Servicio de Fitness','Fitness');
/*!40000 ALTER TABLE `servicetable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicetable_gym`
--

DROP TABLE IF EXISTS `servicetable_gym`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servicetable_gym` (
  `services_id` int(11) NOT NULL,
  `gyms_id` int(11) NOT NULL,
  KEY `FK_r9gamdmbt0f2kj461x50f7oif` (`gyms_id`),
  KEY `FK_x997iw7m82b9c2xqvdkmyk23` (`services_id`),
  CONSTRAINT `FK_x997iw7m82b9c2xqvdkmyk23` FOREIGN KEY (`services_id`) REFERENCES `servicetable` (`id`),
  CONSTRAINT `FK_r9gamdmbt0f2kj461x50f7oif` FOREIGN KEY (`gyms_id`) REFERENCES `gym` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicetable_gym`
--

LOCK TABLES `servicetable_gym` WRITE;
/*!40000 ALTER TABLE `servicetable_gym` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicetable_gym` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicetable_trainer`
--

DROP TABLE IF EXISTS `servicetable_trainer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `servicetable_trainer` (
  `services_id` int(11) NOT NULL,
  `trainers_id` int(11) NOT NULL,
  KEY `FK_2tvk7g2i5mb7h9qy72xur537g` (`trainers_id`),
  KEY `FK_darkni20sjm0c1xu31gsqfg6c` (`services_id`),
  CONSTRAINT `FK_darkni20sjm0c1xu31gsqfg6c` FOREIGN KEY (`services_id`) REFERENCES `servicetable` (`id`),
  CONSTRAINT `FK_2tvk7g2i5mb7h9qy72xur537g` FOREIGN KEY (`trainers_id`) REFERENCES `trainer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicetable_trainer`
--

LOCK TABLES `servicetable_trainer` WRITE;
/*!40000 ALTER TABLE `servicetable_trainer` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicetable_trainer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `socialidentity`
--

DROP TABLE IF EXISTS `socialidentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `socialidentity` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `homePage` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `nick` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `socialidentity`
--

LOCK TABLES `socialidentity` WRITE;
/*!40000 ALTER TABLE `socialidentity` DISABLE KEYS */;
/*!40000 ALTER TABLE `socialidentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spamterm`
--

DROP TABLE IF EXISTS `spamterm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `spamterm` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `term` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l3ddgk2onm11dkeqsq6ebym2p` (`term`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spamterm`
--

LOCK TABLES `spamterm` WRITE;
/*!40000 ALTER TABLE `spamterm` DISABLE KEYS */;
INSERT INTO `spamterm` VALUES (2,0,'viagra'),(3,0,'cialis'),(4,0,'sex'),(5,0,'scort'),(6,0,'money transfer'),(7,0,'lottery'),(8,0,'green card'),(9,0,'email quota'),(10,0,'click here'),(11,0,'spam');
/*!40000 ALTER TABLE `spamterm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainer`
--

DROP TABLE IF EXISTS `trainer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trainer` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `curriculum_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_1v0ljnwuhlwur1d8ukoe2vlhp` (`userAccount_id`),
  KEY `UK_3upyd7x25850hnergulsbqv89` (`name`),
  KEY `UK_bcrix4fo9k0qiy2hlleh8ru1u` (`surname`),
  KEY `FK_2m4njhq2k32jkn0c1wu4beheq` (`curriculum_id`),
  CONSTRAINT `FK_1v0ljnwuhlwur1d8ukoe2vlhp` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`),
  CONSTRAINT `FK_2m4njhq2k32jkn0c1wu4beheq` FOREIGN KEY (`curriculum_id`) REFERENCES `curriculum` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainer`
--

LOCK TABLES `trainer` WRITE;
/*!40000 ALTER TABLE `trainer` DISABLE KEYS */;
/*!40000 ALTER TABLE `trainer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_csivo9yqa08nrbkog71ycilh5` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount`
--

LOCK TABLES `useraccount` WRITE;
/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` VALUES (1,0,'21232f297a57a5a743894a0e4a801fc3','admin');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount_authorities`
--

DROP TABLE IF EXISTS `useraccount_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount_authorities` (
  `UserAccount_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_b63ua47r0u1m7ccc9lte2ui4r` (`UserAccount_id`),
  CONSTRAINT `FK_b63ua47r0u1m7ccc9lte2ui4r` FOREIGN KEY (`UserAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount_authorities`
--

LOCK TABLES `useraccount_authorities` WRITE;
/*!40000 ALTER TABLE `useraccount_authorities` DISABLE KEYS */;
INSERT INTO `useraccount_authorities` VALUES (1,'ADMIN');
/*!40000 ALTER TABLE `useraccount_authorities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-04 18:23:48

commit;