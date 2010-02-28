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
 * This file creation date: 08/01/2004 / 22:11:13
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.security;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author Rafael Steil
 * @version $Id: RoleCollection.java,v 1.11 2006/08/24 01:07:02 rafaelsteil Exp $
 */
public class RoleCollection extends LinkedHashMap implements Serializable
{
	public void add(Role role) 
	{
		super.put(role.getName(), role);
	}
	
	/**
	 * Gets a role.
	 * 
	 * @param name The role's name
	 * @return <code>Role</code> object if a role with a name equals to the name passed
	 * as argument is found, or <code>null</code> otherwise.
	 */
	public Role get(String name)
	{
		return (Role)super.get(name);
	}
	
	/** 
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() 
	{
		StringBuffer sb = new StringBuffer(512);
		
		for (Iterator iter = this.values().iterator(); iter.hasNext(); ) {
			sb.append(iter.next()).append('\n');
		}
		
		return sb.toString();
	}
}
