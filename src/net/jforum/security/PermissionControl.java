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
 * This file creation date: Oct 10, 2003 / 21:46:35 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.security;

import java.io.Serializable;

import net.jforum.dao.GroupSecurityDAO;

/**
 * Methods and properties for all classes that need make use of security actions.
 * 
 * @author Rafael Steil
 * @version $Id: PermissionControl.java,v 1.19 2007/04/12 02:11:55 rafaelsteil Exp $
 */
public class PermissionControl implements Serializable
{
	private RoleCollection roles;

	private transient GroupSecurityDAO smodel;

	public void setRoles(RoleCollection roles)
	{
		this.roles = roles;
	}

	public void setSecurityModel(GroupSecurityDAO smodel)
	{
		this.smodel = smodel;
	}

	public void addRole(int id, Role role)
	{
		this.smodel.addRole(id, role);
	}

	public void addRole(int id, Role role, RoleValueCollection roleValues)
	{
		this.smodel.addRole(id, role, roleValues);
	}

	public void addRoleValue(int id, Role role, RoleValueCollection roleValues)
	{
		this.smodel.addRoleValue(id, role, roleValues);
	}

	public void deleteAllRoles(int id)
	{
		this.smodel.deleteAllRoles(id);
	}

	/**
	 * Gets a role.
	 * 
	 * @param roleName
	 *            The role's name
	 * @return A <code>Role</code> object if the role was found, or <code>null</code> if not
	 *         found.
	 */
	public Role getRole(String roleName)
	{
		return this.roles.get(roleName);
	}

	/**
	 * @see net.jforum.security.PermissionControl#canAccess(java.lang.String)
	 * @param roleName
	 *            String
	 * @return boolean
	 */
	public boolean canAccess(String roleName)
	{
		return this.roles.containsKey(roleName);
	}

	/**
	 * @see net.jforum.security.PermissionControl#canAccess(java.lang.String, java.lang.String)
	 * @return boolean
	 * @param roleName
	 *            String
	 * @param roleValue
	 *            String
	 */
	public boolean canAccess(String roleName, String roleValue)
	{
		Role role = this.roles.get(roleName);

		if (role == null) {
			return false;
		}

		return role.getValues().contains(new RoleValue(roleValue));
	}
}
