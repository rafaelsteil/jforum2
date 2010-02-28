# ####################################
# @author ??? (original coding)
# @author Dirk Rasmussen - d.rasmussen@bevis.de (modifs for MS SqlServer 2005)
# @see net.jforum.dao.sqlserver.SqlServerUserDAO.class
# @see net.jforum.dao.sqlserver.SqlServerTopicDAO..class
# @see net.jforum.dao.sqlserver.SqlServerPostDAO.class
# @version $Id$
# ####################################

# #############
# GenericModel
# #############

GenericModel.selectByLimit = SELECT TOP

# #############
# UserModel
# #############

UserModel.selectById = SELECT COUNT(pm.privmsgs_to_userid) AS private_messages, u.user_id, u.user_active, u.username, u.user_password, u.user_session_time, \
								u.user_session_page, u.user_lastvisit, u.user_regdate, u.user_level, u.user_posts, u.user_timezone, u.user_style, \
								u.user_lang, u.user_dateformat, u.user_new_privmsg, u.user_unread_privmsg, u.user_last_privmsg, u.user_emailtime, \
								u.user_viewemail, u.user_attachsig, u.user_allowhtml, u.user_allowbbcode, u.user_allowsmilies, u.user_allowavatar, \
								u.user_allow_pm, u.user_allow_viewonline, u.user_notify_always, u.user_notify_text, u.user_notify, u.user_notify_pm, u.user_popup_pm, u.rank_id, u.user_avatar, \
								u.user_avatar_type, u.user_email, u.user_icq, u.user_website, u.user_from, CAST(u.user_sig as varchar) as user_sig , u.user_sig_bbcode_uid, \
								u.user_aim, u.user_yim, u.user_msnm, u.user_occ, u.user_interests, CAST(u.user_biography as varchar) as user_biography, u.user_actkey, u.gender, u.themes_id, u.deleted, \
								u.user_viewonline, u.security_hash, u.user_karma \
								FROM jforum_users u \
								LEFT JOIN jforum_privmsgs pm ON pm.privmsgs_type = 1 AND pm.privmsgs_to_userid = u.user_id \
								WHERE u.user_id = ? \
								GROUP BY pm.privmsgs_to_userid, u.user_id, u.user_active, u.username, u.user_password, u.user_session_time, \
								u.user_session_page, u.user_lastvisit, u.user_regdate, u.user_level, u.user_posts, u.user_timezone, u.user_style, \
								u.user_lang, u.user_dateformat, u.user_new_privmsg, u.user_unread_privmsg, u.user_last_privmsg, u.user_emailtime, \
								u.user_viewemail, u.user_attachsig, u.user_allowhtml, u.user_allowbbcode, u.user_allowsmilies, u.user_allowavatar, \
								u.user_allow_pm, u.user_allow_viewonline, u.user_notify_always, u.user_notify_text, u.user_notify, u.user_notify_pm, u.user_popup_pm, u.rank_id, u.user_avatar, \
								u.user_avatar_type, u.user_email, u.user_icq, u.user_website, u.user_from, CAST(u.user_sig as varchar), u.user_sig_bbcode_uid, \
								u.user_aim, u.user_yim, u.user_msnm, u.user_occ, u.user_interests, CAST(u.user_biography as varchar), u.user_actkey, u.gender, u.themes_id, u.deleted, \
								u.user_viewonline, u.security_hash, u.user_karma
								
UserModel.lastUserRegistered = SELECT TOP 1 user_id, username FROM jforum_users ORDER BY user_regdate DESC
UserModel.lastGeneratedUserId = SELECT IDENT_CURRENT('jforum_users') AS user_id
UserModel.selectAllByLimit = SELECT * \
	FROM ( SELECT ROW_NUMBER() OVER (ORDER BY username ASC) AS rownumber, \
	user_email, user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, user_website, user_viewemail \
	FROM jforum_users ) AS tmp \
	WHERE rownumber BETWEEN ? and ?

UserModel.selectAllByGroup = SELECT * \
	FROM ( SELECT ROW_NUMBER() OVER (ORDER BY user_id ASC) AS rownumber, \
	user_email, u.user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, \
	user_website, user_viewemail \
	FROM jforum_users u, jforum_user_groups ug \
	WHERE u.user_id = ug.user_id \
	AND ug.group_id = ? ) AS tmp \
	WHERE rownumber BETWEEN ? and ?

# #############
# GroupModel
# #############

GroupModel.lastGeneratedGroupId = SELECT IDENT_CURRENT('jforum_groups') AS group_id

# #############
# ForumModel
# #############

ForumModel.selectAll =  SELECT f.forum_id, f.categories_id, f.forum_name, f.forum_desc, f.forum_order, f.forum_topics, \
							  f.forum_last_post_id, f.moderated, COUNT(p.post_id) AS total_posts \
						FROM jforum_forums f \
						LEFT JOIN jforum_topics t \
						ON t.forum_id = f.forum_id \
						LEFT JOIN jforum_posts p \
						ON p.topic_id = t.topic_id \
						GROUP BY f.forum_id, f.categories_id, f.forum_name, f.forum_desc, f.forum_order, f.forum_topics, f.forum_last_post_id, f.moderated 
					
# #############
# CategoryModel
# #############

CategoryModel.lastGeneratedCategoryId = SELECT IDENT_CURRENT('jforum_categories') AS categories_id 

# #############
# PostModel
# #############

PostModel.lastGeneratedPostId = SELECT IDENT_CURRENT('jforum_posts') AS post_id

PostModel.addNewPost = INSERT INTO jforum_posts (topic_id, forum_id, user_id, post_time, poster_ip, enable_bbcode, enable_html, enable_smilies, enable_sig, post_edit_time, need_moderate) \
	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)

PostModel.selectAllByTopicByLimit = SELECT * \
	FROM ( SELECT ROW_NUMBER() OVER (ORDER BY post_time ASC) AS rownumber, \
	p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND topic_id = ? \
	AND p.user_id = u.user_id \
	AND p.need_moderate = 0 ) AS tmp \
	WHERE rownumber between ? and ?

PostModel.selectByUserByLimit = SELECT * \
	FROM ( SELECT ROW_NUMBER() OVER (ORDER BY post_id DESC) AS rownumber, \
	p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND p.user_id = u.user_id \
	AND p.user_id = ? \
	AND p.need_moderate = 0 \
	AND forum_id IN(:fids:) ) AS tmp \
	WHERE rownumber between ? and ? \
	ORDER BY p.post_id DESC

	
# #############
# ForumModel
# #############

ForumModel.selectById = SELECT f.*, COUNT(p.post_id) AS total_posts \
	FROM jforum_forums f \
	LEFT JOIN jforum_topics t ON t.forum_id = f.forum_id \
	LEFT JOIN jforum_posts p ON p.topic_id = t.topic_id \
	WHERE f.forum_id = ? \
	GROUP BY f.categories_id, f.forum_id, \
	      f.forum_name, f.forum_desc, f.forum_order, \
	      f.forum_topics, f.forum_last_post_id, f.moderated

ForumModel.lastGeneratedForumId = SELECT IDENT_CURRENT('jforum_forums') AS forum_id

# #############
# TopicModel
# #############

TopicModel.selectAllByForumByLimit = SELECT * \
	FROM ( SELECT ROW_NUMBER() OVER (ORDER BY t.topic_type DESC, t.topic_last_post_id DESC) AS rownumber, \
	t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE (t.forum_id = ? OR t.topic_moved_id = ?) \
	AND p.post_id = t.topic_last_post_id \
	AND p.need_moderate = 0 ) AS tmp \
	WHERE rownumber between ? and ?
	
TopicModel.selectRecentTopicsByLimit = SELECT * \
	FROM ( SELECT ROW_NUMBER() OVER (ORDER BY t.topic_last_post_id DESC) AS rownumber, \
	t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND p.need_moderate = 0 ) AS tmp \
	WHERE rownumber <= ?

TopicModel.selectByUserByLimit = SELECT * \
	FROM ( SELECT ROW_NUMBER() OVER (ORDER BY t.topic_last_post_id DESC) AS rownumber, \
	t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND t.user_id = ? \
	AND p.need_moderate = 0 \
	AND t.forum_id IN(:fids:)AS tmp \
	WHERE rownumber between ? and ?
	
TopicModel.lastGeneratedTopicId = SELECT IDENT_CURRENT('jforum_topics') AS topic_id 

# #############
# PrivateMessagesModel
# #############

PrivateMessagesModel.lastGeneratedPmId = SELECT IDENT_CURRENT('jforum_privmsgs') AS privmsgs_id 

PrivateMessageModel.selectById = SELECT p.privmsgs_id, p.privmsgs_type, p.privmsgs_subject, p.privmsgs_from_userid, p.privmsgs_to_userid, \
									p.privmsgs_date, p.privmsgs_ip, p.privmsgs_enable_bbcode, p.privmsgs_enable_html, p.privmsgs_enable_smilies, \
									p.privmsgs_attach_sig, pt.privmsgs_text FROM jforum_privmsgs p, jforum_privmsgs_text pt \
									WHERE p.privmsgs_id = pt.privmsgs_id AND p.privmsgs_id = ?

# #############
# SearchModel
# #############

SearchModel.lastGeneratedWordId = SELECT IDENT_CURRENT('jforum_search_words') AS word_id 

SearchModel.cleanSearchResults = DELETE FROM jforum_search_results WHERE session_id = ? OR search_time < DATEADD(HOUR, -1, getdate())
SearchModel.cleanSearchTopics = DELETE FROM jforum_search_topics WHERE session_id = ? OR search_time < DATEADD(HOUR, -1, getdate())


SearchModel.insertTopicsIds = INSERT INTO jforum_search_results ( topic_id, session_id, search_time ) \
									SELECT DISTINCT t.topic_id, ?, GETDATE() FROM jforum_topics t, jforum_posts p \
									WHERE t.topic_id = p.topic_id \
									AND p.post_id IN (:posts:)

SearchModel.searchByTime = INSERT INTO jforum_search_results (topic_id, session_id, search_time) SELECT DISTINCT t.topic_id, ?, GETDATE() FROM jforum_topics t, jforum_posts p \
	WHERE t.topic_id = p.topic_id \
	AND p.post_time > ?
	
SearchModel.selectTopicData = INSERT INTO jforum_search_topics (topic_id, forum_id, topic_title, user_id, topic_time, \
	topic_views, topic_status, topic_replies, topic_vote_id, topic_type, topic_first_post_id, topic_last_post_id, moderated, session_id, search_time) \
	SELECT t.topic_id, t.forum_id, t.topic_title, t.user_id, t.topic_time, \
	t.topic_views, t.topic_status, t.topic_replies, t.topic_vote_id, t.topic_type, t.topic_first_post_id, t.topic_last_post_id, t.moderated, ?, GETDATE() \
	FROM jforum_topics t, jforum_search_results s \
	WHERE t.topic_id = s.topic_id \
	AND s.session_id = ?

SearchModel.getPostsToIndex = SELECT * \
	FROM ( SELECT ROW_NUMBER() OVER (ORDER BY p.post_id ASC) AS rownumber, \
	p.post_id, pt.post_text, pt.post_subject \
	FROM jforum_posts p, jforum_posts_text pt \
	WHERE p.post_id = pt.post_id \
	AND p.post_id BETWEEN ? AND ? ) AS tmp \
	WHERE rownumber between ? and ?

# #############
# SmiliesModel
# #############

SmiliesModel.lastGeneratedSmilieId = SELECT IDENT_CURRENT('jforum_smilies') AS smilie_id 

# #############
# PermissionControl
# #############

PermissionControl.lastGeneratedRoleId = SELECT IDENT_CURRENT('jforum_roles') AS role_id 

# #############
# KarmaModel
# #############

KarmaModel.getMostRatedUserByPeriod = u.user_id, u.username, SUM(points) AS total, \
	COUNT(post_user_id) AS votes_received, user_karma, \
	(SELECT COUNT(from_user_id) AS votes_given \
		FROM jforum_karma as k2 \
		WHERE k2.from_user_id = u.user_id) AS votes_given \
	FROM jforum_users u, jforum_karma k \
	WHERE u.user_id = k.post_user_id \
	AND k.rate_date BETWEEN ? AND ? \
	GROUP BY u.user_id, u.username, user_karma
									  
KarmaModel.getMostRaterUserByPeriod = NO

# ################
# ModerationLog
# ################
ModerationLog.lastGeneratedModerationLogId = SELECT IDENT_CURRENT('jforum_moderation_log') AS log_id
ModerationLog.selectAll = SELECT * FROM ( \
	SELECT l.*, u.username, ROW_NUMBER() OVER(ORDER BY l.log_id DESC) -1 LINENUM \
	FROM jforum_moderation_log l, jforum_users u WHERE l.user_id = u.user_id ORDER BY log_id DESC
	) \
	WHERE LINENUM >= ? AND LINENUM < ?