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
 * This file creation date: 15/08/2003 / 21:04:47
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

/**
 * @author Rafael Steil
 * @version  $Id: Config.java,v 1.6 2006/08/23 02:13:45 rafaelsteil Exp $
 */
public class Config 
{
	private int id;
	private String name;
	private String value;
	
	public Config() {}
	
	/**
	 * Gets the entry name
	 * @return The Entry name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the entry value
	 * @return The Entry value
	 */
	public String getValue() {
		return this.value;
	}
	
	public int getId() {
		return this.id;
	}

	/**
	 * Sets the entry name
	 * 
	 * @param string The entry name to set
	 */
	public void setName(String string) {
		this.name = string;
	}

	/**
	 * Sets the entry value
	 * 
	 * @param string The entry value to set
	 */
	public void setValue(String string) {
		this.value = string;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
