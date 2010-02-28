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
 * This file creation date: 04/03/2004 - 20:32:13
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.mail;

import java.text.MessageFormat;
import java.util.List;

import net.jforum.entities.Forum;
import net.jforum.entities.Post;
import net.jforum.entities.Topic;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.view.forum.common.PostCommon;
import net.jforum.view.forum.common.ViewCommon;
import freemarker.template.SimpleHash;

/**
 * @author Rafael Steil
 * @version $Id: ForumNewTopicSpammer.java,v 1.4 2006/10/09 00:54:09 rafaelsteil Exp $
 */
public class ForumNewTopicSpammer extends Spammer 
{
	public ForumNewTopicSpammer(Forum forum, Topic topic, Post post, List users)
	{
		String forumLink = ViewCommon.getForumLink();
		String path = this.postLink(topic, forumLink);
		String unwatch = this.unwatchLink(forum, forumLink);
		
		SimpleHash params = new SimpleHash();
		params.put("topic", topic);
		params.put("path", path);
		params.put("forumLink", forumLink);
		params.put("unwatch", unwatch);
		
		this.setUsers(users);
		
		if (post != null) {
			post = PostCommon.preparePostForDisplay(post);
			params.put("message", post.getText());
		}
		
		this.setTemplateParams(params);
		
		String subject = SystemGlobals.getValue(ConfigKeys.MAIL_NEW_TOPIC_SUBJECT);
		
		super.prepareMessage(
			MessageFormat.format(subject, new Object[] { topic.getTitle() }),
			SystemGlobals.getValue(ConfigKeys.MAIL_NEW_TOPIC_MESSAGE_FILE));
	}

	/**
	 * @param forum
	 * @param forumLink
	 * @return
	 */
	private String unwatchLink(Forum forum, String forumLink)
	{
		String unwatch = new StringBuffer(128)
			.append(forumLink)
			.append("forums/unwatchForum/")
			.append(forum.getId())
			.append(SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION))
			.toString();
		return unwatch;
	}

	/**
	 * @param topic
	 * @param forumLink
	 * @return
	 */
	private String postLink(Topic topic, String forumLink)
	{
		String path = new StringBuffer(128)
			.append(forumLink)
			.append("posts/list/")
			.append(topic.getId()) 
			.append(SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION))
			.append('#')
			.append(topic.getLastPostId())
			.toString();
		return path;
	}
}
