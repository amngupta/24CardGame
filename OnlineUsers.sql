-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: sophia
-- Generation Time: May 20, 2018 at 03:30 PM
-- Server version: 5.1.35
-- PHP Version: 5.5.9-1ubuntu4.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `agupta2`
--

-- --------------------------------------------------------

--
-- Table structure for table `OnlineUsers`
--

CREATE TABLE IF NOT EXISTS `OnlineUsers` (
  `index` int(11) NOT NULL AUTO_INCREMENT,
  `userkey` int(11) NOT NULL,
  `loginTime` date NOT NULL,
  PRIMARY KEY (`index`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
