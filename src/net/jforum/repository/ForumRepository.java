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
 * This file creation date: Apr 23, 2003 / 10:46:05 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import net.jforum.SessionFacade;
import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.dao.CategoryDAO;
import net.jforum.dao.ConfigDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ForumDAO;
import net.jforum.dao.UserDAO;
import net.jforum.entities.Category;
import net.jforum.entities.Config;
import net.jforum.entities.Forum;
import net.jforum.entities.LastPostInfo;
import net.jforum.entities.MostUsersEverOnline;
import net.jforum.entities.Post;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.exceptions.CategoryNotFoundException;
import net.jforum.exceptions.DatabaseException;
import net.jforum.security.PermissionControl;
import net.jforum.security.SecurityConstants;
import net.jforum.util.CategoryOrderComparator;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * Repository for the forums of the System.
 * This repository acts like a cache system, to avoid repetitive and unnecessary SQL queries
 * every time we need some info about the forums. 
 * To start the repository, call the method <code>start(ForumModel, CategoryModel)</code>
 * 
 * @author Rafael Steil
 * @version  $Id: ForumRepository.java,v 1.60 2007/09/10 23:06:59 rafaelsteil Exp $
 */
public class ForumRepository implements Cacheable
{
	private static CacheEngine cache;
	private static ForumRepository instance;
	private static Logger logger = Logger.getLogger(ForumRepository.class);
	
	private static final String FQN = "forumRepository";
	private static final String CATEGORIES_SET = "categoriesSet";
	private static final String RELATION = "relationForums";
	private static final String FQN_MODERATORS = FQN + "/moderators";
	private static final String TOTAL_MESSAGES = "totalMessages";
	private static final String MOST_USERS_ONLINE = "mostUsersEverOnline";
	private static final String LOADED = "loaded";
	private static final String LAST_USER = "lastUser";
	private static final String TOTAL_USERS = "totalUsers";
	
	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}
	
	/**
	 * Starts the repository.
	 * 
	 * @param fm The <code>ForumModel</code> instance which will be
	 * used to retrieve information about the forums.
	 * @param cm The <code>CategoryModel</code> instance which will
	 * be used to retrieve information about the categories.
     * @param  configModel ConfigDAO
	 */
	public synchronized static void start(ForumDAO fm, CategoryDAO cm, ConfigDAO configModel)
	{
		instance = new ForumRepository();
		
		if (cache.get(FQN, LOADED) == null) {
			instance.loadCategories(cm);
			instance.loadForums(fm);
			instance.loadMostUsersEverOnline(configModel);
			instance.loadUsersInfo();
			
			Integer i = (Integer)cache.get(FQN, TOTAL_MESSAGES);
			
			if (i == null) {
				cache.add(FQN, TOTAL_MESSAGES, new Integer(0));
			}
			
			cache.add(FQN, LOADED, "1");
		}
	}
	
	/**
	 * Gets a category by its id.
	 * A call to @link #getCategory(int, int) is made, using the
	 * return of <code>SessionFacade.getUserSession().getUserId()</code>
	 * as argument for the "userId" parameter.
	 * 
	 * @param categoryId The id of the category to check
	 * @return <code>null</code> if the category is either not
	 * found or access is denied.
	 * @see #getCategory(int, int)
	 */
	public static Category getCategory(int categoryId)
	{
		return getCategory(SessionFacade.getUserSession().getUserId(), categoryId);
	}

	/**
	 * Gets a category by its id.
	 *  
	 * @param userId The user id who is requesting the category
	 * @param categoryId The id of the category to get
	 * @return <code>null</code> if the category is either not
	 * found or access is denied.
	 * @see #getCategory(int)
	 */
	public static Category getCategory(int userId, int categoryId)
	{
		if (!isCategoryAccessible(userId, categoryId)) {
			return null;
		}
		
		return (Category)cache.get(FQN, Integer.toString(categoryId));
	}
	
	public static Category getCategory(PermissionControl pc, int categoryId)
	{
		if (!isCategoryAccessible(pc, categoryId)) {
			return null;
		}
		
		return (Category)cache.get(FQN, Integer.toString(categoryId)); 
	}
	
	public static Category retrieveCategory(int categoryId)
	{
		return (Category)cache.get(FQN, Integer.toString(categoryId));
	}
	
	/**
	 * Check is some category is accessible.
	 * 
	 * @param userId The user's id who is trying to get the category
	 * @param categoryId The category's id to check for access rights
	 * @return <code>true</code> if access to the category is allowed.
	 */
	public static boolean isCategoryAccessible(int userId, int categoryId)
	{
		return isCategoryAccessible(SecurityRepository.get(userId), categoryId);
	}
	
	/**
	 * Check if some category is accessible.
	 * 
	 * @param categoryId The category id to check for access rights
	 * @return <code>true</code> if access to the category is allowed.
	 */
	public static boolean isCategoryAccessible(int categoryId)
	{
		return isCategoryAccessible(SessionFacade.getUserSession().getUserId(), categoryId);
	}
	
	/**
	 * Check is some category is accessible.
	 * 
	 * @param pc The <code>PermissionControl</code> instance containing
	 * all security info related to the user.
	 * @param categoryId the category's id to check for access rights
	 * @return <code>true</code> if access to the category is allowed.
	 */
	public static boolean isCategoryAccessible(PermissionControl pc, int categoryId)
	{
		return pc.canAccess(SecurityConstants.PERM_CATEGORY, Integer.toString(categoryId));
	}
	
	/**
	 * Gets all categories from the cache. 
	 *
     * @param userId int
	 * @return <code>List</code> with the categories. Each entry is a <code>Category</code> object.
	 */
	public static List getAllCategories(int userId)
	{
		PermissionControl pc = SecurityRepository.get(userId);
		List l = new ArrayList();
		
		Set categoriesSet = (Set)cache.get(FQN, CATEGORIES_SET);
		
		if (categoriesSet == null) {
			synchronized (ForumRepository.instance) {
				if (categoriesSet == null) {
					logger.warn("Categories set returned null from the cache. Trying to reload");
					
					try {
						ForumRepository.instance.loadCategories(DataAccessDriver.getInstance().newCategoryDAO());
						ForumRepository.instance.loadForums(DataAccessDriver.getInstance().newForumDAO());
					}
					catch (Exception e) {
						throw new CategoryNotFoundException("Failed to get the category", e);
					}
					
					categoriesSet = (Set)cache.get(FQN, CATEGORIES_SET);
					
					if (categoriesSet == null) {
						throw new CategoryNotFoundException("Could not find all categories. There must be a problem with the cache");
					}
				}
			}
		}
		
		for (Iterator iter = categoriesSet.iterator(); iter.hasNext(); ) {
			Category c = getCategory(pc, ((Category)iter.next()).getId());
			
			if (c != null) {
				l.add(c);
			}
		}
		
		return l;
	}

	/**
	 * Get all categories.
	 * A call to @link #getAllCategories(int) is made, passing
	 * the return of <code>SessionFacade.getUserSession().getUserId()</code> 
	 * as the value for the "userId" argument.
	 * 
	 * @return <code>List</code> with the categories. Each entry is a <code>Category</code> object.
	 * @see #getAllCategories(int)
	 */
	public static List getAllCategories()
	{
		return getAllCategories(SessionFacade.getUserSession().getUserId());
	}
	
	private static Category findCategoryByOrder(int order)
	{
		for (Iterator iter = ((Set)cache.get(FQN, CATEGORIES_SET)).iterator(); iter.hasNext(); ) {
			Category c = (Category)iter.next();
			if (c.getOrder() == order) {
				return c;
			}
		}
		
		return null;
	}

	/**
	 * Updates some category.
	 * This method only updated the "name" and "order" fields. 
	 *  
	 * @param c The category to update. The method will search for a category
	 * with the same id and update its data.
	 */
	public synchronized static void reloadCategory(Category c)
	{
		Category current = (Category)cache.get(FQN, Integer.toString(c.getId()));
		Category currentAtOrder = findCategoryByOrder(c.getOrder());
		
		Set tmpSet = new TreeSet(new CategoryOrderComparator());
		tmpSet.addAll((Set)cache.get(FQN, CATEGORIES_SET));
		
		if (currentAtOrder != null) {
			tmpSet.remove(currentAtOrder);
			cache.remove(FQN, Integer.toString(currentAtOrder.getId()));
		}
		
		tmpSet.add(c);
		cache.add(FQN, Integer.toString(c.getId()), c);
		
		if (currentAtOrder != null && c.getId() != currentAtOrder.getId()) {
			tmpSet.remove(current);
			currentAtOrder.setOrder(current.getOrder());
			tmpSet.add(currentAtOrder);
			
			cache.add(FQN, Integer.toString(currentAtOrder.getId()), currentAtOrder);
		}
		
		cache.add(FQN, CATEGORIES_SET, tmpSet);
	}
	
	/**
	 * Refreshes a category entry in the cache.
	 * 
	 * @param c The category to refresh
	 */
	public synchronized static void refreshCategory(Category c)
	{
		cache.add(FQN, Integer.toString(c.getId()), c);
		Set s = (Set)cache.get(FQN, CATEGORIES_SET);
		s.remove(c);
		s.add(c);
		cache.add(FQN, CATEGORIES_SET, s);
	}
	
	public synchronized static void refreshForum(Forum forum)
	{
		Category c = retrieveCategory(forum.getCategoryId());
		c.addForum(forum);
		refreshCategory(c);
	}
	
	/**
	 * Remove a category from the cache
	 * @param c The category to remove. The instance should have the 
	 * category id at least
	 */
	public synchronized static void removeCategory(Category c)
	{
		cache.remove(FQN, Integer.toString(c.getId()));
		
		Set s = (Set)cache.get(FQN, CATEGORIES_SET);
		s.remove(c);
		cache.add(FQN, CATEGORIES_SET, s);
		
		Map m = (Map)cache.get(FQN, RELATION);
		for (Iterator iter = m.values().iterator(); iter.hasNext(); ) {
			if (Integer.parseInt((String)iter.next()) == c.getId()) {
				iter.remove();
			}
		}
		
		cache.add(FQN, RELATION, m);
	}
	
	/**
	 * Adds a new category to the cache.
	 * @param c The category instance to insert in the cache.
	 */
	public synchronized static void addCategory(Category c)
	{
		String categoryId = Integer.toString(c.getId());
		cache.add(FQN, categoryId, c);
		
		Set s = (Set)cache.get(FQN, CATEGORIES_SET);
		
		if (s == null) {
			s = new TreeSet(new CategoryOrderComparator());
		}
		
		s.add(c);
		cache.add(FQN, CATEGORIES_SET, s);
		
		Map relation = (Map)cache.get(FQN, RELATION);
		if (relation == null) {
			relation = new HashMap();
		}
		
		for (Iterator iter = c.getForums().iterator(); iter.hasNext(); ) {
			Forum f = (Forum)iter.next();
			relation.put(Integer.toString(f.getId()), categoryId);
		}
		
		cache.add(FQN, RELATION, relation);
	}
	
	/**
	 * Gets a specific forum from the cache.	 
	 * 
	 * @param forumId The forum's ID to get
	 * @return <code>net.jforum.Forum</code> object instance or <code>null</code>
	 * if the forum was not found or is not accessible to the user.
	 */
	public static Forum getForum(int forumId)
	{
		String categoryId = (String)((Map)cache.get(FQN, RELATION)).get(Integer.toString(forumId));
		
		if (categoryId != null) {
			Category category = (Category)cache.get(FQN, categoryId);
			
			if (isCategoryAccessible(category.getId())) {
				return category.getForum(forumId);
			}
		}
		
		return null;
	}
	
	public static boolean isForumAccessible(int forumId)
	{
		return isForumAccessible(SessionFacade.getUserSession().getUserId(), forumId);
	}
	
	public static boolean isForumAccessible(int userId, int forumId)
	{
		int categoryId = Integer.parseInt((String)((Map)cache.get(FQN, RELATION)).get(Integer.toString(forumId)));
		return isForumAccessible(userId, categoryId, forumId);
	}
	
	public static boolean isForumAccessible(int userId, int categoryId, int forumId)
	{
		return ((Category)cache.get(FQN, Integer.toString(categoryId))).getForum(userId, forumId) != null;
	}
	
	/**
	 * Adds a new forum to the cache repository.	 
	 * 
	 * @param forum The forum to add
	 */
	public synchronized static void addForum(Forum forum)
	{
		String categoryId = Integer.toString(forum.getCategoryId());

		Category c = (Category)cache.get(FQN, categoryId);
		c.addForum(forum);
		cache.add(FQN, categoryId, c);
		
		Map m = (Map)cache.get(FQN, RELATION);
		m.put(Integer.toString(forum.getId()), categoryId);
		cache.add(FQN, RELATION, m);
		
		Set s = (Set)cache.get(FQN, CATEGORIES_SET);
		cache.add(FQN, CATEGORIES_SET, s);
	}
	
	/**
	 * Removes a forum from the cache.
	 * 
	 * @param forum The forum instance to remove.
	 */
	public synchronized static void removeForum(Forum forum)
	{
		String id = Integer.toString(forum.getId());
		Map m = (Map)cache.get(FQN, RELATION);
		m.remove(id);
		cache.add(FQN, RELATION, m);

		id = Integer.toString(forum.getCategoryId());
		
		Category c = (Category)cache.get(FQN, id);
		c.removeForum(forum.getId());
		cache.add(FQN, id, c);
		
		Set s = (Set)cache.get(FQN, CATEGORIES_SET);
		cache.add(FQN, CATEGORIES_SET, s);
	}
	
	/**
	 * Reloads a forum.
	 * The forum should already be in the cache and <b>SHOULD NOT</b>
	 * have its order changed. If the forum's order was changed, 
	 * then you <b>MUST CALL</b> @link Category#changeForumOrder(Forum) <b>BEFORE</b>
	 * calling this method.
	 * 
	 * @param forumId int The forum to reload its information
	 */
	public static synchronized void reloadForum(int forumId)
	{
		Forum f = DataAccessDriver.getInstance().newForumDAO().selectById(forumId);
		
		if (((Map)cache.get(FQN, RELATION)).containsKey(Integer.toString(forumId))) {
			String id = Integer.toString(f.getCategoryId());
			Category c = (Category)cache.get(FQN, id);
			
			f.setLastPostInfo(null);
			f.setLastPostInfo(ForumRepository.getLastPostInfo(f));
			c.reloadForum(f);
			
			cache.add(FQN, id, c);
			Set s = (Set)cache.get(FQN, CATEGORIES_SET);
			cache.add(FQN, CATEGORIES_SET, s);
		}
		
		getTotalMessages(true);
	}
	
	public static synchronized void updateForumStats(Topic t, User u, Post p)
	{
		String f = Integer.toString(t.getForumId());
		
		if (((Map)cache.get(FQN, RELATION)).containsKey(f)) {
			Forum forum = getForum(t.getForumId());

			SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		
			LastPostInfo lpi = forum.getLastPostInfo();
			
			if (lpi == null) {
				lpi = new LastPostInfo();
			}
			
			lpi.setPostId(p.getId());
			lpi.setPostDate(df.format(p.getTime()));
			lpi.setPostTimeMillis(p.getTime().getTime());
			lpi.setTopicId(t.getId());
			lpi.setTopicReplies(t.getTotalReplies());
			lpi.setUserId(u.getId());
			lpi.setUsername(u.getUsername());
			
			forum.setLastPostInfo(lpi);
			
			if (t.getTotalReplies() == 0) {
				forum.setTotalTopics(forum.getTotalTopics() + 1);
			}

			forum.setTotalPosts(forum.getTotalPosts() + 1);
			
			Category c = retrieveCategory(forum.getCategoryId());
			c.reloadForum(forum);
			
			refreshCategory(c);
		}
	}
	
	/**
	 * Gets information about the last message posted in some forum.
	 * @param forum The forum to retrieve information
	 * @return LastPostInfo
	 */
	public static LastPostInfo getLastPostInfo(Forum forum)
	{
		LastPostInfo lpi = forum.getLastPostInfo();
		
		if (lpi == null || !forum.getLastPostInfo().hasInfo()) {
			lpi = DataAccessDriver.getInstance().newForumDAO().getLastPostInfo(forum.getId());
			forum.setLastPostInfo(lpi);
		}
		
		return lpi;
	}
	
	/**
	 * Gets information about the last message posted in some forum.
	 * 
	 * @param forumId The forum's id to retrieve information
	 * @return LastPostInfo
	 */
	public static LastPostInfo getLastPostInfo(int forumId)
	{
		return getLastPostInfo(getForum(forumId));
	}

	/**
	 * Gets information about the moderators of some forum.
	 * @param forumId The forum to retrieve information
	 * @return List
	 */
	public static List getModeratorList(int forumId)
	{
		List l = (List)cache.get(FQN_MODERATORS, Integer.toString(forumId));
		
		if (l == null) {
			synchronized (FQN_MODERATORS) {
				try {
					l = DataAccessDriver.getInstance().newForumDAO().getModeratorList(forumId);
					cache.add(FQN_MODERATORS, Integer.toString(forumId), l);
				}
				catch (Exception e) {
					throw new DatabaseException(e);
				}
			}
		}
		
		return l;
	}
	
	public static void clearModeratorList()
	{
		cache.remove(FQN_MODERATORS);
	}
	
	public static User lastRegisteredUser()
	{
		return (User)cache.get(FQN, LAST_USER);
	}
	
	public static void setLastRegisteredUser(User user)
	{
		cache.add(FQN, LAST_USER, user);
	}
	
	public static Integer totalUsers()
	{
		return (Integer)cache.get(FQN, TOTAL_USERS);
	}
	
	public static void incrementTotalUsers()
	{
		Integer i = (Integer)cache.get(FQN, TOTAL_USERS);
		
		if (i == null) {
			i = new Integer(0);
		}
		
		cache.add(FQN,TOTAL_USERS, new Integer(i.intValue() + 1));
	}
	
	/**
	 * Gets the number of messages in the entire board.
	 * @return int
	 * @see #getTotalMessages(boolean)
	 */
	public static int getTotalMessages()
	{
		return getTotalMessages(false);
	}

	/**
	 * Gets the number of messags in the entire board.
	 * 
	 * @param fromDb If <code>true</code>, a query to the database will
	 * be made, to retrieve the desired information. If <code>false</code>, the
	 * data will be fetched from the cache.
	 * @return The number of messages posted in the board.
	 * @see #getTotalMessages()
	 */
	public static int getTotalMessages(boolean fromDb) 
	{
		Integer i = (Integer)cache.get(FQN, TOTAL_MESSAGES);
		
		int total = i != null ? i.intValue() : 0;
		
		if (fromDb || total == 0) {
			total = DataAccessDriver.getInstance().newForumDAO().getTotalMessages();
			cache.add(FQN, TOTAL_MESSAGES, new Integer(total));
		}
		
		return total;
	}
	
	public static synchronized void incrementTotalMessages()
	{
		int total = ((Integer)cache.get(FQN, TOTAL_MESSAGES)).intValue();
		cache.add(FQN, TOTAL_MESSAGES, new Integer(total + 1));
	}
	
	/**
	 * Gets the number of most online users ever
	 * @return MostUsersEverOnline
	 */
	public static MostUsersEverOnline getMostUsersEverOnline()
	{
		MostUsersEverOnline online = (MostUsersEverOnline)cache.get(FQN, MOST_USERS_ONLINE);
		
		if (online == null) {
			synchronized (MOST_USERS_ONLINE) {
				online = (MostUsersEverOnline)cache.get(FQN, MOST_USERS_ONLINE);
				
				if (online == null) {
					online = instance.loadMostUsersEverOnline(
						DataAccessDriver.getInstance().newConfigDAO());
				}
			}
		}
		
		return online;
	}
	
	/**
	 * Update the value of most online users ever.
	 * 
	 * @param m MostUsersEverOnline The new value to store. Generally it
	 * will be a bigger one.
	 */
	public static void updateMostUsersEverOnline(MostUsersEverOnline m)
	{
		ConfigDAO cm = DataAccessDriver.getInstance().newConfigDAO();
		Config config = cm.selectByName(ConfigKeys.MOST_USERS_EVER_ONLINE);
		
		if (config == null) {
			// Total
			config = new Config();
			config.setName(ConfigKeys.MOST_USERS_EVER_ONLINE);
			config.setValue(Integer.toString(m.getTotal()));
			
			cm.insert(config);
			
			// Date
			config.setName(ConfigKeys.MOST_USER_EVER_ONLINE_DATE);
			config.setValue(Long.toString(m.getTimeInMillis()));
			
			cm.insert(config);
		}
		else {
			// Total
			config.setValue(Integer.toString(m.getTotal()));
			cm.update(config);

			// Date
			config.setName(ConfigKeys.MOST_USER_EVER_ONLINE_DATE);
			config.setValue(Long.toString(m.getTimeInMillis()));
			cm.update(config);
		}
		
		cache.add(FQN, MOST_USERS_ONLINE, m);
	}
	
	/**
	 * Loads all forums.
     * @param fm ForumDAO
     */
	private void loadForums(ForumDAO fm)
	{
		List l = fm.selectAll();
		
		Map m = (Map)cache.get(FQN, RELATION);
		if (m == null) {
			m = new HashMap();
		}
		
		int lastId = 0;
		Category c = null;
		String catId = null;

		for (Iterator iter = l.iterator(); iter.hasNext(); ) {
			Forum f = (Forum)iter.next();
			
			if (f.getCategoryId() != lastId) {
				if (c != null) {
					cache.add(FQN, catId, c);
				}
				
				lastId = f.getCategoryId();
				catId = Integer.toString(f.getCategoryId());
				c = (Category)cache.get(FQN, catId);
			}
			
			if (c == null) {
				throw new CategoryNotFoundException("Category for forum #" + f.getId() + " not found");
			}
			
			String forumId = Integer.toString(f.getId());
			c.addForum(f);
			m.put(forumId, catId);
		}
		
		if (c != null) {
			cache.add(FQN, catId, c);
		}
		
		cache.add(FQN, RELATION, m);
	}
	
	private void loadUsersInfo()
	{
		UserDAO udao = DataAccessDriver.getInstance().newUserDAO();
		cache.add(FQN, LAST_USER, udao.getLastUserInfo());
		cache.add(FQN, TOTAL_USERS, new Integer(udao.getTotalUsers()));
	}

	/**
	 * Loads all categories.
     * @param cm CategoryDAO
     */
	private void loadCategories(CategoryDAO cm)
	{
		List categories = cm.selectAll();
		Set categoriesSet = new TreeSet(new CategoryOrderComparator());
		
		for (Iterator iter = categories.iterator(); iter.hasNext(); ) {
			Category c = (Category)iter.next();
			
			cache.add(FQN, Integer.toString(c.getId()), c);
			categoriesSet.add(c);
		}
		
		cache.add(FQN, CATEGORIES_SET, categoriesSet);
	}
	
	private MostUsersEverOnline loadMostUsersEverOnline(ConfigDAO cm) 
	{
		Config config = cm.selectByName(ConfigKeys.MOST_USERS_EVER_ONLINE);
		MostUsersEverOnline mostUsersEverOnline = new MostUsersEverOnline();
		
		if (config != null) {
			mostUsersEverOnline.setTotal(Integer.parseInt(config.getValue()));
			
			// We're assuming that, if we have one key, the another one
			// will always exist
			config = cm.selectByName(ConfigKeys.MOST_USER_EVER_ONLINE_DATE);
			mostUsersEverOnline.setTimeInMillis(Long.parseLong(config.getValue()));
		}
		
		cache.add(FQN, MOST_USERS_ONLINE, mostUsersEverOnline);
		
		return mostUsersEverOnline;
	}


	public static String getListAllowedForums() 
	{
		int n = 0;
		StringBuffer buf = new StringBuffer();
		
		List allCategories = ForumRepository.getAllCategories();
		
		for (Iterator iter = allCategories.iterator(); iter.hasNext(); ) {
			Collection forums = ((Category)iter.next()).getForums();
			
			for (Iterator tmpIterator = forums.iterator(); tmpIterator.hasNext(); ) {
				Forum f = (Forum)tmpIterator.next();
				
				if (ForumRepository.isForumAccessible(f.getId())) {
					if(n++ > 0) {
						buf.append(',');
					}
					
					buf.append(f.getId());
				}
			}
		}
		
		if (n <= 0) {
			return "-1";
		}
		
		return buf.toString();
	}
}
