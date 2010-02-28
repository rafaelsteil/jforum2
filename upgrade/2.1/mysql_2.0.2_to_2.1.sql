UPDATE jforum_categories SET display_order = categories_id;

ALTER TABLE jforum_users ADD user_karma DECIMAL(10, 2);
ALTER TABLE jforum_posts ADD attach TINYINT(1) DEFAULT '0';
ALTER TABLE jforum_posts ADD need_moderate TINYINT(1) DEFAULT '0';
ALTER TABLE jforum_categories ADD moderated TINYINT(1) DEFAULT '0';
ALTER TABLE jforum_forums CHANGE moderated moderated TINYINT(1) DEFAULT '0';
ALTER TABLE jforum_topics CHANGE moderated moderated TINYINT(1) DEFAULT '0';
ALTER TABLE jforum_search_topics CHANGE topic_title topic_title VARCHAR(100);
--
-- Table structure for table 'jforum_karma'
--
DROP TABLE IF EXISTS jforum_karma;
CREATE TABLE jforum_karma (
	karma_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	post_id INT NOT NULL,
	topic_id INT NOT NULL,
	post_user_id INT NOT NULL,
	from_user_id INT NOT NULL,
	points INT NOT NULL,
	rate_date datetime NULL,
	KEY(post_id),
	KEY(topic_id),
	KEY(post_user_id),
	KEY(from_user_id)
) TYPE=InnoDB;

--
-- Table structure for table 'jforum_bookmark'
--
DROP TABLE IF EXISTS jforum_bookmarks;
CREATE TABLE jforum_bookmarks (
	bookmark_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	user_id INT NOT NULL,
	relation_id INT NOT NULL,
	relation_type INT NOT NULL,
	public_visible INT DEFAULT '1',
	title varchar(255),
	description varchar(255),
	INDEX book_idx_relation (relation_id),
	KEY(user_id)
) TYPE=InnoDB;
-- 
-- Table structure for table 'jforum_quota_limit'
--
DROP TABLE IF EXISTS jforum_quota_limit;
CREATE TABLE jforum_quota_limit (
	quota_limit_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	quota_desc VARCHAR(50) NOT NULL,
	quota_limit INT NOT NULL,
	quota_type TINYINT(1) DEFAULT '1'
) TYPE=InnoDB;

--
-- Table structure for table 'jforum_extension_groups'
--
DROP TABLE IF EXISTS jforum_extension_groups;
CREATE TABLE jforum_extension_groups (
	extension_group_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(100) NOT NULL,
	allow TINYINT(1) DEFAULT '1', 
	upload_icon VARCHAR(100),
	download_mode TINYINT(1) DEFAULT '1'
) TYPE=InnoDB;

-- 
-- Table structure for table 'jforum_extensions'
--
DROP TABLE IF EXISTS jforum_extensions;
CREATE TABLE jforum_extensions (
	extension_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	extension_group_id INT NOT NULL,
	description VARCHAR(100),
	upload_icon VARCHAR(100),
	extension VARCHAR(10),
	allow TINYINT(1) DEFAULT '1',
	KEY(extension_group_id)
) TYPE=InnoDB;

--
-- Table structure for table 'jforum_attach'
--
DROP TABLE IF EXISTS jforum_attach;
CREATE TABLE jforum_attach (
	attach_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	post_id INT,
	privmsgs_id INT,
	user_id INT NOT NULL,
	INDEX idx_att_post(post_id),
	INDEX idx_att_priv(privmsgs_id),
	INDEX idx_att_user(user_id)
) TYPE=InnoDB;

-- 
-- Table structure for table 'jforum_attach_desc'
--
DROP TABLE IF EXISTS jforum_attach_desc;
CREATE TABLE jforum_attach_desc (
	attach_desc_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	attach_id INT NOT NULL,
	physical_filename VARCHAR(255) NOT NULL,
	real_filename VARCHAR(255) NOT NULL,
	download_count INT,
	description VARCHAR(255),
	mimetype VARCHAR(50),
	filesize INT,
	upload_time DATETIME,
	thumb TINYINT(1) DEFAULT '0',
	extension_id INT,
	INDEX idx_att_d_att(attach_id),
	INDEX idx_att_d_ext(extension_id)
) TYPE=InnoDB;

--
-- Table structure for table 'jforum_attach_quota'
--
DROP TABLE IF EXISTS jforum_attach_quota;
CREATE TABLE jforum_attach_quota (
	attach_quota_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	group_id INT NOT NULL,
	quota_limit_id INT NOT NULL,
	KEY(group_id)
) TYPE=InnoDB;