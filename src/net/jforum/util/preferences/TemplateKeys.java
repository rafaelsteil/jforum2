/*
 * Copyright (c) JForum Team
 * 
 * All rights reserved.
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
 * Created on Mar 14, 2005 3:04:11 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.preferences;

/**
 * @author Rafael Steil
 * @version $Id: TemplateKeys.java,v 1.29 2007/09/21 17:26:11 rafaelsteil Exp $
 */
public class TemplateKeys
{
	private TemplateKeys() {}
	
	public static final String FORUMS_LIST = "forums.list";
	public static final String FORUMS_SHOW = "forums.show";
	public static final String FORUMS_PING = "forums.ping";
	public static final String FORUMS_BANNED = "forums.banned";

	public static final String BOOKMARKS_ADD_FORUM = "bookmarks.add.forum";
	public static final String BOOKMARS_ADD_TOPIC = "bookmars.add.topic";
	public static final String BOOKMARKS_ADD_USER = "bookmarks.add.user";
	public static final String BOOKMARKS_INSERT_SAVE = "bookmarks.insert.save";
	public static final String BOOKMARKS_UPDATE_SAVE = "bookmarks.update.save";
	public static final String BOOKMARKS_EDIT = "bookmarks.edit";
	public static final String BOOKMARKS_ERROR = "bookmarks.error";
	public static final String BOOKMARKS_LIST = "bookmarks.list";

	public static final String KARMA_ERROR = "karma.error";
	public static final String KARMA_LIST = "karma.list";
	public static final String KARMA_SEARCH_BYPERIOD = "karma.search.byperiod";
	public static final String KARMA_SEARCH_BYMONTH = "karma.search.bymonth";

	public static final String MODERATION_MOVE_TOPICS = "moderation.move.topics";
	public static final String MODERATION_DONE = "moderation.done";
	public static final String MODERATION_DENIED = "moderation.denied";

	public static final String POSTS_LIST = "posts.list";
	public static final String POSTS_USER_POSTS_LIST = "posts.userposts.list";
	public static final String POSTS_REVIEW = "posts.review";
	public static final String POSTS_TOPIC_NOT_FOUND = "posts.topic.not.found";
	public static final String POSTS_POST_NOT_FOUND = "posts.post.not.found";
	public static final String POSTS_REPLY_ONLY = "posts.reply.only";
	public static final String POSTS_INSERT = "posts.insert";
	public static final String POSTS_EDIT = "posts.edit";
	public static final String POSTS_EDIT_CANNOTEDIT = "posts.edit.cannotedit";
	public static final String POSTS_QUOTE = "posts.quote";
	public static final String POSTS_WAITING = "posts.waiting";
	public static final String POSTS_NOT_MODERATED = "posts.not.moderated";
	public static final String POSTS_CANNOT_DELETE = "posts.cannot.delete";
	public static final String POSTS_UNWATCH = "posts.unwatch";
	public static final String POSTS_CANNOT_DOWNLOAD = "posts.cannot.download";
	public static final String POSTS_ATTACH_NOTFOUND = "posts.attach.notfound";
	public static final String POSTS_TOPIC_LOCKED = "posts.topic.locked";
	public static final String POSTS_LIST_SMILIES = "posts.list.smilies";

	public static final String PM_INBOX = "pm.inbox";
	public static final String PM_SENTBOX = "pm.sentbox";
	public static final String PM_SENDFORM = "pm.sendform";
	public static final String PM_SENDSAVE_USER_NOTFOUND = "pm.sendsave.user.notfound";
	public static final String PM_SENDSAVE = "pm.sendsave";
	public static final String PM_READ = "pm.read";
	public static final String PM_READ_REVIEW = "pm.read.review";
	public static final String PM_READ_DENIED = "pm.read.denied";
	public static final String PM_DELETE = "pm.delete";
	public static final String PM_FIND_USER = "pm.find.user";

	public static final String RECENT_LIST = "recent.list";
	public static final String RECENT_USER_TOPICS_SHOW = "recent.usertopics.show";
	
	public static final String HOTTEST_LIST = "hottest.list";
	public static final String HOTTEST_USER_TOPICS_SHOW = "hottest.usertopics.show";

	public static final String SEARCH_FILTERS = "search.filters";
	public static final String SEARCH_SEARCH = "search.search";
	public static final String SEARCH_NEW_MESSAGES = "search.new.messages";

	public static final String USER_EDIT = "user.edit";
	public static final String USER_REGISTRATION_DISABLED = "user.registration.disabled";
	public static final String USER_INSERT = "user.insert";
	public static final String USER_INSERT_ACTIVATE_MAIL = "user.insert.activate.mail";
	public static final String USER_INVALID_ACTIVATION = "user.invalid.activation";
	public static final String USER_REGISTRATION_COMPLETE = "user.registration.complete";
	public static final String USER_VALIDATE_LOGIN = "user.validate.login";
	public static final String USER_PROFILE = "user.profile";
	public static final String USER_LOGIN = "user.login";
	public static final String USER_LOSTPASSWORD = "user.lostpassword";
	public static final String USER_LOSTPASSWORD_SEND = "user.lostpassword.send";
	public static final String USER_RECOVERPASSWORD = "user.recoverpassword";
	public static final String USER_RECOVERPASSWORD_VALIDATE = "user.recoverpassword.validate";
	public static final String USER_LIST = "user.list";
	public static final String USER_SEARCH_KARMA = "user.search.karma";
	public static final String USER_NOT_FOUND = "user.not.found";

	public static final String VIEWCOMMON_LOGIN = "viewcommon.login";

	public static final String ADMIN_INDEX = "admin.index";
	public static final String ADMIN_MENU = "admin.menu";
	public static final String ADMIN_MAIN = "admin.main";

	public static final String ATTACHMENTS_CONFIG = "attachments.config";
	public static final String ATTACHMENTS_QUOTA_LIMIT = "attachments.quota.limit";
	public static final String ATTACHMENTS_EXTENSION_GROUPS = "attachments.extension.groups";
	public static final String ATTACHMENTS_EXTENSIONS = "attachments.extensions";

	public static final String CACHE_LIST = "cache.list";
	public static final String CACHE_POST_MOREINFO = "cache.post.moreinfo";
	public static final String CACHE_TOPICS_MOREINFO = "cache.topics.moreinfo";

	public static final String CATEGORY_LIST = "category.list";
	public static final String CATEGORY_INSERT = "category.insert";
	public static final String CATEGORY_EDIT = "category.edit";

	public static final String CONFIG_LIST = "config.list";

	public static final String FORUM_ADMIN_LIST = "forum.admin.list";
	public static final String FORUM_ADMIN_INSERT = "forum.admin.insert";
	public static final String FORUM_ADMIN_EDIT = "forum.admin.edit";

	public static final String GROUP_LIST = "group.list";
	public static final String GROUP_INSERT = "group.insert";
	public static final String GROUP_EDIT = "group.edit";
	public static final String GROUP_PERMISSIONS = "group.permissions";

	public static final String MODERATION_ADMIN_LIST = "moderation.admin.list";
	public static final String MODERATION_ADMIN_VIEW = "moderation.admin.view";

	public static final String RANKING_LIST = "ranking.list";
	public static final String RANKING_INSERT = "ranking.insert";
	public static final String RANKING_EDIT = "ranking.edit";

	public static final String SMILIES_INSERT = "smilies.insert";
	public static final String SMILIES_EDIT = "smilies.edit";
	public static final String SMILIES_LIST = "smilies.list";

	public static final String USER_ADMIN_COMMON = "user.admin.common";
	public static final String USER_ADMIN_PERMISSIONS = "user.admin.permissions";
	public static final String USER_ADMIN_EDIT = "user.admin.edit";
	public static final String USER_ADMIN_GROUPS = "user.admin.groups";
	public static final String USER_ADMIN_PENDING_ACTIVATIONS = "user.admin.pending.activations";
	
	public static final String BANNER_LIST = "banner.list";
	public static final String BANNER_INSERT = "banner.insert";
	public static final String BANNER_EDIT = "banner.edit";

	public static final String RSS = "rss";
	public static final String EMPTY = "empty";
	
	public static final String INSTALL_WELCOME = "install.welcome";
	public static final String INSTALL_CHECK_INFO = "install.check.info";
	public static final String INSTALL_ERROR = "install.error";
	public static final String INSTALL_FINISHED = "install.finished";
	
	public static final String API_USER_LIST = "api.user.list";
	public static final String API_USER_INSERT = "api.user.insert";
	public static final String API_ERROR = "api.error";
	public static final String AGREEMENT_LIST = "agreement.list";
	public static final String ACTIVATE_ACCOUNT_MANUAL = "activate.account.manual";
	
	public static final String BANLIST_LIST = "banlist.list";
	public static final String BANLIST_INSERT = "banlist.insert";
	public static final String MODERATION_SHOW_ACTIVITY_LOG = "moderation.show.activity.log";
	public static final String MODERATION_LOG_DENIED = "moderation.log.denied";
	
	public static final String SEARCH_STATS_LIST = "search.stats.list";
	public static final String SEARCH_STATS_NOT_ENABLED = "search.stats.not.enabled";
	public static final String AJAX_PREVIEW_POST = "ajax.preview.post";
	public static final String AJAX_IS_POST_INDEXED = "ajax.is.post.indexed";
	public static final String AJAX_TEST_MAIL = "ajax.test.mail";
	public static final String AJAX_LOAD_POST = "ajax.load.post";
}
