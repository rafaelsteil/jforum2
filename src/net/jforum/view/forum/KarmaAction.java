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
 * Created on Jan 11, 2005 11:44:06 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import net.jforum.Command;
import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.KarmaDAO;
import net.jforum.dao.PostDAO;
import net.jforum.entities.Karma;
import net.jforum.entities.KarmaStatus;
import net.jforum.entities.Post;
import net.jforum.repository.PostRepository;
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
 * @version $Id: KarmaAction.java,v 1.21 2007/08/01 22:30:03 rafaelsteil Exp $
 */
public class KarmaAction extends Command
{
	public void insert()
	{
		if (!SecurityRepository.canAccess(SecurityConstants.PERM_KARMA_ENABLED)) {
			this.error("Karma.featureDisabled", null);
			return;
		}

		int postId = this.request.getIntParameter("post_id");
		int fromUserId = SessionFacade.getUserSession().getUserId();

		PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
		Post p = pm.selectById(postId);

		if (fromUserId == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
			this.error("Karma.anonymousIsDenied", p);
			return;
		}

		if (p.getUserId() == fromUserId) {
			this.error("Karma.cannotSelfVote", p);
			return;
		}

		KarmaDAO km = DataAccessDriver.getInstance().newKarmaDAO();
		
		if (!km.userCanAddKarma(fromUserId, postId)) {
			this.error("Karma.alreadyVoted", p);
			return;
		}
		
		// Check range
		int points = this.request.getIntParameter("points");
		
		if (points < SystemGlobals.getIntValue(ConfigKeys.KARMA_MIN_POINTS)
				|| points > SystemGlobals.getIntValue(ConfigKeys.KARMA_MAX_POINTS)) {
			this.error("Karma.invalidRange", p);
			return;
		}

		Karma karma = new Karma();
		karma.setFromUserId(fromUserId);
		karma.setPostUserId(p.getUserId());
		karma.setPostId(postId);
		karma.setTopicId(p.getTopicId());
		karma.setPoints(points);

		km.addKarma(karma);
		
		p.setKarma(new KarmaStatus(p.getId(), points));
		
		if (SystemGlobals.getBoolValue(ConfigKeys.POSTS_CACHE_ENABLED)) {
			PostRepository.update(p.getTopicId(), PostCommon.preparePostForDisplay(p));
		}

		JForumExecutionContext.setRedirect(this.urlToTopic(p));
	}

	private void error(String message, Post p)
	{
		this.setTemplateName(TemplateKeys.KARMA_ERROR);

		if (p != null) {
			this.context.put("message", I18n.getMessage(message, new String[] { this.urlToTopic(p) }));
		}
		else {
			this.context.put("message", I18n.getMessage(message));
		}
	}

	private String urlToTopic(Post p)
	{
		return JForumExecutionContext.getRequest().getContextPath() + "/posts/list/" 
			+ ViewCommon.getStartPage()
			+ "/" + p.getTopicId()
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION)
			+ "#" + p.getId();
	}

	/**
	 * @see net.jforum.Command#list()
	 */
	public void list() 
	{
		this.setTemplateName(TemplateKeys.KARMA_LIST);
		this.context.put("message", I18n.getMessage("invalidAction"));
	}
}
