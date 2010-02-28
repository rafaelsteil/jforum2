/*
 * Created on 26/08/2006 22:20:46
 */
package net.jforum.api.integration.mail.pop;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import freemarker.template.SimpleHash;

import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.context.JForumContext;
import net.jforum.context.RequestContext;
import net.jforum.context.standard.StandardRequestContext;
import net.jforum.context.standard.StandardSessionContext;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.ForumDAO;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.entities.UserSession;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.view.forum.PostAction;

/**
 * @author Rafael Steil
 * @version $Id: POPPostAction.java,v 1.10 2007/07/31 13:52:47 rafaelsteil Exp $
 */
public class POPPostAction
{
	private static Logger logger = Logger.getLogger(POPPostAction.class);
	
	public void insertMessages(POPParser parser)
	{
		long ms = System.currentTimeMillis();
		int counter = 0;
		
		try {
			JForumExecutionContext ex = JForumExecutionContext.get();
			
			RequestContext request = new StandardRequestContext();
			ex.setForumContext(new JForumContext("/", "", request, null));
			
			JForumExecutionContext.set(ex);
			
			SessionFacade.setAttribute(ConfigKeys.TOPICS_READ_TIME, new HashMap());
			
			for (Iterator iter = parser.getMessages().iterator(); iter.hasNext(); ) {
				POPMessage m = (POPMessage)iter.next();
				String sessionId = ms + m.getSender() + counter++;
				
				request.getSessionContext().setAttribute(StandardSessionContext.SESSION_ID, sessionId);
				
				User user = this.findUser(m.getSender());
				
				if (user == null) {
					logger.warn("Could not find user with email " + m.getSender() + ". Ignoring his message.");
					continue;
				}
				
				try {
					UserSession us = new UserSession();
					us.setUserId(user.getId());
					us.setUsername(us.getUsername());
					us.setSessionId(sessionId);
					
					SessionFacade.add(us, sessionId);
					SessionFacade.setAttribute(ConfigKeys.LOGGED, "1");
					
					SessionFacade.removeAttribute(ConfigKeys.LAST_POST_TIME);
					SessionFacade.setAttribute(ConfigKeys.REQUEST_IGNORE_CAPTCHA, "1");
					
					this.insertMessage(m, user);
				}
				finally {
					SessionFacade.remove(sessionId);
				}
			}
		}
		finally {
			JForumExecutionContext.finish();
		}
	}
	
	/**
	 * Calls {@link PostAction#insertSave()}
	 * @param m the mail message
	 * @param user the user who's sent the message
	 */
	private void insertMessage(POPMessage m, User user)
	{
		this.addDataToRequest(m, user);
		
		PostAction postAction = new PostAction(JForumExecutionContext.getRequest(), new SimpleHash());
		postAction.insertSave();
	}
	
	/**
	 * Extracts information from a mail message and adds it to the request context
	 * @param m the mail message
	 * @param user the user who's sending the message
	 */
	private void addDataToRequest(POPMessage m, User user)
	{
		RequestContext request = JForumExecutionContext.getRequest(); 
		
		request.addParameter("forum_id", Integer.toString(this.discoverForumId(m.getListEmail())));
		request.addParameter("topic_type", Integer.toString(Topic.TYPE_NORMAL));
		request.addParameter("quick", "1");
		request.addParameter("subject", m.getSubject());
		request.addParameter("message", m.getMessage());
		
		int topicId = this.discoverTopicId(m);
		
		if (topicId > 0) {
			request.addParameter("topic_id", Integer.toString(topicId));
		}
		
		if (!user.isBbCodeEnabled()) {
			request.addParameter("disable_bbcode", "on");
		}
		
		if (!user.isSmiliesEnabled()) {
			request.addParameter("disable_smilies", "on");
		}
		
		if (!user.isHtmlEnabled()) {
			request.addParameter("disable_html", "on");
		}
	}
	
	/**
	 * Tries to extract message relationship from the headers
	 * @param m the message to extract headers from
	 * @return the topic id, if found, or 0 (zero) othwerwise
	 */
	private int discoverTopicId(POPMessage m)
	{
		int topicId = 0;
		
		String inReplyTo = m.getInReplyTo();
		
		if (inReplyTo != null) {
			topicId = MessageId.parse(inReplyTo).getTopicId();
		}
		
		return topicId;
	}
	
	/**
	 * Given an email address, finds the forum instance associated to it
	 * @param listEmail the forum's email address to search for
	 * @return the forum's id, or 0 (zero) if nothing was found
	 */
	private int discoverForumId(String listEmail)
	{
		ForumDAO dao = DataAccessDriver.getInstance().newForumDAO();
		return dao.discoverForumId(listEmail);
	}
	
	/**
	 * Finds an user by his email address
	 * @param email the email address to use in the search
	 * @return the matching record, or null if nothing was found
	 */
	private User findUser(String email)
	{
		return DataAccessDriver.getInstance().newUserDAO().findByEmail(email);
	}
}
