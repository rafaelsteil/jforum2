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
 * Created on 24/07/2007 12:23:05
 * 
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.search;

import java.io.IOException;

import net.jforum.entities.Post;
import net.jforum.exceptions.ForumException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;

/**
 * @author Rafael Steil
 * @version $Id: LuceneManager.java,v 1.16 2007/09/09 22:53:35 rafaelsteil Exp $
 */
public class LuceneManager implements SearchManager
{
	private LuceneSearch search;
	private LuceneSettings settings;
	private LuceneIndexer indexer;
	
	/**
	 * @see net.jforum.search.SearchManager#init()
	 */
	public void init()
	{
		try {
			Analyzer analyzer = (Analyzer)Class.forName(SystemGlobals.getValue(
				ConfigKeys.LUCENE_ANALYZER)).newInstance();
			
			this.settings = new LuceneSettings(analyzer);
			
			this.settings.useFSDirectory(SystemGlobals.getValue(ConfigKeys.LUCENE_INDEX_WRITE_PATH));
			
			this.removeLockFile();
			
			this.indexer = new LuceneIndexer(this.settings);
			
			this.search = new LuceneSearch(this.settings, 
				new LuceneContentCollector(this.settings));
			
			this.indexer.watchNewDocuDocumentAdded(this.search);
			
			SystemGlobals.setObjectValue(ConfigKeys.LUCENE_SETTINGS, this.settings);
		}
		
		catch (Exception e) {
			throw new ForumException(e);
		}
	}
	
	public LuceneSearch luceneSearch()
	{
		return this.search;
	}
	
	public LuceneIndexer luceneIndexer()
	{
		return this.indexer;
	}
	
	public void removeLockFile()
	{
		try {
			if (IndexReader.isLocked(this.settings.directory())) {
				IndexReader.unlock(this.settings.directory());
			}
		}
		catch (IOException e) {
			throw new ForumException(e);
		}
	}
	
	/**
	 * @see net.jforum.search.SearchManager#create(net.jforum.entities.Post)
	 */
	public void create(Post post)
	{
		this.indexer.create(post);
	}
	
	/**
	 * @see net.jforum.search.SearchManager#update(net.jforum.entities.Post)
	 */
	public void update(Post post)
	{
		this.indexer.update(post);
	}

	/**
	 * @see net.jforum.search.SearchManager#search(net.jforum.search.SearchArgs)
	 */
	public SearchResult search(SearchArgs args)
	{
		return this.search.search(args);
	}
	
	/**
	 * @see net.jforum.search.SearchManager#delete(net.jforum.entities.Post)
	 */
	public void delete(Post p)
	{
		this.indexer.delete(p);
	}
}
