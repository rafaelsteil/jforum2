/*
 * Copyright (c) JForum Team
 * All rights reserved.

 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided 
 * that the following conditions are met:

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
 * This file creation date: 03/09/2003 / 23:42:55
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.util.Iterator;
import java.util.List;

import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.RankingDAO;
import net.jforum.entities.Ranking;
import net.jforum.exceptions.RankingLoadException;

/**
 * @author Rafael Steil
 * @version $Id: RankingRepository.java,v 1.15 2007/09/19 02:51:11 rafaelsteil Exp $
 */
public class RankingRepository implements Cacheable
{
	private static CacheEngine cache;
	private static final String FQN = "ranking";
	private static final String ENTRIES = "entries";

	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}
	
	public static void loadRanks()
	{
		try {
			RankingDAO rm = DataAccessDriver.getInstance().newRankingDAO();
			cache.add(FQN, ENTRIES, rm.selectAll());
		}
		catch (Exception e) {
			throw new RankingLoadException("Error while loading the rankings: " + e);
		}
	}
	
	public static int size()
	{
		return ((List)cache.get(FQN, ENTRIES)).size();
	}
	
	/**
	 * Gets the title associated to total of messages the user have
	 * @param total Number of messages the user have. The ranking will be
	 * returned according to the range to which this total belongs to. 
	 * @return String with the ranking title. 
	 */	
	public static String getRankTitle(int rankId, int total) 
	{
		String title = null;
		
		if (rankId > 0) {
			title = getRankTitleById(rankId);
		}
		
		if (title == null) {
			title = getRankTitleByPosts(total);
		}
		
		return title;
	}
	
	private static String getRankTitleByPosts(int total)
	{
		Ranking lastRank = new Ranking();
		
		List entries = (List)cache.get(FQN, ENTRIES);
		
		for (Iterator iter = entries.iterator(); iter.hasNext(); ) {
			Ranking r = (Ranking)iter.next();
			
			if (total == r.getMin() && !r.isSpecial()) {
				return r.getTitle();
			}
			else if (total > lastRank.getMin() && total < r.getMin()) {
				return lastRank.getTitle();
			}
			
			lastRank = r;
		}
		
		return lastRank.getTitle();
	}

	private static String getRankTitleById(int rankId)
	{
		Ranking r = new Ranking();
		r.setId(rankId);
		
		List l = (List)cache.get(FQN, ENTRIES);
		int index = l.indexOf(r);
		
		return index > -1
			? ((Ranking)l.get(index)).getTitle()
			: null;
	}
}
