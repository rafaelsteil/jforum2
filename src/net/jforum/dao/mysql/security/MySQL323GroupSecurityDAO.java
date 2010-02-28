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
 * Created on 02/07/2005 13:18:34
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao.mysql.security;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import net.jforum.JForumExecutionContext;
import net.jforum.dao.generic.security.GenericGroupSecurityDAO;
import net.jforum.dao.generic.security.SecurityCommon;
import net.jforum.exceptions.DatabaseException;
import net.jforum.security.RoleCollection;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.SystemGlobals;

/**
 * MySQL 3.23 hacks based on Andy's work
 * 
 * @author Rafael Steil
 * @version $Id: MySQL323GroupSecurityDAO.java,v 1.9 2007/08/16 13:07:33 rafaelsteil Exp $
 */
public class MySQL323GroupSecurityDAO extends GenericGroupSecurityDAO
{	
	/**
	 * @see net.jforum.dao.generic.security.GenericGroupSecurityDAO#loadRoles(int[])
	 */
	protected RoleCollection loadRoles(int[] groupIds)
	{
		String groupIdAsString = SecurityCommon.groupIdAsString(groupIds);

		RoleCollection roleCollection = new RoleCollection();
		
		PreparedStatement rolesP = null;
		PreparedStatement roleValuesP = null;
		ResultSet roles = null;
		ResultSet roleValues = null;
		
		try {
			// Roles
			String sql = this.sqlWithGroups("PermissionControl.getRoles", groupIdAsString);

			rolesP = JForumExecutionContext.getConnection().prepareStatement(sql);
			roles = rolesP.executeQuery();
			
			// RoleValues
			sql = this.sqlWithGroups("PermissionControl.getRoleValues", groupIdAsString);

			roleValuesP = JForumExecutionContext.getConnection().prepareStatement(sql);
			roleValues = roleValuesP.executeQuery();
			
			MySQL323RoleResultSet mergedRs = new MySQL323RoleResultSet(0, 0, null, null);
			mergedRs.merge(roles, roleValues);
			
			roleCollection = SecurityCommon.loadRoles(mergedRs);
		}
		catch (Exception e) {
			throw new DatabaseException(e);
		}
		finally {
			DbUtils.close(roles, rolesP);
			DbUtils.close(roleValues, roleValuesP);
		}
		
		return roleCollection;
	}
	
	private String sqlWithGroups(String queryName, String groups)
	{
		String sql = SystemGlobals.getSql(queryName);
		
		if ("".equals(groups)) {
			// We suppose there is no "negative" group ids
			sql = sql.replaceAll("#IN#", "-1");
		}
		else {
			sql = sql.replaceAll("#IN#", groups);
		}
		
		return sql.replaceAll("#IN#", groups);
	}
	
	/**
	 * @see net.jforum.dao.security.SecurityDAO#deleteAllRoles(int)
	 */
	public void deleteAllRoles(int id)
	{
		PreparedStatement p = null;
		try {
			// First, get the set of role ids
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("PermissionControl.getRoleIdsByGroup"));
			p.setInt(1, id);

			String roleIds = this.getCsvIdList(p);

			p.close();

			if (roleIds.length() > 0) {
				// Then remove all matching values
				p = this.getStatementForCsv(SystemGlobals.getSql("PermissionControl.deleteRoleValuesByRoleId"), roleIds);
				p.executeUpdate();
				p.close();
			}
			
			// Now delete the group roles 
			p = JForumExecutionContext.getConnection().prepareStatement(
					SystemGlobals.getSql("PermissionControl.deleteAllGroupRoles"));
			p.setInt(1, id);
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
	 * Gets a statement to use with some csv data
	 * 
	 * @param sql The SQL query to execute. It must have an "?", which will be replaced by
	 *            <code>csv</code>
	 * @param csv The ids to replace
	 * @return The statement, ready to execute
	 * @throws SQLException
	 */
	protected PreparedStatement getStatementForCsv(String sql, String csv) throws SQLException
	{
		int index = sql.indexOf('?');
		sql = sql.substring(0, index) + csv + sql.substring(index + 1);
		return JForumExecutionContext.getConnection().prepareStatement(sql);
	}

	/**
	 * Gets a set of ids from a statement The statement is expected to return an id in the first
	 * column
	 * 
	 * @param p The statement to execute
	 * @return The ids, separated by comma
	 * @throws SQLException
	 */
	protected String getCsvIdList(PreparedStatement p) throws SQLException
	{
		ResultSet rs = p.executeQuery();

		StringBuffer sb = new StringBuffer();

		while (rs.next()) {
			sb.append(rs.getInt(1)).append(",");
		}

		sb.append("-1");

		rs.close();

		return sb.toString();
	}
	
	private static class MySQL323RoleResultSet extends com.mysql.jdbc.ResultSet
	{
		private Map currentEntry;
		private Iterator dataIterator;
		private List data = new ArrayList();
		
		public MySQL323RoleResultSet(long updateCount, long updateId, Connection connection, Statement statement)
		{
			super(updateCount, updateId, connection, statement);
		}
		
		public void merge(ResultSet roles, ResultSet roleValues) throws SQLException
		{
			this.fillDataFromRs(roles);
			this.fillDataFromRs(roleValues);
			
			this.dataIterator = this.data.iterator();
		}
		
		private void fillDataFromRs(ResultSet rs) throws SQLException
		{
			while (rs.next()) {
				Map m = new HashMap();
				
				m.put("name", rs.getString("name"));
				m.put("role_value", rs.getString("role_value"));
				
				this.data.add(m);
			}
		}
		
		/**
		 * @see com.mysql.jdbc.ResultSet#next()
		 */
		public boolean next() throws SQLException
		{
			boolean hasNext = this.dataIterator.hasNext();
			
			if (hasNext) {
				this.currentEntry = (Map)this.dataIterator.next();
			}
			
			return hasNext;
		}
		
		/**
		 * @see com.mysql.jdbc.ResultSet#getString(java.lang.String)
		 */
		public String getString(String column) throws SQLException
		{
			return (String)this.currentEntry.get(column);
		}
		
		/**
		 * Always returns false
		 */
		public boolean wasNull() throws SQLException
		{
			return false;
		}

		/**
		 * Does nothing
		 */
		public void close() throws SQLException {}
	} 
}
