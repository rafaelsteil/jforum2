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
 * This file creation date: 25/02/2004 - 19:16:25
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.search;

import java.util.Date;

import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: SearchArgs.java,v 1.5 2007/07/30 21:59:21 rafaelsteil Exp $
 */
public class SearchArgs 
{
	private String keywords;
	private int author;
	private String orderDir = "DESC";
	private String orderBy;
	private boolean matchAllKeywords;
	private int forumId;
	private int initialRecord;
	private Date fromDate;
	private Date toDate;
	private String matchType;
	
	public void setMatchType(String matchType)
	{
		this.matchType = matchType;
	}
	
	public String getMatchType()
	{
		return this.matchType;
	}
	
	public void setDateRange(Date fromDate, Date toDate)
	{
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	
	public Date getFromDate()
	{
		return this.fromDate;
	}
	
	public Date getToDate()
	{
		return this.toDate;
	}
	
	public int fetchCount()
	{
		return SystemGlobals.getIntValue(ConfigKeys.TOPICS_PER_PAGE);
	}
	
	public void startFetchingAtRecord(int initialRecord)
	{
		this.initialRecord = initialRecord;
	}
	
	public int startFrom()
	{
		return this.initialRecord;
	}
	
	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}
	
	public void matchAllKeywords()
	{
		this.matchAllKeywords = true;
	}
	
	public void setAuthor(int author)
	{
		this.author = author;
	}
	
	public void setForumId(int forumId)
	{
		this.forumId = forumId;
	}
	
	public void setOrderBy(String orderBy)
	{
		this.orderBy = orderBy;
	}
	
	public void setOrderDir(String orderDir)
	{
		this.orderDir = orderDir;
	}
	
	public String[] getKeywords()
	{
		if (this.keywords == null || this.keywords.trim().length() == 0) {
			return new String[] {};
		}

		return this.keywords.trim().split(" ");
	}
	
	public String rawKeywords()
	{
		if (this.keywords == null) {
			return "";
		}
		
		return this.keywords.trim();
	}
	
	public boolean shouldMatchAllKeywords()
	{
		return this.matchAllKeywords;
	}
	
	public int getAuthor()
	{
		return this.author;
	}
	
	public int getForumId()
	{
		return this.forumId;
	}
	
	public String getOrderDir()
	{
		if (!"ASC".equals(this.orderDir) && !"DESC".equals(this.orderDir)) {
			return "DESC";
		}

		return this.orderDir;
	}
	
	public String getOrderBy()
	{
		return this.orderBy;
	}
}
