/*
 * Created on 30/08/2006 22:09:25
 */
package net.jforum.api.integration.mail.pop;

import javax.mail.Message;

/**
 * @author Rafael Steil
 * @version $Id: POPConnectorMock.java,v 1.2 2006/09/04 01:00:07 rafaelsteil Exp $
 */
public class POPConnectorMock extends POPConnector
{
	private Message[] messages;
	
	void setMessages(Message[] messages)
	{
		this.messages = messages;
	}
	
	public Message[] listMessages()
	{
		return this.messages;
	}
	
	public void openConnection() {}
	public void closeConnection() {}
} 
