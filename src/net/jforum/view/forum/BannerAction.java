/*
 * Copyright (c) 2003, 2004 Rafael Steil
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
 * This file creation date: Apr 2, 2005 / 9:12:07 AM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum;

import net.jforum.Command;
import net.jforum.JForumExecutionContext;
import net.jforum.dao.BannerDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Banner;
import net.jforum.view.forum.common.BannerCommon;

/**
 * @author Samuel Yung
 * @version $Id: BannerAction.java,v 1.6 2006/08/20 12:19:15 sergemaslyukov Exp $
 */
public class BannerAction extends Command
{
	// dummy listing... there's no need to list in banners
	public void list()
	{
	}

	// redirect
	public void redirect() 
	{
		int bannerId = this.request.getIntParameter("banner_id");
		if(!(new BannerCommon()).canBannerDisplay(bannerId))
		{
			JForumExecutionContext.setRedirect("");
			return;
		}

		BannerDAO dao = DataAccessDriver.getInstance().newBannerDAO();
		Banner banner = dao.selectById(bannerId);
		banner.setClicks(banner.getClicks() + 1);
		dao.update(banner);
		JForumExecutionContext.setRedirect(banner.getUrl());
	}
}
