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
 * Created on 21.09.2004 
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.hsqldb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.generic.GenericTopicDAO;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Marc Wick
 * @version $Id: HsqldbTopicDAO.java,v 1.9 2007/08/23 13:47:52 rafaelsteil Exp $
 */
public class HsqldbTopicDAO extends GenericTopicDAO
{
	/**
	 * @see net.jforum.dao.TopicDAO#selectAllByForumByLimit(int, int, int)
	 */
	public List selectAllByForumByLimit(int forumId, int startFrom, int count)
	{
		String sql = SystemGlobals.getSql("TopicModel.selectAllByForumByLimit");

		PreparedStatement p = null;

		try {
			p = JForumExecutionContext.getConnection().prepareStatement(sql);
			p.setInt(1, startFrom);
			p.setInt(2, count);
			p.setInt(3, forumId);
			p.setInt(4, forumId);

			return this.fillTopicsData(p);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
	
	/**
	 * @see net.jforum.dao.generic.GenericTopicDAO#selectByUserByLimit(int, int, int)
	 */
	public List selectByUserByLimit(int userId, int startFrom, int count) 
	{
		return super.selectByUserByLimit(count, startFrom, userId);
	}
}