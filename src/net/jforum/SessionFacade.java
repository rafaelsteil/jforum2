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
 * This file creation date: 12/03/2004 - 18:47:26
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.UserSession;
import net.jforum.repository.SecurityRepository;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: SessionFacade.java,v 1.40 2007/09/20 16:07:10 rafaelsteil Exp $
 */
public class SessionFacade implements Cacheable
{
	private static final Logger logger = Logger.getLogger(SessionFacade.class);
	
	private static final String FQN = "sessions";
	private static final String FQN_LOGGED = FQN + "/logged";
	private static final String FQN_COUNT = FQN + "/count";
	private static final String FQN_USER_ID = FQN + "/userId";
	private static final String ANONYMOUS_COUNT = "anonymousCount";
	private static final String LOGGED_COUNT = "loggedCount";
	
	private static CacheEngine cache;

	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}
	
	/**
	 * Add a new <code>UserSession</code> entry to the session.
	 * This method will make a call to <code>JForum.getRequest.getSession().getId()</code>
	 * to retrieve the session's id
	 * 
	 * @param us The user session objetc to add
	 * @see #add(UserSession, String)
	 */
	public static void add(UserSession us)
	{
		add(us, JForumExecutionContext.getRequest().getSessionContext().getId());
	}

	/**
	 * Registers a new {@link UserSession}.
	 * <p>
	 * If a call to {@link UserSession#getUserId()} return a value different 
	 * of <code>SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)</code>, then 
	 * the user will be registered as "logged". Otherwise it will enter as anonymous.
	 * </p>
	 * 
	 * <p>
	 * Please note that, in order to keep the number of guest and logged users correct, 
	 * it's caller's responsability to {@link #remove(String)} the record before adding it
	 * again if the current session is currently represented as "guest". 
	 * </p>
	 *  
	 * @param us the UserSession to add
	 * @param sessionId the user's session id
	 */
	public static void add(UserSession us, String sessionId)
	{
		if (us.getSessionId() == null || us.getSessionId().equals("")) {
			us.setSessionId(sessionId);
		}
		
		synchronized (FQN) {
			cache.add(FQN, us.getSessionId(), us);
			
			if (!JForumExecutionContext.getForumContext().isBot()) {
				if (us.getUserId() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					changeUserCount(LOGGED_COUNT, true);
					cache.add(FQN_LOGGED, us.getSessionId(), us);
					cache.add(FQN_USER_ID, Integer.toString(us.getUserId()), us.getSessionId());
				}
				else {
					// TODO: check the anonymous IP constraint
					changeUserCount(ANONYMOUS_COUNT, true);
				}
			}
		}
	}
	
	private static void changeUserCount(String cacheEntryName, boolean increment)
	{
		Integer count = (Integer)cache.get(FQN_COUNT, cacheEntryName);
		
		if (count == null) {
			count = new Integer(0);
		}
		
		if (increment) {
			count = new Integer(count.intValue() + 1);
		}
		else if (count.intValue() > 0) {
			count = new Integer(count.intValue() - 1);
		}
		
		cache.add(FQN_COUNT, cacheEntryName, count);
	}
	
	/**
	 * Add a new entry to the user's session
	 * 
	 * @param name The attribute name
	 * @param value The attribute value
	 */
	public static void setAttribute(String name, Object value)
	{
		JForumExecutionContext.getRequest().getSessionContext().setAttribute(name, value);
	}
	
	/**
	 * Removes an attribute from the session
	 * 
	 * @param name The key associated to the the attribute to remove
	 */
	public static void removeAttribute(String name)
	{
		JForumExecutionContext.getRequest().getSessionContext().removeAttribute(name);
	}
	
	/**
	 * Gets an attribute value given its name
	 * 
	 * @param name The attribute name to retrieve the value
	 * @return The value as an Object, or null if no entry was found
	 */
	public static Object getAttribute(String name)
	{
		return JForumExecutionContext.getRequest().getSessionContext().getAttribute(name);
	}

	/**
	 * Remove an entry fro the session map
	 * 
	 * @param sessionId The session id to remove
	 */
	public static void remove(String sessionId)
	{
		if (cache == null) {
			logger.warn("Got a null cache instance. #" + sessionId);
			return;
		}
		
		logger.debug("Removing session " + sessionId);
		
		synchronized (FQN) {
			UserSession us = getUserSession(sessionId);
			
			if (us != null) {
				cache.remove(FQN_LOGGED, sessionId);
				cache.remove(FQN_USER_ID, Integer.toString(us.getUserId()));
				
				if (us.getUserId() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					changeUserCount(LOGGED_COUNT, false);
				}
				else {
					changeUserCount(ANONYMOUS_COUNT, false);
				}
			}
			
			cache.remove(FQN, sessionId);
		}
	}
	
	/**
	 * Get all registered sessions
	 * 
	 * @return <code>ArrayList</code> with the sessions. Each entry
	 * is an <code>UserSession</code> object.
	 */
	public static List getAllSessions()
	{
		synchronized (FQN) {
			return new ArrayList(cache.getValues(FQN));
		}
	}
	
	/**
	 * Gets the {@link UserSession} instance of all logged users
	 * @return A list with the user sessions
	 */
	public static List getLoggedSessions()
	{
		synchronized (FQN) {
			return new ArrayList(cache.getValues(FQN_LOGGED));
		}
	}
	
	/**
	 * Get the number of logged users
	 * @return the number of logged users
	 */
	public static int registeredSize()
	{
		Integer count = (Integer)cache.get(FQN_COUNT, LOGGED_COUNT);

		return (count == null ? 0 : count.intValue());
	}
	
	/**
	 * Get the number of anonymous users
	 * @return the nuber of anonymous users
	 */
	public static int anonymousSize()
	{
		Integer count = (Integer)cache.get(FQN_COUNT, ANONYMOUS_COUNT);

		return (count == null ? 0 : count.intValue());
	}
	
	public static void clear()
	{
		synchronized (FQN) {
			cache.add(FQN, new HashMap());
			cache.add(FQN_COUNT, LOGGED_COUNT, new Integer(0));
			cache.add(FQN_COUNT, ANONYMOUS_COUNT, new Integer(0));
			cache.remove(FQN_LOGGED);
			cache.remove(FQN_USER_ID);
		}
	}
	
	/**
	 * Gets the user's <code>UserSession</code> object
	 * 
	 * @return The <code>UserSession</code> associated to the user's session
	 */
	public static UserSession getUserSession()
	{
		return getUserSession(JForumExecutionContext.getRequest().getSessionContext().getId());
	}
	
	/**
	 * Gets an {@link UserSession} by the session id.
	 * 
	 * @param sessionId the session's id
	 * @return an <b>immutable</b> UserSession, or <code>null</code> if no entry found
	 */
	public static UserSession getUserSession(String sessionId)
	{
		if (cache != null) {
			UserSession us = (UserSession)cache.get(FQN, sessionId);
			return (us != null ? us : null);
		}

		logger.warn("Got a null cache in getUserSession. #" + sessionId);
		return null;
	}

	/**
	 * Gets the number of session elements.
	 * 
	 * @return The number of session elements currently online (without bots)
	 */
	public static int size()
	{
		return (anonymousSize() + registeredSize());
	}
	
	/**
	 * Verify if the user in already loaded
	 * 
	 * @param username The username to check
	 * @return The session id if the user is already registered into the session, 
	 * or <code>null</code> if it is not.
	 */
	public static String isUserInSession(String username)
	{
		int aid = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);
		
		synchronized (FQN) {
			for (Iterator iter = cache.getValues(FQN).iterator(); iter.hasNext(); ) {
				UserSession us = (UserSession)iter.next();
				String thisUsername = us.getUsername();
				
				if (thisUsername == null) {
					continue;
				}
				
				if (us.getUserId() != aid && thisUsername.equals(username)) {
					return us.getSessionId();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Verify if there is an user in the session with the 
	 * user id passed as parameter.
	 * 
	 * @param userId The user id to check for existance in the session
	 * @return The session id if the user is already registered into the session, 
	 * or <code>null</code> if it is not.
	 */
	public static String isUserInSession(int userId)
	{
		return (String)cache.get(FQN_USER_ID, Integer.toString(userId));
	}
	
	/**
	 * Verify is the user is logged in.
	 * 
	 * @return <code>true</code> if the user is logged, or <code>false</code> if is 
	 * an anonymous user.
	 */
	public static boolean isLogged()
	{
		return "1".equals(SessionFacade.getAttribute(ConfigKeys.LOGGED));
	}
	
	/**
	 * Marks the current user session as "logged" in 
	 */
	public static void makeLogged()
	{
		SessionFacade.setAttribute(ConfigKeys.LOGGED, "1");
	}
	
	/**
	 * Marks the current user session as "logged" out
	 *
	 */
	public static void makeUnlogged()
	{
		SessionFacade.removeAttribute(ConfigKeys.LOGGED);
	}
	
	/**
	 * Returns a map containing information about read time of a set of topics.
	 * @return a map where the key is the topicId represented as an Integer, and the
	 * value is a Long representing the read time of such topic. 
	 */
	public static Map getTopicsReadTime()
	{
		Map tracking = (Map)getAttribute(ConfigKeys.TOPICS_READ_TIME);
		
		if (tracking == null) {
			tracking = new HashMap();
			setAttribute(ConfigKeys.TOPICS_READ_TIME, tracking);
		}
		
		return tracking;
	}
	
	/**
	 * Returns a map with "all topics read" flags for some forum 
	 * @return a map where the key is the forum id represented as an Integer, 
	 * and the value is a Long representing the read time to be used in the verifications.
	 */
	public static Map getTopicsReadTimeByForum()
	{
		return (Map)getAttribute(ConfigKeys.TOPICS_READ_TIME_BY_FORUM);
	}

	/**
	 * Persists user session information.
	 * This method will get a <code>Connection</code> making a call to
	 * <code>DBConnection.getImplementation().getConnection()</code>, and
	 * then releasing the connection after the method is processed.   
	 * 
	 * @param sessionId The session which we're going to persist information
	 * @see #storeSessionData(String, Connection)
	 */
	public static void storeSessionData(String sessionId)
	{
		Connection conn = null;
		try {
			conn = DBConnection.getImplementation().getConnection();
			SessionFacade.storeSessionData(sessionId, conn);
		}
		finally {
			if (conn != null) {
				try {
					DBConnection.getImplementation().releaseConnection(conn);
				}
				catch (Exception e) {
					logger.warn("Error while releasing a connection: " + e);
				}
			}
		}
	}

	/**
	 * Persists user session information.
	 * 
	 * @param sessionId The session which we're going to persist
	 * @param conn A <code>Connection</code> to be used to connect to
	 * the database. 
	 * @see #storeSessionData(String)
	 */
	public static void storeSessionData(String sessionId, Connection conn) 
	{
		UserSession us = SessionFacade.getUserSession(sessionId);
		if (us != null) {
			try {
				if (us.getUserId() != SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID)) {
					DataAccessDriver.getInstance().newUserSessionDAO().update(us, conn);
				}
				
				SecurityRepository.remove(us.getUserId());
			}
			catch (Exception e) {
				logger.warn("Error storing user session data: " + e, e);
			}
		}
	}
}
