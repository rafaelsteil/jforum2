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
 * This file creating date: 11.10.2004
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.context;

import net.jforum.util.preferences.ConfigKeys;

/**
 * @author Marc Wick
 * @version $Id: JForumContext.java,v 1.4 2006/09/09 21:32:37 rafaelsteil Exp $
 */
public class JForumContext implements ForumContext
{
	private String contextPath;
	private String servletExtension;
	private RequestContext request;
	private ResponseContext response;
	private boolean isEncodingDisabled;
	private boolean isBot;

	public JForumContext(String contextPath, String servletExtension, 
			RequestContext request, ResponseContext response)
	{
		this.contextPath = contextPath;
		this.servletExtension = servletExtension;
		this.request = request;
		this.response = response;

		Boolean isBotObject = (Boolean) request.getAttribute(ConfigKeys.IS_BOT);
		this.isBot = (isBotObject != null && isBotObject.booleanValue());

		this.isEncodingDisabled = isBot;
	}

	public JForumContext(String contextPath, String servletExtension, 
			RequestContext request, ResponseContext response, boolean isEncodingDisabled)
	{
		this.contextPath = contextPath;
		this.servletExtension = servletExtension;
		this.request = request;
		this.response = response;
		this.isEncodingDisabled = isEncodingDisabled;

		Boolean isBotObject = (Boolean) request.getAttribute(ConfigKeys.IS_BOT);
		this.isBot = (isBotObject != null && isBotObject.booleanValue());
	}

	public boolean isBot()
	{
		return isBot;
	}

	public String encodeURL(String url)
	{
		return this.encodeURL(url, servletExtension);
	}

	public String encodeURL(String url, String extension)
	{
		String ucomplete = contextPath + url + extension;

		if (isEncodingDisabled()) {
			return ucomplete;
		}

		return response.encodeURL(ucomplete);
	}

	public boolean isEncodingDisabled()
	{
		return this.isEncodingDisabled;
	}

	public RequestContext getRequest()
	{
		return request;
	}

	public ResponseContext getResponse()
	{
		return response;
	}
}
