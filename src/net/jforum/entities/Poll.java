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
 * This file creation date: 21/05/2004 - 14:22:16
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * @author David Almilli
 * @version $Id: Poll.java,v 1.4 2006/08/20 22:47:35 rafaelsteil Exp $
 */
public class Poll implements Serializable {
	private int id;
	private int topicId;
	private int length;
	private String label;
	private Date startTime;
	private transient PollChanges pollChanges;
	private List options = new ArrayList();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTopicId() {
		return topicId;
	}
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void addOption(PollOption option) {
		options.add(option);
		option.setPoll(this);
	}
	public void removeOption(PollOption option) {
		if (options.remove(option)) {
			option.setPoll(null);
		}
	}
	public List getOptions() {
		return options;
	}
	
	public void setChanges(PollChanges changes) {
		this.pollChanges = changes;
	}
	
	public PollChanges getChanges() {
		return this.pollChanges;
	}
	
	public int getTotalVotes() {
		int votes = 0;
		Iterator iter = options.iterator();
		while (iter.hasNext()) {
			PollOption option = (PollOption)iter.next();
			votes += option.getVoteCount();
		}
		return votes;
	}
	
	public boolean isOpen() {
		if (length == 0) {
			return true;
		}
		Calendar endTime = Calendar.getInstance();
		endTime.setTime(startTime);
		endTime.add(Calendar.DAY_OF_YEAR, length);
		return System.currentTimeMillis() < endTime.getTimeInMillis();
	}
}
