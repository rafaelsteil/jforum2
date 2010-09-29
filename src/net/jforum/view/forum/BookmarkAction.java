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
 * Created on Jan 16, 2005 4:48:39 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import net.jforum.*;
import net.jforum.context.RequestContext;
import net.jforum.context.ResponseContext;
import net.jforum.dao.BookmarkDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Bookmark;
import net.jforum.entities.BookmarkType;
import net.jforum.entities.Forum;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.I18n;
import net.jforum.util.SafeHtml;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;

/**
 * @author Rafael Steil
 * @version $Id: BookmarkAction.java,v 1.17 2006/08/23 02:13:53 rafaelsteil Exp $
 */
public class BookmarkAction extends Command
{
	public void insert()
	{
		int type = this.request.getIntParameter("relation_type");
		if (type == BookmarkType.FORUM) {
			this.addForum();
		}
		else if (type == BookmarkType.TOPIC) {
			this.addTopic();
		}
		else if (type == BookmarkType.USER) {
			this.addUser();
		}
		else {
			this.error("Bookmarks.invalidType");
		}
	}
	
	private void addForum()
	{
		Forum f = ForumRepository.getForum(this.request.getIntParameter("relation_id"));
		String title = f.getName();
		String description = f.getDescription();
		
		Bookmark b = DataAccessDriver.getInstance().newBookmarkDAO().selectForUpdate(
				f.getId(), BookmarkType.FORUM, SessionFacade.getUserSession().getUserId());
		if (b != null) {
			if (b.getTitle() != null) {
				title = b.getTitle();
			}
			
			if (b.getDescription() != null) {
				description = b.getDescription();
			}

			this.context.put("bookmark", b);
		}
		
		this.setTemplateName(TemplateKeys.BOOKMARKS_ADD_FORUM);
		this.context.put("title", title);
		this.context.put("description", description);
		this.context.put("relationType", new Integer(BookmarkType.FORUM));
		this.context.put("relationId", new Integer(f.getId()));
	}
	
	private void addTopic()
	{
		Topic t = DataAccessDriver.getInstance().newTopicDAO().selectById(
				this.request.getIntParameter("relation_id"));
		String title = t.getTitle();
		
		Bookmark b = DataAccessDriver.getInstance().newBookmarkDAO().selectForUpdate(
				t.getId(), BookmarkType.TOPIC, SessionFacade.getUserSession().getUserId());
		if (b != null) {
			if (b.getTitle() != null) {
				title = b.getTitle();
			}
			
			this.context.put("description", b.getDescription());
			this.context.put("bookmark", b);
		}
		
		this.setTemplateName(TemplateKeys.BOOKMARS_ADD_TOPIC);
		this.context.put("title", title);
		this.context.put("relationType", new Integer(BookmarkType.TOPIC));
		this.context.put("relationId", new Integer(t.getId()));
	}
	
	private void addUser()
	{
		User u = DataAccessDriver.getInstance().newUserDAO().selectById(
				this.request.getIntParameter("relation_id"));
		String title = u.getUsername();
		
		Bookmark b = DataAccessDriver.getInstance().newBookmarkDAO().selectForUpdate(
				u.getId(), BookmarkType.USER, SessionFacade.getUserSession().getUserId());
		if (b != null) {
			if (b.getTitle() != null) {
				title = b.getTitle();
			}
			
			this.context.put("description", b.getDescription());
			this.context.put("bookmark", b);
		}
		
		this.setTemplateName(TemplateKeys.BOOKMARKS_ADD_USER);
		this.context.put("title", title);
		this.context.put("relationType", new Integer(BookmarkType.USER));
		this.context.put("relationId", new Integer(u.getId()));
	}
	
	public void insertSave()
	{
		SafeHtml safeHtml = new SafeHtml();
		
		Bookmark b = new Bookmark();
		b.setDescription(safeHtml.makeSafe(this.request.getParameter("description")));
		b.setTitle(safeHtml.makeSafe(this.request.getParameter("title")));
		
		String publicVisible = this.request.getParameter("visible");
		b.setPublicVisible(publicVisible != null && publicVisible.length() > 0);
		
		b.setRelationId(this.request.getIntParameter("relation_id"));
		b.setRelationType(this.request.getIntParameter("relation_type"));
		b.setUserId(SessionFacade.getUserSession().getUserId());
		
		DataAccessDriver.getInstance().newBookmarkDAO().add(b);
		this.setTemplateName(TemplateKeys.BOOKMARKS_INSERT_SAVE);
	}
	
	public void updateSave()
	{
		int id = this.request.getIntParameter("bookmark_id");
		BookmarkDAO bm = DataAccessDriver.getInstance().newBookmarkDAO();
		Bookmark b = bm.selectById(id);
		
		if (!this.sanityCheck(b)) {
			return;
		}
		
		b.setTitle(this.request.getParameter("title"));
		b.setDescription(this.request.getParameter("description"));
		
		String visible = this.request.getParameter("visible");
		b.setPublicVisible(StringUtils.isNotBlank(visible));
		
		bm.update(b);
		this.setTemplateName(TemplateKeys.BOOKMARKS_UPDATE_SAVE);
	}
	
	public void edit()
	{
		int id = this.request.getIntParameter("bookmark_id");
		BookmarkDAO bm = DataAccessDriver.getInstance().newBookmarkDAO();
		Bookmark b = bm.selectById(id);
		
		if (!this.sanityCheck(b)) {
			return;
		}
		
		this.setTemplateName(TemplateKeys.BOOKMARKS_EDIT);
		this.context.put("bookmark", b);
	}
	
	public void delete()
	{
		int id = this.request.getIntParameter("bookmark_id");
		BookmarkDAO bm = DataAccessDriver.getInstance().newBookmarkDAO();
		Bookmark b = bm.selectById(id);
		
		if (!this.sanityCheck(b)) {
			return;
		}
		
		bm.remove(id);
		
		JForumExecutionContext.setRedirect(this.request.getContextPath() + "/bookmarks/list/" + b.getUserId()
				+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}
	
	private boolean sanityCheck(Bookmark b)
	{
		if (b == null) {
			this.error("Bookmarks.notFound");
			return false;
		}
		
		if (b.getUserId() != SessionFacade.getUserSession().getUserId()) {
			this.error("Bookmarks.notOwner");
			return false;
		}
		
		return true;
	}
	
	private void error(String message)
	{
		this.setTemplateName(TemplateKeys.BOOKMARKS_ERROR);
		this.context.put("message", I18n.getMessage(message));
	}
	
	public void disabled()
	{
		this.error("Bookmarks.featureDisabled");
	}
	
	public void anonymousIsDenied()
	{
		this.error("Bookmarks.anonymousIsDenied");
	}

	/**
	 * @see net.jforum.Command#list()
	 */
	public void list()
	{
		int userId = this.request.getIntParameter("user_id");
		
		this.setTemplateName(TemplateKeys.BOOKMARKS_LIST);
		this.context.put("bookmarks", DataAccessDriver.getInstance().newBookmarkDAO().selectByUser(userId));
		this.context.put("forumType", new Integer(BookmarkType.FORUM));
		this.context.put("userType", new Integer(BookmarkType.USER));
		this.context.put("topicType", new Integer(BookmarkType.TOPIC));
		User u=DataAccessDriver.getInstance().newUserDAO().selectById(userId);
		this.context.put("user", u);
		this.context.put("loggedUserId", new Integer(SessionFacade.getUserSession().getUserId()));
		this.context.put("pageTitle", I18n.getMessage("Bookmarks.for")+" "+u.getUsername());				
	}
	
	/**
	 * @see net.jforum.Command#process(net.jforum.context.RequestContext, net.jforum.context.ResponseContext, freemarker.template.SimpleHash) 
	 */
	public Template process(RequestContext request, ResponseContext response, SimpleHash context)
	{
		if (SessionFacade.getUserSession().getUserId() == SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)
				&& !request.getAction().equals("list")) {
			request.addParameter("action", "anonymousIsDenied");
		}
		else if (!SecurityRepository.canAccess(SecurityConstants.PERM_BOOKMARKS_ENABLED)) {
			request.addParameter("action", "disabled");
		}

		return super.process(request, response, context);
	}
}
