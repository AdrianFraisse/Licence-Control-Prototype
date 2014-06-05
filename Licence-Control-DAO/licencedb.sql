-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Jeu 05 Juin 2014 à 16:25
-- Version du serveur: 5.5.24-log
-- Version de PHP: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `licencedb`
--

-- --------------------------------------------------------

--
-- Structure de la table `builds`
--

CREATE TABLE IF NOT EXISTS `builds` (
  `id_build` varchar(255) NOT NULL,
  `build_checksum` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_build`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `licences`
--

CREATE TABLE IF NOT EXISTS `licences` (
  `licence` varchar(255) NOT NULL,
  `id_build` varchar(255) NOT NULL,
  PRIMARY KEY (`licence`),
  UNIQUE KEY `id_build` (`id_build`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `licences`
--
ALTER TABLE `licences`
  ADD CONSTRAINT `licences_ibfk_1` FOREIGN KEY (`id_build`) REFERENCES `builds` (`id_build`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
