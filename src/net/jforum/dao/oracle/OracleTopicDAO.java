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
 * Created on 24/05/2004 12:25:35
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.oracle;

import java.util.List;

/**
 * @author Dmitriy Kiriy 
 * @author Jake Fear
 * @version $Id: OracleTopicDAO.java,v 1.13 2006/08/20 22:47:32 rafaelsteil Exp $
 */
public class OracleTopicDAO extends net.jforum.dao.generic.GenericTopicDAO
{
   	/**
   	 * Does some addition to make the parameters work with our query flavor.
	 * @see net.jforum.dao.TopicDAO#selectAllByForumByLimit(int, int, int)
	 */
	public List selectAllByForumByLimit(int forumId, int startFrom, int count)
	{
		return super.selectAllByForumByLimit(forumId, startFrom, startFrom + count);
	}
	
	/**
	 * @see net.jforum.dao.TopicDAO#selectByUserByLimit(int, int, int)
	 */
	public List selectByUserByLimit(int userId, int startFrom, int count) 
	{
		return super.selectByUserByLimit(userId, startFrom, startFrom + count);
	}
}
