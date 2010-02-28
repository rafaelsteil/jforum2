/*
 * Created on 04/09/2006 22:04:17
 */
package net.jforum.dao;

/**
 * @author Rafael Steil
 * @version $Id: ApiDAO.java,v 1.2 2006/10/10 00:49:04 rafaelsteil Exp $
 */
public interface ApiDAO
{
	/**
	 * Check if the given API authentication information is valid.
	 * @param apiKey the api key
	 * @return <code>true</code> if the information is correct
	 */
	public boolean isValid(String apiKey);
}
