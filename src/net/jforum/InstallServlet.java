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
 * This file creation date: 27/08/2004 - 18:12:26
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jforum.context.JForumContext;
import net.jforum.context.RequestContext;
import net.jforum.context.ResponseContext;
import net.jforum.context.ForumContext;
import net.jforum.context.web.WebRequestContext;
import net.jforum.context.web.WebResponseContext;
import net.jforum.exceptions.ExceptionWriter;
import net.jforum.repository.ModulesRepository;
import net.jforum.util.I18n;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * @author Rafael Steil
 * @version $Id: InstallServlet.java,v 1.30 2007/10/10 04:54:20 rafaelsteil Exp $
 */
public class InstallServlet extends JForumBaseServlet
{
	/** 
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}
	
	/** 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		try {
			String encoding = SystemGlobals.getValue(ConfigKeys.ENCODING);
			req.setCharacterEncoding(encoding);
			
			// Request
			RequestContext request = new WebRequestContext(req);
			ResponseContext response = new WebResponseContext(res);

			request.setCharacterEncoding(encoding);

            JForumExecutionContext ex = JForumExecutionContext.get();

            ForumContext forumContext = new JForumContext(
                request.getContextPath(),
                SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION),
                request,
                response,
                false
            );

            ex.setForumContext(forumContext);
	
			// Assigns the information to user's thread 
			JForumExecutionContext.set(ex);
			
			// Context
			SimpleHash context = JForumExecutionContext.getTemplateContext();
			context.put("contextPath", req.getContextPath());
			context.put("serverName", req.getServerName());
			context.put("templateName", "default");
			context.put("serverPort", Integer.toString(req.getServerPort()));
			context.put("I18n", I18n.getInstance());
			context.put("encoding", encoding);
			context.put("extension", SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
			context.put("JForumContext", forumContext);
			context.put("version", SystemGlobals.getValue(ConfigKeys.VERSION));
			
			if (SystemGlobals.getBoolValue(ConfigKeys.INSTALLED)) {
				JForumExecutionContext.setRedirect(request.getContextPath() 
					+ "/forums/list" + SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
			}
			else {		
				// Module and Action
				String moduleClass = ModulesRepository.getModuleClass(request.getModule());
				
				context.put("moduleName", request.getModule());
				context.put("action", request.getAction());
				
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), encoding));
				
				try {
					if (moduleClass != null) {
						// Here we go, baby
						Command c = (Command)Class.forName(moduleClass).newInstance();
						Template template = c.process(request, response, context);
			
						if (JForumExecutionContext.getRedirectTo() == null) {
							response.setContentType("text/html; charset=" + encoding);
			
							template.process(context, out);
							out.flush();
						}
					}
				}
				catch (Exception e) {
					response.setContentType("text/html; charset=" + encoding);
					if (out != null) {
						new ExceptionWriter().handleExceptionData(e, out, request);
					}
					else {
						new ExceptionWriter().handleExceptionData(e, 
							new BufferedWriter(new OutputStreamWriter(response.getOutputStream())), request);
					}
				}
			}
			
			String redirectTo = JForumExecutionContext.getRedirectTo();
			
			if (redirectTo != null) {
				response.sendRedirect(response.encodeRedirectURL(redirectTo));
			}
		}
		finally {
			JForumExecutionContext.finish();
		}
	}
}
