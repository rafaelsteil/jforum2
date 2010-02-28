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
 * Created on Jan 16, 2005 12:22:12 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

/**
 * @author Rafael Steil
 * @version $Id: Bookmark.java,v 1.4 2006/08/23 02:13:46 rafaelsteil Exp $
 */
public class Bookmark
{
	private int id;
	private int userId;
	private int relationId;
	private int relationType;
	private boolean publicVisible;
	private String title;
	private String description;
	
	public Bookmark() {}
	
	/**
	 * @return Returns the id.
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * @param id The id to set.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * @return Returns the publicVisible.
	 */
	public boolean isPublicVisible()
	{
		return this.publicVisible;
	}
	
	/**
	 * @param publicVisible The publicVisible to set.
	 */
	public void setPublicVisible(boolean publicVisible)
	{
		this.publicVisible = publicVisible;
	}
	
	/**
	 * @return Returns the relationId.
	 */
	public int getRelationId()
	{
		return this.relationId;
	}
	
	/**
	 * @param relationId The relationId to set.
	 */
	public void setRelationId(int relationId)
	{
		this.relationId = relationId;
	}
	
	/**
	 * @return Returns the relationType.
	 */
	public int getRelationType()
	{
		return this.relationType;
	}
	
	/**
	 * @param relationType The relationType to set.
	 */
	public void setRelationType(int relationType)
	{
		this.relationType = relationType;
	}
	
	/**
	 * @return Returns the userId.
	 */
	public int getUserId()
	{
		return this.userId;
	}
	
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return this.title;
	}
	
	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
}
