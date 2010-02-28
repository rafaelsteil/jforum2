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
 *
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.context;


import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.Cookie;

/**
 * User: SergeMaslyukov Date: 20.08.2006 Time: 17:18:03 <p/> $Id: WebContextRequest.java,v 1.1
 * 2006/08/20 15:30:29 sergemaslyukov Exp $
 */
public interface RequestContext
{
	/**
	 * Returns the part of this request's URL from the protocol name up to the query string in the
	 * first line of the HTTP request. The web container does not decode this String. For example:
	 * 
	 * <table summary="Examples of Returned Values">
	 * <tr align=left>
	 * <th>First line of HTTP request </th>
	 * <th> Returned Value</th>
	 * <tr>
	 * <td>POST /some/path.html HTTP/1.1
	 * <td>
	 * <td>/some/path.html
	 * <tr>
	 * <td>GET http://foo.bar/a.html HTTP/1.0
	 * <td>
	 * <td>/a.html
	 * <tr>
	 * <td>HEAD /xyz?a=b HTTP/1.1
	 * <td>
	 * <td>/xyz </table>
	 * 
	 * <p>
	 * To reconstruct an URL with a scheme and host, use
	 * {@link javax.servlet.http.HttpUtils#getRequestURL}.
	 * 
	 * @return a <code>String</code> containing the part of the URL from the protocol name up to
	 *         the query string
	 * 
	 * @see javax.servlet.http.HttpUtils#getRequestURL
	 */
	public String getRequestURI();

	/**
	 * Returns the query string that is contained in the request URL after the path. This method
	 * returns <code>null</code> if the URL does not have a query string. Same as the value of the
	 * CGI variable QUERY_STRING.
	 * 
	 * @return a <code>String</code> containing the query string or <code>null</code> if the URL
	 *         contains no query string. The value is not decoded by the container.
	 */
	public String getQueryString();

	/**
	 * Returns the value of the specified request header as a <code>String</code>. If the request
	 * did not include a header of the specified name, this method returns <code>null</code>. If
	 * there are multiple headers with the same name, this method returns the first head in the
	 * request. The header name is case insensitive. You can use this method with any request
	 * header.
	 * 
	 * @param name
	 *            a <code>String</code> specifying the header name
	 * 
	 * @return a <code>String</code> containing the value of the requested header, or
	 *         <code>null</code> if the request does not have a header of that name
	 */
	public String getHeader(String name);

	/**
	 * Returns an array containing all of the <code>Cookie</code> objects the client sent with
	 * this request. This method returns <code>null</code> if no cookies were sent.
	 * 
	 * @return an array of all the <code>Cookies</code> included with this request, or
	 *         <code>null</code> if the request has no cookies
	 */
	public Cookie[] getCookies();

	/**
	 * Returns the Internet Protocol (IP) address of the client or last proxy that sent the request.
	 * For HTTP servlets, same as the value of the CGI variable <code>REMOTE_ADDR</code>.
	 * 
	 * @return a <code>String</code> containing the IP address of the client that sent the request
	 */
	public String getRemoteAddr();

	/**
	 * Returns the port number to which the request was sent. It is the value of the part after ":"
	 * in the <code>Host</code> header value, if any, or the server port where the client
	 * connection was accepted on.
	 * 
	 * @return an integer specifying the port number
	 */
	public int getServerPort();

	/**
	 * Returns the name of the scheme used to make this request, for example, <code>http</code>,
	 * <code>https</code>, or <code>ftp</code>. Different schemes have different rules for
	 * constructing URLs, as noted in RFC 1738.
	 * 
	 * @return a <code>String</code> containing the name of the scheme used to make this request
	 */
	public String getScheme();

	/**
	 * Returns the host name of the server to which the request was sent. It is the value of the
	 * part before ":" in the <code>Host</code> header value, if any, or the resolved server name,
	 * or the server IP address.
	 * 
	 * @return a <code>String</code> containing the name of the server
	 */
	public String getServerName();

	/**
	 * Removes an attribute from this request. This method is not generally needed as attributes
	 * only persist as long as the request is being handled.
	 * 
	 * <p>
	 * Attribute names should follow the same conventions as package names. Names beginning with
	 * <code>java.*</code>, <code>javax.*</code>, and <code>com.sun.*</code>, are reserved
	 * for use by Sun Microsystems.
	 * 
	 * @param name a <code>String</code> specifying the name of the attribute to remove
	 */
	public void removeAttribute(String name);

	/**
	 * Stores an attribute in this request. Attributes are reset between requests. This method is
	 * most often used in conjunction with {@link javax.servlet.RequestDispatcher}.
	 * 
	 * <p>
	 * Attribute names should follow the same conventions as package names. Names beginning with
	 * <code>java.*</code>, <code>javax.*</code>, and <code>com.sun.*</code>, are reserved
	 * for use by Sun Microsystems. <br>
	 * If the object passed in is null, the effect is the same as calling {@link #removeAttribute}.
	 * <br>
	 * It is warned that when the request is dispatched from the servlet resides in a different web
	 * application by <code>RequestDispatcher</code>, the object set by this method may not be
	 * correctly retrieved in the caller servlet.
	 * 
	 * @param name a <code>String</code> specifying the name of the attribute
	 * @param o the <code>Object</code> to be stored
	 */
	public void setAttribute(String name, Object o);

	/**
	 * Returns the value of the named attribute as an <code>Object</code>, or <code>null</code>
	 * if no attribute of the given name exists.
	 * 
	 * <p>
	 * Attributes can be set two ways. The servlet container may set attributes to make available
	 * custom information about a request. For example, for requests made using HTTPS, the attribute
	 * <code>javax.servlet.request.X509Certificate</code> can be used to retrieve information on
	 * the certificate of the client. Attributes can also be set programatically using
	 * {@link #setAttribute}. This allows information to be embedded into a request before a
	 * {@link javax.servlet.RequestDispatcher} call.
	 * 
	 * <p>
	 * Attribute names should follow the same conventions as package names. This specification
	 * reserves names matching <code>java.*</code>, <code>javax.*</code>, and
	 * <code>sun.*</code>.
	 * 
	 * @param name a <code>String</code> specifying the name of the attribute
	 * @return an <code>Object</code> containing the value of the attribute, 
	 * or <code>null</code> if the attribute does not exist
	 */

	public Object getAttribute(String name);

	/**
	 * Overrides the name of the character encoding used in the body of this request. This method
	 * must be called prior to reading request parameters or reading input using getReader().
	 * 
	 * @param env a <code>String</code> containing the name of the character encoding.
	 * @throws java.io.UnsupportedEncodingException if this is not a valid encoding
	 */

	public void setCharacterEncoding(String env) throws UnsupportedEncodingException;

	/**
	 * Returns the current <code>HttpSession</code> associated with this request or, if there is
	 * no current session and <code>create</code> is true, returns a new session.
	 * 
	 * <p>
	 * If <code>create</code> is <code>false</code> and the request has no valid
	 * <code>HttpSession</code>, this method returns <code>null</code>.
	 * 
	 * <p>
	 * To make sure the session is properly maintained, you must call this method before the
	 * response is committed. If the container is using cookies to maintain session integrity and is
	 * asked to create a new session when the response is committed, an IllegalStateException is
	 * thrown.
	 * 
	 * @param create <code>true</code> to create a new session for this request if necessary;
	 * <code>false</code> to return <code>null</code> if there's no current session
	 * @return the <code>HttpSession</code> associated with this request or <code>null</code> if
	 * <code>create</code> is <code>false</code> and the request has no valid session
	 * 
	 * @see #getSessionContext()
	 */

	public SessionContext getSessionContext(boolean create);

	/**
	 * Returns the current session associated with this request, or if the request does not have a
	 * session, creates one.
	 * 
	 * @return the <code>HttpSession</code> associated with this request
	 * @see #getSessionContext(boolean)
	 */

	public SessionContext getSessionContext();

	/**
	 * Returns the portion of the request URI that indicates the context of the request. The context
	 * path always comes first in a request URI. The path starts with a "/" character but does not
	 * end with a "/" character. For servlets in the default (root) context, this method returns "".
	 * The container does not decode this string.
	 * 
	 * @return a <code>String</code> specifying the portion of the request URI that indicates the
	 * context of the request
	 */

	public String getContextPath();

	/**
	 * Returns the login of the user making this request, if the user has been authenticated, or
	 * <code>null</code> if the user has not been authenticated. Whether the user name is sent
	 * with each subsequent request depends on the browser and type of authentication. Same as the
	 * value of the CGI variable REMOTE_USER.
	 * 
	 * @return a <code>String</code> specifying the login of the user making this request, or
	 * <code>null</code> if the user login is not known
	 */

	public String getRemoteUser();

	/**
	 * Gets an parameter that is a number. A call to <code>Integer#parseInt(String)</code> is made
	 * to do the conversion
	 * 
	 * @param parameter The parameter name to get the value
	 * @return int
	 */
	public int getIntParameter(String parameter);

	/**
	 * Returns an array of <code>String</code> objects containing all of the values the given
	 * request parameter has, or <code>null</code> if the parameter does not exist.
	 * 
	 * <p>
	 * If the parameter has a single value, the array has a length of 1.
	 * 
	 * @param name a <code>String</code> containing the name of the parameter 
	 * whose value is requested
	 * 
	 * @return an array of <code>String</code> objects containing the parameter's values
	 * @see #getParameter
	 */
	public String[] getParameterValues(String name);

	/**
	 * Returns the value of a request parameter as a <code>String</code>, or <code>null</code>
	 * if the parameter does not exist. Request parameters are extra information sent with the
	 * request. For HTTP servlets, parameters are contained in the query string or posted form data.
	 * 
	 * <p>
	 * You should only use this method when you are sure the parameter has only one value. If the
	 * parameter might have more than one value, use {@link #getParameterValues}.
	 * 
	 * <p>
	 * If you use this method with a multivalued parameter, the value returned is equal to the first
	 * value in the array returned by <code>getParameterValues</code>.
	 * 
	 * <p>
	 * If the parameter data was sent in the request body, such as occurs with an HTTP POST request,
	 * then reading the body directly via {@link #getInputStream} or {@link #getReader} can
	 * interfere with the execution of this method.
	 * 
	 * @param name a <code>String</code> specifying the name of the parameter
	 * @return a <code>String</code> representing the single value of the parameter
	 * @see #getParameterValues
	 */
	public String getParameter(String name);

	/**
	 * Returns an <code>Enumeration</code> of <code>String</code> objects containing the names
	 * of the parameters contained in this request. If the request has no parameters, the method
	 * returns an empty <code>Enumeration</code>.
	 * 
	 * @return an <code>Enumeration</code> of <code>String</code> objects, each
	 * <code>String</code> containing the name of a request parameter; or an empty
	 * <code>Enumeration</code> if the request has no parameters
	 */
	public Enumeration getParameterNames();

	/**
	 * Gets the <i>action</i> of the current request.
	 * 
	 * An <i>Action</i> is the parameter name which specifies what next action should be done by
	 * the system. It may be add or edit a post, editing the groups, whatever. In the URL, the
	 * Action can the represented in two forms:
	 * <p>
	 * <blockquote> <code>
	 * http://www.host.com/webapp/servletName?module=groups&action=list
	 * </code>
	 * </blockquote>
	 * <p>
	 * or
	 * <p>
	 * <blockquote> <code>
	 * http://www.host.com/webapp/servletName/groups/list
	 * </code> </blockquote>
	 * <p>
	 * In both situations, the action's name is "list".
	 * 
	 * @return String representing the action name
	 */
	public String getAction();

	/**
	 * Gets the <i>module</i> of the current request.
	 * 
	 * A <i>Module</i> is the parameter name which specifies what module the user is requesting. It
	 * may be the group administration, the topics or anything else configured module. In the URL,
	 * the Module can the represented in two forms:
	 * <p>
	 * <blockquote> <code>
	 * http://www.host.com/webapp/servletName?module=groups&action=list
	 * </code>
	 * </blockquote>
	 * <p>
	 * or
	 * <p>
	 * <blockquote> <code>
	 * http://www.host.com/webapp/servletName/groups/list
	 * </code> </blockquote>
	 * <p>
	 * In both situations, the module's name is "groups".
	 * 
	 * @return String representing the module name
	 */
	public String getModule();

	/**
	 * Adds a new parameter to the request.
	 * If there is already one parameter which name is equals to the 
	 * value of the "name" parameter, a set of values associated to that
	 * name will be generated, thus requiring a call to getParameterValues()
	 * to retrieve them all. 
	 * 
	 * If you want to <strong>replace</strong> a possible existing value, 
	 * use {@link #addOrReplaceParameter(String, Object)}
	 * 
	 * @param name Parameter name
	 * @param value Parameter value
	 */
	public void addParameter(String name, Object value);
	
	/**
	 * Replace or add a parameter. If it does not exist, it is added to the list, 
	 * otherwise the existing value will be replaced by the new value. 
	 * 
	 * @param name
	 * @param value
	 */
	public void addOrReplaceParameter(String name, Object value);

	/**
	 * Gets some request parameter as <code>Object</code>. This method may be used when you have
	 * to get some value of a <i>multipart/form-data</i> request, like a image of file. <br>
	 * 
	 * @param parameter String
	 * @return Object
	 */
	public Object getObjectParameter(String parameter);
	
	/**
	 * Gets user browser's locale. This method may be used during first installation to
	 * automatically switch to corresponding language I18N resource.
	 * 
	 * @return Locale
	 */
	public Locale getLocale();
}
