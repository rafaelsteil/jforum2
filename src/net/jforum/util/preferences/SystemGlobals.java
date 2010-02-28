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
 * This file creating date: Feb 24, 2003 / 8:25:35 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import net.jforum.ConfigLoader;
import net.jforum.exceptions.ForumException;

import org.apache.log4j.Logger;

/**
 * <p>Store global configurations used in the system.
 * This is an helper class used to access the values
 * defined at SystemGlobals.properties and related
 * config files.</p> 
 * 
 * <p>
 * Transient values are stored in a special place, and are not
 * modified when you change a regular key's value. 
 * </p>
 * 
 * @author Rafael Steil
 * @author Pieter Olivier
 * @version $Id: SystemGlobals.java,v 1.35 2007/09/02 15:51:04 andowson Exp $
 */
public class SystemGlobals implements VariableStore
{
	private static SystemGlobals globals = new SystemGlobals();

	private String defaultConfig;
	private String installationConfig;

	private Properties defaults = new Properties();
	private Properties installation = new Properties();
	private Map objectProperties = new HashMap();
	private static List additionalDefaultsList = new ArrayList();
	private static Properties queries = new Properties();
	private static Properties transientValues = new Properties();

	private VariableExpander expander = new VariableExpander(this, "${", "}");
	
	private static final Logger logger = Logger.getLogger(SystemGlobals.class);
	
	private SystemGlobals() {}

	/**
	 * Initialize the global configuration
	 * @param appPath The application path (normally the path to the webapp base dir
	 * @param mainConfigurationFile The file containing system defaults (when null, defaults to <appPath>/WEB-INF/config/default.conf)
	 */
	public static void initGlobals(String appPath, String mainConfigurationFile)
	{
		globals = new SystemGlobals();
		globals.buildSystem(appPath, mainConfigurationFile);
	}
	
	public static void reset()
	{
		globals.defaults.clear();
		globals.installation.clear();
		additionalDefaultsList.clear();
		queries.clear();
		transientValues.clear();
	}
	
	private void buildSystem(String appPath, String mainConfigurationFile)
	{
		if (mainConfigurationFile == null) {
			throw new InvalidParameterException("defaultConfig could not be null");
		}

		this.defaultConfig = mainConfigurationFile;
		this.defaults = new Properties();

		this.defaults.put(ConfigKeys.APPLICATION_PATH, appPath);
		this.defaults.put(ConfigKeys.DEFAULT_CONFIG, mainConfigurationFile);
		
		SystemGlobals.loadDefaults();
	
		this.installation = new Properties();
		this.installationConfig = getVariableValue(ConfigKeys.INSTALLATION_CONFIG);

		for (Iterator iter = additionalDefaultsList.iterator(); iter.hasNext(); ) {
			loadAdditionalDefaults((String)iter.next());
		}
		
		if (new File(this.installationConfig).exists()) {
			loadAdditionalDefaults(this.installationConfig);
		}
	}
	
	/**
	 * Sets a value for some property
	 * 
	 * @param field The property name
	 * @param value The property value 
	 * @see #getVariableValue(String)
	 * */
	public static void setValue(String field, String value)
	{
		globals.installation.put(field, value);
		globals.expander.clearCache();
	}
	
	public static void setObjectValue(String field, Object value)
	{
		globals.objectProperties.put(field, value);
	}
	
	public static Object getObjectValue(String field)
	{
		return globals.objectProperties.get(field);
	}

	/**
	 * Set a transient configuration value (a value that will not be saved) 
	 * @param field The name of the configuration option
	 * @param value The value of the configuration option
	 */
	public static void setTransientValue(String field, String value)
	{
		transientValues.put(field, value);
	}

	/**
	 * Load system defaults
	 */
	public static void loadDefaults()
	{
		try
		{
			FileInputStream input = new FileInputStream(globals.defaultConfig);
			globals.defaults.load(input);
			input.close();
			globals.expander.clearCache();
		}
		catch (IOException e)
		{
			throw new ForumException(e);
		}
	}
	
	/**
	 * Merge additional configuration defaults
	 * 
	 * @param file File from which to load the additional defaults
	 */
	public static void loadAdditionalDefaults(String file)
	{
		if (!new File(file).exists()) {
			logger.info("Cannot find file " + file + ". Will ignore it");
			return;
		}

		try
		{
			FileInputStream input = new FileInputStream(file);
			globals.installation.load(input);
			input.close();
		}
		catch (IOException e)
		{
			throw new ForumException(e);
		}

		if (!additionalDefaultsList.contains(file)) {
			additionalDefaultsList.add(file);
		}
	}

	/**
	 * Save installation defaults
	 */
	public static void saveInstallation()
	{
		// We need this temporary "p" because, when
		// new FileOutputStream() is called, it will 
		// raise an event to the TimerTask who is listen
		// for file modifications, which then reloads the
		// configurations from the filesystem, overwriting
		// our new keys. 
		class SortedProperties extends Properties{
			public synchronized Enumeration keys() {
				Enumeration keysEnum = super.keys();
				Vector keyList = new Vector();
				while(keysEnum.hasMoreElements()){
					keyList.add(keysEnum.nextElement());
				}
				Collections.sort(keyList);
				return keyList.elements();
			}
		}

		Properties p = new SortedProperties();
		p.putAll(globals.installation);
		
		try {
			FileOutputStream out = new FileOutputStream(globals.installationConfig);
			p.store(out, "Installation specific configuration options");
			out.close();
		}
		catch (IOException e) {
			throw new ForumException(e);
		}

		ConfigLoader.listenInstallationConfig();
	}

	/**
	 * Gets the value of some property
	 * 
	 * @param field The property name to retrieve the value
	 * @return String with the value, or <code>null</code> if not found
	 * @see #setValue(String, String)
	 * */
	public static String getValue(String field)
	{
		return globals.getVariableValue(field);
	}
	
	public static String getTransientValue(String field)
	{
		return transientValues.getProperty(field);
	}

	/**
	 * Retrieve an integer-valued configuration field
	 * 
	 * @param field Name of the configuration option
	 * @return The value of the configuration option
	 * @exception NullPointerException when the field does not exists
	 */
	public static int getIntValue(String field)
	{
		return Integer.parseInt(getValue(field));
	}

	/**
	 * Retrieve an boolean-values configuration field
	 * 
	 * @param field name of the configuration option
	 * @return The value of the configuration option
	 * @exception NullPointerException when the field does not exists
	 */
	public static boolean getBoolValue(String field)
	{
		return "true".equals(getValue(field));
	}

	/**
	 * Return the value of a configuration value as a variable. Variable expansion is performe
	 * on the result.
	 * 
	 * @param field The field name to retrieve
	 * @return The value of the field if present or null if not  
	 */

	public String getVariableValue(String field)
	{
		String preExpansion = globals.installation.getProperty(field);
		
		if (preExpansion == null) {
			preExpansion = this.defaults.getProperty(field);

			if (preExpansion == null) {
				return null;
			}
		}

		return expander.expandVariables(preExpansion);
	}

	/**
	 * Sets the application's root directory 
	 * 
	 * @param ap String containing the complete path to the root dir
	 * @see #getApplicationPath
	 * */
	public static void setApplicationPath(String ap)
	{
		setValue(ConfigKeys.APPLICATION_PATH, ap);
	}

	/**
	 * Getst the complete path to the application's root dir
	 * 
	 * @return String with the path
	 * @see #setApplicationPath
	 * */
	public static String getApplicationPath()
	{
		return getValue(ConfigKeys.APPLICATION_PATH);
	}

	/**
	 * Gets the path to the resource's directory.
	 * This method returns the directory name where the config
	 * files are stored. 
	 *  Caso queira saber o caminho absoluto do diretorio, voce precisa
	 * usar
	 * Note that this method does not return the complete path. If you 
	 * want the full path, you must use 
	 * <blockquote><pre>SystemGlobals.getApplicationPath() + SystemGlobals.getApplicationResourcedir()</pre></blockquote>
	 * 
	 * @return String with the name of the resource dir, relative 
	 * to application's root dir.
	 * @see #getApplicationPath()
	 * */
	public static String getApplicationResourceDir()
	{
		return getValue(ConfigKeys.RESOURCE_DIR);
	}

	/**
	 * Load the SQL queries
	 *
	 * @param queryFile Complete path to the SQL queries file.
	 **/
	public static void loadQueries(String queryFile)
	{
		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(queryFile);
			queries.load(fis);
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

	/**
	 * Gets some SQL statement.
	 * 
	 * @param sql The query's name, as defined in the file loaded by
	 * {@link #loadQueries(String)}
	 * @return The SQL statement, or <code>null</code> if not found.
	 * */
	public static String getSql(String sql)
	{
		return queries.getProperty(sql);
	}

	/**
	 * Retrieve an iterator that iterates over all known configuration keys
	 * 
	 * @return An iterator that iterates over all known configuration keys
	 */
	public static Iterator fetchConfigKeyIterator()
	{
		return globals.defaults.keySet().iterator();
	}
	
	public static Properties getConfigData()
	{
		return new Properties(globals.defaults);
	}
}