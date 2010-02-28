ALTER TABLE jforum_search_results RENAME time TO search_time;
ALTER TABLE jforum_search_topics RENAME time TO search_time;
ALTER TABLE jforum_roles RENAME type TO role_type;
ALTER TABLE jforum_role_values RENAME type TO role_type;
ALTER TABLE jforum_role_values RENAME value TO role_value;

ALTER TABLE jforum_posts ADD tmp TIMESTAMP;
UPDATE jforum_posts SET tmp = ABSTIME(SUBSTRING(post_time, 1, 10)::INT);
ALTER TABLE jforum_posts DROP COLUMN post_time;
ALTER TABLE jforum_posts RENAME tmp TO post_time;

ALTER TABLE jforum_posts ADD tmp TIMESTAMP;
UPDATE jforum_posts SET tmp = ABSTIME(SUBSTRING(post_edit_time, 1, 10)::INT);
ALTER TABLE jforum_posts DROP COLUMN post_edit_time;
ALTER TABLE jforum_posts RENAME tmp TO post_edit_time;

DELETE FROM jforum_sessions;
ALTER TABLE jforum_sessions DROP session_time;
ALTER TABLE jforum_sessions ADD session_time BIGINT;
ALTER TABLE jforum_sessions DROP session_start;
ALTER TABLE jforum_sessions ADD session_start TIMESTAMP;

ALTER TABLE jforum_topics ADD tmp TIMESTAMP;
UPDATE jforum_topics SET tmp = ABSTIME(SUBSTRING(topic_time, 1, 10)::INT);
ALTER TABLE jforum_topics DROP COLUMN topic_time;
ALTER TABLE jforum_topics RENAME tmp TO topic_time;
ALTER TABLE jforum_topics ALTER topic_time SET DEFAULT NOW();

ALTER TABLE jforum_privmsgs ADD tmp TIMESTAMP;
UPDATE jforum_privmsgs SET tmp = ABSTIME(SUBSTRING(privmsgs_date, 1, 10)::INT);
ALTER TABLE jforum_privmsgs DROP COLUMN privmsgs_date;
ALTER TABLE jforum_privmsgs RENAME tmp TO privmsgs_date;
ALTER TABLE jforum_privmsgs ALTER privmsgs_date SET DEFAULT NOW();

ALTER TABLE jforum_users DROP user_session_time;
ALTER TABLE jforum_users ADD user_session_time TIMESTAMP;

ALTER TABLE jforum_users ADD tmp TIMESTAMP;
UPDATE jforum_users SET tmp = ABSTIME(SUBSTRING(user_regdate, 1, 10)::INT);
ALTER TABLE jforum_users DROP COLUMN user_regdate;
ALTER TABLE jforum_users RENAME tmp TO user_regdate;
ALTER TABLE jforum_users ALTER user_regdate SET DEFAULT NOW();

ALTER TABLE jforum_users DROP user_emailtime;
ALTER TABLE jforum_users ADD user_emailtime TIMESTAMP;

ALTER TABLE jforum_users DROP user_lastvisit;
ALTER TABLE jforum_users ADD user_lastvisit TIMESTAMP;

ALTER TABLE jforum_users DROP user_last_privmsg;
ALTER TABLE jforum_users ADD user_last_privmsg TIMESTAMP;

DELETE FROM jforum_search_topics;
ALTER TABLE jforum_search_topics DROP topic_time;
ALTER TABLE jforum_search_topics ADD topic_time TIMESTAMP;
ALTER TABLE jforum_search_topics ALTER topic_time SET DEFAULT NOW();

UPDATE jforum_users SET user_lastvisit = NULL;
UPDATE jforum_users SET user_emailtime = NULL;
UPDATE jforum_users SET user_session_time = NULL;
UPDATE jforum_users SET user_regdate = NOW() where user_regdate = 0;
