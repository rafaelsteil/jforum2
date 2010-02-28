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
 * Created on 12/11/2004 18:04:12
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.SessionFacade;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ForumDAO;
import net.jforum.entities.Category;
import net.jforum.entities.Forum;
import net.jforum.entities.LastPostInfo;
import net.jforum.entities.Post;
import net.jforum.entities.Topic;
import net.jforum.entities.UserSession;
import net.jforum.repository.ForumRepository;
import net.jforum.util.concurrent.Executor;
import net.jforum.util.mail.EmailSenderTask;
import net.jforum.util.mail.ForumNewTopicSpammer;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: ForumCommon.java,v 1.24 2007/08/01 22:30:05 rafaelsteil Exp $
 */
public class ForumCommon 
{
	private static Logger logger = Logger.getLogger(ForumCommon.class);
	/**
	 * Check if some forum has unread messages.
	 * @param forum The forum to search for unread messages 
	 * @param tracking Tracking of the topics read by the user
	 * @param lastVisit The last visit time of the current user
	 */
	public static void checkUnreadPosts(Forum forum, Map tracking, long lastVisit) 
	{
		LastPostInfo lpi = forum.getLastPostInfo();
		
		if (lpi == null) {
			return;
		}

		Integer topicId = new Integer(lpi.getTopicId());
		
		if (tracking != null && tracking.containsKey(topicId)) {
			long readTime = ((Long)tracking.get(topicId)).longValue();
		
			forum.setUnread(readTime > 0 && lpi.getPostTimeMillis() > readTime);
		}
		else {
			forum.setUnread(lpi.getPostTimeMillis() > lastVisit);
		}
	}
	
	/**
	 * Gets all forums available to the user.
	 * 
	 * @param us An <code>UserSession</code> instance with user information
	 * @param anonymousUserId The id which represents the anonymous user
	 * @param tracking <code>Map</code> instance with information 
	 * about the topics read by the user
	 * @param checkUnreadPosts <code>true</code> if is to search for unread topics inside the forums, 
	 * or <code>false</code> if this action is not needed. 
	 * @return A <code>List</code> instance where each record is an instance of a <code>Category</code>
	 * object
	 */
	public static List getAllCategoriesAndForums(UserSession us, int anonymousUserId, 
			Map tracking, boolean checkUnreadPosts)
	{
		long lastVisit = 0;
		int userId = anonymousUserId;
		
		if (us != null) {
			lastVisit = us.getLastVisit().getTime();
			userId = us.getUserId();
		}

        // Do not check for unread posts if the user is not logged in
		checkUnreadPosts = checkUnreadPosts && (userId != anonymousUserId);

		List categories = ForumRepository.getAllCategories(userId);
		
		if (!checkUnreadPosts) {
			return categories;
		}

		List returnCategories = new ArrayList();
		for (Iterator iter = categories.iterator(); iter.hasNext(); ) {
			Category c = new Category((Category)iter.next());
			
			for (Iterator tmpIterator = c.getForums().iterator(); tmpIterator.hasNext(); ) {
				Forum f = (Forum)tmpIterator.next();
				ForumCommon.checkUnreadPosts(f, tracking, lastVisit);
			}
			
			returnCategories.add(c);
		}
		
		return returnCategories;
	}
	
	/**
	 * @see #getAllCategoriesAndForums(UserSession, int, Map, boolean)
     * @return List
     * @param checkUnreadPosts boolean
	 */
	public static List getAllCategoriesAndForums(boolean checkUnreadPosts)
	{
		return getAllCategoriesAndForums(SessionFacade.getUserSession(), 
				SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID), 
				SessionFacade.getTopicsReadTime(), 
				checkUnreadPosts);
	}
	
	/**
	 * @see #getAllCategoriesAndForums(boolean)
     * @return List
	 */
	public static List getAllCategoriesAndForums()
	{
		UserSession us = SessionFacade.getUserSession();
		boolean checkUnread = (us != null && us.getUserId() 
			!= SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID));
		return getAllCategoriesAndForums(checkUnread);
	}
	
	/**
	 * Sends a "new topic" notification message to all users watching the forum.
	 * 
	 * @param f The Forum changed
	 * @param t The new topic
	 * @param post the newly created message
	 */
	public static void notifyUsers(Forum f, Topic t, Post post)
	{
		if (SystemGlobals.getBoolValue(ConfigKeys.MAIL_NOTIFY_ANSWERS)) {
			try {
				ForumDAO dao = DataAccessDriver.getInstance().newForumDAO();
				List usersToNotify = dao.notifyUsers(f);

				// we only have to send an email if there are users
				// subscribed to the topic
				if (usersToNotify != null && usersToNotify.size() > 0) {
					Executor.execute(
						new EmailSenderTask(
							new ForumNewTopicSpammer(f, t, post, usersToNotify)));
				}
			}
			catch (Exception e) {
				logger.warn("Error while sending notification emails: " + e);
			}
		}
	}
}
