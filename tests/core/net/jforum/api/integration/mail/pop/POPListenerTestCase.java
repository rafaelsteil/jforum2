/*
 * Created on 28/08/2006 22:58:20
 */
package net.jforum.api.integration.mail.pop;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;
import net.jforum.ConfigLoader;
import net.jforum.ForumStartup;
import net.jforum.JForumExecutionContext;
import net.jforum.TestCaseUtils;
import net.jforum.dao.DataAccessDriver;
import net.jforum.dao.PostDAO;
import net.jforum.dao.TopicDAO;
import net.jforum.entities.Post;
import net.jforum.entities.Topic;
import net.jforum.entities.User;
import net.jforum.repository.RankingRepository;
import net.jforum.repository.SmiliesRepository;
import net.jforum.util.DbUtils;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: POPListenerTestCase.java,v 1.14 2007/09/04 14:57:25 andowson Exp $
 */
public class POPListenerTestCase extends TestCase
{
	private static boolean started;
	
	/**
	 * A single and simple message
	 */
	public void testSimple() throws Exception
	{
		int beforeTopicId = this.maxTopicId();
		
		String sender = "ze@zinho.com";
		String subject = "Mail Message " + new Date();
		String forumAddress = "forum_test@jforum.testcase";
		String contents = "Mail message contents " + new Date();
		
		this.sendMessage(sender, subject, forumAddress, contents, null);
		
		int afterTopicId = this.maxTopicId();
		
		assertTrue("The message was not inserted", afterTopicId > beforeTopicId);
		
		try {
			this.assertPost(afterTopicId, sender, subject, contents);
		}
		finally {
			this.deleteTopic(afterTopicId);
		}
	}
	
	/**
	 * Sends an invalid In-Reply-To header, which should cause the system
	 * to create a new topic, instead of adding the message as a reply
	 * to something else. 
	 */
	public void testInReplyToIncorrectShouldCreateNewTopic() throws Exception
	{
		int beforeTopicId = this.maxTopicId();
		
		String sender = "ze@zinho.com";
		String subject = "Mail Message " + new Date();
		String forumAddress = "forum_test@jforum.testcase";
		String contents = "Mail message contents " + new Date();
		
		this.sendMessage(sender, subject, forumAddress, contents, MessageId.buildMessageId(7777, 999999, 888888));
		
		int afterTopicId = this.maxTopicId();
		
		assertTrue("The message was not inserted", afterTopicId > beforeTopicId);
		
		try {
			this.assertPost(afterTopicId, sender, subject, contents);
		}
		finally {
			this.deleteTopic(afterTopicId);
		}
	}
	
	/**
	 * Create a new topic, then send a message with the In-Reply-To header, 
	 * which should create an answer to the previously created topic
	 * @throws Exception
	 */
	public void testInReplyToCreateNewTopicThenReply() throws Exception
	{
		int beforeTopicId = this.maxTopicId();
		
		String sender = "ze@zinho.com";
		String subject = "Mail Message " + new Date();
		String forumAddress = "forum_test@jforum.testcase";
		String contents = "Mail message contents " + new Date();
		
		this.sendMessage(sender, subject, forumAddress, contents, null);
		
		int afterTopicId = this.maxTopicId();
		
		assertTrue("The message was not inserted", afterTopicId > beforeTopicId);
		
		try {
			this.assertPost(afterTopicId, sender, subject, contents);
			
			// Ok, now send a new message, replying to the previously topic
			subject = "Reply subject for topic " + afterTopicId;
			contents = "Changed contents, replying tpoic " + afterTopicId;
			
			this.sendMessage(sender, subject, forumAddress, contents, MessageId.buildMessageId(7777, afterTopicId, 999999));
			
			assertTrue("A new message was created, instead of a reply", afterTopicId == maxTopicId());
			
			PostDAO postDAO = DataAccessDriver.getInstance().newPostDAO();
			List posts = postDAO.selectAllByTopic(afterTopicId);
			
			assertTrue("There should be two posts", posts.size() == 2);
			
			// The first message was already validated
			Post p = (Post)posts.get(1);
			User user = DataAccessDriver.getInstance().newUserDAO().selectById(p.getUserId());

			assertNotNull("User should not be null", user);
			assertEquals("sender", sender, user.getEmail());
			assertEquals("subject", subject, p.getSubject());
			assertEquals("text", contents, p.getText());
		}
		finally {
			this.deleteTopic(afterTopicId);
		}
	}
	
	/**
	 * Emulates the action of sending an email.
	 * 
	 * @param sender The sender's email. Should exist in the database
	 * @param subject the subject
	 * @param forumAddress the address of the target forum. There should be a matching
	 * record in jforum_mail_integration
	 * @param contents the message itself
	 * @param inReplyTo the In-Reply-To header, as built by {@link MessageId}. Can be null
	 * @throws Exception
	 */
	private void sendMessage(String sender, String subject, String forumAddress, String contents, String inReplyTo) throws Exception
	{
		POPListener listener = new POPListenerMock();
		
		MimeMessageMock message = this.newMessageMock(sender, subject, forumAddress, contents);
		
		if (inReplyTo != null) {
			message.addHeader("In-Reply-To", inReplyTo);
		}
		
		((POPConnectorMock)listener.getConnector()).setMessages(new Message[] { message });
		
		listener.execute(null);
	}
	
	/**
	 * Asserts the post instance, after execution some part of the testcase
	 * @param topicId the topic's id of the new message
	 * @param sender the matching sender email
	 * @param subject the matching subject
	 * @param contents the matching message contents
	 */
	private void assertPost(int topicId, String sender, String subject, String contents)
	{
		PostDAO postDAO = DataAccessDriver.getInstance().newPostDAO();
		List posts = postDAO.selectAllByTopic(topicId);
		
		assertTrue("There should be exactly one post", posts.size() == 1);
		
		Post p = (Post)posts.get(0);
		
		User user = DataAccessDriver.getInstance().newUserDAO().selectById(p.getUserId());
		assertNotNull("User should not be null", user);
		
		assertEquals("sender", sender, user.getEmail());
		assertEquals("subject", subject, p.getSubject());
		assertEquals("text", contents, p.getText());
	}
	
	/**
	 * Gets the latest topic id existent
	 * @return the topic id, or -1 if something went wrong
	 * @throws Exception
	 */
	private int maxTopicId() throws Exception
	{
		int topicId = -1;
		
		PreparedStatement p = null;
		ResultSet rs = null;
		
		try {
			p = JForumExecutionContext.getConnection().prepareStatement("select max(topic_id) from jforum_topics");
			rs = p.executeQuery();
			
			if (rs.next()) {
				topicId = rs.getInt(1);
			}
		}
		finally {
			DbUtils.close(rs, p);
		}
		
		return topicId;
	}

	/**
	 * Deletes a topic
	 * @param topicId the topic's id to delete
	 */
	private void deleteTopic(int topicId)
	{
		try {
			TopicDAO dao = DataAccessDriver.getInstance().newTopicDAO();
			
			Topic t = new Topic(topicId);
			t.setForumId(2);
			
			dao.delete(t, false);
			
			JForumExecutionContext.finish();
		}
		catch (Exception e) {
			e.printStackTrace();				
		}
	}
	
	private MimeMessageMock newMessageMock(String sender, String subject, String listEmail, 
			String text) throws Exception
	{
		MimeMessageMock m = new MimeMessageMock(null, new ByteArrayInputStream(text.getBytes()));
		
		m.setFrom(new InternetAddress(sender));
		m.setRecipient(RecipientType.TO, new InternetAddress(listEmail));
		m.setSubject(subject);
		
		return m;
	}
	
	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		if (!started) {
			TestCaseUtils.loadEnvironment();
			TestCaseUtils.initDatabaseImplementation();
			ConfigLoader.startCacheEngine();
			
			ForumStartup.startForumRepository();
			RankingRepository.loadRanks();
			SmiliesRepository.loadSmilies();
			
			SystemGlobals.setValue(ConfigKeys.SEARCH_INDEXING_ENABLED, "false");
			
			started = true;
		}
	}
	
	private static class MimeMessageMock extends MimeMessage
	{
		private InputStream is;
		private String messageId;
		
		public MimeMessageMock(Session session, InputStream is) throws MessagingException
		{
			super(session, is);
			this.is = is;
		}
		
		public InputStream getInputStream() throws IOException, MessagingException
		{
			return this.is;
		}
		
		protected void updateMessageID() throws MessagingException
		{
			if (this.messageId != null) {
				this.setMessageId(this.messageId);
			}
			else {
				super.updateMessageID();
			}
		}
		
		public void setMessageId(String messageId) throws MessagingException
		{
			this.addHeader("Message-ID", messageId);
			this.messageId = messageId;
		}
		
		public String getContentType() throws MessagingException
		{
			return "text/plain";
		}
	}
}
