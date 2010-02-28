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
 * This file creation date: 02/04/2004 - 20:31:35
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum.common;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import net.jforum.JForumExecutionContext;
import net.jforum.context.RequestContext;
import net.jforum.entities.User;
import net.jforum.exceptions.ForumException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import freemarker.template.SimpleHash;

/**
 * @author Rafael Steil
 * @version $Id: ViewCommon.java,v 1.32 2007/09/20 16:07:08 rafaelsteil Exp $
 */
public final class ViewCommon
{
	/**
	 * Prepared the user context to use data pagination. 
	 * The following variables are set to the context:
	 * <p>
	 * 	<ul>
	 * 		<li> <i>totalPages</i> - total number of pages
	 * 		<li> <i>recordsPerPage</i> - how many records will be shown on each page
	 * 		<li> <i>totalRecords</i> - number of records fount
	 * 		<li> <i>thisPage</i> - the current page being shown
	 * 		<li> <i>start</i> - 
	 * 	</ul>
	 * </p>
	 * @param start int
	 * @param totalRecords  int
	 * @param recordsPerPage int
	 */
	public static void contextToPagination(int start, int totalRecords, int recordsPerPage)
	{
		SimpleHash context = JForumExecutionContext.getTemplateContext();
		
		context.put("totalPages", new Double(Math.ceil((double) totalRecords / (double) recordsPerPage)));
		context.put("recordsPerPage", new Integer(recordsPerPage));
		context.put("totalRecords", new Integer(totalRecords));
		context.put("thisPage", new Double(Math.ceil((double) (start + 1) / (double) recordsPerPage)));
		context.put("start", new Integer(start));
	}
	
	/**
	 * Prepares the template context to show the login page, using the current URI as return path.
	 * @return TemplateKeys.USER_LOGIN
	 */
	public static String contextToLogin() 
	{
		RequestContext request = JForumExecutionContext.getRequest();
		
		String uri = request.getRequestURI();
		String query = request.getQueryString();
		String returnPath = query == null ? uri : uri + "?" + query;
		
		return contextToLogin(returnPath);
	}
	
	/**
	 * Prepares the template context to show the login page, using "returnPath" as return path
	 * @param returnPath the URI to use as return path
	 * @return TemplateKeys.USER_LOGIN
	 */
	public static String contextToLogin(String returnPath)
	{
		JForumExecutionContext.getTemplateContext().put("returnPath", returnPath);
		
		if (ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
			String redirect = SystemGlobals.getValue(ConfigKeys.SSO_REDIRECT);
			
			if (!StringUtils.isEmpty(redirect)) {
				URI redirectUri = URI.create(redirect);
				
				if (!redirectUri.isAbsolute()) {
					throw new ForumException("SSO redirect URL should start with a scheme");
				}
				
				try {
					returnPath = URLEncoder.encode( ViewCommon.getForumLink() + returnPath, "UTF-8");
				}
				catch (UnsupportedEncodingException e) {}
				
				if (redirect.indexOf('?') == -1) {
					redirect += "?";
				}
				else {
					redirect += "&";
				}
				
				redirect += "returnUrl=" + returnPath;
				
				JForumExecutionContext.setRedirect(redirect);
			}
		}
		
		return TemplateKeys.USER_LOGIN;
	}
	
	/**
	 * Returns the initial page to start fetching records from.
	 *   
	 * @return The initial page number
	 */
	public static int getStartPage()
	{
		String s = JForumExecutionContext.getRequest().getParameter("start");
		int start;
		
		if (StringUtils.isEmpty(s)) {
			start = 0;
		}
		else {
			start = Integer.parseInt(s);
			
			if (start < 0) {
				start = 0;
			}
		}
		
		return start;
	}
	
	/**
	 * Gets the forum base link.
	 * The returned link has a trailing slash
	 * @return The forum link, with the trailing slash
	 */
	public static String getForumLink()
	{
		String forumLink = SystemGlobals.getValue(ConfigKeys.FORUM_LINK);
		
		if (forumLink.charAt(forumLink.length() - 1) != '/') {
			forumLink += "/";
		}
		
		return forumLink;
	}
	
	public static String toUtf8String(String s)
	{
		StringBuffer sb = new StringBuffer();
	
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
	
			if ((c >= 0) && (c <= 255)) {
				sb.append(c);
			}
			else {
				byte[] b;
	
				try {
					b = Character.toString(c).getBytes("utf-8");
				}
				catch (Exception ex) {
					System.out.println(ex);
					b = new byte[0];
				}
	
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
	
					if (k < 0) {
						k += 256;
					}
	
					sb.append('%').append(Integer.toHexString(k).toUpperCase());
				}
			}
		}
	
		return sb.toString();
	}
	
	/**
	 * Formats a date using the pattern defined in the configuration file.
	 * The key is the value of {@link net.jforum.util.preferences.ConfigKeys.DATE_TIME_FORMAT}
	 * @param date the date to format
	 * @return the string with the formated date
	 */
	public static String formatDate(Date date) 
	{
		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		return df.format(date);
	}
	
	/**
	 * Escapes &lt; by &amp;lt; and &gt; by &amp;gt;
	 * @param contents the string to parse
	 * @return the new string
	 */
	public static String espaceHtml(String contents)
	{
		StringBuffer sb = new StringBuffer(contents);
		
		replaceAll(sb, "<", "&lt");
		replaceAll(sb, ">", "&gt;");
		
		return sb.toString();
	}
	
	/**
	 * Replaces some string with another value
	 * @param sb the StrinbBuilder with the contents to work on
	 * @param what the string to be replaced
	 * @param with the new value
	 * @return the new string
	 */
	public static String replaceAll(StringBuffer sb, String what, String with)
	{
		int pos = sb.indexOf(what);
		
		while (pos > -1) {
			sb.replace(pos, pos + what.length(), with);
			pos = sb.indexOf(what);
		}
		
		return sb.toString();
	}
	
	/**
	 * @see #replaceAll(StringBuffer, String, String)
     * @param contents String
     * @param what String
     * @param with String
     * @return String
	 */
	public static String replaceAll(String contents, String what, String with)
	{
		return replaceAll(new StringBuffer(contents), what, with);
	}

	/**
	 * Parse the user's signature, to make it proper to visualization
	 * @param u the user instance
	 */
	public static void prepareUserSignature(User u)
	{
		if (u.getSignature() != null) {
			StringBuffer sb = new StringBuffer(u.getSignature());
			
			replaceAll(sb, "\n", "<br />");
			
			u.setSignature(sb.toString());
			u.setSignature(PostCommon.prepareTextForDisplayExceptCodeTag(u.getSignature(), true, true));
		}
	}
}
