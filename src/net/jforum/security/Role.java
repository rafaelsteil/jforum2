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
 * This file creation date: 08/01/2004 / 21:34:57
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.security;

import java.io.Serializable;

/**
 * @author Rafael Steil
 * @version $Id: Role.java,v 1.9 2006/08/24 01:07:02 rafaelsteil Exp $
 */
public class Role implements Serializable
{
	private int id;
	private String name;
	private RoleValueCollection roleValues = new RoleValueCollection();
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public RoleValueCollection getValues()
	{
		return this.roleValues;
	}
	
	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) 
	{
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof Role)) {
			return false;
		}
		
		return (((Role)o).getId() == this.id);
	}

	/** 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return this.id;
	}
	
	/** 
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return "[name=" + this.name + ", values=(" + this.roleValues + ")]";
	}
}
