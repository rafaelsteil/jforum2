/*
 * Copyright (c) 2003, Rafael Steil
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
 * Created on 04/12/2004 16:00:46
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.http;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author Rafael Steil
 * @version $Id: FakeHttpSession.java,v 1.5 2007/08/01 22:30:04 rafaelsteil Exp $
 */
public class FakeHttpSession implements HttpSession 
{
	private Map attributes = new HashMap();

	/** 
	 * @see javax.servlet.http.HttpSession#getCreationTime()
	 */
	public long getCreationTime() {
		
		return 0;
	}

	/** 
	 * @see javax.servlet.http.HttpSession#getId()
	 */
	public String getId() {
		return "jforum-testcase";
	}

	/** 
	 * @see javax.servlet.http.HttpSession#getLastAccessedTime()
	 */
	public long getLastAccessedTime() {
		
		return 0;
	}

	/** 
	 * @see javax.servlet.http.HttpSession#getServletContext()
	 */
	public ServletContext getServletContext() {
		
		return null;
	}

	/** 
	 * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
	 */
	public void setMaxInactiveInterval(int arg0) {
		
	}

	/** 
	 * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
	 */
	public int getMaxInactiveInterval() {
		
		return 0;
	}

	public HttpSessionContext getSessionContext() {
		
		return null;
	}

	/** 
	 * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	public Object getValue(String arg0) {
		
		return null;
	}

	/** 
	 * @see javax.servlet.http.HttpSession#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		
		return null;
	}

	public String[] getValueNames() {
		
		return null;
	}

	/** 
	 * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}

	public void putValue(String arg0, Object arg1) {
		
	}

	/** 
	 * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String name) {
		this.attributes.remove(name);
	}

	public void removeValue(String arg0) {
		
	}

	/** 
	 * @see javax.servlet.http.HttpSession#invalidate()
	 */
	public void invalidate() {
		
	}

	/** 
	 * @see javax.servlet.http.HttpSession#isNew()
	 */
	public boolean isNew() {
		
		return false;
	}
}
