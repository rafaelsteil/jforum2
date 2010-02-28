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
 * Created on Jan 18, 2005 2:58:22 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.entities;

import java.io.File;

import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: Attachment.java,v 1.5 2006/08/20 22:47:35 rafaelsteil Exp $
 */
public class Attachment
{
	private int id;
	private int postId;
	private int privmsgsId;
	private int userId;
	private AttachmentInfo info;
	
	/**
	 * @return Returns the id.
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * @param id The id to set.
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * @return Returns the postId.
	 */
	public int getPostId()
	{
		return this.postId;
	}
	
	/**
	 * @param postId The postId to set.
	 */
	public void setPostId(int postId)
	{
		this.postId = postId;
	}
	
	/**
	 * @return Returns the privmsgsId.
	 */
	public int getPrivmsgsId()
	{
		return this.privmsgsId;
	}
	
	/**
	 * @param privmsgsId The privmsgsId to set.
	 */
	public void setPrivmsgsId(int privmsgsId)
	{
		this.privmsgsId = privmsgsId;
	}
	
	/**
	 * @return Returns the userId.
	 */
	public int getUserId()
	{
		return this.userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	
	/**
	 * @return Returns the info.
	 */
	public AttachmentInfo getInfo()
	{
		return this.info;
	}
	
	/**
	 * @param info The info to set.
	 */
	public void setInfo(AttachmentInfo info)
	{
		this.info = info;
	}
	
	public boolean hasThumb() 
	{
		return SystemGlobals.getBoolValue(ConfigKeys.ATTACHMENTS_IMAGES_CREATE_THUMB)
			&& new File(SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_STORE_DIR)
					+ '/'
					+ this.info.getPhysicalFilename() + "_thumb").exists();
	}
	
	public String thumbPath() {
		return SystemGlobals.getValue(ConfigKeys.ATTACHMENTS_UPLOAD_DIR)
			+ '/'
			+ this.info.getPhysicalFilename()
			+ "_thumb";
	}
}
