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
 * Created on 20/10/2004 22:59:58
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.rss;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a RSS document
 * 
 * @author Rafael Steil
 * @version $Id: RSS.java,v 1.4 2006/08/23 02:13:38 rafaelsteil Exp $
 */
public class RSS 
{
	private List itens;
	private String title;
	private String description;
	private String encoding;
	private String link;
	
	/**
	 * Creates a new RSS document.
	 * 
	 * @param title The document title
	 * @param description The document description
	 * @param encoding The character encoding
	 * @param link The main document link
	 */
	public RSS(String title, String description, String encoding, String link)
	{
		this.itens = new ArrayList();
		this.title = title;
		this.description = description;
		this.encoding = encoding;
		this.link = link;
	}
	
	/**
	 * Gets the main document link
	 * @return The document link
	 */
	public String getLink()
	{
		return this.link;
	}
	
	/**
	 * Gets he document title 
	 * @return The document title
	 */
	public String getTitle()
	{
		return this.title;
	}
	
	/**
	 * Gets the document description
	 * @return The document description
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Gets the document character encoding
	 * @return The encoding
	 */
	public String getEncoding()
	{
		return this.encoding;
	}
	
	/**
	 * Gets all <code>RSSItem</code> instances related
	 * to this RSS document.
	 * 
	 * @return <code>java.util.List</code> with the entries
	 */
	public List getItens()
	{
		return this.itens;
	}
	
	/**
	 * Add a new item to the RSS document
	 * 
	 * @param entry <code>RSSItem</code> object containing the item information 
	 */
	public void addItem(RSSItem item)
	{
		this.itens.add(item);
	}
}
