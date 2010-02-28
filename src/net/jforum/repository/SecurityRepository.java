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
 * This file creation date: 18/11/2003 / 23:09:15
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.GroupSecurityDAO;
import net.jforum.dao.UserDAO;
import net.jforum.entities.User;
import net.jforum.entities.UserSession;
import net.jforum.exceptions.SecurityLoadException;
import net.jforum.security.PermissionControl;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: SecurityRepository.java,v 1.25 2006/08/27 01:22:02 rafaelsteil Exp $
 */
public class SecurityRepository implements Cacheable
{
	private static final Logger logger = Logger.getLogger(SecurityRepository.class);
	private static CacheEngine cache;
	private static final String FQN = "security";

	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}

	/***
	 * Load user's roles. 
	 * 
	 * @param userId The user's id
	 * @param force If <code>true</code>, forces a reload. If <code>false</code>, the call
	 * will be ignored if the roles are already loaded.
	 * 
	 * @see SecurityRepository#load(int)
	 * @see SecurityRepository#load(User)
	 * @see SecurityRepository#load(User, boolean)
     * @return PermissionControl
	 */
	public static PermissionControl load(int userId, boolean force)
	{
		if (force || cache.get(FQN, Integer.toString(userId)) == null) {
			UserDAO um = DataAccessDriver.getInstance().newUserDAO();
			
			return SecurityRepository.load(um.selectById(userId), force);
		}
		
		return SecurityRepository.get(userId);
	}

	/**
	 * Load user's roles.
	 * 
	 * @param userId The users's id
	 * 
	 * @see SecurityRepository#load(int, boolean)
	 * @see SecurityRepository#load(User)
	 * @see SecurityRepository#load(User, boolean)
     * @return PermissionControl
	 */
	public static PermissionControl load(int userId)
	{
		return SecurityRepository.load(userId, false);
	}
	
	/**
	 * Load user's roles.
	 * 
	 * @param user The <code>User</code> to load.
	 * 
	 * @see SecurityRepository#load(int)
	 * @see SecurityRepository#load(int, boolean), 
	 * @see SecurityRepository#load(User, boolean)
     * @return  PermissionControl
	 */
	public static PermissionControl load(User user)
	{
		return SecurityRepository.load(user, false);
	}

	/**
	 * Load user's roles.
	 * 
	 * @param user The <code>User</code> to load
	 * @param force If <code>true</code>, forces a reload. If <code>false</code>, the call
	 * will be ignored if the roles are already loaded.
	 * 
	 * @see SecurityRepository#load(int)
	 * @see SecurityRepository#load(int, boolean)
	 * @see SecurityRepository#load(User)
     * @return PermissionControl
	 */
	public static PermissionControl load(User user, boolean force)
	{
		String userId = Integer.toString(user.getId());
		
		if (force || cache.get(FQN, userId) == null) {
			PermissionControl pc = new PermissionControl();
			
			// load roles
			GroupSecurityDAO dao = DataAccessDriver.getInstance().newGroupSecurityDAO();
			pc.setRoles(dao.loadRolesByUserGroups(user));
			
			cache.add(FQN, userId, pc);
			
			return pc;
		}
		
		return SecurityRepository.get(user.getId());
	}
	
	/**
	 * Check if the logged user has access to the role. 
	 * This method gets user's id from its session.
	 * 
	 * @param roleName The role name to verity
	 * @return <code>true</code> if the user has access to the role, <code>false</code> if access is denied
	 * @throws SecurityLoadException if case of erros while trying
	 * to load the roles
	 * @see #canAccess(String, String)
	 * @see #canAccess(int, String, String)
	 */
	public static boolean canAccess(String roleName)
	{
		return canAccess(roleName, null);
	}
	
	public static boolean canAccess(int userId, String roleName)
	{
		return canAccess(userId, roleName, null);
	}

	/**
	 * Check if the logged user has access to the role. 
	 * This method gets user's id from its session.
	 * 
	 * @param roleName The role name to verify
	 * @param value The value relacted to the role to verify for access
	 * @return <code>true</code> if the user has access to the role, <code>false</code> if access is denied
	 */
	public static boolean canAccess(String roleName, String value)
	{
		UserSession us = SessionFacade.getUserSession();
		
		if (us == null) {
			logger.warn("Found null userSession. Going anonymous. Session id #" 
				+ JForumExecutionContext.getRequest().getSessionContext().getId());
			us = new UserSession();
			us.makeAnonymous();
		}
		
		return canAccess(us.getUserId(), roleName, value);
	}
	
	public static boolean canAccess(int userId, String roleName, String value)
	{
		PermissionControl pc = SecurityRepository.get(userId);
		
		if (pc == null) {
			throw new SecurityLoadException("Failed to load security roles for userId " + userId + " (null PermissionControl returned). "
				+ "roleName=" + roleName + ", roleValue=" + value);
		}
		
		return (value != null ? pc.canAccess(roleName, value) : pc.canAccess(roleName));
	}

	/**
	 * Gets the permssion schema of some specific user.
	 * If the roles of the user aren't loaded yet, a call
	 * to {@link #load(int)} will be made.
	 * 
	 * @param userId The user's id to get the permissions
	 * @return The <code>PermissionControl</code> instance related
	 * to the user id passed as argument
	 * @throws SecurityLoadException if case of erros while trying
	 * to load the roles
	 */
	public static PermissionControl get(int userId)
	{
		PermissionControl pc = (PermissionControl)cache.get(FQN, Integer.toString(userId));
		
		if (pc == null) {
			try {
				pc = load(userId);
			}
			catch (Exception e) {
				throw new SecurityLoadException(e);
			}
		}
		
		return pc;
	}

	/**
	 * Adds a new permission control schema to the cache
	 * 
	 * @param userId The user's id to associate with the schema
	 * @param pc The <code>PermissionControl</code> instance to add
	 */
	public static synchronized void add(int userId, PermissionControl pc)
	{
		cache.add(FQN, Integer.toString(userId), pc);
	}
	
	/**
	 * Remove the cached roles from a specific user.
	 * 
	 * @param userId The id of the user to remove from the cache
	 */
	public static synchronized void remove(int userId)
	{
		cache.remove(FQN, Integer.toString(userId));
	}
	
	/**
	 * Clear all cached security entries.
	 */
	public static synchronized void clean()
	{
		cache.remove(FQN);
	}
}
