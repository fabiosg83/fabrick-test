-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versione server:              5.6.16-1~exp1 - (Ubuntu)
-- S.O. server:                  debian-linux-gnu
-- HeidiSQL Versione:            10.1.0.5464
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dump della struttura del database fabrick_test
CREATE DATABASE IF NOT EXISTS `fabrick_test` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `fabrick_test`;

-- Dump della struttura di tabella fabrick_test.operations
CREATE TABLE IF NOT EXISTS `operations` (
  `progressive_id` int(11) NOT NULL AUTO_INCREMENT,
  `apikey` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `route` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `dt_call` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`progressive_id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- L’esportazione dei dati non era selezionata.
-- Dump della struttura di tabella fabrick_test.payments
CREATE TABLE IF NOT EXISTS `payments` (
  `progressive_id` int(11) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `creditor_name` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `creditor_account_code` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 NOT NULL,
  `amount` double NOT NULL,
  `currency` enum('EUR','USD','OTHER') CHARACTER SET utf8 NOT NULL,
  `status` tinyint(2) NOT NULL DEFAULT '0',
  `dt_execution` date NOT NULL,
  `dt_update` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`progressive_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


