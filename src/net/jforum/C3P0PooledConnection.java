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
 * Created on 30/11/2005 17:07:51
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum;

import java.lang.reflect.Method;
import java.sql.Connection;

import net.jforum.exceptions.DatabaseException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * @author Rafael Steil
 * @version $Id: C3P0PooledConnection.java,v 1.7 2007/04/12 02:11:52 rafaelsteil Exp $
 */
public class C3P0PooledConnection extends DBConnection
{
	private ComboPooledDataSource ds;
	
	/**
	 * 
	 * @see net.jforum.DBConnection#init()
	 */
	public void init() throws Exception
	{
		this.ds = new ComboPooledDataSource();
		
		this.ds.setDriverClass(SystemGlobals.getValue(ConfigKeys.DATABASE_CONNECTION_DRIVER));
		this.ds.setJdbcUrl(SystemGlobals.getValue(ConfigKeys.DATABASE_CONNECTION_STRING));
		this.ds.setMinPoolSize(SystemGlobals.getIntValue(ConfigKeys.DATABASE_POOL_MIN));
		this.ds.setMaxPoolSize(SystemGlobals.getIntValue(ConfigKeys.DATABASE_POOL_MAX));
		this.ds.setIdleConnectionTestPeriod(SystemGlobals.getIntValue(ConfigKeys.DATABASE_PING_DELAY));
		
		this.extraParams();
	}
	
	private void extraParams()
	{
		String extra = SystemGlobals.getValue(ConfigKeys.C3P0_EXTRA_PARAMS);
		
		if (extra != null && extra.trim().length() > 0) {
			String[] p = extra.split(";");
			
			for (int i = 0; i < p.length; i++) {
				String[] kv = p[i].trim().split("=");
				
				if (kv.length == 2) {
					this.invokeSetter(kv[0], kv[1]);
				}
			}
		}
	}
	
	/**
	 * Huge hack to invoke methods without the need of an external configuration file
	 * and whithout knowing the argument's type
	 */
	private void invokeSetter(String propertyName, String value)
	{
		try {
			String setter = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
			
			Method[] methods = this.ds.getClass().getMethods();
			
			for (int i = 0; i < methods.length; i++) {
				Method method = methods[i];
				
				if (method.getName().equals(setter)) {
					Class[] paramTypes = method.getParameterTypes();
					
					if (paramTypes[0] == String.class) {
						method.invoke(this.ds, new Object[] { value });
					}
					else if (paramTypes[0] == int.class) {
						method.invoke(this.ds, new Object[] { new Integer(value) });
					}
					else if (paramTypes[0] == boolean.class) {
						method.invoke(this.ds, new Object[] { Boolean.valueOf(value) });
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see net.jforum.DBConnection#getConnection()
	 */
	public Connection getConnection()
	{
		try {
			return this.ds.getConnection();
		}
		catch (Exception e) {
			throw new DatabaseException(e);
		}
	}

	/**
	 * @see net.jforum.DBConnection#releaseConnection(java.sql.Connection)
	 */
	public void releaseConnection(Connection conn)
	{
        if (conn==null) {
            return;
        }

        try {
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see net.jforum.DBConnection#realReleaseAllConnections()
	 */
	public void realReleaseAllConnections() throws Exception
	{
		DataSources.destroy(this.ds);
	}
}
