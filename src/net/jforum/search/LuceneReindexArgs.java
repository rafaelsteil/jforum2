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
 * Created on 05/08/2007 12:14:52
 * 
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.search;

import java.util.Date;

/**
 * @author Rafael Steil
 * @version $Id: LuceneReindexArgs.java,v 1.1 2007/08/05 16:29:21 rafaelsteil Exp $
 */
public class LuceneReindexArgs
{
	public static final int TYPE_UNKNOWN = 0;
	public static final int TYPE_DATE = 1;
	public static final int TYPE_MESSAGE = 2;
	
	private Date fromDate;
	private Date toDate;
	private int firstPostId;
	private int lastPostId;
	private int type;
	private boolean avoidDuplicated;
	
	public LuceneReindexArgs(Date fromDate, Date toDate, int firstPostId, 
		int lastPostId, boolean avoidDuplicated, int type)
	{
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.firstPostId = firstPostId;
		this.lastPostId = lastPostId;
		this.avoidDuplicated = avoidDuplicated;
		this.type = type;
	}

	/**
	 * @return the from
	 */
	public Date getFromDate()
	{
		return this.fromDate;
	}

	/**
	 * @return the to
	 */
	public Date getToDate()
	{
		return this.toDate;
	}

	/**
	 * @return the fetchStart
	 */
	public int getFirstPostId()
	{
		return this.firstPostId;
	}

	/**
	 * @return the avoidDuplicated
	 */
	public boolean avoidDuplicatedRecords()
	{
		return this.avoidDuplicated;
	}
	
	/**
	 * @return the fetchEnd
	 */
	public int getLastPostId()
	{
		return this.lastPostId;
	}
	
	public boolean filterByDate()
	{
		return this.type == TYPE_DATE && this.getFromDate() != null && this.getToDate() != null;
	}
	
	public boolean filterByMessage()
	{
		return this.type == TYPE_MESSAGE && this.getLastPostId() > this.getFirstPostId();
	}
}
