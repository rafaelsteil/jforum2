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
 * Created on 24/07/2007 10:27:23
 * 
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.generic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.LuceneDAO;
import net.jforum.entities.Post;
import net.jforum.exceptions.DatabaseException;
import net.jforum.search.LuceneReindexArgs;
import net.jforum.search.SearchPost;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: GenericLuceneDAO.java,v 1.13 2007/10/17 04:36:13 rafaelsteil Exp $
 */
public class GenericLuceneDAO implements LuceneDAO
{
	/**
	 * @see net.jforum.dao.LuceneDAO#firstPostId()
	 */
	public int firstPostId() 
	{
		int postId = 0;
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("SearchModel.getFirstPostId"));
			
			rs = p.executeQuery();
			
			if (rs.next()) {
				postId = rs.getInt(1);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
		
		return postId;
	}
	
	/**
	 * @see net.jforum.dao.LuceneDAO#getPostsToIndex(LuceneReindexArgs, int, int)
	 */
	public List getPostsToIndex(int fromPostId, int toPostId)
	{
		List l = new ArrayList();
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("SearchModel.getPostsToIndexForLucene"));
			
			p.setInt(1, fromPostId);
			p.setInt(2, toPostId);
			
			rs = p.executeQuery();
			
			while (rs.next()) {
				l.add(this.makePost(rs));
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
	
	/**
	 * @see net.jforum.dao.LuceneDAO#firstPostIdByDate(java.util.Date)
	 */
	public int firstPostIdByDate(Date date) 
	{
		return this.getPostIdByDate(date, SystemGlobals.getSql("SearchModel.firstPostIdByDate"));
	}
	
	/**
	 * @see net.jforum.dao.LuceneDAO#lastPostIdByDate(java.util.Date)
	 */
	public int lastPostIdByDate(Date date) 
	{
		return this.getPostIdByDate(date, SystemGlobals.getSql("SearchModel.lastPostIdByDate"));
	}
	
	private int getPostIdByDate(Date date, String query)
	{
		int postId = 0;
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(query);
			
			p.setTimestamp(1, new Timestamp(date.getTime()));
			
			rs = p.executeQuery();
			
			if (rs.next()) {
				postId = rs.getInt(1);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
		
		return postId;
	}
	
	/**
	 * @see net.jforum.dao.LuceneDAO#getPostsData(int[])
	 */
	public List getPostsData(int[] postIds)
	{
		if (postIds.length == 0) {
			return new ArrayList();
		}
		
		List l = new ArrayList();
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			String sql = SystemGlobals.getSql("SearchModel.getPostsDataForLucene");
			sql = sql.replaceAll(":posts:", this.buildInClause(postIds));
			
			p = JForumExecutionContext.getConnection().prepareStatement(sql);
			rs = p.executeQuery();
			
			while (rs.next()) {
				Post post = this.makePost(rs);
				post.setPostUsername(rs.getString("username"));
				
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
	
	private String buildInClause(int[] postIds)
	{
		StringBuffer sb = new StringBuffer(128);
		
		for (int i = 0; i < postIds.length - 1; i++) {
			sb.append(postIds[i]).append(',');
		}
		
		sb.append(postIds[postIds.length - 1]);
		
		return sb.toString();
	}
	
	private Post makePost(ResultSet rs) throws SQLException
	{
		Post p = new SearchPost();
		
		p.setId(rs.getInt("post_id"));
		p.setForumId(rs.getInt("forum_id"));
		p.setTopicId(rs.getInt("topic_id"));
		p.setUserId(rs.getInt("user_id"));
		p.setTime(new Date(rs.getTimestamp("post_time").getTime()));
		p.setText(this.readPostTextFromResultSet(rs));
		p.setBbCodeEnabled(rs.getInt("enable_bbcode") == 1);
		p.setSmiliesEnabled(rs.getInt("enable_smilies") == 1);
		
		String subject = rs.getString("post_subject");
		
		if (subject == null || subject.trim().length() == 0) {
			subject = rs.getString("topic_title");
		}
		
		p.setSubject(subject);
		
		return p;
	}
	
	protected String readPostTextFromResultSet(ResultSet rs) throws SQLException
	{
		return rs.getString("post_text");
	}
}
