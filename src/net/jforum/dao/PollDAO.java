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
 * Created on Dec 29, 2004 2:00:00 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import net.jforum.entities.Poll;

public interface PollDAO {

	/**
	 * Gets a specific <code>Poll</code>.
	 * 
	 * @param pollId The Poll ID to search
	 * @return <code>Poll</code>object containing all the information
	 */
	public Poll selectById(int pollId) ;
		
	/**
	 * Delete a Poll.
	 * 
	 * @param pollId The Poll to delete
	 */
	public void delete(int pollId) ;
		
	/**
	 * Delete a Poll.
	 * 
	 * @param topicId The topic id for the poll to delete
	 */
	public void deleteByTopicId(int topicId) ;
	
	/**
	 * Updates a Poll.
	 * 
	 * @param poll Reference to a <code>Poll</code> object to update
	 */
	public void update(Poll poll) ;
	
	/**
	 * Adds a new Poll.
	 * 
	 * @param poll Poll Reference to a valid and configured <code>Poll</code> object
	 * @return The new ID
	 */
	public int addNew(Poll poll) ;

	/**
	 * Increments the vote count on the poll for the given poll id and option id
	 * @param pollId the poll id that the vote is for
	 * @param optionId the option that was selected for the poll
	 * @param userId int 
	 * @param ipAddress String 
	 */
	public void voteOnPoll(int pollId, int optionId, int userId, String ipAddress);
	
	/**
	 * Tells if the user has already voted on the given poll
	 * @param pollId the poll id that is being checked
	 * @param userId the user id to check the vote for
	 * @return true if the user has already voted on the given poll
	 */
	public boolean hasUserVotedOnPoll(int pollId, int userId) ;
	
	/**
	 * Tells if any user has already voted on the given poll from the given IP
	 * @param pollId the poll id that is being checked
	 * @param ipAddress the IP address of the user to check the vote for
	 * @return true if the user has already voted on the given poll
	 */
	public boolean hasUserVotedOnPoll(int pollId, String ipAddress) ;
}
