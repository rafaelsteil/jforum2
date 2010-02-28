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
 * This file creation date: Mar 3, 2003 / 10:55:19 AM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum;

import java.io.IOException;

import net.jforum.context.RequestContext;
import net.jforum.context.ResponseContext;
import net.jforum.exceptions.ForumException;
import net.jforum.exceptions.TemplateNotFoundException;
import net.jforum.repository.Tpl;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.util.preferences.TemplateKeys;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * <code>Command</code> Pattern implementation.
 * All View Helper classes, which are intead to configure and processs
 * presentation actions must extend this class. 
 * 
 * @author Rafael Steil
 * @version $Id: Command.java,v 1.27 2007/07/28 14:17:11 rafaelsteil Exp $
 */
public abstract class Command 
{
	private static Class[] NO_ARGS_CLASS = new Class[0];
	private static Object[] NO_ARGS_OBJECT = new Object[0];
	
	private boolean ignoreAction;
	
	protected String templateName;
	protected RequestContext request;
	protected ResponseContext response;
	protected SimpleHash context;
	
	protected void setTemplateName(String templateName)
	{
		this.templateName = Tpl.name(templateName);
	}
	
	protected void ignoreAction()
	{
		this.ignoreAction = true;
	}
	
	/**
	 * Base method for listings. 
	 * May be used as general listing or as helper
	 * to another specialized type of listing. Subclasses
	 * must implement it to the cases where some invalid
	 * action is called ( which means that the exception will
	 * be caught and the general listing will be used )
	 */
	public abstract void list() ;
	
	/**
	 * Process and manipulate a requisition.
	 * @return <code>Template</code> reference
     * @param request WebContextRequest
     * @param response WebContextResponse
	 */
	public Template process(RequestContext request, ResponseContext response, SimpleHash context)
	{
		this.request = request;
		this.response = response;
		this.context = context;
		
		String action = this.request.getAction();

		if (!this.ignoreAction) {
			try {
				this.getClass().getMethod(action, NO_ARGS_CLASS).invoke(this, NO_ARGS_OBJECT);
			}
			catch (NoSuchMethodException e) {		
				this.list();		
			}
			catch (Exception e)
            {
                throw new ForumException(e);
			}
		}
		
		if (JForumExecutionContext.getRedirectTo() != null) {
			this.setTemplateName(TemplateKeys.EMPTY);
		}
		else if (request.getAttribute("template") != null) {
			this.setTemplateName((String)request.getAttribute("template"));
		}
		
		if (JForumExecutionContext.isCustomContent()) {
			return null;
		}
		
		if (this.templateName == null) {
			throw new TemplateNotFoundException("Template for action " + action + " is not defined");
		}

        try {
            return JForumExecutionContext.templateConfig().getTemplate(
                new StringBuffer(SystemGlobals.getValue(ConfigKeys.TEMPLATE_DIR)).
                append('/').append(this.templateName).toString());
        }
        catch (IOException e) {
            throw new ForumException( e);
        }
    }
}
