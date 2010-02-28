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
 * This file creation date: Apr 24, 2003 / 10:15:07 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jforum.Command;
import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ForumDAO;
import net.jforum.dao.ModerationDAO;
import net.jforum.entities.Forum;
import net.jforum.entities.MostUsersEverOnline;
import net.jforum.entities.UserSession;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.I18n;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.view.admin.ModerationAction;
import net.jforum.view.forum.common.ForumCommon;
import net.jforum.view.forum.common.PostCommon;
import net.jforum.view.forum.common.TopicsCommon;
import net.jforum.view.forum.common.ViewCommon;

/**
 * @author Rafael Steil
 * @version $Id: ForumAction.java,v 1.76 2007/09/03 12:24:32 rafaelsteil Exp $
 */
public class ForumAction extends Command
{
	/**
	 * List all the forums (first page of forum index)?
	 */
	public void list()
	{
		this.setTemplateName(TemplateKeys.FORUMS_LIST);

		this.context.put("allCategories", ForumCommon.getAllCategoriesAndForums(true));
		this.context.put("topicsPerPage", new Integer(SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE)));
		this.context.put("rssEnabled", SystemGlobals.getBoolValue(ConfigKeys.RSS_ENABLED));

		this.context.put("totalMessages", new Integer(ForumRepository.getTotalMessages()));
		this.context.put("totalRegisteredUsers", ForumRepository .totalUsers());
		this.context.put("lastUser", ForumRepository.lastRegisteredUser());

		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		GregorianCalendar gc = new GregorianCalendar();
		this.context.put("now", df.format(gc.getTime()));

		this.context.put("lastVisit", df.format(SessionFacade.getUserSession().getLastVisit()));
		this.context.put("forumRepository", new ForumRepository());

		// Online Users
		this.context.put("totalOnlineUsers", new Integer(SessionFacade.size()));
		int aid = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);

		List onlineUsersList = SessionFacade.getLoggedSessions();

		// Check for an optional language parameter
		UserSession currentUser = SessionFacade.getUserSession();

		if (currentUser.getUserId() == aid) {
			String lang = this.request.getParameter("lang");

			if (lang != null && I18n.languageExists(lang)) {
				currentUser.setLang(lang);
			}
		}

		// If there are only guest users, then just register
		// a single one. In any other situation, we do not
		// show the "guest" username
		if (onlineUsersList.size() == 0) {
			UserSession us = new UserSession();

			us.setUserId(aid);
			us.setUsername(I18n.getMessage("Guest"));

			onlineUsersList.add(us);
		}

		int registeredSize = SessionFacade.registeredSize();
		int anonymousSize = SessionFacade.anonymousSize();
		int totalOnlineUsers = registeredSize + anonymousSize;

		this.context.put("userSessions", onlineUsersList);
		this.context.put("totalOnlineUsers", new Integer(totalOnlineUsers));
		this.context.put("totalRegisteredOnlineUsers", new Integer(registeredSize));
		this.context.put("totalAnonymousUsers", new Integer(anonymousSize));

		// Most users ever online
		MostUsersEverOnline mostUsersEverOnline = ForumRepository.getMostUsersEverOnline();

		if (totalOnlineUsers > mostUsersEverOnline.getTotal()) {
			mostUsersEverOnline.setTotal(totalOnlineUsers);
			mostUsersEverOnline.setTimeInMillis(System.currentTimeMillis());

			ForumRepository.updateMostUsersEverOnline(mostUsersEverOnline);
		}

		this.context.put("mostUsersEverOnline", mostUsersEverOnline);
	}

	public void moderation()
	{
		this.context.put("openModeration", true);
		this.show();
	}

	/**
	 * Display all topics in a forum
	 */
	public void show()
	{
		int forumId = this.request.getIntParameter("forum_id");
		ForumDAO fm = DataAccessDriver.getInstance().newForumDAO();

		// The user can access this forum?
		Forum forum = ForumRepository.getForum(forumId);

		if (forum == null || !ForumRepository.isCategoryAccessible(forum.getCategoryId())) {
			new ModerationHelper().denied(I18n.getMessage("ForumListing.denied"));
			return;
		}

		int start = ViewCommon.getStartPage();

		List tmpTopics = TopicsCommon.topicsByForum(forumId, start);

		this.setTemplateName(TemplateKeys.FORUMS_SHOW);

		// Moderation
		UserSession userSession = SessionFacade.getUserSession();
		boolean isLogged = SessionFacade.isLogged();
		boolean isModerator = userSession.isModerator(forumId);

		boolean canApproveMessages = (isLogged && isModerator 
			&& SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_APPROVE_MESSAGES));

		Map topicsToApprove = new HashMap();

		if (canApproveMessages) {
			ModerationDAO mdao = DataAccessDriver.getInstance().newModerationDAO();
			topicsToApprove = mdao.topicsByForum(forumId);
			this.context.put("postFormatter", new PostCommon());
		}

		this.context.put("topicsToApprove", topicsToApprove);

		this.context.put("attachmentsEnabled", SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_ENABLED,
		        Integer.toString(forumId))
		        || SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_DOWNLOAD));

		this.context.put("topics", TopicsCommon.prepareTopics(tmpTopics));
		this.context.put("allCategories", ForumCommon.getAllCategoriesAndForums(false));
		this.context.put("forum", forum);
		this.context.put("rssEnabled", SystemGlobals.getBoolValue(ConfigKeys.RSS_ENABLED));
		this.context.put("pageTitle", forum.getName());
		this.context.put("canApproveMessages", canApproveMessages);
		this.context.put("replyOnly", !SecurityRepository.canAccess(SecurityConstants.PERM_REPLY_ONLY, Integer
		        .toString(forum.getId())));

		this.context.put("readonly", !SecurityRepository.canAccess(SecurityConstants.PERM_READ_ONLY_FORUMS, Integer
		        .toString(forumId)));

		this.context.put("watching", fm.isUserSubscribed(forumId, userSession.getUserId()));

		// Pagination
		int topicsPerPage = SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
		int totalTopics = forum.getTotalTopics();

		ViewCommon.contextToPagination(start, totalTopics, topicsPerPage);
		this.context.put("postsPerPage", new Integer(postsPerPage));

		TopicsCommon.topicListingBase();
		this.context.put("moderator", isLogged && isModerator);
	}

	// Make an URL to some action
	private String makeRedirect(String action)
	{
		String path = this.request.getContextPath() + "/forums/" + action + "/";
		String thisPage = this.request.getParameter("start");

		if (thisPage != null && !thisPage.equals("0")) {
			path += thisPage + "/";
		}

		String forumId = this.request.getParameter("forum_id");

		if (forumId == null) {
			forumId = this.request.getParameter("persistData");
		}

		path += forumId + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION);

		return path;
	}

	// Mark all topics as read
	public void readAll()
	{
		String forumId = this.request.getParameter("forum_id");
		
		if (forumId != null) {
			Map tracking = SessionFacade.getTopicsReadTimeByForum();
			
			if (tracking == null) {
				tracking = new HashMap();
			}
			
			tracking.put(new Integer(forumId), new Long(System.currentTimeMillis()));
			SessionFacade.setAttribute(ConfigKeys.TOPICS_READ_TIME_BY_FORUM, tracking);
		}

		if (forumId != null) {
			JForumExecutionContext.setRedirect(this.makeRedirect("show"));
		}
		else {
			JForumExecutionContext.setRedirect(this.request.getContextPath() + "/forums/list"
		        + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		}
	}

	// Messages since last visit
	public void newMessages()
	{
		this.request.addParameter("from_date", SessionFacade.getUserSession().getLastVisit());
		this.request.addParameter("to_date", new Date());

		SearchAction searchAction = new SearchAction(this.request, this.response, this.context);
		searchAction.newMessages();
		
		this.setTemplateName(TemplateKeys.SEARCH_NEW_MESSAGES);
	}

	public void approveMessages()
	{
		if (SessionFacade.getUserSession().isModerator(this.request.getIntParameter("forum_id"))) {
			new ModerationAction(this.context, this.request).doSave();
		}

		JForumExecutionContext.setRedirect(this.request.getContextPath() + "/forums/show/"
			+ this.request.getParameter("forum_id") + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}

	/**
	 * Action when users click on "watch this forum"
	 * It gets teh forum_id and userId, and put them into a watch_forum table in the database;
	 */
	public void watchForum()
	{
		int forumId = this.request.getIntParameter("forum_id");
		int userId = SessionFacade.getUserSession().getUserId();

		this.watchForum(DataAccessDriver.getInstance().newForumDAO(), forumId, userId);

		JForumExecutionContext.setRedirect(this.redirectLinkToShowAction(forumId));
	}

	public void banned()
	{
		this.setTemplateName(TemplateKeys.FORUMS_BANNED);
		this.context.put("message", I18n.getMessage("ForumBanned.banned"));
	}

	private String redirectLinkToShowAction(int forumId)
	{
		int start = ViewCommon.getStartPage();

		return this.request.getContextPath() + "/forums/show/" + (start > 0 ? start + "/" : "") + forumId
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION);
	}

	/**
	 * 
	 * @param dao ForumDAO
	 * @param forumId int
	 * @param userId int
	 */
	private void watchForum(ForumDAO dao, int forumId, int userId)
	{
		if (SessionFacade.isLogged() && !dao.isUserSubscribed(forumId, userId)) {
			dao.subscribeUser(forumId, userId);
		}
	}

	/**
	 * Unwatch the forum watched.
	 */
	public void unwatchForum()
	{
		if (SessionFacade.isLogged()) {
			int forumId = this.request.getIntParameter("forum_id");
			int userId = SessionFacade.getUserSession().getUserId();

			DataAccessDriver.getInstance().newForumDAO().removeSubscription(forumId, userId);

			String returnPath = this.redirectLinkToShowAction(forumId);

			this.setTemplateName(TemplateKeys.POSTS_UNWATCH);
			this.context.put("message", I18n.getMessage("ForumBase.forumUnwatched", new String[] { returnPath }));
		}
		else {
			this.setTemplateName(ViewCommon.contextToLogin());
		}
	}
}
