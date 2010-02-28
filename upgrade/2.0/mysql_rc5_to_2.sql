ALTER TABLE jforum_search_results CHANGE time search_time DATETIME;
ALTER TABLE jforum_search_topics CHANGE time search_time DATETIME;
ALTER TABLE jforum_roles CHANGE type role_type TINYINT(1) DEFAULT 1;
ALTER TABLE jforum_role_values CHANGE type role_type TINYINT(1) DEFAULT 1;
ALTER TABLE jforum_role_values CHANGE value role_value VARCHAR(255);

ALTER TABLE jforum_posts ADD tmp DATETIME;
UPDATE jforum_posts SET tmp = FROM_UNIXTIME(SUBSTRING(post_time, 1, 10));
ALTER TABLE jforum_posts DROP COLUMN post_time;
ALTER TABLE jforum_posts CHANGE tmp post_time DATETIME;

ALTER TABLE jforum_posts ADD tmp DATETIME;
UPDATE jforum_posts SET tmp = FROM_UNIXTIME(SUBSTRING(post_edit_time, 1, 10));
ALTER TABLE jforum_posts DROP COLUMN post_edit_time;
ALTER TABLE jforum_posts CHANGE tmp post_edit_time DATETIME;

DELETE FROM jforum_sessions;
ALTER TABLE jforum_sessions CHANGE session_time session_time BIGINT;
ALTER TABLE jforum_sessions CHANGE session_start session_start DATETIME;

ALTER TABLE jforum_topics ADD tmp DATETIME;
UPDATE jforum_topics SET tmp = FROM_UNIXTIME(SUBSTRING(topic_time, 1, 10));
ALTER TABLE jforum_topics DROP COLUMN topic_time;
ALTER TABLE jforum_topics CHANGE tmp topic_time DATETIME;

ALTER TABLE jforum_privmsgs ADD tmp DATETIME;
UPDATE jforum_privmsgs SET tmp = FROM_UNIXTIME(SUBSTRING(privmsgs_date, 1, 10));
ALTER TABLE jforum_privmsgs DROP COLUMN privmsgs_date;
ALTER TABLE jforum_privmsgs CHANGE tmp privmsgs_date DATETIME;

UPDATE jforum_users SET user_session_time = NULL;
ALTER TABLE jforum_users CHANGE user_session_time user_session_time DATETIME;

ALTER TABLE jforum_users ADD tmp DATETIME;
UPDATE jforum_users SET tmp = FROM_UNIXTIME(SUBSTRING(user_regdate, 1, 10));
ALTER TABLE jforum_users DROP COLUMN user_regdate;
ALTER TABLE jforum_users CHANGE tmp user_regdate DATETIME;

ALTER TABLE jforum_users CHANGE user_emailtime user_emailtime DATETIME;

UPDATE jforum_users SET user_lastvisit = NULL;
ALTER TABLE jforum_users CHANGE user_lastvisit user_lastvisit DATETIME;

UPDATE jforum_users SET user_last_privmsg = NULL;
ALTER TABLE jforum_users CHANGE user_last_privmsg user_last_privmsg DATETIME;

DELETE FROM jforum_search_topics;
ALTER TABLE jforum_search_topics CHANGE topic_time topic_time DATETIME;

UPDATE jforum_users SET user_lastvisit = NULL;
UPDATE jforum_users SET user_emailtime = NULL;
UPDATE jforum_users SET user_session_time = NULL;
UPDATE jforum_users SET user_regdate = NOW() where user_regdate = 0;