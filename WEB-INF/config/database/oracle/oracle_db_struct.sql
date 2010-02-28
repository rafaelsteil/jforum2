 --
-- jforum_banlist
--
CREATE SEQUENCE jforum_banlist_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;
CREATE TABLE jforum_banlist (
    banlist_id NUMBER(10),
    user_id NUMBER(10) DEFAULT 0,
    banlist_ip VARCHAR2(20),
    banlist_email VARCHAR2(255),
    PRIMARY KEY(banlist_id)
);
CREATE INDEX idx_banlist_user ON jforum_banlist(user_id);
CREATE INDEX idx_banlist_ip ON jforum_banlist(banlist_ip);
CREATE INDEX idx_banlist_email ON jforum_banlist(banlist_email);

--
-- Table structure for table 'jforum_categories'
--
CREATE SEQUENCE jforum_categories_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_categories (
  categories_id NUMBER(10) NOT NULL,
  title VARCHAR2(100) DEFAULT ' ' NOT NULL ,
  display_order NUMBER(10) DEFAULT 0 NOT NULL,
  moderated NUMBER(10) DEFAULT 0,
  PRIMARY KEY(categories_id)
);

--
-- Table structure for table 'jforum_config'
--
CREATE SEQUENCE jforum_config_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_config (
  config_name VARCHAR2(255)  DEFAULT ' ' NOT NULL,
  config_value VARCHAR2(255) DEFAULT ' ' NOT NULL,
  config_id NUMBER(10) NOT NULL,
  PRIMARY KEY(config_id)
);

--
-- Table structure for table 'jforum_forums'
--

CREATE SEQUENCE jforum_forums_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_forums (
  forum_id NUMBER(10) NOT NULL,
  categories_id NUMBER(10)  DEFAULT 1 NOT NULL,
  forum_name VARCHAR2(150) DEFAULT ' ' NOT NULL,
  forum_desc VARCHAR2(255) DEFAULT ' ',
  forum_order NUMBER(10) DEFAULT 1,
  forum_topics NUMBER(10) DEFAULT 0 NOT NULL,
  forum_last_post_id NUMBER(10)  DEFAULT 0 NOT NULL,
  moderated NUMBER(10) DEFAULT 0,
  PRIMARY KEY (forum_id)
);
CREATE INDEX idx_forums_categories_id ON jforum_forums(categories_id);

--
-- Table structure for table 'jforum_forums_watch'
--
CREATE TABLE jforum_forums_watch (
  forum_id NUMBER NOT NULL,
  user_id NUMBER NOT NULL,
  is_read NUMBER(1) DEFAULT 1
);
CREATE INDEX idx_fw_forum ON jforum_forums_watch(forum_id);
CREATE INDEX idx_fw_user ON jforum_forums_watch(user_id);


--
-- Table structure for table 'jforum_groups'
--

CREATE SEQUENCE jforum_groups_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_groups (
  group_id NUMBER(10) NOT NULL,
  group_name VARCHAR2(40) DEFAULT ' ' NOT NULL,
  group_description VARCHAR2(255) DEFAULT NULL,
  parent_id NUMBER(10) DEFAULT 0,
  PRIMARY KEY (group_id)
);

ALTER TABLE jforum_forums ADD CONSTRAINT fk_jforum_categories FOREIGN KEY(categories_id)
	REFERENCES jforum_categories(categories_id);


CREATE TABLE jforum_user_groups (
	group_id NUMBER(10) NOT NULL,
	user_id NUMBER(10) NOT NULL
);
CREATE INDEX idx_ug_group ON jforum_user_groups(group_id);
CREATE INDEX idx_ug_user ON jforum_user_groups(user_id);

--
-- Table structure for table 'jforum_roles'
--

CREATE SEQUENCE jforum_roles_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_roles (
  role_id NUMBER(10) NOT NULL,
  group_id NUMBER(10) DEFAULT 0,
  name VARCHAR2(255) NOT NULL,
  PRIMARY KEY (role_id)
);

CREATE INDEX idx_roles_group ON jforum_roles(group_id);
CREATE INDEX idx_roles_name ON jforum_roles(name);

--
-- Table structure for table 'jforum_role_values'
--
CREATE TABLE jforum_role_values (
  role_id NUMBER(10) NOT NULL,
  role_value VARCHAR2(255)
);
CREATE INDEX idx_rv_role ON jforum_role_values(role_id);

--
-- Table structure for table 'jforum_posts'
--

CREATE SEQUENCE jforum_posts_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_posts (
  post_id NUMBER(10) NOT NULL,
  topic_id NUMBER(10) DEFAULT 0 NOT NULL,
  forum_id NUMBER(10) DEFAULT 0 NOT NULL,
  user_id NUMBER(10) DEFAULT 0 NOT NULL,
  post_time DATE DEFAULT NULL,
  poster_ip VARCHAR2(15) DEFAULT NULL,
  enable_bbcode NUMBER(10) DEFAULT 1 NOT NULL,
  enable_html NUMBER(10) DEFAULT 1 NOT NULL,
  enable_smilies NUMBER(10) DEFAULT 1 NOT NULL,
  enable_sig NUMBER(10) DEFAULT 1 NOT NULL,
  post_edit_time DATE DEFAULT NULL,
  post_edit_count NUMBER(10) DEFAULT 0 NOT NULL,
  status NUMBER(10) DEFAULT 1,
  attach NUMBER(1) DEFAULT 0,
  need_moderate NUMBER(1) DEFAULT 0,
  PRIMARY KEY (post_id)
);

CREATE INDEX idx_posts_user ON jforum_posts(user_id);
CREATE INDEX idx_posts_topic ON jforum_posts(topic_id);
CREATE INDEX idx_posts_forum ON jforum_posts(forum_id);
CREATE INDEX idx_posts_time ON jforum_posts(post_time);
CREATE INDEX idx_posts_moderate ON jforum_posts(need_moderate);

--
-- Table structure for table 'jforum_posts_text'
--
CREATE TABLE jforum_posts_text (
	post_id NUMBER(10) NOT NULL,
	post_text BLOB,
	post_subject VARCHAR2(100) DEFAULT NULL,
	PRIMARY KEY (post_id)
);

--
-- Table structure for table 'jforum_privmsgs'
--

CREATE SEQUENCE jforum_privmsgs_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_privmsgs (
  privmsgs_id NUMBER(10) NOT NULL,
  privmsgs_type NUMBER(10) DEFAULT 0 NOT NULL,
  privmsgs_subject VARCHAR2(255) DEFAULT ' ' NOT NULL ,
  privmsgs_from_userid NUMBER(10) DEFAULT 0 NOT NULL,
  privmsgs_to_userid NUMBER(10) DEFAULT 0 NOT NULL,
  privmsgs_date DATE DEFAULT SYSDATE NOT NULL,
  privmsgs_ip VARCHAR2(15) DEFAULT ' ' NOT NULL,
  privmsgs_enable_bbcode NUMBER(10) DEFAULT 1 NOT NULL,
  privmsgs_enable_html NUMBER(10) DEFAULT 0 NOT NULL,
  privmsgs_enable_smilies NUMBER(10) DEFAULT 1 NOT NULL,
  privmsgs_attach_sig NUMBER(10) DEFAULT 1 NOT NULL,
  PRIMARY KEY  (privmsgs_id)
);

CREATE TABLE jforum_privmsgs_text (
	privmsgs_id NUMBER(10) NOT NULL,
	privmsgs_text BLOB
);
CREATE INDEX idx_pm_text_id ON jforum_privmsgs_text (privmsgs_id);

--
-- Table structure for table 'jforum_ranks'
--

CREATE SEQUENCE jforum_ranks_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_ranks (
  rank_id NUMBER(10) NOT NULL,
  rank_title VARCHAR2(50) DEFAULT ' ' NOT NULL,
  rank_min NUMBER(10) DEFAULT 0 NOT NULL,
  rank_special NUMBER(10) DEFAULT NULL,
  rank_image VARCHAR2(255) DEFAULT NULL,
  PRIMARY KEY (rank_id)
);

--
-- Table structure for table 'jforum_sessions'
--

CREATE TABLE jforum_sessions (
  session_id VARCHAR2(150) DEFAULT ' ' NOT NULL,
  session_user_id NUMBER(10) DEFAULT 0,
  session_start DATE DEFAULT SYSDATE NOT NULL,
  session_time NUMBER(10) DEFAULT 0 NOT NULL,
  session_ip VARCHAR2(15) DEFAULT ' ' NOT NULL,
  session_page NUMBER(10) DEFAULT 0 NOT NULL,
  session_logged_int NUMBER(10) DEFAULT NULL
);

CREATE INDEX idx_sess_user ON jforum_sessions(session_user_id);

--
-- Table structure for table 'jforum_smilies'
--

CREATE SEQUENCE jforum_smilies_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_smilies (
  smilie_id NUMBER(10) NOT NULL,
  code VARCHAR2(50) DEFAULT ' ' NOT NULL,
  url VARCHAR2(100) DEFAULT NULL,
  disk_name VARCHAR2(255),
  PRIMARY KEY (smilie_id)
);

--
-- Table structure for table 'jforum_themes'
--

CREATE SEQUENCE jforum_themes_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_themes (
  themes_id NUMBER(10) NOT NULL,
  template_name VARCHAR2(30) DEFAULT ' ' NOT NULL,
  style_name VARCHAR2(30) DEFAULT ' ' NOT NULL,
  PRIMARY KEY (themes_id)
);

--
-- Table structure for table 'jforum_topics'
--

CREATE SEQUENCE jforum_topics_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_topics (
  topic_id NUMBER(10) NOT NULL,
  forum_id NUMBER(10) DEFAULT 0 NOT NULL,
  topic_title VARCHAR2(100) DEFAULT ' ' NOT NULL,
  user_id NUMBER(10) DEFAULT 0 NOT NULL,
  topic_time DATE DEFAULT SYSDATE NOT NULL,
  topic_views NUMBER(10) DEFAULT 1,
  topic_replies NUMBER(10) DEFAULT 0,
  topic_status NUMBER(10) DEFAULT 0,
  topic_vote_id NUMBER(10) DEFAULT 0,
  topic_type NUMBER(10) DEFAULT 0,
  topic_first_post_id NUMBER(10) DEFAULT 0,
  topic_last_post_id NUMBER(10) DEFAULT 0 NOT NULL,
  topic_moved_id NUMBER(10) DEFAULT 0,
  moderated NUMBER(10) DEFAULT 0,
  PRIMARY KEY (topic_id)
);

CREATE INDEX idx_topics_forum ON jforum_topics(forum_id);
CREATE INDEX idx_topics_user ON jforum_topics(user_id);
CREATE INDEX idx_topics_fp ON jforum_topics(topic_first_post_id);
CREATE INDEX idx_topics_lp ON jforum_topics(topic_last_post_id);
CREATE INDEX idx_topics_time ON jforum_topics(topic_time);
CREATE INDEX idx_topics_type ON jforum_topics(topic_type);
CREATE INDEX idx_topics_moved ON jforum_topics(topic_moved_id);

--
-- Table structure for table 'jforum_topics_watch'
--

CREATE TABLE jforum_topics_watch (
  topic_id NUMBER(10) DEFAULT 0 NOT NULL,
  user_id NUMBER(10) DEFAULT 0 NOT NULL,
  is_read NUMBER(10) DEFAULT 0 NOT NULL
);
CREATE INDEX idx_tw_topic ON jforum_topics_watch(topic_id);
CREATE INDEX idx_tw_user ON jforum_topics_watch(user_id);

--
-- Table structure for table 'jforum_users'
--

CREATE SEQUENCE jforum_users_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_users (
  user_id NUMBER(10) NOT NULL,
  user_active NUMBER(10) DEFAULT NULL,
  username VARCHAR2(50) DEFAULT ' ' NOT NULL,
  user_password VARCHAR2(32) DEFAULT ' ' NOT NULL,
  user_session_time NUMBER(10) DEFAULT 0 NOT NULL,
  user_session_page NUMBER(10) DEFAULT 0 NOT NULL,
  user_lastvisit DATE DEFAULT SYSDATE NOT NULL,
  user_regdate DATE DEFAULT SYSDATE NOT NULL,
  user_level NUMBER(10) DEFAULT NULL,
  user_posts NUMBER(10) DEFAULT 0 NOT NULL,
  user_timezone VARCHAR2(5) DEFAULT ' ' NOT NULL,
  user_style NUMBER(10) DEFAULT NULL,
  user_lang VARCHAR2(255) DEFAULT NULL,
  user_dateformat VARCHAR2(30) DEFAULT '%d/%M/%Y %H:%i' NOT NULL,
  user_new_privmsg NUMBER(10) DEFAULT 0 NOT NULL,
  user_unread_privmsg NUMBER(10) DEFAULT 0 NOT NULL,
  user_last_privmsg DATE NULL,
  user_emailtime DATE DEFAULT NULL,
  user_viewemail NUMBER(10) DEFAULT 0,
  user_attachsig NUMBER(10) DEFAULT 1,
  user_allowhtml NUMBER(10) DEFAULT 0,
  user_allowbbcode NUMBER(10) DEFAULT 1,
  user_allowsmilies NUMBER(10) DEFAULT 1,
  user_allowavatar NUMBER(10) DEFAULT 1,
  user_allow_pm NUMBER(10) DEFAULT 1,
  user_allow_viewonline NUMBER(10) DEFAULT 1,
  user_notify NUMBER(10) DEFAULT 1,
  user_notify_pm NUMBER(10) DEFAULT 1,
  user_popup_pm NUMBER(10) DEFAULT 1,
  rank_id NUMBER(10) DEFAULT 0,
  user_avatar VARCHAR2(100) DEFAULT NULL,
  user_avatar_type NUMBER(10) DEFAULT 0 NOT NULL,
  user_email VARCHAR2(255) DEFAULT ' ' NOT NULL,
  user_icq VARCHAR2(15) DEFAULT NULL,
  user_website VARCHAR2(255) DEFAULT NULL,
  user_from VARCHAR2(100) DEFAULT NULL,
  user_sig VARCHAR2(4000),
  user_sig_bbcode_uid VARCHAR2(10) DEFAULT NULL,
  user_aim VARCHAR2(255) DEFAULT NULL,
  user_yim VARCHAR2(255) DEFAULT NULL,
  user_msnm VARCHAR2(255) DEFAULT NULL,
  user_occ VARCHAR2(100) DEFAULT NULL,
  user_interests VARCHAR2(255) DEFAULT NULL,
  user_biography VARCHAR2(4000) DEFAULT NULL,
  user_actkey VARCHAR2(32) DEFAULT NULL,
  gender CHAR(1) DEFAULT NULL,
  themes_id NUMBER(10) DEFAULT NULL,
  deleted NUMBER(10) DEFAULT NULL,
  user_viewonline NUMBER(10) DEFAULT 1,
  security_hash VARCHAR2(32),
  user_karma DECIMAL(10,2),
  user_authhash VARCHAR(32),
  user_notify_always NUMBER(1) DEFAULT 0,
  user_notify_text NUMBER(1) DEFAULT 0,
  PRIMARY KEY (user_id)
);


--
-- Table structure for table 'jforum_vote_desc'
--

CREATE SEQUENCE jforum_vote_desc_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_vote_desc (
  vote_id NUMBER(10) NOT NULL,
  topic_id NUMBER(10) DEFAULT 0 NOT NULL,
  vote_text VARCHAR2(255) DEFAULT ' ' NOT NULL,
  vote_start DATE DEFAULT SYSDATE NOT NULL,
  vote_length NUMBER(10) DEFAULT 0 NOT NULL,
  PRIMARY KEY  (vote_id)
);

CREATE INDEX idx_vd_topic ON jforum_vote_desc(topic_id);

--
-- Table structure for table 'jforum_vote_results'
--

CREATE TABLE jforum_vote_results (
  vote_id NUMBER(10) DEFAULT 0 NOT NULL,
  vote_option_id NUMBER(10) DEFAULT 0 NOT NULL,
  vote_option_text VARCHAR2(255) DEFAULT ' ' NOT NULL,
  vote_result NUMBER(10) DEFAULT 0 NOT NULL
);

CREATE INDEX idx_vr_id ON jforum_vote_results(vote_id);

--
-- Table structure for table 'jforum_vote_voters'
--

CREATE TABLE jforum_vote_voters (
  vote_id NUMBER(10) DEFAULT 0 NOT NULL,
  vote_user_id NUMBER(10) DEFAULT 0 NOT NULL,
  vote_user_ip VARCHAR2(15) DEFAULT ' ' NOT NULL
);

CREATE INDEX idx_vv_id ON jforum_vote_voters(vote_id);
CREATE INDEX idx_vv_user ON jforum_vote_voters(vote_user_id);

--
-- Table structure for table 'jforum_words'
--

CREATE SEQUENCE jforum_words_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_words (
  word_id NUMBER(10) NOT NULL,
  word VARCHAR2(100) DEFAULT ' ' NOT NULL,
  replacement VARCHAR2(100) DEFAULT ' ' NOT NULL,
  PRIMARY KEY (word_id)
);

--
-- Table structure for table 'jforum_karma'
--
CREATE SEQUENCE jforum_karma_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_karma (
	karma_id NUMBER(10) NOT NULL,
	post_id NUMBER(10) NOT NULL,
	topic_id NUMBER(10) NOT NULL,
	post_user_id NUMBER(10) NOT NULL,
	from_user_id NUMBER(10) NOT NULL,
	points NUMBER(10) NOT NULL,
	rate_date DATE DEFAULT NULL,
	PRIMARY KEY(karma_id)
);

CREATE INDEX idx_krm_post ON jforum_karma(post_id);
CREATE INDEX idx_krm_topic ON jforum_karma(topic_id);
CREATE INDEX idx_krm_user ON jforum_karma(post_user_id);
CREATE INDEX idx_krm_from ON jforum_karma(from_user_id);

--
-- Table structure for table 'jforum_bookmark'
--
CREATE SEQUENCE jforum_bookmarks_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_bookmarks (
	bookmark_id NUMBER(10) NOT NULL,
	user_id NUMBER(10) NOT NULL,
	relation_id NUMBER(10) NOT NULL,
	relation_type NUMBER(10) NOT NULL,
	public_visible NUMBER(10) DEFAULT 1,
	title VARCHAR(255),
	description VARCHAR(255),
	PRIMARY KEY(bookmark_id)
);

CREATE INDEX idx_bok_user ON jforum_bookmarks(user_id);
CREATE INDEX idx_bok_rel ON jforum_bookmarks(relation_id);

-- 
-- Table structure for table 'jforum_quota_limit'
--
CREATE SEQUENCE jforum_quota_limit_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_quota_limit (
	quota_limit_id NUMBER(10) NOT NULL,
	quota_desc VARCHAR(50) NOT NULL,
	quota_limit NUMBER(10) NOT NULL,
	quota_type NUMBER(1) DEFAULT 1,
	PRIMARY KEY(quota_limit_id)
);

--
-- Table structure for table 'jforum_extension_groups'
--
CREATE SEQUENCE jforum_extension_groups_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_extension_groups (
	extension_group_id NUMBER(10) NOT NULL,
	name VARCHAR(100) NOT NULL,
	allow NUMBER(1) DEFAULT 1, 
	upload_icon VARCHAR(100),
	download_mode NUMBER(1) DEFAULT 1,
	PRIMARY KEY(extension_group_id)
) ;

-- 
-- Table structure for table 'jforum_extensions'
--
CREATE SEQUENCE jforum_extensions_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_extensions (
	extension_id NUMBER(10) NOT NULL,
	extension_group_id NUMBER(10) NOT NULL,
	description VARCHAR(100),
	upload_icon VARCHAR(100),
	extension VARCHAR(10),
	allow NUMBER(1) DEFAULT 1,
	PRIMARY KEY(extension_id)
);

CREATE INDEX idx_ext_group ON jforum_extensions(extension_group_id);
CREATE INDEX idx_ext_ext ON jforum_extensions(extension);

--
-- Table structure for table 'jforum_attach'
--
CREATE SEQUENCE jforum_attach_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_attach (
	attach_id NUMBER(10) NOT NULL,
	post_id NUMBER(10),
	privmsgs_id NUMBER(10),
	user_id NUMBER(10) NOT NULL,
	PRIMARY KEY(attach_id)
);

CREATE INDEX idx_att_post ON jforum_attach(post_id);
CREATE INDEX idx_att_priv ON jforum_attach(privmsgs_id);
CREATE INDEX idx_att_user ON jforum_attach(user_id);

-- 
-- Table structure for table 'jforum_attach_desc'
--
CREATE SEQUENCE jforum_attach_desc_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_attach_desc (
	attach_desc_id NUMBER(10) NOT NULL,
	attach_id NUMBER(10) NOT NULL,
	physical_filename VARCHAR(255) NOT NULL,
	real_filename VARCHAR(255) NOT NULL,
	download_count NUMBER(10),
	description VARCHAR(255),
	mimetype VARCHAR(50),
	filesize NUMBER(20),
	upload_time DATE,
	thumb NUMBER(1) DEFAULT 0,
	extension_id NUMBER(10),
	PRIMARY KEY(attach_desc_id)
);

CREATE INDEX idx_att_d_att ON jforum_attach_desc(attach_id);
CREATE INDEX idx_att_d_ext ON jforum_attach_desc(extension_id);

--
-- Table structure for table 'jforum_attach_quota'
--
CREATE SEQUENCE jforum_attach_quota_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_attach_quota (
	attach_quota_id NUMBER(10) NOT NULL,
	group_id NUMBER(10) NOT NULL,
	quota_limit_id NUMBER(10) NOT NULL,
	PRIMARY KEY(attach_quota_id)
);

CREATE INDEX idx_aq_group ON jforum_attach_quota(group_id);
CREATE INDEX idx_aq_ql ON jforum_attach_quota(quota_limit_id);

--
-- Table structure for table 'jforum_banner'
--
CREATE SEQUENCE jforum_banner_seq
	INCREMENT BY 1
    START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
    CACHE 200 ORDER;

CREATE TABLE jforum_banner (
	banner_id NUMBER(10) NOT NULL,
	banner_name VARCHAR(90),
	banner_placement NUMBER(1) DEFAULT 0 NOT NULL,
	banner_description VARCHAR(250),
	banner_clicks NUMBER(8) DEFAULT 0 NOT NULL,
	banner_views NUMBER(8) DEFAULT 0 NOT NULL,
	banner_url VARCHAR(250),
	banner_weight NUMBER(2) DEFAULT 50 NOT NULL,
	banner_active NUMBER(1) DEFAULT 0 NOT NULL,
	banner_comment VARCHAR(250),
	banner_type NUMBER(5) DEFAULT 0 NOT NULL,
	banner_width NUMBER(5) DEFAULT 0 NOT NULL,
	banner_height NUMBER(5) DEFAULT 0 NOT NULL,
	PRIMARY KEY(banner_id)
);

--
-- Table structure for table 'jforum_moderation_log'
-- 
CREATE SEQUENCE jforum_moderation_log_seq 
	INCREMENT BY 1
	START WITH 1 MAXVALUE 2.0E9 MINVALUE 1 NOCYCLE
	CACHE 200 ORDER;

CREATE TABLE jforum_moderation_log (
	log_id NUMBER(10) NOT NULL,
	user_id NUMBER(10) NOT NULL,
	log_description BLOB NOT NULL,
	log_original_message BLOB,
	log_date DATE NOT NULL,
	log_type NUMBER(1) DEFAULT 0,
	post_id NUMBER(10),
	topic_id NUMBER(10),
	post_user_id NUMBER(10),
	PRIMARY KEY(log_id)
);

CREATE INDEX idx_ml_user ON jforum_moderation_log(user_id);
CREATE INDEX idx_ml_post_user ON jforum_moderation_log(post_user_id);