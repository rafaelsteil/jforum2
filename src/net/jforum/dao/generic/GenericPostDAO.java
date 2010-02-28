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
 * This file creation date: Mar 28, 2003 / 22:57:43 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.generic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Post;
import net.jforum.exceptions.DatabaseException;
import net.jforum.repository.ForumRepository;
import net.jforum.search.SearchFacade;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @author Vanessa Sabino
 * @version $Id: GenericPostDAO.java,v 1.29 2007/09/12 14:43:15 rafaelsteil Exp $
 */
public class GenericPostDAO extends AutoKeys implements net.jforum.dao.PostDAO
{
	/**
	 * @see net.jforum.dao.PostDAO#selectById(int)
	 */
	public Post selectById(int postId)
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("PostModel.selectById"));
			p.setInt(1, postId);

			rs = p.executeQuery();

			Post post = new Post();

			if (rs.next()) {
				post = this.makePost(rs);
			}

			return post;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	protected Post makePost(ResultSet rs) throws SQLException
	{
		Post post = new Post();
		post.setId(rs.getInt("post_id"));
		post.setTopicId(rs.getInt("topic_id"));
		post.setForumId(rs.getInt("forum_id"));
		post.setUserId(rs.getInt("user_id"));
		
		Timestamp postTime = rs.getTimestamp("post_time");
		post.setTime(new Date(postTime.getTime()));
		post.setUserIp(rs.getString("poster_ip"));
		post.setBbCodeEnabled(rs.getInt("enable_bbcode") > 0);
		post.setHtmlEnabled(rs.getInt("enable_html") > 0);
		post.setSmiliesEnabled(rs.getInt("enable_smilies") > 0);
		post.setSignatureEnabled(rs.getInt("enable_sig") > 0);
		post.setEditCount(rs.getInt("post_edit_count"));
		
		Timestamp editTime = rs.getTimestamp("post_edit_time");
		post.setEditTime(editTime != null ? new Date(editTime.getTime()) : null);
		
		post.setSubject(rs.getString("post_subject"));
		post.setText(this.getPostTextFromResultSet(rs));
		post.setPostUsername(rs.getString("username"));
		post.hasAttachments(rs.getInt("attach") > 0);
		post.setModerate(rs.getInt("need_moderate") == 1);

		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		post.setFormatedTime(df.format(postTime));

		post.setKarma(DataAccessDriver.getInstance().newKarmaDAO().getPostKarma(post.getId()));

		return post;
	}

	/**
	 * Utility method to read the post text fromt the result set. This method may be useful when
	 * using some "non-standart" way to store text, like oracle does when using (c|b)lob
	 * 
	 * @param rs The resultset to fetch data from
	 * @return The post text string
	 * @throws SQLException
	 */
	protected String getPostTextFromResultSet(ResultSet rs) throws SQLException
	{
		return rs.getString("post_text");
	}

	/**
	 * @see net.jforum.dao.PostDAO#delete(Post)
	 */
	public void delete(Post post)
	{
		List l = new ArrayList();
		l.add(post);
		this.removePosts(l);
	}

	private void removePosts(List posts)
	{
		PreparedStatement post = null;
		PreparedStatement text = null;

		try {
			post = JForumExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("PostModel.deletePost"));

			text = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("PostModel.deletePostText"));

			for (Iterator iter = posts.iterator(); iter.hasNext();) {
				Post p = (Post) iter.next();

				post.setInt(1, p.getId());
				text.setInt(1, p.getId());

				text.executeUpdate();
				post.executeUpdate();
				
				SearchFacade.delete(p);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(post);
			DbUtils.close(text);
		}
	}

	/**
	 * @see net.jforum.model.PostModel#deleteByTopic(int)
	 */
	public void deleteByTopic(int topicId)
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection()
				.prepareStatement(SystemGlobals.getSql("PostModel.deleteByTopic"));
			p.setInt(1, topicId);
			rs = p.executeQuery();

			List posts = new ArrayList();
			
			while (rs.next()) {
				Post post = new Post();
				post.setId(rs.getInt("post_id"));
				post.setUserId(rs.getInt("user_id"));

				posts.add(post);
			}

			this.removePosts(posts);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.PostDAO#update(net.jforum.entities.Post)
	 */
	public void update(Post post)
	{
		this.updatePostsTable(post);
		this.updatePostsTextTable(post);

		SearchFacade.update(post);
	}

	protected void updatePostsTextTable(Post post)
	{
		PreparedStatement p = null;

		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("PostModel.updatePostText"));
			p.setString(1, post.getText());
			p.setString(2, post.getSubject());
			p.setInt(3, post.getId());

			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	protected void updatePostsTable(Post post)
	{
		PreparedStatement p = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("PostModel.updatePost"));
			p.setInt(1, post.getTopicId());
			p.setInt(2, post.getForumId());
			p.setInt(3, post.isBbCodeEnabled() ? 1 : 0);
			p.setInt(4, post.isHtmlEnabled() ? 1 : 0);
			p.setInt(5, post.isSmiliesEnabled() ? 1 : 0);
			p.setInt(6, post.isSignatureEnabled() ? 1 : 0);
			p.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			p.setInt(8, post.getEditCount() + 1);
			p.setString(9, post.getUserIp());
			p.setInt(10, post.getId());

			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	/**
	 * @see net.jforum.dao.PostDAO#addNew(net.jforum.entities.Post)
	 */
	public int addNew(Post post)
	{
		try {
			this.addNewPost(post);
			this.addNewPostText(post);

			// Search
			SearchFacade.create(post);
			
			return post.getId();
		}
		catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	protected void addNewPostText(Post post) throws Exception
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("PostModel.addNewPostText"));
			p.setInt(1, post.getId());
			p.setString(2, post.getText());
			p.setString(3, post.getSubject());
			p.executeUpdate();
		}
		finally {
			DbUtils.close(p);
		}
	}

	protected void addNewPost(Post post)
	{
		PreparedStatement p = null;
		try {
			p = this.getStatementForAutoKeys("PostModel.addNewPost");

			p.setInt(1, post.getTopicId());
			p.setInt(2, post.getForumId());
			p.setLong(3, post.getUserId());
			p.setTimestamp(4, new Timestamp(post.getTime().getTime()));
			p.setString(5, post.getUserIp());
			p.setInt(6, post.isBbCodeEnabled() ? 1 : 0);
			p.setInt(7, post.isHtmlEnabled() ? 1 : 0);
			p.setInt(8, post.isSmiliesEnabled() ? 1 : 0);
			p.setInt(9, post.isSignatureEnabled() ? 1 : 0);
			p.setInt(10, post.isModerationNeeded() ? 1 : 0);

			this.setAutoGeneratedKeysQuery(SystemGlobals.getSql("PostModel.lastGeneratedPostId"));
			int postId = this.executeAutoKeysQuery(p);
			post.setId(postId);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	/**
	 * @see net.jforum.dao.PostDAO#selectAllBytTopic(int)
	 */
	public List selectAllByTopic(int topicId)
	{
		return this.selectAllByTopicByLimit(topicId, 0, Integer.MAX_VALUE - 1);
	}

	/**
	 * @see net.jforum.dao.PostDAO#selectAllBytTopicByLimit(int, int, int)
	 */
	public List selectAllByTopicByLimit(int topicId, int startFrom, int count)
	{
		List l = new ArrayList();

		String sql = SystemGlobals.getSql("PostModel.selectAllByTopicByLimit");

		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(sql);
			p.setInt(1, topicId);
			p.setInt(2, startFrom);
			p.setInt(3, count);

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

			p.setInt(1, userId);
			p.setInt(2, startFrom);
			p.setInt(3, count);

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

	public int countUserPosts(int userId)
	{
		int total = 0;

		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("PostModel.countUserPosts").replaceAll(":fids:",
							ForumRepository.getListAllowedForums()));
			p.setInt(1, userId);

			rs = p.executeQuery();

			if (rs.next()) {
				total = rs.getInt(1);
			}

			return total;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.model.PostModel#countPreviousPosts(int)
	 */
	public int countPreviousPosts(int postId)
	{
		int total = 0;

		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("PostModel.countPreviousPosts"));
			p.setInt(1, postId);
			p.setInt(2, postId);

			rs = p.executeQuery();

			if (rs.next()) {
				total = rs.getInt(1);
			}

			return total;
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
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("PostModel.selectLatestByForumForRSS"));
			p.setInt(1, forumId);
			p.setInt(2, limit);
			
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
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("PostModel.selectHotForRSS"));
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

