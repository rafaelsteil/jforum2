CREATE INDEX idx_banlist_email ON jforum_banlist(banlist_email);
CREATE INDEX idx_posts_moderate ON jforum_posts(need_moderate);
CREATE INDEX idx_posts_time ON jforum_posts(post_time);
ALTER TABLE jforum_topics ADD topic_moved_id INT;
ALTER TABLE jforum_topics ALTER COLUMN topic_moved_id SET DEFAULT 0;
CREATE INDEX idx_topics_moved ON jforum_topics(topic_moved_id);
ALTER TABLE jforum_users ALTER COLUMN rank_id SET DEFAULT 0;

ALTER TABLE jforum_sessions DROP session_ip;
ALTER TABLE jforum_sessions ADD session_ip VARCHAR(15);
ALTER TABLE jforum_sessions ALTER COLUMN session_ip SET DEFAULT '';
ALTER TABLE jforum_privmsgs DROP privmsgs_ip;
ALTER TABLE jforum_privmsgs ADD privmsgs_ip VARCHAR(15);
ALTER TABLE jforum_privmsgs ALTER COLUMN privmsgs_ip SET DEFAULT '';

CREATE INDEX idx_vd_topic ON jforum_vote_desc(topic_id);
CREATE INDEX idx_vr_vote ON jforum_vote_results(vote_id);
CREATE INDEX idx_vv_vote ON jforum_vote_voters(vote_id);
CREATE INDEX idx_vv_user ON jforum_vote_voters(vote_user_id);
CREATE INDEX idx_extensions_ext ON jforum_extensions(extension);

DROP TABLE jforum_search_words;
DROP TABLE jforum_search_wordmatch;
DROP TABLE jforum_search_results;
DROP TABLE jforum_search_topics;

CREATE SEQUENCE jforum_moderation_log_seq;
CREATE TABLE jforum_moderation_log (
	log_id INT NOT NULL PRIMARY KEY DEFAULT NEXTVAL('jforum_moderation_log_seq'),
	user_id INT NOT NULL,
	log_description TEXT NOT NULL,
	log_original_message TEXT,
	log_date timestamp NOT NULL,
	log_type INT DEFAULT 0,
	post_id INT DEFAULT 0,
	topic_id INT DEFAULT 0,
	post_user_id INT DEFAULT 0
);

CREATE INDEX idx_moderation_user ON jforum_moderation_log(user_id);
CREATE INDEX idx_moderation_pu ON jforum_moderation_log(post_user_id);

UPDATE jforum_forums SET forum_topics = (SELECT COUNT(*) FROM jforum_topics t WHERE t.forum_id = jforum_forums.forum_id AND moderated = 0);
