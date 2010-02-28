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
 * Created on Dec 29, 2004 2:00:00 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: MostUsersEverOnline.java,v 1.5 2006/08/23 02:13:46 rafaelsteil Exp $
 */
public class MostUsersEverOnline implements Serializable
{
	private int total;
	private String date;
	private long timeInMillis;
	
	public MostUsersEverOnline() {}
	
	public int getTotal() {
		return this.total;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public long getTimeInMillis() {
		return this.timeInMillis;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setTimeInMillis(long time) {
		this.timeInMillis = time;
		
		SimpleDateFormat df = new SimpleDateFormat(SystemGlobals.getValue(ConfigKeys.DATE_TIME_FORMAT));
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(time);
		
		this.setDate(df.format(gc.getTime()));
	}
}
