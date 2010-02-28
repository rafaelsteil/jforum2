/*
 * Created on 21/08/2006 22:00:12
 */
package net.jforum.api.integration.mail.pop;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;

import org.apache.log4j.Logger;

/**
 * @author Rafael Steil
 * @version $Id: POPParser.java,v 1.3 2006/10/05 02:00:23 rafaelsteil Exp $
 */
public class POPParser
{
	private static Logger logger = Logger.getLogger(POPParser.class);
	
	private List messages = new ArrayList();
	
	public void parseMessages(POPConnector connector)
	{
		Message[] connectorMessages = connector.listMessages();
		
		for (int i = 0; i < connectorMessages.length; i++) {
			POPMessage message = new POPMessage(connectorMessages[i]);
			this.messages.add(message);
			
			logger.debug("Retrieved message " + message);
		}
	}
	
	public List getMessages()
	{
		return this.messages;
	}
}
