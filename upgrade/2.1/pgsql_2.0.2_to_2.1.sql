UPDATE jforum_categories SET display_order = categories_id;

ALTER TABLE jforum_users ADD user_karma DECIMAL(10, 2);

ALTER TABLE jforum_posts ADD attach int;
ALTER TABLE jforum_posts ALTER attach SET DEFAULT '0';

ALTER TABLE jforum_posts ADD need_moderate int;
ALTER TABLE jforum_posts ALTER need_moderate SET DEFAULT '0';

ALTER TABLE jforum_categories ADD moderated int;
ALTER TABLE jforum_categories ALTER moderated SET DEFAULT '0';

ALTER TABLE jforum_search_topics ALTER topic_title TYPE VARCHAR(100);

--
-- Table structure for table 'jforum_karma'
--
CREATE SEQUENCE jforum_karma_seq;
CREATE TABLE jforum_karma (
	karma_id INTEGER NOT NULL DEFAULT NEXTVAL('jforum_karma_seq'),
	post_id INTEGER NOT NULL,
	topic_id INTEGER NOT NULL,
	post_user_id INTEGER NOT NULL,
	from_user_id INTEGER NOT NULL,
	points INTEGER NOT NULL,
	rate_date TIMESTAMP,
	PRIMARY KEY(karma_id)
);

CREATE INDEX idx_krm_post ON jforum_karma(post_id);
CREATE INDEX idx_krm_topic ON jforum_karma(topic_id);
CREATE INDEX idx_krm_user ON jforum_karma(post_user_id);
CREATE INDEX idx_krm_from ON jforum_karma(from_user_id);

--
-- Table structure for table 'jforum_bookmark'
--
CREATE SEQUENCE jforum_bookmarks_seq;
CREATE TABLE jforum_bookmarks (
	bookmark_id INTEGER NOT NULL DEFAULT NEXTVAL('jforum_bookmarks_seq'),
	user_id INTEGER NOT NULL,
	relation_id INTEGER NOT NULL,
	relation_type INTEGER NOT NULL,
	public_visible INTEGER DEFAULT 1,
	title VARCHAR(255),
	description VARCHAR(255),
	PRIMARY KEY(bookmark_id)
);

CREATE INDEX idx_bok_user ON jforum_bookmarks(user_id);
CREATE INDEX idx_bok_rel ON jforum_bookmarks(relation_id);

-- 
-- Table structure for table 'jforum_quota_limit'
--
CREATE SEQUENCE jforum_quota_limit_seq;
CREATE TABLE jforum_quota_limit (
	quota_limit_id INTEGER NOT NULL DEFAULT NEXTVAL('jforum_quota_limit_seq'),
	quota_desc VARCHAR(50) NOT NULL,
	quota_limit INTEGER NOT NULL,
	quota_type INTEGER DEFAULT 1,
	PRIMARY KEY(quota_limit_id)
);

--
-- Table structure for table 'jforum_extension_groups'
--
CREATE SEQUENCE jforum_extension_groups_seq;
CREATE TABLE jforum_extension_groups (
	extension_group_id INTEGER NOT NULL DEFAULT NEXTVAL('jforum_extension_groups_seq'),
	name VARCHAR(100) NOT NULL,
	allow INTEGER DEFAULT 1, 
	upload_icon VARCHAR(100),
	download_mode INTEGER DEFAULT 1,
	PRIMARY KEY(extension_group_id)
);

-- 
-- Table structure for table 'jforum_extensions'
--
CREATE SEQUENCE jforum_extensions_seq;
CREATE TABLE jforum_extensions (
	extension_id INTEGER NOT NULL DEFAULT NEXTVAL('jforum_extensions_seq'),
	extension_group_id INTEGER NOT NULL,
	description VARCHAR(100),
	upload_icon VARCHAR(100),
	extension VARCHAR(10),
	allow INTEGER DEFAULT 1,
	PRIMARY KEY(extension_id)
);

CREATE INDEX idx_ext_group ON jforum_extensions(extension_group_id);
CREATE INDEX idx_ext_ext ON jforum_extensions(extension);

--
-- Table structure for table 'jforum_attach'
--
CREATE SEQUENCE jforum_attach_seq;
CREATE TABLE jforum_attach (
	attach_id INTEGER NOT NULL DEFAULT NEXTVAL('jforum_attach_seq'),
	post_id INTEGER,
	privmsgs_id INTEGER,
	user_id INTEGER NOT NULL,
	PRIMARY KEY(attach_id)
);

CREATE INDEX idx_att_post ON jforum_attach(post_id);
CREATE INDEX idx_att_priv ON jforum_attach(privmsgs_id);
CREATE INDEX idx_att_user ON jforum_attach(user_id);

-- 
-- Table structure for table 'jforum_attach_desc'
--
CREATE SEQUENCE jforum_attach_desc_seq;
CREATE TABLE jforum_attach_desc (
	attach_desc_id INTEGER NOT NULL PRIMARY KEY DEFAULT NEXTVAL('jforum_attach_desc_seq'),
	attach_id INTEGER NOT NULL,
	physical_filename VARCHAR(255) NOT NULL,
	real_filename VARCHAR(255) NOT NULL,
	download_count INTEGER,
	description VARCHAR(255),
	mimetype VARCHAR(50),
	filesize NUMERIC(20),
	upload_time DATE,
	thumb INTEGER DEFAULT 0,
	extension_id INTEGER
);

CREATE INDEX idx_att_d_att ON jforum_attach_desc(attach_id);
CREATE INDEX idx_att_d_ext ON jforum_attach_desc(extension_id);

--
-- Table structure for table 'jforum_attach_quota'
--
CREATE SEQUENCE jforum_attach_quota_seq;
CREATE TABLE jforum_attach_quota (
	attach_quota_id INTEGER NOT NULL DEFAULT NEXTVAL('jforum_attach_quota_seq'),
	group_id INTEGER NOT NULL,
	quota_limit_id INTEGER NOT NULL,
	PRIMARY KEY(attach_quota_id)
);

CREATE INDEX idx_aq_group ON jforum_attach_quota(group_id);
CREATE INDEX idx_aq_ql ON jforum_attach_quota(quota_limit_id);