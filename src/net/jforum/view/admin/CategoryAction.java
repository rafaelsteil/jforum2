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
 * This file creation date: Mar 10, 2003 / 8:49:51 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.admin;

import java.util.ArrayList;
import java.util.List;

import net.jforum.dao.CategoryDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.GroupSecurityDAO;
import net.jforum.entities.Category;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.repository.RolesRepository;
import net.jforum.security.PermissionControl;
import net.jforum.security.Role;
import net.jforum.security.RoleValue;
import net.jforum.security.RoleValueCollection;
import net.jforum.security.SecurityConstants;
import net.jforum.util.I18n;
import net.jforum.util.TreeGroup;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.view.admin.common.ModerationCommon;

/**
 * ViewHelper for category administration.
 * 
 * @author Rafael Steil
 * @version $Id: CategoryAction.java,v 1.25 2006/12/07 23:34:04 rafaelsteil Exp $
 */
public class CategoryAction extends AdminCommand 
{
	private CategoryDAO cm = DataAccessDriver.getInstance().newCategoryDAO();
	
	// Listing
	public void list()
	{
		this.context.put("categories", DataAccessDriver.getInstance().newCategoryDAO().selectAll());
		this.context.put("repository", new ForumRepository());
		this.setTemplateName(TemplateKeys.CATEGORY_LIST);
	}
	
	// One more, one more
	public void insert()
	{
		this.context.put("groups", new TreeGroup().getNodes());
		this.context.put("selectedList", new ArrayList());
		this.setTemplateName(TemplateKeys.CATEGORY_INSERT);
		this.context.put("action", "insertSave");
	}
	
	// Edit
	public void edit()
	{
		this.context.put("category", this.cm.selectById(this.request.getIntParameter("category_id")));
		this.setTemplateName(TemplateKeys.CATEGORY_EDIT);
		this.context.put("action", "editSave");
	}
	
	//  Save information
	public void editSave()
	{
		Category c = new Category(ForumRepository.getCategory(
				this.request.getIntParameter("categories_id")));
		c.setName(this.request.getParameter("category_name"));
		c.setModerated("1".equals(this.request.getParameter("moderate")));
			
		this.cm.update(c);
		ForumRepository.reloadCategory(c);
		
		new ModerationCommon().setForumsModerationStatus(c, c.isModerated());
		
		this.list();
	}
	
	// Delete
	public void delete()
	{
		String ids[] = this.request.getParameterValues("categories_id");
		List errors = new ArrayList();
		
		if (ids != null) {						
			for (int i = 0; i < ids.length; i++){
				if (this.cm.canDelete(Integer.parseInt(ids[i]))) {
					int id = Integer.parseInt(ids[i]);
					Category c = this.cm.selectById(id);
					this.cm.delete(id);
					
					ForumRepository.removeCategory(c);
				}
				else {
					errors.add(I18n.getMessage(I18n.CANNOT_DELETE_CATEGORY, new Object[] { new Integer(ids[i]) }));
				}
			}
		}

		if (errors.size() > 0) {
			this.context.put("errorMessage", errors);
		}
		
		this.list();
	}
	
	// A new one
	public void insertSave()
	{
		Category c = new Category();
		c.setName(this.request.getParameter("category_name"));
		c.setModerated("1".equals(this.request.getParameter("moderated")));
			
		int categoryId = this.cm.addNew(c);
		c.setId(categoryId);

		ForumRepository.addCategory(c);
		
		String[] groups = this.request.getParameterValues("groups");
		if (groups != null) {
			GroupSecurityDAO gmodel = DataAccessDriver.getInstance().newGroupSecurityDAO();
			PermissionControl pc = new PermissionControl();
			pc.setSecurityModel(gmodel);

			Role role = new Role();
			role.setName(SecurityConstants.PERM_CATEGORY);

			for (int i = 0; i < groups.length; i++) {
				int groupId = Integer.parseInt(groups[i]);
				RoleValueCollection roleValues = new RoleValueCollection();
				
				RoleValue rv = new RoleValue();
				rv.setValue(Integer.toString(categoryId));
				
				roleValues.add(rv);
				
				pc.addRoleValue(groupId, role, roleValues);
			}
			
			SecurityRepository.clean();
			RolesRepository.clear();
		}
			
		this.list();
	}
	
	public void up()
	{
		this.processOrdering(true);
	}
	
	public void down()
	{
		this.processOrdering(false);
	}
	
	private void processOrdering(boolean up) 
	{
		Category toChange = new Category(ForumRepository.getCategory(Integer.parseInt(
				this.request.getParameter("category_id"))));
		
		List categories = ForumRepository.getAllCategories();
		
		int index = categories.indexOf(toChange);
		if (index == -1 || (up && index == 0) || (!up && index + 1 == categories.size())) {
			this.list();
			return;
		}
		
		if (up) {
			// Get the category which comes *before* the category we want to change
			Category otherCategory = new Category((Category)categories.get(index - 1));
			this.cm.setOrderUp(toChange, otherCategory);
		}
		else {
			// Get the category which comes *after* the category we want to change
			Category otherCategory = new Category((Category)categories.get(index + 1));
			this.cm.setOrderDown(toChange, otherCategory);
		}
		
		ForumRepository.reloadCategory(toChange);
		this.list();
	}
}
