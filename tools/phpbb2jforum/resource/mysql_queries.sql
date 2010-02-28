#
# Clean
#
query.clean.banlist = DELETE FROM jforum_banlist
query.clean.categories = DELETE FROM jforum_categories
query.clean.forums = DELETE FROM jforum_forums
query.clean.groups = DELETE FROM jforum_groups
query.clean.posts = DELETE FROM jforum_posts
query.clean.posts.text = DELETE FROM jforum_posts_text
query.clean.privmsgs = DELETE FROM jforum_privmsgs
query.clean.privmsgs.text = DELETE FROM jforum_privmsgs_text
query.clean.ranks = DELETE FROM jforum_ranks
query.clean.smilies = DELETE FROM jforum_smilies
query.clean.topics = DELETE FROM jforum_topics
query.clean.topicswatch = DELETE FROM jforum_topics_watch
query.clean.users = DELETE FROM jforum_users
query.clean.usergroups = DELETE FROM jforum_user_groups
query.clean.votedesc = DELETE FROM jforum_vote_desc
query.clean.voteresults = DELETE FROM jforum_vote_results
query.clean.votevoters = DELETE FROM jforum_vote_voters

#
# General
#
query.totalposts = SELECT COUNT(1) AS total FROM ${phpbb}.${table.prefix}posts
query.select.poststext = SELECT post_id, post_subject, post_text FROM ${phpbb}.${table.prefix}posts_text

query.select.users = SELECT user_id, user_active, username, user_password, user_regdate user_regdate, user_level, user_posts, user_timezone, \
	user_style, user_lang, user_dateformat, user_new_privmsg, user_unread_privmsg, user_last_privmsg, \
	user_viewemail, user_attachsig, \
	user_allowhtml, user_allowbbcode, user_allowsmile, user_allowavatar, user_allow_pm, \
	user_notify, user_notify_pm, user_popup_pm, user_rank, user_avatar, user_avatar_type, user_email, user_icq, \
	user_website, user_from, user_sig, user_aim, user_yim, user_msnm, user_occ, user_interests, \
	user_allow_viewonline FROM ${phpbb}.${table.prefix}users

query.select.pm = SELECT privmsgs_text_id, privmsgs_text FROM ${phpbb}.${table.prefix}privmsgs_text
query.update.anonymous = UPDATE jforum_users SET user_id = 1 WHERE user_id = -1
query.maxgroupid = SELECT MAX(group_id) FROM jforum_groups

#
# Insert
#
query.admingroup = INSERT INTO jforum_groups (group_name, group_description) VALUES ('JForum Admin', 'Administrators. Created by phpbb2jforum')

query.adminrole = INSERT INTO jforum_roles (group_id, name) \
	SELECT MAX(group_id), 'perm_administration' FROM jforum_groups
		
query.adminusergroups = INSERT INTO jforum_user_groups (group_id, user_id) \
	SELECT ?, user_id FROM ${phpbb}.${table.prefix}users WHERE user_level = 1
	
query.groups = INSERT INTO jforum_groups (group_id, group_name, group_description) \
	SELECT group_id, group_name, group_description FROM ${phpbb}.${table.prefix}groups
	
query.usergroups = INSERT INTO jforum_user_groups (group_id, user_id) \
	SELECT group_id, user_id FROM ${phpbb}.${table.prefix}user_group
	
quer.anonymoususer.group = UPDATE jforum_user_groups SET user_id = 1 WHERE user_id = -1

query.votedesc = INSERT INTO jforum_vote_desc (vote_id, topic_id, vote_text, vote_start, vote_length) \
	SELECT vote_id, topic_id, vote_text, FROM_UNIXTIME(vote_start), vote_length FROM ${phpbb}.${table.prefix}vote_desc
		
query.voteresults = INSERT INTO jforum_vote_results (vote_id, vote_option_id, vote_option_text, vote_result) \
	SELECT vote_id, vote_option_id, vote_option_text, vote_result FROM ${phpbb}.${table.prefix}vote_results
	
query.votevoters = INSERT INTO jforum_vote_voters (vote_id, vote_user_id, vote_user_ip) \
	SELECT vote_id, vote_user_id, concat( \
		conv(substr(vote_user_ip, 1, 2), 16, 10), '.', \
		conv(substr(vote_user_ip, 3, 2), 16, 10), '.', \
		conv(substr(vote_user_ip, 5, 2), 16, 10), '.', \
		conv(substr(vote_user_ip, 7, 2), 16, 10)) \
		FROM ${phpbb}.${table.prefix}vote_voters

query.banlist = INSERT INTO jforum_banlist (user_id, banlist_ip, banlist_email) \
	SELECT ban_userid, concat( \
		conv(substr(ban_ip, 1, 2), 16, 10), '.', \
		conv(substr(ban_ip, 3, 2), 16, 10), '.', \
		conv(substr(ban_ip, 5, 2), 16, 10), '.', \
		conv(substr(ban_ip, 7, 2), 16, 10)), \
		ban_email FROM ${phpbb}.${table.prefix}banlist

query.posts = INSERT INTO jforum_posts ( post_id, topic_id, forum_id, user_id, post_time, poster_ip, enable_bbcode, \
	enable_html, enable_smilies, enable_sig ) \
	SELECT post_id, topic_id, forum_id, poster_id, FROM_UNIXTIME(post_time), \
	concat( \
		conv(substr(poster_ip, 1, 2), 16, 10), '.', \
		conv(substr(poster_ip, 3, 2), 16, 10), '.', \
		conv(substr(poster_ip, 5, 2), 16, 10), '.', \
		conv(substr(poster_ip, 7, 2), 16, 10)), \
	enable_bbcode, enable_html, enable_smilies, enable_sig \
	FROM ${phpbb}.${table.prefix}posts
	
query.posts.text = INSERT INTO jforum_posts_text ( post_id, post_subject, post_text ) VALUES (?, ?, ?)

query.categories = INSERT INTO jforum_categories ( categories_id, title, display_order ) \
	SELECT cat_id, cat_title, cat_order FROM ${phpbb}.${table.prefix}categories

query.forums = INSERT INTO jforum_forums ( forum_id, categories_id, forum_name, forum_desc, forum_order, forum_topics, forum_last_post_id ) \
	SELECT forum_id, cat_id, forum_name, forum_desc, forum_order, forum_topics, forum_last_post_id FROM ${phpbb}.${table.prefix}forums

query.privmsgs = INSERT INTO jforum_privmsgs ( privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, \
	privmsgs_date, privmsgs_ip, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, \
	privmsgs_attach_sig ) \
	SELECT privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, privmsgs_to_userid, \
	FROM_UNIXTIME(privmsgs_date), \
	concat( \
		conv(substr(privmsgs_ip, 1, 2), 16, 10), '.', \
		conv(substr(privmsgs_ip, 3, 2), 16, 10), '.', \
		conv(substr(privmsgs_ip, 5, 2), 16, 10), '.', \
		conv(substr(privmsgs_ip, 7, 2), 16, 10)), \
	privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, privmsgs_attach_sig \
	FROM ${phpbb}.${table.prefix}privmsgs
	
query.privmsgs.text = INSERT INTO jforum_privmsgs_text ( privmsgs_id, privmsgs_text ) VALUES (?, ?)

query.ranks = INSERT INTO jforum_ranks ( rank_id, rank_title, rank_min, rank_special, rank_image ) \
	SELECT rank_id, rank_title, rank_min, rank_special, rank_image FROM ${phpbb}.${table.prefix}ranks

query.topics = INSERT INTO jforum_topics (topic_id, forum_id, topic_title, user_id, topic_time, topic_views, \
	topic_replies, topic_status, topic_vote_id, topic_type, topic_first_post_id, topic_last_post_id, topic_moved_id) \
	SELECT topic_id, forum_id, topic_title, topic_poster, FROM_UNIXTIME(topic_time), topic_views, \
	topic_replies, topic_status, topic_vote, topic_type, topic_first_post_id, topic_last_post_id, topic_moved_id \
	FROM ${phpbb}.${table.prefix}topics

query.topics.watch = INSERT INTO jforum_topics_watch ( topic_id, user_id, is_read ) \
	SELECT topic_id, user_id, '1' FROM ${phpbb}.${table.prefix}topics_watch

query.users = INSERT INTO jforum_users ( user_id, user_active, username, user_password, user_regdate, \
	user_level, user_posts, user_timezone, user_style, user_lang, user_dateformat, user_new_privmsg, \
	user_unread_privmsg, user_last_privmsg, user_viewemail, user_attachsig, \
	user_allowhtml, user_allowbbcode, user_allowsmilies, user_allowavatar, user_allow_pm, \
	user_notify, user_notify_pm, user_popup_pm, rank_id, user_avatar, user_avatar_type, user_email, user_icq, \
	user_website, user_from, user_sig, user_aim, user_yim, user_msnm, user_occ, user_interests, \
	user_viewonline ) VALUES (?, ?, ?, ?, FROM_UNIXTIME(?), ?, ?, ?, ?, ?, ?, ?, ?, FROM_UNIXTIME(?), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
