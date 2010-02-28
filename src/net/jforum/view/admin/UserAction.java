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
 * This file creation date: Apr 19, 2003 / 9:13:16 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.jforum.SessionFacade;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.GroupDAO;
import net.jforum.dao.UserDAO;
import net.jforum.entities.Group;
import net.jforum.entities.User;
import net.jforum.repository.SecurityRepository;
import net.jforum.util.I18n;
import net.jforum.util.TreeGroup;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.view.forum.common.UserCommon;
import net.jforum.view.forum.common.ViewCommon;

/**
 * @author Rafael Steil
 * @version $Id: UserAction.java,v 1.35 2007/09/21 17:29:30 rafaelsteil Exp $
 */
public class UserAction extends AdminCommand 
{
	public void list()
	{
		int start = this.preparePagination(DataAccessDriver.getInstance().newUserDAO().getTotalUsers());
		int usersPerPage = SystemGlobals.getIntValue(ConfigKeys.USERS_PER_PAGE);
		
		this.context.put("users", DataAccessDriver.getInstance().newUserDAO().selectAll(start ,usersPerPage));
		this.commonData();
	}
	
	public void pendingActivations()
	{
		UserDAO dao = DataAccessDriver.getInstance().newUserDAO();
		List users = dao.pendingActivations();
		
		this.setTemplateName(TemplateKeys.USER_ADMIN_PENDING_ACTIVATIONS);
		this.context.put("users", users);
	}
	
	public void activateAccount()
	{
		String[] ids = this.request.getParameterValues("user_id");
		
		if (ids != null) {
			UserDAO dao = DataAccessDriver.getInstance().newUserDAO();
			
			for (int i = 0; i < ids.length; i++) {
				int userId = Integer.parseInt(ids[i]);
				dao.writeUserActive(userId);
			}
		}
		
		this.pendingActivations();
	}
	
	private int preparePagination(int totalUsers)
	{
		int start = ViewCommon.getStartPage();
		int usersPerPage = SystemGlobals.getIntValue(ConfigKeys.USERS_PER_PAGE);
		
		ViewCommon.contextToPagination(start, totalUsers, usersPerPage);
		
		return start;
	}
	
	private void commonData()
	{
		this.context.put("selectedList", new ArrayList());
		this.context.put("groups", new TreeGroup().getNodes());
		this.setTemplateName(TemplateKeys.USER_ADMIN_COMMON);
		this.context.put("searchAction", "list");
		this.context.put("searchId", new Integer(-1));
	}
	
	public void groupSearch()
	{
		final int groupId = this.request.getIntParameter("group_id");
		if (groupId == 0) {
			this.list();
			return;
		}
		
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		
		int start = this.preparePagination(um.getTotalUsersByGroup(groupId));
		int usersPerPage = SystemGlobals.getIntValue(ConfigKeys.USERS_PER_PAGE);
		
		this.commonData();
		
		List l = new ArrayList();
		l.add(new Integer(groupId));
		
		this.context.put("selectedList", l);
		this.context.put("searchAction", "groupSearch");
		this.context.put("users", um.selectAllByGroup(groupId, start, usersPerPage));
		this.context.put("searchId", new Integer(groupId));
	}
	
	public void search()
	{
		String search = this.request.getParameter("username");
		String group = this.request.getParameter("group");
		
		if (search != null && !"".equals(search)) {
            List users;
			users = DataAccessDriver.getInstance().newUserDAO().findByName(search, false);
			
			this.commonData();
			
			this.context.put("users", users);
			this.context.put("search", search);
			this.context.put("start", new Integer(1));
		}
		else if (!"0".equals(group)) {
			this.groupSearch();
		}
		else {
			this.list();
		}
	}
	
	public void edit()
	{
		int userId = this.request.getIntParameter("id");	
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		User u = um.selectById(userId);
		
		this.setTemplateName(TemplateKeys.USER_ADMIN_EDIT);
		this.context.put("u", u);
		this.context.put("action", "editSave");		
		this.context.put("specialRanks", DataAccessDriver.getInstance().newRankingDAO().selectSpecials());
		this.context.put("avatarAllowExternalUrl", SystemGlobals.getBoolValue(ConfigKeys.AVATAR_ALLOW_EXTERNAL_URL));
		this.context.put("admin", true);
	}
	
	public void editSave() 
	{
		int userId = this.request.getIntParameter("user_id");
		UserCommon.saveUser(userId);

		this.list();
	}

	// Delete
	public void delete()
	{
		String ids[] = this.request.getParameterValues("user_id");
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		
		if (ids != null) {
			for (int i = 0; i < ids.length; i++) {
				
				int user = Integer.parseInt(ids[i]);
				
				if (um.isDeleted(user)){
					um.undelete(user);
				} 
				else {
					String sessionId = SessionFacade.isUserInSession(user);
					
					if (sessionId != null) {
						SessionFacade.remove(sessionId);
					}
					
					um.delete(user);
				}
			}
		}
		
		this.list();
	}
	
	// Groups
	public void groups()
	{
		int userId = this.request.getIntParameter("id");
		
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		User u = um.selectById(userId);
		
		List selectedList = new ArrayList();
		for (Iterator iter = u.getGroupsList().iterator(); iter.hasNext(); ) {
			selectedList.add(new Integer(((Group)iter.next()).getId()));
		}
		
		this.context.put("selectedList", selectedList);
		this.context.put("groups", new TreeGroup().getNodes());
		this.context.put("user", u);
		this.context.put("userId", new Integer(userId));
		this.setTemplateName(TemplateKeys.USER_ADMIN_GROUPS);
		this.context.put("groupFor", I18n.getMessage("User.GroupsFor", new String[] { u.getUsername() }));
	}
	
	// Groups Save
	public void groupsSave()
	{
		int userId = this.request.getIntParameter("user_id");
		
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		GroupDAO gm = DataAccessDriver.getInstance().newGroupDAO();
		
		// Remove the old groups
		List allGroupsList = gm.selectAll();
		int[] allGroups = new int[allGroupsList.size()];
		
		int counter = 0;
		for (Iterator iter = allGroupsList.iterator(); iter.hasNext(); counter++) {
			Group g = (Group)iter.next();
			
			allGroups[counter] = g.getId();
		}
		
		um.removeFromGroup(userId, allGroups);
		
		// Associate the user to the selected groups
		String[] selectedGroups = this.request.getParameterValues("groups");
		
		if(selectedGroups == null) {
			selectedGroups = new String[0]; 
		}
		
		int[] newGroups = new int[selectedGroups.length];
		
		for (int i = 0; i < selectedGroups.length; i++) {
			newGroups[i] = Integer.parseInt(selectedGroups[i]);
		}
		
		um.addToGroup(userId, newGroups);
		SecurityRepository.remove(userId);
		
		this.list();
	}
}
