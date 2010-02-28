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
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.context;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: SergeMaslyukov Date: 20.08.2006 Time: 18:48:56 <p/> $Id: WebContextResponse.java,v 1.1
 * 2006/08/20 15:30:30 sergemaslyukov Exp $
 */
public interface ResponseContext
{
	/**
	 * Sets the length of the content body in the response In HTTP servlets, this method sets the
	 * HTTP Content-Length header.
	 * 
	 * @param len an integer specifying the length of the content being returned to the client; 
	 * sets the Content-Length header
	 */
	public void setContentLength(int len);

	/**
	 * Returns a boolean indicating whether the named response header has already been set.
	 * 
	 * @param name the header name
	 * @return <code>true</code> if the named response header has already been set;
	 * <code>false</code> otherwise
	 */
	public boolean containsHeader(String name);

	/**
	 * Sets a response header with the given name and value. If the header had already been set, the
	 * new value overwrites the previous one. The <code>containsHeader</code> method can be used
	 * to test for the presence of a header before setting its value.
	 * 
	 * @param name the name of the header
	 * @param value the header value If it contains octet string, it should be encoded 
	 * according to RFC 2047 (http://www.ietf.org/rfc/rfc2047.txt)
	 * 
	 * @see #containsHeader
	 * @see #addHeader
	 */
	public void setHeader(String name, String value);

	/**
	 * Adds the specified cookie to the response. This method can be called multiple times to set
	 * more than one cookie.
	 * 
	 * @param cookie the Cookie to return to the client
	 */
	public void addCookie(Cookie cookie);

	/**
	 * Encodes the specified URL for use in the <code>sendRedirect</code> method or, if encoding
	 * is not needed, returns the URL unchanged. The implementation of this method includes the
	 * logic to determine whether the session ID needs to be encoded in the URL. Because the rules
	 * for making this determination can differ from those used to decide whether to encode a normal
	 * link, this method is separated from the <code>encodeURL</code> method.
	 * 
	 * <p>
	 * All URLs sent to the <code>HttpServletResponse.sendRedirect</code> method should be run
	 * through this method. Otherwise, URL rewriting cannot be used with browsers which do not
	 * support cookies.
	 * 
	 * @param url the url to be encoded.
	 * @return the encoded URL if encoding is needed; the unchanged URL otherwise.
	 * 
	 * @see #sendRedirect
	 * @see #encodeUrl
	 */
	public String encodeRedirectURL(String url);

	/**
	 * Returns the name of the character encoding (MIME charset) used for the body sent in this
	 * response. The character encoding may have been specified explicitly using the
	 * {@link #setCharacterEncoding} or {@link #setContentType} methods, or implicitly using the
	 * {@link #setLocale} method. Explicit specifications take precedence over implicit
	 * specifications. Calls made to these methods after <code>getWriter</code> has been called or
	 * after the response has been committed have no effect on the character encoding. If no
	 * character encoding has been specified, <code>ISO-8859-1</code> is returned.
	 * <p>
	 * See RFC 2047 (http://www.ietf.org/rfc/rfc2047.txt) for more information about character
	 * encoding and MIME.
	 * 
	 * @return a <code>String</code> specifying the name of the character encoding, for example,
	 * <code>UTF-8</code>
	 * 
	 */
	public String getCharacterEncoding();

	/**
	 * Sends a temporary redirect response to the client using the specified redirect location URL.
	 * This method can accept relative URLs; the servlet container must convert the relative URL to
	 * an absolute URL before sending the response to the client. If the location is relative
	 * without a leading '/' the container interprets it as relative to the current request URI. If
	 * the location is relative with a leading '/' the container interprets it as relative to the
	 * servlet container root.
	 * 
	 * <p>
	 * If the response has already been committed, this method throws an IllegalStateException.
	 * After using this method, the response should be considered to be committed and should not be
	 * written to.
	 * 
	 * @param location the redirect location URL
	 * @exception IOException If an input or output exception occurs
	 * @exception IllegalStateException If the response was committed or if a partial 
	 * URL is given and cannot be converted into a valid URL
	 */
	public void sendRedirect(String location) throws IOException;

	/**
	 * Returns a {@link javax.servlet.ServletOutputStream} suitable for writing binary data in the
	 * response. The servlet container does not encode the binary data.
	 * 
	 * <p>
	 * Calling flush() on the ServletOutputStream commits the response.
	 * 
	 * Either this method or {@link #getWriter} may be called to write the body, not both.
	 * 
	 * @return a {@link javax.servlet.ServletOutputStream} for writing binary data
	 * @exception IllegalStateException if the <code>getWriter</code> method has 
	 * been called on this response
	 * @exception IOException if an input or output exception occurred
	 * @see #getWriter
	 */

	public ServletOutputStream getOutputStream() throws IOException;

	/**
	 * Returns a <code>PrintWriter</code> object that can send character text to the client. The
	 * <code>PrintWriter</code> uses the character encoding returned by
	 * {@link #getCharacterEncoding}. If the response's character encoding has not been specified
	 * as described in <code>getCharacterEncoding</code> (i.e., the method just returns the
	 * default value <code>ISO-8859-1</code>), <code>getWriter</code> updates it to
	 * <code>ISO-8859-1</code>.
	 * <p>
	 * Calling flush() on the <code>PrintWriter</code> commits the response.
	 * <p>
	 * Either this method or {@link #getOutputStream} may be called to write the body, not both.
	 * 
	 * @return a <code>PrintWriter</code> object that can return character data to the client
	 * 
	 * @exception java.io.UnsupportedEncodingException if the character encoding 
	 * returned by <code>getCharacterEncoding</code> cannot be used
	 * @exception IllegalStateException if the <code>getOutputStream</code> method has 
	 * already been called for this response object
	 * @exception IOException if an input or output exception occurred
	 * 
	 * @see #getOutputStream
	 * @see #setCharacterEncoding
	 * 
	 */
	public PrintWriter getWriter() throws IOException;

	/**
	 * Sets the content type of the response being sent to the client, if the response has not been
	 * committed yet. The given content type may include a character encoding specification, for
	 * example, <code>text/html;charset=UTF-8</code>. The response's character encoding is only
	 * set from the given content type if this method is called before <code>getWriter</code> is
	 * called.
	 * <p>
	 * This method may be called repeatedly to change content type and character encoding. This
	 * method has no effect if called after the response has been committed. It does not set the
	 * response's character encoding if it is called after <code>getWriter</code> has been called
	 * or after the response has been committed.
	 * <p>
	 * Containers must communicate the content type and the character encoding used for the servlet
	 * response's writer to the client if the protocol provides a way for doing so. In the case of
	 * HTTP, the <code>Content-Type</code> header is used.
	 * 
	 * @param type a <code>String</code> specifying the MIME type of the content
	 * 
	 * @see #setLocale
	 * @see #setCharacterEncoding
	 * @see #getOutputStream
	 * @see #getWriter
	 */
	public void setContentType(String type);

	/**
	 * Encodes the specified URL by including the session ID in it, or, if encoding is not needed,
	 * returns the URL unchanged. The implementation of this method includes the logic to determine
	 * whether the session ID needs to be encoded in the URL. For example, if the browser supports
	 * cookies, or session tracking is turned off, URL encoding is unnecessary.
	 * 
	 * <p>
	 * For robust session tracking, all URLs emitted by a servlet should be run through this method.
	 * Otherwise, URL rewriting cannot be used with browsers which do not support cookies.
	 * 
	 * @param url the url to be encoded.
	 * @return the encoded URL if encoding is needed; the unchanged URL otherwise.
	 */
	public String encodeURL(String url);

	/**
	 * Adds a response header with the given name and value. This method allows response headers to
	 * have multiple values.
	 * 
	 * @param name the name of the header
	 * @param value the additional header value If it contains octet string, 
	 * it should be encoded according to RFC 2047 (http://www.ietf.org/rfc/rfc2047.txt)
	 */
	public void addHeader(String name, String value);

	/**
	 * Sends an error response to the client using the specified status code and clearing the
	 * buffer.
	 * <p>
	 * If the response has already been committed, this method throws an IllegalStateException.
	 * After using this method, the response should be considered to be committed and should not be
	 * written to.
	 * 
	 * @param sc the error status code
	 * @exception java.io.IOException If an input or output exception occurs
	 * @exception IllegalStateException If the response was committed before this method call
	 */
	public void sendError(int sc) throws IOException;
}
