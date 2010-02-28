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
 * This file creating date: Feb 23, 2003 / 2:43:40 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import java.util.List;

import net.jforum.entities.Forum;
import net.jforum.entities.ForumStats;
import net.jforum.entities.LastPostInfo;

/**
* Model interface for {@link net.jforum.entities.Forum}.
 * This interface defines methods which are expected to be
 * implementd by a specific data access driver. The intention is
 * to provide all functionality needed to update, insert, delete and
 * select some specific data.
 * 
 * @author Rafael Steil
 * @version $Id: ForumDAO.java,v 1.12 2006/08/28 23:22:27 rafaelsteil Exp $
 */
public interface ForumDAO 
{
	/**
	 * Gets a specific <code>Forum</code>.
	 * 
	 * @param forumId The ForumID to search
	 * @return <code>Forum</code>object containing all the information
	 * @see #selectAll
	 */
	public Forum selectById(int forumId) ;
	
	/**
	 * Selects all forums data from the database.
	 * 
	 * @return ArrayList with the forums found 
	 * @see #selectById
	 */
	public List selectAll() ;
	
	/**
	 * Sets the forum's order one level up.
	 * When you call this method on a specific forum, the forum that 
	 * is one level up will be sent down one level, and the forum which
	 * you are sending up wil take the order position of the forum which
	 * was sent down.
	 * 
	 * @param forum The forum to change its order
	 * @param related The forum which comes before the forum we want to change
	 * @return The changed forum, with the new order set
	 */
	public Forum setOrderUp(Forum forum, Forum related) ;
	
	/**
	 * Sets the forum's order one level down.
	 * For more information, take a look at @link #setOrderUp method. 
	 * The only different between both is that this method sends the 
	 * forum order down.
	 * 
	 * @param forum The forum to change its order
	 * @param related The forum which comes after the forum we want to change
	 * @return The changed forum, with the new order set
	 */
	public Forum setOrderDown(Forum forum, Forum related) ;
	
	/**
	 * Delete a forum.
	 * 
	 * @param forumId The forum ID to delete
	 */
	public void delete(int forumId) ;
		
	/**
	 * Updates a Forum.
	 * 
	 * @param forum Reference to a <code>Forum</code> object to update
	 */
	public void update(Forum forum) ;
	
	/**
	 * Adds a new Forum.
	 * 
	 * @param forum Reference to a valid and configured <code>Forum</code> object
	 * @return The forum's ID
	 */
	public int addNew(Forum forum) ;
	
	/**
	 * Sets the last topic of a forum
	 * 
	 * @param forumId The forum ID to update
	 * @param postId Last post ID
	 */
	public void setLastPost(int forumId, int postId) ;

	/**
	 * Increments the total number of topics of a forum
	 * 
	 * @param forumId The forum ID to update
	 * @param count Increment a total of <code>count</code> elements
	 */
	public void incrementTotalTopics(int forumId, int count) ;
	
	/**
	 * Decrements the total number of topics of a forum
	 * 
	 * @param forumId The forum ID to update
	 * @param count Decrement a total of <code>count</code> elements 
	 */
	public void decrementTotalTopics(int forumId, int count) ;

	/**
	 * Gets information about the latest message posted in some forum.
	 * 
	 * @param forumId the forum's id to inspect
	 * @return A {@link LastPostInfo} instance
	 */
	public LastPostInfo getLastPostInfo(int forumId) ;

	/**
	 * Get all moderators of some forum
	 * @param forumId the forum's id to inspect
	 * @return a list with all moderators. Each entry is an instance of
	 * {@link net.jforum.entities.ModeratorInfo}
	 */
	public List getModeratorList(int forumId) ;
	
	/**
	 * Gets the total number of messages of a forum
     * @return int
     */
	public int getTotalMessages() ;
	
	/**
	 * Gets the total number os topics of some forum
	 * 
	 * @return Total of topics
     * @param forumId int
	 */
	public int getTotalTopics(int forumId) ;

	
	/**
	 * Gets the last post id associated to the forum
	 * 
	 * @param forumId The forum id
     * @return int
	 */
	public int getMaxPostId(int forumId) ;
	
	/**
	 * Move the topics to a new forum
	 * 
	 * @param topics The topics id array
	 * @param fromForumId The original forum id
	 * @param toForumId The destination forum id
	 */
	public void moveTopics(String[] topics, int fromForumId, int toForumId) ;
	
	/**
	 * Check if the forum has unread topics.
	 * 
	 * @param forumId The forum's id to check
	 * @param lastVisit The last visit time the user has seen the forum
	 * @return An <code>java.util.List</code> instance, where each entry is a
	 * <code>net.jforum.entities.Topic</code> instance. 
	 */
	public List checkUnreadTopics(int forumId, long lastVisit) ;
	
	/**
	 * Enable or disabled moderation for the forum.
	 * 
	 * @param categoryId The main category for the forum
	 * @param status a boolean value representing the desired status
	 */
	public void setModerated(int categoryId, boolean status) ;
	
	/**
	 * Ges general statistics from the board
	 * @return ForumStats
	 */
	public ForumStats getBoardStatus() ;
	
	
	//codes below are added by socialnework@gmail.com for "watching forum" purpose
	/**
	 * Get the users to notify
	 * 
	 * @param forum The forum 
	 * @return <code>ArrayList</code> of <code>User</code> objects. Each
	 * entry is an user who will receive the new topic in the forum notification
	 * */
	public List notifyUsers(Forum forum) ;
	
	
	/**
	 * Subscribe the user for notification of new topic in the forum
	 * Added by socialnetwork@gmail.com
	 * 
	 * @param forumId int
	 * @param userId int
	 */
	public void subscribeUser(int forumId, int userId) ;
	
	/**
	 * Return the subscrition status of the user on the forum.
	 * Added by socialnetwork@gmail.com
	 * 
	 * @param forumId int
	 * @param userId int
	 * @return boolean
	 */
	public boolean isUserSubscribed(int forumId, int userId) ;
	
	/**
	 * Remove the user's subscription of the forum
	 * 
	 * @param forumId The forum id
	 * @param userId the User id
	 */
	public void removeSubscription(int forumId, int userId) ;
	
	/**
	 * Clean all subscriptions of some forum
	 * 
	 * @param forumId The forum id
	 */
	public void removeSubscriptionByForum(int forumId) ;

	/**
	 * Given an email address, finds the forum Id
	 * @param listEmail the email of the forum
	 * @return the forum id of the given email, or 0 if not found
	 */
	public int discoverForumId(String listEmail);
}