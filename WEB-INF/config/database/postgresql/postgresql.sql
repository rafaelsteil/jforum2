# ##########
# UserModel
# ##########
UserModel.selectAllByLimit = SELECT user_email, user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, user_website, user_viewemail \
	FROM jforum_users ORDER BY user_id OFFSET ? LIMIT ?

UserModel.lastGeneratedUserId = SELECT CURRVAL('jforum_users_seq')

UserModel.selectById = SELECT u.*, \
	(SELECT COUNT(1) FROM jforum_privmsgs pm \
	WHERE pm.privmsgs_to_userid = u.user_id \
	AND pm.privmsgs_type = 1) AS private_messages \
	FROM jforum_users u \
	WHERE u.user_id = ?
	
UserModel.selectAllByGroup = SELECT user_email, u.user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, user_website, user_viewemail \
	FROM jforum_users u, jforum_user_groups ug \
	WHERE u.user_id = ug.user_id \
	AND ug.group_id = ? \
	ORDER BY username \
	OFFSET ? LIMIT ?

# #############
# PostModel
# #############
PostModel.lastGeneratedPostId = SELECT CURRVAL('jforum_posts_seq')

PostModel.selectAllByTopicByLimit = SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND topic_id = ? \
	AND p.user_id = u.user_id \
	AND p.need_moderate = 0 \
	ORDER BY post_time ASC \
	OFFSET ? LIMIT ?

PostModel.selectByUserByLimit = SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND p.user_id = u.user_id \
	AND p.user_id = ? \
	AND p.need_moderate = 0 \
	AND forum_id IN(:fids:) \
	ORDER BY p.post_id DESC \
	OFFSET ? LIMIT ?

# ##########
# PollModel
# ##########
PollModel.lastGeneratedPollId = SELECT CURRVAL('jforum_vote_desc_seq')
		
# #############
# ForumModel
# #############
ForumModel.lastGeneratedForumId = SELECT CURRVAL('jforum_forums_seq');

# #############
# TopicModel
# #############
TopicModel.selectAllByForumByLimit = SELECT t.*, p.user_id AS last_user_id, p.post_time, (SELECT SUM(p.attach) \
        FROM jforum_posts p \
        WHERE p.topic_id = t.topic_id \
        AND p.need_moderate = 0) AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE (t.forum_id = ? OR t.topic_moved_id = ?) \
	AND p.post_id = t.topic_last_post_id \
	AND p.need_moderate = 0 \
	ORDER BY t.topic_type DESC, t.topic_last_post_id DESC \
	OFFSET ? LIMIT ?

TopicModel.selectByUserByLimit = SELECT t.*, p.user_id AS last_user_id, p.post_time, (SELECT SUM(p.attach) \
        FROM jforum_posts p \
        WHERE p.topic_id = t.topic_id \
        AND p.need_moderate = 0) AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND t.user_id = ? \
	AND p.need_moderate = 0 \
	AND t.forum_id IN(:fids:) \
	ORDER BY t.topic_last_post_id DESC \
	OFFSET ? LIMIT ?
		
TopicModel.lastGeneratedTopicId = SELECT CURRVAL('jforum_topics_seq')

# #####################
# PrivateMessagesModel
# #####################
PrivateMessagesModel.lastGeneratedPmId = SELECT CURRVAL('jforum_privmsgs_seq')

# #############
# SmiliesModel
# #############
SmiliesModel.lastGeneratedSmilieId = SELECT CURRVAL('jforum_smilies_seq')

# ##################
# PermissionControl
# ##################
PermissionControl.lastGeneratedRoleId = SELECT CURRVAL('jforum_roles_seq')

# ##############
# CategoryModel
# ##############
CategoryModel.lastGeneratedCategoryId = SELECT CURRVAL('jforum_categories_seq')

# ################
# AttachmentModel
# ################
AttachmentModel.lastGeneratedAttachmentId = SELECT CURRVAL('jforum_attach_seq')

# ##########
# UserModel
# ##########
UserModel.login = SELECT user_id FROM jforum_users WHERE lower(username) = lower(?) AND user_password = ?

# ###############
# BanlistModel
# ###############
BanlistModel.lastGeneratedBanlistId = SELECT CURRVAL('jforum_banlist_seq')

# ################
# ModerationLog
# ################
ModerationLog.lastGeneratedModerationLogId = SELECT CURRVAL('jforum_moderation_log_seq')

ModerationLog.selectAll = SELECT l.*, u.username, u2.username AS poster_username \
	FROM jforum_moderation_log l \
	LEFT JOIN jforum_users u2 ON u2.user_id = l.post_user_id \
	LEFT JOIN jforum_users u ON l.user_id = u.user_id \
	ORDER BY log_id DESC \
	OFFSET ? LIMIT ?
