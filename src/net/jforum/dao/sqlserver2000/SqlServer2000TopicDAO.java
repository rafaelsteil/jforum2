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
package net.jforum.dao.sqlserver2000;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.generic.GenericTopicDAO;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.exceptions.DatabaseException;
import net.jforum.repository.ForumRepository;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Andre de Andrade da Silva - andre.de.andrade@gmail.com
 * @author Dirk Rasmussen - d.rasmussen@bevis.de (2006/11/27, modifs for MS SqlServer 2005)
 * @author Andowson Chang - andowson@gmail.com (2007/12/06, fix for MS SQL Server 2000)
 * @see WEB-INF\config\database\sqlserver\sqlserver.sql (2006/11/27, MS SqlServer 2005 specific version!)
 * @version $Id: SqlServer2000TopicDAO.java,v 1.2 2008/01/22 23:52:41 rafaelsteil Exp $
 */
public class SqlServer2000TopicDAO extends GenericTopicDAO
{
	/**
	 * @see net.jforum.dao.TopicDAO#selectAllByForumByLimit(int, int, int)
	 */
	public List selectAllByForumByLimit(int forumId, int startFrom, int count)
	{
		String sql = SystemGlobals.getSql("TopicModel.selectAllByForumByLimit");
		
		PreparedStatement p = null;

		try {
			p = JForumExecutionContext.getConnection().prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			p.setInt(1, startFrom + count);
			p.setInt(2, forumId);
			p.setInt(3, forumId);

			return this.fillTopicsDataByLimit(p, startFrom);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	/**
	 * @see net.jforum.dao.TopicDAO#selectByUserByLimit(int, int, int)
	 */
	public List selectByUserByLimit(int userId, int startFrom, int count)
	{
		String sql = SystemGlobals.getSql("TopicModel.selectByUserByLimit");
        
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					sql.replaceAll(":fids:",
							ForumRepository.getListAllowedForums()), 
							ResultSet.TYPE_SCROLL_INSENSITIVE,
							ResultSet.CONCUR_READ_ONLY);

			p.setInt(1, startFrom + count);
			p.setInt(2, userId);

			List list = this.fillTopicsDataByLimit(p, startFrom);
			p = null;
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
	
	/**
		 * Fills all topic data. The method will try to get all fields from the
		 * topics table, as well information about the user who made the first
		 * and the last post in the topic. <br>
		 * <b>The method <i>will</i> close the <i>PreparedStatement</i></b>
		 * 
		 * @param p
		 *            the PreparedStatement to execute
		 * @return A list with all topics found
		 * @throws SQLException
		 */
	private List fillTopicsDataByLimit(PreparedStatement p, int startFrom) {
		List l = new ArrayList();

		ResultSet rs = null;
		try {
			rs = p.executeQuery();
			rs.absolute(startFrom);

			SimpleDateFormat df = new SimpleDateFormat(SystemGlobals
					.getValue(ConfigKeys.DATE_TIME_FORMAT));

			StringBuffer sbFirst = new StringBuffer(128);
			StringBuffer sbLast = new StringBuffer(128);

			while (rs.next()) {
				Topic t = this.getBaseTopicData(rs);

				// Posted by
				User u = new User();
				u.setId(rs.getInt("user_id"));
				t.setPostedBy(u);

				// Last post by
				u = new User();
				u.setId(rs.getInt("last_user_id"));
				t.setLastPostBy(u);

				t.setHasAttach(rs.getInt("attach") > 0);
				t.setFirstPostTime(df.format(rs.getTimestamp("topic_time")));
				t.setLastPostTime(df.format(rs.getTimestamp("post_time")));
				t.setLastPostDate(new Date(rs.getTimestamp("post_time")
						.getTime()));

				l.add(t);

				sbFirst.append(rs.getInt("user_id")).append(',');
				sbLast.append(rs.getInt("last_user_id")).append(',');
			}

			rs.close();
			rs = null;
			p.close();
			p = null;

			// Users
			if (sbFirst.length() > 0) {
				sbLast.delete(sbLast.length() - 1, sbLast.length());

				String sql = SystemGlobals
						.getSql("TopicModel.getUserInformation");
				sql = sql.replaceAll("#ID#", sbFirst.toString()
						+ sbLast.toString());

				Map users = new HashMap();

				p = JForumExecutionContext.getConnection()
						.prepareStatement(sql);
				rs = p.executeQuery();

				while (rs.next()) {
					users.put(new Integer(rs.getInt("user_id")), rs
							.getString("username"));
				}

				rs.close();
				rs = null;
				p.close();
				p = null;

				for (Iterator iter = l.iterator(); iter.hasNext();) {
					Topic t = (Topic) iter.next();
					t.getPostedBy().setUsername(
							(String) users.get(new Integer(t.getPostedBy()
									.getId())));
					t.getLastPostBy().setUsername(
							(String) users.get(new Integer(t.getLastPostBy()
									.getId())));
				}
			}

			return l;
		} catch (SQLException e) {
			throw new DatabaseException(e);
		} finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.TopicDAO#selectRecentTopics(int)
	 */
	public List selectRecentTopics(int limit)
	{
		String sql = SystemGlobals.getSql("TopicModel.selectRecentTopicsByLimit");
		
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(sql);
			p.setInt(1, limit);
			
			List list = this.fillTopicsData(p);
			return list;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
	
	/**
	 * @see net.jforum.dao.TopicDAO#selectHottestTopics(int)
	 */
	public List selectHottestTopics(int limit)
	{
		String sql = SystemGlobals.getSql("TopicModel.selectHottestTopicsByLimit");
		
		PreparedStatement p = null;
	    try {
	        p = JForumExecutionContext.getConnection().prepareStatement(sql);
	        p.setInt(1, limit);
	        
	        List list = this.fillTopicsData(p);
	        p = null;
	        return list;
	    }
	    catch (SQLException e) {
	        throw new DatabaseException(e);
	    }
	    finally {
	        DbUtils.close(p);
	    }    
	}
}
