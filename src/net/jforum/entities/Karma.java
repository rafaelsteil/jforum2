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
 * Created on Jan 11, 2005 10:57:13 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.util.Date;

/**
 * @author Rafael Steil
 * @version $Id: Karma.java,v 1.6 2006/08/23 02:13:46 rafaelsteil Exp $
 */
public class Karma
{
	private int id;
	private int postId;
	private int topicId;
	private int postUserId;
	private int fromUserId;
	private int points;
	private Date rateDate;
	
	/**
	 * @return Returns the topicId.
	 */
	public int getTopicId()
	{
		return this.topicId;
	}
	
	/**
	 * @param topicId The topicId to set.
	 */
	public void setTopicId(int topicId)
	{
		this.topicId = topicId;
	}
	
	/**
	 * @return Returns the fromUserId.
	 */
	public int getFromUserId()
	{
		return this.fromUserId;
	}
	
	/**
	 * @param fromUserId The fromUserId to set.
	 */
	public void setFromUserId(int fromUserId)
	{
		this.fromUserId = fromUserId;
	}
	
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
	 * @return Returns the userId.
	 */
	public int getPostUserId()
	{
		return this.postUserId;
	}
	
	/**
	 * @param userId The userId to set.
	 */
	public void setPostUserId(int userId)
	{
		this.postUserId = userId;
	}
	
	/**
	 * @return Returns the points.
	 */
	public int getPoints()
	{
		return this.points;
	}
	
	/**
	 * @param points The points to set.
	 */
	public void setPoints(int points)
	{
		this.points = points;
	}
	
	/**
	 * @return Returns the postId.
	 */
	public int getPostId()
	{
		return this.postId;
	}
	
	/**
	 * @param postId The postId to set.
	 */
	public void setPostId(int postId)
	{
		this.postId = postId;
	}
	
	/**
	 * @return Returns the date of the vote.
	 */
    public Date getRateDate() 
    {
        return rateDate;
    }
    
    /**
	 * @param dateDate The date of the vote.
	 */
    public void setRateDate(Date rateDate) 
    {
        this.rateDate = rateDate;
    }
}
