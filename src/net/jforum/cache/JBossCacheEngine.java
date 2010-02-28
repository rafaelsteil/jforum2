/*
 * Copyright (c) 2003, 2004 Rafael Steil
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
 * Created on Jan 13, 2005 11:42:54 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.cache;

import java.util.ArrayList;
import java.util.Collection;

import net.jforum.exceptions.CacheException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.log4j.Logger;
import org.jboss.cache.Fqn;
import org.jboss.cache.Node;
import org.jboss.cache.PropertyConfigurator;
import org.jboss.cache.TreeCache;

/**
 * @author Rafael Steil
 * @version $Id: JBossCacheEngine.java,v 1.7 2005/09/25 02:40:28 rafaelsteil Exp $
 */
public class JBossCacheEngine implements CacheEngine
{
	private Logger logger = Logger.getLogger(JBossCacheEngine.class);
	private TreeCache cache;

	/**
	 * @see net.jforum.cache.CacheEngine#init()
	 */
	public void init()
	{
		try {
			this.cache = new TreeCache();
			PropertyConfigurator config = new PropertyConfigurator();
			config.configure(this.cache, SystemGlobals.getValue(ConfigKeys.JBOSS_CACHE_PROPERTIES));
			
			this.cache.startService();
		}
		catch (Exception e) {
			throw new CacheException("Error while trying to configure jboss-cache: " + e);
		}
	}
	
	/**
	 * @see net.jforum.cache.CacheEngine#stop()
	 */
	public void stop()
	{
		this.cache.stopService();
	}

	/**
	 * @see net.jforum.cache.CacheEngine#add(java.lang.String, java.lang.Object)
	 */
	public void add(String key, Object value)
	{
		this.add(CacheEngine.DUMMY_FQN, key, value);
	}

	/**
	 * @see net.jforum.cache.CacheEngine#add(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public void add(String fqn, String key, Object value)
	{
		try {
			this.cache.put(Fqn.fromString(fqn), key, value);
		}
		catch (Exception e) {
			throw new CacheException("Error adding a new entry to the cache: " + e);
		}
	}

	/**
	 * @see net.jforum.cache.CacheEngine#get(java.lang.String, java.lang.String)
	 */
	public Object get(String fqn, String key)
	{
		try {
			return this.cache.get(Fqn.fromString(fqn), key);
		}
		catch (Exception e) {
			throw new CacheException("Error while trying to get an entry from the cache: " + e);
		}
	}

	/**
	 * @see net.jforum.cache.CacheEngine#get(java.lang.String)
	 */
	public Object get(String fqn)
	{
		try {
			return this.cache.get(Fqn.fromString(fqn));
		}
		catch (Exception e) {
			throw new CacheException("Error while trying to get an entry from the cache: " + e);
		}
	}
	
	/**
	 * @see net.jforum.cache.CacheEngine#getValues(java.lang.String)
	 */
	public Collection getValues(String fqn)
	{
		Node node = (Node)this.get(fqn);
		if (node == null) {
			return new ArrayList();
		}
		
		return node.getData().values();
	}

	/**
	 * @see net.jforum.cache.CacheEngine#remove(java.lang.String, java.lang.String)
	 */
	public void remove(String fqn, String key)
	{
		try {
			if (key == null) {
				this.cache.remove(Fqn.fromString(fqn));
			}
			else {
				this.cache.remove(Fqn.fromString(fqn), key);
			}
		}
		catch (Exception e) {
			logger.warn("Error while removing a FQN from the cache: " + e);
		}
	}

	/**
	 * @see net.jforum.cache.CacheEngine#remove(java.lang.String)
	 */
	public void remove(String fqn)
	{
		try {
			this.cache.remove(Fqn.fromString(fqn));
		}
		catch (Exception e) {
			logger.warn("Error while removing a FQN from the cache: " + e);
		}
	}

}
