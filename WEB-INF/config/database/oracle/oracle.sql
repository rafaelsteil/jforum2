# #############
# GroupModel
# #############
GroupModel.addNew = INSERT INTO jforum_groups (group_id, group_name, group_description, parent_id) VALUES (jforum_groups_seq.nextval, ?, ?, ?)

# #############
# CategoryModel
# #############
CategoryModel.addNew = INSERT INTO jforum_categories (categories_id, title, display_order, moderated) VALUES (jforum_categories_seq.nextval, ?, ?, ?)

# #############
# RankingModel
# #############
RankingModel.addNew = INSERT INTO jforum_ranks (rank_id, rank_title, rank_min, rank_special ) VALUES (jforum_ranks_seq.nextval, ?, ?, ? )

# #############
# ConfigModel
# #############
ConfigModel.insert = INSERT INTO jforum_config (config_id, config_name, config_value) VALUES (jforum_config_seq.nextval, ?, ?)

# ##########
# UserModel
# ##########
UserModel.addNew = INSERT INTO jforum_users (user_id, username, user_password, user_email, user_regdate, user_actkey, rank_id) VALUES (jforum_users_seq.nextval, ?, ?, ?, ?, ?, 0)

UserModel.selectAllByLimit = SELECT * FROM ( \
        SELECT user_email, user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, user_website, user_viewemail, ROW_NUMBER() OVER(ORDER BY username) - 1 LINENUM \
        FROM jforum_users ORDER BY username \
        ) \
        WHERE LINENUM >= ? AND LINENUM < ?

UserModel.lastGeneratedUserId = SELECT jforum_users_seq.currval FROM DUAL

UserModel.selectById = SELECT u.*, \
	(SELECT COUNT(1) FROM jforum_privmsgs pm \
	WHERE pm.privmsgs_to_userid = u.user_id \
	AND pm.privmsgs_type = 1) AS private_messages \
	FROM jforum_users u \
	WHERE u.user_id = ?

UserModel.lastUserRegistered = SELECT * FROM ( \
		SELECT user_id, username, ROW_NUMBER() OVER(ORDER BY user_regdate DESC) - 1 LINENUM FROM jforum_users ORDER BY user_regdate DESC \
	) \
	WHERE LINENUM = 0
	
UserModel.selectAllByGroup = SELECT * FROM ( \
	SELECT user_email, u.user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, user_website, user_viewemail, ROW_NUMBER() OVER(ORDER BY u.user_id) LINENUM \
	FROM jforum_users u, jforum_user_groups ug \
	WHERE u.user_id = ug.user_id \
	AND ug.group_id = ? \
	ORDER BY username ) WHERE LINENUM >= ? AND LINENUM <= ?

# #############
# PostModel
# #############
PostModel.selectLatestByForumForRSS = SELECT * FROM ( \
		SELECT p.topic_id, p.post_id, p.forum_id, pt.post_subject AS subject, pt.post_text, p.post_time, p.user_id, u.username, \
		ROW_NUMBER() OVER(ORDER BY t.topic_id DESC) - 1 LINENUM \
		FROM jforum_topics t, jforum_posts p, jforum_posts_text pt, jforum_users u \
		WHERE p.post_id = t.topic_first_post_id \
		AND p.topic_id = t.topic_id \
		AND p.user_id = u.user_id \
		AND p.post_id = pt.post_id \
		AND p.need_moderate = 0 \
		AND t.forum_id = ? \
		ORDER BY t.topic_id DESC \
	) \
	WHERE LINENUM <= ?
	
PostModel.selectHotForRSS = SELECT * FROM ( \
		SELECT t.topic_id, t.topic_title AS subject, p.post_id, t.forum_id, pt.post_text, p.post_time, p.user_id, u.username, \
		ROW_NUMBER() OVER(ORDER BY topic_first_post_id DESC) - 1 LINENUM \
		FROM jforum_topics t, jforum_posts p, jforum_posts_text pt, jforum_users u \
		WHERE p.post_id = t.topic_first_post_id \
		AND p.topic_id = t.topic_id \
		AND p.user_id = u.user_id \
		AND p.post_id = pt.post_id \
		AND p.need_moderate = 0  \
		ORDER BY topic_first_post_id DESC \
	) \
	WHERE LINENUM <= ?

PostModel.addNewPost = INSERT INTO jforum_posts (post_id, topic_id, forum_id, user_id, post_time, poster_ip, enable_bbcode, enable_html, enable_smilies, enable_sig, post_edit_time, need_moderate) \
	VALUES (jforum_posts_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)

PostModel.addNewPostText = INSERT INTO jforum_posts_text ( post_text, post_id, post_subject ) VALUES (EMPTY_BLOB(), ?, ?)
PostModel.addNewPostTextField = SELECT post_text from jforum_posts_text WHERE post_id = ? FOR UPDATE
PostModel.updatePostText = UPDATE jforum_posts_text SET post_subject = ?, post_text = EMPTY_BLOB() WHERE post_id = ?

PostModel.lastGeneratedPostId = SELECT jforum_posts_seq.currval FROM DUAL

PostModel.selectAllByTopicByLimit = SELECT * FROM ( \
    SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, p.need_moderate, \
   	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username,  \
   	ROW_NUMBER() OVER(ORDER BY p.post_time ASC) - 1 LINENUM \
   	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id  \
	AND topic_id = ? \
	AND p.user_id = u.user_id \
	AND p.need_moderate = 0 \
	ORDER BY post_time ASC \
) \
WHERE LINENUM >= ? AND LINENUM < ?

PostModel.selectByUserByLimit = SELECT * FROM ( \
    SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate, \
	ROW_NUMBER() OVER(ORDER BY p.post_id DESC) - 1 LINENUM \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND p.user_id = u.user_id \
	AND p.user_id = ? \
	AND p.need_moderate = 0 \
	AND forum_id IN(:fids:) \
	ORDER BY post_id DESC \
) \
WHERE LINENUM >= ? AND LINENUM < ?

# #############
# PollModel
# #############
PollModel.addNewPoll = INSERT INTO jforum_vote_desc (vote_id, topic_id, vote_text, vote_length, vote_start) \
	VALUES (jforum_vote_desc_seq.nextval, ?, ?, ?, SYSDATE)
	
PollModel.lastGeneratedPollId = SELECT jforum_vote_desc_seq.currval FROM DUAL

# #############
# ForumModel
# #############
ForumModel.addNew = INSERT INTO jforum_forums (forum_id, categories_id, forum_name, forum_desc, forum_order, moderated) VALUES (jforum_forums_seq.nextval, ?, ?, ?, ?, ?)
ForumModel.lastGeneratedForumId = SELECT jforum_forums_seq.currval FROM DUAL
ForumModel.statsFirstPostTime = SELECT MIN(post_time) FROM jforum_posts
ForumModel.statsFirstRegisteredUserTime = SELECT MIN(user_regdate) FROM jforum_users

# #############
# TopicModel
# #############
TopicModel.addNew = INSERT INTO jforum_topics (topic_id, forum_id, topic_title, user_id, topic_time, topic_first_post_id, topic_last_post_id, topic_type, moderated) \
	VALUES (jforum_topics_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?)

##########################################################################################
# Ignores attachements (0 as attach), but goes two orders of magnitude higher...
##########################################################################################
TopicModel.selectAllByForumByLimit = SELECT * FROM ( \
       SELECT t.*, p.user_id AS last_user_id, p.post_time, (SELECT SUM(p.attach) \
        FROM jforum_posts p \
        WHERE p.topic_id = t.topic_id \
        AND p.need_moderate = 0) AS attach, \
       ROW_NUMBER() OVER(ORDER BY topic_type DESC, topic_last_post_id DESC) - 1 LINENUM \
       FROM jforum_topics t, jforum_posts p \
       WHERE (t.forum_id = ? OR t.topic_moved_id = ?) \
       AND p.post_id = t.topic_last_post_id \
       AND p.need_moderate = 0 \
	) \
	WHERE LINENUM >= ? AND LINENUM < ?

TopicModel.selectByUserByLimit = SELECT * FROM ( \
    SELECT t.*, p.user_id AS last_user_id, p.post_time, (SELECT SUM(p.attach) \
        FROM jforum_posts p \
        WHERE p.topic_id = t.topic_id \
        AND p.need_moderate = 0) AS attach, \
    ROW_NUMBER() OVER(ORDER BY topic_last_post_id ASC) - 1 LINENUM \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND t.user_id = ? \
	AND p.need_moderate = 0 \
	AND t.forum_id IN(:fids:) \
	ORDER BY t.topic_last_post_id DESC \
) \
WHERE LINENUM >= ? AND LINENUM < ?

TopicModel.selectRecentTopicsByLimit = SELECT * FROM ( \
       SELECT t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach,  \
       ROW_NUMBER() OVER(ORDER BY topic_type DESC, topic_last_post_id DESC) - 1 LINENUM \
       FROM jforum_topics t, jforum_posts p \
       WHERE p.post_id = t.topic_last_post_id \
       AND p.need_moderate = 0 \
	) \
	WHERE LINENUM < ?

TopicModel.selectHottestTopicsByLimit = SELECT * FROM (\
	SELECT t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach, \
	ROW_NUMBER() OVER(ORDER BY topic_views DESC) - 1 LINENUM \
    FROM jforum_topics t, jforum_posts p \
    WHERE p.post_id = t.topic_last_post_id \
    AND p.need_moderate = 0 \
    ORDER BY topic_views DESC \
	) \
	WHERE LINENUM < ?

TopicModel.lastGeneratedTopicId = SELECT jforum_topics_seq.currval FROM DUAL

TopicModel.topicPosters = SELECT user_id, username, user_karma, user_avatar, user_allowavatar, user_regdate, user_posts, user_icq, \
	user_from, user_email, rank_id, user_sig, user_attachsig, user_viewemail, user_msnm, user_yim, user_website, user_sig, user_aim \
	FROM jforum_users \
	WHERE user_id IN (:ids:)

# ####################
# PrivateMessageModel
# ####################
PrivateMessageModel.add = INSERT INTO jforum_privmsgs (privmsgs_id, privmsgs_type, privmsgs_subject, privmsgs_from_userid, \
	privmsgs_to_userid, privmsgs_date, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, \
	privmsgs_attach_sig ) \
	VALUES (jforum_privmsgs_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ? )

PrivateMessagesModel.addText = INSERT INTO jforum_privmsgs_text ( privmsgs_id, privmsgs_text ) VALUES ( ?, EMPTY_BLOB() )
PrivateMessagesModel.addTextField = SELECT privmsgs_text from jforum_privmsgs_text WHERE privmsgs_id = ? FOR UPDATE
PrivateMessagesModel.lastGeneratedPmId = SELECT jforum_privmsgs_seq.currval FROM DUAL

# #############
# SmiliesModel
# #############
SmiliesModel.addNew = INSERT INTO jforum_smilies (smilie_id, code, url, disk_name) VALUES (jforum_smilies_seq.nextval, ?, ?, ?)

SmiliesModel.lastGeneratedSmilieId = SELECT jforum_smilies_seq.currval FROM DUAL

# ##################
# PermissionControl
# ##################
PermissionControl.addGroupRole = INSERT INTO jforum_roles (role_id, group_id, name) VALUES (jforum_roles_seq.nextval, ?, ?)
PermissionControl.lastGeneratedRoleId = SELECT jforum_roles_seq.currval FROM DUAL

# ##############
# CategoryModel
# ##############
CategoryModel.lastGeneratedCategoryId = SELECT jforum_categories_seq.currval  FROM DUAL

# ###########
# KarmaModel
# ###########
KarmaModel.add = INSERT INTO jforum_karma (karma_id, post_id, post_user_id, from_user_id, points, topic_id, rate_date) VALUES (jforum_karma_seq.nextval, ?, ?, ?, ?, ?, ?)

# ##############
# BookmarkModel
# ##############
BookmarkModel.add = INSERT INTO jforum_bookmarks (bookmark_id, user_id, relation_id, relation_type, public_visible, title, description) \
	VALUES (jforum_bookmarks_seq.nextval, ?, ?, ?, ?, ?, ?)

# ################
# AttachmentModel
# ################
AttachmentModel.addQuotaLimit = INSERT INTO jforum_quota_limit (quota_limit_id, quota_desc, quota_limit, quota_type) VALUES (jforum_quota_limit_seq.nextval, ?, ?, ?)
AttachmentModel.lastGeneratedAttachmentId = SELECT jforum_attach_seq.currval FROM dual

AttachmentModel.addExtensionGroup = INSERT INTO jforum_extension_groups (extension_group_id, name, allow, upload_icon, download_mode) \
	VALUES (jforum_extension_groups_seq.nextval, ?, ?, ?, ?)

AttachmentModel.addExtension = INSERT INTO jforum_extensions (extension_id, extension_group_id, description, upload_icon, extension, allow) \
	VALUES (jforum_extensions_seq.nextval, ?, ?, ?, ?, ?)

AttachmentModel.addAttachment = INSERT INTO jforum_attach (attach_id, post_id, privmsgs_id, user_id) VALUES (jforum_attach_seq.nextval, ?, ?, ?)

AttachmentModel.addAttachmentInfo = INSERT INTO jforum_attach_desc (attach_desc_id, attach_id, physical_filename, real_filename, description, \
	mimetype, filesize, upload_time, thumb, extension_id ) VALUES (jforum_attach_desc_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)
		
AttachmentModel.setGroupQuota = INSERT INTO jforum_attach_quota (attach_quota_id, group_id, quota_limit_id) VALUES (jforum_attach_quota_seq.nextval, ?, ?)

# ###############
# BanlistModel
# ###############
BanlistModel.lastGeneratedBanlistId = SELECT jforum_banlist_seq.currval FROM dual
BanlistModel.insert = INSERT INTO jforum_banlist (banlist_id, user_id, banlist_ip, banlist_email) VALUES (jforum_banlist_seq.nextval, ?, ?, ?)

# #############
# SearchModel
# ############
SearchModel.lastPostIdByDate = SELECT * FROM (\
	SELECT post_id, ROW_NUMBER() OVER(ORDER BY post_id DESC) -1 LINENUM \
	FROM jforum_posts WHERE post_time < ? ORDER BY post_id DESC) \
	WHERE LINENUM = 0

# ################
# ModerationLog
# ################
ModerationLog.addNew = INSERT INTO jforum_moderation_log (log_id, user_id, log_description, log_original_message, log_date, log_type, post_id, topic_id, post_user_id) VALUES (jforum_moderation_log_seq.nextval, ?, EMPTY_BLOB(), EMPTY_BLOB(), ?, ?, ?, ?, ?)
ModerationLog.setDescription = SELECT log_description FROM jforum_moderation_log WHERE log_id = ? FOR UPDATE
ModerationLog.setOriginalMessage = SELECT log_original_message FROM jforum_moderation_log WHERE log_id = ? FOR UPDATE
ModerationLog.lastGeneratedModerationLogId = SELECT jforum_moderation_log_seq.currval FROM dual

ModerationLog.selectAll = SELECT * FROM ( \
	SELECT l.*, u.username, u2.username AS poster_username, ROW_NUMBER() OVER(ORDER BY l.log_id DESC) -1 LINENUM \
	FROM jforum_moderation_log l \
    LEFT JOIN jforum_users u2 ON u2.user_id = l.post_user_id \
    LEFT JOIN jforum_users u ON l.user_id = u.user_id \
    ORDER BY log_id DESC \
	) \
	WHERE LINENUM >= ? AND LINENUM < ?