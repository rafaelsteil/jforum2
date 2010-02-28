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
 * This file creation date: 10/03/2004 - 18:43:12
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.context.RequestContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ForumDAO;
import net.jforum.dao.ModerationLogDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.entities.ModerationLog;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.PostRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.repository.TopicRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.I18n;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.view.forum.common.ForumCommon;

import org.apache.log4j.Logger;

import freemarker.template.SimpleHash;

/**
 * @author Rafael Steil
 * @version $Id: ModerationHelper.java,v 1.43 2007/09/10 14:56:38 rafaelsteil Exp $
 */
public class ModerationHelper 
{
	private static Logger logger = Logger.getLogger(ModerationHelper.class);
	
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	public static final int IGNORE = 3;
	
	public int doModeration(String returnUrl)
	{
		int status = FAILURE;

		if (SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION)) {
			// Deleting topics
			RequestContext request = JForumExecutionContext.getRequest();
			
			if (request.getParameter("topicRemove") != null) {
				if (SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_POST_REMOVE)) {
					this.removeTopics();
					
					status = SUCCESS;
				}
			}
			else if (request.getParameter("topicMove") != null) {
				if (SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_TOPIC_MOVE)) {
					this.moveTopics();
					
					status = IGNORE;
				}
			}
			else if (request.getParameter("topicLock") != null) {
				if (SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_TOPIC_LOCK_UNLOCK)) {
					this.lockUnlockTopics(Topic.STATUS_LOCKED);
					
					status = SUCCESS;
				}
			}
			else if (request.getParameter("topicUnlock") != null) {
				if (SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_TOPIC_LOCK_UNLOCK)) {
					this.lockUnlockTopics(Topic.STATUS_UNLOCKED);
					
					status = SUCCESS;
				}
			}
		}

		if (status == ModerationHelper.FAILURE) {
			this.denied();
		}
		else if (status == ModerationHelper.SUCCESS && returnUrl != null) {
			JForumExecutionContext.setRedirect(returnUrl);
		}
		
		return status;
	}
	
	public void saveModerationLog(ModerationLog log)
	{
		ModerationLogDAO dao = DataAccessDriver.getInstance().newModerationLogDAO();
		dao.add(log);
	}
	
	public ModerationLog buildModerationLogFromRequest()
	{
		RequestContext request = JForumExecutionContext.getRequest();
		
		ModerationLog log = new ModerationLog();
		
		User user = new User();
		user.setId(SessionFacade.getUserSession().getUserId());
		log.setUser(user);
		
		log.setDescription(request.getParameter("log_description"));
		log.setOriginalMessage(request.getParameter("log_original_message"));
		log.setType(request.getIntParameter("log_type"));
		
		if (request.getParameter("post_id") != null) {
			log.setPostId(request.getIntParameter("post_id"));
		}
		
		String[] values = request.getParameterValues("topic_id");
		
		if (values != null && values.length == 1) {
			log.setTopicId(request.getIntParameter("topic_id"));
		}
		
		return log;
	}
	
	public int doModeration() 
	{
		return this.doModeration(null);
	}
	
	private void removeTopics()
	{
		String[] topics = JForumExecutionContext.getRequest().getParameterValues("topic_id");
		
		List forumsList = new ArrayList();
		TopicDAO tm = DataAccessDriver.getInstance().newTopicDAO();
		
		List topicsToDelete = new ArrayList();
		
		if (topics != null && topics.length > 0) {
			ModerationLog log = this.buildModerationLogFromRequest();
			
			for (int i = 0; i < topics.length; i++) {
				Topic t = tm.selectRaw(Integer.parseInt(topics[i]));
				
				log.setTopicId(t.getId());
				log.setPosterUser(t.getPostedBy());
				
				this.saveModerationLog(log);
				
				if (!forumsList.contains(new Integer(t.getForumId()))) {
					forumsList.add(new Integer(t.getForumId()));
				}
				
				topicsToDelete.add(t);
				PostRepository.clearCache(t.getId());
			}
			
			tm.deleteTopics(topicsToDelete, false);
			
			ForumDAO fm = DataAccessDriver.getInstance().newForumDAO();
			TopicRepository.loadMostRecentTopics();
			
			// Reload changed forums
			for (Iterator iter = forumsList.iterator(); iter.hasNext(); ) {
				int forumId = ((Integer)iter.next()).intValue();

				TopicRepository.clearCache(forumId);
				
				int postId = fm.getMaxPostId(forumId);
				
				if (postId > -1) {
					fm.setLastPost(forumId, postId);
				}
				else {
					logger.warn("Could not find last post id for forum " + forumId);
				}
				
				ForumRepository.reloadForum(forumId);
			}
		}
	}
	
	private void lockUnlockTopics(int status)
	{
		String[] topics = JForumExecutionContext.getRequest().getParameterValues("topic_id");
		
		if (topics != null && topics.length > 0) {
			int[] ids = new int[topics.length];
			
			ModerationLog log = this.buildModerationLogFromRequest();

			for (int i = 0; i < topics.length; i++) {
				ids[i] = Integer.parseInt(topics[i]);
				log.setTopicId(ids[i]);
				this.saveModerationLog(log);
			}
			
			DataAccessDriver.getInstance().newTopicDAO().lockUnlock(ids, status);
			
			// Clear the cache
			Topic t = DataAccessDriver.getInstance().newTopicDAO().selectById(ids[0]);
			TopicRepository.clearCache(t.getForumId());
		}
	}
	
	private void moveTopics()
	{
		SimpleHash context = JForumExecutionContext.getTemplateContext();
		
		context.put("persistData", JForumExecutionContext.getRequest().getParameter("persistData"));
		context.put("allCategories", ForumCommon.getAllCategoriesAndForums(false));
		
		String[] topics = JForumExecutionContext.getRequest().getParameterValues("topic_id");
		
		if (topics.length > 0) {
			// If forum_id is null, get from the database
			String forumId = JForumExecutionContext.getRequest().getParameter("forum_id");
			
			if (forumId == null) {
				int topicId = Integer.parseInt(topics[0]);
				
				Topic topic = TopicRepository.getTopic(new Topic(topicId));
				
				if (topic == null) {
					topic = DataAccessDriver.getInstance().newTopicDAO().selectRaw(topicId);
				}
				
				forumId = Integer.toString(topic.getForumId());
			}
			
			context.put("forum_id", forumId);
			
			StringBuffer sb = new StringBuffer(128);
			
			for (int i = 0; i < topics.length - 1; i++) {
				sb.append(topics[i]).append(",");
			}
			
			sb.append(topics[topics.length - 1]);
			
			context.put("topics", sb.toString());
		}
	}
	
	public int moveTopicsSave(String successUrl)
	{
		int status = SUCCESS;
		
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_TOPIC_MOVE)) {
			status = FAILURE;
		}
		else {
			RequestContext request = JForumExecutionContext.getRequest();
			String topics = request.getParameter("topics");
			
			if (topics != null) {
				int fromForumId = Integer.parseInt(request.getParameter("forum_id"));
				int toForumId = Integer.parseInt(request.getParameter("to_forum"));
				
				String[] topicList = topics.split(",");
				
				DataAccessDriver.getInstance().newForumDAO().moveTopics(topicList, fromForumId, toForumId);
				
				ModerationLog log = this.buildModerationLogFromRequest();
				
				for (int i = 0; i < topicList.length; i++) {
					int topicId = Integer.parseInt(topicList[i]);
					log.setTopicId(topicId);
					this.saveModerationLog(log);
				}

				ForumRepository.reloadForum(fromForumId);
				ForumRepository.reloadForum(toForumId);
				
				TopicRepository.clearCache(fromForumId);
				TopicRepository.clearCache(toForumId);
				
				TopicRepository.loadMostRecentTopics();
				TopicRepository.loadHottestTopics();
			}
		}
		
		if (status == FAILURE) {
			this.denied();
		}
		else {
			this.moderationDone(successUrl);
		}
		
		return status;
	}
	
	public String moderationDone(String redirectUrl)
	{
		JForumExecutionContext.getRequest().setAttribute("template", TemplateKeys.MODERATION_DONE);
		JForumExecutionContext.getTemplateContext().put("message", I18n.getMessage("Moderation.ModerationDone", new String[] { redirectUrl }));
		
		return TemplateKeys.MODERATION_DONE;
	}
	
	public void denied()
	{
		this.denied(I18n.getMessage("Moderation.Denied"));
	}
	
	public void denied(String message)
	{
		JForumExecutionContext.getRequest().setAttribute("template", TemplateKeys.MODERATION_DENIED);
		JForumExecutionContext.getTemplateContext().put("message", message);		
	}
}
