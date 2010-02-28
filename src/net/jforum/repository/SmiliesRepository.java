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
 * This file creation date: 13/01/2004 / 20:23:52
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.util.Iterator;
import java.util.List;

import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Smilie;
import net.jforum.exceptions.SmiliesLoadException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: SmiliesRepository.java,v 1.15 2006/08/20 22:47:38 rafaelsteil Exp $
 */
public class SmiliesRepository implements Cacheable
{
	private static CacheEngine cache;
	private static final String FQN = "smilies";
	private static final String ENTRIES = "entries";
	private static boolean contexted = false;

	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}
	
	public static void loadSmilies()
	{
		try {
			cache.add(FQN, ENTRIES, DataAccessDriver.getInstance().newSmilieDAO().selectAll());
			contexted = false;
		}
		catch (Exception e) {
			throw new SmiliesLoadException("Error while loading smilies: " + e);
		}
	}
	
	public static List getSmilies()
	{
		List list = (List)cache.get(FQN, ENTRIES);
		if (!contexted) {
			String forumLink = SystemGlobals.getValue(ConfigKeys.FORUM_LINK);
			
			for (Iterator iter = list.iterator(); iter.hasNext(); ) {
				Smilie s = (Smilie)iter.next();
				s.setUrl(s.getUrl().replaceAll("#CONTEXT#", forumLink).replaceAll("\\\\", ""));
			}
			
			cache.add(FQN, ENTRIES, list);
			contexted = true;
		}
		
		return list;
	}
}
