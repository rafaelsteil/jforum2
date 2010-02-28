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
 * Created on 22/10/2004 00:51:36
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.rss;

import java.util.Iterator;
import java.util.List;

import net.jforum.entities.Post;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.view.forum.common.PostCommon;
import net.jforum.view.forum.common.ViewCommon;

/**
 * RSS for the messages of some topic
 * 
 * @author Rafael Steil
 * @version $Id: TopicPostsRSS.java,v 1.12 2007/08/20 20:21:58 rafaelsteil Exp $
 */
public class TopicPostsRSS extends GenericRSS 
{
	private List posts;
	protected RSS rss;
	protected String forumLink;
	
	public TopicPostsRSS(String title, String description, int topicId, List posts)
	{
		this.forumLink = ViewCommon.getForumLink();
		
		this.posts = posts;
		this.rss = new RSS(title, description, 
			SystemGlobals.getValue(ConfigKeys.ENCODING),
			this.forumLink + "posts/list/" + topicId 
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		this.prepareRSS();
	}
	
	private void prepareRSS()
	{
		for (Iterator iter = this.posts.iterator(); iter.hasNext(); ) {
			Post p = (Post)iter.next();
			
			p.setBbCodeEnabled(false);
			p.setHtmlEnabled(false);
			p.setHtmlEnabled(false);
			
			RSSItem item = new RSSItem();
			item.setAuthor(p.getPostUsername());
			item.setContentType(RSSAware.CONTENT_HTML);
			item.setDescription(PostCommon.preparePostForDisplay(p).getText());
			item.setPublishDate(RSSUtils.formatDate(p.getTime()));
			item.setTitle(p.getSubject());
			item.setLink(this.forumLink 
				+ "posts/preList/" + p.getTopicId()
				+ "/" + p.getId()
				+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));

			this.rss.addItem(item);
		}
		
		super.setRSS(this.rss);
	}
}
