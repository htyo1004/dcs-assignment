-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Dec 15, 2013 at 10:32 AM
-- Server version: 5.5.32
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `bank`
--
DROP DATABASE IF EXISTS `bank`;
CREATE DATABASE IF NOT EXISTS `bank` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `bank`;

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `aid` smallint(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `accNo` char(14) DEFAULT NULL,
  `accType` varchar(10) DEFAULT NULL,
  `balance` double(10,2) DEFAULT '0.00',
  `withdrawLimit` double(6,2) DEFAULT '1000.00',
  `accHolder` char(12) DEFAULT NULL,
  `dateCreated` date DEFAULT NULL,
  `bid` smallint(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`aid`),
  KEY `FK_AccOwner` (`accHolder`),
  KEY `FK_AccBranch` (`bid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`aid`, `accNo`, `accType`, `balance`, `withdrawLimit`, `accHolder`, `dateCreated`, `bid`) VALUES
(0000000001, '66490000000001', 'Saving', 118200.60, 9999.99, '910904085055', '2013-12-01', 1),
(0000000002, '66490000000002', 'Saving', 2500.00, 9999.99, '911025085428', '2013-12-04', 1),
(0000000003, '66490000000003', 'Saving', 2500.00, 9999.99, '911012085523', '2013-12-04', 1),
(0000000004, '66490000000004', 'Saving', 0.00, 9999.99, '323213213213', '2013-12-04', 1);

-- --------------------------------------------------------

--
-- Table structure for table `accountholder`
--

DROP TABLE IF EXISTS `accountholder`;
CREATE TABLE IF NOT EXISTS `accountholder` (
  `accHolder` char(12) NOT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `state` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `postCode` char(5) DEFAULT NULL,
  `contactNo` char(10) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`accHolder`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `accountholder`
--

INSERT INTO `accountholder` (`accHolder`, `firstname`, `lastname`, `gender`, `address`, `state`, `city`, `postCode`, `contactNo`, `email`) VALUES
('323213213213', 'qweqweqe', 'qweqweqwe', 'M', '192.168.0.105', 'Perak', 'qeqweqe', '32000', '3213213213', 'qwe@'),
('910904085055', 'Kenny', 'Woon', 'M', '105, Persiaran bekor 2, Taman Pertama', 'Perak', 'Ipoh', '30100', '0165641801', 'maydayrains@hotmail.com'),
('911012085523', 'Henny', 'Kong', 'M', '115, Pergunsin 245, Jalan Uma', 'Perak', 'Ipoh', '39120', '0165841215', 'himyfairlady@gmail.com'),
('911025085428', 'Jenny', 'Wong', 'F', '15, Persin 25, Jalan Utama', 'Perak', 'Ipoh', '35120', '0165845215', 'hioriae@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `branch`
--

DROP TABLE IF EXISTS `branch`;
CREATE TABLE IF NOT EXISTS `branch` (
  `bid` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `branchCode` char(4) DEFAULT NULL,
  `ipAddress` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`bid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `branch`
--

INSERT INTO `branch` (`bid`, `branchCode`, `ipAddress`) VALUES
(1, '6649', '192.168.0.105'),
(2, '6650', '192.168.0.104'),
(3, '6651', '192.168.0.112'),
(4, '6652', '192.168.0.115');

-- --------------------------------------------------------

--
-- Table structure for table `loan`
--

DROP TABLE IF EXISTS `loan`;
CREATE TABLE IF NOT EXISTS `loan` (
  `lid` smallint(10) NOT NULL AUTO_INCREMENT,
  `loanType` varchar(30) DEFAULT NULL,
  `loanAmount` double(10,2) DEFAULT '0.00',
  `interestRate` double(4,2) DEFAULT '0.00',
  `duration` varchar(20) DEFAULT NULL,
  `loanerName` varchar(50) DEFAULT NULL,
  `loanerIc` char(12) DEFAULT NULL,
  `loanerContact` char(10) DEFAULT NULL,
  PRIMARY KEY (`lid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `transactionlog`
--

DROP TABLE IF EXISTS `transactionlog`;
CREATE TABLE IF NOT EXISTS `transactionlog` (
  `tid` smallint(10) unsigned NOT NULL AUTO_INCREMENT,
  `transactionType` varchar(10) DEFAULT NULL,
  `amount` double(10,2) DEFAULT '0.00',
  `transactionDate` date DEFAULT NULL,
  `aid` smallint(10) unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`tid`),
  KEY `FK_AccTransaction` (`aid`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=76 ;

--
-- Dumping data for table `transactionlog`
--

INSERT INTO `transactionlog` (`tid`, `transactionType`, `amount`, `transactionDate`, `aid`) VALUES
(1, 'CRD', 1500.00, '2013-10-01', 0000000001),
(2, 'CRD', 156.00, '2013-10-03', 0000000003),
(3, 'DBT', 256.00, '2013-10-24', 0000000001),
(4, 'DBT', 214.50, '2013-11-15', 0000000001),
(5, 'CRD', 365.12, '2013-11-24', 0000000003),
(6, 'CRD', 253.23, '2013-12-04', 0000000001),
(7, 'CRD', 253.23, '2013-12-04', 0000000003),
(8, 'CRD', 253.23, '2013-12-04', 0000000001),
(9, 'CRD', 253.23, '2013-12-04', 0000000001),
(10, 'CRD', 253.23, '2013-12-04', 0000000001),
(11, 'DBT', 253.23, '2013-12-04', 0000000003),
(12, 'DBT', 222.00, '2013-12-04', 0000000001),
(13, 'DBT', 25.00, '2013-12-04', 0000000001),
(14, 'DBT', 25.00, '2013-12-04', 0000000001),
(15, 'DBT', 25.00, '2013-12-04', 0000000003),
(16, 'DBT', 100.50, '2013-12-04', 0000000001),
(17, 'DBT', 50.10, '2013-12-04', 0000000001),
(18, 'DBT', 59.00, '2013-12-04', 0000000001),
(19, 'DBT', 20.00, '2013-12-04', 0000000001),
(20, 'DBT', 266.00, '2013-12-04', 0000000001),
(21, 'DBT', 35.00, '2013-12-04', 0000000001),
(22, 'DBT', 35.00, '2013-12-04', 0000000001),
(23, 'DBT', 16.00, '2013-12-04', 0000000001),
(24, 'DBT', 25.00, '2013-12-05', 0000000003),
(25, 'DBT', 25.00, '2013-12-05', 0000000001),
(26, 'DBT', 2354.00, '2013-12-05', 0000000001),
(27, 'DBT', 2500.00, '2013-12-05', 0000000002),
(28, 'DBT', 2500.00, '2013-12-05', 0000000003),
(29, 'DBT', 2.00, '2013-12-06', 0000000001),
(30, 'CRD', 2.00, '2013-12-06', 0000000001),
(31, 'DBT', 22.00, '2013-12-06', 0000000001),
(32, 'CRD', 58462.00, '2013-12-06', 0000000002),
(33, 'DBT', 220.00, '2013-12-06', 0000000002),
(34, 'DBT', 220.00, '2013-12-06', 0000000001),
(35, 'DBT', 220.00, '2013-12-06', 0000000002),
(36, 'DBT', 220.00, '2013-12-06', 0000000001),
(37, 'DBT', 220.00, '2013-12-06', 0000000002),
(38, 'CRD', 58462.00, '2013-12-06', 0000000001),
(39, 'DBT', 25.00, '2013-12-13', 0000000001),
(40, 'CRD', 2.00, '2013-12-13', 0000000001),
(41, 'CRD', 2.00, '2013-12-13', 0000000001),
(42, 'CRD', 25.00, '2013-12-13', 0000000001),
(43, 'CRD', 25.00, '2013-12-13', 0000000001),
(44, 'CRD', 5.00, '2013-12-13', 0000000003),
(45, 'CRD', 25.00, '2013-12-13', 0000000001),
(46, 'CRD', 5.00, '2013-12-13', 0000000002),
(47, 'DBT', 25.00, '2013-12-13', 0000000003),
(48, 'DBT', 25.00, '2013-12-13', 0000000001),
(49, 'DBT', 25.00, '2013-12-13', 0000000003),
(50, 'CRD', 20.00, '2013-12-13', 0000000001),
(51, 'CRD', 20.00, '2013-12-13', 0000000001),
(52, 'CRD', 20.00, '2013-12-13', 0000000003),
(53, 'CRD', 20.00, '2013-12-13', 0000000002),
(54, 'CRD', 20.00, '2013-12-13', 0000000001),
(55, 'DBT', 50.00, '2013-12-15', 0000000001),
(56, 'DBT', 25.00, '2013-12-15', 0000000003),
(57, 'DBT', 50.00, '2013-12-15', 0000000001),
(58, 'DBT', 50.00, '2013-12-15', 0000000003),
(59, 'DBT', 100.00, '2013-12-15', 0000000001),
(60, 'DBT', 50.00, '2013-12-15', 0000000003),
(61, 'DBT', 50.00, '2013-12-15', 0000000001),
(62, 'DBT', 50.00, '2013-12-15', 0000000001),
(63, 'DBT', 50.00, '2013-12-15', 0000000002),
(64, 'DBT', 50.00, '2013-12-15', 0000000003),
(65, 'DBT', 50.00, '2013-12-15', 0000000001),
(66, 'DBT', 50.00, '2013-12-15', 0000000002),
(67, 'DBT', 50.00, '2013-12-15', 0000000001),
(68, 'DBT', 50.00, '2013-12-15', 0000000002),
(69, 'DBT', 50.00, '2013-12-15', 0000000001),
(70, 'DBT', 50.00, '2013-12-15', 0000000002),
(71, 'DBT', 50.00, '2013-12-15', 0000000001),
(72, 'DBT', 50.00, '2013-12-15', 0000000001),
(73, 'DBT', 50.00, '2013-12-15', 0000000002),
(74, 'DBT', 50.00, '2013-12-15', 0000000003),
(75, 'DBT', 50.00, '2013-12-15', 0000000001);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `FK_AccBranch` FOREIGN KEY (`bid`) REFERENCES `branch` (`bid`),
  ADD CONSTRAINT `FK_AccOwner` FOREIGN KEY (`accHolder`) REFERENCES `accountholder` (`accHolder`);

--
-- Constraints for table `transactionlog`
--
ALTER TABLE `transactionlog`
  ADD CONSTRAINT `FK_AccTransaction` FOREIGN KEY (`aid`) REFERENCES `account` (`aid`);
