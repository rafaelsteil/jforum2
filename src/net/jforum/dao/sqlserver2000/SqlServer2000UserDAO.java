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
 * Created on 24/05/2004 22:36:07
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.sqlserver2000;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.generic.GenericUserDAO;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;


/**
 * @author Andre de Andrade da Silva - andre.de.andrade@gmail.com
 * @author Dirk Rasmussen - d.rasmussen@bevis.de (2006/11/27, modifs for MS SqlServer 2005)
 * @author Andowson Chang - andowson@gmail.com (2007/12/06, fix for MS SQL Server 2000)
 * @see WEB-INF\config\database\sqlserver\sqlserver.sql (2006/11/27, MS SqlServer 2005 specific version!)
 * @version $Id: SqlServer2000UserDAO.java,v 1.2 2008/01/22 23:52:41 rafaelsteil Exp $
 */
public class SqlServer2000UserDAO extends GenericUserDAO
{
	/**
	 * @see net.jforum.dao.UserDAO#selectAll(int, int)
	 */
	public List selectAll(int startFrom, int count)
	{
		String sql = SystemGlobals.getSql("UserModel.selectAllByLimit");
		
		PreparedStatement p = null;
		ResultSet rs = null;

		try {
			if (count > 0) {
				p = JForumExecutionContext.getConnection().prepareStatement(sql, 
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
				p.setInt(1, startFrom + count);
				
				rs = p.executeQuery();
				rs.absolute(startFrom);
			}
			else {
				p = JForumExecutionContext.getConnection()
						.prepareStatement(SystemGlobals.getSql("UserModel.selectAll"));
				rs = p.executeQuery();
			}			

			return this.processSelectAll(rs);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.UserDAO#selectAllWithKarma(int, int)
	 */
	public List selectAllWithKarma(int startFrom, int count)
	{
		return super.loadKarma(this.selectAll(startFrom, count));
	}
}
