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
 * Created on Jun 2, 2005 5:41:11 PM
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.sso;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import net.jforum.dao.UserDAO;
import net.jforum.entities.User;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * Authenticate users against a LDAP server. 
 * 
 * @author Rafael Steil
 * @version $Id: LDAPAuthenticator.java,v 1.8 2006/08/20 22:47:43 rafaelsteil Exp $
 */
public class LDAPAuthenticator implements LoginAuthenticator
{
	private Hashtable prepareEnvironment()
	{
		Hashtable h = new Hashtable();
		
		h.put(Context.INITIAL_CONTEXT_FACTORY, SystemGlobals.getValue(ConfigKeys.LDAP_FACTORY));
		h.put(Context.PROVIDER_URL, SystemGlobals.getValue(ConfigKeys.LDAP_SERVER_URL));
		
		String protocol = SystemGlobals.getValue(ConfigKeys.LDAP_SECURITY_PROTOCOL);
		
		if (protocol != null && !"".equals(protocol.trim())) {
			h.put(Context.SECURITY_PROTOCOL, protocol);
		}

		String  authentication = SystemGlobals.getValue(ConfigKeys.LDAP_AUTHENTICATION);

		if (authentication != null && !"".equals(authentication.trim())) {
			h.put(Context.SECURITY_AUTHENTICATION, authentication);
		}
		
		return h;
	}
	
	/**
	 * @see net.jforum.sso.LoginAuthenticator#validateLogin(java.lang.String, java.lang.String, java.util.Map)
	 */
	public User validateLogin(String username, String password, Map extraParams)
	{
		Hashtable environment = this.prepareEnvironment();
		
		StringBuffer principal = new StringBuffer(256)
			.append(SystemGlobals.getValue(ConfigKeys.LDAP_LOGIN_PREFIX))
			.append(username)
			.append(',')
			.append(SystemGlobals.getValue(ConfigKeys.LDAP_LOGIN_SUFFIX));
		
		environment.put(Context.SECURITY_PRINCIPAL, principal.toString());
		environment.put(Context.SECURITY_CREDENTIALS, password);
		
		DirContext dir = null;
		
		try {
			dir = new InitialDirContext(environment);
			
			String lookupPrefix = SystemGlobals.getValue(ConfigKeys.LDAP_LOOKUP_PREFIX);
			String lookupSuffix = SystemGlobals.getValue(ConfigKeys.LDAP_LOOKUP_SUFFIX);
			
			if (lookupPrefix == null || lookupPrefix.length() == 0) {
				lookupPrefix = SystemGlobals.getValue(ConfigKeys.LDAP_LOGIN_PREFIX);
			}
			
			if (lookupSuffix == null || lookupSuffix .length() == 0) {
				lookupSuffix = SystemGlobals.getValue(ConfigKeys.LDAP_LOGIN_SUFFIX);
			}

			String lookupPrincipal = lookupPrefix + username + "," + lookupSuffix ;

			Attribute att = dir.getAttributes(lookupPrincipal ).get(SystemGlobals.getValue(ConfigKeys.LDAP_FIELD_EMAIL));
			
			SSOUtils utils = new SSOUtils();
			
			if (!utils.userExists(username)) {
				String email = att != null ? (String)att.get() : "noemail";
				utils.register("ldap", email);
			}
			
			return utils.getUser();
		}
		catch (AuthenticationException e) {
			return null;
		}
        catch (NamingException e)
        {
            return null;
        }
        finally {
			if (dir != null) {
                try
                {
                    dir.close();
                }
                catch (NamingException e)
                {
                    //close jndi context
                }
            }
		}
	}

	/**
	 * @see net.jforum.sso.LoginAuthenticator#setUserModel(net.jforum.dao.UserDAO)
	 */
	public void setUserModel(UserDAO dao) 
	{
	}
}
