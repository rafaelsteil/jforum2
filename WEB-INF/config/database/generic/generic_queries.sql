# ############
# GroupModel 
# ############
GroupModel.selectAll = SELECT group_id, group_name, parent_id, group_description FROM jforum_groups ORDER BY group_name
GroupModel.selectById = SELECT group_id, group_name, parent_id, group_description FROM jforum_groups WHERE group_id = ?
GroupModel.canDelete = SELECT COUNT(1) AS total FROM jforum_user_groups WHERE group_id = ?
GroupModel.delete = DELETE FROM jforum_groups WHERE group_id = ?
GroupModel.update = UPDATE jforum_groups SET group_name = ?, parent_id = ?, group_description = ? WHERE group_id = ?
GroupModel.addNew = INSERT INTO jforum_groups (group_name, group_description, parent_id) VALUES (?, ?, ?)
GroupModel.selectUsersIds = SELECT user_id FROM jforum_user_groups WHERE group_id = ?

# #############
# CategoryModel
# #############
CategoryModel.selectById = SELECT categories_id, title, display_order, moderated FROM jforum_categories WHERE categories_id = ? ORDER BY title 
CategoryModel.selectAll = SELECT categories_id, title, display_order, moderated FROM jforum_categories ORDER BY display_order
CategoryModel.canDelete = SELECT COUNT(1) AS total FROM jforum_forums WHERE categories_id = ?
CategoryModel.delete = DELETE FROM jforum_categories WHERE categories_id = ?
CategoryModel.update = UPDATE jforum_categories SET title = ?, moderated = ? WHERE categories_id = ?
CategoryModel.addNew = INSERT INTO jforum_categories (title, display_order, moderated) VALUES (?, ?, ?)
CategoryModel.setOrderById = UPDATE jforum_categories SET display_order = ? WHERE categories_id = ?
CategoryModel.getMaxOrder = SELECT MAX(display_order) FROM jforum_categories

# #############
# RankingModel
# #############
RankingModel.selectById = SELECT * FROM jforum_ranks WHERE rank_id = ?
RankingModel.selectSpecials = SELECT * FROM jforum_ranks WHERE rank_special = 1
RankingModel.selectAll = SELECT * FROM jforum_ranks ORDER BY rank_min
RankingModel.delete = DELETE FROM jforum_ranks WHERE rank_id = ?
RankingModel.update = UPDATE jforum_ranks SET rank_title = ?, rank_image = ?, rank_special = ?, rank_min = ? WHERE rank_id = ?
RankingModel.addNew = INSERT INTO jforum_ranks ( rank_title, rank_min, rank_special) VALUES ( ?, ?, ? )

# #############
# ConfigModel
# #############
ConfigModel.insert = INSERT INTO jforum_config (config_name, config_value) VALUES (?, ?)
ConfigModel.selectById = SELECT config_name, config_value FROM jforum_config WHERE config_id = ?
ConfigModel.selectByName = SELECT config_name, config_value, config_id FROM jforum_config WHERE config_name = ?
ConfigModel.selectAll = SELECT config_name, config_value, config_id FROM jforum_config
ConfigModel.delete = DELETE FROM jforum_config WHERE config_id = ?
ConfigModel.update = UPDATE jforum_config SET config_value = ? WHERE config_name = ?

# ##########
# UserModel
# ##########
UserModel.pendingActivations = SELECT user_id, username, user_regdate FROM jforum_users WHERE user_actkey IS NOT NULL ORDER BY user_id

UserModel.selectById = SELECT COUNT(pm.privmsgs_to_userid) AS private_messages, u.* \
	FROM jforum_users u \
	LEFT JOIN jforum_privmsgs pm ON pm.privmsgs_type = 1 AND pm.privmsgs_to_userid = u.user_id \
	WHERE u.user_id = ? \
	GROUP BY pm.privmsgs_to_userid

UserModel.selectAll = SELECT user_email, user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, \
	user_website, user_viewemail FROM jforum_users ORDER BY user_id

UserModel.selectAllByLimit = SELECT user_email, user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, user_website, user_viewemail \
	FROM jforum_users ORDER BY username LIMIT ?, ?

UserModel.selectAllByGroup = SELECT user_email, u.user_id, user_posts, user_regdate, username, deleted, user_karma, user_from, \
	user_website, user_viewemail \
	FROM jforum_users u, jforum_user_groups ug \
	WHERE u.user_id = ug.user_id \
	AND ug.group_id = ? \
	ORDER BY user_id LIMIT ?, ?

UserModel.saveUserAuthHash = UPDATE jforum_users SET user_authhash = ? WHERE user_id = ?
UserModel.getUserAuthHash = SELECT user_authhash FROM jforum_users WHERE user_id = ?
UserModel.totalUsersByGroup = SELECT COUNT(1) FROM jforum_user_groups WHERE group_id = ?
UserModel.deletedStatus = UPDATE jforum_users SET deleted = ? WHERE user_id = ?
UserModel.isDeleted = SELECT deleted FROM jforum_users WHERE user_id = ?
UserModel.incrementPosts = UPDATE jforum_users SET user_posts = user_posts + 1 WHERE user_id = ?
UserModel.decrementPosts = UPDATE jforum_users SET user_posts = user_posts - 1 WHERE user_id = ?
UserModel.rankingId = UPDATE jforum_users SET rank_id = ? WHERE user_id = ?
UserModel.activeStatus = UPDATE jforum_users SET user_active = ? WHERE user_id = ?
UserModel.addNew = INSERT INTO jforum_users (username, user_password, user_email, user_regdate, user_actkey, rank_id) VALUES (?, ?, ?, ?, ?, 0)
UserModel.findByName = SELECT user_id, username, user_email, deleted FROM jforum_users WHERE UPPER(username) LIKE UPPER(?)
UserModel.findByEmail = SELECT * FROM jforum_users WHERE LOWER(user_email) = LOWER(?)
UserModel.selectByName = SELECT * FROM jforum_users WHERE LOWER(username) = LOWER(?)
UserModel.addNewWithId = INSERT INTO jforum_users (username, user_password, user_email, user_regdate, user_actkey, user_id) VALUES (?, ?, ?, ?, ?, ?)

UserModel.update = UPDATE jforum_users SET user_aim = ?, \
	user_avatar = ?,\
	gender = ?, \
	themes_id = ?,\
	user_allow_pm = ?, \
	user_allowavatar = ?, \
	user_allowbbcode = ?, \
	user_allowhtml = ?, \
	user_allowsmilies = ?, \
	user_email = ?, \
	user_from = ?, \
	user_icq = ?, \
	user_interests = ?, \
	user_occ = ?, \
	user_sig = ?, \
	user_website = ?, \
	user_yim = ?, \
	user_msnm = ?, \
	user_password = ?, \
	user_viewemail = ?, \
	user_viewonline = ?, \
	user_notify = ?, \
	user_attachsig = ?, \
	username = ?, \
	user_lang = ?, \
	user_notify_pm = ?, \
	user_biography = ?, \
	user_lastvisit = ?, \
	user_notify_always = ?, \
	user_notify_text = ?, \
	rank_id = ? \
	WHERE user_id = ?
	
UserModel.lastUserRegistered = SELECT user_id, username FROM jforum_users ORDER BY user_regdate DESC LIMIT 1
UserModel.totalUsers = SELECT COUNT(1) as total_users FROM jforum_users
UserModel.isUsernameRegistered = SELECT COUNT(1) as registered FROM jforum_users WHERE LOWER(username) = LOWER(?)
UserModel.login = SELECT user_id FROM jforum_users WHERE LOWER(username) = LOWER(?) AND user_password = ?
UserModel.loginMatch = SELECT user_id FROM jforum_users WHERE LOWER(username) = LOWER(?)
UserModel.addToGroup = INSERT INTO jforum_user_groups ( user_id, group_id ) VALUES ( ?, ? )
UserModel.removeFromGroup = DELETE FROM jforum_user_groups WHERE user_id = ? AND group_id = ?

UserModel.selectGroups = SELECT ug.group_id, g.group_name FROM jforum_user_groups ug, jforum_groups g \
	WHERE ug.group_id = g.group_id \
	AND ug.user_id = ? \
	ORDER BY g.group_id

UserModel.saveNewPassword = UPDATE jforum_users SET user_password = ?, security_hash = null WHERE user_email = ?
UserModel.validateLostPasswordHash = SELECT COUNT(1) AS valid FROM jforum_users WHERE security_hash = ? AND user_email = ?
UserModel.writeLostPasswordHash = UPDATE jforum_users SET security_hash = ? WHERE user_email = ?
UserModel.getUsernameByEmail = SELECT username FROM jforum_users WHERE user_email = ?
UserModel.validateActivationKeyHash = SELECT COUNT(1) AS valid FROM jforum_users WHERE user_actkey = ? AND user_id = ?
UserModel.writeUserActive = UPDATE jforum_users SET user_active = 1, user_actkey = NULL WHERE user_id = ?
UserModel.updateUsername = UPDATE jforum_users SET username = ? WHERE user_id = ?
UserModel.getUsername = SELECT username FROM jforum_users WHERE user_id = ?

# #############
# PostModel
# #############
PostModel.selectLatestByForumForRSS = SELECT p.topic_id, p.post_id, p.forum_id, pt.post_subject AS subject, pt.post_text, p.post_time, p.user_id, u.username \
	FROM jforum_topics t, jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = t.topic_first_post_id \
	AND p.topic_id = t.topic_id \
	AND p.user_id = u.user_id \
	AND p.post_id = pt.post_id \
	AND p.need_moderate = 0 \
	AND t.forum_id = ? \
	ORDER BY t.topic_id DESC \
	LIMIT ?
	
PostModel.selectHotForRSS = SELECT t.topic_id, t.topic_title AS subject, p.post_id, t.forum_id, pt.post_text, p.post_time, p.user_id, u.username \
	FROM jforum_topics t, jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = t.topic_first_post_id \
	AND p.topic_id = t.topic_id \
	AND p.user_id = u.user_id \
	AND p.post_id = pt.post_id \
	AND p.need_moderate = 0  \
	ORDER BY topic_first_post_id DESC \
	LIMIT ?

PostModel.countPreviousPosts = SELECT COUNT(p2.post_id) AS prev_posts \
	FROM jforum_posts p, jforum_posts p2 \
	WHERE p.post_id = ? \
	AND p2.topic_id = p.topic_id \
	AND p2.post_id <= ?

PostModel.selectById = SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, enable_html, \
	enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.attach, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND p.post_id = ? \
	AND p.user_id = u.user_id

PostModel.deletePost = DELETE FROM jforum_posts WHERE post_id = ?
PostModel.deletePostText = DELETE FROM jforum_posts_text WHERE post_id = ?

PostModel.updatePost = UPDATE jforum_posts SET topic_id = ?, forum_id = ?, enable_bbcode = ?, enable_html = ?, enable_smilies = ?, enable_sig = ?, post_edit_time = ?, post_edit_count = ?, poster_ip = ? WHERE post_id = ?
PostModel.updatePostText = UPDATE jforum_posts_text SET post_text = ?, post_subject = ? WHERE post_id = ?

PostModel.addNewPost = INSERT INTO jforum_posts (topic_id, forum_id, user_id, post_time, poster_ip, enable_bbcode, enable_html, enable_smilies, enable_sig, post_edit_time, need_moderate) \
	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)
PostModel.addNewPostText = INSERT INTO jforum_posts_text ( post_id, post_text, post_subject ) VALUES (?, ?, ?)

PostModel.selectAllByTopicByLimit = SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND topic_id = ? \
	AND p.user_id = u.user_id \
	AND p.need_moderate = 0 \
	ORDER BY post_time ASC \
	LIMIT ?, ?

PostModel.selectByUserByLimit = SELECT p.post_id, topic_id, forum_id, p.user_id, post_time, poster_ip, enable_bbcode, p.attach, \
	enable_html, enable_smilies, enable_sig, post_edit_time, post_edit_count, status, pt.post_subject, pt.post_text, username, p.need_moderate \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND p.user_id = u.user_id \
	AND p.user_id = ? \
	AND p.need_moderate = 0 \
	AND forum_id IN(:fids:) \
	ORDER BY p.post_id DESC \
	LIMIT ?, ?

PostModel.countUserPosts = SELECT COUNT(1) AS total FROM jforum_posts where user_id = ? AND forum_id IN (:fids:) AND need_moderate = 0
	
PostModel.setForumByTopic = UPDATE jforum_posts SET forum_id = ? WHERE topic_id = ?
PostModel.deleteByTopic = SELECT post_id, user_id FROM jforum_posts WHERE topic_id = ?

# #############
# PollModel
# #############
PollModel.addNewPoll = INSERT INTO jforum_vote_desc (topic_id, vote_text, vote_length, vote_start) VALUES (?, ?, ?, NOW())
PollModel.addNewPollOption = INSERT INTO jforum_vote_results (vote_id, vote_option_id, vote_option_text, vote_result) VALUES (?, ?, ?, 0)
PollModel.addNewVoter = INSERT INTO jforum_vote_voters (vote_id, vote_user_id, vote_user_ip) VALUES (?, ?, ?)
PollModel.selectById = SELECT vote_id, topic_id, vote_start, vote_length, vote_text FROM jforum_vote_desc WHERE vote_id = ?
PollModel.selectPollByTopicId SELECT vote_id, topic_id, vote_start, vote_length, vote_text FROM jforum_vote_desc WHERE topic_id = ?
PollModel.incrementVoteCount = UPDATE jforum_vote_results SET vote_result = vote_result + 1 WHERE vote_id = ? AND vote_option_id = ?
PollModel.selectMaxVoteId = SELECT MAX(vote_option_id) FROM jforum_vote_results WHERE vote_id = ?
PollModel.selectVoter = SELECT vote_id, vote_user_id, vote_user_ip FROM jforum_vote_voters WHERE vote_id = ? AND vote_user_id = ?
PollModel.deletePollVoters = DELETE FROM jforum_vote_voters WHERE vote_id = ?
PollModel.deletePollOption = DELETE FROM jforum_vote_results WHERE vote_id = ? AND vote_option_id = ?
PollModel.deleteAllPollOptions = DELETE FROM jforum_vote_results WHERE vote_id = ?
PollModel.deletePoll = DELETE FROM jforum_vote_desc WHERE vote_id = ?
PollModel.updatePollOption = UPDATE jforum_vote_results SET vote_option_text = ? WHERE vote_option_id = ? AND vote_id = ?
PollModel.updatePoll = UPDATE jforum_vote_desc SET vote_text = ?, vote_length = ? WHERE vote_id = ?

PollModel.selectOptionsByPollId = SELECT vote_id, vote_option_id, vote_option_text, vote_result \
	FROM jforum_vote_results WHERE vote_id = ? ORDER BY vote_option_id

PollModel.selectVoterByIP = SELECT vote_id, vote_user_id, vote_user_ip \
	FROM jforum_vote_voters WHERE vote_id = ? AND vote_user_ip = ?

# #############
# ForumModel
# #############
ForumModel.selectById = SELECT forum_id, forum_name, categories_id, forum_desc, forum_order, forum_topics, forum_last_post_id, moderated \
	FROM jforum_forums \
	WHERE forum_id = ?

ForumModel.selectAll = SELECT forum_id, forum_name, categories_id, forum_desc, forum_order, forum_topics, forum_last_post_id, moderated \
	FROM jforum_forums \
	ORDER BY forum_order ASC

ForumModel.selectAllForPermissions = SELECT forum_id, forum_name FROM jforum_forums ORDER BY forum_name

ForumModel.statsFirstPostTime = SELECT MIN(post_time) FROM jforum_posts WHERE post_time > 0
ForumModel.statsFirstRegisteredUserTime = SELECT MIN(user_regdate) FROM jforum_users WHERE user_regdate > 0
ForumModel.discoverForumId = SELECT forum_id FROM jforum_mail_integration WHERE forum_email = ?
ForumModel.countForumPosts = SELECT COUNT(1) FROM jforum_posts WHERE forum_id = ?
ForumModel.setModerated = UPDATE jforum_forums SET moderated = ? WHERE categories_id = ?
ForumModel.delete = DELETE FROM jforum_forums WHERE forum_id = ?
ForumModel.update = UPDATE jforum_forums SET categories_id = ?, forum_name = ?, forum_desc = ?, moderated = ? WHERE forum_id = ?
ForumModel.addNew = INSERT INTO jforum_forums (categories_id, forum_name, forum_desc, forum_order, moderated) VALUES (?, ?, ?, ?, ?)
ForumModel.updateLastPost = UPDATE jforum_forums SET forum_last_post_id = ? WHERE forum_id = ?
ForumModel.incrementTotalTopics = UPDATE jforum_forums SET forum_topics = forum_topics + ? WHERE forum_id = ?
ForumModel.decrementTotalTopics = UPDATE jforum_forums SET forum_topics = forum_topics - ? WHERE forum_id = ?
ForumModel.decrementTotalPosts = UPDATE jforum_forums SET total_posts = total_posts - ? WHERE forum_id = ?
ForumModel.getTotalTopics = SELECT COUNT(topic_id) as total FROM jforum_topics WHERE forum_id = ?
ForumModel.setOrderById = UPDATE jforum_forums SET forum_order = ? WHERE forum_id = ? 
ForumModel.getMaxOrder = SELECT MAX(forum_order) FROM jforum_forums

ForumModel.lastPostInfo = SELECT post_time, p.topic_id, t.topic_replies, post_id, u.user_id, username \
	FROM jforum_posts p, jforum_users u, jforum_topics t , jforum_forums f \
	WHERE t.forum_id = f.forum_id \
	AND t.topic_id = p.topic_id \
	AND f.forum_last_post_id = t.topic_last_post_id \
	AND t.topic_last_post_id = p.post_id \
	AND p.forum_id = ? \
	AND p.user_id = u.user_id \
    AND p.need_moderate = 0

ForumModel.getModeratorList = SELECT g.group_id AS id, g.group_name AS name \
	FROM jforum_groups g, jforum_roles r, jforum_role_values rv, jforum_roles r2 \
	WHERE g.group_id = r.group_id \
	AND r.role_id = rv.role_id \
	AND r.name = 'perm_moderation_forums' \
	AND rv.role_value = ? \
	AND r2.name = 'perm_moderation' \
	AND r2.group_id = g.group_id 		

ForumModel.totalMessages = SELECT COUNT(1) as total_messages FROM jforum_posts WHERE need_moderate = 0
ForumModel.getMaxPostId = SELECT MAX(post_id) AS post_id FROM jforum_posts WHERE forum_id = ?
ForumModel.moveTopics = UPDATE jforum_topics SET forum_id = ?, topic_moved_id = ? WHERE topic_id = ?
ForumModel.checkUnreadTopics = SELECT MAX(post_time), topic_id FROM jforum_posts WHERE forum_id = ? AND post_time > ? GROUP BY topic_id
ForumModel.latestTopicIdForfix = SELECT MAX(topic_id) AS topic_id FROM jforum_posts WHERE forum_id = ? AND need_moderate = 0
ForumModel.fixLatestPostData = UPDATE jforum_topics SET topic_last_post_id = ? WHERE topic_id = ?
ForumModel.fixForumLatestPostData = UPDATE jforum_forums SET forum_last_post_id = ? WHERE forum_id = ?

ForumModel.getUnreadForums = SELECT t.forum_id, t.topic_id, p.post_time \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND p.post_time > ?

ForumModel.subscribeUser = INSERT INTO jforum_forums_watch(forum_id, user_id) VALUES (?, ?)
ForumModel.isUserSubscribed = SELECT user_id FROM jforum_forums_watch WHERE forum_id = ? AND user_id = ?
ForumModel.removeSubscription = DELETE FROM jforum_forums_watch WHERE forum_id = ? AND user_id = ?
ForumModel.removeSubscriptionByForum = DELETE FROM jforum_forums_watch WHERE forum_id = ?

ForumModel.notifyUsers = SELECT u.user_id, u.username, u.user_lang, u.user_email, \
	u.user_notify_always, u.user_notify_text \
	FROM jforum_forums_watch fw, jforum_users u \
	WHERE fw.user_id = u.user_id \
	AND fw.forum_id = ? \
	AND u.user_id NOT IN ( ?, ? )
	AND u.user_notify_always IN (0, 1)

# #############
# TopicModel
# #############
TopicModel.findTopicsByDateRange = SELECT DISTINCT topic_id FROM jforum_posts WHERE post_time >= ? AND post_time <= ?

TopicModel.selectById = SELECT t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE t.topic_id = ? \
	AND p.post_id = t.topic_last_post_id
	
TopicModel.selectRaw = SELECT topic_id, forum_id, topic_title, user_id, topic_views, topic_replies, topic_status, topic_vote_id, topic_type, \
	topic_first_post_id, topic_last_post_id, moderated, topic_time, topic_moved_id \
	FROM jforum_topics WHERE topic_id = ?

TopicModel.selectAllByForumByLimit = SELECT t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE (t.forum_id = ? OR t.topic_moved_id = ?) \
	AND p.post_id = t.topic_last_post_id \
	AND p.need_moderate = 0 \
	ORDER BY t.topic_type DESC, t.topic_last_post_id DESC \
	LIMIT ?, ?

TopicModel.topicPosters = SELECT user_id, username, user_karma, user_avatar, user_allowavatar, user_regdate, user_posts, user_icq, \
	user_from, user_email, rank_id, user_sig, user_attachsig, user_viewemail, user_msnm, user_yim, user_website, user_sig, user_aim \
	FROM jforum_users \
	WHERE user_id IN (:ids:)

TopicModel.distinctPosters = SELECT DISTINCT user_id FROM jforum_posts WHERE topic_id = ?

TopicModel.selectTopicTitlesByIds = SELECT topic_id, topic_title FROM jforum_topics WHERE topic_id IN (:ids:)
TopicModel.setModerationStatus = UPDATE jforum_topics SET moderated = ? WHERE forum_id = ?
TopicModel.setModerationStatusByTopic = UPDATE jforum_topics SET moderated = ? WHERE topic_id = ?
TopicModel.deleteByForum = SELECT topic_id FROM jforum_topics where forum_id = ?

TopicModel.delete = DELETE FROM jforum_topics WHERE topic_id = ?
TopicModel.deletePosts = DELETE FROM jforum_posts WHERE topic_id = ?
TopicModel.incrementTotalViews = UPDATE jforum_topics SET topic_views = topic_views + 1 WHERE topic_id = ?
TopicModel.incrementTotalReplies = UPDATE jforum_topics SET topic_replies = topic_replies + 1 WHERE topic_id = ?
TopicModel.decrementTotalReplies = UPDATE jforum_topics SET topic_replies = topic_replies - 1 WHERE topic_id = ?
TopicModel.setLastPostId = UPDATE jforum_topics SET topic_last_post_id = ? WHERE topic_id = ?
TopicModel.setFirstPostId = UPDATE jforum_topics SET topic_first_post_id = ? WHERE topic_id = ?
TopicModel.getMinPostId = SELECT MIN(post_id) AS post_id FROM jforum_posts WHERE topic_id = ?

TopicModel.addNew = INSERT INTO jforum_topics (forum_id, topic_title, user_id, topic_time, topic_first_post_id, topic_last_post_id, topic_type, moderated) \
	VALUES (?, ?, ?, ?, ?, ?, ?, ?)

TopicModel.update = UPDATE jforum_topics SET topic_title = ?, topic_last_post_id = ?, topic_first_post_id = ?, topic_type = ?, moderated = ?, topic_vote_id = ? WHERE topic_id = ?
TopicModel.getMaxPostId = SELECT MAX(post_id) AS post_id FROM jforum_posts WHERE topic_id = ?
TopicModel.getTotalPosts = SELECT COUNT(1) AS total FROM jforum_posts WHERE topic_id = ? AND need_moderate = 0

TopicModel.subscribeUser = INSERT INTO jforum_topics_watch(topic_id, user_id, is_read) VALUES (?, ?, 1)
TopicModel.isUserSubscribed = SELECT user_id FROM jforum_topics_watch WHERE topic_id = ? AND user_id = ?
TopicModel.removeSubscription = DELETE FROM jforum_topics_watch WHERE topic_id = ? AND user_id = ?
TopicModel.removeSubscriptionByTopic = DELETE FROM jforum_topics_watch WHERE topic_id = ?
TopicModel.updateReadStatus = UPDATE jforum_topics_watch SET is_read = ? WHERE topic_id = ? AND user_id = ?

TopicModel.notifyUsers = SELECT u.user_id, u.username, u.user_lang, u.user_email, u.user_notify_text \
	FROM jforum_topics_watch tw, jforum_users u \
	WHERE tw.user_id = u.user_id \
	AND tw.topic_id = ? \
	AND (tw.is_read = 1 OR u.user_notify_always = 1) \
	AND u.user_id NOT IN ( ?, ? )
	
TopicModel.markAllAsUnread = UPDATE jforum_topics_watch SET is_read = '0' WHERE topic_id = ? AND user_id NOT IN (?, ?)
TopicModel.lockUnlock = UPDATE jforum_topics SET topic_status = ? WHERE topic_id = ?

TopicModel.selectRecentTopicsByLimit = SELECT t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND p.need_moderate = 0 \
	ORDER BY topic_last_post_id DESC \
	LIMIT ?
	
TopicModel.selectForNewMessages = SELECT t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE t.topic_id IN (:topicIds:) \
	AND p.need_moderate = 0 \
	AND p.post_id = t.topic_last_post_id \
	ORDER BY topic_last_post_id DESC

TopicModel.selectHottestTopicsByLimit = SELECT t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
    FROM jforum_topics t, jforum_posts p \
    WHERE p.post_id = t.topic_last_post_id \
    AND p.need_moderate = 0 \
    ORDER BY topic_views DESC \
    LIMIT ?
    
TopicModel.getUserInformation = SELECT user_id, username FROM jforum_users WHERE user_id IN (#ID#)

TopicModel.selectByUserByLimit = SELECT t.*, p.user_id AS last_user_id, p.post_time, p.attach AS attach \
	FROM jforum_topics t, jforum_posts p \
	WHERE p.post_id = t.topic_last_post_id \
	AND t.user_id = ? \
	AND p.need_moderate = 0 \
	AND t.forum_id IN(:fids:) \
	ORDER BY t.topic_last_post_id DESC \
	LIMIT ?, ?

TopicModel.countUserTopics = SELECT COUNT(1) AS total FROM jforum_topics t, jforum_posts p WHERE t.user_id = ? AND t.forum_id IN (:fids:) AND p.post_id = t.topic_first_post_id AND p.need_moderate = 0
	
TopicModel.getFirstLastPostId = SELECT MIN(post_id) AS first_post_id, MAX(post_id) AS last_post_id FROM jforum_posts WHERE topic_id = ?
TopicModel.fixFirstLastPostId = UPDATE jforum_topics SET topic_first_post_id = ?, topic_last_post_id = ? WHERE topic_id = ?
TopicModel.totalTopics = SELECT COUNT(1) FROM jforum_topics

# ############
# SearchModel
# ############
SearchModel.getFirstPostId = SELECT MIN(post_id) FROM jforum_posts
SearchModel.firstPostIdByDate = SELECT post_id FROM jforum_posts WHERE post_time > ? LIMIT 1
SearchModel.lastPostIdByDate = SELECT post_id FROM jforum_posts WHERE post_time < ? ORDER BY post_id DESC LIMIT 1

SearchModel.getPostsToIndexForLucene = SELECT p.post_id, p.forum_id, p.enable_bbcode, p.enable_smilies, '' AS topic_title, p.topic_id, p.user_id, p.post_time, pt.post_text, pt.post_subject \
	FROM jforum_posts p, jforum_posts_text pt \
	WHERE p.post_id = pt.post_id \
	AND p.post_id >= ? AND p.post_id < ?

SearchModel.getPostsDataForLucene = SELECT p.post_id, p.forum_id, p.topic_id, p.user_id, u.username, p.enable_bbcode, p.enable_smilies, p.post_time, pt.post_subject, pt.post_text, t.topic_title \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u, jforum_topics t \
	WHERE p.post_id IN (:posts:) \
	AND p.post_id = pt.post_id  \
	AND p.topic_id = t.topic_id \
	AND p.user_id = u.user_Id

# ##########
# TreeGroup
# ##########
TreeGroup.selectGroup = SELECT group_id, group_name FROM jforum_groups WHERE parent_id = ? ORDER BY group_name

# ################
# PermissionControl
# ################
PermissionControl.deleteAllRoleValues = DELETE FROM jforum_role_values WHERE role_id IN (SELECT role_id FROM jforum_roles WHERE group_id = ?)
PermissionControl.deleteAllGroupRoles = DELETE FROM jforum_roles WHERE group_id = ?
PermissionControl.addGroupRole = INSERT INTO jforum_roles (group_id, name) VALUES (?, ?)
PermissionControl.addRoleValues = INSERT INTO jforum_role_values (role_id, role_value) VALUES (?, ?)
PermissionControl.getRoleIdByName = SELECT role_id FROM jforum_roles WHERE name = ? AND group_id = ?

PermissionControl.selectForumRoles = SELECT DISTINCT r.role_id FROM jforum_roles r, jforum_role_values rv \
	WHERE r.role_id = rv.role_id \
	AND r.name in ('perm_forum', 'perm_anonymous_post', 'perm_reply_only', 'perm_read_only_forums', 'perm_reply_without_moderation', 'perm_html_disabled', 'perm_attachments_enabled', 'perm_moderation_forums') \
	AND rv.role_value = ?

PermissionControl.deleteRoleValues = DELETE FROM jforum_role_values WHERE role_value = ? AND role_id IN (#IDS#) 

PermissionControl.loadGroupRoles = SELECT r.name, '0' AS role_value FROM jforum_roles r WHERE r.group_id IN (#IN#) \
	UNION \
	SELECT r.name, rv.role_value \
	FROM jforum_roles r, jforum_role_values rv \
	WHERE r.role_id = rv.role_id  \
	AND r.group_id IN (#IN#) \
	ORDER BY name

# #############
# TopicListing
# #############
TopicListing.selectTopicData = SELECT topic_id, topic_title, topic_views, topic_replies, topic_last_post_id, user_id FROM jforum_topics WHERE forum_id = ?

# #############
# SmiliesModel
# #############
SmiliesModel.addNew = INSERT INTO jforum_smilies ( code, url, disk_name) VALUES (?, ?, ?)
SmiliesModel.delete = DELETE FROM jforum_smilies WHERE smilie_id = ?
SmiliesModel.update = UPDATE jforum_smilies SET code = ?, url = ?, disk_name =? WHERE smilie_id = ?
SmiliesModel.selectAll = SELECT * FROM jforum_smilies ORDER BY smilie_id
SmiliesModel.selectById = SELECT * FROM jforum_smilies WHERE smilie_id = ?

# ####################
# PrivateMessageModel
# ####################
PrivateMessageModel.add = INSERT INTO jforum_privmsgs ( privmsgs_type, privmsgs_subject, privmsgs_from_userid, \
	privmsgs_to_userid, privmsgs_date, privmsgs_enable_bbcode, privmsgs_enable_html, privmsgs_enable_smilies, \
	privmsgs_attach_sig ) \
	VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )
	
PrivateMessagesModel.addText = INSERT INTO jforum_privmsgs_text ( privmsgs_id, privmsgs_text ) VALUES (?, ?)
	
PrivateMessagesModel.isDeleteAllowed = SELECT 1 FROM jforum_privmsgs WHERE privmsgs_id = ? \
	AND ( \
	    (privmsgs_from_userid = ? AND privmsgs_type = 2) \
	    OR (privmsgs_to_userid = ? AND privmsgs_type IN(0, 1, 5)) \
	)

PrivateMessageModel.delete = DELETE FROM jforum_privmsgs WHERE privmsgs_id = ?	
PrivateMessagesModel.deleteText = DELETE FROM jforum_privmsgs_text WHERE privmsgs_id = ?

PrivateMessageModel.baseListing = SELECT pm.privmsgs_type, pm.privmsgs_id, pm.privmsgs_date, pm.privmsgs_subject, u.user_id, u.username \
	FROM jforum_privmsgs pm, jforum_users u \
	#FILTER# \
	ORDER BY pm.privmsgs_date DESC
	
PrivateMessageModel.inbox = WHERE privmsgs_to_userid = ? \
	AND u.user_id = pm.privmsgs_from_userid \
	AND ( pm.privmsgs_type = 1 \
	OR pm.privmsgs_type = 0 \
	OR privmsgs_type = 5)
	
PrivateMessageModel.sent = WHERE privmsgs_from_userid = ? \
	AND u.user_id = pm.privmsgs_to_userid \
	AND pm.privmsgs_type = 2
	
PrivateMessageModel.updateType = UPDATE jforum_privmsgs SET privmsgs_type = ? WHERE privmsgs_id = ?

PrivateMessageModel.selectById = SELECT p.*, pt.privmsgs_text \
	FROM jforum_privmsgs p, jforum_privmsgs_text pt \
	WHERE p.privmsgs_id = pt.privmsgs_id \
	AND p.privmsgs_id = ?


# #################
# UserSessionModel
# #################
UserSessionModel.add = INSERT INTO jforum_sessions ( session_id, session_user_id, session_start ) VALUES (?, ?, ?)
UserSessionModel.update = UPDATE jforum_sessions SET session_start = ?, session_time = ?, session_id = ? WHERE session_user_id = ?
UserSessionModel.delete = DELETE FROM jforum_sessions WHERE session_user_id = ?
UserSessionModel.selectById = SELECT session_time, session_start, session_id FROM jforum_sessions WHERE session_user_id = ?

# ###########
# KarmaModel
# ###########
KarmaModel.add = INSERT INTO jforum_karma (post_id, post_user_id, from_user_id, points, topic_id, rate_date) VALUES (?, ?, ?, ?, ?, ?)
KarmaModel.update = UPDATE jforum_karma SET points = ? WHERE karma_id = ?
KarmaModel.getUserKarma = SELECT user_karma FROM jforum_users WHERE user_id = ?
KarmaModel.updateUserKarma = UPDATE jforum_users SET user_karma = ? WHERE user_id = ?
KarmaModel.getPostKarma = SELECT SUM(points) / COUNT(post_id) AS points FROM jforum_karma WHERE post_id = ?
KarmaModel.userCanAddKarma = SELECT COUNT(1) FROM jforum_karma WHERE post_id = ? AND from_user_id = ?

KarmaModel.getUserKarmaPoints = SELECT SUM(points) AS points, COUNT(1) AS votes, from_user_id \
	FROM jforum_karma WHERE post_user_id = ? GROUP BY from_user_id
KarmaModel.getUserVotes = SELECT points, post_id FROM jforum_karma WHERE topic_id = ? AND from_user_id = ?	

#Frankiln Tests
KarmaModel.getUserGivenVotes = SELECT COUNT(post_id) AS votes FROM jforum_karma WHERE from_user_id = ?
KarmaModel.getUserTotalVotes = SELECT SUM(points) AS points, COUNT(post_id) AS votes FROM jforum_karma WHERE post_user_id = ?

KarmaModel.getMostRatedUserByPeriod = SELECT u.user_id, u.username, SUM(points) AS total, \
	  COUNT(post_user_id) AS votes_received, user_karma, \
	  -1 AS given \
	  FROM jforum_users u, jforum_karma k \
	  WHERE u.user_id = k.post_user_id \
	  AND k.rate_date BETWEEN ? AND ? \
	  GROUP BY u.user_id, u.username, user_karma

# ##############
# BookmarkModel
# ##############
BookmarkModel.add = INSERT INTO jforum_bookmarks (user_id, relation_id, relation_type, public_visible, title, description) VALUES (?, ?, ?, ?, ?, ?)
BookmarkModel.update = UPDATE jforum_bookmarks SET public_visible = ?, title = ?, description = ? WHERE bookmark_id = ?
BookmarkModel.remove = DELETE FROM jforum_bookmarks WHERE bookmark_id = ?

BookmarkModel.selectForumBookmarks = SELECT b.bookmark_id, b.user_id, b.relation_type, b.relation_id, b.public_visible, b.title, b.description, f.forum_name, f.forum_desc \
	FROM jforum_bookmarks b, jforum_forums f \
	WHERE b.relation_type = 1 \
	AND b.relation_id = f.forum_id \
	AND b.user_id = ? \
	ORDER BY f.forum_name
	
BookmarkModel.selectTopicBookmarks = SELECT b.bookmark_id, b.user_id, b.relation_type, b.relation_id, b.public_visible, b.title, b.description, t.topic_title \
	FROM jforum_bookmarks b, jforum_topics t \
	WHERE b.relation_type = 2 \
	AND b.relation_id = t.topic_id \
	AND b.user_id = ? \
	ORDER BY t.topic_title
	
BookmarkModel.selectUserBookmarks = SELECT b.bookmark_id, b.user_id, b.relation_type, b.relation_id, b.public_visible, b.title, b.description, u.username \
	FROM jforum_bookmarks b, jforum_users u \
	WHERE b.relation_type = 3 \
	AND b.relation_id = u.user_id \
	AND b.user_id = ? \
	ORDER BY u.username
	
BookmarkModel.selectAllFromUser = SELECT b.bookmark_id, b.user_id, b.relation_type, b.relation_id, b.public_visible, b.title, b.description \
	FROM jforum_bookmarks b \
	WHERE b.user_id = ? \
	ORDER BY b.title

BookmarkModel.selectForUpdate = SELECT bookmark_id, relation_id, public_visible, relation_type, title, description, user_id \
	FROM jforum_bookmarks WHERE relation_id = ? AND relation_type = ? AND user_id = ?
	
BookmarkModel.selectById = SELECT bookmark_id, relation_id, public_visible, title, description, user_id, relation_type \
	FROM jforum_bookmarks WHERE bookmark_id = ?
	
# ################
# AttachmentModel
# ################
AttachmentModel.addQuotaLimit = INSERT INTO jforum_quota_limit (quota_desc, quota_limit, quota_type) VALUES (?, ?, ?)
AttachmentModel.updateQuotaLimit = UPDATE jforum_quota_limit SET quota_desc = ?, quota_limit = ?, quota_type = ? WHERE quota_limit_id = ?
AttachmentModel.removeQuotaLimit = DELETE FROM jforum_quota_limit WHERE quota_limit_id = ?

AttachmentModel.selectQuotaLimit = SELECT quota_limit_id, quota_desc, quota_limit, quota_type \
	FROM jforum_quota_limit ORDER BY quota_type, quota_limit
	
AttachmentModel.addExtensionGroup = INSERT INTO jforum_extension_groups (name, allow, upload_icon, download_mode) VALUES (?, ?, ?, ?)

AttachmentModel.updateExtensionGroups = UPDATE jforum_extension_groups SET name = ?, allow = ?, upload_icon = ?, download_mode = ? \
	WHERE extension_group_id = ?

AttachmentModel.removeExtensionGroups = DELETE FROM jforum_extension_groups WHERE extension_group_id = ?
AttachmentModel.selectExtensionGroups = SELECT extension_group_id, name, allow, upload_icon, download_mode FROM jforum_extension_groups ORDER BY name

AttachmentModel.addExtension = INSERT INTO jforum_extensions (extension_group_id, description, upload_icon, extension, allow) VALUES (?, ?, ?, ?, ?)

AttachmentModel.updateExtension = UPDATE jforum_extensions SET extension_group_id = ?, description = ?, upload_icon = ?, extension = ?, allow = ? \
	WHERE extension_id = ?

AttachmentModel.removeExtension = DELETE FROM jforum_extensions WHERE extension_id = ?
AttachmentModel.addAttachment = INSERT INTO jforum_attach (post_id, privmsgs_id, user_id) VALUES (?, ?, ?)

AttachmentModel.addAttachmentInfo = INSERT INTO jforum_attach_desc (attach_id, physical_filename, real_filename, description, \
	mimetype, filesize, upload_time, thumb, extension_id ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
	
AttachmentModel.updatePost = UPDATE jforum_posts SET attach = ? WHERE post_id = ?

AttachmentModel.selectExtensions = SELECT extension_id, extension_group_id, extension, description, upload_icon, allow, '' AS group_icon FROM jforum_extensions ORDER BY extension

AttachmentModel.selectExtension = SELECT e.extension_id, e.extension_group_id, e.extension, e.description, e.upload_icon, e.allow, g.upload_icon AS group_icon \
	FROM jforum_extensions e, jforum_extension_groups g \
	WHERE e.$field = ? \
	AND e.extension_group_id = g.extension_group_id

AttachmentModel.extensionsForSecurity = SELECT e.extension, e.allow, eg.allow AS group_allow \
	FROM jforum_extensions e, jforum_extension_groups eg \
	WHERE e.extension_group_id = eg.extension_group_id

AttachmentModel.isPhysicalDownloadMode = SELECT download_mode FROM jforum_extension_groups WHERE extension_group_id = ?

AttachmentModel.selectAttachments = SELECT a.attach_id, a.user_id, a.post_id, a.privmsgs_id, d.mimetype, d.physical_filename, d.real_filename, \
	d.download_count, d.description, d.filesize, d.upload_time, d.extension_id \
	FROM jforum_attach a, jforum_attach_desc d \
	WHERE a.post_id = ? \
	AND a.attach_id = d.attach_id
	
AttachmentModel.selectAttachmentById = SELECT a.attach_id, a.user_id, a.post_id, a.privmsgs_id, d.mimetype, d.physical_filename, d.real_filename, \
	d.download_count, d.description, d.filesize, d.upload_time, d.extension_id \
	FROM jforum_attach a, jforum_attach_desc d \
	WHERE a.attach_id = ? \
	AND a.attach_id = d.attach_id
	
AttachmentModel.updateAttachment = UPDATE jforum_attach_desc SET description = ?, download_count = ? WHERE attach_id = ?
AttachmentModel.removeAttachment = DELETE FROM jforum_attach WHERE attach_id = ?
AttachmentModel.removeAttachmentInfo = DELETE FROM jforum_attach_desc WHERE attach_id = ?
AttachmentModel.countPostAttachments = SELECT COUNT(1) FROM jforum_attach WHERE post_id = ?
AttachmentModel.deleteGroupQuota = DELETE FROM jforum_attach_quota
AttachmentModel.setGroupQuota = INSERT INTO jforum_attach_quota (group_id, quota_limit_id) VALUES (?, ?)
AttachmentModel.selectGroupsQuotaLimits = SELECT group_id, quota_limit_id FROM jforum_attach_quota

AttachmentModel.selectQuotaLimitByGroup = SELECT ql.quota_limit_id, ql.quota_desc, ql.quota_limit, ql.quota_type \
	FROM jforum_quota_limit ql, jforum_attach_quota at \
	WHERE ql.quota_limit_id = at.quota_limit_id \
	AND at.group_id = ?
	
# ################
# ModerationModel
# ################
ModerationModel.aprovePost = UPDATE jforum_posts SET need_moderate = 0, post_time = ? WHERE post_id = ?
ModerationModel.categoryPendingModeration = SELECT c.categories_id, c.title, f.forum_id, f.forum_name, COUNT(p.post_id) AS total \
	FROM jforum_categories c, jforum_forums f, jforum_posts p \
	WHERE p.need_moderate = 1 \
	AND p.forum_id = f.forum_id \
	AND f.categories_id = c.categories_id \
	GROUP BY c.categories_id, c.title, f.forum_id, f.forum_name

ModerationModel.topicsByForum = SELECT p.post_id, t.topic_id, t.topic_title, t.topic_replies, p.user_id, enable_bbcode, p.attach, \
	enable_html, enable_smilies, pt.post_subject, pt.post_text, username \
	FROM jforum_posts p, jforum_posts_text pt, jforum_users u, jforum_topics t \
	WHERE p.post_id = pt.post_id \
	AND p.topic_id = t.topic_id \
	AND t.forum_id = ? \
	AND p.user_id = u.user_id \
	AND p.need_moderate = 1 \
	ORDER BY t.topic_id, post_time ASC 

# #############
# BannerDAO
# #############
BannerDAO.selectById = SELECT banner_id, name, banner_placement, banner_description, banner_clicks, banner_views, \
	banner_url, banner_weight, banner_active, banner_comment, banner_type, banner_width, banner_height \
	FROM jforum_banner \
	WHERE banner_id = ?
	
BannerDAO.selectAll = SELECT banner_id, banner_name, banner_placement, banner_description, banner_clicks, banner_views, \
	banner_url, banner_weight, banner_active, banner_comment, banner_type, banner_width, banner_height \
	FROM jforum_banner \
	ORDER BY comment
	
BannerDAO.canDelete = SELECT COUNT(1) AS total FROM jforum_banner WHERE banner_id = ?
BannerDAO.delete = DELETE FROM jforum_banner WHERE banner_id = ?

BannerDAO.update = UPDATE jforum_banner SET banner_name = ?, banner_placement = ?, banner_description = ?, banner_clicks = ?, \
	banner_views = ?, banner_url = ?, banner_weight = ?, banner_active = ?, banner_comment = ?, banner_type = ?, \
	banner_width = ?, banner_height = ? \
	WHERE banner_id = ?

BannerDAO.addNew = INSERT INTO jforum_banner (banner_name, banner_placement, banner_description, banner_clicks, banner_views, banner_url, banner_weight, \
	banner_active, banner_comment, banner_type, banner_width, banner_height) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

BannerDAO.selectActiveBannerByPlacement = SELECT banner_id, banner_name, banner_placement, banner_description, banner_clicks, \
	banner_views, banner_url, \
	banner_weight, banner_active, banner_comment, banner_type, banner_width, banner_height \
	FROM jforum_banner \
	WHERE banner_placement = ? \
	AND banner_active = 1 \
	ORDER BY banner_weight ASC

##############
# SummaryDAO
##############

SummaryDAO.selectAllRecipients = SELECT username, user_email FROM jforum_users 

SummaryDAO.selectPosts = SELECT p.post_id, p.topic_id, p.forum_id, p.user_id, post_time, \
	pt.post_subject, pt.post_text, username \
	FROM jforum_topics t,jforum_posts p, jforum_posts_text pt, jforum_users u \
	WHERE p.post_id = pt.post_id \
	AND p.post_id = t.topic_first_post_id \
	AND p.user_id = u.user_id \
	AND p.need_moderate = 0 \
	AND status = 1 \
	AND t.topic_time BETWEEN ? AND ? \
	ORDER BY post_time DESC 
###################
# MailIntegration
###################
MailIntegration.findAll = SELECT * FROM jforum_mail_integration
MailIntegration.find = SELECT * FROM jforum_mail_integration WHERE forum_id = ?
MailIntegration.delete = DELETE FROM jforum_mail_integration WHERE forum_id = ?
MailIntegration.add = INSERT INTO jforum_mail_integration (forum_id, forum_email, pop_host, pop_username, pop_password, pop_port, pop_ssl) VALUES (?, ?, ?, ?, ?, ?, ?)
MailIntegration.update = UPDATE jforum_mail_integration SET forum_id = ?, forum_email = ?, pop_host = ?, pop_username = ?, pop_password = ?, pop_port = ?, pop_ssl = ? WHERE forum_id = ?

############
# ApiModel
############
ApiModel.isValid = SELECT 1 FROM jforum_api WHERE api_key = ?

# ###############
# BanlistModel
# ###############
BanlistModel.delete = DELETE FROM jforum_banlist WHERE banlist_id = ?
BanlistModel.insert = INSERT INTO jforum_banlist (user_id, banlist_ip, banlist_email) VALUES (?, ?, ?)
BanlistModel.selectAll = SELECT * FROM jforum_banlist ORDER BY banlist_id


# ################
# ModerationLog
# ################
ModerationLog.addNew = INSERT INTO jforum_moderation_log (user_id, log_description, log_original_message, log_date, log_type, post_id, topic_id, post_user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
ModerationLog.totalRecords = SELECT COUNT(log_id) FROM jforum_moderation_log

ModerationLog.selectAll = SELECT l.*, u.username, u2.username AS poster_username FROM jforum_moderation_log l \
	LEFT JOIN jforum_users u2 ON u2.user_id = l.post_user_id \
	LEFT JOIN jforum_users u ON l.user_id = u.user_id \
	ORDER BY log_id DESC \
	LIMIT ?, ?
