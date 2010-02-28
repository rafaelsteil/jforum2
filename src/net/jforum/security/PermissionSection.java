/*
 * Copyright (c) JForum Team
 * All rights reserved.

 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:

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
 * This file creation date: 21/09/2003 / 16:38:49
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.security;

/**
 * @author Rafael Steil
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the permissions page's sections and its respective items
 * 
 * @author Rafael Steil
 * @version $Id: PermissionSection.java,v 1.6 2006/08/20 22:47:35 rafaelsteil Exp $
 */
public class PermissionSection 
{
	private String sectionName;
	private String sectionId;
	private List permissionItens;
	
	public PermissionSection(String sectionName, String sectionId)
	{
		this.sectionName = sectionName;
		this.sectionId = sectionId;
		this.permissionItens = new ArrayList();
	}		
	
	public void addPermission(PermissionItem item)
	{
		this.permissionItens.add(item);
	}

	public List getPermissions()
	{
		return this.permissionItens;
	}

	public String getSectionId() {
		return this.sectionId;
	}

	public String getSectionName() {
		return this.sectionName;
	}
}
