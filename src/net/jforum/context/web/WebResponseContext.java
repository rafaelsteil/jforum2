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
 * The JForum Project
 * http://www.jforum.net
 * 20.08.2006 18:52:22
 */
package net.jforum.context.web;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.ServletOutputStream;

import net.jforum.context.ResponseContext;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

/**
 * @author SergeMaslyukov
 * @version $Id: WebResponseContext.java,v 1.3 2007/08/26 17:15:20 rafaelsteil Exp $
 */
public class WebResponseContext implements ResponseContext
{
	private HttpServletResponse response = null;

	public WebResponseContext(HttpServletResponse response)
	{
		this.response = response;
	}

	public void setContentLength(int len)
	{
		response.setContentLength(len);
	}

	public boolean containsHeader(String name)
	{
		return response.containsHeader(name);
	}

	public void setHeader(String name, String value)
	{
		response.setHeader(name, value);
	}

	public void addCookie(Cookie cookie)
	{
		response.addCookie(cookie);
	}

	public String encodeRedirectURL(String url)
	{
		return response.encodeRedirectURL(url);
	}

	public void sendRedirect(String location) throws IOException
	{
		if (SystemGlobals.getBoolValue(ConfigKeys.REDIRECT_ABSOLUTE_PATHS)) {
			URI uri = URI.create(location);
			
			if (!uri.isAbsolute()) {
				location = SystemGlobals.getValue(ConfigKeys.REDIRECT_BASE_URL) + location;
			}
		}
		
		response.sendRedirect(location);
	}

	public String getCharacterEncoding()
	{
		return response.getCharacterEncoding();
	}

	public void setContentType(String type)
	{
		response.setContentType(type);
	}

	public ServletOutputStream getOutputStream() throws IOException
	{
		return response.getOutputStream();
	}

	public PrintWriter getWriter() throws IOException
	{
		return response.getWriter();
	}

	public String encodeURL(String url)
	{
		return response.encodeURL(url);
	}

	public void sendError(int sc) throws IOException
	{
		response.sendError(sc);
	}

	public void addHeader(String name, String value)
	{
		response.addHeader(name, value);
	}
}
