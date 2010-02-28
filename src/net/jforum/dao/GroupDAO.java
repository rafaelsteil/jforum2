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
 * This file creating date: Feb 19, 2003 / 8:56:28 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import java.util.List;

import net.jforum.entities.Group;

/**
 * Model interface for {@link net.jforum.entities.Group}.
 * This interface defines methods which are expected to be
 * implementd by a specific data access driver. The intention is
 * to provide all functionality needed to update, insert, delete and
 * select some specific data.
 * 
 * @author Rafael Steil
 * @version $Id: GroupDAO.java,v 1.6 2006/08/23 02:13:34 rafaelsteil Exp $
 */
public interface GroupDAO 
{
	/**
	 * Gets a specific <code>Group</code>.
	 * 
	 * @param groupId The Group ID to search
	 * @return <code>Group</code>object containing all the information
	 * @see #selectAll
	 */
	public Group selectById(int groupId) ;
	
	/**
	 * Get all groups
	 * 
	 * @return <code>ArrayList</code> containing the groups. Each entry
	 * is an <code>Group</code> object.
	 */
	public List selectAll() ;
	
	/**
	 * Checks if is possible to delete a specific group.
	 * 
	 * @param groupId The group ID to verify
	 * @return <code>true</code> if is possible to delete, <code>false</code> if not
	 * @see #delete(int)
	 */
	public boolean canDelete(int groupId) ;
	
	/**
	 * Deletes a group.
	 * 
	 * @param groupId The group ID to delete
	 * @see #canDelete(int)
	 */
	public void delete(int groupId) ;
	
	
	/**
	 * Updates a group.
	 * 
	 * @param group Reference to a <code>Group</code> object to update
	 */
	public void update(Group group) ;
	
	/**
	 * Adds a new group.
	 * 
	 * @param group Reference to a valid and configured <code>Group</code> object
	 */
	public void addNew(Group group) ;

	/**
	 * Select the users associated to some group; 
	 * 
	 * @return <code>ArrayList</code> with the user ids
     * @param groupId int
	 */
	public List selectUsersIds(int groupId) ;
}
