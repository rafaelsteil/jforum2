-- Banlist
ALTER TABLE jforum_banlist CHANGE banlist_id banlist_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_banlist CHANGE user_id user_id INT NOT NULL;

-- Categories
ALTER TABLE jforum_categories CHANGE categories_id categories_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_categories CHANGE display_order display_order INT NOT NULL;

-- Forums
ALTER TABLE jforum_forums CHANGE forum_id forum_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_forums CHANGE categories_id categories_id INT NOT NULL;
ALTER TABLE jforum_forums CHANGE forum_order forum_order INT DEFAULT '1';
ALTER TABLE jforum_forums CHANGE forum_topics forum_topics INT NOT NULL;
ALTER TABLE jforum_forums CHANGE forum_last_post_id forum_last_post_id INT NOT NULL;

CREATE INDEX idx_forums_cats ON jforum_forums(categories_id);

-- Groups
ALTER TABLE jforum_groups CHANGE group_id group_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_groups CHANGE parent_id parent_id INT NOT NULL;

-- Roles
ALTER TABLE jforum_roles CHANGE group_id group_id INT DEFAULT '0';

-- Posts
ALTER TABLE jforum_posts CHANGE post_id post_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_posts CHANGE topic_id topic_id INT NOT NULL;
ALTER TABLE jforum_posts CHANGE forum_id forum_id INT NOT NULL;
ALTER TABLE jforum_posts CHANGE user_id user_id INT NOT NULL;
ALTER TABLE jforum_posts CHANGE post_edit_count post_edit_count INT;

-- Posts Text
ALTER TABLE jforum_posts_text CHANGE post_id post_id INT NOT NULL;

-- Privmsgs
ALTER TABLE jforum_privmsgs CHANGE privmsgs_id privmsgs_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_privmsgs CHANGE privmsgs_from_userid privmsgs_from_userid INT NOT NULL;
ALTER TABLE jforum_privmsgs CHANGE privmsgs_to_userid privmsgs_to_userid INT NOT NULL;

-- Privmsgs Text
ALTER TABLE jforum_privmsgs_text CHANGE privmsgs_id privmsgs_id INT NOT NULL;

-- Ranks
ALTER TABLE jforum_ranks CHANGE rank_id rank_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_ranks CHANGE rank_min rank_min INT NOT NULL;

-- Sessions
ALTER TABLE jforum_sessions CHANGE session_user_id session_user_id INT NOT NULL;
ALTER TABLE jforum_sessions CHANGE session_logged_int session_logged_int TINYINT(1);

CREATE INDEX idx_sessions_users ON jforum_sessions(session_user_id) ;

-- Smilies
ALTER TABLE jforum_smilies CHANGE smilie_id smilie_id INT NOT NULL AUTO_INCREMENT;

-- Themes
ALTER TABLE jforum_themes CHANGE themes_id themes_id INT NOT NULL AUTO_INCREMENT;

-- Topics
ALTER TABLE jforum_topics CHANGE topic_id topic_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_topics CHANGE forum_id forum_id INT NOT NULL;
ALTER TABLE jforum_topics CHANGE user_id user_id INT NOT NULL;
ALTER TABLE jforum_topics CHANGE topic_views topic_views INT DEFAULT '1';
ALTER TABLE jforum_topics CHANGE topic_replies topic_replies INT DEFAULT '0';
ALTER TABLE jforum_topics CHANGE topic_vote topic_vote_id INT DEFAULT '0';
ALTER TABLE jforum_topics CHANGE topic_first_post_id topic_first_post_id INT NOT NULL;
ALTER TABLE jforum_topics CHANGE topic_last_post_id topic_last_post_id INT NOT NULL;

-- Topics Watch
ALTER TABLE jforum_topics_watch CHANGE topic_id topic_id INT NOT NULL;
ALTER TABLE jforum_topics_watch CHANGE user_id user_id INT NOT NULL;

-- Users
ALTER TABLE jforum_users CHANGE user_id user_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_users CHANGE user_session_page user_session_page INT;
ALTER TABLE jforum_users CHANGE user_posts user_posts INT DEFAULT '0';
ALTER TABLE jforum_users CHANGE user_new_privmsg user_new_privmsg INT DEFAULT '0';
ALTER TABLE jforum_users CHANGE user_unread_privmsg user_unread_privmsg INT DEFAULT '0';
ALTER TABLE jforum_users CHANGE rank_id rank_id INT DEFAULT '1';
ALTER TABLE jforum_users CHANGE user_website user_website VARCHAR(255);
ALTER TABLE jforum_users ADD user_biography TEXT DEFAULT NULL;
ALTER TABLE jforum_users CHANGE themes_id themes_id INT;

-- Vote Desc
ALTER TABLE jforum_vote_desc CHANGE vote_id vote_id INT NOT NULL AUTO_INCREMENT;
ALTER TABLE jforum_vote_desc CHANGE topic_id topic_id INT NOT NULL;
ALTER TABLE jforum_vote_desc CHANGE vote_text vote_text VARCHAR(255) NOT NULL;
ALTER TABLE jforum_vote_desc CHANGE vote_start vote_start DATETIME NOT NULL;

-- Vote Results
ALTER TABLE jforum_vote_results CHANGE vote_id vote_id INT NOT NULL;

-- Vote Voters
ALTER TABLE jforum_vote_voters CHANGE vote_id vote_id INT NOT NULL;
ALTER TABLE jforum_vote_voters CHANGE vote_user_id vote_user_id INT NOT NULL;
ALTER TABLE jforum_vote_voters CHANGE vote_user_ip vote_user_ip VARCHAR(15);

-- Words
ALTER TABLE jforum_words CHANGE word_id word_id INT NOT NULL AUTO_INCREMENT;

-- Search Results
ALTER TABLE jforum_search_results CHANGE session session_id VARCHAR(50);

-- Search Topics
ALTER TABLE jforum_search_topics CHANGE topic_id topic_id INT NOT NULL;
ALTER TABLE jforum_search_topics CHANGE forum_id forum_id INT NOT NULL;
ALTER TABLE jforum_search_topics CHANGE user_id user_id INT NOT NULL;
ALTER TABLE jforum_search_topics CHANGE topic_views topic_views INT;
ALTER TABLE jforum_search_topics CHANGE topic_replies topic_replies INT;
ALTER TABLE jforum_search_topics CHANGE topic_vote topic_vote_id INT;
ALTER TABLE jforum_search_topics CHANGE topic_first_post_id topic_first_post_id INT;
ALTER TABLE jforum_search_topics CHANGE topic_last_post_id topic_last_post_id INT;
ALTER TABLE jforum_search_topics CHANGE session session_id VARCHAR(50);
ALTER TABLE jforum_search_topics CHANGE topic_title topic_title VARCHAR(100);

-- Banner
DROP TABLE IF EXISTS jforum_banner;
CREATE TABLE jforum_banner (
	banner_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	banner_name VARCHAR(90),
	banner_placement INT NOT NULL DEFAULT '0',
	banner_description VARCHAR(250),
	banner_clicks INT NOT NULL DEFAULT '0',
	banner_views INT NOT NULL DEFAULT '0',
	banner_url VARCHAR(250),
	banner_weight TINYINT(1) NOT NULL DEFAULT '50',
	banner_active TINYINT(1) NOT NULL DEFAULT '0',
	banner_comment VARCHAR(250),
	banner_type INT NOT NULL DEFAULT '0',
	banner_width INT NOT NULL DEFAULT '0',
	banner_height INT NOT NULL DEFAULT '0',
	KEY(banner_id)
) TYPE=InnoDB;

--
-- Table structure for table 'jforum_forums_watch'
--
DROP TABLE IF EXISTS jforum_forums_watch;
CREATE TABLE jforum_forums_watch (
  forum_id INT NOT NULL,
  user_id INT NOT NULL,
  is_read tinyint(1) DEFAULT 1,
  INDEX idx_fw_forum (forum_id),
  INDEX idx_fw_user (user_id)
) TYPE=InnoDB;
