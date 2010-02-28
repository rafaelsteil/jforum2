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
 * This file creation date: 19/03/2004 - 18:41:50
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import net.jforum.entities.User;
import net.jforum.security.Role;
import net.jforum.security.RoleCollection;
import net.jforum.security.RoleValueCollection;


/**
 * @author Rafael Steil
 * @version $Id: GroupSecurityDAO.java,v 1.2 2007/08/24 23:11:35 rafaelsteil Exp $
 */
public interface GroupSecurityDAO 
{
	/**
	 * Deletes all roles related to a forum
	 * @param forumId
	 */
	public void deleteForumRoles(int forumId);
	
	/**
	 * Delete all roles from a specific group
	 * @param groupId ID of the group
	 **/
	public void deleteAllRoles(int groupId);

	/**
	 * Adds a new role
	 * @param groupId Group id the role should be associated
	 * */
	public void addRole(int groupId, Role role) ;

	/**
	 * @param id
	 * @param role
	 * @param rvc
	 */
	public void addRoleValue(int id, Role role, RoleValueCollection rvc) ;
	
	/**
	 * @param id
	 * @param roleName
	 * @param roleValues
	 */
	public void addRole(int id, Role role, RoleValueCollection roleValues) ;

	/**
	 * @param groupId int
	 * @return RoleCollection
	 */
	public RoleCollection loadRoles(int groupId) ;

    public RoleCollection loadRolesByUserGroups(User user) ;
}
