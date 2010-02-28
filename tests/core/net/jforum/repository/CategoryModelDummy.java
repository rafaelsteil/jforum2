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
 * Created on 13/11/2004 11:50:19
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.repository;

import java.util.List;

import net.jforum.dao.CategoryDAO;
import net.jforum.entities.Category;

/**
 * @author Rafael Steil
 * @version $Id: CategoryModelDummy.java,v 1.8 2007/08/01 22:30:04 rafaelsteil Exp $
 */
class CategoryModelDummy implements CategoryDAO 
{
	public CategoryModelDummy() {}

	/** 
	 * @see net.jforum.dao.CategoryDAO#selectById(int)
	 */
	public Category selectById(int categoryId) {
		return null;
	}

	/** 
	 * @see net.jforum.dao.CategoryDAO#selectAll()
	 */
	public List selectAll() {
		return null;
	}

	/** 
	 * @see net.jforum.dao.CategoryDAO#canDelete(int)
	 */
	public boolean canDelete(int categoryId) {
		return false;
	}

	/** 
	 * @see net.jforum.dao.CategoryDAO#delete(int)
	 */
	public void delete(int categoryId) {
	}

	/** 
	 * @see net.jforum.dao.CategoryDAO#update(net.jforum.entities.Category)
	 */
	public void update(Category category) {
	}

	/** 
	 * @see net.jforum.dao.CategoryDAO#addNew(net.jforum.entities.Category)
	 */
	public int addNew(Category category) {
		return 0;
	}
	
	/** 
	 * @see net.jforum.dao.CategoryDAO#setOrderDown(net.jforum.entities.Category, net.jforum.entities.Category)
	 */
	public void setOrderDown(Category c, Category c2) {
	}
	
	/** 
	 * @see net.jforum.dao.CategoryDAO#setOrderUp(net.jforum.entities.Category, net.jforum.entities.Category) 
	 */
	public void setOrderUp(Category c, Category c2) {
	}
	
}
