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
 * This file creation date: 31/01/2004 - 20:53:44
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.rss;

import java.io.StringWriter;

import net.jforum.JForumExecutionContext;
import net.jforum.exceptions.ForumException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * @author Rafael Steil
 * @version $Id: GenericRSS.java,v 1.10 2007/07/28 14:17:13 rafaelsteil Exp $
 */
public class GenericRSS implements RSSAware 
{
	private RSS rss;
	
	protected void setRSS(RSS rss) 
	{
		this.rss = rss;
	}
	
	public String createRSS()
	{
		try
		{
			Template t = JForumExecutionContext.templateConfig().getTemplate(SystemGlobals.getValue(ConfigKeys.TEMPLATE_DIR)
					+ "/rss_template.htm");
			StringWriter sw = new StringWriter();

			SimpleHash templateContext = JForumExecutionContext.getTemplateContext();

			templateContext.put("encoding", SystemGlobals.getValue(ConfigKeys.ENCODING));
			templateContext.put("rss", this.rss);
			t.process(templateContext, sw);

			return sw.toString();
		}
		catch (Exception e)
		{
			throw new ForumException(e);
		}
	}
}