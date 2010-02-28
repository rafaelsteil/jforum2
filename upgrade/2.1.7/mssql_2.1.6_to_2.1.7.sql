DROP TABLE dbo.jforum_banlist
GO
CREATE TABLE dbo.jforum_banlist
	(
	banlist_id bigint NOT NULL IDENTITY (1, 1),
	user_id bigint,
	banlist_ip varchar(15),
	banlist_email varchar(255),
	)  ON [PRIMARY]
GO
ALTER TABLE dbo.jforum_banlist ADD CONSTRAINT
	PK__jforum_banlist PRIMARY KEY CLUSTERED 
	(
	banlist_id
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO
CREATE NONCLUSTERED INDEX idx_user ON dbo.jforum_banlist
	(
	user_id
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX idx_ip ON dbo.jforum_banlist
	(
	banlist_ip
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = OFF) ON [PRIMARY]
GO

ALTER TABLE jforum_users ADD user_authhash VARCHAR(32)
GO

ALTER TABLE jforum_users ADD user_notify_always TINYINT(1) DEFAULT '0'
GO

ALTER TABLE jforum_users ADD user_notify_text TINYINT(1) DEFAULT '0'
GO

ALTER TABLE jforum_sessions ALTER COLUMN session_id VARCHAR(150)
GO

ALTER TABLE jforum_search_results ALTER COLUMN session_id VARCHAR(150)
GO

ALTER TABLE jforum_search_topics ALTER COLUMN session_id VARCHAR(150)
GO

CREATE NONCLUSTERED INDEX idx_sid ON dbo.jforum_search_topics
	(
	session_id
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = OFF) ON [PRIMARY]
GO

CREATE NONCLUSTERED INDEX idx_sid ON dbo.jforum_search_results
	(
	session_id
	) WITH( STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = OFF) ON [PRIMARY]
GO

ALTER TABLE jforum_forums_watch DROP CONSTRAINT [DF__jforum_fo__is_re__0CBAE877]
GO

ALTER TABLE jforum_forums_watch DROP COLUMN is_read
GO

DELETE FROM jforum_roles WHERE name <> 'perm_administration'
GO

DELETE FROM jforum_roles WHERE role_type = 0
GO

DELETE FROM jforum_role_values
GO

ALTER TABLE jforum_roles DROP role_type
GO

ALTER TABLE jforum_role_values DROP role_type
GO

DELETE FROM jforum_smilies WHERE code IN (':)', ':-)', ':D', ':-D', ':(', ':mrgreen:', ':-o', ':shock:', ':?:', '8)', ':lol:', ':x', ':P', ':-P', ':oops:', ':cry:', ':evil:', ':twisted:', ':roll:', ':wink:', ';)', ';-)', ':!:', ':?', ':idea:', ':arrow:', ':-(', ':hunf:', ':XD:', ':thumbup:', ':thumbdown:', ':|')
GO

INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':)', '<img src=\"#CONTEXT#/images/smilies/3b63d1616c5dfcf29f8a7a031aaa7cad.gif\" />', '3b63d1616c5dfcf29f8a7a031aaa7cad.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-)', '<img src=\"#CONTEXT#/images/smilies/3b63d1616c5dfcf29f8a7a031aaa7cad.gif\"/>', '3b63d1616c5dfcf29f8a7a031aaa7cad.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':D', '<img src=\"#CONTEXT#/images/smilies/283a16da79f3aa23fe1025c96295f04f.gif\" />', '283a16da79f3aa23fe1025c96295f04f.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-D', '<img src=\"#CONTEXT#/images/smilies/283a16da79f3aa23fe1025c96295f04f.gif\" />', '283a16da79f3aa23fe1025c96295f04f.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':(', '<img src=\"#CONTEXT#/images/smilies/9d71f0541cff0a302a0309c5079e8dee.gif\" />', '9d71f0541cff0a302a0309c5079e8dee.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':mrgreen:', '<img src=\"#CONTEXT#/images/smilies/ed515dbff23a0ee3241dcc0a601c9ed6.gif\" />', 'ed515dbff23a0ee3241dcc0a601c9ed6.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-o', '<img src=\"#CONTEXT#/images/smilies/47941865eb7bbc2a777305b46cc059a2.gif\"  />', '47941865eb7bbc2a777305b46cc059a2.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':shock:', '<img src=\"#CONTEXT#/images/smilies/385970365b8ed7503b4294502a458efa.gif\" />', '385970365b8ed7503b4294502a458efa.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':?:', '<img src=\"#CONTEXT#/images/smilies/0a4d7238daa496a758252d0a2b1a1384.gif\" />', '0a4d7238daa496a758252d0a2b1a1384.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES ('8)', '<img src=\"#CONTEXT#/images/smilies/b2eb59423fbf5fa39342041237025880.gif\"  />', 'b2eb59423fbf5fa39342041237025880.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':lol:', '<img src=\"#CONTEXT#/images/smilies/97ada74b88049a6d50a6ed40898a03d7.gif\" />', '97ada74b88049a6d50a6ed40898a03d7.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':x', '<img src=\"#CONTEXT#/images/smilies/1069449046bcd664c21db15b1dfedaee.gif\"  />', '1069449046bcd664c21db15b1dfedaee.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':P', '<img src=\"#CONTEXT#/images/smilies/69934afc394145350659cd7add244ca9.gif\" />', '69934afc394145350659cd7add244ca9.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-P', '<img src=\"#CONTEXT#/images/smilies/69934afc394145350659cd7add244ca9.gif\" />', '69934afc394145350659cd7add244ca9.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':oops:', '<img src=\"#CONTEXT#/images/smilies/499fd50bc713bfcdf2ab5a23c00c2d62.gif\" />', '499fd50bc713bfcdf2ab5a23c00c2d62.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':cry:', '<img src=\"#CONTEXT#/images/smilies/c30b4198e0907b23b8246bdd52aa1c3c.gif\" />', 'c30b4198e0907b23b8246bdd52aa1c3c.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':evil:', '<img src=\"#CONTEXT#/images/smilies/2e207fad049d4d292f60607f80f05768.gif\" />', '2e207fad049d4d292f60607f80f05768.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':twisted:', '<img src=\"#CONTEXT#/images/smilies/908627bbe5e9f6a080977db8c365caff.gif\" />', '908627bbe5e9f6a080977db8c365caff.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':roll:', '<img src=\"#CONTEXT#/images/smilies/2786c5c8e1a8be796fb2f726cca5a0fe.gif\" />', '2786c5c8e1a8be796fb2f726cca5a0fe.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':wink:', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (';)', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (';-)', '<img src=\"#CONTEXT#/images/smilies/8a80c6485cd926be453217d59a84a888.gif\" />', '8a80c6485cd926be453217d59a84a888.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':!:', '<img src=\"#CONTEXT#/images/smilies/9293feeb0183c67ea1ea8c52f0dbaf8c.gif\" />', '9293feeb0183c67ea1ea8c52f0dbaf8c.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':?', '<img src=\"#CONTEXT#/images/smilies/136dd33cba83140c7ce38db096d05aed.gif\" />', '136dd33cba83140c7ce38db096d05aed.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':idea:', '<img src=\"#CONTEXT#/images/smilies/8f7fb9dd46fb8ef86f81154a4feaada9.gif\" />', '8f7fb9dd46fb8ef86f81154a4feaada9.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':arrow:', '<img src=\"#CONTEXT#/images/smilies/d6741711aa045b812616853b5507fd2a.gif\" />', 'd6741711aa045b812616853b5507fd2a.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':hunf:', '<img src=\"#CONTEXT#/images/smilies/0320a00cb4bb5629ab9fc2bc1fcc4e9e.gif\" />', '0320a00cb4bb5629ab9fc2bc1fcc4e9e.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':-(', '<img src=\"#CONTEXT#/images/smilies/9d71f0541cff0a302a0309c5079e8dee.gif\"  />', '9d71f0541cff0a302a0309c5079e8dee.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':XD:', '<img src=\"#CONTEXT#/images/smilies/49869fe8223507d7223db3451e5321aa.gif\" />', '49869fe8223507d7223db3451e5321aa.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':thumbup:', '<img src=\"#CONTEXT#/images/smilies/e8a506dc4ad763aca51bec4ca7dc8560.gif\" />', 'e8a506dc4ad763aca51bec4ca7dc8560.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':thumbdown:', '<img src=\"#CONTEXT#/images/smilies/e78feac27fa924c4d0ad6cf5819f3554.gif\" />', 'e78feac27fa924c4d0ad6cf5819f3554.gif')
GO
INSERT INTO jforum_smilies (code, url, disk_name) VALUES (':|', '<img src=\"#CONTEXT#/images/smilies/1cfd6e2a9a2c0cf8e74b49b35e2e46c7.gif\" />', '1cfd6e2a9a2c0cf8e74b49b35e2e46c7.gif')
GO
