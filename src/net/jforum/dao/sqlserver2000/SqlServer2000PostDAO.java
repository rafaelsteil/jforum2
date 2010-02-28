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
package net.jforum.dao.sqlserver2000;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.generic.GenericPostDAO;
import net.jforum.entities.Post;
import net.jforum.exceptions.DatabaseException;
import net.jforum.repository.ForumRepository;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Andre de Andrade da Silva - andre.de.andrade@gmail.com
 * @author Dirk Rasmussen - d.rasmussen@bevis.de (2006/11/27, modifs for MS SqlServer 2005)
 * @author Andowson Chang - andowson@gmail.com (2007/12/06, fix for MS SQL Server 2000)
 * @see WEB-INF\config\database\sqlserver\sqlserver.sql (2006/11/27, MS SqlServer 2005 specific version!)
 * @version $Id: SqlServer2000PostDAO.java,v 1.2 2008/01/22 23:52:41 rafaelsteil Exp $
 */
public class SqlServer2000PostDAO extends GenericPostDAO
{
	/**
	 * @see net.jforum.dao.PostDAO#selectAllByTopicByLimit(int, int, int)
	 */
	public List selectAllByTopicByLimit(int topicId, int startFrom, int count)
	{
		List l = new ArrayList();

		String sql = SystemGlobals.getSql("PostModel.selectAllByTopicByLimit");
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(sql);
			p.setInt(1, startFrom + count);
			p.setInt(2, topicId);

			rs = p.executeQuery();

			while (rs.next()) {
				l.add(this.makePost(rs));
			}

			return l;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.PostDAO#selectByUserByLimit(int, int, int)
	 */
	public List selectByUserByLimit(int userId, int startFrom, int count)
	{
		String sql = SystemGlobals.getSql("PostModel.selectByUserByLimit");
		sql = sql.replaceAll(":fids:", ForumRepository.getListAllowedForums());
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(sql);
			p.setInt(1, startFrom + count);
			p.setInt(2, userId);

			rs = p.executeQuery();
			List l = new ArrayList();

			while (rs.next()) {
				l.add(this.makePost(rs));
			}

			return l;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}
	
	public List selectLatestByForumForRSS(int forumId, int limit) 
	{
		List l = new ArrayList();

		String sql = SystemGlobals.getSql("PostModel.selectLatestByForumForRSS");
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(sql);
			p.setInt(1, limit);
			p.setInt(2, forumId);
			
			rs = p.executeQuery();
			
			while (rs.next()) {
				Post post = this.buildPostForRSS(rs);
				l.add(post);
			}
			
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
		
		return l;
	}

	public List selectHotForRSS(int limit) 
	{
		List l = new ArrayList();

		String sql = SystemGlobals.getSql("PostModel.selectHotForRSS");
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(sql);
			p.setInt(1, limit);
			
			rs = p.executeQuery();
			
			while (rs.next()) {
				Post post = this.buildPostForRSS(rs);
				l.add(post);
			}
			
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
		
		return l;
	}
	
	private Post buildPostForRSS(ResultSet rs) throws SQLException 
	{
		Post post = new Post();
		
		post.setId(rs.getInt("post_id"));
		post.setSubject(rs.getString("subject"));
		post.setText(rs.getString("post_text"));
		post.setTopicId(rs.getInt("topic_id"));
		post.setForumId(rs.getInt("forum_id")); 
		post.setUserId(rs.getInt("user_id"));
		post.setPostUsername(rs.getString("username"));
		post.setTime(new Date(rs.getTimestamp("post_time").getTime()));
		
		return post;
	}
}
