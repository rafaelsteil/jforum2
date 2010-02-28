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
 * This file creation date: 27/08/2004 - 18:22:10
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum;

import java.io.File;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.jforum.exceptions.ForumStartupException;
import net.jforum.repository.BBCodeRepository;
import net.jforum.repository.ModulesRepository;
import net.jforum.repository.Tpl;
import net.jforum.util.I18n;
import net.jforum.util.bbcode.BBCodeHandler;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

/**
 * @author Rafael Steil
 * @version $Id: JForumBaseServlet.java,v 1.24 2007/09/21 15:54:31 rafaelsteil Exp $
 */
public class JForumBaseServlet extends HttpServlet
{
	private static Logger logger = Logger.getLogger(JForumBaseServlet.class);

	protected boolean debug;

	protected void startApplication()
	{
		try {
			SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_GENERIC));
			SystemGlobals.loadQueries(SystemGlobals.getValue(ConfigKeys.SQL_QUERIES_DRIVER));
			
			String filename = SystemGlobals.getValue(ConfigKeys.QUARTZ_CONFIG);
			SystemGlobals.loadAdditionalDefaults(filename);

			ConfigLoader.createLoginAuthenticator();
			ConfigLoader.loadDaoImplementation();
			ConfigLoader.listenForChanges();
			ConfigLoader.startSearchIndexer();
			ConfigLoader.startSummaryJob();
		}
		catch (Exception e) {
			throw new ForumStartupException("Error while starting JForum", e);
		}
	}

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);

		try {
			String appPath = config.getServletContext().getRealPath("");
			debug = "true".equals(config.getInitParameter("development"));

			DOMConfigurator.configure(appPath + "/WEB-INF/log4j.xml");

			logger.info("Starting JForum. Debug mode is " + debug);

			ConfigLoader.startSystemglobals(appPath);
			ConfigLoader.startCacheEngine();

			// Configure the template engine
			Configuration templateCfg = new Configuration();
			templateCfg.setTemplateUpdateDelay(2);
			templateCfg.setSetting("number_format", "#");
			templateCfg.setSharedVariable("startupTime", new Long(new Date().getTime()));

			// Create the default template loader
			String defaultPath = SystemGlobals.getApplicationPath() + "/templates";
			FileTemplateLoader defaultLoader = new FileTemplateLoader(new File(defaultPath));

			String extraTemplatePath = SystemGlobals.getValue(ConfigKeys.FREEMARKER_EXTRA_TEMPLATE_PATH);
			
			if (StringUtils.isNotBlank(extraTemplatePath)) {
				// An extra template path is configured, we need a MultiTemplateLoader
				FileTemplateLoader extraLoader = new FileTemplateLoader(new File(extraTemplatePath));
				TemplateLoader[] loaders = new TemplateLoader[] { extraLoader, defaultLoader };
				MultiTemplateLoader multiLoader = new MultiTemplateLoader(loaders);
				templateCfg.setTemplateLoader(multiLoader);
			} 
			else {
				// An extra template path is not configured, we only need the default loader
				templateCfg.setTemplateLoader(defaultLoader);
			}

			ModulesRepository.init(SystemGlobals.getValue(ConfigKeys.CONFIG_DIR));

			this.loadConfigStuff();

			if (!this.debug) {
				templateCfg.setTemplateUpdateDelay(3600);
			}

			JForumExecutionContext.setTemplateConfig(templateCfg);
		}
		catch (Exception e) {
			throw new ForumStartupException("Error while starting JForum", e);
		}
	}

	protected void loadConfigStuff()
	{
		ConfigLoader.loadUrlPatterns();
		I18n.load();
		Tpl.load(SystemGlobals.getValue(ConfigKeys.TEMPLATES_MAPPING));

		// BB Code
		BBCodeRepository.setBBCollection(new BBCodeHandler().parse());
	}
}
