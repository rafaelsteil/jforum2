/*
 * Copyright (c) JForum Team
 * 
 * All rights reserved.
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
 * Created on Mar 14, 2005 3:27:33 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;

import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.exceptions.ConfigLoadException;

/**
 * @author Rafael Steil
 * @version $Id: Tpl.java,v 1.7 2007/04/12 02:11:54 rafaelsteil Exp $
 */
public class Tpl implements Cacheable
{
	private static final String FQN = "templates";
	
	private static CacheEngine cache;
	
	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}

	/**
	 * Loads the HTML mappings file. 
	 * 
	 * @param filename The complete path to the file to load
	 * @throws ConfigLoadException if the file is not found or
	 * some other error occurs when loading the file.
	 */
	public static void load(String filename)
	{
		FileInputStream fis = null;
		
		try {
			Properties p = new Properties();
			fis = new FileInputStream(filename);
			p.load(fis);
			
			for (Iterator iter = p.keySet().iterator(); iter.hasNext(); ) {
				String key = (String)iter.next();
				
				cache.add(FQN, key, p.getProperty(key));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ConfigLoadException("Error while trying to load " + filename + ": " + e);
		}
		finally {
			if (fis != null) {
				try { fis.close(); } catch (Exception e) {}
			}
		}
	}
	
	/**
	 * Gets a template filename by its configuration's key
	 * 
	 * @param key The Key to load.
	 * @return The html template filename
	 */
	public static String name(String key)
	{
		return (String)cache.get(FQN, key);
	}
}
