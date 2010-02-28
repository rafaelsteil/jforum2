/*
 * Created on 21/08/2006 21:17:12
 */
package net.jforum.dao;

import java.util.List;

import net.jforum.entities.MailIntegration;

/**
 * @author Rafael Steil
 * @version $Id: MailIntegrationDAO.java,v 1.1 2006/08/22 02:05:24 rafaelsteil Exp $
 */
public interface MailIntegrationDAO
{
	/**
	 * Adds a new mail integration
	 * @param integration the information to add
	 */
	public void add(MailIntegration integration);
	
	/**
	 * Updates an existing mail integration data
	 * @param integration
	 */
	public void update(MailIntegration integration);
	
	/**
	 * Deletes a mail integration by its forumId
	 * @param forumId the forumId of the underlying mailintegration 
	 * to be deleted
	 */
	public void delete(int forumId);
	
	/**
	 * Search for a mail integration instance
	 * @param forumId the forumId of the desired object. 
	 * @return the requested information, or null if not found
	 */
	public MailIntegration find(int forumId);
	
	/**
	 * Returns all MailIntegration objects currently registered
	 * @return a list with all data found. Each entry is a MailIntegration instance
	 */
	public List findAll();
}
