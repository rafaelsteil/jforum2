/*
 * Copyright (c) 2006, JForum Team
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
package net.jforum.http;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * User: SergeMaslyukov
 * Date: 24.08.2006
 * Time: 23:51:07
 * <p/>
 * $Id: FakeHttpResponse.java,v 1.1 2006/08/24 21:02:58 sergemaslyukov Exp $
 */
public class FakeHttpResponse implements HttpServletResponse
{

    public void addCookie(Cookie cookie)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean containsHeader(String name)
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String encodeURL(String url)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String encodeRedirectURL(String url)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String encodeUrl(String url)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String encodeRedirectUrl(String url)
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void sendError(int sc, String msg) throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void sendError(int sc) throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void sendRedirect(String location) throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setDateHeader(String name, long date)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addDateHeader(String name, long date)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setHeader(String name, String value)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addHeader(String name, String value)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setIntHeader(String name, int value)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addIntHeader(String name, int value)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setStatus(int sc)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setStatus(int sc, String sm)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCharacterEncoding()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getContentType()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ServletOutputStream getOutputStream() throws IOException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PrintWriter getWriter() throws IOException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setCharacterEncoding(String charset)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setContentLength(int len)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setContentType(String type)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setBufferSize(int size)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getBufferSize()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void flushBuffer() throws IOException
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void resetBuffer()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isCommitted()
    {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void reset()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setLocale(Locale loc)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Locale getLocale()
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
