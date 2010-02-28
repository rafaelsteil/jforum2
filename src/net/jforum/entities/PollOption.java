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
 * This file creation date: 21/05/2004 - 15:17:46
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.io.Serializable;

/**
 * @author David Almilli
 * @version $Id: PollOption.java,v 1.4 2006/08/20 22:47:36 rafaelsteil Exp $
 */
public class PollOption implements Serializable {
	private int id;
	private int pollId;
	private String text;
	private int voteCount;

	private Poll poll;
	
	public PollOption() {}
	
	public PollOption(int id, String text, int voteCount) {
		this.id = id;
		this.text = text;
		this.voteCount = voteCount;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPollId() {
		return pollId;
	}
	public void setPollId(int pollId) {
		this.pollId = pollId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	
	public int getVotePercentage() {
		int percent = 0;
		if (poll != null) {
			int totalCount = poll.getTotalVotes();
			percent = Math.round(100f*voteCount/totalCount);
		}
		return percent;
	}
	
	public Poll getPoll() {
		return poll;
	}
	
	protected void setPoll(Poll poll) {
		this.poll = poll;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new StringBuffer(128)
			.append('[')
			.append(this.id)
			.append(", ")
			.append(this.text)
			.append(", ")
			.append(this.voteCount)
			.append(']')
			.toString();
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof PollOption)) {
			return false;
		}
		
		PollOption po = (PollOption)o;
		return po.getId() == this.id
			&& po.getText().equals(this.text)
			&& po.getVoteCount() == this.voteCount;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		int result = 17;
		
		result *= 37 + this.id;
		result *= 37 + this.text.hashCode();
		result *= 37 + this.voteCount;
		
		return result;
	}
}
