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
 * Created on Oct 19, 2004
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.Command;
import net.jforum.JForumExecutionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.UserDAO;
import net.jforum.entities.Forum;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.TopicRepository;
import net.jforum.util.I18n;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.view.forum.common.TopicsCommon;
import net.jforum.view.forum.common.ViewCommon;

/**
 * Display a list of recent Topics
 * 
 * @author James Yong
 * @author Rafael Steil
 * @version $Id: RecentTopicsAction.java,v 1.20 2007/08/20 19:35:52 rafaelsteil Exp $
 */
public class RecentTopicsAction extends Command 
{
	private List forums;

	public void list()
	{
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);

		this.setTemplateName(TemplateKeys.RECENT_LIST);
		
		this.context.put("postsPerPage", new Integer(postsPerPage));
		this.context.put("topics", this.topics());
		this.context.put("forums", this.forums);
		this.context.put("pageTitle", I18n.getMessage("ForumBase.recentTopics"));

		TopicsCommon.topicListingBase();
		this.request.setAttribute("template", null);
	}
	
	private List topics()
	{
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
		List topics = TopicRepository.getRecentTopics();
		
		this.forums = new ArrayList(postsPerPage);

		for (Iterator iter = topics.iterator(); iter.hasNext(); ) {
			Topic t = (Topic)iter.next();
			
			if (TopicsCommon.isTopicAccessible(t.getForumId())) {
				Forum f = ForumRepository.getForum(t.getForumId());
				forums.add(f);
			}
			else {
				iter.remove();
			}
		}
		
		JForumExecutionContext.getRequest().removeAttribute("template");
		
		return TopicsCommon.prepareTopics(topics);
	}

	public void showTopicsByUser() 
	{
		DataAccessDriver da = DataAccessDriver.getInstance();
		
		UserDAO udao = da.newUserDAO();
		User u = udao.selectById(this.request.getIntParameter("user_id"));
		
		if (u.getId() == 0) {
			this.context.put("message", I18n.getMessage("User.notFound"));
			this.setTemplateName(TemplateKeys.USER_NOT_FOUND);
			return;
		} 
			
		TopicsCommon.topicListingBase();
		
		int start = ViewCommon.getStartPage();
		int topicsPerPage = SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
		int postsPerPage = SystemGlobals.getIntValue(ConfigKeys.POSTS_PER_PAGE);
		
		this.setTemplateName(TemplateKeys.RECENT_USER_TOPICS_SHOW);
		
		int totalTopics = da.newTopicDAO().countUserTopics(u.getId());
		
		this.context.put("u", u);
		this.context.put("pageTitle", I18n.getMessage("ForumListing.userTopics") + " " + u.getUsername());
		
		this.context.put("postsPerPage", new Integer(postsPerPage));
		
		List topics = da.newTopicDAO().selectByUserByLimit(u.getId(),start,topicsPerPage);
		
		List l = TopicsCommon.prepareTopics(topics);
		Map forums = new HashMap();
		
		for (Iterator iter = l.iterator(); iter.hasNext(); ) {
			Topic t = (Topic)iter.next();
			
			Forum f = ForumRepository.getForum(t.getForumId());
			
			if (f == null) {
				iter.remove();
				totalTopics--;
				continue;
			}
			
			forums.put(new Integer(t.getForumId()), f);
		}
		
		this.context.put("topics", l);
		this.context.put("forums", forums);
		
		ViewCommon.contextToPagination(start, totalTopics, topicsPerPage);
	}
}
