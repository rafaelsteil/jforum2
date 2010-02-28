/*
 * Created on 21/08/2006 22:14:04
 */
package net.jforum.api.integration.mail.pop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

import net.jforum.exceptions.MailException;

/**
 * Represents a pop message. 
 * @author Rafael Steil
 * @version $Id: POPMessage.java,v 1.6 2006/10/09 00:54:08 rafaelsteil Exp $
 */
public class POPMessage
{
	private static final String IN_REPLY_TO = "In-Reply-To";
	private static final String REFERENCES = "References";
	
	private String subject;
	private Object message;
	private String messageContents;
	private String sender;
	private String replyTo;
	private String references;
	private String inReplyTo;
	private String contentType;
	private String listEmail;
	private Date sendDate;
	private Map headers;
	
	/**
	 * Creates a new instance based on a {@link Message}
	 * @param message the message to convert from.
	 */
	public POPMessage(Message message)
	{
		this.extract(message);
	}
	
	/**
	 * Given a {@link Message}, converts it to our internal format
	 * @param message the message to convert
	 */
	private void extract(Message message)
	{
		try {
			this.subject = message.getSubject();
			
			this.message = message.getContent();
			this.contentType = message.getContentType();
			this.sender = ((InternetAddress)message.getFrom()[0]).getAddress();
			this.listEmail = ((InternetAddress)message.getAllRecipients()[0]).getAddress();
			this.sendDate = message.getSentDate();
				
			if (message.getReplyTo().length > 0) {
				this.replyTo = ((InternetAddress)message.getReplyTo()[0]).getAddress();
			}
			else {
				this.replyTo = this.sender;
			}
			
			this.headers = new HashMap();
			
			for (Enumeration e = message.getAllHeaders(); e.hasMoreElements(); ) {
				Header header = (Header)e.nextElement();
				this.headers.put(header.getName(), header.getValue());
			}
			
			if (this.headers.containsKey(IN_REPLY_TO)) {
				this.inReplyTo = this.headers.get(IN_REPLY_TO).toString();
			}
			
			if (this.headers.containsKey(REFERENCES)) {
				this.references = this.headers.get(REFERENCES).toString();
			}
			
			this.extractMessageContents(message);
		}
		catch (Exception e) {
			
		}
	}
	
	private void extractMessageContents(Message m) throws MessagingException
	{
		Part messagePart = m;
		
		if (this.message instanceof Multipart) {
			messagePart = ((Multipart)this.message).getBodyPart(0);
		}
		
		if (contentType.startsWith("text/html")
			|| contentType.startsWith("text/plain")) {
			InputStream is = null;
			BufferedReader reader = null;
			
			try {
				is = messagePart.getInputStream();
				is.reset();
				reader = new BufferedReader(
					new InputStreamReader(is));
				
				StringBuffer sb = new StringBuffer(512);
				int c = 0;
				char[] ch = new char[2048];
				
				while ((c = reader.read(ch)) != -1) {
					sb.append(ch, 0, c);
				}
				
				this.messageContents = sb.toString();
			}
			catch (IOException e) {
				throw new MailException(e);
			}
			finally {
				if (reader != null) { try { reader.close(); } catch (Exception e) {} }
				if (is != null) { try { is.close(); } catch (Exception e) {} }
			}
		}
	}
	
	public String getListEmail()
	{
		return this.listEmail;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType()
	{
		return this.contentType;
	}

	/**
	 * @return the headers
	 */
	public Map getHeaders()
	{
		return this.headers;
	}

	/**
	 * @return the inReplyTo
	 */
	public String getInReplyTo()
	{
		return this.inReplyTo;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return this.messageContents;
	}

	/**
	 * @return the references
	 */
	public String getReferences()
	{
		return this.references;
	}

	/**
	 * @return the replyTo
	 */
	public String getReplyTo()
	{
		return this.replyTo;
	}

	/**
	 * @return the sendDate
	 */
	public Date getSendDate()
	{
		return this.sendDate;
	}

	/**
	 * @return the sender
	 */
	public String getSender()
	{
		return this.sender;
	}

	/**
	 * @return the subject
	 */
	public String getSubject()
	{
		return this.subject;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(Map headers)
	{
		this.headers = headers;
	}

	/**
	 * @param inReplyTo the inReplyTo to set
	 */
	public void setInReplyTo(String inReplyTo)
	{
		this.inReplyTo = inReplyTo;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(Object message)
	{
		this.message = message;
	}

	/**
	 * @param references the references to set
	 */
	public void setReferences(String references)
	{
		this.references = references;
	}

	/**
	 * @param replyTo the replyTo to set
	 */
	public void setReplyTo(String replyTo)
	{
		this.replyTo = replyTo;
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(Date sendDate)
	{
		this.sendDate = sendDate;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender)
	{
		this.sender = sender;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new StringBuffer()
			.append('[')
			.append(", subject=").append(this.subject)
			.append(", sender=").append(this.sender)
			.append(", replyTo=").append(this.replyTo)
			.append(", references=").append(this.references)
			.append(", inReplyTo=").append(this.inReplyTo)
			.append(", contentType=").append(this.contentType)
			.append(", date=").append(this.sendDate)
			.append(", content=").append(this.messageContents)
			.append(", headers=").append(this.headers)
			.append(']')
			.toString();
	}
}
