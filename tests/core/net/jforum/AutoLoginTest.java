/*
 * Created on 07/07/2005 21:28:45
 */
package net.jforum;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import junit.framework.TestCase;
import net.jforum.entities.UserSession;

/**
 * Tests the auto login feature
 * @author Rafael Steil
 * @version $Id: AutoLoginTest.java,v 1.4 2005/07/26 04:01:19 diegopires Exp $
 */
public class AutoLoginTest extends TestCase
{
	public void testAutoLoginWithNullCookieExpectFail()
	{
		ControllerUtils c = this.newControllerUtils();
		c.checkAutoLogin(this.newUserSession());
	}
	
	private UserSession newUserSession()
	{
		return new UserSession() {
			public void makeAnonymous() {
				throw new RuntimeException("went anonymous");
			}
		};
	}

	private ControllerUtils newControllerUtils()
	{
		return new ControllerUtils() {
			private Map cookiesMap = new HashMap();
			
			protected Cookie getCookieTemplate(String name) {
				return (Cookie)this.cookiesMap.get(name);
			}
			
			protected void addCookieTemplate(String name, String value) {
				this.cookiesMap.put(name, new Cookie(name, value));
			}
			
			
		};
	}
}
