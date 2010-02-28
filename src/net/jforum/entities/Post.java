/*
 * Copyright (c)Rafael Steil
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
 * This file creating date: Feb 23, 2003 / 1:02:01 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.io.Serializable;
import java.util.Date;

import net.jforum.view.forum.common.ViewCommon;

/**
 * Represents every message post in the system.
 * 
 * @author Rafael Steil
 * @version $Id: Post.java,v 1.15 2007/07/25 03:08:15 rafaelsteil Exp $
 */
public class Post implements Serializable 
{
	private int id;
	private int topicId;
	private int forumId;
	private String formatedTime;
	private int userId;
	private Date time;
	private String text;
	private String subject;
	private String postUsername;
	private boolean bbCodeEnabled = true;
	private boolean htmlEnabled = true;
	private boolean smiliesEnabled = true;
	private boolean signatureEnabled = true;
	private Date editTime;
	private int editCount;
	private String userIp;
	private boolean canEdit;
	private KarmaStatus karma;
	private boolean hasAttachments;
	private boolean moderate;

	public Post() { }
	
	public Post(int postId)
	{
		this.id = postId;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param p The Post to make a copy from
	 */
	public Post(Post p)
	{
		this.setBbCodeEnabled(p.isBbCodeEnabled());
		this.setCanEdit(p.getCanEdit());
		this.setEditCount(p.getEditCount());
		this.setEditTime(p.getEditTime());
		this.setFormatedTime(p.getFormatedTime());
		this.setForumId(p.getForumId());
		this.setHtmlEnabled(p.isHtmlEnabled());
		this.setId(p.getId());
		this.setPostUsername(p.getPostUsername());
		this.setSignatureEnabled(p.isSignatureEnabled());
		this.setSmiliesEnabled(p.isSmiliesEnabled());
		this.setSubject(p.getSubject());
		this.setText(p.getText());
		this.setTime(p.getTime());
		this.setTopicId(p.getTopicId());
		this.setUserId(p.getUserId());
		this.setUserIp(p.getUserIp());
		this.setKarma(new KarmaStatus(p.getKarma()));
		this.setModerate(p.isModerationNeeded());
		this.hasAttachments(p.hasAttachments());
	}
	
	public void setModerate(boolean status)
	{
		this.moderate = status;
	}
	
	public boolean isModerate()
	{
		return this.isModerationNeeded();
	}
	
	public boolean isModerationNeeded()
	{
		return this.moderate;
	}
	
	public KarmaStatus getKarma()
	{
		return this.karma;
	}
	
	public void setKarma(KarmaStatus karma)
	{
		this.karma = karma;
	}
	
	/**
	 * Checks if the BB code is enabled
	 * 
	 * @return boolean value representing the result
	 */
	public boolean isBbCodeEnabled() {
		return this.bbCodeEnabled;
	}

	/**
	 * Gets the total number of times the post was edited
	 * 
	 * @return int value with the total number of times the post was edited
	 */
	public int getEditCount() {
		return this.editCount;
	}

	/**
	 * Gets the edit time of the post
	 * 
	 * @return long value representing the time
	 */
	public Date getEditTime() {
		return this.editTime;
	}

	/**
	 * Gets the forum's id the post is associated
	 * 
	 * @return int value with the id of the forum
	 */
	public int getForumId() {
		return this.forumId;
	}

	/**
	 * Checks if HTML is enabled in the topic
	 * 
	 * @return boolean value representing the result
	 */
	public boolean isHtmlEnabled() {
		return this.htmlEnabled;
	}

	/**
	 * Gets the ID of the post
	 * 
	 * @return int value with the ID
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Gets the username of the user ( an anonymous user ) that have posted the message
	 * 
	 * @return String with the username
	 */
	public String getPostUsername() {
		return this.postUsername;
	}

	/**
	 * Checks if signature is allowd in the message
	 * 
	 * @return boolean representing the result
	 */
	public boolean isSignatureEnabled() {
		return this.signatureEnabled;
	}

	/**
	 * Checks if smart Smilies are enabled :)
	 * 
	 * @return boolean representing the result
	 */
	public boolean isSmiliesEnabled() {
		return this.smiliesEnabled;
	}

	/**
	 * Gets the time, represented as long, of the message post
	 * 
	 * @return long representing the post time
	 */
	public Date getTime() {
		return this.time;
	}

	/**
	 * Gets the id of the topic this message is associated
	 * 
	 * @return int value with the topic id
	 */
	public int getTopicId() {
		return this.topicId;
	}

	/**
	 * Gets the ID of the user that have posted the message
	 * 
	 * @return int value with the user id
	 */
	public int getUserId() {
		return this.userId;
	}

	/**
	 * Gets the IP of the user who have posted the message
	 * 
	 * @return String value with the user IP
	 */
	public String getUserIp() {
		return this.userIp;
	}
	/**
	 * Sets the status for BB code in the message
	 * 
	 * @param bbCodeEnabled <code>true</code> or <code>false</code>, depending the intention
	 */
	public void setBbCodeEnabled(boolean bbCodeEnabled) {
		this.bbCodeEnabled = bbCodeEnabled;
	}

	/**
	 * Sets the count times the message was edited
	 * 
	 * @param editCount The count time
	 */
	public void setEditCount(int editCount) {
		this.editCount = editCount;
	}

	/**
	 * Sets the edit time the message was last edited
	 * 
	 * @param editTime long value representing the time
	 */
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	/**
	 * Sets the id of the forum this message belongs to
	 * 
	 * @param forumId The forum's id
	 */
	public void setForumId(int forumId) {
		this.forumId = forumId;
	}

	/**
	 * Sets the status for HTML code in the message
	 * 
	 * @param htmlEnabled <code>true</code> or <code>false</code>, depending the intention
	 */
	public void setHtmlEnabled(boolean htmlEnabled) {
		this.htmlEnabled = htmlEnabled;
	}

	/**
	 * Sets the id for the message
	 * 
	 * @param id The id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the username of the anonymous user that have sent the message
	 * 
	 * @param postUsername String with the username
	 */
	public void setPostUsername(String postUsername) {
		this.postUsername = postUsername;
	}

	/**
	 * Sets the status for signatures in the message
	 * 
	 * @param signatureEnabled <code>true</code> or <code>false</code>, depending the intention
	 */
	public void setSignatureEnabled(boolean signatureEnabled) {
		this.signatureEnabled = signatureEnabled;
	}

	/**
	 * Sets the status for smilies in the message
	 * 
	 * @param smiliesEnabled <code>true</code> or <code>false</code>, depending the intention
	 */
	public void setSmiliesEnabled(boolean smiliesEnabled) {
		this.smiliesEnabled = smiliesEnabled;
	}

	/**
	 * Sets the time the message was sent
	 * 
	 * @param time The time 
	 */
	public void setTime(Date time) {
		this.time = time;
	}
	
	public void setFormatedTime(String t)
	{
		this.formatedTime = t;
	}
	
	public String getFormatedTime()
	{
		if (this.formatedTime == null && this.time != null) {
			this.formatedTime = ViewCommon.formatDate(this.time);
		}
		
		return this.formatedTime;
	}

	/**
	 * Sets the id of the topic that the message belongs to
	 * 
	 * @param topicId The id of the topic
	 */
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	/**
	 * Sets the id of the user that sent the message
	 * 
	 * @param userId The user Id
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	/**
	 * Gets the message of the post
	 * 
	 * @return String containing the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Sets the text of the post
	 * 
	 * @param text The text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Gets the subject of the post 
	 * 
	 * @return String with the subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * Sets the subject for the message
	 * 
	 * @param subject The subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Sets the IP of the user
	 * 
	 * @param userIP The IP address of the user
	 */
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public boolean getCanEdit() {
		return this.canEdit;
	}
	
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	
	/**
	 * @return Returns the hasAttachments.
	 */
	public boolean hasAttachments()
	{
		return this.hasAttachments;
	}
	
	/**
	 * @param hasAttachments The hasAttachments to set.
	 */
	public void hasAttachments(boolean hasAttachments)
	{
		this.hasAttachments = hasAttachments;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof Post)) {
			return false;
		}
		
		return ((Post)o).getId() == this.id;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return this.id;
	}
}
