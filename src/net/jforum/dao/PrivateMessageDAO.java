/*
 * Copyright (c) JForum Team
 * All rights reserved.
 * 
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
 * This file creation date: 20/05/2004 - 15:37:25
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import java.util.List;

import net.jforum.entities.PrivateMessage;
import net.jforum.entities.User;

/**
 * @author Rafael Steil
 * @version $Id: PrivateMessageDAO.java,v 1.8 2007/08/01 22:30:04 rafaelsteil Exp $
 */
public interface PrivateMessageDAO
{
	/**
	 * Send a new <code>PrivateMessage</code>
	 * 
	 * @param pm The pm to add
	 */
	public void send(PrivateMessage pm) ;
	
	/**
	 * Deletes a collection of private messages.
	 * Each instance should at least have the private message
	 * id and the owner user id.
	 * 
	 * @param pm PrivateMessage[]
	 * @param userId
	 */
	public void delete(PrivateMessage[] pm, int userId) ;
	
	/**
	 * Update the type of some private message.
	 * You should pass as argument a <code>PrivateMessage</code> instance
	 * with the pm's id and the new message status. There is no need to
	 * fill the other members.
	 * 
	 * @param pm The instance to update 
	 */
	public void updateType(PrivateMessage pm) ;
	
	/**
	 * Selects all messages from the user's inbox. 
	 * 
	 * @param user The user to fetch the messages
	 * @return A <code>List</code> with all messages found. Each 
	 * entry is a <code>PrivateMessage</code> entry.
	 */
	public List selectFromInbox(User user) ;
	
	/**
	 * Selects all messages from the user's sent box. 
	 * 
	 * @param user The user to fetch the messages
	 * @return A <code>List</code> with all messages found. Each 
	 * entry is a <code>PrivateMessage</code> entry.
	 */
	public List selectFromSent(User user) ;
	
	/**
	 * Gets a <code>PrivateMessage</code> by its id.
	 * 
	 * @param pm A <code>PrivateMessage</code> instance containing the pm's id
	 * to retrieve
	 * @return The pm contents
	 */
	public PrivateMessage selectById(PrivateMessage pm) ;
}
