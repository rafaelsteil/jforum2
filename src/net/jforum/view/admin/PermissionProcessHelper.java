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
 * This file creation date: 17/03/2004 - 21:19:29
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.context.RequestContext;
import net.jforum.security.PermissionControl;
import net.jforum.security.Role;
import net.jforum.security.RoleValue;
import net.jforum.security.RoleValueCollection;

/**
 * @author Rafael Steil
 * @version $Id: PermissionProcessHelper.java,v 1.19 2006/08/24 01:07:02 rafaelsteil Exp $
 */
class PermissionProcessHelper 
{
	private PermissionControl pc;
	private int groupId;
	
	public PermissionProcessHelper(PermissionControl pc, int id)
	{
		this.groupId = id;
		this.pc = pc;
		
		this.init();
	}
	
	public void processData()
	{
		RequestContext request = JForumExecutionContext.getRequest();
		Enumeration e = request.getParameterNames();
		
		while (e.hasMoreElements()) {
			String paramName = (String)e.nextElement();
			
			if (paramName.startsWith("perm_")) {
				if (paramName.endsWith("$single")) {
					String paramValue = request.getParameter(paramName);
					
					if (paramValue.equals("deny")) {
						continue;
					}

					paramName = paramName.substring(0, paramName.indexOf('$'));
					
					Role role = new Role();
					role.setName(paramName);
					
					this.pc.addRole(this.groupId, role);
				}
				else {
					String[] paramValues = request.getParameterValues(paramName);
					RoleValueCollection roleValues = new RoleValueCollection();
					
					if ("all".equals(paramValues[0])) {
						this.addRoleValues(roleValues, this.getSplitedValues("all" + paramName));
					}
					else {
						List allowList = new ArrayList(Arrays.asList(this.getSplitedValues("all" + paramName))); 
						allowList.removeAll(Arrays.asList(paramValues));
						
						this.addRoleValues(roleValues, allowList.toArray());
					}
					
					Role role = new Role();
					role.setName(paramName);
					
					this.pc.addRole(this.groupId, role, roleValues);
				}
			}
		}
	}
	
	private String[] getSplitedValues(String paramName)
	{
		String[] allValues = JForumExecutionContext.getRequest().getParameter(paramName).split(";");
		String[] returnValues = new String[allValues.length];
		
		for (int i = 0, counter = 0; i < allValues.length; i++) {
			if (allValues[i].trim().equals("")) {
				continue;
			}
			
			returnValues[counter++] = allValues[i];
		}
		
		return returnValues;
	}
	
	private void addRoleValues(RoleValueCollection roleValues, Object[] allValues)
	{
		for (int i = 0; i < allValues.length; i++) {
			String value = (String)allValues[i];
			
			if (value == null || value.equals("")) {
				continue;
			}

			roleValues.add(new RoleValue((String)allValues[i]));
		}
	}
	
	private void init()
	{
		this.pc.deleteAllRoles(this.groupId);
	}
}
