/*
 * Copyright (c) JForum Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:
 * 
 * 1) Redistributions of source code must retain the above 
 * copyright notice, this list of conditions and the 
 * following  disclaimer.
 * 2)  Redistributions in binary form must reproduce the 
 * above copyright notice, this list of conditions and 
 * the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 * 3) Neither the name of "Rafael Steil" nor 
 * the names of its contributors may be used to endorse 
 * or promote products derived from this software without 
 * specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT 
 * HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL 
 * THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER 
 * IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN 
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 * 
 * Created on May 29, 2004 by pieter
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.preferences;

/**
* Encapsulate all configuration keys in constants. This is more typesafe and provides
* a nice overview of all configuration keys. Last but not least this lets us autocomplete
* configuration keys under eclipse ;-)
* 
* @author Pieter Olivier
* @version $Id: ConfigKeys.java,v 1.120 2007/09/21 15:54:30 rafaelsteil Exp $
*/

public class ConfigKeys 
{
	public static final String LOGGED = "logged";
	public static final String APPLICATION_PATH = "application.path";
	public static final String INSTALLATION = "installation";
	public static final String INSTALLED = "installed";

	public static final String INSTALLATION_CONFIG = "installation.config";
	public static final String CACHEABLE_OBJECTS = "cacheable.objects";
	
	public static final String FILECHANGES_DELAY = "file.changes.delay";
	public static final String DATABASE_PING_DELAY = "database.ping.delay";
	public static final String DATABASE_CONNECTION_IMPLEMENTATION = "database.connection.implementation";
	public static final String DATABASE_DRIVER_NAME = "database.driver.name";
	public static final String DATABASE_DRIVER_CONFIG = "database.driver.config";
	public static final String DATABASE_CONNECTION_HOST = "database.connection.host";
	public static final String DATABASE_CONNECTION_USERNAME = "database.connection.username";
	public static final String DATABASE_CONNECTION_PASSWORD = "database.connection.password";
	public static final String DATABASE_CONNECTION_DBNAME = "database.connection.dbname";
	public static final String DATABASE_CONNECTION_ENCODING = "dbencoding";
	public static final String DATABASE_CONNECTION_DRIVER = "database.connection.driver";
	public static final String DATABASE_CONNECTION_STRING = "database.connection.string";
	public static final String DATABASE_CONNECTION_PORT = "database.connection.port";
	public static final String DATABASE_POOL_MIN = "database.connection.pool.min";
	public static final String DATABASE_POOL_MAX = "database.connection.pool.max";
	public static final String DATABASE_USE_TRANSACTIONS = "database.use.transactions";
	public static final String DATABASE_DATASOURCE_NAME = "database.datasource.name";
	public static final String DATABASE_ERROR_PAGE = "database.error.page";
	public static final String DATABASE_MYSQL_UNICODE = "mysql.unicode";
	public static final String DATABASE_MYSQL_ENCODING = "mysql.encoding";
	public static final String DATABASE_AUTO_KEYS = "database.support.autokeys";
	public static final String DATABASE_SUPPORT_SUBQUERIES = "database.support.subqueries";
	public static final String C3P0_EXTRA_PARAMS = "c3p0.extra.params";
	
	public static final String AUTHENTICATION_TYPE = "authentication.type";
	public static final String SSO_IMPLEMENTATION = "sso.implementation";
	public static final String LOGIN_AUTHENTICATOR = "login.authenticator";
	public static final String LOGIN_AUTHENTICATOR_INSTANCE = "login.authenticator.instance";
	public static final String TYPE_DEFAULT = "default";
	public static final String TYPE_SSO = "sso";
	public static final String AUTO_LOGIN_ENABLED = "auto.login.enabled";
	
	public static final String SSO_PASSWORD_ATTRIBUTE = "sso.password.attribute";
	public static final String SSO_EMAIL_ATTRIBUTE = "sso.email.attribute";
	public static final String SSO_DEFAULT_PASSWORD = "sso.default.password";
	public static final String SSO_DEFAULT_EMAIL = "sso.default.email";
	public static final String SSO_REDIRECT = "sso.redirect";
	
	public static final String RESOURCE_DIR = "resource.dir";
	public static final String CONFIG_DIR = "config.dir";
	public static final String DATABASE_PROPERTIES = "database.properties";
	public static final String DATABASE_DRIVER_PROPERTIES = "database.driver.properties";
	public static final String SQL_QUERIES_GENERIC = "sql.queries.generic";
	public static final String SQL_QUERIES_DRIVER = "sql.queries.driver";

	public static final String TEMPLATES_MAPPING = "templates.mapping";
	public static final String TEMPLATE_DIR = "template.dir";
	public static final String ENCODING = "encoding";
	public static final String DEFAULT_CONTAINER_ENCODING = "default.container.encoding";
	public static final String SERVLET_NAME = "servlet.name";
	public static final String DEFAULT_CONFIG = "default.config";
	public static final String CONTEXT_NAME = "context.name";
	public static final String SERVLET_EXTENSION = "servlet.extension";
	public static final String COOKIE_NAME_DATA = "cookie.name.data";
	public static final String COOKIE_NAME_USER = "cookie.name.user";
	public static final String COOKIE_AUTO_LOGIN = "cookie.name.autologin";
	public static final String COOKIE_USER_HASH = "cookie.name.userHash";
		
	public static final String ANONYMOUS_USER_ID = "anonymous.userId";
	public static final String DEFAULT_USER_GROUP = "defaultUserGroup";
	public static final String USER_HASH_SEQUENCE = "user.hash.sequence";
	public static final String TOPICS_READ_TIME = "topics.tracking";
	public static final String TOPICS_READ_TIME_BY_FORUM = "topics.tracking.byforum";
	
	public static final String TOPIC_CACHE_ENABLED = "topic.cache.enabled";
	public static final String SECURITY_CACHE_ENABLED = "security.cache.enabled";

	public static final String VERSION = "version";
	public static final String BACKGROUND_TASKS = "background.tasks";

	public static final String FORUM_LINK = "forum.link";
	public static final String HOMEPAGE_LINK = "homepage.link";
	public static final String FORUM_NAME = "forum.name";
	public static final String FORUM_PAGE_TITLE = "forum.page.title";
	public static final String FORUM_PAGE_METATAG_KEYWORDS = "forum.page.metatag.keywords";
	public static final String FORUM_PAGE_METATAG_DESCRIPTION = "forum.page.metatag.description";

	public static final String TMP_DIR = "tmp.dir";
	public static final String CACHE_DIR = "cache.dir";

	public static final String DAO_DRIVER = "dao.driver";

	public static final String DATE_TIME_FORMAT = "dateTime.format";
	public static final String RSS_DATE_TIME_FORMAT = "rss.datetime.format";
	public static final String RSS_ENABLED = "rss.enabled";
	public static final String HOT_TOPIC_BEGIN = "hot.topic.begin";

	public static final String TOPICS_PER_PAGE = "topicsPerPage";
	public static final String POSTS_PER_PAGE = "postsPerPage";
	public static final String USERS_PER_PAGE = "usersPerPage";
	public static final String RECENT_TOPICS = "topic.recent";
	public static final String HOTTEST_TOPICS = "topic.hottest";
	public static final String POSTS_CACHE_SIZE = "posts.cache.size";
	public static final String POSTS_CACHE_ENABLED = "posts.cache.enabled";

	public static final String CAPTCHA_IGNORE_CASE = "captcha.ignore.case";
	public static final String CAPTCHA_REGISTRATION = "captcha.registration";
	public static final String CAPTCHA_POSTS = "captcha.posts";
	public static final String CAPTCHA_WIDTH = "captcha.width";
	public static final String CAPTCHA_HEIGHT = "captcha.height";
	public static final String CAPTCHA_MIN_FONT_SIZE = "captcha.min.font.size";
	public static final String CAPTCHA_MAX_FONT_SIZE = "captcha.max.font.size";
	public static final String CAPTCHA_MIN_WORDS = "captcha.min.words";
	public static final String CAPTCHA_MAX_WORDS = "captcha.max.words";
	
	public static final String I18N_DEFAULT = "i18n.board.default";
	public static final String I18N_DEFAULT_ADMIN = "i18n.internal";
	public static final String I18N_IMAGES_DIR = "i18n.images.dir";
	public static final String LOCALES_DIR = "locales.dir";
	public static final String LOCALES_NAMES = "locales.names";

	public static final String MAIL_LOST_PASSWORD_MESSAGE_FILE = "mail.lostPassword.messageFile";
	public static final String MAIL_LOST_PASSWORD_SUBJECT = "mail.lostPassword.subject";
	public static final String MAIL_NOTIFY_ANSWERS = "mail.notify.answers";
	public static final String MAIL_SENDER = "mail.sender";
	public static final String MAIL_CHARSET = "mail.charset";
	public static final String MAIL_TEMPLATE_ENCODING = "mail.template.encoding";
	public static final String MAIL_NEW_ANSWER_MESSAGE_FILE = "mail.newAnswer.messageFile";
	public static final String MAIL_NEW_ANSWER_SUBJECT = "mail.newAnswer.subject";
	public static final String MAIL_NEW_PM_SUBJECT = "mail.newPm.subject";
	public static final String MAIL_NEW_PM_MESSAGE_FILE = "mail.newPm.messageFile";
	public static final String MAIL_MESSSAGE_FORMAT = "mail.messageFormat";
	
	public static final String MAIL_POP3_DEBUG_KEEP_MESSAGES = "mail.pop3.debug.keep.messages";
	public static final String MAIL_POP3_INTEGRATION_ENABLED = "mail.pop3.integration.enabled";
	
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	
	public static final String MAIL_SMTP_SSL_AUTH = "mail.smtps.auth";
	public static final String MAIL_SMTP_SSL_HOST = "mail.smtps.host";
	public static final String MAIL_SMTP_SSL_PORT = "mail.smtps.port";
	public static final String MAIL_SMTP_SSL_LOCALHOST = "mail.smtps.localhost";
	
	public static final String MAIL_SMTP_SSL = "mail.smtp.ssl";
	
	public static final String MAIL_SMTP_LOCALHOST = "mail.smtp.localhost";
	public static final String MAIL_SMTP_USERNAME = "mail.smtp.username";
	public static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";
	public static final String MAIL_SMTP_DELAY = "mail.smtp.delay";
	
	public static final String MAIL_USER_EMAIL_AUTH = "mail.user.email.auth";
	public static final String MAIL_ACTIVATION_KEY_MESSAGE_FILE = "mail.activationKey.messageFile";
	public static final String MAIL_ACTIVATION_KEY_SUBJECT = "mail.activationKey.subject";

	public static final String MAIL_NEW_TOPIC_MESSAGE_FILE = "mail.newTopic.messageFile";
	public static final String MAIL_NEW_TOPIC_SUBJECT = "mail.newTopic.subject";

	public static final String HTML_TAGS_WELCOME = "html.tags.welcome";
	public static final String HTML_ATTRIBUTES_WELCOME = "html.attributes.welcome";
	public static final String HTML_LINKS_ALLOW_RELATIVE = "html.links.allow.relative";
	public static final String HTML_LINKS_ALLOW_PROTOCOLS = "html.links.allow.protocols";

	public static final String SMILIE_IMAGE_DIR = "smilie.image.dir";
	public static final String SMILIE_IMAGE_PATTERN = "smilie.image.pattern";

	public static final String AVATAR_MAX_WIDTH = "avatar.maxWidth";
	public static final String AVATAR_MAX_HEIGHT = "avatar.maxHeight";
	public static final String AVATAR_ALLOW_EXTERNAL_URL = "avatar.allow.external.url";

	public static final String MOST_USERS_EVER_ONLINE = "most.users.ever.online";
	public static final String MOST_USER_EVER_ONLINE_DATE = "most.users.ever.online.date";
	
	public static final String JBOSS_CACHE_PROPERTIES = "jboss.cache.properties";
	public static final String CACHE_IMPLEMENTATION = "cache.engine.implementation";
	
	public static final String ATTACHMENTS_MAX_POST = "attachments.max.post";
	public static final String ATTACHMENTS_IMAGES_CREATE_THUMB = "attachments.images.createthumb";
	public static final String ATTACHMENTS_IMAGES_MAX_THUMB_W = "attachments.images.thumb.maxsize.w";
	public static final String ATTACHMENTS_IMAGES_MAX_THUMB_H = "attachments.images.thumb.maxsize.h";
	public static final String ATTACHMENTS_IMAGES_THUMB_BOX_SHOW = "attachments.images.thumb.box.show";
	public static final String ATTACHMENTS_ICON = "attachments.icon";
	public static final String ATTACHMENTS_STORE_DIR = "attachments.store.dir";
	public static final String ATTACHMENTS_UPLOAD_DIR = "attachments.upload.dir";
	public static final String ATTACHMENTS_ANONYMOUS = "attachments.anonymous";
	
	public static final String AGREEMENT_SHOW = "agreement.show";
	public static final String AGREEMENT_ACCEPTED = "agreement.accepted";
	public static final String AGREEMENT_DEFAULT_FILE = "agreement.default.file";
	public static final String AGREEMENT_FILES_PATH = "agreement.files.path";
	public static final String REGISTRATION_ENABLED = "registration.enabled";
	public static final String USERNAME_MAX_LENGTH = "username.max.length";

	public static final String QUARTZ_CONFIG = "quartz.config";
	
	public static final String QUARTZ_CONTEXT = "org.quartz.context.";
	public static final String SEARCH_INDEXING_ENABLED = "search.indexing.enabled";
	public static final String SEARCH_INDEXER_IMPLEMENTATION = "search.indexer.implementation";
	public static final String SEARCH_INDEXER_CRON_EXPRESSON = "indexer.cron.expression";
	public static final String EXTENSION_FIELD = "extension.field";
	
	public static final String LDAP_SECURITY_PROTOCOL = "ldap.security.protocol";
	public static final String LDAP_AUTHENTICATION = "ldap.authentication";
	public static final String LDAP_FACTORY = "ldap.factory";
	public static final String LDAP_LOGIN_PREFIX = "ldap.login.prefix";
	public static final String LDAP_LOGIN_SUFFIX = "ldap.login.suffix";
	public static final String LDAP_SERVER_URL = "ldap.server.url";
	public static final String LDAP_FIELD_EMAIL = "ldap.field.email";
	public static final String LDAP_LOOKUP_PREFIX = "ldap.lookup.prefix";
	public static final String LDAP_LOOKUP_SUFFIX = "ldap.lookup.suffix";
	
	public static final String CLICKSTREAM_CONFIG = "clickstream.config";
	public static final String IS_BOT = "clickstream.is.bot";

	public static final String POSTS_NEW_DELAY = "posts.new.delay";
	public static final String LAST_POST_TIME = "last.post.time";

	public static final String KARMA_MIN_POINTS = "karma.min.points";
	public static final String KARMA_MAX_POINTS = "karma.max.points";
    
    public static final String MAIL_SUMMARY_SUBJECT = "mail.summary.weekly.subject";
    public static final String MAIL_SUMMARY_FILE = "mail.summary.weekly.messageFile";
    public static final String SUMMARY_DAYS_BEFORE = "summary.days.before";
    public static final String SUMMARY_IS_ENABLED = "summary.enabled";
    
    public static final String STACKTRACE_MODERATORS_ONLY = "stacktrace.moderators.only";
    public static final String JFORUM_VERSION_URL = "jforum.version.url";
	public static final String REQUEST_IGNORE_CAPTCHA = "request.ignore.captcha";
	
	public static final String API_SECURITY_KEY = "api.security.key";
	public static final String BANLIST_SEND_403FORBIDDEN = "banlist.send.403forbidden";
	
	public static final String LOGIN_IGNORE_XFORWARDEDHOST = "login.ignore.xforwardedhost";
	public static final String LOGIN_IGNORE_REFERER = "login.ignore.referer";
	
	public static final String LUCENE_ANALYZER = "lucene.analyzer";
	public static final String LUCENE_INDEX_WRITE_PATH = "lucene.index.write.path";
	public static final String LUCENE_SETTINGS = "lucene.settings";
	public static final String LUCENE_CURRENTLY_INDEXING = "lucene.currently.indexing";
	public static final String LUCENE_INDEXER_RAM_NUMDOCS = "lucene.indexer.ram.numdocs";
	public static final String LUCENE_INDEXER_DB_FETCH_COUNT = "lucene.indexer.db.fetch.count";
	
	public static final String MODERATION_LOGGING_ENABLED = "moderation.logging.enabled";
	public static final String PROXIED_CONTEXT_PATH = "proxied.context.path";
	public static final String REDIRECT_ABSOLUTE_PATHS = "redirect.absolute.paths";
	public static final String REDIRECT_BASE_URL = "redirect.base.url";
    
	public static final String FREEMARKER_EXTRA_TEMPLATE_PATH = "freemarker.extra.template.path";

	private ConfigKeys() {}
}
