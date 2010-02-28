/*
 * Copyright (c) JForum Team
 * All rights reserved.

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
 * Created on 29/05/2004 00:12:37
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.sqlserver2000;

import net.jforum.dao.ModerationLogDAO;
import net.jforum.dao.PostDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.dao.UserDAO;
import net.jforum.dao.generic.GenericDataAccessDriver;

/**
 * @author Andre de Andrade da Silva - andre.de.andrade@gmail.com
 * @author Andowson Chang http://www.andowson.com
 * @version $Id: SqlServer2000DataAccessDriver.java,v 1.1 2007/12/21 13:03:59 andowson Exp $
 */
public class SqlServer2000DataAccessDriver extends GenericDataAccessDriver
{
	private static PostDAO postDao = new SqlServer2000PostDAO();
	private static TopicDAO topicDao = new SqlServer2000TopicDAO();
	private static UserDAO userDao = new SqlServer2000UserDAO();
	private static ModerationLogDAO moderationLogDao = new SqlServer2000ModerationLogDAO();

	/** 
	 * @see net.jforum.dao.DataAccessDriver#newPostDAO()
	 */
	public net.jforum.dao.PostDAO newPostDAO()
	{
		return postDao;
	}

	/** 
	 * @see net.jforum.dao.DataAccessDriver#newTopicDAO()
	 */
	public net.jforum.dao.TopicDAO newTopicDAO()
	{
		return topicDao;
	}
	
	/** 
	 * @see net.jforum.dao.DataAccessDriver#newUserDAO()
	 */
	public net.jforum.dao.UserDAO newUserDAO()
	{
		return userDao;
	}
	
	/**
	 * @see net.jforum.dao.generic.GenericDataAccessDriver#newModerationLogDAO()
	 */
	public ModerationLogDAO newModerationLogDAO() 
	{
		return moderationLogDao;
	}	
}
