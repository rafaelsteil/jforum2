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
 * Created on 18/07/2007 14:03:15
 * 
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.jforum.entities.Post;

import org.apache.log4j.xml.DOMConfigurator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;

/**
 * @author Rafael Steil
 * @version $Id: LuceneSearchTestCase.java,v 1.31 2007/09/09 22:53:36 rafaelsteil Exp $
 */
public class LuceneSearchTestCase extends TestCase
{
	private static boolean logInitialized;
	
	private LuceneSearch search;
	private LuceneSettings settings;
	private LuceneIndexer indexer;

	public void testFivePostsInTwoForumsSearchOneForumAndTwoValidTermsAndOneInvalidTermExpectThreeResults()
	{
		List l = this.createThreePosts();
		((Post)l.get(0)).setForumId(1);
		((Post)l.get(1)).setForumId(2);
		((Post)l.get(2)).setForumId(1);
		
		this.indexer.create((Post)l.get(0));
		this.indexer.create((Post)l.get(1));
		this.indexer.create((Post)l.get(2));
		
		// Post 4
		Post p = this.newPost();
		p.setText("It introduces you to searching, sorting, filtering and highlighting [...]");
		p.setForumId(1);
		this.indexer.create(p);
		
		// Post 5
		p = this.newPost();
		p.setText("How to integrate lucene into your applications");
		p.setForumId(2);
		l.add(p);
		
		this.indexer.create(p);
		
		// Search
		SearchArgs args = new SearchArgs();
		args.setForumId(1);
		args.setKeywords("open lucene xpto authoritative");
		
		List results = this.search.search(args).records();
		
		Assert.assertEquals(3, results.size());
	}
	
	public void testORExpressionUsingThreePostsSearchTwoTermsExpectThreeResults()
	{
		List l = this.createThreePosts();
		
		this.indexer.create((Post)l.get(0));
		this.indexer.create((Post)l.get(1));
		this.indexer.create((Post)l.get(2));
		
		// Search
		SearchArgs args = new SearchArgs();
		args.setKeywords("open lucene");
		
		List results = this.search.search(args).records();
		
		Assert.assertEquals(3, results.size());
	}

	private List createThreePosts()
	{
		List l = new ArrayList();
		
		// 1
		Post p = this.newPost();
		p.setText("lucene is a gem in the open source world");
		l.add(p);
		
		// 2
		p = this.newPost();
		p.setText("lucene in action is the authoritative guide to lucene");
		l.add(p);
		
		// 3
		p = this.newPost();
		p.setText("Powers search in surprising places [...] open to everyone");
		l.add(p);
		
		return l;
	}
	
	public void testANDExpressionUsingTwoPostsWithOneCommonWordSearchTwoTermsExpectOneResult()
	{
		// 1
		Post p = this.newPost();
		p.setText("a regular text with some magic word");
		this.indexer.create(p);
		
		// 2
		p = this.newPost();
		p.setText("say shazan to see the magic happen");
		this.indexer.create(p);
		
		// Search
		SearchArgs args = new SearchArgs();
		args.matchAllKeywords();
		args.setKeywords("magic regular");
		
		List results = this.search.search(args).records();
		
		Assert.assertEquals(1, results.size());
	}
	
	public void testThreePostsSearchContentsExpectOneResult()
	{
		// 1
		Post p = this.newPost();
		p.setSubject("java");
		this.indexer.create(p);
		
		// 2
		p = this.newPost();
		p.setSubject("something else");
		this.indexer.create(p);
		
		// 3
		p = this.newPost();
		p.setSubject("debug");
		this.indexer.create(p);
		
		// Search
		SearchArgs args = new SearchArgs();
		args.setKeywords("java");
		
		List results = this.search.search(args).records();
		
		Assert.assertEquals(1, results.size());
	}
	
	public void testTwoDifferentForumsSearchOneExpectOneResult()
	{
		Post p1 = this.newPost();
		p1.setForumId(1);
		this.indexer.create(p1);
		
		Post p2 = this.newPost();
		p2.setForumId(2);
		this.indexer.create(p2);
		
		SearchArgs args = new SearchArgs();
		args.setForumId(1);
		
		List results = this.search.search(args).records();
		
		Assert.assertEquals(1, results.size());
	}
	
	private Post newPost() 
	{
		Post p = new Post();
		
		p.setText("");
		p.setTime(new Date());
		p.setSubject("");
		p.setPostUsername("");
		
		return p;
	}
	
	protected void setUp() throws Exception
	{
		if (!logInitialized) {
			DOMConfigurator.configure(this.getClass().getResource("/log4j.xml"));
			logInitialized = true;
		}
		
		this.settings = new LuceneSettings(new StandardAnalyzer());
		
		this.settings.useRAMDirectory();
		
		this.indexer = new LuceneIndexer(this.settings);
		
		this.search = new LuceneSearch(this.settings, 
			new FakeResultCollector());
		
		this.indexer.watchNewDocuDocumentAdded(this.search);
	}
	
	private static class FakeResultCollector implements LuceneResultCollector
	{
		public List collect(SearchArgs args, Hits hits, Query query)
		{
			List l = new ArrayList();
			
			for (int i = 0; i < hits.length(); i++) {
				// We really don't care about the results, only how many they are
				l.add(""); 
			}
			
			return l;
		}
	}
}
