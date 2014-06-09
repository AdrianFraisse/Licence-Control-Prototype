-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Dim 08 Juin 2014 à 14:57
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

DROP TABLE IF EXISTS `builds`;
CREATE TABLE IF NOT EXISTS `builds` (
  `id_build` varchar(255) NOT NULL,
  `build_checksum` varchar(255) DEFAULT NULL COMMENT 'Checksum du jar avec cet id de build',
  PRIMARY KEY (`id_build`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `licences`
--

DROP TABLE IF EXISTS `licences`;
CREATE TABLE IF NOT EXISTS `licences` (
  `licence` varchar(255) NOT NULL COMMENT 'Texte de la licence',
  `id_build` varchar(255) NOT NULL COMMENT 'Id du build associé à la licence',
  `nb_users_max` int(11) NOT NULL COMMENT 'Nombres d''utilisateurs maximum de la licence',
  PRIMARY KEY (`licence`),
  UNIQUE KEY `id_build` (`id_build`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `session`
--

DROP TABLE IF EXISTS `session`;
CREATE TABLE IF NOT EXISTS `session` (
  `licence` varchar(255) NOT NULL COMMENT 'Licence liée à la clé',
  `session_key` varchar(255) NOT NULL COMMENT 'Texte de la clé',
  `expiration_date` datetime NOT NULL COMMENT 'Date d''expiration de la clé',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `licence` (`licence`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Table des clés temporaires' AUTO_INCREMENT=1 ;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `licences`
--
ALTER TABLE `licences`
  ADD CONSTRAINT `licences_ibfk_1` FOREIGN KEY (`id_build`) REFERENCES `builds` (`id_build`);

--
-- Contraintes pour la table `session`
--
ALTER TABLE `session`
  ADD CONSTRAINT `session_ibfk_1` FOREIGN KEY (`licence`) REFERENCES `licences` (`licence`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
