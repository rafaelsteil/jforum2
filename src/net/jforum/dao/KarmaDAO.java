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
 * Created on Jan 11, 2005 11:00:06 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.jforum.entities.Karma;
import net.jforum.entities.KarmaStatus;
import net.jforum.entities.User;

/**
 * @author Rafael Steil
 * @version $Id: KarmaDAO.java,v 1.7 2007/08/01 22:30:04 rafaelsteil Exp $
 */
public interface KarmaDAO
{
	/**
	 * Insert a new Karma.
	 * 
	 * @param karma The karma to add. The instance should at
	 * least have set the karma status, the user who is receiving
	 * the karma and the user which is setting the karme.
	 */
	public void addKarma(Karma karma) ;
	
	/**
	 * Gets the karma status of some user.
	 * 
	 * @param userId The user id to get the karma status
	 * @return A <code>net.jforum.entities.KarmaStatus</code> instance
	 */
	public KarmaStatus getUserKarma(int userId) ;
	
	/**
	 * Updates the karma status for some user. 
	 * This method will store the user's karme in the
	 * users table. 
	 * 
	 * @param userId The id of the user to update
	 */
	public void updateUserKarma(int userId) ;
	
	/**
	 * Checks if the user can add the karma.
	 * The method will search for existing entries in
	 * the karma table associated with the user id and post id
	 * passed as argument. If found, it means that the user 
	 * already has voted, so we cannot allow him to vote one
	 * more time.
	 * 
	 * @param userId The user id to check
	 * @param postId The post id to chekc
	 * @return <code>true</code> if the user hasn't voted on the
	 * post yet, or <code>false</code> otherwise. 
	 */
	public boolean userCanAddKarma(int userId, int postId) ;
	
	/**
	 * Gets the karma status of some post.
	 * 
	 * @param postId The post id to get the karma status
	 * @return A <code>net.jforum.entities.KarmaStatus</code> instance
	 */
	public KarmaStatus getPostKarma(int postId) ;
	
	/**
	 * Updates a karma
	 * @param karma The karma instance to update
	 */
	public void update(Karma karma) ;
	
	/**
	 * Gets the votes the user made on some topic.
	 * @param topicId The topic id.
	 * @param userId 
	 * 
	 * @return A <code>java.util.Map</code>, where the key is the post id and the
	 * value id the rate made by the user.
	 */
	public Map getUserVotes(int topicId, int userId) ;
	
	/**
	 * @param user User
	 */
	public void getUserTotalKarma(User user) ;
	
	
	/**
	 * Total points received, grouped by user and filtered by a range of dates.
	 * 
	 * @param firstPeriod Date
	 * @param lastPeriod Date
     * @param start int
     * @param orderField orderField
	 * @return Returns a List of users ant your total votes.
	 */
	public List getMostRatedUserByPeriod(int start, Date firstPeriod, Date lastPeriod, String orderField) ;
}
