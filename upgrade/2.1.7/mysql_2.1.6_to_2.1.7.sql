DROP TABLE IF EXISTS jforum_banlist;
CREATE TABLE jforum_banlist (
  banlist_id INT NOT NULL auto_increment,
  user_id INT,
  banlist_ip varchar(15),
  banlist_email varchar(255),
  PRIMARY KEY  (banlist_id),
  INDEX idx_user (user_id),
  INDEX (banlist_ip)
) TYPE=InnoDB;

ALTER TABLE jforum_users ADD COLUMN user_authhash VARCHAR(32);
ALTER TABLE jforum_users ADD COLUMN user_notify_always TINYINT(1) DEFAULT '0';
ALTER TABLE jforum_users ADD COLUMN user_notify_text TINYINT(1) DEFAULT '0';
ALTER TABLE jforum_users CHANGE user_karma user_karma DOUBLE;

ALTER TABLE jforum_sessions CHANGE session_id session_id VARCHAR(150);
ALTER TABLE jforum_search_results CHANGE session_id session_id VARCHAR(150);
ALTER TABLE jforum_search_topics CHANGE session_id session_id VARCHAR(150);

ALTER TABLE jforum_search_topics ADD KEY (session_id);
ALTER TABLE jforum_search_results ADD KEY (session_id);

ALTER TABLE jforum_forums_watch DROP COLUMN is_read;

DELETE FROM jforum_roles WHERE name <> 'perm_administration';
DELETE FROM jforum_roles WHERE role_type = 0;
DELETE FROM jforum_role_values;

ALTER TABLE jforum_roles DROP COLUMN role_type;
ALTER TABLE jforum_role_values DROP COLUMN role_type;

DELETE FROM jforum_smilies WHERE code IN (':)', ':-)', ':D', ':-D', ':(', ':mrgreen:', ':-o', ':shock:', ':?:', '8)', ':lol:', ':x', ':P', ':-P', ':oops:', ':cry:', ':evil:', ':twisted:', ':roll:', ':wink:', ';)', ';-)', ':!:', ':?', ':idea:', ':arrow:', ':-(', ':hunf:', ':XD:', ':thumbup:', ':thumbdown:', ':|');

INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':)', '<img src=\"#CONTEXT#/images/smilies/3b63d1616c5dfcf29f8a7a031aaa7cad.gif\" />', '3b63d1616c5dfcf29f8a7a031aaa7cad.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-)', '<img src=\"#CONTEXT#/images/smilies/3b63d1616c5dfcf29f8a7a031aaa7cad.gif\"/>', '3b63d1616c5dfcf29f8a7a031aaa7cad.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':D', '<img src=\"#CONTEXT#/images/smilies/283a16da79f3aa23fe1025c96295f04f.gif\" />', '283a16da79f3aa23fe1025c96295f04f.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-D', '<img src=\"#CONTEXT#/images/smilies/283a16da79f3aa23fe1025c96295f04f.gif\" />', '283a16da79f3aa23fe1025c96295f04f.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':(', '<img src=\"#CONTEXT#/images/smilies/9d71f0541cff0a302a0309c5079e8dee.gif\" />', '9d71f0541cff0a302a0309c5079e8dee.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':mrgreen:', '<img src=\"#CONTEXT#/images/smilies/ed515dbff23a0ee3241dcc0a601c9ed6.gif\" />', 'ed515dbff23a0ee3241dcc0a601c9ed6.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-o', '<img src=\"#CONTEXT#/images/smilies/47941865eb7bbc2a777305b46cc059a2.gif\"  />', '47941865eb7bbc2a777305b46cc059a2.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':shock:', '<img src=\"#CONTEXT#/images/smilies/385970365b8ed7503b4294502a458efa.gif\" />', '385970365b8ed7503b4294502a458efa.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':?:', '<img src=\"#CONTEXT#/images/smilies/0a4d7238daa496a758252d0a2b1a1384.gif\" />', '0a4d7238daa496a758252d0a2b1a1384.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES ('8)', '<img src=\"#CONTEXT#/images/smilies/b2eb59423fbf5fa39342041237025880.gif\"  />', 'b2eb59423fbf5fa39342041237025880.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':lol:', '<img src=\"#CONTEXT#/images/smilies/97ada74b88049a6d50a6ed40898a03d7.gif\" />', '97ada74b88049a6d50a6ed40898a03d7.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':x', '<img src=\"#CONTEXT#/images/smilies/1069449046bcd664c21db15b1dfedaee.gif\"  />', '1069449046bcd664c21db15b1dfedaee.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':P', '<img src=\"#CONTEXT#/images/smilies/69934afc394145350659cd7add244ca9.gif\" />', '69934afc394145350659cd7add244ca9.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-P', '<img src=\"#CONTEXT#/images/smilies/69934afc394145350659cd7add244ca9.gif\" />', '69934afc394145350659cd7add244ca9.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':oops:', '<img src=\"#CONTEXT#/images/smilies/499fd50bc713bfcdf2ab5a23c00c2d62.gif\" />', '499fd50bc713bfcdf2ab5a23c00c2d62.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':cry:', '<img src=\"#CONTEXT#/images/smilies/c30b4198e0907b23b8246bdd52aa1c3c.gif\" />', 'c30b4198e0907b23b8246bdd52aa1c3c.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':evil:', '<img src=\"#CONTEXT#/images/smilies/2e207fad049d4d292f60607f80f05768.gif\" />', '2e207fad049d4d292f60607f80f05768.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':twisted:', '<img src=\"#CONTEXT#/images/smilies/908627bbe5e9f6a080977db8c365caff.gif\" />', '908627bbe5e9f6a080977db8c365caff.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':roll:', '<img src=\"#CONTEXT#/images/smilies/2786c5c8e1a8be796fb2f726cca5a0fe.gif\" />', '2786c5c8e1a8be796fb2f726cca5a0fe.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':wink:', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (';)', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (';-)', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':!:', '<img src=\"#CONTEXT#/images/smilies/9293feeb0183c67ea1ea8c52f0dbaf8c.gif\" />', '9293feeb0183c67ea1ea8c52f0dbaf8c.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':?', '<img src=\"#CONTEXT#/images/smilies/136dd33cba83140c7ce38db096d05aed.gif\" />', '136dd33cba83140c7ce38db096d05aed.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':idea:', '<img src=\"#CONTEXT#/images/smilies/8f7fb9dd46fb8ef86f81154a4feaada9.gif\" />', '8f7fb9dd46fb8ef86f81154a4feaada9.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':arrow:', '<img src=\"#CONTEXT#/images/smilies/d6741711aa045b812616853b5507fd2a.gif\" />', 'd6741711aa045b812616853b5507fd2a.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':hunf:', '<img src=\"#CONTEXT#/images/smilies/0320a00cb4bb5629ab9fc2bc1fcc4e9e.gif\" />', '0320a00cb4bb5629ab9fc2bc1fcc4e9e.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-(', '<img src=\"#CONTEXT#/images/smilies/9d71f0541cff0a302a0309c5079e8dee.gif\"  />', '9d71f0541cff0a302a0309c5079e8dee.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':XD:', '<img src=\"#CONTEXT#/images/smilies/49869fe8223507d7223db3451e5321aa.gif\" />', '49869fe8223507d7223db3451e5321aa.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':thumbup:', '<img src=\"#CONTEXT#/images/smilies/e8a506dc4ad763aca51bec4ca7dc8560.gif\" />', 'e8a506dc4ad763aca51bec4ca7dc8560.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':thumbdown:', '<img src=\"#CONTEXT#/images/smilies/e78feac27fa924c4d0ad6cf5819f3554.gif\" />', 'e78feac27fa924c4d0ad6cf5819f3554.gif');
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':|', '<img src=\"#CONTEXT#/images/smilies/1cfd6e2a9a2c0cf8e74b49b35e2e46c7.gif\" />', '1cfd6e2a9a2c0cf8e74b49b35e2e46c7.gif');
