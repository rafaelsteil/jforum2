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
 * This file creation date: 07/02/2005 - 10:29:14
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.jforum.cache.CacheEngine;
import net.jforum.cache.Cacheable;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.PostDAO;
import net.jforum.entities.Post;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.view.forum.common.PostCommon;

/**
 * Repository for the post in the top n topics for each forum.
 * 
 * @author Sean Mitchell
 * @author Rafael Steil
 * @version $Id: PostRepository.java,v 1.13 2006/08/23 02:13:48 rafaelsteil Exp $
 */
public class PostRepository implements Cacheable
{
	private static final int CACHE_SIZE = SystemGlobals.getIntValue(ConfigKeys.POSTS_CACHE_SIZE);
	private static final String FQN = "posts";
	private static CacheEngine cache;
	
	/**
	 * @see net.jforum.cache.Cacheable#setCacheEngine(net.jforum.cache.CacheEngine)
	 */
	public void setCacheEngine(CacheEngine engine)
	{
		cache = engine;
	}
	
	public static int size()
	{
		Map m = (Map)cache.get(FQN);
		return (m != null ? m.size() : 0);
	}
	
	public static int size(int topicId)
	{
		List posts = (List)cache.get(FQN, Integer.toString(topicId));
		return (posts == null ? 0 : posts.size());
	}
	
	public static Collection cachedTopics()
	{
		Map m = (Map)cache.get(FQN);
		if (m == null) {
			return new ArrayList();
		}
		
		return m.keySet();
	}
		
	public static List selectAllByTopicByLimit(int topicId, int start, int count)  
	{
		String tid = Integer.toString(topicId);
		
		List posts = (List)cache.get(FQN, tid);
		if (posts == null || posts.size() == 0) {
			PostDAO pm = DataAccessDriver.getInstance().newPostDAO();
			posts = pm.selectAllByTopic(topicId);
			
			for (Iterator iter = posts.iterator(); iter.hasNext(); ) {
				PostCommon.preparePostForDisplay((Post)iter.next());
			}
	
			Map topics = (Map)cache.get(FQN);
			if (topics == null || topics.size() == 0 || topics.size() < CACHE_SIZE) {
				cache.add(FQN, tid, posts);
			}
			else {
				if (!(topics instanceof LinkedHashMap)) {
					topics = new LinkedHashMap(topics) {
						protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
							return this.size() > CACHE_SIZE;
						}
					};
				}
				
				topics.put(tid, posts);
				cache.add(FQN, topics);
			}
		}
		
		int size = posts.size();
		return posts.subList(start, (size < start + count) ? size : start + count);
   }
	
	public static void remove(int topicId, int postId)
	{
		synchronized (FQN) {
			String tid = Integer.toString(topicId);
			
			List posts = (List)cache.get(FQN, tid);
			
			if (posts != null) {
				Post p = new Post();
				p.setId(postId);
				posts.remove(p);
				
				cache.add(FQN, tid, posts);
			}
		}
	}
	
	public static void update(int topicId, Post p)
	{
		String tid = Integer.toString(topicId);
		List posts = (List)cache.get(FQN, tid);
		if (posts != null && posts.contains(p)) {
			posts.set(posts.indexOf(p), p);
			cache.add(FQN, tid, posts);
		}
	}
	
	public static void append(int topicId, Post p)
	{
		String tid = Integer.toString(topicId);
		List posts = (List)cache.get(FQN, tid);
		if (posts != null && !posts.contains(p)) {
			posts.add(p);
			cache.add(FQN, tid, posts);
		}
	}
	
	public static void clearCache(int topicId)
	{
		cache.remove(FQN, Integer.toString(topicId));
	}
}

