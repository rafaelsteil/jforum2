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
 * This file creation date: 30/03/2003 / 02:37:20
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.generic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.GroupSecurityDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.entities.Forum;
import net.jforum.entities.ForumStats;
import net.jforum.entities.LastPostInfo;
import net.jforum.entities.ModeratorInfo;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @author Vanessa Sabino
 * @author socialnetwork@gmail.com, adding "watch forum" methods.
 * 
 * @version $Id: GenericForumDAO.java,v 1.33 2007/08/24 23:11:35 rafaelsteil Exp $
 */
public class GenericForumDAO extends AutoKeys implements net.jforum.dao.ForumDAO
{
	/**
	 * @see net.jforum.dao.ForumDAO#selectById(int)
	 */
	public Forum selectById(int forumId)
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ForumModel.selectById"));
			p.setInt(1, forumId);

			rs = p.executeQuery();

			Forum f = new Forum();

			if (rs.next()) {
				f = this.fillForum(rs);
			}
			return f;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	protected Forum fillForum(ResultSet rs) throws SQLException
	{
		Forum f = new Forum();

		f.setId(rs.getInt("forum_id"));
		f.setIdCategories(rs.getInt("categories_id"));
		f.setName(rs.getString("forum_name"));
		f.setDescription(rs.getString("forum_desc"));
		f.setOrder(rs.getInt("forum_order"));
		f.setTotalTopics(rs.getInt("forum_topics"));
		f.setLastPostId(rs.getInt("forum_last_post_id"));
		f.setModerated(rs.getInt("moderated") > 0);
		f.setTotalPosts(this.countForumPosts(f.getId()));

		return f;
	}

	protected int countForumPosts(int forumId)
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.countForumPosts"));
			p.setInt(1, forumId);
			rs = p.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
			
			return 0;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.ForumDAO#selectAll()
	 */
	public List selectAll()
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ForumModel.selectAll"));
			List l = new ArrayList();

			rs = p.executeQuery();

			while (rs.next()) {
				l.add(this.fillForum(rs));
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
	 * @see net.jforum.dao.ForumDAO#setOrderUp(Forum, Forum)
	 */
	public Forum setOrderUp(Forum forum, Forum related)
	{
		return this.changeForumOrder(forum, related);
	}

	/**
	 * @see net.jforum.dao.ForumDAO#setOrderDown(Forum, Forum)
	 */
	public Forum setOrderDown(Forum forum, Forum related)
	{
		return this.changeForumOrder(forum, related);
	}

	private Forum changeForumOrder(Forum forum, Forum related)
	{
		int tmpOrder = related.getOrder();
		related.setOrder(forum.getOrder());
		forum.setOrder(tmpOrder);

		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("ForumModel.setOrderById"));
			p.setInt(1, forum.getOrder());
			p.setInt(2, forum.getId());
			p.executeUpdate();
			p.close();
			p = null;

			p = JForumExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("ForumModel.setOrderById"));
			p.setInt(1, related.getOrder());
			p.setInt(2, related.getId());
			p.executeUpdate();

			return this.selectById(forum.getId());
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	/**
	 * @see net.jforum.dao.ForumDAO#delete(int)
	 */
	public void delete(int forumId)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ForumModel.delete"));
			p.setInt(1, forumId);

			p.executeUpdate();
			
			GroupSecurityDAO groupSecurity = DataAccessDriver.getInstance().newGroupSecurityDAO();
			groupSecurity.deleteForumRoles(forumId);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	/**
	 * @see net.jforum.dao.ForumDAO#update(net.jforum.entities.Forum)
	 */
	public void update(Forum forum)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ForumModel.update"));

			p.setInt(1, forum.getCategoryId());
			p.setString(2, forum.getName());
			p.setString(3, forum.getDescription());
			p.setInt(4, forum.isModerated() ? 1 : 0);
			p.setInt(5, forum.getId());

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
	 * @see net.jforum.dao.ForumDAO#addNew(net.jforum.entities.Forum)
	 */
	public int addNew(Forum forum)
	{
		// Gets the higher order
		PreparedStatement pOrder = null;
		ResultSet rs = null;
		try {
			pOrder = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.getMaxOrder"));
			rs = pOrder.executeQuery();

			if (rs.next()) {
				forum.setOrder(rs.getInt(1) + 1);
			}

			rs.close();
			rs = null;
			pOrder.close();
			pOrder = null;

			pOrder = this.getStatementForAutoKeys("ForumModel.addNew");

			pOrder.setInt(1, forum.getCategoryId());
			pOrder.setString(2, forum.getName());
			pOrder.setString(3, forum.getDescription());
			pOrder.setInt(4, forum.getOrder());
			pOrder.setInt(5, forum.isModerated() ? 1 : 0);

			this.setAutoGeneratedKeysQuery(SystemGlobals.getSql("ForumModel.lastGeneratedForumId"));
			int forumId = this.executeAutoKeysQuery(pOrder);

			forum.setId(forumId);
			return forumId;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, pOrder);
		}
	}

	/**
	 * @see net.jforum.dao.ForumDAO#setLastPost(int, int)
	 */
	public void setLastPost(int forumId, int postId)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.updateLastPost"));

			p.setInt(1, postId);
			p.setInt(2, forumId);

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
	 * @see net.jforum.dao.ForumDAO#setTotalTopics(int)
	 */
	public void incrementTotalTopics(int forumId, int count)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.incrementTotalTopics"));
			p.setInt(1, count);
			p.setInt(2, forumId);
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
	 * @see net.jforum.dao.ForumDAO#setTotalTopics(int)
	 */
	public void decrementTotalTopics(int forumId, int count)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.decrementTotalTopics"));
			p.setInt(1, count);
			p.setInt(2, forumId);
			p.executeUpdate();

			// If there are no more topics, then clean the
			// last post id information
			int totalTopics = this.getTotalTopics(forumId);
			if (totalTopics < 1) {
				this.setLastPost(forumId, 0);
			}
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}

	private LastPostInfo getLastPostInfo(int forumId, boolean tryFix)
	{
		LastPostInfo lpi = new LastPostInfo();

		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("ForumModel.lastPostInfo"));
			p.setInt(1, forumId);

			rs = p.executeQuery();

			if (rs.next()) {
				lpi.setUsername(rs.getString("username"));
				lpi.setUserId(rs.getInt("user_id"));

				SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
				lpi.setPostDate(df.format(rs.getTimestamp("post_time")));
				lpi.setPostId(rs.getInt("post_id"));
				lpi.setTopicId(rs.getInt("topic_id"));
				lpi.setPostTimeMillis(rs.getTimestamp("post_time").getTime());
				lpi.setTopicReplies(rs.getInt("topic_replies"));

				lpi.setHasInfo(true);

				// Check if the topic is consistent
				TopicDAO tm = DataAccessDriver.getInstance().newTopicDAO();
				Topic t = tm.selectById(lpi.getTopicId());

				if (t.getId() == 0) {
					// Hm, that's not good. Try to fix it
					tm.fixFirstLastPostId(lpi.getTopicId());
				}

				tryFix = false;
			}
			else if (tryFix) {
				rs.close();
				rs = null;
				p.close();
				p = null;

				int postId = this.getMaxPostId(forumId);

				p = JForumExecutionContext.getConnection().prepareStatement(
						SystemGlobals.getSql("ForumModel.latestTopicIdForfix"));
				p.setInt(1, forumId);
				rs = p.executeQuery();

				if (rs.next()) {
					int topicId;
					topicId = rs.getInt("topic_id");

					rs.close();
					rs = null;
					p.close();
					p = null;

					// Topic
					p = JForumExecutionContext.getConnection().prepareStatement(
							SystemGlobals.getSql("ForumModel.fixLatestPostData"));
					p.setInt(1, postId);
					p.setInt(2, topicId);
					p.executeUpdate();
					p.close();
					p = null;

					// Forum
					p = JForumExecutionContext.getConnection().prepareStatement(
							SystemGlobals.getSql("ForumModel.fixForumLatestPostData"));
					p.setInt(1, postId);
					p.setInt(2, forumId);
					p.executeUpdate();
				}
			}

			return (tryFix ? this.getLastPostInfo(forumId, false) : lpi);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.ForumDAO#getLastPostInfo(int)
	 */
	public LastPostInfo getLastPostInfo(int forumId)
	{
		return this.getLastPostInfo(forumId, true);
	}

	/**
	 * @see net.jforum.dao.ForumDAO#getModeratorList(int)
	 */
	public List getModeratorList(int forumId)
	{
		List l = new ArrayList();

		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.getModeratorList"));
			p.setInt(1, forumId);

			rs = p.executeQuery();

			while (rs.next()) {
				ModeratorInfo mi = new ModeratorInfo();

				mi.setId(rs.getInt("id"));
				mi.setName(rs.getString("name"));

				l.add(mi);
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
	 * @see net.jforum.dao.ForumDAO#getTotalMessages()
	 */
	public int getTotalMessages()
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.totalMessages"));
			rs = p.executeQuery();

			if (rs.next()) {
				return rs.getInt("total_messages");
			}

			return 0;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.ForumDAO#getTotalTopics(int)
	 */
	public int getTotalTopics(int forumId)
	{
		int total = 0;
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.getTotalTopics"));
			p.setInt(1, forumId);
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
	 * @see net.jforum.dao.ForumDAO#getMaxPostId(int)
	 */
	public int getMaxPostId(int forumId)
	{
		int id = -1;

		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("ForumModel.getMaxPostId"));
			p.setInt(1, forumId);

			rs = p.executeQuery();
			if (rs.next()) {
				id = rs.getInt("post_id");
			}

			return id;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.ForumDAO#moveTopics(java.lang.String[], int, int)
	 */
	public void moveTopics(String[] topics, int fromForumId, int toForumId)
	{
		PreparedStatement p = null;
		PreparedStatement t = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("ForumModel.moveTopics"));
			t = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("PostModel.setForumByTopic"));

			p.setInt(1, toForumId);
			p.setInt(2, fromForumId);
			
			t.setInt(1, toForumId);

			TopicDAO tdao = DataAccessDriver.getInstance().newTopicDAO();

			Forum f = this.selectById(toForumId);

			for (int i = 0; i < topics.length; i++) {
				int topicId = Integer.parseInt(topics[i]);
				p.setInt(3, topicId);
				t.setInt(2, topicId);

				p.executeUpdate();
				t.executeUpdate();

				tdao.setModerationStatusByTopic(topicId, f.isModerated());
			}

			this.decrementTotalTopics(fromForumId, topics.length);
			this.incrementTotalTopics(toForumId, topics.length);

			this.setLastPost(fromForumId, this.getMaxPostId(fromForumId));
			this.setLastPost(toForumId, this.getMaxPostId(toForumId));
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
			DbUtils.close(t);
		}
	}

	/**
	 * @see net.jforum.dao.ForumDAO#hasUnreadTopics(int, long)
	 */
	public List checkUnreadTopics(int forumId, long lastVisit)
	{
		List l = new ArrayList();

		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.checkUnreadTopics"));
			p.setInt(1, forumId);
			p.setTimestamp(2, new Timestamp(lastVisit));

			rs = p.executeQuery();
			while (rs.next()) {
				Topic t = new Topic();
				t.setId(rs.getInt("topic_id"));
				t.setTime(new Date(rs.getTimestamp(1).getTime()));

				l.add(t);
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
	 * @see net.jforum.dao.ForumDAO#setModerated(int, boolean)
	 */
	public void setModerated(int categoryId, boolean status)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("ForumModel.setModerated"));
			p.setInt(1, status ? 1 : 0);
			p.setInt(2, categoryId);
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
	 * @see net.jforum.dao.ForumDAO#getBoardStatus()
	 */
	public ForumStats getBoardStatus()
	{
		ForumStats fs = new ForumStats();
		fs.setPosts(this.getTotalMessages());

		Connection c = JForumExecutionContext.getConnection();

		// Total Users
		Statement s = null;
		ResultSet rs = null;

		try {
			s = c.createStatement();
			rs = s.executeQuery(SystemGlobals.getSql("UserModel.totalUsers"));
			rs.next();
			fs.setUsers(rs.getInt(1));
			rs.close();
			rs = null;
			s.close();
			s = null;

			// Total Topics
			s = c.createStatement();
			rs = s.executeQuery(SystemGlobals.getSql("TopicModel.totalTopics"));
			rs.next();
			fs.setTopics(rs.getInt(1));
			rs.close();
			rs = null;
			s.close();
			s = null;

			// Posts per day
			double postPerDay = 0;
			
			// Topics per day
			double topicPerDay = 0;
			
			// user per day
			double userPerDay = 0;

			s = c.createStatement();
			rs = s.executeQuery(SystemGlobals.getSql("ForumModel.statsFirstPostTime"));
			if (rs.next()) {

				Timestamp firstTime = rs.getTimestamp(1);
				if (rs.wasNull()) {
					firstTime = null;
				}
				rs.close();
				rs = null;
				s.close();
				s = null;

				Date today = new Date();

				postPerDay = firstTime != null ? fs.getPosts() / this.daysUntilToday(today, firstTime) : 0;

				if (fs.getPosts() > 0 && postPerDay < 1) {
					postPerDay = 1;
				}

				topicPerDay = firstTime != null ? fs.getTopics() / this.daysUntilToday(today, firstTime) : 0;

				// Users per day
				s = c.createStatement();
				rs = s.executeQuery(SystemGlobals.getSql("ForumModel.statsFirstRegisteredUserTime"));
				if (rs.next()) {
					firstTime = rs.getTimestamp(1);
					if (rs.wasNull()) {
						firstTime = null;
					}
				}
				rs.close();
				rs = null;
				s.close();
				s = null;

				userPerDay = firstTime != null ? fs.getUsers() / this.daysUntilToday(today, firstTime) : 0;
			}

			fs.setPostsPerDay(postPerDay);
			fs.setTopicsPerDay(topicPerDay);
			fs.setUsersPerDay(userPerDay);

			return fs;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, s);
		}
	}

	private int daysUntilToday(Date today, Date from)
	{
		int days = (int) ((today.getTime() - from.getTime()) / (24 * 60 * 60 * 1000));
		return days == 0 ? 1 : days;
	}

	/**
	 * This code is writen by looking at GenericTopicDAO.java
	 * 
	 * @see
	 */
	public List notifyUsers(Forum forum)
	{
		int posterId = SessionFacade.getUserSession().getUserId();
		int anonUser = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);

		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.notifyUsers"));

			p.setInt(1, forum.getId());
			p.setInt(2, posterId); // don't notify the poster
			p.setInt(3, anonUser); // don't notify the anonimous user

			rs = p.executeQuery();
			List users = new ArrayList();
			
			while (rs.next()) {
				User user = new User();

				user.setId(rs.getInt("user_id"));
				user.setEmail(rs.getString("user_email"));
				user.setUsername(rs.getString("username"));
				user.setLang(rs.getString("user_lang"));
				user.setNotifyAlways(rs.getInt("user_notify_always") == 1);
				user.setNotifyText(rs.getInt("user_notify_text") == 1);

				users.add(user);
			}
			
			return users;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}

	}

	public void subscribeUser(int forumId, int userId)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.subscribeUser"));

			p.setInt(1, forumId);
			p.setInt(2, userId);

			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}

	}

	public boolean isUserSubscribed(int forumId, int userId)
	{
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.isUserSubscribed"));

			stmt.setInt(1, forumId);
			stmt.setInt(2, userId);

			rs = stmt.executeQuery();

			return rs.next();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, stmt);
		}
	}

	public void removeSubscription(int forumId, int userId)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.removeSubscription"));
			p.setInt(1, forumId);
			p.setInt(2, userId);

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
	 * Remove all subscriptions on a forum, such as when a forum is locked. It is not used now.
	 * 
	 * @param forumId int
	 */
	public void removeSubscriptionByForum(int forumId)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("ForumModel.removeSubscriptionByForum"));
			p.setInt(1, forumId);

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
	 * @see net.jforum.dao.ForumDAO#discoverForumId(java.lang.String)
	 */
	public int discoverForumId(String listEmail)
	{
		int forumId = 0;
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
				SystemGlobals.getSql("ForumModel.discoverForumId"));
			p.setString(1, listEmail);
			rs = p.executeQuery();
			
			if (rs.next()) {
				forumId = rs.getInt(1);
			}
		}
		catch (SQLException e) {
			
		}
		finally {
			DbUtils.close(rs, p);
		}
		
		return forumId;
	}
}
