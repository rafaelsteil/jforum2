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
 * Created on Mar 17, 2005 5:38:11 PM
 *
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum;

import java.util.Date;

import javax.servlet.http.Cookie;

import net.jforum.context.ForumContext;
import net.jforum.context.RequestContext;
import net.jforum.context.SessionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.UserDAO;
import net.jforum.dao.UserSessionDAO;
import net.jforum.entities.User;
import net.jforum.entities.UserSession;
import net.jforum.exceptions.DatabaseException;
import net.jforum.exceptions.ForumException;
import net.jforum.repository.SecurityRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.sso.SSO;
import net.jforum.sso.SSOUtils;
import net.jforum.util.I18n;
import net.jforum.util.MD5;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import freemarker.template.SimpleHash;

/**
 * Common methods used by the controller.
 * 
 * @author Rafael Steil
 * @version $Id: ControllerUtils.java,v 1.38 2007/08/01 22:30:06 rafaelsteil Exp $
 */
public class ControllerUtils
{
	/**
	 * Setup common variables used by almost all templates.
	 * 
	 * @param context SimpleHash The context to use
     * @param jforumContext JForumContext
	 */
	public void prepareTemplateContext(SimpleHash context, ForumContext jforumContext)
	{
		RequestContext request = JForumExecutionContext.getRequest();
		
		context.put("karmaEnabled", SecurityRepository.canAccess(SecurityConstants.PERM_KARMA_ENABLED));
		context.put("dateTimeFormat", SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		context.put("autoLoginEnabled", SystemGlobals.getBoolValue(ConfigKeys.AUTO_LOGIN_ENABLED));
		context.put("sso", ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE)));
		context.put("contextPath", request.getContextPath());
		context.put("serverName", request.getServerName());
		context.put("templateName", SystemGlobals.getValue(ConfigKeys.TEMPLATE_DIR));
		context.put("extension", SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		context.put("serverPort", Integer.toString(request.getServerPort()));
		context.put("I18n", I18n.getInstance());
		context.put("version", SystemGlobals.getValue(ConfigKeys.VERSION));
		context.put("forumTitle", SystemGlobals.getValue(ConfigKeys.FORUM_PAGE_TITLE));
		context.put("pageTitle", SystemGlobals.getValue(ConfigKeys.FORUM_PAGE_TITLE));
		context.put("metaKeywords", SystemGlobals.getValue(ConfigKeys.FORUM_PAGE_METATAG_KEYWORDS));
		context.put("metaDescription", SystemGlobals.getValue(ConfigKeys.FORUM_PAGE_METATAG_DESCRIPTION));
		context.put("forumLink", SystemGlobals.getValue(ConfigKeys.FORUM_LINK));
		context.put("homepageLink", SystemGlobals.getValue(ConfigKeys.HOMEPAGE_LINK));
		context.put("encoding", SystemGlobals.getValue(ConfigKeys.ENCODING));
		context.put("bookmarksEnabled", SecurityRepository.canAccess(SecurityConstants.PERM_BOOKMARKS_ENABLED));
		context.put("canAccessModerationLog", SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_LOG));
		context.put("JForumContext", jforumContext);
		context.put("timestamp", new Long(System.currentTimeMillis()));
	}

	/**
	 * Checks user credentials / automatic login.
	 * 
	 * @param userSession The UserSession instance associated to the user's session
	 * @return <code>true</code> if auto login was enabled and the user was sucessfuly 
	 * logged in.
	 * @throws DatabaseException
	 */
	protected boolean checkAutoLogin(UserSession userSession)
	{
		String cookieName = SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_DATA);

		Cookie cookie = this.getCookieTemplate(cookieName);
		Cookie hashCookie = this.getCookieTemplate(SystemGlobals.getValue(ConfigKeys.COOKIE_USER_HASH));
		Cookie autoLoginCookie = this.getCookieTemplate(SystemGlobals.getValue(ConfigKeys.COOKIE_AUTO_LOGIN));

		if (hashCookie != null && cookie != null
				&& !cookie.getValue().equals(SystemGlobals.getValue(ConfigKeys.ANONYMOUS_USER_ID))
				&& autoLoginCookie != null 
				&& "1".equals(autoLoginCookie.getValue())) {
			String uid = cookie.getValue();
			String uidHash = hashCookie.getValue();

			// Load the user-specific security hash from the database
			try {
				UserDAO userDao = DataAccessDriver.getInstance().newUserDAO();
				String userHash = userDao.getUserAuthHash(Integer.parseInt(uid));
				
				if (userHash == null || userHash.trim().length() == 0) {
					return false;
				}
				
				String securityHash = MD5.crypt(userHash);
	
				if (securityHash.equals(uidHash)) {
					int userId = Integer.parseInt(uid);
					userSession.setUserId(userId);
					
					User user = userDao.selectById(userId);
	
					if (user == null || user.getId() != userId || user.isDeleted()) {
						userSession.makeAnonymous();
						return false;
					}
	
					this.configureUserSession(userSession, user);
					
					return true;
				}
			}
			catch (Exception e) {
				throw new DatabaseException(e);
			}
			
			userSession.makeAnonymous();
		}
		
		return false;
	}

	/**
	 * Setup optios and values for the user's session if authentication was ok.
	 * 
	 * @param userSession The UserSession instance of the user
	 * @param user The User instance of the authenticated user
	 */
	protected void configureUserSession(UserSession userSession, User user)
	{
		userSession.dataToUser(user);

		// As an user may come back to the forum before its
		// last visit's session expires, we should check for
		// existent user information and then, if found, store
		// it to the database before getting his information back.
		String sessionId = SessionFacade.isUserInSession(user.getId());

		UserSession tmpUs;
		if (sessionId != null) {
			SessionFacade.storeSessionData(sessionId, JForumExecutionContext.getConnection());
			tmpUs = SessionFacade.getUserSession(sessionId);
			SessionFacade.remove(sessionId);
		}
		else {
			UserSessionDAO sm = DataAccessDriver.getInstance().newUserSessionDAO();
			tmpUs = sm.selectById(userSession, JForumExecutionContext.getConnection());
		}

		if (tmpUs == null) {
			userSession.setLastVisit(new Date(System.currentTimeMillis()));
		}
		else {
			// Update last visit and session start time
			userSession.setLastVisit(new Date(tmpUs.getStartTime().getTime() + tmpUs.getSessionTime()));
		}

		// If the execution point gets here, then the user
		// has chosen "autoLogin"
		userSession.setAutoLogin(true);
		SessionFacade.makeLogged();

		I18n.load(user.getLang());
	}

	/**
	 * Checks for user authentication using some SSO implementation
     * @param userSession UserSession
     */
	protected void checkSSO(UserSession userSession)
	{
		try {
			SSO sso = (SSO) Class.forName(SystemGlobals.getValue(ConfigKeys.SSO_IMPLEMENTATION)).newInstance();
			String username = sso.authenticateUser(JForumExecutionContext.getRequest());

			if (username == null || username.trim().equals("")) {
				userSession.makeAnonymous();
			}
			else {
				SSOUtils utils = new SSOUtils();

				if (!utils.userExists(username)) {
					SessionContext session = JForumExecutionContext.getRequest().getSessionContext();

					String email = (String) session.getAttribute(SystemGlobals.getValue(ConfigKeys.SSO_EMAIL_ATTRIBUTE));
					String password = (String) session.getAttribute(SystemGlobals.getValue(ConfigKeys.SSO_PASSWORD_ATTRIBUTE));

					if (email == null) {
						email = SystemGlobals.getValue(ConfigKeys.SSO_DEFAULT_EMAIL);
					}

					if (password == null) {
						password = SystemGlobals.getValue(ConfigKeys.SSO_DEFAULT_PASSWORD);
					}

					utils.register(password, email);
				}

				this.configureUserSession(userSession, utils.getUser());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ForumException("Error while executing SSO actions: " + e);
		}
	}

	/**
	 * Do a refresh in the user's session. This method will update the last visit time for the
	 * current user, as well checking for authentication if the session is new or the SSO user has
	 * changed
	 */
	public void refreshSession()
	{
		UserSession userSession = SessionFacade.getUserSession();
		RequestContext request = JForumExecutionContext.getRequest();

		if (userSession == null) {
			userSession = new UserSession();
			userSession.registerBasicInfo();
			userSession.setSessionId(request.getSessionContext().getId());
			userSession.setIp(request.getRemoteAddr());
			SessionFacade.makeUnlogged();

			if (!JForumExecutionContext.getForumContext().isBot()) {
				// Non-SSO authentications can use auto login
				if (!ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
					if (SystemGlobals.getBoolValue(ConfigKeys.AUTO_LOGIN_ENABLED)) {
						this.checkAutoLogin(userSession);
					}
					else {
						userSession.makeAnonymous();
					}
				}
				else {
					this.checkSSO(userSession);
				}
			}

			SessionFacade.add(userSession);
		}
		else if (ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
			SSO sso;
			
			try {
				sso = (SSO) Class.forName(SystemGlobals.getValue(ConfigKeys.SSO_IMPLEMENTATION)).newInstance();
			}
			catch (Exception e) {
				throw new ForumException(e);
			}

			// If SSO, then check if the session is valid
			if (!sso.isSessionValid(userSession, request)) {
				SessionFacade.remove(userSession.getSessionId());
				refreshSession();
			}
		}
		else {
			SessionFacade.getUserSession().updateSessionTime();
		}
	}

	/**
	 * Gets a cookie by its name.
	 * 
	 * @param name The cookie name to retrieve
	 * @return The <code>Cookie</code> object if found, or <code>null</code> oterwhise
	 */
	public static Cookie getCookie(String name)
	{
		Cookie[] cookies = JForumExecutionContext.getRequest().getCookies();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];

				if (c.getName().equals(name)) {
					return c;
				}
			}
		}

		return null;
	}
	
	/**
	 * Template method to get a cookie.
	 * Useful to situations when a subclass
	 * wants to have a different way to 
	 * retrieve a cookie.
	 * @param name The cookie name to retrieve
	 * @return The Cookie object if found, or null otherwise
	 * @see #getCookie(String)
	 */
	protected Cookie getCookieTemplate(String name)
	{
		return ControllerUtils.getCookie(name);
	}

	/**
	 * Add or update a cookie. This method adds a cookie, serializing its value using XML.
	 * 
	 * @param name The cookie name.
	 * @param value The cookie value
	 */
	public static void addCookie(String name, String value)
	{
		int maxAge = 3600 * 24 * 365;
		
		if (value == null) {
			maxAge = 0;
			value = "";
		}
		
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");

		JForumExecutionContext.getResponse().addCookie(cookie);
	}
	
	/**
	 * Template method to add a cookie.
	 * Useful to suatins when a subclass wants to add
	 * a cookie in a fashion different than the normal 
	 * behaviour
	 * @param name The cookie name
	 * @param value The cookie value
	 * @see #addCookie(String, String)
	 */
	protected void addCookieTemplate(String name, String value)
	{
		ControllerUtils.addCookie(name, value);
	}
}
