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
 * This file creating date: Feb 17, 2003 / 10:45:42 PM
 * The JForum Project 
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.io.Serializable;

/**
 * Represents an user ranking in the System.
 * An user ranking is just a given "status" which some user have
 * basead on the number of messages posted by them.  
 *  
 * @author Rafael Steil
 * @version $Id: Ranking.java,v 1.8 2006/12/02 03:19:55 rafaelsteil Exp $
 */
public class Ranking implements Serializable
{
	private int id;
	private String title;
	private boolean special;
	private String image;
	private int min;
	
	public Ranking() {}
	
	/**
	 * @return int
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return String
	 */
	public String getImage() {
		return this.image;
	}

	/**
	 * @return String
	 */
	public boolean isSpecial() {
		return this.special;
	}

	/**
	 * @return String
	 */
	public String getTitle() 
	{
		return (this.title == null ? "" : this.title);
	}

	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the image.
	 * @param image The image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Sets the special.
	 * @param special The special to set
	 */
	public void setSpecial(boolean special) {
		this.special = special;
	}

	/**
	 * Sets the title.
	 * @param title The title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public int getMin() {
		return this.min;
	}

	/**
	 * @param i
	 */
	public void setMin(int i) {
		this.min = i;
	}

	public boolean equals(Object o)
	{
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof Ranking)) {
			return false;
		}
		
		return ((Ranking)o).getId() == this.getId();
	}
	
	public int hashCode()
	{
		return this.getId();
	}
}
