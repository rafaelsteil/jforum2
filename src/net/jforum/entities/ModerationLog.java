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
 * Created on 08/07/2007 11:25:54
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.util.Date;

/**
 * @author Rafael Steil
 * @version $Id: ModerationLog.java,v 1.3 2007/07/10 01:04:31 rafaelsteil Exp $
 */
public class ModerationLog
{
	private int id;
	private int postId;
	private int topicId;
	private User user;
	private User posterUser = new User();
	private String description;
	private String originalMessage;
	private Date date;
	private int type;
	/**
	 * @return the id
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * @return the userId
	 */
	public User getUser()
	{
		return this.user;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * @return the originalMessage
	 */
	public String getOriginalMessage()
	{
		return this.originalMessage;
	}
	
	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return this.date;
	}
	
	/**
	 * @return the type
	 */
	public int getType()
	{
		return this.type;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * @param userId the userId to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * @param originalMessage the originalMessage to set
	 */
	public void setOriginalMessage(String originalMessage)
	{
		this.originalMessage = originalMessage;
	}
	
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(int type)
	{
		this.type = type;
	}

	/**
	 * @return the postId
	 */
	public int getPostId()
	{
		return this.postId;
	}

	/**
	 * @return the topicId
	 */
	public int getTopicId()
	{
		return this.topicId;
	}

	/**
	 * @param postId the postId to set
	 */
	public void setPostId(int postId)
	{
		this.postId = postId;
	}

	/**
	 * @param topicId the topicId to set
	 */
	public void setTopicId(int topicId)
	{
		this.topicId = topicId;
	}

	/**
	 * @return the posterUser
	 */
	public User getPosterUser()
	{
		return this.posterUser;
	}

	/**
	 * @param posterUser the posterUser to set
	 */
	public void setPosterUser(User posterUser)
	{
		this.posterUser = posterUser;
	}
}
