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
package net.jforum;

import java.util.HashMap;
import java.util.Map;


/**
 * Keeps a collection of <code>UrlPattern</code> objects.
 *
 * @author Rafael Steil
 * @version $Id: UrlPatternCollection.java,v 1.1 2006/08/23 02:13:49 rafaelsteil Exp $
 */
public class UrlPatternCollection
{
    private static Map patternsMap = new HashMap();

    /**
     * Try to find a <code>UrlPattern</code> by its name.
     *
     * @param name The pattern name
     * @return The <code>UrlPattern</code> object if a match was found, or <code>null</code> if not
     */
    public static UrlPattern findPattern(String name)
    {
        return (UrlPattern)UrlPatternCollection.patternsMap.get(name);
    }

    /**
     * Adds a new <code>UrlPattern</code>.
     *
     * @param name The pattern name
     * @param value The pattern value
     */
    public static void addPattern(String name, String value)
    {
        UrlPatternCollection.patternsMap.put(name, new UrlPattern(name, value));
    }
}
