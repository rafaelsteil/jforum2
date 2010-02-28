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
 * This file creation date: Mar 10, 2003 / 9:28:40 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.jforum.SessionFacade;
import net.jforum.entities.UserSession;
import net.jforum.exceptions.ForumException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.log4j.Logger;

import freemarker.template.SimpleSequence;

/**
 * I18n (Internationalization) class implementation. Does nothing of special, just loads the
 * messages into memory and provides a static method to acess them.
 * 
 * @author Rafael Steil
 * @author James Yong
 * @version $Id: I18n.java,v 1.36 2007/07/08 14:11:43 rafaelsteil Exp $
 */
public class I18n
{
	private static final Logger logger = Logger.getLogger(I18n.class);

	private static I18n classInstance = new I18n();

	private static Map messagesMap = new HashMap();

	private static Properties localeNames = new Properties();

	private static String defaultName;

	private static String baseDir;

	private static List watching = new ArrayList();

	public static final String CANNOT_DELETE_GROUP = "CannotDeleteGroup";

	public static final String CANNOT_DELETE_CATEGORY = "CannotDeleteCategory";

	public static final String CANNOT_DELETE_BANNER = "CannotDeleteBanner";

	private I18n()
	{
	}

	/**
	 * Gets the singleton
	 * 
	 * @return Instance of I18n class
	 */
	public static I18n getInstance()
	{
		return classInstance;
	}

	/**
	 * Load the default I18n file
	 * 
	 */
	public static synchronized void load()
	{
		baseDir = SystemGlobals.getApplicationResourceDir() + "/" + SystemGlobals.getValue(ConfigKeys.LOCALES_DIR);

		loadLocales();

		defaultName = SystemGlobals.getValue(ConfigKeys.I18N_DEFAULT_ADMIN);
		load(defaultName, null);

		String custom = SystemGlobals.getValue(ConfigKeys.I18N_DEFAULT);
		if (!custom.equals(defaultName)) {
			load(custom, defaultName);
			defaultName = custom;
		}
	}

	public static void changeBoardDefault(String newDefaultLanguage)
	{
		load(newDefaultLanguage, SystemGlobals.getValue(ConfigKeys.I18N_DEFAULT_ADMIN));
		defaultName = newDefaultLanguage;
	}

	private static void loadLocales()
	{
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(baseDir + SystemGlobals.getValue(ConfigKeys.LOCALES_NAMES));
			localeNames.load(fis);
		}
		catch (IOException e) {
			throw new ForumException(e);
		}
		finally {
			if (fis != null) {
				try { fis.close(); } catch (Exception e) {}
			}
		}
	}

	static void load(String localeName, String mergeWith)
	{
		load(localeName, mergeWith, false);
	}

	static void load(String localeName, String mergeWith, boolean force)
	{
		if (!force && (localeName == null || localeName.trim().equals("") || I18n.contains(localeName))) {
			return;
		}

		if (localeNames.size() == 0) {
			loadLocales();
		}

		Properties p = new Properties();

		if (mergeWith != null) {
			if (!I18n.contains(mergeWith)) {
				load(mergeWith, null);
			}

			p.putAll((Properties) messagesMap.get(mergeWith));
		}
		
		FileInputStream fis = null;

		try {
			String filename = baseDir + localeNames.getProperty(localeName);
			
			// If the requested locale does not exist, use the default
			if (!new File(filename).exists()) {
				filename = baseDir + localeNames.getProperty(SystemGlobals.getValue(ConfigKeys.I18N_DEFAULT_ADMIN));
			}
			
			fis = new FileInputStream(filename);
			p.load(fis);
		}
		catch (IOException e) {
			throw new ForumException(e);
		}
		finally {
			if (fis != null) {
				try { fis.close(); } catch (Exception e) {}
			}
		}
		
		messagesMap.put(localeName, p);

		watchForChanges(localeName);
	}

	/**
	 * Loads a new locale. If <code>localeName</code> is either null or empty, or if the locale is
	 * already loaded, the method will return without executing any code.
	 * 
	 * @param localeName
	 *            The locale name to load
	 */
	public static void load(String localeName)
	{
		load(localeName, SystemGlobals.getValue(ConfigKeys.I18N_DEFAULT));
	}

	public static void reset()
	{
		messagesMap = new HashMap();
		localeNames = new Properties();
		defaultName = null;
	}

	private static void watchForChanges(final String localeName)
	{
		if (!watching.contains(localeName)) {
			watching.add(localeName);

			int fileChangesDelay = SystemGlobals.getIntValue(ConfigKeys.FILECHANGES_DELAY);

			if (fileChangesDelay > 0) {
				FileMonitor.getInstance().addFileChangeListener(new FileChangeListener() {
					/**
					 * @see net.jforum.util.FileChangeListener#fileChanged(java.lang.String)
					 */
					public void fileChanged(String filename)
					{
						if (logger.isDebugEnabled()) {
							logger.info("Reloading i18n for " + localeName);
						}

						I18n.load(localeName, SystemGlobals.getValue(ConfigKeys.I18N_DEFAULT), true);
					}
				}, baseDir + localeNames.getProperty(localeName), fileChangesDelay);
			}
		}
	}

	/**
	 * Gets a I18N (internationalized) message.
	 * 
	 * @param localeName
	 *            The locale name to retrieve the messages from
	 * @param messageName
	 *            The message name to retrieve. Must be a valid entry into the file specified by
	 *            <code>i18n.file</code> property.
	 * @param params
	 *            Parameters needed by some messages. The messages with extra parameters are
	 *            formated according to {@link java.text.MessageFormat}specification
	 * @return String With the message
	 */
	public static String getMessage(String localeName, String messageName, Object params[])
	{
		return MessageFormat.format(((Properties) messagesMap.get(localeName)).getProperty(messageName), params);
	}

	/**
	 * @see #getMessage(String, String, Object[])
	 * @param messageName String
	 * @param params Object
	 * @return String
	 */
	public static String getMessage(String messageName, Object params[])
	{
		String lang = "";
		UserSession us = SessionFacade.getUserSession();

		if (us != null && us.getLang() != null) {
			lang = us.getLang();
		}

		if ("".equals(lang)) {
			return getMessage(defaultName, messageName, params);
		}

		return getMessage(lang, messageName, params);
	}
	
	public static String getMessage(String messageName, SimpleSequence params)
	{
		try {
			return getMessage(messageName, params.toList().toArray());
		}
		catch (Exception e) {
			throw new ForumException(e);
		}
	}

	/**
	 * Gets an I18N (internationalization) message.
	 * 
	 * @param m
	 *            The message name to retrieve. Must be a valid entry into the file specified by
	 *            <code>i18n.file</code> property.
	 * @return String With the message
	 * @param localeName
	 *            String
	 */
	public static String getMessage(String localeName, String m)
	{
		if (!messagesMap.containsKey(localeName)) {
			load(localeName);
		}

		return (((Properties) messagesMap.get(localeName)).getProperty(m));
	}

	public static String getMessage(String m)
	{
		return getMessage(getUserLanguage(), m);
	}

	public static String getMessage(String m, UserSession us)
	{
		if (us == null || us.getLang() == null || us.getLang().equals("")) {
			return getMessage(defaultName, m);
		}

		return getMessage(us.getLang(), m);
	}

	/**
	 * Gets the language name for the current request. The method will first look at
	 * {@link UserSession#getLang()} and use it if any value is found. Otherwise, the default board
	 * language will be used
	 * 
	 * @return String
	 */
	public static String getUserLanguage()
	{
		UserSession us = SessionFacade.getUserSession();

		if (us == null || us.getLang() == null || us.getLang().trim().equals("")) {
			return defaultName;
		}

		return us.getLang();
	}

	/**
	 * Check whether the language is loaded in i18n.
	 * 
	 * @param language
	 *            String
	 * @return boolean
	 */
	public static boolean contains(String language)
	{
		return messagesMap.containsKey(language);
	}

	/**
	 * Check if the given language exist.
	 * 
	 * @param language
	 *            The language to check
	 * @return <code>true</code> if the language is a valid and registered translation.
	 */
	public static boolean languageExists(String language)
	{
		return (localeNames.getProperty(language) != null);
	}
}