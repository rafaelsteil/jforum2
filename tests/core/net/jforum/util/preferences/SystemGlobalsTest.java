/*
 * Copyright (c) 2003, Rafael Steil
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
 * This file creation date: 01/10/2004 - 15:23:32
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.preferences;

import junit.framework.TestCase;
import net.jforum.TestCaseUtils;

/**
 * @author Rafael Steil
 * @version $Id: SystemGlobalsTest.java,v 1.5 2005/07/26 04:01:23 diegopires Exp $
 */
public class SystemGlobalsTest extends TestCase
{
	private static final String USER_DEFAULTS = "userDefaultsTest.properties";
	private static final String GLOBALS = "/WEB-INF/tests/config/SystemGlobalsTest.properties";
	
	/** 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() throws Exception
	{
		SystemGlobals.initGlobals(TestCaseUtils.getRootDir(), 
						TestCaseUtils.getRootDir() + GLOBALS);
	}
	
	public void testLoad()
	{
		assertEquals(TestCaseUtils.getRootDir() + "/WEB-INF", 
						SystemGlobals.getApplicationResourceDir());
		assertEquals(SystemGlobals.getApplicationResourceDir() + "/config", 
						SystemGlobals.getValue("config.dir"));
		assertEquals("some value", SystemGlobals.getValue("some.key"));
	}
	
	public void testTypes()
	{
		assertEquals(123, SystemGlobals.getIntValue("int.key"));
		assertEquals(true, SystemGlobals.getBoolValue("bool.key"));
		assertEquals(false, SystemGlobals.getBoolValue("bool.key.2"));
		assertEquals("some string", SystemGlobals.getValue("string.key"));
	}
	
	public void testUserDefaults() throws Exception
	{
		SystemGlobals.loadAdditionalDefaults(TestCaseUtils.getRootDir()
						+ "/WEB-INF/tests/config/" + USER_DEFAULTS);
		assertEquals("user value 1", SystemGlobals.getValue("user.key.1"));
		assertEquals("user value 2", SystemGlobals.getValue("user.key.2"));
		assertEquals("some user value", SystemGlobals.getValue("some.key"));
		
		try {
			SystemGlobals.getValue("blablabla");
		}
		catch (RuntimeException e) {
			assertEquals("unknown property: blablabla", e.getMessage());
		}
	}
}
