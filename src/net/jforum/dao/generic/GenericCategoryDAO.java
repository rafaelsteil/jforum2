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
 * This file creation date: Mar 6, 2003 / 11:09:34 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.generic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.jforum.JForumExecutionContext;
import net.jforum.entities.Category;
import net.jforum.exceptions.DatabaseException;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: GenericCategoryDAO.java,v 1.9 2007/07/28 14:17:10 rafaelsteil Exp $
 */
public class GenericCategoryDAO extends AutoKeys implements net.jforum.dao.CategoryDAO
{
	/**
	 * @see net.jforum.dao.CategoryDAO#selectById(int)
	 */
	public Category selectById(int categoryId)
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("CategoryModel.selectById"));
			p.setInt(1, categoryId);

			rs = p.executeQuery();

			Category c = new Category();
			if (rs.next()) {
				c = this.getCategory(rs);
			}

			return c;
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.CategoryDAO#selectAll()
	 */
	public List selectAll()
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("CategoryModel.selectAll"));
			List l = new ArrayList();

			rs = p.executeQuery();
			while (rs.next()) {
				l.add(this.getCategory(rs));
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

	protected Category getCategory(ResultSet rs) throws SQLException
	{
		Category c = new Category();

		c.setId(rs.getInt("categories_id"));
		c.setName(rs.getString("title"));
		c.setOrder(rs.getInt("display_order"));
		c.setModerated(rs.getInt("moderated") == 1);

		return c;
	}

	/**
	 * @see net.jforum.dao.CategoryDAO#canDelete(int)
	 */
	public boolean canDelete(int categoryId)
	{
		PreparedStatement p = null;
		ResultSet rs = null;
		try {
			p = JForumExecutionContext.getConnection()
					.prepareStatement(SystemGlobals.getSql("CategoryModel.canDelete"));
			p.setInt(1, categoryId);

			rs = p.executeQuery();
			return !rs.next() || rs.getInt("total") < 1;

		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(rs, p);
		}
	}

	/**
	 * @see net.jforum.dao.CategoryDAO#delete(int)
	 */
	public void delete(int categoryId)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("CategoryModel.delete"));
			p.setInt(1, categoryId);
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
	 * @see net.jforum.dao.CategoryDAO#update(net.jforum.entities.Category)
	 */
	public void update(Category category)
	{
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(SystemGlobals.getSql("CategoryModel.update"));
			p.setString(1, category.getName());
			p.setInt(2, category.isModerated() ? 1 : 0);
			p.setInt(3, category.getId());
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
	 * @see net.jforum.dao.CategoryDAO#addNew(net.jforum.entities.Category)
	 */
	public int addNew(Category category)
	{
		int order = 1;
		ResultSet rs = null;
		PreparedStatement p = null;
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("CategoryModel.getMaxOrder"));
			rs = p.executeQuery();
			if (rs.next()) {
				order = rs.getInt(1) + 1;
			}
			rs.close();
			rs = null;
			p.close();
			p = null;

			p = this.getStatementForAutoKeys("CategoryModel.addNew");
			p.setString(1, category.getName());
			p.setInt(2, order);
			p.setInt(3, category.isModerated() ? 1 : 0);

			this.setAutoGeneratedKeysQuery(SystemGlobals.getSql("CategoryModel.lastGeneratedCategoryId"));
			int id = this.executeAutoKeysQuery(p);

			category.setId(id);
			category.setOrder(order);
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
	 * @see net.jforum.dao.CategoryDAO#setOrderUp(Category, Category)
	 */
	public void setOrderUp(Category category, Category relatedCategory)
	{
		this.setOrder(category, relatedCategory);
	}

	/**
	 * @see net.jforum.dao.CategoryDAO#setOrderDown(Category, Category)
	 */
	public void setOrderDown(Category category, Category relatedCategory)
	{
		this.setOrder(category, relatedCategory);
	}

	/**
	 * @param category Category
	 * @param otherCategory Category
	 */
	private void setOrder(Category category, Category otherCategory)
	{
		int tmpOrder = otherCategory.getOrder();
		otherCategory.setOrder(category.getOrder());
		category.setOrder(tmpOrder);

		PreparedStatement p = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("CategoryModel.setOrderById"));
			p.setInt(1, otherCategory.getOrder());
			p.setInt(2, otherCategory.getId());
			p.executeUpdate();
			p.close();
			p = null;

			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("CategoryModel.setOrderById"));
			p.setInt(1, category.getOrder());
			p.setInt(2, category.getId());
			p.executeUpdate();
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(p);
		}
	}
}
