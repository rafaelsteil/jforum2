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
 * Created on 27/07/2007 15:10:51
 * 
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.search;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Post;
import net.jforum.exceptions.ForumException;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;

/**
 * @author Rafael Steil
 * @version $Id: LuceneContentCollector.java,v 1.8 2007/07/30 14:06:44 rafaelsteil Exp $
 */
public class LuceneContentCollector implements LuceneResultCollector
{
	private LuceneSettings settings;
	
	public LuceneContentCollector(LuceneSettings settings)
	{
		this.settings = settings;
	}
	
	/**
	 * @see net.jforum.search.LuceneResultCollector#collect(SearchArgs, org.apache.lucene.search.Hits, org.apache.lucene.search.Query)
	 */
	public List collect(SearchArgs args, Hits hits, Query query)
	{
		try {
			int[] postIds = new int[Math.min(args.fetchCount(), hits.length())];
			
			for (int docIndex = args.startFrom(), i = 0; 
				docIndex < args.startFrom() + args.fetchCount() && docIndex < hits.length(); 
				docIndex++, i++) {
				Document doc = hits.doc(docIndex);
				postIds[i] = Integer.parseInt(doc.get(SearchFields.Keyword.POST_ID));
			}
			
			return this.retrieveRealPosts(postIds, query);
		}
		catch (Exception e) {
			throw new ForumException(e.toString(), e);
		}
	}

	private List retrieveRealPosts(int[] postIds, Query query) throws IOException
	{
		List posts = DataAccessDriver.getInstance().newLuceneDAO().getPostsData(postIds);
		
		for (Iterator iter = posts.iterator(); iter.hasNext(); ) {
			Post post = (Post)iter.next();
			
			Scorer scorer = new QueryScorer(query);
			Highlighter highlighter = new Highlighter(scorer);
			
			TokenStream tokenStream = this.settings.analyzer().tokenStream(
				SearchFields.Indexed.CONTENTS, new StringReader(post.getText()));

			String fragment = highlighter.getBestFragment(tokenStream, post.getText());
			post.setText(fragment != null ? fragment : post.getText());
		}
		
		return posts;
	}
}
