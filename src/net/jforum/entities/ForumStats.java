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
 * Created on 03/12/2005 21:31:00
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

/**
 * @author Rafael Steil
 * @version $Id: ForumStats.java,v 1.3 2006/08/20 22:47:36 rafaelsteil Exp $
 */
public class ForumStats
{
	private int users;
	private int posts;
	private int topics;
	private double postsPerDay;
	private double topicsPerDay;
	private double usersPerDay;
	
	/**
	 * @return Returns the posts.
	 */
	public int getPosts()
	{
		return this.posts;
	}
	
	/**
	 * @param posts The posts to set.
	 */
	public void setPosts(int posts)
	{
		this.posts = posts;
	}
	
	/**
	 * @return Returns the postsPerDay.
	 */
	public double getPostsPerDay()
	{
		return this.postsPerDay;
	}
	
	/**
	 * @param postsPerDay The postsPerDay to set.
	 */
	public void setPostsPerDay(double postsPerDay)
	{
		this.postsPerDay = postsPerDay;
	}
	
	/**
	 * @return Returns the topics.
	 */
	public int getTopics()
	{
		return this.topics;
	}
	
	/**
	 * @param topics The topics to set.
	 */
	public void setTopics(int topics)
	{
		this.topics = topics;
	}
	
	/**
	 * @return Returns the topicsPerDay.
	 */
	public double getTopicsPerDay()
	{
		return this.topicsPerDay;
	}
	
	/**
	 * @param topicsPerDay The topicsPerDay to set.
	 */
	public void setTopicsPerDay(double topicsPerDay)
	{
		this.topicsPerDay = topicsPerDay;
	}
	
	/**
	 * @return Returns the users.
	 */
	public int getUsers()
	{
		return this.users;
	}
	
	/**
	 * @param users The users to set.
	 */
	public void setUsers(int users)
	{
		this.users = users;
	}
	
	/**
	 * @return Returns the usersPerDay.
	 */
	public double getUsersPerDay()
	{
		return this.usersPerDay;
	}
	
	/**
	 * @param usersPerDay The usersPerDay to set.
	 */
	public void setUsersPerDay(double usersPerDay)
	{
		this.usersPerDay = usersPerDay;
	}
}
