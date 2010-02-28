/*
 * Created on 21/08/2006 21:07:36
 */
package net.jforum.api.integration.mail.pop;

import java.util.Iterator;
import java.util.List;

import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.MailIntegration;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Rafael Steil
 * @version $Id: POPListener.java,v 1.9 2006/10/10 01:59:55 rafaelsteil Exp $
 */
public class POPListener implements Job
{
	private static final Logger logger = Logger.getLogger(POPListener.class);
	private static boolean working;
	protected POPConnector connector = new POPConnector();
	
	/**
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	public void execute(JobExecutionContext jobContext) throws JobExecutionException
	{
		if (!working) {
			try {
				working = true;

				List integrationList = DataAccessDriver.getInstance().newMailIntegrationDAO().findAll();
				POPParser parser = new POPParser();
				
				for (Iterator iter = integrationList.iterator(); iter.hasNext(); ) {
					MailIntegration integration = (MailIntegration)iter.next();
					
					connector.setMailIntegration(integration);
					
					try {
						logger.debug("Going to check " + integration);
						
						connector.openConnection();
						parser.parseMessages(connector);
						
						POPPostAction postAction = new POPPostAction();
						postAction.insertMessages(parser);
					}
					finally {
						connector.closeConnection();
					}
				}
			}
			finally {
				working = false;
			}
		}
		else {
			logger.debug("Already working. Leaving for now.");
		}
	}
	
	POPConnector getConnector()
	{
		return this.connector;
	}
}
