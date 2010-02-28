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
 * This file creation date: 30/09/2004 - 12:50:46
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util;

import junit.framework.TestCase;
import net.jforum.SessionFacade;
import net.jforum.TestCaseUtils;
import net.jforum.entities.UserSession;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: I18nTest.java,v 1.4 2005/07/26 03:04:41 rafaelsteil Exp $
 */
public class I18nTest extends TestCase
{
	private static boolean loaded = false;
	private static final String SESSION_ID = "1";
	private UserSession us;
	
	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		if (!loaded) {
			TestCaseUtils.loadEnvironment();
			SystemGlobals.setValue(ConfigKeys.RESOURCE_DIR, 
							SystemGlobals.getApplicationResourceDir() + "/tests");
			loaded = true;
		}
		
		SystemGlobals.setValue(ConfigKeys.I18N_DEFAULT_ADMIN, "default");
		SystemGlobals.setValue(ConfigKeys.I18N_DEFAULT, "default");
		
		this.us = new UserSession();
		this.us.setSessionId(SESSION_ID);
		SessionFacade.add(this.us, SESSION_ID);
		
		I18n.reset();
		I18n.load();
	}
	
	public void testLoad() throws Exception
	{
		assertTrue(I18n.contains("default"));
	}
	
	public void testDefaultKeys()
	{
		assertEquals("default value 1", I18n.getMessage("defaultKey1", this.us));
		assertEquals("default value 2", I18n.getMessage("defaultKey2", this.us));
		assertEquals("default value 3", I18n.getMessage("defaultKey3", this.us));
		assertEquals("default value 4", I18n.getMessage("defaultKey4", this.us));
		assertEquals("default value 5", I18n.getMessage("defaultKey5", this.us));
	}
	
	public void testLoadCheese() throws Exception
	{
		I18n.load("cheese");
		assertTrue(I18n.contains("cheese"));
	}
	
	public void testCheeseKeys()
	{
		this.us.setLang("cheese");
		assertEquals("default cheese 1", I18n.getMessage("defaultKey1", this.us));
		assertEquals("default cheese 2", I18n.getMessage("defaultKey2", this.us));
		assertEquals("default cheese 3", I18n.getMessage("defaultKey3", this.us));
		assertEquals("default cheese 4", I18n.getMessage("defaultKey4", this.us));
		assertEquals("default value 5", I18n.getMessage("defaultKey5", this.us));
	}
	
	public void testLoadOrange() throws Exception
	{
		I18n.load("orange");
		assertTrue(I18n.contains("orange"));
	}
	
	public void testOrangeKeys()
	{
		this.us.setLang("orange");
		assertEquals("default orange 1", I18n.getMessage("defaultKey1", this.us));
		assertEquals("default orange 2", I18n.getMessage("defaultKey2", this.us));
		assertEquals("default orange 3", I18n.getMessage("defaultKey3", this.us));
		assertEquals("default value 4", I18n.getMessage("defaultKey4", this.us));
		assertEquals("default value 5", I18n.getMessage("defaultKey5", this.us));
		assertEquals("orange is not cheese", I18n.getMessage("orange", this.us));
	}
	
	public void testGetMessageWithLocale()
	{
		assertEquals("default value 1", I18n.getMessage("default", "defaultKey1"));
		assertEquals("default cheese 1", I18n.getMessage("cheese", "defaultKey1"));
		assertEquals("default orange 1", I18n.getMessage("orange", "defaultKey1"));
		assertNull(I18n.getMessage("default", "orange"));
	}
	
	public void testRest()
	{
		I18n.reset();
		assertFalse(I18n.contains("default"));
		assertFalse(I18n.contains("orange"));
		assertFalse(I18n.contains("cheese"));
	}
	
	public void testMergeCheeseOrange() throws Exception
	{
		this.testRest();
		I18n.load("cheese", "orange");
		assertTrue(I18n.contains("cheese"));
		assertTrue(I18n.contains("orange"));
		assertEquals("default cheese 1", I18n.getMessage("cheese", "defaultKey1"));
		assertEquals("orange is not cheese", I18n.getMessage("cheese", "orange"));
	}
	
	public void testOrangeIsDefault() throws Exception
	{
		this.testRest();
		SystemGlobals.setValue(ConfigKeys.I18N_DEFAULT, "orange");
		I18n.load();
		assertTrue(I18n.contains("default"));
		assertTrue(I18n.contains("orange"));
		this.testOrangeKeys();
	}
}
