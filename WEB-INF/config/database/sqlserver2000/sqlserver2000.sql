# ####################################
# @author ??? (original coding)
# @author Dirk Rasmussen - d.rasmussen@bevis.de (modifs for MS SqlServer 2005)
# @author Andowson Chang - http://www.andowson.com (fix for MS SQL Server 2000)
# @version $Id$
# ####################################

# #############
# GroupModel
# #############
GroupModel.lastGeneratedGroupId = SELECT IDENT_CURRENT('jforum_groups') AS group_id

# #############
# CategoryModel
# #############
CategoryModel.lastGeneratedCategoryId = SELECT IDENT_CURRENT('jforum_categories') AS categories_id 

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
UserModel.selectAllByLimit = SELECT TOP ? \
	user_email, user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, user_website, user_viewemail \
	FROM jforum_users \
	ORDER BY username ASC

# #############
# PostModel
# #############
PostModel.selectLatestByForumForRSS = SELECT TOP ? \
    p.topic_id, p.topic_id, p.post_id, p.forum_id, pt.post_subject AS subject, pt.post_text, p.post_time, p.user_id, u.username \
	FROM jforum_topics t, jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = t.topic_first_post_id \
	AND p.topic_id = t.topic_id \
	AND p.user_id = u.user_id \
	AND p.post_id = pt.post_id \
	AND p.need_moderate = 0 \
	AND t.forum_id = ? \
	ORDER BY t.topic_id DESC
	
PostModel.selectHotForRSS = SELECT TOP ? \
    t.topic_id, t.topic_title AS subject, p.post_id, t.forum_id, pt.post_text, p.post_time, p.user_id, u.username \
	FROM jforum_topics t, jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = t.topic_first_post_id \
	AND p.topic_id = t.topic_id \
	AND p.user_id = u.user_id \
	AND p.post_id = pt.post_id \
	AND p.need_moderate = 0  \
	ORDER BY topic_first_post_id DESC

PostModel.lastGeneratedPostId = SELECT IDENT_CURRENT('jforum_posts') AS post_id

PostModel.addNewPost = INSERT INTO jforum_posts (topic_id, forum_id, user_id, post_time, poster_ip, enable_bbcode, enable_html, enable_smilies, enable_sig, post_edit_time, need_moderate) \
	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)

PostModel.selectAllByTopicByLimit = SELECT TOP ? \
	p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND topic_id = ? \
	AND p.user_id = u.user_id \
	AND p.need_moderate = 0 
	ORDER BY post_time ASC

PostModel.selectByUserByLimit = SELECT TOP ? \
    p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND p.user_id = u.user_id \
	AND p.user_id = ? \
	AND p.need_moderate = 0 \
	AND forum_id IN(:fids:) \
	ORDER BY p.post_id DESC

# ##########
# PollModel
# ##########
PollModel.addNewPoll = INSERT INTO jforum_vote_desc (topic_id, vote_text, vote_length, vote_start) VALUES (?, ?, ?, GETDATE())
PollModel.lastGeneratedPollId = SELECT IDENT_CURRENT('jforum_vote_desc') AS vote_desc_id

# #############
# ForumModel
# #############				
ForumModel.lastGeneratedForumId = SELECT IDENT_CURRENT('jforum_forums') AS forum_id

# #############
# TopicModel
# #############
TopicModel.selectAllByForumByLimit =  SELECT TOP ? \
    t.*, p.user_id AS last_user_id, p.post_time, (SELECT SUM(p.attach) \
        FROM jforum_posts p \
        WHERE p.topic_id = t.topic_id \
        AND p.need_moderate = 0) AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE (t.forum_id = ? OR t.topic_moved_id = ?) \
	AND p.post_id = t.topic_last_post_id \
	AND p.need_moderate = 0 \
	ORDER BY t.topic_type DESC, t.topic_last_post_id DESC

TopicModel.selectRecentTopicsByLimit = SELECT TOP ? \
	t.*, p.user_id AS last_user_id, p.post_time, (SELECT SUM(p.attach) \
        FROM jforum_posts p \
        WHERE p.topic_id = t.topic_id \
        AND p.need_moderate = 0) AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND p.need_moderate = 0  \
	ORDER BY t.topic_last_post_id DESC

TopicModel.selectHottestTopicsByLimit = SELECT TOP ? \
  t.*, p.user_id AS last_user_id, p.post_time, (SELECT SUM(p.attach) \
        FROM jforum_posts p \
        WHERE p.topic_id = t.topic_id \
        AND p.need_moderate = 0) AS attach \
  FROM jforum_topics t, jforum_posts p \
  WHERE p.post_id = t.topic_last_post_id \
  AND p.need_moderate = 0 \
  ORDER BY topic_views DESC

TopicModel.selectByUserByLimit = SELECT TOP ? \
    t.*, p.user_id AS last_user_id, p.post_time, (SELECT SUM(p.attach) \
        FROM jforum_posts p \
        WHERE p.topic_id = t.topic_id \
        AND p.need_moderate = 0) AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND t.user_id = ? \
	AND p.need_moderate = 0 \
	AND t.forum_id IN(:fids:) \
	ORDER BY t.topic_last_post_id DESC

TopicModel.lastGeneratedTopicId = SELECT IDENT_CURRENT('jforum_topics') AS topic_id 

# ############
# SearchModel
# ############
SearchModel.firstPostIdByDate = SELECT TOP 1 post_id FROM jforum_posts WHERE post_time > ?
SearchModel.lastPostIdByDate = SELECT TOP 1 post_id FROM jforum_posts WHERE post_time < ? ORDER BY post_id DESC

# #############
# PermissionControl
# #############
PermissionControl.lastGeneratedRoleId = SELECT IDENT_CURRENT('jforum_roles') AS role_id 

# #############
# SmiliesModel
# #############
SmiliesModel.lastGeneratedSmilieId = SELECT IDENT_CURRENT('jforum_smilies') AS smilie_id 

# #############
# PrivateMessagesModel
# #############
PrivateMessagesModel.lastGeneratedPmId = SELECT IDENT_CURRENT('jforum_privmsgs') AS privmsgs_id 

# ################
# AttachmentModel
# ################
AttachmentModel.lastGeneratedAttachmentId = SELECT IDENT_CURRENT('jforum_attach') AS attach_id

# ###############
# BanlistModel
# ###############
BanlistModel.lastGeneratedBanlistId = SELECT IDENT_CURRENT('jforum_banlist') AS banlist_id

# ################
# ModerationLog
# ################
ModerationLog.selectAll = SELECT TOP ? \
    l.*, u.username, u2.username AS poster_username FROM jforum_moderation_log l \
	LEFT JOIN jforum_users u2 ON u2.user_id = l.post_user_id \
	LEFT JOIN jforum_users u ON l.user_id = u.user_id \
	ORDER BY log_id DESC

ModerationLog.lastGeneratedModerationLogId = SELECT IDENT_CURRENT('jforum_moderation_log') AS moderation_log_id
