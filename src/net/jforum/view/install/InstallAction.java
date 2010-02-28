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
 * This file creation date: 27/08/2004 - 18:15:54
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.install;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import net.jforum.Command;
import net.jforum.ConfigLoader;
import net.jforum.DBConnection;
import net.jforum.DataSourceConnection;
import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.SimpleConnection;
import net.jforum.context.RequestContext;
import net.jforum.context.ResponseContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ForumDAO;
import net.jforum.dao.PostDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.entities.Post;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.entities.UserSession;
import net.jforum.exceptions.DatabaseException;
import net.jforum.exceptions.ForumException;
import net.jforum.util.DbUtils;
import net.jforum.util.FileMonitor;
import net.jforum.util.I18n;
import net.jforum.util.MD5;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.SystemGlobalsListener;
import net.jforum.util.preferences.TemplateKeys;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * JForum Web Installer.
 * 
 * @author Rafael Steil
 * @version $Id: InstallAction.java,v 1.79 2007/10/08 17:34:40 rafaelsteil Exp $
 */
public class InstallAction extends Command
{
	private static Logger logger = Logger.getLogger(InstallAction.class);

    private static final String POOLED_CONNECTION = net.jforum.PooledConnection.class.getName();
    private static final String SIMPLE_CONNECTION = net.jforum.SimpleConnection.class.getName();
    private static final String DATASOURCE_CONNECTION = net.jforum.DataSourceConnection.class.getName();

    public void welcome()
	{
		this.checkLanguage();
		
		this.context.put("language", this.getFromSession("language"));
		this.context.put("database", this.getFromSession("database"));
		this.context.put("dbhost", this.getFromSession("dbHost"));
		this.context.put("dbuser", this.getFromSession("dbUser"));
		this.context.put("dbname", this.getFromSession("dbName"));
		this.context.put("dbport", this.getFromSession("dbPort"));
		this.context.put("dbpasswd", this.getFromSession("dbPassword"));
		this.context.put("dbencoding", this.getFromSession("dbEncoding"));
		this.context.put("use_pool", this.getFromSession("usePool"));
		this.context.put("forumLink", this.getFromSession("forumLink"));
		this.context.put("siteLink", this.getFromSession("siteLink"));
		this.context.put("dbdatasource", this.getFromSession("dbdatasource"));
		
		this.setTemplateName(TemplateKeys.INSTALL_WELCOME);
	}
	
	private void checkLanguage()
	{
		String lang = this.request.getParameter("l");
		
		if (lang == null) {
			Locale locale = this.request.getLocale();
			lang = locale.getLanguage() + "_" + locale.getCountry();
		}
		
		if (!I18n.languageExists(lang)) {
			return;
		}
		I18n.load(lang);
		
		UserSession us = new UserSession();
		us.setLang(lang);
		
		SessionFacade.add(us);
		this.addToSessionAndContext("language", lang);
	}
	
	private String getFromSession(String key)
	{
		return (String)this.request.getSessionContext().getAttribute(key);
	}
	
	private void error()
	{
		this.setTemplateName(TemplateKeys.INSTALL_ERROR);
	}
	
	public void doInstall()
	{
		if (!this.checkForWritableDir()) {
			return;
		}
		
		this.removeUserConfig();
		
		Connection conn = null;

		if (!"passed".equals(this.getFromSession("configureDatabase"))) {
			logger.info("Going to configure the database...");
			
			conn = this.configureDatabase();
			
			if (conn == null) {
				this.context.put("message", I18n.getMessage("Install.databaseError"));
				this.error();
				return;
			}
		}
		
		logger.info("Database configuration ok");

		// Database Configuration is ok
		this.addToSessionAndContext("configureDatabase", "passed");
		
		DBConnection simpleConnection = new SimpleConnection();
		
		if (conn == null) {
			conn = simpleConnection.getConnection();
		}
		
		boolean dbError = false;
		
		try {
			//this.setupAutoCommit(conn);
			
			if (!"passed".equals(this.getFromSession("createTables")) && !this.createTables(conn)) {
				this.context.put("message", I18n.getMessage("Install.createTablesError"));
				dbError = true;
				this.error();
				return;
			}
			
			// Create tables is ok
			this.addToSessionAndContext("createTables", "passed");
			logger.info("Table creation is ok");
			this.setupAutoCommit(conn); 
	        if (!"passed".equals(this.getFromSession("importTablesData")) && !this.importTablesData(conn)) {
				this.context.put("message", I18n.getMessage("Install.importTablesDataError"));
				dbError = true;
				this.error();
				return;
			}
			
			// Dump is ok
			this.addToSessionAndContext("importTablesData", "passed");
			
			if (!this.updateAdminPassword(conn)) {
				this.context.put("message", I18n.getMessage("Install.updateAdminError"));
				dbError = true;
				this.error();
				return;
			}
			
			this.storeSupportProjectMessage(conn);
		}
		finally {
			if (conn != null) {
				try {
					if (dbError) {
						conn.rollback();
					}
					else {
						conn.commit();
					}
				}
				catch (SQLException e) { }
				
				simpleConnection.releaseConnection(conn);
			}
		}

		JForumExecutionContext.setRedirect(this.request.getContextPath() + "/install/install"
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION)
			+ "?module=install&action=finished");
	}
	
	private void setupAutoCommit(Connection conn)
	{
		try {
			conn.setAutoCommit(false);
		}
		catch (SQLException e) {
			throw new DatabaseException(e);
		}
	}
	
	private void removeUserConfig()
	{
		File f = new File(SystemGlobals.getValue(ConfigKeys.INSTALLATION_CONFIG));
		
		if (f.exists() && f.canWrite()) {
			try {
				f.delete();
			}
			catch (Exception e) {
				logger.info(e.toString());
			}
		}
	}
	
	public void finished() 
	{
		this.setTemplateName(TemplateKeys.INSTALL_FINISHED);

		this.context.put("clickHere", I18n.getMessage("Install.clickHere"));
		this.context.put("forumLink", this.getFromSession("forumLink"));

		String lang = this.getFromSession("language");

		if (lang == null) {
			lang = "en_US";
		}

		this.context.put("lang", lang);

		this.fixModulesMapping();
		this.configureSystemGlobals();

		SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_GENERIC));
		SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_DRIVER));
		
		SessionFacade.remove(this.request.getSessionContext().getId());
	}
	
	private void fixModulesMapping()
	{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			// Modules Mapping
			String modulesMapping = SystemGlobals.getValue(ConfigKeys.CONFIG_DIR) + "/modulesMapping.properties";
			
			if (new File(modulesMapping).canWrite()) {
				Properties p = new Properties();
				fis = new FileInputStream(modulesMapping);
				p.load(fis);
				
				if (p.containsKey("install")) {
					p.remove("install");

					fos = new FileOutputStream(modulesMapping);
					
					p.store(fos, "Modified by JForum Installer");
					ConfigLoader.loadModulesMapping(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR));
				}
				
				this.addToSessionAndContext("mappingFixed", "true");
			}
		}
		catch (Exception e) {
			logger.warn("Error while working on modulesMapping.properties: " + e);
		}
		finally {
			if (fis != null) {
				try { fis.close(); } catch (Exception e) {}
			}
			
			if (fos != null) {
				try { fos.close(); } catch (Exception e) {}
			}
		}
	}
	
	private void configureSystemGlobals()
	{
		SystemGlobals.setValue(ConfigKeys.USER_HASH_SEQUENCE, MD5.crypt(this.getFromSession("dbPassword")
			+ System.currentTimeMillis()));

		SystemGlobals.setValue(ConfigKeys.FORUM_LINK, this.getFromSession("forumLink"));
		SystemGlobals.setValue(ConfigKeys.HOMEPAGE_LINK, this.getFromSession("siteLink"));
		SystemGlobals.setValue(ConfigKeys.I18N_DEFAULT, this.getFromSession("language"));
		SystemGlobals.setValue(ConfigKeys.INSTALLED, "true");
		
		SystemGlobals.saveInstallation();
		
		this.restartSystemGlobals();
	}
	
	private boolean importTablesData(Connection conn)
    {
        try
        {
            boolean status = true;
            boolean autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            String dbType = this.getFromSession("database");

            List statements = ParseDBDumpFile.parse(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR)
                + "/database/"
                + dbType
                + "/" + dbType + "_data_dump.sql");

            for (Iterator iter = statements.iterator(); iter.hasNext();) {
                String query = (String)iter.next();

                if (query == null || "".equals(query.trim())) {
                    continue;
                }

                query = query.trim();

                Statement s = conn.createStatement();

                try {
                    if (query.startsWith("UPDATE") || query.startsWith("INSERT") || query.startsWith("SET")) {
                        s.executeUpdate(query);
                    }
                    else if (query.startsWith("SELECT")) {
                        s.executeQuery(query);
                    }
                    else {
                        throw new SQLException("Invalid query: " + query);
                    }
                }
                catch (SQLException ex) {
                    status = false;
                    conn.rollback();
                    logger.error("Error importing data for " + query + ": " + ex, ex);
                    this.context.put("exceptionMessage", ex.getMessage() + "\n" + query);
                    break;
                }
                finally {
                    s.close();
                }
            }

            conn.setAutoCommit(autoCommit);
            return status;
        }
        catch (Exception e)
        {
            throw new ForumException(e);
        }
    }
	
	private boolean createTables(Connection conn)
	{
           
		logger.info("Going to create tables...");
		String dbType = this.getFromSession("database");

		if ("postgresql".equals(dbType) || "oracle".equals(dbType)) {
			// This should be in a separate transaction block; otherwise, an empty database will fail.
			this.dropOracleOrPostgreSQLTables(dbType, conn);
		}
		try { 
			boolean status = true;
			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);

			List statements = ParseDBStructFile.parse(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR)
					+ "/database/"
					+ dbType
					+ "/" + dbType + "_db_struct.sql");


			for (Iterator iter = statements.iterator(); iter.hasNext(); ) {
				String query = (String)iter.next();

				if (query == null || "".equals(query.trim())) {
					continue;
				}

				Statement s = null;

				try {
					s = conn.createStatement();
					s.executeUpdate(query);
				}
				catch (SQLException ex) {
					status = false;

					logger.error("Error executing query: " + query + ": " + ex, ex);
					this.context.put("exceptionMessage", ex.getMessage() + "\n" + query);

					break;
				}
				finally {
					DbUtils.close(s);
				}
			}
			conn.setAutoCommit(autoCommit);
			return status;
		}
		catch (Exception e)
		{
			throw new ForumException(e);
		}
	}
	
	private void dropOracleOrPostgreSQLTables(String dbName, Connection conn)
	{
		Statement s = null;
		
		try {
			boolean autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			
			List statements = ParseDBStructFile.parse(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR)
				+ "/database/" + dbName + "/" + dbName + "_drop_tables.sql");
			
			this.setupAutoCommit(conn);
			for (Iterator iter = statements.iterator(); iter.hasNext(); ) {
				try {
					String query = (String)iter.next();
					
					if (query == null || "".equals(query.trim())) {
						continue;
					}
					
					s = conn.createStatement();
					s.executeUpdate(query);
					s.close();
                }
				catch (Exception e) {
					logger.error("IGNORE: " + e.toString());
				}
			}
			conn.setAutoCommit(autoCommit);
		}
		catch (Exception e) {
			logger.error(e.toString(), e);
		}
		finally {
            DbUtils.close(s);
		}
	}
	
	private boolean checkForWritableDir()
	{
		boolean canWriteToWebInf = this.canWriteToWebInf();
		boolean canWriteToLuceneIndex = this.canWriteToLuceneIndex();
		
		if (!canWriteToWebInf || !canWriteToLuceneIndex) {
			if (!canWriteToWebInf) {
				this.context.put("message", I18n.getMessage("Install.noWritePermission"));
			}
			else if (!canWriteToLuceneIndex) {
				this.context.put("message", I18n.getMessage("Install.noWritePermissionLucene", 
					new Object[] { SystemGlobals.getValue(ConfigKeys.LUCENE_INDEX_WRITE_PATH) } ));
			}
			
			this.context.put("tryAgain", true);
			this.error();
			return false;
		}

		return true;
	}
	
	private boolean canWriteToWebInf()
	{
		return new File(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR) + "/modulesMapping.properties").canWrite();
	}
	
	private boolean canWriteToLuceneIndex()
	{
		File file = new File(SystemGlobals.getValue(ConfigKeys.LUCENE_INDEX_WRITE_PATH));
		
		if (!file.exists()) {
			return file.mkdir();
		}
		
		return file.canWrite();
	}
	
	private void handleDatabasePort(Properties p, String port)
	{
		String portKey = ":${database.connection.port}";
		String connectionString = p.getProperty(ConfigKeys.DATABASE_CONNECTION_STRING);
		
		if (port == null || port.trim().length() == 0) {
			int index = connectionString.indexOf(portKey);
			
			if (index > -1) {
				if (connectionString.charAt(index - 1) == '\\') {
					connectionString = connectionString.replaceAll("\\" + portKey, "");
				}
				else {
					connectionString = connectionString.replaceAll(portKey, "");
				}
			}
		}
		else if (connectionString.indexOf(portKey) == -1) {
			String hostKey = "${database.connection.host}";
			connectionString = StringUtils.replace(connectionString, hostKey, hostKey + portKey);
		}
		
		p.setProperty(ConfigKeys.DATABASE_CONNECTION_STRING, connectionString);
	}
	
	private void configureJDBCConnection()
	{
		String username = this.getFromSession("dbUser");
		String password = this.getFromSession("dbPassword");
		String dbName = this.getFromSession("dbName");
		String host = this.getFromSession("dbHost");
		String type = this.getFromSession("database");
		String encoding = this.getFromSession("dbEncoding");
		String port = this.getFromSession("dbPort");
		
		String dbConfigFilePath = SystemGlobals.getValue(ConfigKeys.CONFIG_DIR) 
			+ "/database/" + type + "/" + type + ".properties";
		
		Properties p = new Properties();
		FileInputStream fis = null;
		
		try {
            fis = new FileInputStream(dbConfigFilePath);
			p.load(fis);
        }
        catch (IOException e) {
            throw new ForumException(e);
        }
        finally {
        	if (fis != null) {
        		try { fis.close(); } catch (Exception e) {}
        	}
        }

        this.handleDatabasePort(p, port);
		
		// Write database information to the respective file
		p.setProperty(ConfigKeys.DATABASE_CONNECTION_HOST, host);
		p.setProperty(ConfigKeys.DATABASE_CONNECTION_USERNAME, username);
		p.setProperty(ConfigKeys.DATABASE_CONNECTION_PASSWORD, password);
		p.setProperty(ConfigKeys.DATABASE_CONNECTION_DBNAME, dbName);
		p.setProperty(ConfigKeys.DATABASE_CONNECTION_ENCODING, encoding);
		p.setProperty(ConfigKeys.DATABASE_CONNECTION_PORT, port);
		p.setProperty(ConfigKeys.DATABASE_DRIVER_NAME, type);

		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(dbConfigFilePath);
			p.store(fos, null);
		}
		catch (Exception e) {
			logger.warn("Error while trying to write to " + type + ".properties: " + e);
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
				}
				catch (IOException e) { }
			}
		}
		
		// Proceed to SystemGlobals / jforum-custom.conf configuration
		for (Enumeration e = p.keys(); e.hasMoreElements(); ) {
			String key = (String)e.nextElement();
			String value = p.getProperty(key);
			
			SystemGlobals.setValue(key, value);
			
			logger.info("Updating key " + key + " with value " + value);
		}
	}
	
	private Connection configureDatabase()
	{
		String database = this.getFromSession("database");
		String connectionType = this.getFromSession("db_connection_type");
		String implementation;
		
		boolean isDatasource = false;
		
		if ("JDBC".equals(connectionType)) {
			implementation = "yes".equals(this.getFromSession("usePool")) && !"hsqldb".equals(database) 
				? POOLED_CONNECTION
                : SIMPLE_CONNECTION;
			
			this.configureJDBCConnection();
		}
		else {
			isDatasource = true;
			implementation = DATASOURCE_CONNECTION;
			SystemGlobals.setValue(ConfigKeys.DATABASE_DATASOURCE_NAME, this.getFromSession("dbdatasource"));
		}
		
		SystemGlobals.setValue(ConfigKeys.DATABASE_CONNECTION_IMPLEMENTATION, implementation);
		SystemGlobals.setValue(ConfigKeys.DATABASE_DRIVER_NAME, database);
		
		SystemGlobals.saveInstallation();
		this.restartSystemGlobals();
		
		int fileChangesDelay = SystemGlobals.getIntValue(ConfigKeys.FILECHANGES_DELAY);
		
		if (fileChangesDelay > 0) {
			FileMonitor.getInstance().addFileChangeListener(new SystemGlobalsListener(),
				SystemGlobals.getValue(ConfigKeys.INSTALLATION_CONFIG), fileChangesDelay);
		}
		
		Connection conn;
		try {
			DBConnection s;
			
			if (!isDatasource) { 
				s = new SimpleConnection();
			}
			else {
				s =  new DataSourceConnection();
			}
			
			s.init();
			
			conn = s.getConnection();
		}
		catch (Exception e) {
			logger.warn("Error while trying to get a connection: " + e);
			this.context.put("exceptionMessage", e.getMessage());
			return null;
		}
		
		return conn;
	}
	
	private void restartSystemGlobals() 
	{
		String appPath = SystemGlobals.getApplicationPath();
		
		SystemGlobals.reset();
		
		ConfigLoader.startSystemglobals(appPath);
	}
	
	private boolean updateAdminPassword(Connection conn)
	{
		logger.info("Going to update the administrator's password");
		
		boolean status = false;
		
        PreparedStatement p = null;
        
		try {
            p = conn.prepareStatement("UPDATE jforum_users SET user_password = ? WHERE username = 'Admin'");
            p.setString(1, MD5.crypt(this.getFromSession("adminPassword")));
			p.executeUpdate();
			status = true;
		}
		catch (Exception e) {
			logger.warn("Error while trying to update the administrator's password: " + e);
			this.context.put("exceptionMessage", e.getMessage());
		}
        finally {
            DbUtils.close(p);
        }

        return status;
	}
	
	public void checkInformation() 
	{
		this.setTemplateName(TemplateKeys.INSTALL_CHECK_INFO);
		
		String language = this.request.getParameter("language");
		String database = this.request.getParameter("database");
		String dbHost = this.request.getParameter("dbhost");
		String dbPort = this.request.getParameter("dbport");
		String dbUser = this.request.getParameter("dbuser");
		String dbName = this.request.getParameter("dbname");
		String dbPassword = this.request.getParameter("dbpasswd");
		String dbEncoding = this.request.getParameter("dbencoding");
		String dbEncodingOther = this.request.getParameter("dbencoding_other");
		String usePool = this.request.getParameter("use_pool");
		String forumLink = this.request.getParameter("forum_link");
		String adminPassword = this.request.getParameter("admin_pass1");
		
		dbHost = this.notNullDefault(dbHost, "localhost");
		dbEncodingOther = this.notNullDefault(dbEncodingOther, "utf-8");
		dbEncoding = this.notNullDefault(dbEncoding, dbEncodingOther);
		forumLink = this.notNullDefault(forumLink, "http://localhost");
		dbName = this.notNullDefault(dbName, "jforum");
		
		if ("hsqldb".equals(database)) {
			dbUser = this.notNullDefault(dbUser, "sa");
		}
		
		this.addToSessionAndContext("language", language);
		this.addToSessionAndContext("database", database);
		this.addToSessionAndContext("dbHost", dbHost);
		this.addToSessionAndContext("dbPort", dbPort);
		this.addToSessionAndContext("dbUser", dbUser);
		this.addToSessionAndContext("dbName", dbName);
		this.addToSessionAndContext("dbPassword", dbPassword);
		this.addToSessionAndContext("dbEncoding", dbEncoding);
		this.addToSessionAndContext("usePool", usePool);
		this.addToSessionAndContext("forumLink", forumLink);
		this.addToSessionAndContext("siteLink", this.request.getParameter("site_link"));
		this.addToSessionAndContext("adminPassword", adminPassword);
		this.addToSessionAndContext("dbdatasource", this.request.getParameter("dbdatasource"));
		this.addToSessionAndContext("db_connection_type", this.request.getParameter("db_connection_type"));
		
		this.addToSessionAndContext("configureDatabase", null);
		this.addToSessionAndContext("createTables", null);
		this.addToSessionAndContext("importTablesData", null);
		
		this.context.put("canWriteToWebInf", this.canWriteToWebInf());		
		this.context.put("moduleAction", "install_check_info.htm");
	}
	
	private void addToSessionAndContext(String key, String value)
	{
		this.request.getSessionContext().setAttribute(key, value);
		this.context.put(key, value);
	}
	
	private String notNullDefault(String value, String useDefault)
	{
		if (value == null || value.trim().equals("")) {
			return useDefault;
		}
		
		return value;
	}
	
	private void storeSupportProjectMessage(Connection connection)
	{
		StringBuffer message = new StringBuffer("[color=#3AA315][size=18][b]Support JForum - Help the project[/b][/size][/color]")
			.append("<hr>")
			.append("This project is Open Source, and maintained by at least one full time Senior Developer, [i]which costs US$ 3,000.00 / month[/i]. ")
			.append("If it helped you, please consider helping this project - especially with some [b][url=http://www.jforum.net/contribute.jsp]donation[/url][/b].")
			.append('\n')
			.append('\n')
			.append("[color=#137C9F][size=14][b]Why supporting this project is a good thing[/b][/size][/color]")
			.append("<hr>")
			.append("The JForum Project started four years ago as a completely free and Open Source program, initially entirely developed on my (Rafael Steil) ")
			.append("free time. Today, with the help of some very valuable people, I can spend more time on JForum, to improve it and implement new features ")
			.append("(lots of things, requested either on the [url=http://www.jforum.net/forums/list.page]forums[/url] or registered in the ")
			.append("[url=http://www.jforum.net/jira]bug tracker[/url]).")
			.append('\n')
			.append("That's why I'm asking you to financially support this work. I love Open Source. I love to use good products without having to pay for it too. ")
			.append("But when I see some program that is valuable to my work, that helps me making money, I think it's a good idea to support this project.")
			.append('\n')
			.append('\n')
			.append("[b]Some reasons to support open projects[/b]:")
			.append("<ul><li>Because Open Source is cool? Yes")
			.append("<li>To thank for a great tool? Yes")
			.append("<li>To help the project evolve because this will help my work and my earnings? Yes</ul>")
			.append("Also, as the project grows more and more, it would be great to, sometimes, reward some of the great people who help JForum.")
			.append('\n')
			.append('\n')
			.append("So, that's what I'm asking you: if JForum helps your work, saves your time (time is money, remember?) and increase your earnings, support ")
			.append("this project. The simpler way is to make [url=http://www.jforum.net/contribute.jsp]any donation[/url] via PayPal.")
			.append('\n')
			.append('\n')
			.append("JForum has grown a lot every day, since four years ago, which is a great thing, and initially it wasn't my intention to fully work on this tool. ")
			.append("Lately, I'm spending a lot of time on it, specially to make JForum 3 a reality, to help users, to improve the program, to research about ")
			.append("better solutions. So, your support is very welcome!")
			.append('\n')
			.append('\n')
			.append("Thanks!") 
			.append('\n')
			.append('\n')
			.append(":arrow: [size=16][b][url=http://www.jforum.net/contribute.jsp]Click here[/url][/b] to go to the [i][b][url=http://www.jforum.net/contribute.jsp]")
			.append("\"Support JForum\"[/url][/b][/i] page.[/size]")
			.append('\n')
			.append('\n');
		
		try {
			ConfigLoader.createLoginAuthenticator();
			ConfigLoader.loadDaoImplementation();
			
			SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_GENERIC));
			SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_DRIVER));
			
			SystemGlobals.setValue(ConfigKeys.SEARCH_INDEXING_ENABLED, "false");
			
			JForumExecutionContext ex = JForumExecutionContext.get();
			ex.setConnection(connection);
			JForumExecutionContext.set(ex);
			
			User user = new User(2);
			
			// Create topic
			Topic topic = new Topic();
			topic.setPostedBy(user);
			topic.setTitle("Support JForum - Please read");
			topic.setTime(new Date());
			topic.setType(Topic.TYPE_ANNOUNCE);
			topic.setForumId(1);
			
			TopicDAO topicDao = DataAccessDriver.getInstance().newTopicDAO();
			topicDao.addNew(topic);
			
			// Create post
			Post post = new Post();
			post.setSubject(topic.getTitle());
			post.setTime(topic.getTime());
			post.setUserId(user.getId());
			post.setText(message.toString());
			post.setForumId(topic.getForumId());
			post.setSmiliesEnabled(true);
			post.setHtmlEnabled(true);
			post.setBbCodeEnabled(true);
			post.setUserIp("127.0.0.1");
			post.setTopicId(topic.getId());
			
			PostDAO postDao = DataAccessDriver.getInstance().newPostDAO();
			postDao.addNew(post);
			
			// Update topic
			topic.setFirstPostId(post.getId());
			topic.setLastPostId(post.getId());
			
			topicDao.update(topic);
			
			// Update forum stats
			ForumDAO forumDao = DataAccessDriver.getInstance().newForumDAO();
			forumDao.incrementTotalTopics(1, 1);
			forumDao.setLastPost(1, post.getId());
		}
		finally {
			SystemGlobals.setValue(ConfigKeys.SEARCH_INDEXING_ENABLED, "true");
			
			JForumExecutionContext ex = JForumExecutionContext.get();
			ex.setConnection(null);
			JForumExecutionContext.set(ex);
		}
	}
	
	/** 
	 * @see net.jforum.Command#list()
	 */
	public void list()
	{
		this.welcome();
	}
	
	/** 
	 * @see net.jforum.Command#process(net.jforum.context.RequestContext, net.jforum.context.ResponseContext, freemarker.template.SimpleHash) 
     * @param request AWebContextRequest     
     * @param response HttpServletResponse
     * @param context SimpleHash
	 */
	public Template process(RequestContext request,
			ResponseContext response,
			SimpleHash context)  
	{
		this.setTemplateName("default/empty.htm");
		return super.process(request, response, context);
	}
}
