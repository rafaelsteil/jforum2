/*
 * Created on 09/09/2006 17:00:27
 */
package net.jforum.api.integration.rest;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import net.jforum.JForumExecutionContext;
import net.jforum.TestCaseUtils;
import net.jforum.api.rest.RESTAuthentication;

/**
 * @author Rafael Steil
 * @version $Id: RESTAuthenticationTestCase.java,v 1.4 2007/07/28 14:17:11 rafaelsteil Exp $
 */
public class RESTAuthenticationTestCase extends TestCase
{
	public static final String API_KEY = "api.key.test";
	
	public void testInvalid() throws Exception
	{
		RESTAuthentication auth = new RESTAuthentication();
		boolean isValid = auth.validateApiKey("1");
		
		assertFalse("The api key should not be valid", isValid);
	}
	
	public void testValid() throws Exception
	{
		RESTAuthentication auth = new RESTAuthentication();
		boolean isValid = auth.validateApiKey(API_KEY);
		
		assertTrue("The api key should be valid", isValid);
	}
	
	Date tomorrow()
	{
		Calendar c = Calendar.getInstance();
		return new GregorianCalendar(c.get(Calendar.YEAR), 
			c.get(Calendar.MONTH), 
			c.get(Calendar.DATE) + 1).getTime();
	}

	/**
	 * @throws SQLException
	 */
	void createApiKey(Date validity) throws SQLException
	{
		PreparedStatement p = null;
		
		try {
			p = JForumExecutionContext.getConnection()
				.prepareStatement("INSERT INTO jforum_api (api_key, api_validity) "
						+ " VALUES (?, ?)");
			p.setString(1, API_KEY);
			p.setTimestamp(2, new Timestamp(validity.getTime()));
			p.executeUpdate();
		}
		finally {
			if (p != null) p.close();
		}
	}

	/**
	 * @throws SQLException
	 */
	void deleteApiKey() throws SQLException
	{
		PreparedStatement p = null;
		
		try {
			p = JForumExecutionContext.getConnection()
				.prepareStatement("DELETE FROM jforum_api WHERE api_key = ?");
			p.setString(1, API_KEY);
			p.executeUpdate();
		}
		finally {
			if (p != null) p.close();
		}
	}
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		TestCaseUtils.loadEnvironment();
		TestCaseUtils.initDatabaseImplementation();
		this.createApiKey(this.tomorrow());
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		this.deleteApiKey();
		JForumExecutionContext.finish();
	}
}
