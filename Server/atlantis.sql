-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mer 22 Juillet 2015 à 22:18
-- Version du serveur: 5.5.43
-- Version de PHP: 5.4.39-0+deb7u2
--
-- Atlantis - Nawras GEORGI
-- v0.1 - July 2015

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

CREATE TABLE IF NOT EXISTS `at_cameras` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(20) NOT NULL,
  `type` varchar(100) NOT NULL,
  `image` varchar(200) NOT NULL,
  `video` varchar(200) DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `alias` varchar(150) DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ip` (`ip`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_courses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `quantity` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_cuisine` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `element` varchar(200) NOT NULL,
  `peremption` date NOT NULL,
  `quantite` int(11) NOT NULL,
  `endroit` varchar(50) DEFAULT NULL,
  `status` tinyint(11) NOT NULL,
  `date2` date NOT NULL DEFAULT '0000-00-00',
  `avoid` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_ean` (
  `ean` varchar(15) NOT NULL,
  `nom` varchar(200) NOT NULL,
  PRIMARY KEY (`ean`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_pharmacie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(200) NOT NULL,
  `peremption` date NOT NULL,
  `quantite` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_entretien` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(200) NOT NULL,
  `peremption` date NOT NULL,
  `quantite` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `type` tinyint(4) NOT NULL,
  `mail` varchar(200) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `cle` varchar(20) NOT NULL,
  `password` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nom` (`nom`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `ip` varchar(16) NOT NULL,
  `mac` varchar(17) NOT NULL,
  `dns` varchar(100) DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  `type` varchar(50) NOT NULL,
  `connexion` varchar(50) NOT NULL,
  `note` varchar(200) DEFAULT NULL,
  `username` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mac` (`mac`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_lights` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `protocol` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `ip` varchar(25) DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  `uid` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_music` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file` varchar(250) NOT NULL,
  `length` varchar(10) DEFAULT NULL,
  `album` varchar(100) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `file` (`file`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_playlists` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_sensors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sensor` varchar(100) NOT NULL,
  `device` varchar(100) NOT NULL,
  `protocol` varchar(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `unit` varchar(10) NOT NULL,
  `history` int(11) NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `ignore` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sensor` (`sensor`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_sensors_devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device` varchar(100) NOT NULL,
  `alias` varchar(100) DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device` (`device`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_sensors_values` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sensor` int(11) NOT NULL,
  `value` double NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sensor` (`sensor`,`date`,`time`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_sensors_scenarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sensor` int(11) NOT NULL,
  `scenario` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sensor` (`sensor`,`scenario`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_songsplaylists` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playlist` int(11) NOT NULL,
  `song` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `playlist` (`playlist`,`song`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_switches` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `switch` int(11) NOT NULL,
  `sensor` int(11) NOT NULL,
  `type` varchar(20) NOT NULL,
  `action` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `switch` (`switch`,`sensor`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_welcome` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mac` varchar(20) NOT NULL,
  `music` int(200) NOT NULL,
  `jour` date NOT NULL,
  `heure` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_plants` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sensor` varchar(50) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `picture` text,
  `color` varchar(20) DEFAULT NULL,
  `room` int(11) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sensor` (`sensor`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `at_plants_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sensor` int(11) NOT NULL,
  `battery` int(11) NOT NULL,
  `light` double NOT NULL,
  `soil_conductivity` double NOT NULL,
  `soil_temperature` double NOT NULL,
  `air_temperature` double NOT NULL,
  `moisture` double NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `at_gcm` (
  `mac` varchar(17) NOT NULL,
  `gcm` text NOT NULL,
  UNIQUE KEY `mac` (`mac`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `at_geo` (
  `mac` varchar(17) NOT NULL,
  `lat` double NOT NULL,
  `long` double NOT NULL,
  `date` date NOT NULL,
  `time` time NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
