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
 * Created on 29/11/2005 13:25:55
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Properties;

import net.jforum.ConfigLoader;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.log4j.Logger;

/**
 * Try to fix some database configuration problems.
 * This class will much likely do some checks only for mysql.
 * @author Rafael Steil
 * @version $Id: MySQLVersionWorkarounder.java,v 1.1 2007/09/12 14:43:13 rafaelsteil Exp $
 */
public class MySQLVersionWorkarounder
{
	private static Logger logger = Logger.getLogger(MySQLVersionWorkarounder.class);
    private static final String MYSQL_323_DATA_ACCESS_DRIVER = net.jforum.dao.mysql.MySQL323DataAccessDriver.class.getName();
    private static final String MYSQL_DATA_ACCESS_DRIVER = net.jforum.dao.mysql.MysqlDataAccessDriver.class.getName();

    public void handleWorkarounds(Connection c)
	{
		if (c == null) {
			logger.warn("Cannot work with a null connection");
			return;
    	}
    	
    	if (!"mysql".equals(SystemGlobals.getValue(ConfigKeys.DATABASE_DRIVER_NAME))) {
    		return;
    	}
    	
    	try {
    		DatabaseMetaData meta = c.getMetaData();
    		logger.debug("MySQL Version: " + meta.getDatabaseProductVersion());
    		
    		int major = meta.getDatabaseMajorVersion();
    		int minor = meta.getDatabaseMinorVersion();
    		
    		if (major == 3 && minor == 23) {
    			this.handleMySql323();
    		}
    		else if (major == 4 && minor == 0) {
    			this.handleMySql40x();
    		}
    		else if (major > 4 || (major == 4 && minor > 0)) {
    			this.handleMySql41xPlus();
    		}
    	}
    	catch (Exception e) {
    		logger.error(e.toString(), e);
    	}
	}
	
	private void handleMySql323() throws Exception
	{
		this.ensureDaoClassIsCorrect(MYSQL_323_DATA_ACCESS_DRIVER);
		
		Properties p = this.loadSqlQueries();
		
		if (p != null) {
			String[] necessaryKeys = { 
				"PermissionControl.deleteRoleValuesByRoleId",
				"PermissionControl.getRoleIdsByGroup",
				"PermissionControl.getRoles",
				"PermissionControl.getRoleValues"
			};
			
			boolean shouldUpdate = false;
			
			if (p.size() == 0) {
				shouldUpdate = true;
			}
			else {
				for (int i = 0; i < necessaryKeys.length; i++) {
					String key = necessaryKeys[i];
					
					if (p.getProperty(key) == null) {
						shouldUpdate = true;
						break;
					}
				}
			}
			
			if (shouldUpdate) {
				String path = this.buildPath("mysql_323.sql");
				
				FileInputStream fis = new FileInputStream(path);
				
				try {
					p.load(fis);
					this.saveSqlQueries(p);
				}
				finally {
					fis.close();
				}
			}
		}
	}
	
	private void handleMySql40x() throws Exception
	{
		this.ensureDaoClassIsCorrect(MYSQL_DATA_ACCESS_DRIVER);
		
		Properties p = this.loadSqlQueries();
		
		if (p != null) {
			if (p.size() == 0 || p.getProperty("PermissionControl.deleteAllRoleValues") == null) {
				String path = this.buildPath("mysql_40.sql");
				
				FileInputStream fis = new FileInputStream(path);
				
				try {
					p.load(fis);
					this.saveSqlQueries(p);
				}
				finally {
					fis.close();
				}
			}
		}
	}
	
	private void handleMySql41xPlus() throws Exception
	{
		this.ensureDaoClassIsCorrect(MYSQL_DATA_ACCESS_DRIVER);
		
		Properties p = this.loadSqlQueries();
		
		if (p != null && p.size() > 0) {
			this.saveSqlQueries(new Properties());
		}
		
		this.fixEncoding();
	}
	
	private void fixEncoding() throws Exception
	{
		FileInputStream fis = null;
		OutputStream os = null;
		
		try {
			Properties p = new Properties();
			
			File f = new File(SystemGlobals.getValue(ConfigKeys.DATABASE_DRIVER_CONFIG));
			
			if (f.canWrite()) {
				fis = new FileInputStream(f);
				
				p.load(fis);
				
				p.setProperty(ConfigKeys.DATABASE_MYSQL_ENCODING, "");
				p.setProperty(ConfigKeys.DATABASE_MYSQL_UNICODE, "");
				
				os = new FileOutputStream(f);
				p.store(os, null);
			}
		}
		finally {
			if (fis != null) {
				fis.close();
			}
			if (os != null) {
				os.close();
			}
		}
	}
	
	private void ensureDaoClassIsCorrect(String shouldBe) throws Exception
	{
		if (!shouldBe.equals(SystemGlobals.getValue(ConfigKeys.DAO_DRIVER))) {
			logger.info("MySQL DAO class is incorrect. Setting it to " + shouldBe);
			
			this.fixDAODriver(shouldBe);
			
			SystemGlobals.setValue(ConfigKeys.DAO_DRIVER, shouldBe);
			ConfigLoader.loadDaoImplementation();
		}
	}
	
	private Properties loadSqlQueries() throws Exception
	{
		// First, check if we really have a problem
		String sqlQueries = SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_DRIVER);
		
		File f = new File(sqlQueries);
		
		Properties p = new Properties();
		
		FileInputStream fis = new FileInputStream(f);
		
		try {
			p.load(fis);
			
			if (f.canWrite()) {
				return p;
			}
		}
		finally {
			try { fis.close(); } catch (Exception e) {}
		}
		
		logger.warn("Cannot overwrite" + sqlQueries + " file. Insuficient privileges");
		return null;
	}
	
	private void saveSqlQueries(Properties p) throws Exception
	{
		FileOutputStream fos = new FileOutputStream(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_DRIVER));
		
		try {
			p.store(fos, null);
		}
		finally {
			fos.close();
		}

		SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_DRIVER));
	}
	
	private void fixDAODriver(String daoClassName) throws Exception
	{
		String driverConfigPath = SystemGlobals.getValue(ConfigKeys.DATABASE_DRIVER_CONFIG);
		
		File f = new File(driverConfigPath);
		
		if (f.canWrite()) {
			// Fix the DAO class
			Properties p = new Properties();
			
			FileInputStream fis = new FileInputStream(driverConfigPath);
			FileOutputStream fos = null;
			
			try {
				p.load(fis);
				p.setProperty(ConfigKeys.DAO_DRIVER, daoClassName);
				
				fos = new FileOutputStream(driverConfigPath);
				p.store(fos, null);
			}
			finally {
				if (fos != null) {
					fos.close();
				}
                
				fis.close();
			}
		}
		else {
			logger.warn("Cannot overwrite" + driverConfigPath + ". Insuficient privileges");
		}
	}
	
	private String buildPath(String concat)
	{
		return new StringBuffer(256)
			.append(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR))
			.append('/')
			.append("database/mysql/")
			.append(concat)
			.toString();
	}
}
