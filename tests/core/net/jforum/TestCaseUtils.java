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
 * This file creation date: 29/09/2004 - 18:16:46
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum;

import java.io.File;
import java.io.IOException;

import net.jforum.dao.DataAccessDriver;
import net.jforum.util.I18n;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import freemarker.template.Configuration;

/**
 * General utilities for the test cases.
 * 
 * @author Rafael Steil
 * @version $Id: TestCaseUtils.java,v 1.23 2007/09/19 05:43:23 rafaelsteil Exp $
 */
public class TestCaseUtils
{
	private static TestCaseUtils utils = new TestCaseUtils();
	private String rootDir;
	
	private TestCaseUtils() {}
	
	public static void loadEnvironment() throws Exception
	{
		utils.init();
	}
	
	/**
	 * Inits the database stuff. 
	 * Must be called <b>after</b> #loadEnvironment
	 * 
	 * @throws Exception
	 */
	public static void initDatabaseImplementation() throws Exception
	{
		SystemGlobals.loadAdditionalDefaults(SystemGlobals.getValue(ConfigKeys.DATABASE_DRIVER_CONFIG));
		
		SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_GENERIC));
        SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_DRIVER));
        
        // Start the dao.driver implementation
        String driver = SystemGlobals.getValue(ConfigKeys.DAO_DRIVER);
        Class c = Class.forName(driver);
        DataAccessDriver d = (DataAccessDriver)c.newInstance();
        DataAccessDriver.init(d);
        
        DBConnection.createInstance();
		DBConnection.getImplementation().init();
	}
	
	public static String getRootDir()
	{
		if (utils.rootDir == null) {
			utils.rootDir = utils.getClass().getResource("/").getPath();
			utils.rootDir = utils.rootDir.substring(0, utils.rootDir.length() - "/tests/bin".length());
		}
		
		return utils.rootDir;
	}
	
	private void init() throws IOException 
	{
		getRootDir();
		SystemGlobals.initGlobals(this.rootDir, this.rootDir
			+ "/WEB-INF/config/SystemGlobals.properties");
		
		if (new File(SystemGlobals.getValue(ConfigKeys.INSTALLATION_CONFIG)).exists()) {
        	SystemGlobals.loadAdditionalDefaults(SystemGlobals.getValue(ConfigKeys.INSTALLATION_CONFIG));
        }
		
		// Configure the template engine
        Configuration templateCfg = new Configuration();
        templateCfg.setDirectoryForTemplateLoading(new File(SystemGlobals.getApplicationPath()
        	+ "/templates"));
        templateCfg.setTemplateUpdateDelay(0);
        JForumExecutionContext.setTemplateConfig(templateCfg);

		I18n.load();
	}
}
