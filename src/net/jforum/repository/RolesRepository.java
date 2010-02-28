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
 * Created on Apr 5, 2005 12:53:14 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.dao.generic.security.SecurityCommon;
import net.jforum.security.RoleCollection;

/**
 * @author Rafael Steil
 * @version $Id: RolesRepository.java,v 1.7 2007/08/01 22:30:05 rafaelsteil Exp $
 */
public class RolesRepository implements Cacheable
{
	private static final String FQN = "roles";
	private static CacheEngine cache;
	
	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}
	
	/**
	 * Gets the group roles.
	 * 
	 * @param groupId The group id
	 * @return The roles, if found, or <code>null</code> otherwise. 
	 */
	public static RoleCollection getGroupRoles(int groupId)
	{
		return (RoleCollection)cache.get(FQN, Integer.toString(groupId));
	}
	
	/**
	 * Clears the cache
	 */
	public static void clear()
	{
		cache.add(FQN, null);
	}
	
	/**
	 * Get merged roles from a set of groups
	 * @param ids The group ids
	 * @return The roles, if found, or <code>null</code> otherwise. 
	 */
	public static RoleCollection getGroupRoles(int[] ids)
	{
		return (RoleCollection)cache.get(FQN, SecurityCommon.groupIdAsString(ids));
	}
	
	/**
	 * Adds merged roles to the cache.
	 * 
	 * @param groupIds The ids of the groups 
	 * @param roles The merges roles to add 
	 */
	public static void addGroupRoles(int[] groupIds, RoleCollection roles)
	{
		cache.add(FQN, SecurityCommon.groupIdAsString(groupIds), roles);
	}
}
