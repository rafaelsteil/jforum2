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
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.mail;

import java.util.ArrayList;
import java.util.List;

import net.jforum.entities.User;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.view.forum.common.ViewCommon;
import freemarker.template.SimpleHash;

/**
 * @author James Young
 * @version $Id: ActivationKeySpammer.java,v 1.12 2007/09/21 17:26:09 rafaelsteil Exp $
 */
public class ActivationKeySpammer extends Spammer
{
	public ActivationKeySpammer(User u)
	{
		//gets the url to the forum.
		String forumLink = ViewCommon.getForumLink();

		String url = new StringBuffer()
			.append(forumLink) 
			.append("user/activateAccount/") 
			.append(u.getActivationKey()) 
			.append('/')
			.append(u.getId()) 
			.append(SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION))
			.toString();
		
		String manualUrl = new StringBuffer()
			.append(forumLink)
			.append("user/activateManual") 
			.append(SystemGlobals.getValue(ConfigKeys.SERVLET_EXTENSION))
			.toString();
		
		SimpleHash params = new SimpleHash();
		params.put("url", url);
		params.put("user", u);
		params.put("manualUrl", manualUrl);
		
		List recipients = new ArrayList();
		recipients.add(u);
		
		this.setUsers(recipients);
		this.setTemplateParams(params);

		this.prepareMessage(SystemGlobals.getValue(ConfigKeys.MAIL_ACTIVATION_KEY_SUBJECT), 
				SystemGlobals.getValue(ConfigKeys.MAIL_ACTIVATION_KEY_MESSAGE_FILE));
	}
}
