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
 * This file creation date: 24/05/2004 / 12:01 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.oracle;

import net.jforum.dao.LuceneDAO;
import net.jforum.dao.ModerationDAO;
import net.jforum.dao.ModerationLogDAO;
import net.jforum.dao.PostDAO;
import net.jforum.dao.PrivateMessageDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.dao.UserDAO;
import net.jforum.dao.generic.GenericDataAccessDriver;

/**
 * @author Dmitriy Kiriy
 * @version $Id: OracleDataAccessDriver.java,v 1.11 2007/09/10 22:34:21 rafaelsteil Exp $
 */
public class OracleDataAccessDriver extends GenericDataAccessDriver
{
	private static PostDAO postDao = new OraclePostDAO();
	private static TopicDAO topicDao = new OracleTopicDAO();
	private static UserDAO userDao = new OracleUserDAO();
	private static PrivateMessageDAO pmDao = new OraclePrivateMessageDAO();
	private static ModerationDAO moderationDao = new OracleModerationDAO();
	private static ModerationLogDAO moderationLogDao = new OracleModerationLogDAO();
	private static LuceneDAO luceneDao = new OracleLuceneDAO();
	
	/**
	 * @see net.jforum.dao.generic.GenericDataAccessDriver#newModerationLogDAO()
	 */
	public ModerationLogDAO newModerationLogDAO() 
	{
		return moderationLogDao;
	}
	
	/**
	 * @see net.jforum.dao.DataAccessDriver#newModerationDAO()
	 */
	public ModerationDAO newModerationDAO()
	{
		return moderationDao;
	}
	
	/**
	 * @see net.jforum.dao.DataAccessDriver#newPostDAO()
	 */
	public PostDAO newPostDAO()
	{
		return postDao;
	}

	/** 
	 * @see net.jforum.dao.DataAccessDriver#newTopicDAO()
	 */
	public TopicDAO newTopicDAO()
	{
		return topicDao;
	}
	
	/** 
	 * @see net.jforum.dao.DataAccessDriver#newUserDAO()
	 */
	public UserDAO newUserDAO()
	{
		return userDao;
	}
	
	/**
	 * @see net.jforum.dao.DataAccessDriver#newPrivateMessageDAO()
	 */
	public PrivateMessageDAO newPrivateMessageDAO()
	{
		return pmDao;
	}
	
	/**
	 * @see net.jforum.dao.generic.GenericDataAccessDriver#newLuceneDAO()
	 */
	public LuceneDAO newLuceneDAO() 
	{
		return luceneDao;
	}
}
