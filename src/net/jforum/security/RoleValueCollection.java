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
 * This file creation date: 08/01/2004 / 21:41:11
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * @author Rafael Steil
 * @version $Id: RoleValueCollection.java,v 1.9 2006/08/24 01:07:01 rafaelsteil Exp $
 */
public class RoleValueCollection extends LinkedHashSet implements Serializable 
{
	/**
	 * Gets a <code>RoleValue</code> by its name.
	 * 
	 * @param valueName The <code>RoleValue</code> name
	 * @return The <code>RoleValue</code> object if found, or <code>null</code> if not found
	 */
	public RoleValue get(String valueName)
	{
		for (Iterator iter = this.iterator(); iter.hasNext(); ) {
			RoleValue v = (RoleValue)iter.next();
			
			if (v.getValue().equals(valueName)) {
				return v;
			}
		}
		
		return null;
	}
	
	/** 
	 * @see java.util.HashSet#contains(java.lang.Object)
	 */
	public boolean contains(Object o) 
	{
		boolean c = super.contains(o);
		return c;
	}
	
	/** 
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(Object o) 
	{
		if (!(o instanceof RoleValue)) {
			throw new IllegalArgumentException("Object passed as parameter is not a RoleValue type");
		}
		
		return super.add(o);
	}

	/** 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection c) 
	{
		boolean status = true;
		
		for (Iterator iter = c.iterator(); iter.hasNext(); ) {
			status = this.add(iter.next());
		}
		
		return status;
	}
}
