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
 * Created on Feb 20, 2005 12:00:02 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.admin;

import java.util.Collection;

import net.jforum.SessionFacade;
import net.jforum.dao.DataAccessDriver;
import net.jforum.repository.BBCodeRepository;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.ModulesRepository;
import net.jforum.repository.PostRepository;
import net.jforum.repository.RankingRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.repository.SmiliesRepository;
import net.jforum.repository.TopicRepository;
import net.jforum.util.bbcode.BBCodeHandler;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;

/**
 * @author Rafael Steil
 * @version $Id: CacheAction.java,v 1.9 2006/08/20 22:47:45 rafaelsteil Exp $
 */
public class CacheAction extends AdminCommand
{
	/**
	 * @see net.jforum.Command#list()
	 */
	public void list()
	{
		this.setTemplateName(TemplateKeys.CACHE_LIST);
		
		this.context.put("bb", new BBCodeRepository());
		this.context.put("modules", new ModulesRepository());
		this.context.put("ranking", new RankingRepository());
		this.context.put("smilies", new SmiliesRepository());
		this.context.put("security", new SecurityRepository());
		this.context.put("forum", new ForumRepository());
		this.context.put("topic", new TopicRepository());
		this.context.put("session", new SessionFacade());
		this.context.put("posts", new PostRepository());
	}
	
	public void bbReload()
	{
		BBCodeRepository.setBBCollection(new BBCodeHandler().parse());
		this.list();
	}
	
	public void sessionClear()
	{
		SessionFacade.clear();
		this.list();
	}
	
	public void modulesReload()
	{
		ModulesRepository.init(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR));
		this.list();
	}
	
	public void smiliesReload()
	{
		SmiliesRepository.loadSmilies();
		this.list();
	}
	
	public void rankingReload()
	{
		RankingRepository.loadRanks();
		this.list();
	}
	
	public void topicsMoreInfo()
	{
		if (!SystemGlobals.getBoolValue(ConfigKeys.TOPIC_CACHE_ENABLED)) {
			this.list();
			return;
		}
		
		this.setTemplateName(TemplateKeys.CACHE_TOPICS_MOREINFO);
		
		this.context.put("categories", ForumRepository.getAllCategories());
	}
	
	public void topicsClear()
	{
		int forumId = this.request.getIntParameter("forum_id");
		TopicRepository.clearCache(forumId);
		this.topicsMoreInfo();
	}
	
	public void postsMoreInfo()
	{
		if (!SystemGlobals.getBoolValue(ConfigKeys.POSTS_CACHE_ENABLED)) {
			this.list();
			return;
		}
		
		Collection topics = PostRepository.cachedTopics();
		
		this.context.put("topics", DataAccessDriver.getInstance().newTopicDAO().selectTopicTitlesByIds(topics));
		this.context.put("repository", new PostRepository());
		this.setTemplateName(TemplateKeys.CACHE_POST_MOREINFO);
	}
	
	public void postsClear() 
	{
		int topicId = this.request.getIntParameter("topic_id");
		PostRepository.clearCache(topicId);
		this.postsMoreInfo();
	}
}
