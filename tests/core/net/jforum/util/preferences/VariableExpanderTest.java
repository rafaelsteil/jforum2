/*
 * Created on 28/08/2005 22:23:41
 */
package net.jforum.util.preferences;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Rafael Steil
 * @version $Id: VariableExpanderTest.java,v 1.1 2005/08/29 02:13:23 rafaelsteil Exp $
 */
public class VariableExpanderTest extends TestCase
{
	private static class MyStore implements VariableStore
	{
		private Map data = new HashMap();
		
		public MyStore()
		{
			this.fill();
		}
		
		private void fill()
		{
			this.data.put("config.dir", "/config");
			this.data.put("database.driver.name", "mysql");
		}
		
		public String getVariableValue(String variableName)
		{
			return (String)this.data.get(variableName);
		}
	}
	
	private VariableExpander extapnder;
	
	private String test = "${config.dir}/database/${database.driver.name}/${database.driver.name}.properties";
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		this.extapnder = new VariableExpander(new MyStore(), "${", "}");
	}
	
	public void testExpand()
	{
		String result = this.extapnder.expandVariables(this.test);
		assertEquals("/config/database/mysql/mysql.properties", result);
	}
}
