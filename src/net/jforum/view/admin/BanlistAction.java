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
 * Created on 07/12/2006 21:24:12
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.admin;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.jforum.dao.BanlistDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Banlist;
import net.jforum.exceptions.ForumException;
import net.jforum.repository.BanlistRepository;
import net.jforum.util.preferences.TemplateKeys;

/**
 * @author Rafael Steil
 * @version $Id: BanlistAction.java,v 1.4 2006/12/11 00:44:51 rafaelsteil Exp $
 */
public class BanlistAction extends AdminCommand
{
	public void insert()
	{
		this.setTemplateName(TemplateKeys.BANLIST_INSERT);
	}
	
	public void insertSave()
	{
		String type = this.request.getParameter("type");
		String value = this.request.getParameter("value");
		
		if (!StringUtils.isEmpty(type) && !StringUtils.isEmpty(value)) {
			Banlist b = new Banlist();
			
			if ("email".equals(type)) {
				b.setEmail(value);
			}
			else if ("user".equals(type)) {
				b.setUserId(Integer.parseInt(value));
			}
			else if ("ip".equals(type)) {
				b.setIp(value);
			}
			else {
				throw new ForumException("Unknown banlist type");
			}
			
			BanlistDAO dao = DataAccessDriver.getInstance().newBanlistDAO();
			dao.insert(b);
			
			BanlistRepository.add(b);
		}
		
		this.list();
	}
	
	public void delete() 
	{
		String[] banlist = this.request.getParameterValues("banlist_id");
		
		if (banlist != null && banlist.length > 0) {
			BanlistDAO dao = DataAccessDriver.getInstance().newBanlistDAO();
			
			for (int i = 0; i < banlist.length; i++) {
				int current = Integer.parseInt(banlist[i]);
				dao.delete(current);
				
				BanlistRepository.remove(current);
			}
		}
		
		this.list();
	}
	
	/**
	 * @see net.jforum.Command#list()
	 */
	public void list()
	{
		this.setTemplateName(TemplateKeys.BANLIST_LIST);
		
		List l = DataAccessDriver.getInstance().newBanlistDAO().selectAll();
		this.context.put("banlist", l);
	}
}
