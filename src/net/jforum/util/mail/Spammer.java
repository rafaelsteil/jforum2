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
 * This file creation date: 03/03/2004 - 20:29:45
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util.mail;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.jforum.JForumExecutionContext;
import net.jforum.entities.User;
import net.jforum.exceptions.MailException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import freemarker.template.SimpleHash;
import freemarker.template.Template;

/**
 * Dispatch emails to the world. 
 * 
 * @author Rafael Steil
 * @version $Id: Spammer.java,v 1.36 2007/09/20 16:07:08 rafaelsteil Exp $
 */
public class Spammer
{
	private static final Logger logger = Logger.getLogger(Spammer.class);

	private static final int MESSAGE_HTML = 0;
	private static final int MESSAGE_TEXT = 1;
	
	private static int messageFormat;
	private Session session;
	private String username;
	private String password;
	
	private Properties mailProps = new Properties();
	private MimeMessage message;
	private List users = new ArrayList();
	private String messageId;
	private String inReplyTo;
	private boolean needCustomization;
	private SimpleHash templateParams;
	private Template template;
	
	protected Spammer() throws MailException
	{
		boolean ssl = SystemGlobals.getBoolValue(ConfigKeys.MAIL_SMTP_SSL);
		
		String hostProperty = this.hostProperty(ssl);
		String portProperty = this.portProperty(ssl);
		String authProperty = this.authProperty(ssl);
		String localhostProperty = this.localhostProperty(ssl);
		
		mailProps.put(hostProperty, SystemGlobals.getValue(ConfigKeys.MAIL_SMTP_HOST));
		mailProps.put(portProperty, SystemGlobals.getValue(ConfigKeys.MAIL_SMTP_PORT));

		String localhost = SystemGlobals.getValue(ConfigKeys.MAIL_SMTP_LOCALHOST);
		
		if (!StringUtils.isEmpty(localhost)) {
			mailProps.put(localhostProperty, localhost);
		}
		
		mailProps.put("mail.mime.address.strict", "false");
		mailProps.put("mail.mime.charset", SystemGlobals.getValue(ConfigKeys.MAIL_CHARSET));
		mailProps.put(authProperty, SystemGlobals.getValue(ConfigKeys.MAIL_SMTP_AUTH));

		username = SystemGlobals.getValue(ConfigKeys.MAIL_SMTP_USERNAME);
		password = SystemGlobals.getValue(ConfigKeys.MAIL_SMTP_PASSWORD);

		messageFormat = SystemGlobals.getValue(ConfigKeys.MAIL_MESSSAGE_FORMAT).equals("html") 
			? MESSAGE_HTML
			: MESSAGE_TEXT;

		this.session = Session.getInstance(mailProps);
	}

	public boolean dispatchMessages()
	{
        try
        {
            int sendDelay = SystemGlobals.getIntValue(ConfigKeys.MAIL_SMTP_DELAY);
            
			if (SystemGlobals.getBoolValue(ConfigKeys.MAIL_SMTP_AUTH)) {
                if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
                	boolean ssl = SystemGlobals.getBoolValue(ConfigKeys.MAIL_SMTP_SSL);
                	
                    Transport transport = this.session.getTransport(ssl ? "smtps" : "smtp");
                    
                    try {
	                    String host = SystemGlobals.getValue(ConfigKeys.MAIL_SMTP_HOST);
	                    transport.connect(host, username, password);
	
	                    if (transport.isConnected()) {
	                        for (Iterator userIter = this.users.iterator(); userIter.hasNext(); ) {
	                        	User user = (User)userIter.next();
	                        	
	                        	if (this.needCustomization) {
	                        		this.defineUserMessage(user);
	                        	}
	                        	
	                        	Address address = new InternetAddress(user.getEmail());
	                        	
	                        	logger.debug("Sending mail to: " + user.getEmail());
	                        	
	                        	this.message.setRecipient(Message.RecipientType.TO, address);	                            
	                        	transport.sendMessage(this.message, new Address[] { address });
	                        	
	                        	if (sendDelay > 0) {
		                        	try {
		                            	Thread.sleep(sendDelay);
		                            } 
		                        	catch (InterruptedException ie) {
		                            	logger.error("Error while Thread.sleep." + ie, ie);
		                            }
	                        	}
	                        }
	                    }
                    }
                    catch (Exception e) {
                    	throw new MailException(e);
                    }
                    finally {
                    	try { transport.close(); } catch (Exception e) {}
                    }
                }
            }
            else {
                for (Iterator iter = this.users.iterator(); iter.hasNext();) {
                	User user = (User)iter.next();
                	
                	if (this.needCustomization) {
                		this.defineUserMessage(user);
                	}
                	
                	Address address = new InternetAddress(user.getEmail());
                	logger.debug("Sending mail to: " + user.getEmail());
                	this.message.setRecipient(Message.RecipientType.TO,address);
                    Transport.send(this.message, new Address[] { address });
                    
                    if (sendDelay > 0) {
	                    try {
	                    	Thread.sleep(sendDelay);
	                    } catch (InterruptedException ie) {
	                    	logger.error("Error while Thread.sleep." + ie, ie);
	                    }
                    }
                }
            }
        }
        catch (MessagingException e) {
            logger.error("Error while dispatching the message." + e, e);
        }

        return true;
	}

	private void defineUserMessage(User user)
	{
		try {
			this.templateParams.put("user", user);
			
			String text = this.processTemplate();
			
			this.defineMessageText(text);
		}
		catch (Exception e) {
			throw new MailException(e);
		}
	}

	/**
	 * Prepares the mail message for sending.
	 * 
	 * @param subject the subject of the email
	 * @param messageFile the path to the mail message template
	 * @throws MailException
	 */
	protected void prepareMessage(String subject, String messageFile) throws MailException
	{
		if (this.messageId != null) {
			this.message = new IdentifiableMimeMessage(session);
			((IdentifiableMimeMessage)this.message).setMessageId(this.messageId);
		}
		else {
			this.message = new MimeMessage(session);
		}
		
		this.templateParams.put("forumName", SystemGlobals.getValue(ConfigKeys.FORUM_NAME));

		try {
			this.message.setSentDate(new Date());
			this.message.setFrom(new InternetAddress(SystemGlobals.getValue(ConfigKeys.MAIL_SENDER)));
			this.message.setSubject(subject, SystemGlobals.getValue(ConfigKeys.MAIL_CHARSET));
			
			if (this.inReplyTo != null) {
				this.message.addHeader("In-Reply-To", this.inReplyTo);
			}
			
			this.createTemplate(messageFile);
			this.needCustomization = this.isCustomizationNeeded();

			// If we don't need to customize any part of the message, 
			// then build the generic text right now
			if (!this.needCustomization) {
				String text = this.processTemplate();
				this.defineMessageText(text);
			}
		}
		catch (Exception e) {
			throw new MailException(e);
		}
	}
	
	/**
	 * Set the text contents of the email we're sending
	 * @param text the text to set
	 * @throws MessagingException
	 */
	private void defineMessageText(String text) throws MessagingException
	{
		String charset = SystemGlobals.getValue(ConfigKeys.MAIL_CHARSET);
		
		if (messageFormat == MESSAGE_HTML) {
			this.message.setContent(text.replaceAll("\n", "<br />"), "text/html; charset=" + charset);
		}
		else {
			this.message.setText(text);
		}
	}
	
	/**
	 * Gets the message text to send in the email.
	 * 
	 * @param messageFile The optional message file to load the text. 
	 * @return The email message text
	 * @throws Exception
	 */
	protected void createTemplate(String messageFile) throws Exception
	{
		String templateEncoding = SystemGlobals.getValue(ConfigKeys.MAIL_TEMPLATE_ENCODING);

		if (StringUtils.isEmpty(templateEncoding)) {
			this.template = JForumExecutionContext.templateConfig().getTemplate(messageFile);
		}
		else {
			this.template = JForumExecutionContext.templateConfig().getTemplate(messageFile, templateEncoding);
		}
	}

	/**
	 * Merge the template data, creating the final content.
	 * This method should only be called after {@link #createTemplate(String)}
	 * and {@link #setTemplateParams(SimpleHash)}
	 * 
	 * @return the generated content
	 * @throws Exception
	 */
	protected String processTemplate() throws Exception
	{
		StringWriter writer = new StringWriter();
		this.template.process(this.templateParams, writer);
		return writer.toString();
	}
	
	/**
	 * Set the parameters for the template being processed
	 * @param params the parameters to the template
	 */
	protected void setTemplateParams(SimpleHash params)
	{
		this.templateParams = params;
	}
	
	/**
	 * Check if we have to send customized emails
	 * @return true if there is a need for customized emails
	 */
	private boolean isCustomizationNeeded()
	{
		boolean need = false;
		
		for (Iterator iter = this.users.iterator(); iter.hasNext(); ) {
			User user = (User)iter.next();

			if (user.notifyText()) {
				need = true;
				break;
			}
		}
		
		return need;
	}
	
	protected void setMessageId(String messageId)
	{
		this.messageId = messageId;
	}
	
	protected void setInReplyTo(String inReplyTo)
	{
		this.inReplyTo = inReplyTo;
	}
	
	protected void setUsers(List users)
	{
		this.users = users;
	}

	private String localhostProperty(boolean ssl)
	{
		return ssl 
			? ConfigKeys.MAIL_SMTP_SSL_LOCALHOST
			: ConfigKeys.MAIL_SMTP_LOCALHOST;
	}

	private String authProperty(boolean ssl)
	{
		return ssl 
			? ConfigKeys.MAIL_SMTP_SSL_AUTH
			: ConfigKeys.MAIL_SMTP_AUTH;
	}

	private String portProperty(boolean ssl)
	{
		return ssl 
			? ConfigKeys.MAIL_SMTP_SSL_PORT
			: ConfigKeys.MAIL_SMTP_PORT;
	}

	private String hostProperty(boolean ssl)
	{
		return ssl 
			? ConfigKeys.MAIL_SMTP_SSL_HOST
			: ConfigKeys.MAIL_SMTP_HOST;
	}
}
