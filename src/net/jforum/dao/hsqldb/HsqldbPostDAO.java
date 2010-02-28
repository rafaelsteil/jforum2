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
 * Created on 24/05/2004 / 12:04:11
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.hsqldb;

import java.util.List;

import net.jforum.dao.generic.GenericPostDAO;

/**
 * @author Marc Wick
 * @version $Id: HsqldbPostDAO.java,v 1.8 2007/09/02 15:08:05 rafaelsteil Exp $
 */
public class HsqldbPostDAO extends GenericPostDAO
{
	/**
	 * @see net.jforum.dao.generic.GenericPostDAO#selectLatestByForumForRSS(int, int)
	 */
	public List selectLatestByForumForRSS(int forumId, int limit) 
	{
		return super.selectLatestByForumForRSS(limit, forumId);
	}
	
	/**
	 * @see net.jforum.dao.PostDAO#selectAllByTopicByLimit(int, int, int)
	 */
	public List selectAllByTopicByLimit(int topicId, int startFrom, int count)
	{
		return super.selectAllByTopicByLimit(startFrom, count, topicId);
	}
	
	/**
	 * @see net.jforum.dao.generic.GenericPostDAO#selectByUserByLimit(int, int, int)
	 */
	public List selectByUserByLimit(int userId, int startFrom, int count) 
	{
		return super.selectByUserByLimit(startFrom, count, userId);
	}
}