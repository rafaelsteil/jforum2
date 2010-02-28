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
 * This file creation date: May 3, 2003 / 5:05:18 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.Command;
import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.context.RequestContext;
import net.jforum.dao.AttachmentDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ForumDAO;
import net.jforum.dao.KarmaDAO;
import net.jforum.dao.PollDAO;
import net.jforum.dao.PostDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.dao.UserDAO;
import net.jforum.entities.Attachment;
import net.jforum.entities.Forum;
import net.jforum.entities.ModerationLog;
import net.jforum.entities.Poll;
import net.jforum.entities.PollChanges;
import net.jforum.entities.Post;
import net.jforum.entities.QuotaLimit;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.entities.UserSession;
import net.jforum.exceptions.AttachmentException;
import net.jforum.exceptions.ForumException;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.PostRepository;
import net.jforum.repository.RankingRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.repository.SmiliesRepository;
import net.jforum.repository.TopicRepository;
import net.jforum.security.PermissionControl;
import net.jforum.security.SecurityConstants;
import net.jforum.util.I18n;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.view.forum.common.AttachmentCommon;
import net.jforum.view.forum.common.ForumCommon;
import net.jforum.view.forum.common.PollCommon;
import net.jforum.view.forum.common.PostCommon;
import net.jforum.view.forum.common.TopicsCommon;
import net.jforum.view.forum.common.ViewCommon;

import org.apache.commons.lang.StringUtils;

import freemarker.template.SimpleHash;

/**
 * @author Rafael Steil
 * @version $Id: PostAction.java,v 1.198 2007/09/27 04:47:19 rafaelsteil Exp $
 */
public class PostAction extends Command 
{
	public PostAction() {
	}

	public PostAction(RequestContext request, SimpleHash templateContext) {
		super.context = templateContext;
		super.request = request;
	}

	public void list()
	{
		PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
		PollDAO pollDao = DataAccessDriver.getInstance().newPollDAO();

		TopicDAO topicDao = DataAccessDriver.getInstance().newTopicDAO();

		UserSession us = SessionFacade.getUserSession();
		int anonymousUser = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);
		boolean logged = SessionFacade.isLogged();
		
		int topicId = this.request.getIntParameter("topic_id");
		
		Topic topic = TopicRepository.getTopic(new Topic(topicId));
		
		if (topic == null) {
			topic = topicDao.selectById(topicId);
		}

		// The topic exists?
		if (topic.getId() == 0) {
			this.topicNotFound();
			return;
		}

		// Shall we proceed?
		Forum forum = ForumRepository.getForum(topic.getForumId());
		
		if (!logged) {
			if (forum == null || !ForumRepository.isCategoryAccessible(forum.getCategoryId())) {
				this.setTemplateName(ViewCommon.contextToLogin());
				return;
			}
		}
		else if (!TopicsCommon.isTopicAccessible(topic.getForumId())) {
			return;
		}

		int count = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
		int start = ViewCommon.getStartPage();

		PermissionControl pc = SecurityRepository.get(us.getUserId());

		boolean moderatorCanEdit = false;
		if (pc.canAccess(SecurityConstants.PERM_MODERATION_POST_EDIT)) {
			moderatorCanEdit = true;
		}

		List helperList = PostCommon.topicPosts(postDao, moderatorCanEdit, us.getUserId(), topic.getId(), start, count);
		
		// Ugly assumption:
		// Is moderation pending for the topic?
		if (topic.isModerated() && helperList.size() == 0) {
			this.notModeratedYet();
			return;
		}

		// Set the topic status as read
		if (logged) {
			topicDao.updateReadStatus(topic.getId(), us.getUserId(), true);
		}

		boolean canVoteOnPoll = logged && SecurityRepository.canAccess(SecurityConstants.PERM_VOTE);
		Poll poll = null;
		
		if (topic.isVote()) {
			// It has a poll associated with the topic
			poll = pollDao.selectById(topic.getVoteId());
			
			if (canVoteOnPoll) {
				canVoteOnPoll = !pollDao.hasUserVotedOnPoll(topic.getVoteId(), us.getUserId());
			}
		}
		
		topicDao.incrementTotalViews(topic.getId());
		topic.setTotalViews(topic.getTotalViews() + 1);

		if (us.getUserId() != anonymousUser) {
			SessionFacade.getTopicsReadTime().put(new Integer(topic.getId()),
				new Long(System.currentTimeMillis()));
		}
		
		boolean karmaEnabled = SecurityRepository.canAccess(SecurityConstants.PERM_KARMA_ENABLED);
		Map userVotes = new HashMap();
		
		if (logged && karmaEnabled) {
			userVotes = DataAccessDriver.getInstance().newKarmaDAO().getUserVotes(topic.getId(), us.getUserId());
		}
		
		this.setTemplateName(TemplateKeys.POSTS_LIST);
		this.context.put("attachmentsEnabled", pc.canAccess(
			SecurityConstants.PERM_ATTACHMENTS_ENABLED, Integer.toString(topic.getForumId())));
		this.context.put("canDownloadAttachments", pc.canAccess(
			SecurityConstants.PERM_ATTACHMENTS_DOWNLOAD));
		this.context.put("thumbShowBox", SystemGlobals.getBoolValue(ConfigKeys.ATTACHMENTS_IMAGES_THUMB_BOX_SHOW));
		this.context.put("am", new AttachmentCommon(this.request, topic.getForumId()));
		this.context.put("karmaVotes", userVotes);
		this.context.put("rssEnabled", SystemGlobals.getBoolValue(ConfigKeys.RSS_ENABLED));
		this.context.put("canRemove", pc.canAccess(SecurityConstants.PERM_MODERATION_POST_REMOVE));
		this.context.put("moderatorCanEdit", moderatorCanEdit);
		this.context.put("allCategories", ForumCommon.getAllCategoriesAndForums(false));
		this.context.put("topic", topic);
		this.context.put("poll", poll);
		this.context.put("canVoteOnPoll", canVoteOnPoll);
		this.context.put("rank", new RankingRepository());
		this.context.put("posts", helperList);
		this.context.put("forum", forum);
		this.context.put("karmaMin", new Integer(SystemGlobals.getValue(ConfigKeys.KARMA_MIN_POINTS)));
		this.context.put("karmaMax", new Integer(SystemGlobals.getValue(ConfigKeys.KARMA_MAX_POINTS)));
		this.context.put("avatarAllowExternalUrl", SystemGlobals.getBoolValue(ConfigKeys.AVATAR_ALLOW_EXTERNAL_URL));
		this.context.put("moderationLoggingEnabled", SystemGlobals.getBoolValue(ConfigKeys.MODERATION_LOGGING_ENABLED));
		this.context.put("needCaptcha", SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_POSTS));
		
		Map topicPosters = topicDao.topicPosters(topic.getId());
		
		for (Iterator iter = topicPosters.values().iterator(); iter.hasNext(); ) {
			ViewCommon.prepareUserSignature((User)iter.next());
		}
		
		this.context.put("users", topicPosters);
		this.context.put("anonymousPosts", pc.canAccess(SecurityConstants.PERM_ANONYMOUS_POST, 
			Integer.toString(topic.getForumId())));
		this.context.put("watching", topicDao.isUserSubscribed(topicId, SessionFacade.getUserSession().getUserId()));
		this.context.put("pageTitle", topic.getTitle());
		this.context.put("isAdmin", pc.canAccess(SecurityConstants.PERM_ADMINISTRATION));
		this.context.put("readonly", !pc.canAccess(SecurityConstants.PERM_READ_ONLY_FORUMS, 
			Integer.toString(topic.getForumId())));
		this.context.put("replyOnly", !pc.canAccess(SecurityConstants.PERM_REPLY_ONLY, 
			Integer.toString(topic.getForumId())));

		this.context.put("isModerator", us.isModerator(topic.getForumId()));

		ViewCommon.contextToPagination(start, topic.getTotalReplies() + 1, count);
		
		TopicsCommon.topicListingBase();
		TopicRepository.updateTopic(topic);
	}
	
	/**
	 * Given a postId, sends the user to the right page
	 */
	public void preList()
	{
		int postId = this.request.getIntParameter("post_id");
		
		PostDAO dao = DataAccessDriver.getInstance().newPostDAO();
		
		int count = dao.countPreviousPosts(postId);
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
		
		int topicId = 0;
		
		if (this.request.getParameter("topic_id") != null) {
			topicId = this.request.getIntParameter("topic_id");
		}
		
		if (topicId == 0) {
			Post post = dao.selectById(postId);
			topicId = post.getTopicId();
		}
		
		String page = "";
		
		if (count > postsPerPage) {
			page = Integer.toString(postsPerPage * ((count - 1) / postsPerPage)) + "/";
		} 

		JForumExecutionContext.setRedirect(this.request.getContextPath() + "/posts/list/"
			+ page + topicId
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) 
			+ "#" + postId);
	}
	
	/**
	 * Votes on a poll.
	 */
	public void vote()
	{
		int pollId = this.request.getIntParameter("poll_id");
		int topicId = this.request.getIntParameter("topic_id");
		
		if (SessionFacade.isLogged() && this.request.getParameter("poll_option") != null) {
			Topic topic = TopicRepository.getTopic(new Topic(topicId));
			
			if (topic == null) {
				topic = DataAccessDriver.getInstance().newTopicDAO().selectRaw(topicId);
			}
			
			if (topic.getStatus() == Topic.STATUS_LOCKED) {
				this.topicLocked();
				return;
			}
			
			// They voted, save the value
			int optionId = this.request.getIntParameter("poll_option");
			
			PollDAO dao = DataAccessDriver.getInstance().newPollDAO();
			
			//vote on the poll
			UserSession user = SessionFacade.getUserSession();
			dao.voteOnPoll(pollId, optionId, user.getUserId(), request.getRemoteAddr());
		}

		JForumExecutionContext.setRedirect(this.request.getContextPath() 
			+ "/posts/list/"
			+ topicId
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}

	public void listByUser()
	{
		PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		TopicDAO tm = DataAccessDriver.getInstance().newTopicDAO();

		User u = um.selectById(this.request.getIntParameter("user_id"));
		
		if (u.getId() == 0) {
			this.context.put("message", I18n.getMessage("User.notFound"));
			this.setTemplateName(TemplateKeys.USER_NOT_FOUND);
			return;
		} 
			
		int count = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
		int start = ViewCommon.getStartPage();
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
		
		List posts = pm.selectByUserByLimit(u.getId(), start, postsPerPage);
		int totalMessages = pm.countUserPosts(u.getId());
		
		// get list of forums
		Map topics = new HashMap();
		Map forums = new HashMap();
		
		for (Iterator iter = posts.iterator(); iter.hasNext(); ) {
			Post p = (Post)iter.next();

			if (!topics.containsKey(new Integer(p.getTopicId()))) {
				Topic t = TopicRepository.getTopic(new Topic(p.getTopicId()));
				
				if (t == null) {
					t = tm.selectRaw(p.getTopicId());
				}
				
				this.context.put("attachmentsEnabled", SecurityRepository.canAccess(
						SecurityConstants.PERM_ATTACHMENTS_ENABLED, Integer.toString(t.getForumId())));
				this.context.put("am", new AttachmentCommon(this.request, t.getForumId()));				
	
				topics.put(new Integer(t.getId()), t);
			}
			
			if (!forums.containsKey(new Integer(p.getForumId()))) {
				Forum f = ForumRepository.getForum(p.getForumId());
				
				if (f == null) {
					// Ok, probably the user does not have permission to see this forum
					iter.remove();
					totalMessages--;
					continue;
				}
				
				forums.put(new Integer(f.getId()), f);
			}
			
			PostCommon.preparePostForDisplay(p);
		}
		
		this.setTemplateName(TemplateKeys.POSTS_USER_POSTS_LIST);
		
		this.context.put("canDownloadAttachments", SecurityRepository.canAccess(
				SecurityConstants.PERM_ATTACHMENTS_DOWNLOAD));				
		this.context.put("rssEnabled", SystemGlobals.getBoolValue(ConfigKeys.RSS_ENABLED));
		this.context.put("allCategories", ForumCommon.getAllCategoriesAndForums(false));
		this.context.put("posts", posts);
		this.context.put("topics", topics);
		this.context.put("forums", forums);
		this.context.put("u", u);
		this.context.put("pageTitle", I18n.getMessage("PostShow.userPosts") + " " + u.getUsername());
		this.context.put("karmaMin", new Integer(SystemGlobals.getValue(ConfigKeys.KARMA_MIN_POINTS)));
		this.context.put("karmaMax", new Integer(SystemGlobals.getValue(ConfigKeys.KARMA_MAX_POINTS)));
		
		ViewCommon.contextToPagination(start, totalMessages, count);
	}

	public void review()
	{
		PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
		TopicDAO topicDao = DataAccessDriver.getInstance().newTopicDAO();

		int userId = SessionFacade.getUserSession().getUserId();
		int topicId = this.request.getIntParameter("topic_id");
		
		Topic topic = TopicRepository.getTopic(new Topic(topicId));
		
		if (topic == null) {
			topic = topicDao.selectById(topicId);
		}

		if (!TopicsCommon.isTopicAccessible(topic.getForumId())) {
			return;
		}

		int count = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
		int start = ViewCommon.getStartPage();

		Map usersMap = topicDao.topicPosters(topic.getId());
		List helperList = PostCommon.topicPosts(postDao, false, userId, topic.getId(), start, count);
		Collections.reverse(helperList);

		this.setTemplateName(SystemGlobals.getValue(ConfigKeys.TEMPLATE_DIR) + "/empty.htm");

		this.setTemplateName(TemplateKeys.POSTS_REVIEW);
		this.context.put("posts", helperList);
		this.context.put("users", usersMap);
	}

	private void topicNotFound() {
		this.setTemplateName(TemplateKeys.POSTS_TOPIC_NOT_FOUND);
		this.context.put("message", I18n.getMessage("PostShow.TopicNotFound"));
	}

	private void postNotFound() {
		this.setTemplateName(TemplateKeys.POSTS_POST_NOT_FOUND);
		this.context.put("message", I18n.getMessage("PostShow.PostNotFound"));
	}
	
	private void replyOnly()
	{
		this.setTemplateName(TemplateKeys.POSTS_REPLY_ONLY);
		this.context.put("message", I18n.getMessage("PostShow.replyOnly"));
	}
	
	private boolean isReplyOnly(int forumId)
	{
		return !SecurityRepository.canAccess(SecurityConstants.PERM_REPLY_ONLY, 
				Integer.toString(forumId));
	}
	
	public void reply()
	{
		this.insert();
	}

	public void insert()
	{
		int forumId;

		// If we have a topic_id, then it should be a reply
		if (this.request.getParameter("topic_id") != null) {
			int topicId = this.request.getIntParameter("topic_id");
			
			Topic t = TopicRepository.getTopic(new Topic(topicId));
			
			if (t == null) {
				t = DataAccessDriver.getInstance().newTopicDAO().selectRaw(topicId);
				
				if (t == null) {
					throw new ForumException("Could not find a topic with id #" + topicId);
				}
			}
			
			forumId = t.getForumId();
			
			if (!TopicsCommon.isTopicAccessible(t.getForumId())) {
				return;
			}

			if (t.getStatus() == Topic.STATUS_LOCKED) {
				this.topicLocked();
				return;
			}

			this.context.put("topic", t);
			this.context.put("setType", false);
			this.context.put("pageTitle", I18n.getMessage("PostForm.reply")+" "+t.getTitle());
		}
		else {
			forumId = this.request.getIntParameter("forum_id");

			if (this.isReplyOnly(forumId)) {
				this.replyOnly();
				return;
			}
			this.context.put("setType", true);
			this.context.put("pageTitle", I18n.getMessage("PostForm.title"));
		}
		
		Forum forum = ForumRepository.getForum(forumId);
		
		if (forum == null) {
			throw new ForumException("Could not find a forum with id #" + forumId);
		}
		
		if (!TopicsCommon.isTopicAccessible(forumId)) {
			return;
		}
		
		if (!this.anonymousPost(forumId)
				|| this.isForumReadonly(forumId, this.request.getParameter("topic_id") != null)) {
			return;
		}
		
		int userId = SessionFacade.getUserSession().getUserId();
		
		this.setTemplateName(TemplateKeys.POSTS_INSERT);
		
		// Attachments
		boolean attachmentsEnabled = SecurityRepository.canAccess(
			SecurityConstants.PERM_ATTACHMENTS_ENABLED, Integer.toString(forumId));
		
		if (attachmentsEnabled && !SessionFacade.isLogged() 
			&& !SystemGlobals.getBoolValue(ConfigKeys.ATTACHMENTS_ANONYMOUS)) {
			attachmentsEnabled = false;
		}

		this.context.put("attachmentsEnabled", attachmentsEnabled);
		
		if (attachmentsEnabled) {
			QuotaLimit ql = new AttachmentCommon(this.request, forumId).getQuotaLimit(userId);
			this.context.put("maxAttachmentsSize", new Long(ql != null ? ql.getSizeInBytes() : 1));
			this.context.put("maxAttachments", SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		}
		
		boolean needCaptcha = SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_POSTS);
		
		this.context.put("moderationLoggingEnabled", SystemGlobals.getBoolValue(ConfigKeys.MODERATION_LOGGING_ENABLED));
		this.context.put("smilies", SmiliesRepository.getSmilies());
		this.context.put("forum", forum);
		this.context.put("action", "insertSave");
		this.context.put("start", this.request.getParameter("start"));
		this.context.put("isNewPost", true);
		this.context.put("needCaptcha", needCaptcha);
		this.context.put("htmlAllowed",
			SecurityRepository.canAccess(SecurityConstants.PERM_HTML_DISABLED, Integer.toString(forumId)));
		this.context.put("canCreateStickyOrAnnouncementTopics",
			SecurityRepository.canAccess(SecurityConstants.PERM_CREATE_STICKY_ANNOUNCEMENT_TOPICS));
		this.context.put("canCreatePolls",
			SecurityRepository.canAccess(SecurityConstants.PERM_CREATE_POLL));

		User user = DataAccessDriver.getInstance().newUserDAO().selectById(userId);
		
		ViewCommon.prepareUserSignature(user);
		
		if (this.request.getParameter("preview") != null) {
			user.setNotifyOnMessagesEnabled(this.request.getParameter("notify") != null);
		}

		this.context.put("user", user);
	}

	public void edit()  {
		this.edit(false, null);
	}

	private void edit(boolean preview, Post p)
	{
		int userId = SessionFacade.getUserSession().getUserId();

		if (!preview) {
			PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
			p = pm.selectById(this.request.getIntParameter("post_id"));

			// The post exist?
			if (p.getId() == 0) {
				this.postNotFound();
				return;
			}
		}

		boolean isModerator = SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_POST_EDIT);
		boolean canEdit = SessionFacade.isLogged() && (isModerator || p.getUserId() == userId);

		if (!canEdit) {
			this.setTemplateName(TemplateKeys.POSTS_EDIT_CANNOTEDIT);
			this.context.put("message", I18n.getMessage("CannotEditPost"));
		}
		else {
			Topic topic = TopicRepository.getTopic(new Topic(p.getTopicId()));
				
			if (topic == null) {
				topic = DataAccessDriver.getInstance().newTopicDAO().selectRaw(p.getTopicId());
			}

			if (!TopicsCommon.isTopicAccessible(topic.getForumId())) {
				return;
			}

			if (topic.getStatus() == Topic.STATUS_LOCKED && !isModerator) {
				this.topicLocked();
				return;
			}

			if (preview && this.request.getParameter("topic_type") != null) {
				topic.setType(this.request.getIntParameter("topic_type"));
			}
			
			if (p.hasAttachments()) {
				this.context.put("attachments", 
						DataAccessDriver.getInstance().newAttachmentDAO().selectAttachments(p.getId()));
			}

			Poll poll = null;
			
			if (topic.isVote() && topic.getFirstPostId() == p.getId()) {
				// It has a poll associated with the topic
				PollDAO poolDao = DataAccessDriver.getInstance().newPollDAO();
				poll = poolDao.selectById(topic.getVoteId());
			}
			
			this.setTemplateName(TemplateKeys.POSTS_EDIT);

			this.context.put("attachmentsEnabled", SecurityRepository.canAccess(
					SecurityConstants.PERM_ATTACHMENTS_ENABLED, Integer.toString(p.getForumId())));
			
			this.context.put("moderationLoggingEnabled", SystemGlobals.getBoolValue(ConfigKeys.MODERATION_LOGGING_ENABLED));
		
			QuotaLimit ql = new AttachmentCommon(this.request, p.getForumId()).getQuotaLimit(userId);
			this.context.put("maxAttachmentsSize", new Long(ql != null ? ql.getSizeInBytes() : 1));
			this.context.put("isEdit", true);
			this.context.put("maxAttachments", SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
			this.context.put("smilies", SmiliesRepository.getSmilies());
			this.context.put("forum", ForumRepository.getForum(p.getForumId()));
			this.context.put("action", "editSave");
			this.context.put("post", p);
			this.context.put("setType", p.getId() == topic.getFirstPostId());
			this.context.put("topic", topic);
			this.context.put("poll", poll);
			this.context.put("pageTitle", I18n.getMessage("PostShow.messageTitle") + " " + p.getSubject());
			this.context.put("isModerator", isModerator);
			this.context.put("start", this.request.getParameter("start"));
			this.context.put("htmlAllowed", SecurityRepository.canAccess(SecurityConstants.PERM_HTML_DISABLED, 
					Integer.toString(topic.getForumId())));
			this.context.put("canCreateStickyOrAnnouncementTopics",
					SecurityRepository.canAccess(SecurityConstants.PERM_CREATE_STICKY_ANNOUNCEMENT_TOPICS));
			this.context.put("canCreatePolls",
					SecurityRepository.canAccess(SecurityConstants.PERM_CREATE_POLL));
		}

		UserDAO udao = DataAccessDriver.getInstance().newUserDAO();
		User u = udao.selectById(userId);
		ViewCommon.prepareUserSignature(u);

		if (preview) {
			u.setNotifyOnMessagesEnabled(this.request.getParameter("notify") != null);
			
			if (u.getId() != p.getUserId()) {
				// Probably a moderator is editing the message
				User previewUser = udao.selectById(p.getUserId());
				ViewCommon.prepareUserSignature(previewUser);
				this.context.put("previewUser", previewUser);
			}
		}

		this.context.put("user", u);
	}
	
	public void quote()
	{
		PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
		Post p = pm.selectById(this.request.getIntParameter("post_id"));
		
		if (p.getId() == 0) {
			this.postNotFound();
			return;
		}
		
		if (p.isModerationNeeded()) {
			this.notModeratedYet();
			return;
		}

		if (!this.anonymousPost(p.getForumId())) {
			return;
		}

		Topic topic = TopicRepository.getTopic(new Topic(p.getTopicId()));
		
		if (topic == null) {
			topic = DataAccessDriver.getInstance().newTopicDAO().selectRaw(p.getTopicId());
		}

		if (!TopicsCommon.isTopicAccessible(topic.getForumId())) {
			return;
		}

		if (topic.getStatus() == Topic.STATUS_LOCKED) {
			this.topicLocked();
			return;
		}
		
		this.setTemplateName(TemplateKeys.POSTS_QUOTE);

		this.context.put("forum", ForumRepository.getForum(p.getForumId()));
		this.context.put("action", "insertSave");
		this.context.put("post", p);

		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		User u = um.selectById(p.getUserId());

		int userId = SessionFacade.getUserSession().getUserId();
		
		this.context.put("attachmentsEnabled", SecurityRepository.canAccess(
			SecurityConstants.PERM_ATTACHMENTS_ENABLED, Integer.toString(topic.getForumId())));
		
		QuotaLimit ql = new AttachmentCommon(this.request, topic.getForumId()).getQuotaLimit(userId);
		this.context.put("maxAttachmentsSize", new Long(ql != null ? ql.getSizeInBytes() : 1));
		
		this.context.put("moderationLoggingEnabled", SystemGlobals.getBoolValue(ConfigKeys.MODERATION_LOGGING_ENABLED));
		this.context.put("maxAttachments", SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_MAX_POST));
		this.context.put("isNewPost", true);
		this.context.put("topic", topic);
		this.context.put("quote", "true");
		this.context.put("quoteUser", u.getUsername());
		this.context.put("setType", false);
		this.context.put("htmlAllowed", SecurityRepository.canAccess(SecurityConstants.PERM_HTML_DISABLED, 
			Integer.toString(topic.getForumId())));
		this.context.put("start", this.request.getParameter("start"));
		this.context.put("user", DataAccessDriver.getInstance().newUserDAO().selectById(userId));
		this.context.put("pageTitle", I18n.getMessage("PostForm.reply") + " " + topic.getTitle());
		this.context.put("smilies", SmiliesRepository.getSmilies());
		
		boolean needCaptcha = SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_POSTS);
		
		if (needCaptcha) {
			SessionFacade.getUserSession().createNewCaptcha();
		}
		
		this.context.put("needCaptcha", needCaptcha);
	}

	public void editSave()
	{
		PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
		PollDAO pollDao = DataAccessDriver.getInstance().newPollDAO();
		TopicDAO topicDao = DataAccessDriver.getInstance().newTopicDAO();
		
		Post post = postDao.selectById(this.request.getIntParameter("post_id"));
		
		if (!PostCommon.canEditPost(post)) {
			this.cannotEdit();
			return;
		}
		
		boolean isModerator = SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_POST_EDIT);
		
		String originalMessage = post.getText();
		
		post = PostCommon.fillPostFromRequest(post, true);

		// The user wants to preview the message before posting it?
		if ("1".equals(this.request.getParameter("preview"))) {
			this.context.put("preview", true);

			Post postPreview = new Post(post);
			this.context.put("postPreview", PostCommon.preparePostForDisplay(postPreview));

			this.edit(true, post);
		}
		else {
			AttachmentCommon attachments = new AttachmentCommon(this.request, post.getForumId());
			
			try {
				attachments.preProcess();
			}
			catch (AttachmentException e) {
				JForumExecutionContext.enableRollback();
				post.setText(this.request.getParameter("message"));
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", post);
				this.edit(false, post);
				return;
			}
			
			Topic t = TopicRepository.getTopic(new Topic(post.getTopicId()));
			
			if (t == null) {
				t = topicDao.selectById(post.getTopicId());
			}

			if (!TopicsCommon.isTopicAccessible(t.getForumId())) {
				return;
			}

			if (t.getStatus() == Topic.STATUS_LOCKED
					&& !SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_POST_EDIT)) {
				this.topicLocked();
				return;
			}

			postDao.update(post);
			
			// Attachments
			attachments.editAttachments(post.getId(), post.getForumId());
			attachments.insertAttachments(post);

			// The first message (the one which originated the topic) was changed
			if (t.getFirstPostId() == post.getId()) {
				t.setTitle(post.getSubject());
				
				int newType = this.request.getIntParameter("topic_type");
				boolean changeType = SecurityRepository.canAccess(SecurityConstants.PERM_CREATE_STICKY_ANNOUNCEMENT_TOPICS)
					&& newType != t.getType();
				
				if (changeType) {
					t.setType(newType);
				}

				// Poll
				Poll poll = PollCommon.fillPollFromRequest();
				
				if (poll != null && !t.isVote()) {
					// They added a poll
					poll.setTopicId(t.getId());
					
					if (!this.ensurePollMinimumOptions(post, poll)) {
						return;
					}
					
					pollDao.addNew(poll);
					t.setVoteId(poll.getId());

				} 
				else if (poll != null) {
					if (!this.ensurePollMinimumOptions(post, poll)) {
						return;
					}
					
					// They edited the poll in the topic
					Poll existing = pollDao.selectById(t.getVoteId());
					PollChanges changes = new PollChanges(existing, poll);
					
					if (changes.hasChanges()) {
						poll.setId(existing.getId());
						poll.setChanges(changes);
						pollDao.update(poll);
					}
					
				} 
				else if (t.isVote()) {
					// They deleted the poll from the topic
					pollDao.delete(t.getVoteId());
					t.setVoteId(0);
				}
				
				topicDao.update(t);
				
				if (changeType) {
					TopicRepository.addTopic(t);
				}
				else {
					TopicRepository.updateTopic(t);
				}
			}
			
			if (SystemGlobals.getBoolValue(ConfigKeys.MODERATION_LOGGING_ENABLED)
					&& isModerator && post.getUserId() != SessionFacade.getUserSession().getUserId()) {
				ModerationHelper helper = new ModerationHelper();
				this.request.addParameter("log_original_message", originalMessage);
				ModerationLog log = helper.buildModerationLogFromRequest();
				log.getPosterUser().setId(post.getUserId());
				helper.saveModerationLog(log);
			}

			if (this.request.getParameter("notify") == null) {
				topicDao.removeSubscription(post.getTopicId(), SessionFacade.getUserSession().getUserId());
			}

			String path = this.request.getContextPath() + "/posts/list/";
			int start = ViewCommon.getStartPage();
			
			if (start > 0) {
				path += start + "/";
			}

			path += post.getTopicId() + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) + "#" + post.getId();
			JForumExecutionContext.setRedirect(path);
			
			if (SystemGlobals.getBoolValue(ConfigKeys.POSTS_CACHE_ENABLED)) {
				PostRepository.update(post.getTopicId(), PostCommon.preparePostForDisplay(post));
			}
		}
	}
	
	private boolean ensurePollMinimumOptions(Post p, Poll poll)
	{
		if (poll.getOptions().size() < 2) {
			// It is not a valid poll, cancel the post
			JForumExecutionContext.enableRollback();
			p.setText(this.request.getParameter("message"));
			p.setId(0);
			this.context.put("errorMessage", I18n.getMessage("PostForm.needMorePollOptions"));
			this.context.put("post", p);
			this.context.put("poll", poll);
			this.edit();
			return false;
		}
		
		return true;
	}
	
	public void waitingModeration()
	{
		this.setTemplateName(TemplateKeys.POSTS_WAITING);
		
		int topicId = this.request.getIntParameter("topic_id");
		String path = this.request.getContextPath();
		
		if (topicId == 0) {
			path += "/forums/show/" + this.request.getParameter("forum_id");
		}
		else {
			path += "/posts/list/" + topicId;
		}
		
		this.context.put("message", I18n.getMessage("PostShow.waitingModeration", 
				new String[] { path + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) }));
	}
	
	private void notModeratedYet()
	{
		this.setTemplateName(TemplateKeys.POSTS_NOT_MODERATED);
		this.context.put("message", I18n.getMessage("PostShow.notModeratedYet"));
	}

	public void insertSave()
	{
		int forumId = this.request.getIntParameter("forum_id");
		boolean firstPost = false;

		if (!this.anonymousPost(forumId)) {
			return;
		}
		
		Topic t = new Topic(-1);
		t.setForumId(forumId);

		boolean newTopic = (this.request.getParameter("topic_id") == null);
		
		if (!TopicsCommon.isTopicAccessible(t.getForumId())
				|| this.isForumReadonly(t.getForumId(), newTopic)) {
			return;
		}

		TopicDAO topicDao = DataAccessDriver.getInstance().newTopicDAO();
		PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
		PollDAO poolDao = DataAccessDriver.getInstance().newPollDAO();
		ForumDAO forumDao = DataAccessDriver.getInstance().newForumDAO();

		if (!newTopic) {
			int topicId = this.request.getIntParameter("topic_id");
			
			t = TopicRepository.getTopic(new Topic(topicId));
			
			if (t == null) {
				t = topicDao.selectById(topicId);
			}
			
			// Could not find the topic. The topicId sent was invalid
			if (t == null || t.getId() == 0) {
				newTopic = true;
			}
			else {
				if (!TopicsCommon.isTopicAccessible(t.getForumId())) {
					return;
				}
	
				// Cannot insert new messages on locked topics
				if (t.getStatus() == Topic.STATUS_LOCKED) {
					this.topicLocked();
					return;
				}
			}
		}
		
		// We don't use "else if" here because there is a possibility of the
		// checking above set the newTopic var to true
		if (newTopic) {
			if (this.isReplyOnly(forumId)) {
				this.replyOnly();
				return;
			}
			
			if (this.request.getParameter("topic_type") != null) {
				t.setType(this.request.getIntParameter("topic_type"));
				
				if (t.getType() != Topic.TYPE_NORMAL 
						&& !SecurityRepository.canAccess(SecurityConstants.PERM_CREATE_STICKY_ANNOUNCEMENT_TOPICS)) {
					t.setType(Topic.TYPE_NORMAL);
				}
			}
		}
		
		UserSession us = SessionFacade.getUserSession();
		User u = DataAccessDriver.getInstance().newUserDAO().selectById(us.getUserId());
		
		if ("1".equals(this.request.getParameter("quick")) && SessionFacade.isLogged()) {
			this.request.addParameter("notify", u.isNotifyOnMessagesEnabled() ? "1" : null);
			this.request.addParameter("attach_sig", u.getAttachSignatureEnabled() ? "1" : "0");
		}
		else {
			u.setId(us.getUserId());
			u.setUsername(us.getUsername());
		}

		// Set the Post
		Post p = PostCommon.fillPostFromRequest();
		
		if (p.getText() == null || p.getText().trim().equals("")) {
			this.insert();
			return;
		}
		
		// Check the elapsed time since the last post from the user
		int delay = SystemGlobals.getIntValue(ConfigKeys.POSTS_NEW_DELAY);
		
		if (delay > 0) {
			Long lastPostTime = (Long)SessionFacade.getAttribute(ConfigKeys.LAST_POST_TIME);
			
			if (lastPostTime != null) {
				if (System.currentTimeMillis() < (lastPostTime.longValue() + delay)) {
					this.context.put("post", p);
					this.context.put("start", this.request.getParameter("start"));
					this.context.put("error", I18n.getMessage("PostForm.tooSoon"));
					this.insert();
					return;
				}
			}
		}
		
		p.setForumId(this.request.getIntParameter("forum_id"));
		
		if (StringUtils.isBlank(p.getSubject())) {
			p.setSubject(t.getTitle());
		}
		
		boolean needCaptcha = SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_POSTS)
			&& request.getSessionContext().getAttribute(ConfigKeys.REQUEST_IGNORE_CAPTCHA) == null;
		
		if (needCaptcha) {
			if (!us.validateCaptchaResponse(this.request.getParameter("captcha_anwser"))) {
				this.context.put("post", p);
				this.context.put("start", this.request.getParameter("start"));
				this.context.put("error", I18n.getMessage("CaptchaResponseFails"));
				
				this.insert();
				
				return;
			}
		}

		boolean preview = "1".equals(this.request.getParameter("preview"));
		
		if (!preview) {
			AttachmentCommon attachments = new AttachmentCommon(this.request, forumId);
			
			try {
				attachments.preProcess();
			}
			catch (AttachmentException e) {
				JForumExecutionContext.enableRollback();
				p.setText(this.request.getParameter("message"));
				p.setId(0);
				this.context.put("errorMessage", e.getMessage());
				this.context.put("post", p);
				this.insert();
				return;
			}
			
			Forum forum = ForumRepository.getForum(forumId);
			PermissionControl pc = SecurityRepository.get(us.getUserId());

			// Moderators and admins don't need to have their messages moderated
			boolean moderate = (forum.isModerated() 
				&& !pc.canAccess(SecurityConstants.PERM_MODERATION)
				&& !pc.canAccess(SecurityConstants.PERM_ADMINISTRATION));
			
			if (newTopic) {
				t.setTime(new Date());
				t.setTitle(this.request.getParameter("subject"));
				t.setModerated(moderate);
				t.setPostedBy(u);
				t.setFirstPostTime(ViewCommon.formatDate(t.getTime()));

				int topicId = topicDao.addNew(t);
				t.setId(topicId);
				firstPost = true;
			}
			
			if (!firstPost && pc.canAccess(
					SecurityConstants.PERM_REPLY_WITHOUT_MODERATION, Integer.toString(t.getForumId()))) {
				moderate = false;
			}

			// Topic watch
			if (this.request.getParameter("notify") != null) {
				this.watch(topicDao, t.getId(), u.getId());
			}

			p.setTopicId(t.getId());

			// add a poll
			Poll poll = PollCommon.fillPollFromRequest();
			
			if (poll != null && newTopic) {
				poll.setTopicId(t.getId());
				
				if (poll.getOptions().size() < 2) {
					//it is not a valid poll, cancel the post
					JForumExecutionContext.enableRollback();
					p.setText(this.request.getParameter("message"));
					p.setId(0);
					this.context.put("errorMessage", I18n.getMessage("PostForm.needMorePollOptions"));
					this.context.put("post", p);
					this.context.put("poll", poll);
					this.insert();
					return;
				}
				
				poolDao.addNew(poll);
				t.setVoteId(poll.getId());
			}

			// Save the remaining stuff
			p.setModerate(moderate);
			int postId = postDao.addNew(p);

			if (newTopic) {
				t.setFirstPostId(postId);
			}
			
			if (!moderate) {
				t.setLastPostId(postId);
				t.setLastPostBy(u);
				t.setLastPostDate(p.getTime());
				t.setLastPostTime(p.getFormatedTime());
			}
			
			topicDao.update(t);
			
			attachments.insertAttachments(p);
			
			if (!moderate) {
				StringBuffer path = new StringBuffer(512);
				path.append(this.request.getContextPath()).append("/posts/list/");
				
				int start = ViewCommon.getStartPage();
	
				path.append(this.startPage(t, start)).append("/")
					.append(t.getId()).append(SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION))
					.append('#').append(postId);
	
				JForumExecutionContext.setRedirect(path.toString());
				
				if (newTopic) {
					// Notify "forum new topic" users
					ForumCommon.notifyUsers(forum, t, p);
				}
				else {
					t.setTotalReplies(t.getTotalReplies() + 1);
					TopicsCommon.notifyUsers(t, p);
				}
				
				// Update forum stats, cache and etc
				t.setTotalViews(t.getTotalViews() + 1);
				
				DataAccessDriver.getInstance().newUserDAO().incrementPosts(p.getUserId());
				
				TopicsCommon.updateBoardStatus(t, postId, firstPost, topicDao, forumDao);
				ForumRepository.updateForumStats(t, u, p);
				
				int anonymousUser = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);
				
				if (u.getId() != anonymousUser) {
					SessionFacade.getTopicsReadTime().put(new Integer(t.getId()),
						new Long(p.getTime().getTime()));
				}
				
				if (SystemGlobals.getBoolValue(ConfigKeys.POSTS_CACHE_ENABLED)) {
					SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
					p.setFormatedTime(df.format(p.getTime()));
					
					PostRepository.append(p.getTopicId(), PostCommon.preparePostForDisplay(p));
				}
			}
			else {
				JForumExecutionContext.setRedirect(this.request.getContextPath() 
					+ "/posts/waitingModeration/" 
					+ (firstPost ? 0 : t.getId())
					+ "/" + t.getForumId()
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
			}
			
			if (delay > 0) {
				SessionFacade.setAttribute(ConfigKeys.LAST_POST_TIME, new Long(System.currentTimeMillis()));
			}
		}
		else {
			this.context.put("preview", true);
			this.context.put("post", p);
			this.context.put("start", this.request.getParameter("start"));

			Post postPreview = new Post(p);
			this.context.put("postPreview", PostCommon.preparePostForDisplay(postPreview));

			this.insert();
		}
	}

	private int startPage(Topic t, int currentStart) {
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);

		int newStart = (t.getTotalReplies() + 1) / postsPerPage * postsPerPage;
		
		return (newStart > currentStart) ? newStart : currentStart;
	}

	public void delete()
	{
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_POST_REMOVE)) {
			this.setTemplateName(TemplateKeys.POSTS_CANNOT_DELETE);
			this.context.put("message", I18n.getMessage("CannotRemovePost"));

			return;
		}

		// Post
		PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
		Post p = postDao.selectById(this.request.getIntParameter("post_id"));
		
		if (p.getId() == 0) {
			this.postNotFound();
			return;
		}

		TopicDAO topicDao = DataAccessDriver.getInstance().newTopicDAO();
		Topic t = topicDao.selectRaw(p.getTopicId());

		if (!TopicsCommon.isTopicAccessible(t.getForumId())) {
			return;
		}

		postDao.delete(p);
		DataAccessDriver.getInstance().newUserDAO().decrementPosts(p.getUserId());
		
		// Karma
		KarmaDAO karmaDao = DataAccessDriver.getInstance().newKarmaDAO();
		karmaDao.updateUserKarma(p.getUserId());
		
		// Attachments
		new AttachmentCommon(this.request, p.getForumId()).deleteAttachments(p.getId(), p.getForumId());

		// It was the last remaining post in the topic?
		int totalPosts = topicDao.getTotalPosts(p.getTopicId());

		if (totalPosts > 0) {
			// Topic
			topicDao.decrementTotalReplies(p.getTopicId());

			int maxPostId = topicDao.getMaxPostId(p.getTopicId());
			if (maxPostId > -1) {
				topicDao.setLastPostId(p.getTopicId(), maxPostId);
			}

			int minPostId = topicDao.getMinPostId(p.getTopicId());
			if (minPostId > -1) {
			  topicDao.setFirstPostId(p.getTopicId(), minPostId);
			}
	        
			// Forum
			ForumDAO fm = DataAccessDriver.getInstance().newForumDAO();

			maxPostId = fm.getMaxPostId(p.getForumId());
			if (maxPostId > -1) {
				fm.setLastPost(p.getForumId(), maxPostId);
			}

			String returnPath = this.request.getContextPath() + "/posts/list/";

			int page = ViewCommon.getStartPage();
			
			if (page > 0) {
				int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);

				if (totalPosts % postsPerPage == 0) {
					page -= postsPerPage;
				}

				returnPath += page + "/";
			}

			JForumExecutionContext.setRedirect(returnPath 
				+ p.getTopicId() 
				+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
			
			// Update the cache
			if (TopicRepository.isTopicCached(t)) {
				t = topicDao.selectById(t.getId());
				TopicRepository.updateTopic(t);
			}
		}
		else {
			// Ok, all posts were removed. Time to say goodbye
			TopicsCommon.deleteTopic(p.getTopicId(), p.getForumId(), false);

			JForumExecutionContext.setRedirect(this.request.getContextPath() 
				+ "/forums/show/" 
				+ p.getForumId()
				+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		}
		
		this.request.addOrReplaceParameter("log_original_message", p.getText());
		ModerationHelper moderationHelper = new ModerationHelper();
		ModerationLog moderationLog = moderationHelper.buildModerationLogFromRequest();
		moderationLog.getPosterUser().setId(p.getUserId());
		moderationHelper.saveModerationLog(moderationLog);
		
		PostRepository.remove(t.getId(), p.getId());
		TopicRepository.loadMostRecentTopics();
		TopicRepository.loadHottestTopics();
		ForumRepository.reloadForum(p.getForumId());
	}

	private void watch(TopicDAO tm, int topicId, int userId)  
	{
		if (!tm.isUserSubscribed(topicId, userId)) {
			tm.subscribeUser(topicId, userId);
		}
	}

	public void watch()  
	{
		int topicId = this.request.getIntParameter("topic_id");
		int userId = SessionFacade.getUserSession().getUserId();

		this.watch(DataAccessDriver.getInstance().newTopicDAO(), topicId, userId);
		this.list();
	}

	public void unwatch()  
	{
		if (!SessionFacade.isLogged()) {
			this.setTemplateName(ViewCommon.contextToLogin());
		}
		else {
			int topicId = this.request.getIntParameter("topic_id");
			int userId = SessionFacade.getUserSession().getUserId();
			int start = ViewCommon.getStartPage();

			DataAccessDriver.getInstance().newTopicDAO().removeSubscription(topicId, userId);

			String returnPath = this.request.getContextPath() + "/posts/list/";
			
			if (start > 0) {
				returnPath += start + "/";
			}

			returnPath += topicId + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION);

			this.setTemplateName(TemplateKeys.POSTS_UNWATCH);
			this.context.put("pageTitle", I18n.getMessage("PostShow.unwatch"));
			this.context.put("message", I18n.getMessage("ForumBase.unwatched", new String[] { returnPath }));
		}
	}
	
	public void downloadAttach()
	{
		int id = this.request.getIntParameter("attach_id");
		
		if (!SessionFacade.isLogged() && !SystemGlobals.getBoolValue(ConfigKeys.ATTACHMENTS_ANONYMOUS)) {
			String referer = this.request.getHeader("Referer");
			
			if (referer != null) {
				this.setTemplateName(ViewCommon.contextToLogin(referer));
			}
			else {
				this.setTemplateName(ViewCommon.contextToLogin());
			}
			
			return;
		}
		
		AttachmentDAO am = DataAccessDriver.getInstance().newAttachmentDAO();
		Attachment a = am.selectAttachmentById(id);
		
		PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
		Post post = postDao.selectById(a.getPostId());
		
		String forumId = Integer.toString(post.getForumId());
		
		boolean attachmentsEnabled = SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_ENABLED, forumId);
		boolean attachmentsDownload = SecurityRepository.canAccess(SecurityConstants.PERM_ATTACHMENTS_DOWNLOAD, forumId);
		
		if (!attachmentsEnabled && !attachmentsDownload) {
			this.setTemplateName(TemplateKeys.POSTS_CANNOT_DOWNLOAD);
			this.context.put("message", I18n.getMessage("Attachments.featureDisabled"));
			return;
		}
		
		String filename = SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_STORE_DIR)
			+ "/"
			+ a.getInfo().getPhysicalFilename();

		if (!new File(filename).exists()) {
			this.setTemplateName(TemplateKeys.POSTS_ATTACH_NOTFOUND);
			this.context.put("message", I18n.getMessage("Attachments.notFound"));
			return;
		}
		
		FileInputStream fis = null;
		OutputStream os = null;

		try {
			a.getInfo().setDownloadCount(a.getInfo().getDownloadCount() + 1);
			am.updateAttachment(a);

			fis = new FileInputStream(filename);
			os = response.getOutputStream();

			if (am.isPhysicalDownloadMode(a.getInfo().getExtension().getExtensionGroupId())) {
				this.response.setContentType("application/octet-stream");
			}
			else {
				this.response.setContentType(a.getInfo().getMimetype());
			}

			if (this.request.getHeader("User-Agent").indexOf("Firefox") != -1) {
				this.response.setHeader("Content-Disposition", "attachment; filename=\""
					+ new String(a.getInfo().getRealFilename().getBytes(SystemGlobals.getValue(ConfigKeys.ENCODING)),
						SystemGlobals.getValue(ConfigKeys.DEFAULT_CONTAINER_ENCODING)) + "\";");
			}
			else {
				this.response.setHeader("Content-Disposition", "attachment; filename=\""
					+ ViewCommon.toUtf8String(a.getInfo().getRealFilename()) + "\";");
			}

			this.response.setContentLength((int)a.getInfo().getFilesize());

			int c;
			byte[] b = new byte[4096];
			while ((c = fis.read(b)) != -1) {
				os.write(b, 0, c);
			}

			
			JForumExecutionContext.enableCustomContent(true);
		}
		catch (IOException e) {
			throw new ForumException(e);
		}
		finally {
			if (fis != null) {
				try { fis.close(); }
				catch (Exception e) {}
			}
			
			if (os != null) {
				try { os.close(); }
				catch (Exception e) {}
			}
		}
	}
	
	private void cannotEdit()
	{
		this.setTemplateName(TemplateKeys.POSTS_EDIT_CANNOTEDIT);
		this.context.put("message", I18n.getMessage("CannotEditPost"));
	}
	
	private void topicLocked() 
	{
		this.setTemplateName(TemplateKeys.POSTS_TOPIC_LOCKED);
		this.context.put("message", I18n.getMessage("PostShow.topicLocked"));
	}
	
	public void listSmilies()
	{
		this.setTemplateName(SystemGlobals.getValue(ConfigKeys.TEMPLATE_DIR) + "/empty.htm");
		this.setTemplateName(TemplateKeys.POSTS_LIST_SMILIES);
		this.context.put("smilies", SmiliesRepository.getSmilies());
	}

	private boolean isForumReadonly(int forumId, boolean isReply) {
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_READ_ONLY_FORUMS, Integer.toString(forumId))) {
			if (isReply) {
				this.list();
			}
			else {
				JForumExecutionContext.setRedirect(this.request.getContextPath() + "/forums/show/" + forumId
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
			}

			return true;
		}

		return false;
	}
	
	private boolean anonymousPost(int forumId)  
	{
		// Check if anonymous posts are allowed
		if (!SessionFacade.isLogged()
				&& !SecurityRepository.canAccess(SecurityConstants.PERM_ANONYMOUS_POST, Integer.toString(forumId))) {
			this.setTemplateName(ViewCommon.contextToLogin());

			return false;
		}

		return true;
	}
}
