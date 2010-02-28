---
--- Posts
---
DROP TABLE IF EXISTS jforum_posts_text;
CREATE TABLE jforum_posts_text (
	post_id MEDIUMINT(8) NOT NULL PRIMARY KEY,
	post_text TEXT,
	post_subject VARCHAR(100)
) TYPE=InnoDB;

INSERT INTO jforum_posts_text (post_id, post_text, post_subject) SELECT post_id, post_text, post_subject FROM jforum_posts;

ALTER TABLE jforum_posts DROP COLUMN post_subject;
ALTER TABLE jforum_posts DROP COLUMN post_text;

--
-- Private Messages
--
DROP TABLE IF EXISTS jforum_privmsgs_text;
CREATE TABLE jforum_privmsgs_text (
	privmsgs_id MEDIUMINT(8) NOT NULL,
	privmsgs_text TEXT,
	PRIMARY KEY ( privmsgs_id )
) Type=InnoDB;

INSERT INTO jforum_privmsgs_text ( privmsgs_id, privmsgs_text ) SELECT privmsgs_id, privmsgs_text FROM jforum_privmsgs;

ALTER TABLE jforum_privmsgs DROP COLUMN privmsgs_text;

--
-- Votes
--
ALTER TABLE jforum_vote_resutls RENAME jforum_vote_results;

--
-- Tables Type
--
ALTER TABLE jforum_search_words RENAME jforum_tmp;
CREATE TABLE jforum_search_words (
  word_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  word VARCHAR(100) NOT NULL,
  word_hash INT,
  KEY(word),
  KEY(word_hash)
) TYPE=InnoDB;
INSERT INTO jforum_search_words ( word_id, word, word_hash ) SELECT word_id, word, word_hash FROM jforum_tmp;
DROP TABLE jforum_tmp;

ALTER TABLE jforum_search_wordmatch RENAME jforum_tmp;
CREATE TABLE jforum_search_wordmatch (
  post_id INT NOT NULL,
  word_id INT NOT NULL,
  title_match TINYINT(1) DEFAULT '0',
  KEY(post_id),
  KEY(word_id),
  KEY(title_match)
) TYPE=InnoDB;
INSERT INTO jforum_search_wordmatch ( post_id, word_id, title_match ) SELECT post_id, word_id, title_match FROM jforum_tmp;
DROP TABLE jforum_tmp;

ALTER TABLE jforum_search_results RENAME jforum_tmp;
CREATE TABLE jforum_search_results (
  topic_id INT NOT NULL,
  session VARCHAR(50),
  time DATETIME,
  KEY (topic_id)
) TYPE=InnoDB;
INSERT INTO jforum_search_results ( topic_id, session, time ) SELECT topic_id, session, time FROM jforum_tmp;
DROP TABLE jforum_tmp;