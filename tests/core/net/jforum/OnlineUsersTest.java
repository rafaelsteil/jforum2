/*
 * Created on Jun 15, 2005 11:57:08 AM
 */
package net.jforum;

import junit.framework.TestCase;
import net.jforum.cache.DefaultCacheEngine;
import net.jforum.context.web.WebRequestContext;
import net.jforum.context.web.WebResponseContext;
import net.jforum.context.RequestContext;
import net.jforum.context.ResponseContext;
import net.jforum.context.ForumContext;
import net.jforum.context.JForumContext;
import net.jforum.entities.UserSession;
import net.jforum.http.FakeHttpRequest;
import net.jforum.http.FakeHttpResponse;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * Test {@link net.jforum.SessionFacade} methods for online users
 * 
 * @author Rafael Steil
 * @version $Id: OnlineUsersTest.java,v 1.9 2006/08/24 21:03:00 sergemaslyukov Exp $
 */
public class OnlineUsersTest extends TestCase
{
	private static final int ANONYMOUS = 1;
	
	protected void setUp() throws Exception
	{
		new SessionFacade().setCacheEngine(new DefaultCacheEngine());
		
        RequestContext requestContext = new WebRequestContext(new FakeHttpRequest());
        ResponseContext responseContext = new WebResponseContext(new FakeHttpResponse());

        ForumContext forumContext = new JForumContext(
            requestContext.getContextPath(),
            SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION),
            requestContext,
            responseContext,
            false
        );
        JForumExecutionContext ex = JForumExecutionContext.get();
        ex.setForumContext( forumContext );

		JForumExecutionContext.set(ex);
		
		SystemGlobals.setValue(ConfigKeys.ANONYMOUS_USER_ID, Integer.toString(ANONYMOUS));
	}
	
	/**
	 * Check if guest users are being counted correctly
	 */
	public void testAnonymousCount()
	{
		this.createUserSession(ANONYMOUS, ANONYMOUS + 1 + "_" + System.currentTimeMillis());
		this.createUserSession(ANONYMOUS, ANONYMOUS + 2 + "_" + System.currentTimeMillis());
		this.createUserSession(ANONYMOUS, ANONYMOUS + 3 + "_" + System.currentTimeMillis());
		
		assertEquals(3, SessionFacade.anonymousSize());
	}
	
	/**
	 * Check if counting of both guest and logged users is correct
	 */
	public void test2Anymous1Logged() 
	{
		// Anonymous
		this.createUserSession(ANONYMOUS, ANONYMOUS + "1_" + System.currentTimeMillis());
		this.createUserSession(ANONYMOUS, ANONYMOUS + "2_" + System.currentTimeMillis());
		
		// Logged
		SessionFacade.setAttribute("logged", "1");
		this.createUserSession(2, "logged" + System.currentTimeMillis());
		
		// Assert
		assertEquals(2, SessionFacade.anonymousSize());
		assertEquals(1, SessionFacade.registeredSize());
	}
	
	/**
	 * First register as anonymous, then change to logged, and check counting
	 */
	public void testAnonymousThenLogged()
	{
		// Anonymous
		String sessionId = ANONYMOUS + "1_" + System.currentTimeMillis();
		
		this.createUserSession(ANONYMOUS, sessionId);
		
		assertEquals(1, SessionFacade.anonymousSize());
		assertEquals(0, SessionFacade.registeredSize());
		
		// Logged
		UserSession us = SessionFacade.getUserSession(sessionId);
		us.setUserId(2);
		
		SessionFacade.setAttribute("logged", "1");
		
		SessionFacade.remove(sessionId);
		SessionFacade.add(us);
		
		assertEquals(0, SessionFacade.anonymousSize());
		assertEquals(1, SessionFacade.registeredSize());
	}
	
	public void test3LoggedThen1Logout()
	{
		// Logged
		SessionFacade.setAttribute("logged", "1");
		
		this.createUserSession(2, "2_" + System.currentTimeMillis());
		
		String sessionId = "3_" + System.currentTimeMillis();
		this.createUserSession(3, sessionId);

		this.createUserSession(4, "4_" + System.currentTimeMillis());
		
		assertEquals(3, SessionFacade.registeredSize());
		assertEquals(0, SessionFacade.anonymousSize());
		
		// Logout (goes as guest)
		SessionFacade.removeAttribute("logged");
		SessionFacade.remove(sessionId);
		
		this.createUserSession(ANONYMOUS, sessionId);
		
		assertEquals(2, SessionFacade.registeredSize());
		assertEquals(1, SessionFacade.anonymousSize());
	}
	
	private void createUserSession(int userId, String sessionId)
	{
		UserSession us = new UserSession();

		us.setUserId(userId);
		us.setSessionId(sessionId);
		us.setUsername("blah_" + System.currentTimeMillis());
		
		SessionFacade.add(us, sessionId);
	}
}
