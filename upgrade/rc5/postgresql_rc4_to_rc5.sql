CREATE TABLE jforum_posts_text (
	post_id INTEGER NOT NULL,
	post_text TEXT,
	post_subject VARCHAR(100) DEFAULT NULL,
	PRIMARY KEY ( post_id )
);

INSERT INTO jforum_posts_text ( post_id, post_text, post_subject ) SELECT post_id, post_text, post_subject FROM jforum_posts;

ALTER TABLE jforum_posts DROP COLUMN post_subject;
ALTER TABLE jforum_posts DROP COLUMN post_text;

CREATE TABLE jforum_privmsgs_text (
	privmsgs_id INTEGER NOT NULL,
	privmsgs_text TEXT
);
CREATE INDEX idx_pm_text_id ON jforum_privmsgs_text (privmsgs_id);

INSERT INTO jforum_privmsgs_text ( privmsgs_id, privmsgs_text ) SELECT privmsgs_id, privmsgs_text FROM jforum_privmsgs;

ALTER TABLE jforum_privmsgs DROP COLUMN privmsgs_text;

DROP TABLE jforum_resutls;
CREATE TABLE jforum_vote_results (
  vote_id INTEGER NOT NULL DEFAULT 0,
  vote_option_id INTEGER NOT NULL DEFAULT 0,
  vote_option_text VARCHAR(255) NOT NULL DEFAULT '',
  vote_result INTEGER NOT NULL DEFAULT 0
);