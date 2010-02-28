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
 * Created on 10/12/2006 19:12:49
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.dao.BanlistDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Banlist;

/**
 * @author Rafael Steil
 * @version $Id: BanlistRepository.java,v 1.2 2006/12/11 00:44:50 rafaelsteil Exp $
 */
public class BanlistRepository implements Cacheable
{
	private static CacheEngine cache;
	private static final String FQN = "banlist";
	private static final String BANLIST = "banlistCollection";
	
	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}
	
	public static boolean shouldBan(Banlist b) {
		boolean status = false;
		
		for (Iterator iter = banlist().values().iterator(); iter.hasNext(); ) {
			Banlist current = (Banlist)iter.next();
			
			if (current.matches(b)) {
				status = true;
				break;
			}
		}
		
		return status;
	}

	public static void add(Banlist b)
	{
		Map m = banlist();
		m.put(new Integer(b.getId()), b);
		
		cache.add(FQN, BANLIST, m);
	}
	
	public static void remove(int banlistId)
	{
		Map m = banlist();
		
		Integer key = new Integer(banlistId);
		
		if (m.containsKey(key)) {
			m.remove(key);
		}
		
		cache.add(FQN, BANLIST, m);
	}
	
	private static Map banlist()
	{
		Map m = (Map)cache.get(FQN, BANLIST);
		
		if (m == null) {
			m = new HashMap();
		}
		
		return m;
	}
	
	public static void loadBanlist() 
	{
		BanlistDAO dao = DataAccessDriver.getInstance().newBanlistDAO();
		List banlist = dao.selectAll();
		
		for (Iterator iter = banlist.iterator(); iter.hasNext(); ) {
			BanlistRepository.add((Banlist)iter.next());
		}
	}
}
