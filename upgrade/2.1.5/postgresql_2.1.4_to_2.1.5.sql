-- Roles
ALTER TABLE jforum_roles DROP user_id;

-- Posts
ALTER TABLE jforum_posts ALTER user_id SET DEFAULT 0;

-- Topics
ALTER TABLE jforum_topics RENAME COLUMN topic_vote TO topic_vote_id;

-- Users
ALTER TABLE jforum_users ADD user_website_tmp VARCHAR(255);
UPDATE jforum_users SET user_website_tmp = user_website;
ALTER TABLE jforum_users DROP user_website;
ALTER TABLE jforum_users RENAME COLUMN user_website_tmp TO user_website;

ALTER TABLE jforum_users ADD user_biography TEXT;

-- Vote Desc
ALTER TABLE jforum_vote_desc DROP vote_text;
ALTER TABLE jforum_vote_desc ADD vote_text VARCHAR(255);

ALTER TABLE jforum_vote_desc DROP vote_start;
ALTER TABLE jforum_vote_desc ADD vote_start TIMESTAMP;

-- Vote Voters
ALTER TABLE jforum_vote_voters DROP vote_user_ip;
ALTER TABLE jforum_vote_voters ADD vote_user_ip VARCHAR(15);

-- Search Results
ALTER TABLE jforum_search_results RENAME COLUMN session TO session_id;

-- Search Topics
ALTER TABLE jforum_search_topics RENAME COLUMN topic_vote TO topic_vote_id;
ALTER TABLE jforum_search_topics RENAME COLUMN session TO session_id;

-- Banner
CREATE SEQUENCE jforum_banner_seq;
CREATE TABLE jforum_banner (
	banner_id INTEGER NOT NULL DEFAULT NEXTVAL('jforum_banner_seq'),
	banner_name VARCHAR(90),
	banner_placement INTEGER NOT NULL DEFAULT 0,
	banner_description VARCHAR(250),
	banner_clicks INTEGER NOT NULL DEFAULT 0,
	banner_views INTEGER NOT NULL DEFAULT 0,
	banner_url VARCHAR(250),
	banner_weight INTEGER NOT NULL DEFAULT 50,
	banner_active INTEGER NOT NULL DEFAULT 0,
	banner_comment VARCHAR(250),
	banner_type INTEGER NOT NULL DEFAULT 0,
	banner_width INTEGER NOT NULL DEFAULT 0,
	banner_height INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY(banner_id)
);

--
-- Table structure for table 'jforum_forums_watch'
--
DROP TABLE IF EXISTS jforum_forums_watch;
CREATE TABLE jforum_forums_watch (
  forum_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  is_read INTEGER DEFAULT 1
);
CREATE INDEX idx_fw_forum ON jforum_forums_watch(forum_id);
CREATE INDEX idx_fw_user ON jforum_forums_watch(user_id);
