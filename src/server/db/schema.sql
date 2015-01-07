DROP TABLE IF EXISTS `audiotracks`;
CREATE TABLE `audiotracks` (
  `id` INTEGER PRIMARY KEY,
  `name` varchar(255) NOT NULL,
  `artist` varchar(255) DEFAULT NULL,
  `album` varchar(255) DEFAULT NULL,
  `filename` varchar(255) NOT NULL,
  `last_played` int(11) DEFAULT NULL
);

DROP TABLE IF EXISTS `queuelist`;
CREATE TABLE `queuelist` (
  `added` int(11) NOT NULL,
  `trackid` int(11) NOT NULL
);

DROP TABLE IF EXISTS `history`;
CREATE TABLE `history` (
  `played` int(11) NOT NULL,
  `trackid` int(11) NOT NULL
);

DROP TABLE IF EXISTS `settings`;
CREATE TABLE `settings` (
  `name` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL
);

DROP TABLE IF EXISTS `wishlist`;
CREATE TABLE `wishlist` (
  `id` INTEGER PRIMARY KEY,
  `title` varchar(255) NOT NULL,
  `artist` varchar(255) DEFAULT NULL,
  `album` varchar(255) DEFAULT NULL,
  `added` int(11) DEFAULT NULL
);
