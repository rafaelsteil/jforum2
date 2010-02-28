/*
 * Created on 30/08/2006 22:08:44
 */
package net.jforum.api.integration.mail.pop;

/**
 * @author Rafael Steil
 * @version $Id: POPListenerMock.java,v 1.1 2006/08/31 02:12:22 rafaelsteil Exp $
 */
public class POPListenerMock extends POPListener
{
	public POPListenerMock() 
	{
		super.connector = new POPConnectorMock();
	}
}
