-- --------------------------------------------------------
-- Hostitel:                     127.0.0.1
-- Verze serveru:                5.7.24 - MySQL Community Server (GPL)
-- OS serveru:                   Win64
-- HeidiSQL Verze:               10.2.0.5599
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Exportování struktury databáze pro
DROP DATABASE IF EXISTS `notebook`;
CREATE DATABASE IF NOT EXISTS `notebook` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `notebook`;

-- Exportování struktury pro tabulka notebook.category
DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `tag` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Exportování dat pro tabulku notebook.category: ~5 rows (přibližně)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` (`id`, `tag`) VALUES
	(1, 'lékárna'),
	(2, 'oblečení'),
	(3, 'lékař'),
	(4, 'Olča'),
	(10, 'hygiena');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;

-- Exportování struktury pro tabulka notebook.todo_item
DROP TABLE IF EXISTS `todo_item`;
CREATE TABLE IF NOT EXISTS `todo_item` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `text` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0',
  `author` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Olinka',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deadline` timestamp NOT NULL,
  `fulfilled` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Exportování dat pro tabulku notebook.todo_item: ~3 rows (přibližně)
/*!40000 ALTER TABLE `todo_item` DISABLE KEYS */;
INSERT INTO `todo_item` (`id`, `text`, `author`, `created_at`, `updated_at`, `deadline`, `fulfilled`) VALUES
	(1, 'Sehnat podprsenku!!!', 'Olinka', '2020-10-07 21:41:59', '2020-10-07 21:41:59', '2020-10-20 21:00:00', 1),
	(2, 'Vyšetření ve dvacátém týdnu :-)', 'Olinka', '2020-10-17 23:25:28', '2020-10-23 21:59:42', '2020-10-22 23:25:18', 1),
	(3, 'koupit druhé kalhoty - tentokrát velikosti 44!', 'Olinka', '2020-10-20 17:05:08', '2020-10-23 22:58:10', '2020-11-20 17:04:58', 0);
/*!40000 ALTER TABLE `todo_item` ENABLE KEYS */;

-- Exportování struktury pro tabulka notebook.todo_item_category
DROP TABLE IF EXISTS `todo_item_category`;
CREATE TABLE IF NOT EXISTS `todo_item_category` (
  `item_id` int(10) unsigned NOT NULL,
  `category_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`item_id`,`category_id`),
  KEY `FK_todo_item-category_category` (`category_id`),
  CONSTRAINT `FK_todo_item-category_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_todo_item-category_todo_item` FOREIGN KEY (`item_id`) REFERENCES `todo_item` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Exportování dat pro tabulku notebook.todo_item_category: ~6 rows (přibližně)
/*!40000 ALTER TABLE `todo_item_category` DISABLE KEYS */;
INSERT INTO `todo_item_category` (`item_id`, `category_id`) VALUES
	(1, 2),
	(3, 2),
	(2, 3),
	(1, 4),
	(2, 4),
	(3, 4);
/*!40000 ALTER TABLE `todo_item_category` ENABLE KEYS */;

-- Exportování struktury pro trigger notebook.todo_item_before_update
DROP TRIGGER IF EXISTS `todo_item_before_update`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `todo_item_before_update` BEFORE UPDATE ON `todo_item` FOR EACH ROW SET NEW.updated_at = NOW()//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
