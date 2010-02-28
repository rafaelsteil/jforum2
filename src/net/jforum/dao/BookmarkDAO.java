/*
 * Copyright (c) JForum Team
 * All rights reserved.
 * 
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
 * Created on Jan 16, 2005 12:30:52 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.dao;

import java.util.List;

import net.jforum.entities.Bookmark;

/**
 * @author Rafael Steil
 * @version $Id: BookmarkDAO.java,v 1.6 2006/08/23 02:13:35 rafaelsteil Exp $
 */
public interface BookmarkDAO
{
	/**
	 * Adds a new bookmark.
	 * 
	 * @param b The bookmark to add
	 */
	public void add(Bookmark b);
	
	/**
	 * Updates a bookmark.
	 * Only the fields <i>publicVisible</i>, <i>title</i>
	 * and <i>description</i> are changed.
	 * All other fields remain with the same value.
	 * 
	 * @param b The bookmark to update
	 */
	public void update(Bookmark b);
	
	/**
	 * Removes a bookmark.
	 * 
	 * @param bookmarkId The bookmark's id to remove
	 */
	public void remove(int bookmarkId);
	
	/**
	 * Gets all bookmarks of a given type.
	 * 
	 * @param userId The bookmark's owner
	 * @param relationType Any valid type declared in
	 * <code>net.jforum.entities.BookmarkType</code>
	 * @return A list with all results found. Each entry is
	 * a {@link net.jforum.entities.Bookmark} instance.
	 */
	public List selectByUser(int userId, int relationType);
	
	/**
	 * Gets all bookmarks from some user.
	 * 
	 * @param userId The bookmark's owner
	 * <code>net.jforum.entities.BookmarkType</code>
	 * @return A list with all results found. Each entry is
	 * a {@link net.jforum.entities.Bookmark} instance.
	 */
	public List selectByUser(int userId);
	
	/**
	 * Gets a bookmark.
	 * 
	 * @param bookmarkId The bookmark id
	 * @return A Bookmark instance or null if no entry found
	 */
	public Bookmark selectById(int bookmarkId);
	
	/**
	 * Gets a bookmark for edition.
	 * 
	 * @param relationId The relation's id
	 * @param relationType The relation type.
	 * @param userId The bookmark's owner
	 * @return A bookmark instance of <code>null</code> if 
	 * the record cannot be found
	 */
	public Bookmark selectForUpdate(int relationId, int relationType, int userId);
}
