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
 * Created on 25/07/2007 19:27:48
 * 
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jforum.entities.Forum;
import net.jforum.repository.ForumRepository;

/**
 * @author Rafael Steil
 * @version $Id: SearchOperation.java,v 1.5 2007/07/30 14:06:44 rafaelsteil Exp $
 */
public abstract class SearchOperation
{
	public abstract SearchResult performSearch(SearchArgs args);
	public abstract int totalRecords();
	public abstract void prepareForDisplay();
	public abstract List results();
	public abstract String viewTemplate();
	protected abstract int extractForumId(Object value);
	
	public final List filterResults(List results)
	{
		List l = new ArrayList();
		
		Map forums = new HashMap();
		
		for (Iterator iter = results.iterator(); iter.hasNext(); ) {
			Object currentObject = iter.next();
			
			Integer forumId = new Integer(this.extractForumId(currentObject));
			ForumFilterResult status = (ForumFilterResult)forums.get(forumId);
			
			if (status == null) {
				Forum f = ForumRepository.getForum(forumId.intValue());
				status = new ForumFilterResult(f);
				forums.put(forumId, status);
			}
			
			if (status.isValid()) {
				// TODO: decouple
				if (currentObject instanceof SearchPost) {
					((SearchPost)currentObject).setForum(status.getForum());
				}
				
				l.add(currentObject);
			}
		}
		
		return l;
	}
	
	private static class ForumFilterResult
	{
		private Forum forum;
		
		public ForumFilterResult(Forum forum)
		{
			this.forum = forum;
		}
		
		public Forum getForum() 
		{
			return this.forum;
		}
		
		public boolean isValid()
		{
			return this.forum != null;
		}
	}
}
