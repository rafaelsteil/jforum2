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
 * Created on 02/10/2005 20:04:15
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import java.util.Iterator;
import java.util.List;

import net.jforum.Command;
import net.jforum.JForumExecutionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ModerationLogDAO;
import net.jforum.dao.PostDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.entities.ModerationLog;
import net.jforum.entities.Post;
import net.jforum.entities.Topic;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.I18n;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.view.forum.common.PostCommon;
import net.jforum.view.forum.common.ViewCommon;

/**
 * @author Rafael Steil
 * @version $Id: ModerationAction.java,v 1.6 2007/07/28 14:17:09 rafaelsteil Exp $
 */
public class ModerationAction extends Command
{
	/**
	 * @throws UnsupportedOperationException always
	 * @see net.jforum.Command#list()
	 */
	public void list()
	{
		throw new UnsupportedOperationException();
	}
	
	public void showActivityLog() 
	{
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_LOG)) {
			this.denied();
			return;
		}
		
		ModerationLogDAO dao = DataAccessDriver.getInstance().newModerationLogDAO();
		
		int start = ViewCommon.getStartPage();
		int recordsPerPage = SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		
		List list = dao.selectAll(start, recordsPerPage);
		boolean canAccessFullModerationLog = SecurityRepository.canAccess(SecurityConstants.PERM_FULL_MODERATION_LOG);
		
		PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
		TopicDAO topicDao = DataAccessDriver.getInstance().newTopicDAO();
		
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ModerationLog log = (ModerationLog)iter.next();
			
			if (log.getPostId() > 0) {
				Post post = postDao.selectById(log.getPostId());
				
				if (post.getId() > 0 && ForumRepository.getForum(post.getForumId()) == null) {
					iter.remove();
					continue;
				}
			}
			else if (log.getTopicId() > 0) {
				Topic topic = topicDao.selectRaw(log.getTopicId());
				
				if (topic.getId() > 0 && ForumRepository.getForum(topic.getForumId()) == null) {
					iter.remove();
					continue;
				}
			}
			
			if (log.getOriginalMessage() != null && canAccessFullModerationLog) {
				Post post = new Post();
				post.setText(log.getOriginalMessage());
				
				log.setOriginalMessage(PostCommon.preparePostForDisplay(post).getText());
			}
		}
		
		this.setTemplateName(TemplateKeys.MODERATION_SHOW_ACTIVITY_LOG);
		this.context.put("activityLog", list);
		this.context.put("canAccessFullModerationLog", canAccessFullModerationLog);
		
		int totalRecords = dao.totalRecords();
		
		ViewCommon.contextToPagination(start, totalRecords, recordsPerPage);
	}
	
	private void denied() {
		this.setTemplateName(TemplateKeys.MODERATION_LOG_DENIED);
		this.context.put("message", I18n.getMessage("ModerationLog.denied"));
	}
	
	public void doModeration()
	{
		String returnUrl = this.request.getParameter("returnUrl");
		
		new ModerationHelper().doModeration(returnUrl);
		
		this.context.put("returnUrl", returnUrl);
		
		if (JForumExecutionContext.getRequest().getParameter("topicMove") != null) {
			this.setTemplateName(TemplateKeys.MODERATION_MOVE_TOPICS);
		}
	}
	
	public void moveTopic()
	{
		new ModerationHelper().moveTopicsSave(this.request.getParameter("returnUrl"));
	}
	
	public void moderationDone()
	{
		this.setTemplateName(new ModerationHelper().moderationDone(this.request.getParameter("returnUrl")));
	}
}
