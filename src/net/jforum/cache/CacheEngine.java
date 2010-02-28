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
 * Created on Jan 13, 2005 5:58:36 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.cache;

import java.util.Collection;

/**
 * @author Rafael Steil
 * @version $Id: CacheEngine.java,v 1.11 2006/08/20 22:47:56 rafaelsteil Exp $
 */
public interface CacheEngine
{
	public static final String DUMMY_FQN = "";
	public static final String NOTIFICATION = "notification";
	
	/**
	 * Inits the cache engine. 
	 */
	public void init();
	
	/**
	 * Stops the cache engine
	 */
	public void stop();
	
	/**
	 * Adds a new object to the cache. 
	 * The fqn will be set as the value of {@link #DUMMY_FQN}
	 * 
	 * @param key The key to associate with the object. 
	 * @param value The object to cache
	 */
	public void add(String key, Object value);
	
	/**
	 * 
	 * Adds a new object to the cache.
	 * 
	 * @param fqn The fully qualified name of the cache. 
	 * @param key The key to associate with the object
	 * @param value The object to cache
	 */
	public void add(String fqn, String key, Object value);
	
	/**
	 * Gets some object from the cache.
	 * 
	 * @param fqn The fully qualified name associated with the key
	 * @param key The key to get
	 * @return The cached object, or <code>null</code> if no entry was found
	 */
	public Object get(String fqn, String key);
	
	/**
	 * Gets some object from the cache.
	 * 
	 * @param fqn The fqn tree to get
	 * @return The cached object, or <code>null</code> if no entry was found
	 */
	public Object get(String fqn);
	
	/**
	 * Gets all values from some given FQN.
	 * 
	 * @param fqn String
	 * @return Collection
	 */
	public Collection getValues(String fqn);
	
	/**
	 * Removes an entry from the cache.
	 * 
	 * @param fqn The fully qualified name associated with the key
	 * @param key The key to remove
	 */
	public void remove(String fqn, String key);
	
	/**
	 * Removes a complete note from the cache
	 * @param fqn The fqn to remove
	 */
	public void remove(String fqn);
}
