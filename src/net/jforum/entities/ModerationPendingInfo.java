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
 * Created on Jan 30, 2005 11:50:42 AM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rafael Steil
 * @version $Id: ModerationPendingInfo.java,v 1.4 2006/08/23 02:13:47 rafaelsteil Exp $
 */
public class ModerationPendingInfo
{
	private int categoryId;
	private String categoryName;
	private List infoList = new ArrayList();
	
	public void setCategoryId(int categoryId)
	{
		this.categoryId = categoryId;
	}
	
	public void setCategoryName(String name)
	{
		this.categoryName = name;
	}
	
	public String getCategoryName()
	{
		return this.categoryName;
	}
	
	public int getCategoryId()
	{
		return this.categoryId;
	}
	
	public void addInfo(String forumName, int forumId, int postsToModerate)
	{
		this.infoList.add(new ForumModerationInfo(forumName, forumId, postsToModerate));
	}
	
	public List getForums()
	{
		return this.infoList;
	}
}
