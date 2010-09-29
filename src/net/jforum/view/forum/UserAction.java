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
 * This file creation date: May 12, 2003 / 8:31:25 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.jforum.Command;
import net.jforum.ControllerUtils;
import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.context.RequestContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.UserDAO;
import net.jforum.dao.UserSessionDAO;
import net.jforum.entities.Bookmark;
import net.jforum.entities.User;
import net.jforum.entities.UserSession;
import net.jforum.exceptions.ForumException;
import net.jforum.repository.ForumRepository;
import net.jforum.repository.RankingRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.I18n;
import net.jforum.util.MD5;
import net.jforum.util.concurrent.Executor;
import net.jforum.util.mail.ActivationKeySpammer;
import net.jforum.util.mail.EmailSenderTask;
import net.jforum.util.mail.LostPasswordSpammer;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import net.jforum.view.forum.common.UserCommon;
import net.jforum.view.forum.common.ViewCommon;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: UserAction.java,v 1.94 2007/09/21 17:26:09 rafaelsteil Exp $
 */
public class UserAction extends Command 
{
	private static final Logger logger = Logger.getLogger(UserAction.class);
	
	private boolean canEdit()
	{
		int tmpId = SessionFacade.getUserSession().getUserId();
		boolean canEdit = SessionFacade.isLogged() && tmpId == this.request.getIntParameter("user_id");
		
		if (!canEdit) {
			this.profile();
		}
		
		return canEdit;
	}
	
	public void edit()
	{
		if (this.canEdit()) {
			int userId = this.request.getIntParameter("user_id");
			UserDAO um = DataAccessDriver.getInstance().newUserDAO();
			User u = um.selectById(userId);

			this.context.put("u", u);
			this.context.put("action", "editSave");
			this.context.put("pageTitle", I18n.getMessage("UserProfile.profileFor") + " " + u.getUsername());
			this.context.put("avatarAllowExternalUrl", SystemGlobals.getBoolValue(ConfigKeys.AVATAR_ALLOW_EXTERNAL_URL));
			this.setTemplateName(TemplateKeys.USER_EDIT);
		} 
	}

	public void editDone()
	{
		this.context.put("editDone", true);
		this.edit();
	}

	public void editSave()
	{
		if (this.canEdit()) {
			int userId = this.request.getIntParameter("user_id");
			List warns = UserCommon.saveUser(userId);
	
			if (warns.size() > 0) {
				this.context.put("warns", warns);
				this.edit();
			} 
			else {
				JForumExecutionContext.setRedirect(this.request.getContextPath()
					+ "/user/editDone/" + userId
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
			}
		}
	}
	
	private void registrationDisabled()
	{
		this.setTemplateName(TemplateKeys.USER_REGISTRATION_DISABLED);
		this.context.put("message", I18n.getMessage("User.registrationDisabled"));
	}
	
	private void insert(boolean hasErrors)
	{
		int userId = SessionFacade.getUserSession().getUserId();

		if ((!SystemGlobals.getBoolValue(ConfigKeys.REGISTRATION_ENABLED)
				&& !SecurityRepository.get(userId).canAccess(SecurityConstants.PERM_ADMINISTRATION))
				|| ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
			this.registrationDisabled();
			return;
		}
		
		if (!hasErrors && SystemGlobals.getBoolValue(ConfigKeys.AGREEMENT_SHOW) && !this.agreementAccepted()) {
			this.setTemplateName(TemplateKeys.AGREEMENT_LIST);
			this.context.put("agreementContents", this.agreementContents());
			return;
		}

		this.setTemplateName(TemplateKeys.USER_INSERT);
		this.context.put("action", "insertSave");
		this.context.put("username", this.request.getParameter("username"));
		this.context.put("email", this.request.getParameter("email"));
		this.context.put("pageTitle", I18n.getMessage("ForumBase.register"));
		
		if (SystemGlobals.getBoolValue(ConfigKeys.CAPTCHA_REGISTRATION)){
			// Create a new image captcha
			SessionFacade.getUserSession().createNewCaptcha();
			this.context.put("captcha_reg", true);
		}

		SessionFacade.removeAttribute(ConfigKeys.AGREEMENT_ACCEPTED);
	}

	public void insert() 
	{
		this.insert(false);
	}
	
	public void acceptAgreement()
	{
		SessionFacade.setAttribute(ConfigKeys.AGREEMENT_ACCEPTED, "1");
		JForumExecutionContext.setRedirect(this.request.getContextPath()
			+ "/user/insert"
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}
	
	private String agreementContents()
	{
		StringBuffer contents = new StringBuffer();
		
		BufferedReader reader = null;
		FileReader fileReader = null;

		try {
			String directory = new StringBuffer()
				.append(SystemGlobals.getApplicationPath()) 
				.append(SystemGlobals.getValue(ConfigKeys.AGREEMENT_FILES_PATH)) 
				.append('/')
				.toString();

			String filename = "terms_" + SystemGlobals.getValue(ConfigKeys.I18N_DEFAULT) + ".txt";
			
			File file = new File(directory + filename);
			
			if (!file.exists()) {
				filename = SystemGlobals.getValue(ConfigKeys.AGREEMENT_DEFAULT_FILE);
				file = new File(directory + filename);
				
				if (!file.exists()) {
					throw new FileNotFoundException("Could not locate any terms agreement file");
				}
			}
			
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			
			char[] buffer = new char[2048];
			int c = 0;
			
			while ((c = reader.read(buffer, 0, buffer.length)) > -1) {
				contents.append(buffer, 0, c);
			}
		}
		catch (Exception e) {
			logger.warn("Failed to read agreement data: " + e, e);
			contents = new StringBuffer(I18n.getMessage("User.agreement.noAgreement"));
		}
		finally {
			if (fileReader != null) { try { fileReader.close(); } catch (Exception e) {} }
			if (reader != null) { try { reader.close(); } catch (Exception e) {} }
		}
		
		return contents.toString();
	}
	
	private boolean agreementAccepted()
	{
		return "1".equals(SessionFacade.getAttribute(ConfigKeys.AGREEMENT_ACCEPTED));
	}

	public void insertSave()
	{
		UserSession userSession = SessionFacade.getUserSession();
		int userId = userSession.getUserId();

		if ((!SystemGlobals.getBoolValue(ConfigKeys.REGISTRATION_ENABLED)
				&& !SecurityRepository.get(userId).canAccess(SecurityConstants.PERM_ADMINISTRATION))
				|| ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
			this.registrationDisabled();
			return;
		}

		User u = new User();
		UserDAO dao = DataAccessDriver.getInstance().newUserDAO();

		String username = this.request.getParameter("username");
		String password = this.request.getParameter("password");
		String email = this.request.getParameter("email");
		String captchaResponse = this.request.getParameter("captchaResponse");

		boolean error = false;
		if (username == null || username.trim().equals("") 
				|| password == null || password.trim().equals("")) {
			this.context.put("error", I18n.getMessage("UsernamePasswordCannotBeNull"));
			error = true;
		}
		
		if (username != null) {
			username = username.trim();
		}

        if (!error && username.length() > SystemGlobals.getIntValue(ConfigKeys.USERNAME_MAX_LENGTH)) {
			this.context.put("error", I18n.getMessage("User.usernameTooBig"));
			error = true;
		}
		
		if (!error && username.indexOf('<') > -1 || username.indexOf('>') > -1) {
			this.context.put("error", I18n.getMessage("User.usernameInvalidChars"));
			error = true;
		}

		if (!error && dao.isUsernameRegistered(username)) {
			this.context.put("error", I18n.getMessage("UsernameExists"));
			error = true;
		}
		
		if (!error && dao.findByEmail(email) != null) {
			this.context.put("error", I18n.getMessage("User.emailExists", new String[] { email }));
			error = true;
		}
		
		if (!error && !userSession.validateCaptchaResponse(captchaResponse)){
			this.context.put("error", I18n.getMessage("CaptchaResponseFails"));
			error = true;
		}

		if (error) {
			this.insert(true);
			return;
		}

		u.setUsername(username);
		u.setPassword(MD5.crypt(password));
		u.setEmail(email);

		boolean requiresMailActivation = SystemGlobals.getBoolValue(ConfigKeys.MAIL_USER_EMAIL_AUTH);
		
		if (requiresMailActivation) {
			u.setActivationKey(MD5.crypt(username + System.currentTimeMillis()));
		}

		int newUserId = dao.addNew(u);

		if (requiresMailActivation) {
			Executor.execute(new EmailSenderTask(new ActivationKeySpammer(u)));

			this.setTemplateName(TemplateKeys.USER_INSERT_ACTIVATE_MAIL);
			this.context.put("message", I18n.getMessage("User.GoActivateAccountMessage"));
		} 
		else if(SecurityRepository.get(userId).canAccess(SecurityConstants.PERM_ADMINISTRATION)) {
			JForumExecutionContext.setRedirect(this.request.getContextPath()
				+ "/adminUsers/list"
				+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
		}
		else {
			this.logNewRegisteredUserIn(newUserId, u);
		}
		
		if (!requiresMailActivation) {
			dao.writeUserActive(newUserId);
		}
	}

	public void activateAccount()
	{
		String hash = this.request.getParameter("hash");
		int userId = (new Integer(this.request.getParameter("user_id"))).intValue();

		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
		User u = um.selectById(userId);

		boolean isValid = um.validateActivationKeyHash(userId, hash);
		
		if (isValid) {
			// Activate the account
			um.writeUserActive(userId);
			this.logNewRegisteredUserIn(userId, u);
		} 
		else {
			this.setTemplateName(TemplateKeys.USER_INVALID_ACTIVATION);
			this.context.put("message", I18n.getMessage("User.invalidActivationKey", 
				new Object[] { this.request.getContextPath()
					+ "/user/activateManual"
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) 
				}
			));
		}

	}
	
	public void activateManual()
	{
		this.setTemplateName(TemplateKeys.ACTIVATE_ACCOUNT_MANUAL);
	}

	private void logNewRegisteredUserIn(int userId, User u) 
	{
		SessionFacade.makeLogged();

		UserSession userSession = new UserSession();
		userSession.setAutoLogin(true);
		userSession.setUserId(userId);
		userSession.setUsername(u.getUsername());
		userSession.setLastVisit(new Date(System.currentTimeMillis()));
		userSession.setStartTime(new Date(System.currentTimeMillis()));

		SessionFacade.add(userSession);

		// Finalizing.. show to user the congrats page
		JForumExecutionContext.setRedirect(this.request.getContextPath()
			+ "/user/registrationComplete"
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));
	}

	public void registrationComplete()
	{
		int userId = SessionFacade.getUserSession().getUserId();
		
		ForumRepository.setLastRegisteredUser(
				DataAccessDriver.getInstance().newUserDAO().selectById(userId));
		ForumRepository.incrementTotalUsers();

		String profilePage = JForumExecutionContext.getForumContext().encodeURL("/user/edit/" + userId);
		String homePage = JForumExecutionContext.getForumContext().encodeURL("/forums/list");

		String message = I18n.getMessage("User.RegistrationCompleteMessage", 
				new Object[] { profilePage, homePage });
		this.context.put("message", message);
		this.setTemplateName(TemplateKeys.USER_REGISTRATION_COMPLETE);
	}

	public void validateLogin()
	{
		String password;
		String username;

		if (parseBasicAuthentication()) {
			username = (String)this.request.getAttribute("username");
			password = (String)this.request.getAttribute("password");
		} 
		else {
			username = this.request.getParameter("username");
			password = this.request.getParameter("password");
		}

		boolean validInfo = false;
		
		if (password.length() > 0) {
			User user = this.validateLogin(username, password);

			if (user != null) {
				// Note: here we only want to set the redirect location if it hasn't already been
				// set. This will give the LoginAuthenticator a chance to set the redirect location.
				this.buildSucessfulLoginRedirect();

				SessionFacade.makeLogged();
				
				String sessionId = SessionFacade.isUserInSession(user.getId());
				UserSession userSession = new UserSession(SessionFacade.getUserSession());
				
				// Remove the "guest" session
				SessionFacade.remove(userSession.getSessionId());
				
				userSession.dataToUser(user);
				
				UserSession currentUs = SessionFacade.getUserSession(sessionId);

				// Check if the user is returning to the system
				// before its last session has expired ( hypothesis )
                UserSession tmpUs;
				if (sessionId != null && currentUs != null) {
					// Write its old session data
					SessionFacade.storeSessionData(sessionId, JForumExecutionContext.getConnection());
					tmpUs = new UserSession(currentUs);
					SessionFacade.remove(sessionId);
				}
				else {
					UserSessionDAO sm = DataAccessDriver.getInstance().newUserSessionDAO();
					tmpUs = sm.selectById(userSession, JForumExecutionContext.getConnection());
				}

				I18n.load(user.getLang());

				// Autologin
				if (this.request.getParameter("autologin") != null
						&& SystemGlobals.getBoolValue(ConfigKeys.AUTO_LOGIN_ENABLED)) {
					userSession.setAutoLogin(true);
					
					// Generate the user-specific hash
					String systemHash = MD5.crypt(SystemGlobals.getValue(ConfigKeys.USER_HASH_SEQUENCE) + user.getId());
					String userHash = MD5.crypt(System.currentTimeMillis() + systemHash);
					
					// Persist the user hash
					UserDAO dao = DataAccessDriver.getInstance().newUserDAO();
					dao.saveUserAuthHash(user.getId(), userHash);
					
					systemHash = MD5.crypt(userHash);
					
					ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_AUTO_LOGIN), "1");
					ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_USER_HASH), systemHash);
				}
				else {
					// Remove cookies for safety
					ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_USER_HASH), null);
					ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_AUTO_LOGIN), null);
				}
				
				if (tmpUs == null) {
					userSession.setLastVisit(new Date(System.currentTimeMillis()));
				}
				else {
					// Update last visit and session start time
					userSession.setLastVisit(new Date(tmpUs.getStartTime().getTime() + tmpUs.getSessionTime()));
				}
				
				SessionFacade.add(userSession);
				SessionFacade.setAttribute(ConfigKeys.TOPICS_READ_TIME, new HashMap());
				ControllerUtils.addCookie(SystemGlobals.getValue(ConfigKeys.COOKIE_NAME_DATA), 
					Integer.toString(user.getId()));

				SecurityRepository.load(user.getId(), true);
				validInfo = true;
			}
		}

		// Invalid login
		if (!validInfo) {
			this.context.put("invalidLogin", "1");
			this.setTemplateName(TemplateKeys.USER_VALIDATE_LOGIN);

			if (this.request.getParameter("returnPath") != null) {
				this.context.put("returnPath",
						this.request.getParameter("returnPath"));
			}
		} 
		else if (this.request.getParameter("returnPath") != null) {
			JForumExecutionContext.setRedirect(this.request.getParameter("returnPath"));
		}
	}

	private void buildSucessfulLoginRedirect()
	{
		if (JForumExecutionContext.getRedirectTo() == null) {
			String forwaredHost = request.getHeader("X-Forwarded-Host");
			
			if (forwaredHost == null 
					|| SystemGlobals.getBoolValue(ConfigKeys.LOGIN_IGNORE_XFORWARDEDHOST)) {
				JForumExecutionContext.setRedirect(this.request.getContextPath()
					+ "/forums/list"
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));						
			}
			else {
				JForumExecutionContext.setRedirect(this.request.getScheme()
					+ "://"
					+ forwaredHost
					+ this.request.getContextPath()
					+ "/forums/list"
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION)); 
			}
		}
	}

    public void validateLogin(RequestContext request)  {
        this.request = request;
        validateLogin();
    }

    public static boolean hasBasicAuthentication(RequestContext request) {
        String auth = request.getHeader("Authorization");
        return (auth != null && auth.startsWith("Basic "));
    }

    private boolean parseBasicAuthentication()
	{
		if (hasBasicAuthentication(request)) {
			String auth = request.getHeader("Authorization");
			String decoded;
			
			try {
				decoded = new String(new sun.misc.BASE64Decoder().decodeBuffer(auth.substring(6)));
			}
			catch (IOException e) {
				throw new ForumException(e);
			}
			
			int p = decoded.indexOf(':');
			
			if (p != -1) {
				request.setAttribute("username", decoded.substring(0, p));
				request.setAttribute("password", decoded.substring(p + 1));
				return true;
			}
		}
		return false;
	}

    private User validateLogin(String name, String password)
	{
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();
        return um.validateLogin(name, password);
	}

	public void profile()
	{
		DataAccessDriver da = DataAccessDriver.getInstance();
		UserDAO udao = da.newUserDAO();

		User u = udao.selectById(this.request.getIntParameter("user_id"));
		
		if (u.getId() == 0) {
			this.userNotFound();
		}
		else {
			this.setTemplateName(TemplateKeys.USER_PROFILE);
			this.context.put("karmaEnabled", SecurityRepository.canAccess(SecurityConstants.PERM_KARMA_ENABLED));
			this.context.put("rank", new RankingRepository());
			this.context.put("u", u);
			this.context.put("avatarAllowExternalUrl", SystemGlobals.getBoolValue(ConfigKeys.AVATAR_ALLOW_EXTERNAL_URL));
			
			int loggedId = SessionFacade.getUserSession().getUserId();
			int count = 0;
			
			List bookmarks = da.newBookmarkDAO().selectByUser(u.getId());
			for (Iterator iter = bookmarks.iterator(); iter.hasNext(); ) {
				Bookmark b = (Bookmark)iter.next();

				if (b.isPublicVisible() || loggedId == u.getId()) {
					count++;
				}
			}

			this.context.put("pageTitle", I18n.getMessage("UserProfile.allAbout")+" "+u.getUsername());
			this.context.put("nbookmarks", new Integer(count));
			this.context.put("ntopics", new Integer(da.newTopicDAO().countUserTopics(u.getId())));
			this.context.put("nposts", new Integer(da.newPostDAO().countUserPosts(u.getId())));
		}
	}
	
	private void userNotFound()
	{
		this.context.put("message", I18n.getMessage("User.notFound"));
		this.setTemplateName(TemplateKeys.USER_NOT_FOUND);
	}

	public void logout()
	{
		JForumExecutionContext.setRedirect(this.request.getContextPath()
			+ "/forums/list"
			+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION));

		UserSession userSession = SessionFacade.getUserSession();
		SessionFacade.storeSessionData(userSession.getSessionId(), JForumExecutionContext.getConnection());

		SessionFacade.makeUnlogged();
		SessionFacade.remove(userSession.getSessionId());
		
		// Disable auto login
		userSession.setAutoLogin(false);
		userSession.makeAnonymous();

		SessionFacade.add(userSession);
	}

	public void login()
	{
		if (ConfigKeys.TYPE_SSO.equals(SystemGlobals.getValue(ConfigKeys.AUTHENTICATION_TYPE))) {
			this.registrationDisabled();
			return;
		}
		
		if (this.request.getParameter("returnPath") != null) {
			this.context.put("returnPath", this.request.getParameter("returnPath"));
		}
		else if (!SystemGlobals.getBoolValue(ConfigKeys.LOGIN_IGNORE_REFERER)) {
			String referer = this.request.getHeader("Referer");
			
			if (referer != null) {
				this.context.put("returnPath", referer);
			}
		}

		this.context.put("pageTitle", I18n.getMessage("ForumBase.login"));
		this.setTemplateName(TemplateKeys.USER_LOGIN);
	}

	// Lost password form
	public void lostPassword() 
	{
		this.setTemplateName(TemplateKeys.USER_LOSTPASSWORD);
		this.context.put("pageTitle", I18n.getMessage("PasswordRecovery.title"));
	}
	
	public User prepareLostPassword(String username, String email)
	{
		User user = null;
		UserDAO um = DataAccessDriver.getInstance().newUserDAO();

		if (email != null && !email.trim().equals("")) {
			username = um.getUsernameByEmail(email);
		}

		if (username != null && !username.trim().equals("")) {
			List l = um.findByName(username, true);
			if (l.size() > 0) {
				user = (User)l.get(0);
			}
		}
		
		if (user == null) {
			return null;
		}
		
		String hash = MD5.crypt(user.getEmail() 
			+ System.currentTimeMillis() 
			+ SystemGlobals.getValue(ConfigKeys.USER_HASH_SEQUENCE)
			+ new Random().nextInt(999999));

		um.writeLostPasswordHash(user.getEmail(), hash);
		
		user.setActivationKey(hash);
		
		return user;
	}

	// Send lost password email
	public void lostPasswordSend()
	{
		String email = this.request.getParameter("email");
		String username = this.request.getParameter("username");

		User user = this.prepareLostPassword(username, email);
		if (user == null) {
			// user could not be found
			this.context.put("message",
					I18n.getMessage("PasswordRecovery.invalidUserEmail"));
			this.lostPassword();
			return;
		}
		
		Executor.execute(new EmailSenderTask(
				new LostPasswordSpammer(user, 
					SystemGlobals.getValue(ConfigKeys.MAIL_LOST_PASSWORD_SUBJECT))));

		this.setTemplateName(TemplateKeys.USER_LOSTPASSWORD_SEND);
		this.context.put("message", I18n.getMessage(
			"PasswordRecovery.emailSent",
			new String[] { 
					this.request.getContextPath()
					+ "/user/login"
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) 
				}));
	}

	// Recover user password ( aka, ask him a new one )
	public void recoverPassword()
	{
		String hash = this.request.getParameter("hash");

		this.setTemplateName(TemplateKeys.USER_RECOVERPASSWORD);
		this.context.put("recoverHash", hash);
	}

	public void recoverPasswordValidate()
	{
		String hash = this.request.getParameter("recoverHash");
		String email = this.request.getParameter("email");

		String message;
		boolean isOk = DataAccessDriver.getInstance().newUserDAO().validateLostPasswordHash(email, hash);
		
		if (isOk) {
			String password = this.request.getParameter("newPassword");
			DataAccessDriver.getInstance().newUserDAO().saveNewPassword(MD5.crypt(password), email);

			message = I18n.getMessage("PasswordRecovery.ok",
				new String[] { this.request.getContextPath()
					+ "/user/login"
					+ SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION) });
		} 
		else {
			message = I18n.getMessage("PasswordRecovery.invalidData");
		}

		this.setTemplateName(TemplateKeys.USER_RECOVERPASSWORD_VALIDATE);
		this.context.put("message", message);
	}

		
	public void list()
	{
		int start = this.preparePagination(DataAccessDriver.getInstance().newUserDAO().getTotalUsers());
		int usersPerPage = SystemGlobals.getIntValue(ConfigKeys.USERS_PER_PAGE);
							
		List users = DataAccessDriver.getInstance().newUserDAO().selectAll(start ,usersPerPage);
		this.context.put("users", users);
		this.context.put("pageTitle", I18n.getMessage("ForumBase.usersList"));
		this.setTemplateName(TemplateKeys.USER_LIST);
	}

	public void listGroup()
	{
		int groupId = this.request.getIntParameter("group_id");
		
		int start = this.preparePagination(DataAccessDriver.getInstance().newUserDAO().getTotalUsersByGroup(groupId));
		int usersPerPage = SystemGlobals.getIntValue(ConfigKeys.USERS_PER_PAGE);
							
		List users = DataAccessDriver.getInstance().newUserDAO().selectAllByGroup(groupId, start ,usersPerPage);
		
		this.context.put("users", users);
		this.setTemplateName(TemplateKeys.USER_LIST);
	}
	
	/**
	 * @deprecated probably will be removed. Use KarmaAction to load Karma
	 */
	public void searchKarma() 
	{
		int start = this.preparePagination(DataAccessDriver.getInstance().newUserDAO().getTotalUsers());
		int usersPerPage = SystemGlobals.getIntValue(ConfigKeys.USERS_PER_PAGE);
		
		//Load all users with your karma
		List users = DataAccessDriver.getInstance().newUserDAO().selectAllWithKarma(start ,usersPerPage);
		this.context.put("users", users);
		this.setTemplateName(TemplateKeys.USER_SEARCH_KARMA);
	}
	
	
	private int preparePagination(int totalUsers)
	{
		int start = ViewCommon.getStartPage();
		int usersPerPage = SystemGlobals.getIntValue(ConfigKeys.USERS_PER_PAGE);
		
		ViewCommon.contextToPagination(start, totalUsers, usersPerPage);
		
		return start;
	}	
}
