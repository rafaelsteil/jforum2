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
 * Created on 07/12/2006 20:59:12
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.io.Serializable;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

/**
 * @author Rafael Steil
 * @version $Id: Banlist.java,v 1.3 2006/12/10 23:08:21 rafaelsteil Exp $
 */
public class Banlist implements Serializable
{
	private int id;
	private int userId;
	private String ip;
	private String email;
	
	/**
	 * @return the id
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * @return the userId
	 */
	public int getUserId()
	{
		return this.userId;
	}
	
	/**
	 * @return the ip
	 */
	public String getIp()
	{
		return this.ip;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return this.email;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public boolean matches(Banlist b)
	{
		boolean status = false;
		
		if (this.matchesUserId(b) || this.matchesEmail(b)) {
			status = true;
		}
		else if (!StringUtils.isEmpty(b.getIp()) && !StringUtils.isEmpty(this.getIp()))  {
			if (b.getIp().equalsIgnoreCase(this.getIp())) {
				status = true;
			}
			else {
				status = this.matchIp(b);
			}
		}
		
		return status;
	}

	private boolean matchesEmail(Banlist b)
	{
		return (!StringUtils.isEmpty(b.getEmail()) && b.getEmail().equals(this.getEmail()));
	}

	private boolean matchesUserId(Banlist b)
	{
		return b.getUserId() > 0 && this.getUserId() > 0 && b.getUserId() == this.getUserId();
	}

	private boolean matchIp(Banlist b)
	{
		boolean status = false;
		
		StringTokenizer userToken = new StringTokenizer(b.getIp(), ".");
		StringTokenizer thisToken = new StringTokenizer(this.getIp(), ".");
		
		if (userToken.countTokens() == thisToken.countTokens()) {
			String[] userValues = this.tokenizerAsArray(userToken);
			String[] thisValues = this.tokenizerAsArray(thisToken);
			
			status = this.compareIpValues(userValues, thisValues);
		}
		return status;
	}

	private boolean compareIpValues(String[] userValues, String[] thisValues)
	{
		boolean helperStatus = true;
		boolean onlyStars = true;
		
		for (int i = 0; i < thisValues.length; i++) {
			if (thisValues[i].charAt(0) != '*') {
				onlyStars = false;
				
				if (!thisValues[i].equals(userValues[i])) {
					helperStatus = false;
				}
			}
		}
		
		return helperStatus && !onlyStars;
	}

	private String[] tokenizerAsArray(StringTokenizer token)
	{
		String[] values = new String[token.countTokens()];
		
		for (int i = 0; token.hasMoreTokens(); i++) {
			values[i] = token.nextToken();
		}
		
		return values;
	}
}
