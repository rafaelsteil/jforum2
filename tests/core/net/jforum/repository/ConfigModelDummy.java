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
 * Created on Dec 29, 2004 1:54:13 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.util.List;

import net.jforum.dao.ConfigDAO;
import net.jforum.entities.Config;

/**
 * @author Rafael Steil
 * @version $Id: ConfigModelDummy.java,v 1.6 2007/08/01 22:30:05 rafaelsteil Exp $
 */
public class ConfigModelDummy implements ConfigDAO
{

	/**
	 * @see net.jforum.dao.ConfigDAO#insert(net.jforum.entities.Config)
	 */
	public void insert(Config config) 	{
		

	}

	/**
	 * @see net.jforum.dao.ConfigDAO#update(net.jforum.entities.Config)
	 */
	public void update(Config config)
	{
		

	}

	/**
	 * @see net.jforum.dao.ConfigDAO#delete(net.jforum.entities.Config)
	 */
	public void delete(Config config)
	{
		

	}

	/**
	 * @see net.jforum.dao.ConfigDAO#selectAll()
	 */
	public List selectAll()
	{
		
		return null;
	}

	/**
	 * @see net.jforum.dao.ConfigDAO#selectByName(java.lang.String)
	 */
	public Config selectByName(String name) 
	{
		
		return null;
	}

}
