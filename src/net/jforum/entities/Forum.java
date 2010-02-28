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
 * This file creating date: Feb 23, 2003 / 12:25:04 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.io.Serializable;
import java.util.List;

import net.jforum.repository.ForumRepository;

/**
 * Represents a specific forum.
 * 
 * @author Rafael Steil
 * @version $Id: Forum.java,v 1.13 2007/07/28 14:17:09 rafaelsteil Exp $
 */
public class Forum implements Serializable
{
	private int id;
	private int idCategories;
	private String name;
	private String description;
	private int order;
	private int totalTopics;
	private int totalPosts;
	private int lastPostId;
	private boolean moderated;
	private boolean unread;
	private LastPostInfo lpi;

	public Forum() { }
	
	public Forum(int forumId) {
		this.id = forumId;
	}
	
	public Forum(Forum f)
	{
		this.description = f.getDescription();
		this.id = f.getId();
		this.idCategories = f.getCategoryId();
		this.lastPostId = f.getLastPostId();
		this.moderated = f.isModerated();
		this.name = f.getName();
		this.order = f.getOrder();
		this.totalPosts = f.getTotalPosts();
		this.totalTopics = f.getTotalTopics();
		this.unread = f.getUnread();
		this.lpi = f.getLastPostInfo();
	}
	
	public void setLastPostInfo(LastPostInfo lpi) {
		this.lpi = lpi;
	}
	
	public LastPostInfo getLastPostInfo() {
		return this.lpi;
	}
	
	public List getModeratorList() 
	{
		return ForumRepository.getModeratorList(this.id);
	}
	
	/**
	 * Gets the forum's description
	 * 
	 * @return String with the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the forum's ID
	 * 
	 * @return int value representing the ID
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the category which the forum belongs to
	 * 
	 * @return int value representing the ID of the category 
	 */
	public int getCategoryId() {
		return this.idCategories;
	}

	/**
	 * Gets the ID of the last post
	 * 
	 * @return int value representing the ID of the post
	 */
	public int getLastPostId() {
		return this.lastPostId;
	}

	/**
	 * Checks if is a moderated forum
	 * 
	 * @return boolean value. <code>true</code> if the forum is moderated, <code>false</code> if not.
	 */
	public boolean isModerated() {
		return this.moderated;
	}

	/**
	 * Gets the name of the forum
	 * 
	 * @return String with the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the order
	 * 
	 * @return int value representing the order of the forum
	 */
	public int getOrder() {
		return this.order;
	}

	/**
	 * Gets the total number of topics posted in the forum
	 * 
	 * @return int value with the total number of the topics
	 */
	public int getTotalTopics() {
		return this.totalTopics;
	}
	
	public boolean getUnread() {
		return this.unread;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id The id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the category id
	 * 
	 * @param idCategories The ID of the category  to set to the forum
	 */
	public void setIdCategories(int idCategories) {
		this.idCategories = idCategories;
	}

	/**
	 * Sets the ID of the last post
	 * 
	 * @param lastPostId The post id
	 */
	public void setLastPostId(int lastPostId) {
		this.lastPostId = lastPostId;
	}

	/**
	 * Sets the moderated flag to the forum
	 * 
	 * @param moderated <code>true</code> or <code>false</code>
	 */
	public void setModerated(boolean moderated) {
		this.moderated = moderated;
	}

	/**
	 * Sets the name of the forum
	 * 
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the order.
	 * 
	 * @param order The order to set
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	
	public void setUnread(boolean status) {
		this.unread = status;
	}

	/**
	 * Sets the total number of topics
	 * 
	 * @param totalTopics int value with the total number of topics
	 */
	public void setTotalTopics(int totalTopics) {
		this.totalTopics = totalTopics;
	}
	
	public int getTotalPosts() {
		return this.totalPosts;
	}
	
	public void setTotalPosts(int totalPosts) {
		this.totalPosts = totalPosts;
	}
	
	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) 
	{
		return ((o instanceof Forum) && (((Forum)o).getId() == this.id));
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
	public String toString() {
		return "[" + this.name + ", id=" + this.id + ", order=" + this.order + "]";
	}
}
