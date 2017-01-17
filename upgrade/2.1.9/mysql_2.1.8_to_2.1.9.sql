USE jforum;
ALTER TABLE jforum_posts MODIFY poster_ip VARCHAR(150);
ALTER TABLE jforum_banlist MODIFY banlist_ip VARCHAR(150);
