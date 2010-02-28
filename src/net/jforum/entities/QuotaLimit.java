/*
 * Copyright (c) 2003, 2004 Rafael Steil
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
 * Created on Jan 17, 2005 4:22:27 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

/**
 * @author Rafael Steil
 * @version $Id: QuotaLimit.java,v 1.5 2005/07/26 03:04:51 rafaelsteil Exp $
 */
public class QuotaLimit
{
	public static final int KB = 1;
	public static final int MB = 2;
	
	private int id;
	private String description;
	private int size;
	private int type;
	
	/**
	 * Checks if the size passed as argument
	 * is greater than the quota's limit.
	 * 
	 * @param size The size to check
	 * @return <code>true</code> if the size is greater than
	 * quota's limit. 
	 */
	public boolean exceedsQuota(long size)
	{
		if (this.type == QuotaLimit.KB) {
			return (size > this.size * 1024);
		}
		
		return (size > this.size * 1024 * 1024);
	}
	
	public int getSizeInBytes()
	{
		if (this.type == QuotaLimit.KB) {
			return (this.size * 1024);
		}
		
		return (this.size * 1024 * 1024);
	}
	
	/**
	 * @return Returns the description.
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * @return Returns the id.
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * @param id The id to set.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * @return Returns the size.
	 */
	public int getSize()
	{
		return this.size;
	}
	
	/**
	 * @param size The size to set.
	 */
	public void setSize(int size)
	{
		this.size = size;
	}
	
	/**
	 * @return Returns the type.
	 */
	public int getType()
	{
		return this.type;
	}
	
	/**
	 * @param type The type to set.
	 */
	public void setType(int type)
	{
		this.type = type;
	}
}
