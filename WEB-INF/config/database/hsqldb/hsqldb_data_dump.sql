--
-- Groups
--
INSERT INTO jforum_groups (group_id, group_name, group_description ) VALUES (1,'General', 'General Users');
INSERT INTO jforum_groups (group_id, group_name, group_description ) VALUES (2,'Administration', 'Admin Users');

-- 
-- Users
--
INSERT INTO jforum_users (user_id, username, user_password ) VALUES (1,'Anonymous', 'nopass');
INSERT INTO jforum_users (user_id, username, user_password, user_posts ) VALUES (2,'Admin', '21232f297a57a5a743894a0e4a801fc3', 1);

--
-- User Groups
--
INSERT INTO jforum_user_groups (group_id, user_id) VALUES (1, 1);
INSERT INTO jforum_user_groups (group_id, user_id) VALUES (2, 2);

--
-- Smilies
--
INSERT INTO jforum_smilies VALUES (1, ':)', '<img src=\"#CONTEXT#/images/smilies/3b63d1616c5dfcf29f8a7a031aaa7cad.gif\" />', '3b63d1616c5dfcf29f8a7a031aaa7cad.gif');
INSERT INTO jforum_smilies VALUES (2, ':-)', '<img src=\"#CONTEXT#/images/smilies/3b63d1616c5dfcf29f8a7a031aaa7cad.gif\"/>', '3b63d1616c5dfcf29f8a7a031aaa7cad.gif');
INSERT INTO jforum_smilies VALUES (3, ':D', '<img src=\"#CONTEXT#/images/smilies/283a16da79f3aa23fe1025c96295f04f.gif\" />', '283a16da79f3aa23fe1025c96295f04f.gif');
INSERT INTO jforum_smilies VALUES (4, ':-D', '<img src=\"#CONTEXT#/images/smilies/283a16da79f3aa23fe1025c96295f04f.gif\" />', '283a16da79f3aa23fe1025c96295f04f.gif');
INSERT INTO jforum_smilies VALUES (5, ':(', '<img src=\"#CONTEXT#/images/smilies/9d71f0541cff0a302a0309c5079e8dee.gif\" />', '9d71f0541cff0a302a0309c5079e8dee.gif');
INSERT INTO jforum_smilies VALUES (6, ':mrgreen:', '<img src=\"#CONTEXT#/images/smilies/ed515dbff23a0ee3241dcc0a601c9ed6.gif\" />', 'ed515dbff23a0ee3241dcc0a601c9ed6.gif');
INSERT INTO jforum_smilies VALUES (7, ':-o', '<img src=\"#CONTEXT#/images/smilies/47941865eb7bbc2a777305b46cc059a2.gif\"  />', '47941865eb7bbc2a777305b46cc059a2.gif');
INSERT INTO jforum_smilies VALUES (8, ':shock:', '<img src=\"#CONTEXT#/images/smilies/385970365b8ed7503b4294502a458efa.gif\" />', '385970365b8ed7503b4294502a458efa.gif');
INSERT INTO jforum_smilies VALUES (9, ':?:', '<img src=\"#CONTEXT#/images/smilies/0a4d7238daa496a758252d0a2b1a1384.gif\" />', '0a4d7238daa496a758252d0a2b1a1384.gif');
INSERT INTO jforum_smilies VALUES (10, '8)', '<img src=\"#CONTEXT#/images/smilies/b2eb59423fbf5fa39342041237025880.gif\"  />', 'b2eb59423fbf5fa39342041237025880.gif');
INSERT INTO jforum_smilies VALUES (11, ':lol:', '<img src=\"#CONTEXT#/images/smilies/97ada74b88049a6d50a6ed40898a03d7.gif\" />', '97ada74b88049a6d50a6ed40898a03d7.gif');
INSERT INTO jforum_smilies VALUES (12, ':x', '<img src=\"#CONTEXT#/images/smilies/1069449046bcd664c21db15b1dfedaee.gif\"  />', '1069449046bcd664c21db15b1dfedaee.gif');
INSERT INTO jforum_smilies VALUES (13, ':P', '<img src=\"#CONTEXT#/images/smilies/69934afc394145350659cd7add244ca9.gif\" />', '69934afc394145350659cd7add244ca9.gif');
INSERT INTO jforum_smilies VALUES (14, ':-P', '<img src=\"#CONTEXT#/images/smilies/69934afc394145350659cd7add244ca9.gif\" />', '69934afc394145350659cd7add244ca9.gif');
INSERT INTO jforum_smilies VALUES (15, ':oops:', '<img src=\"#CONTEXT#/images/smilies/499fd50bc713bfcdf2ab5a23c00c2d62.gif\" />', '499fd50bc713bfcdf2ab5a23c00c2d62.gif');
INSERT INTO jforum_smilies VALUES (16, ':cry:', '<img src=\"#CONTEXT#/images/smilies/c30b4198e0907b23b8246bdd52aa1c3c.gif\" />', 'c30b4198e0907b23b8246bdd52aa1c3c.gif');
INSERT INTO jforum_smilies VALUES (17, ':evil:', '<img src=\"#CONTEXT#/images/smilies/2e207fad049d4d292f60607f80f05768.gif\" />', '2e207fad049d4d292f60607f80f05768.gif');
INSERT INTO jforum_smilies VALUES (18, ':twisted:', '<img src=\"#CONTEXT#/images/smilies/908627bbe5e9f6a080977db8c365caff.gif\" />', '908627bbe5e9f6a080977db8c365caff.gif');
INSERT INTO jforum_smilies VALUES (19, ':roll:', '<img src=\"#CONTEXT#/images/smilies/2786c5c8e1a8be796fb2f726cca5a0fe.gif\" />', '2786c5c8e1a8be796fb2f726cca5a0fe.gif');
INSERT INTO jforum_smilies VALUES (20, ':wink:', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO jforum_smilies VALUES (21, ';)', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO jforum_smilies VALUES (22, ';-)', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO jforum_smilies VALUES (23, ':!:', '<img src=\"#CONTEXT#/images/smilies/9293feeb0183c67ea1ea8c52f0dbaf8c.gif\" />', '9293feeb0183c67ea1ea8c52f0dbaf8c.gif');
INSERT INTO jforum_smilies VALUES (24, ':?', '<img src=\"#CONTEXT#/images/smilies/136dd33cba83140c7ce38db096d05aed.gif\" />', '136dd33cba83140c7ce38db096d05aed.gif');
INSERT INTO jforum_smilies VALUES (25, ':idea:', '<img src=\"#CONTEXT#/images/smilies/8f7fb9dd46fb8ef86f81154a4feaada9.gif\" />', '8f7fb9dd46fb8ef86f81154a4feaada9.gif');
INSERT INTO jforum_smilies VALUES (26, ':arrow:', '<img src=\"#CONTEXT#/images/smilies/d6741711aa045b812616853b5507fd2a.gif\" />', 'd6741711aa045b812616853b5507fd2a.gif');
INSERT INTO jforum_smilies VALUES (32, ':hunf:', '<img src=\"#CONTEXT#/images/smilies/0320a00cb4bb5629ab9fc2bc1fcc4e9e.gif\" />', '0320a00cb4bb5629ab9fc2bc1fcc4e9e.gif');
INSERT INTO jforum_smilies VALUES (31, ':-(', '<img src=\"#CONTEXT#/images/smilies/9d71f0541cff0a302a0309c5079e8dee.gif\"  />', '9d71f0541cff0a302a0309c5079e8dee.gif');
INSERT INTO jforum_smilies VALUES (33, ':XD:', '<img src=\"#CONTEXT#/images/smilies/49869fe8223507d7223db3451e5321aa.gif\" />', '49869fe8223507d7223db3451e5321aa.gif');
INSERT INTO jforum_smilies VALUES (34, ':thumbup:', '<img src=\"#CONTEXT#/images/smilies/e8a506dc4ad763aca51bec4ca7dc8560.gif\" />', 'e8a506dc4ad763aca51bec4ca7dc8560.gif');
INSERT INTO jforum_smilies VALUES (35, ':thumbdown:', '<img src=\"#CONTEXT#/images/smilies/e78feac27fa924c4d0ad6cf5819f3554.gif\" />', 'e78feac27fa924c4d0ad6cf5819f3554.gif');
INSERT INTO jforum_smilies VALUES (36, ':|', '<img src=\"#CONTEXT#/images/smilies/1cfd6e2a9a2c0cf8e74b49b35e2e46c7.gif\" />', '1cfd6e2a9a2c0cf8e74b49b35e2e46c7.gif');

--
-- Demonstration Forum
--
INSERT INTO jforum_categories VALUES (1,'Category Test',1,0);
INSERT INTO jforum_forums VALUES (1,1,'Test Forum','This is a test forum',1,1,1,0);
INSERT INTO jforum_topics VALUES (1,1,'Welcome to JForum',2,CURRENT_TIMESTAMP,1,0,0,0,0,1,1,0,0);
INSERT INTO jforum_posts VALUES (1,1,1,2,CURRENT_TIMESTAMP,'127.0.0.1',1,0,1,1,null,0,1,0,0);
INSERT INTO jforum_posts_text VALUES (1,'[b]Congratulations![/b]. You have completed the installation of JForum. To start administering the board, login as [i]Admin / <the password you supplied in the installer>[/i] and access the [b]Admin Control Panel[/b] using the link that shows up in the bottom of the page. There you will be able to create Categories, Forums and much more. For more information and support, please access [url]http://www.jforum.net/community.htm[/url] and [url]http://www.jforum.net/help.htm[/url]. Thank you for choosing JForum. [url=http://www.jforum.net/doc/Team]The JForum Team[/url]','Welcome to JForum');

--
-- Roles
--
INSERT INTO jforum_roles (group_id, name) VALUES (1, 'perm_vote');
INSERT INTO jforum_roles (group_id, name) VALUES (1, 'perm_karma_enabled');
INSERT INTO jforum_roles (group_id, name) VALUES (1, 'perm_anonymous_post');
INSERT INTO jforum_roles (group_id, name) VALUES (1, 'perm_create_poll');
INSERT INTO jforum_roles (group_id, name) VALUES (1, 'perm_bookmarks_enabled');
INSERT INTO jforum_roles (group_id, name) VALUES (1, 'perm_attachments_enabled');
INSERT INTO jforum_roles (group_id, name) VALUES (1, 'perm_attachments_download');
INSERT INTO jforum_roles (group_id, name) VALUES (1, 'perm_moderation_log');

--
-- Admin
--
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_administration');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_moderation');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_moderation_post_remove');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_moderation_post_edit');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_moderation_topic_move');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_moderation_topic_lockUnlock');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_moderation_approve_messages');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_vote');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_create_poll');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_karma_enabled');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_bookmarks_enabled');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_attachments_enabled');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_attachments_download');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_moderation_log');
INSERT INTO jforum_roles (group_id, name) VALUES (2, 'perm_full_moderation_log');

--
-- View Forum
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_forum', 1);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

INSERT INTO jforum_roles (name, group_id) VALUES ('perm_forum', 2);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

--
-- Anonymous posts
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_anonymous_post', 1);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

INSERT INTO jforum_roles (name, group_id) VALUES ('perm_anonymous_post', 2);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');


--
-- View Category
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_category', 1);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

INSERT INTO jforum_roles (name, group_id) VALUES ('perm_category', 2);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');


--
-- Sticky / Announcements
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_create_sticky_announcement_topics', 1);
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_create_sticky_announcement_topics', 2);

--
-- Create / Reply to topics
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_read_only_forums', 1);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');
	
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_read_only_forums', 2);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

-- 
-- Enable HTML
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_html_disabled', 1);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

INSERT INTO jforum_roles (name, group_id) VALUES ('perm_html_disabled', 2);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');


--
-- Attachments
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_attachments_enabled', 1);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

INSERT INTO jforum_roles (name, group_id) VALUES ('perm_attachments_enabled', 2);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

--
-- Reply only
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_reply_only', 1);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

INSERT INTO jforum_roles (name, group_id) VALUES ('perm_reply_only', 2);
INSERT INTO jforum_role_values ( role_id, role_value) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');
	
--
-- Reply without moderation
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_reply_without_moderation',  1);
INSERT INTO jforum_role_values ( role_id, role_value ) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

INSERT INTO jforum_roles (name, group_id) VALUES ('perm_reply_without_moderation', 2);
INSERT INTO jforum_role_values ( role_id, role_value ) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');

--
-- Moderation of forums
--
INSERT INTO jforum_roles (name, group_id) VALUES ('perm_moderation_forums', 2);
INSERT INTO jforum_role_values ( role_id, role_value ) VALUES ((SELECT MAX(role_id) FROM jforum_roles), '1');
