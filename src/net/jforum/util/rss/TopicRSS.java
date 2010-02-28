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
 * Created on 21/10/2004 00:10:00
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
 * @author Rafael Steil
 * @version $Id: TopicRSS.java,v 1.18 2007/08/20 19:35:52 rafaelsteil Exp $
 */
public class TopicRSS extends GenericRSS 
{
	protected List posts;
	protected RSS rss;
	protected String forumLink;
	
	TopicRSS() {}
	
	public TopicRSS(String title, String description, int forumId, List posts)
	{
		this.posts = posts;
		this.forumLink = ViewCommon.getForumLink();
		
		this.rss = new RSS(title, description, 
			SystemGlobals.getValue(ConfigKeys.ENCODING), 
			this.forumLink + "forums/show/" + forumId 
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		
		this.prepareRSS();
	}
	
	protected void prepareRSS()
	{
		for (Iterator iter = posts.iterator(); iter.hasNext(); ) {
			Post post = (Post)iter.next();
			
			post.setBbCodeEnabled(false);
			post.setSmiliesEnabled(false);
			
			RSSItem item = new RSSItem();
			
			item.setAuthor(post.getPostUsername());
			item.setPublishDate(RSSUtils.formatDate(post.getTime()));
			item.setLink(this.forumLink + "posts/preList/" + post.getTopicId() + "/" + post.getId()
				+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
			item.setTitle(post.getSubject());
			item.setContentType(RSSAware.CONTENT_HTML);
			item.setDescription(PostCommon.preparePostForDisplay(post).getText());
			
			this.rss.addItem(item);
		}
		
		super.setRSS(this.rss);
	}
}
