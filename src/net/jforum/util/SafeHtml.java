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
 * This file creation date: 27/09/2004 23:59:10
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import net.jforum.exceptions.ForumException;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;
import net.jforum.view.forum.common.ViewCommon;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;

/**
 * Process text with html and remove possible malicious tags and attributes.
 * Work based on tips from Amit Klein and the following documents:
 * <br>
 * <li>http://ha.ckers.org/xss.html
 * <li>http://quickwired.com/kallahar/smallprojects/php_xss_filter_function.php
 * <br>
 * @author Rafael Steil
 * @version $Id: SafeHtml.java,v 1.25 2007/09/19 14:08:57 rafaelsteil Exp $
 */
public class SafeHtml 
{
	private static Set welcomeTags;
	private static Set welcomeAttributes;
	private static Set allowedProtocols;
	
	static {
		welcomeTags = new HashSet();
		welcomeAttributes = new HashSet();
		allowedProtocols = new HashSet();
		
		splitAndTrim(ConfigKeys.HTML_TAGS_WELCOME, welcomeTags);
		splitAndTrim(ConfigKeys.HTML_ATTRIBUTES_WELCOME, welcomeAttributes);
		splitAndTrim(ConfigKeys.HTML_LINKS_ALLOW_PROTOCOLS, allowedProtocols);
	}
	
	private static void splitAndTrim(String s, Set data)
	{
		String s1 = SystemGlobals.getValue(s);
		
		if (s1 == null) {
			return;
		}
		
		String[] tags = s1.toUpperCase().split(",");

		for (int i = 0; i < tags.length; i++) {
			data.add(tags[i].trim());
		}
	}
	
	/**
	 * Given an input, analyze each HTML tag and remove unsecure attributes from them. 
	 * @param contents The content to verify
	 * @return the content, secure. 
	 */
	public String ensureAllAttributesAreSafe(String contents) 
	{
		StringBuffer sb = new StringBuffer(contents.length());
		
		try {
			Lexer lexer = new Lexer(contents);
			Node node;
			
			while ((node = lexer.nextNode()) != null) {
				if (node instanceof Tag) {
					Tag tag = (Tag)node;
					
					this.checkAndValidateAttributes(tag, false);
					
					sb.append(tag.toHtml());
				}
				else {
					sb.append(node.toHtml());
				}
			}
		}
		catch (Exception e) {
			throw new ForumException("Problems while parsing HTML: " + e, e);
		}
		
		return sb.toString();
	}
	
	/**
	 * Given an input, makes it safe for HTML displaying. 
	 * Removes any not allowed HTML tag or attribute, as well
	 * unwanted Javascript statements inside the tags. 
	 * @param contents the input to analyze
	 * @return the modified and safe string
	 */
	public String makeSafe(String contents)
	{
		if (contents == null || contents.length() == 0) {
			return contents;
		}
		
		StringBuffer sb = new StringBuffer(contents.length());
		
		try {
			Lexer lexer = new Lexer(contents);
			Node node;
			
			while ((node = lexer.nextNode()) != null) {
				boolean isTextNode = node instanceof TextNode;
				
				if (isTextNode) {
					// Text nodes are raw data, so we just
					// strip off all possible html content
					String text = node.toHtml();
					
					if (text.indexOf('>') > -1 || text.indexOf('<') > -1) {
						StringBuffer tmp = new StringBuffer(text);
						
						ViewCommon.replaceAll(tmp, "<", "&lt;");
						ViewCommon.replaceAll(tmp, ">", "&gt;");
						ViewCommon.replaceAll(tmp, "\"", "&quot;");
						
						node.setText(tmp.toString());
					}
				}
				
				if (isTextNode || (node instanceof Tag && this.isTagWelcome(node))) {
					sb.append(node.toHtml());
				}
				else {
					StringBuffer tmp = new StringBuffer(node.toHtml());
					
					ViewCommon.replaceAll(tmp, "<", "&lt;");
					ViewCommon.replaceAll(tmp, ">", "&gt;");
					
					sb.append(tmp.toString());
				}
			}
		}
		catch (Exception e) {
			throw new ForumException("Error while parsing HTML: " + e, e);
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns true if a given tag is allowed. 
	 * Also, it checks and removes any unwanted attribute the tag may contain. 
	 * @param node The tag node to analyze
	 * @return true if it is a valid tag. 
	 */
	private boolean isTagWelcome(Node node)
	{
		Tag tag = (Tag)node;

		if (!welcomeTags.contains(tag.getTagName())) {
			return false;
		}
		
		this.checkAndValidateAttributes(tag, true);
		
		return true;
	}
	
	/**
	 * Given a tag, check its attributes, removing those unwanted or not secure 
	 * @param tag The tag to analyze
	 * @param checkIfAttributeIsWelcome true if the attribute name should be matched
	 * against the list of welcome attributes, set in the main configuration file. 
	 */
	private void checkAndValidateAttributes(Tag tag, boolean checkIfAttributeIsWelcome)
	{
		Vector newAttributes = new Vector();
		
		for (Iterator iter = tag.getAttributesEx().iterator(); iter.hasNext(); ) {
			Attribute a = (Attribute)iter.next();

			String name = a.getName();
			
			if (name == null) {
				newAttributes.add(a);
			}
			else {
				name = name.toUpperCase();
				
				if (a.getValue() == null) {
					newAttributes.add(a);
					continue;
				}
				
				String value = a.getValue().toLowerCase();
				
				if (checkIfAttributeIsWelcome && !this.isAttributeWelcome(name)) {
					continue;
				}
				
				if (!this.isAttributeSafe(name, value)) {
					continue;
				}
					
				if (a.getValue().indexOf("&#") > -1) {
					a.setValue(a.getValue().replaceAll("&#", "&amp;#"));
				}
				
				newAttributes.add(a);
			}
		}
		
		tag.setAttributesEx(newAttributes);
	}
	
	/**
	 * Check if the given attribute name is in the list of allowed attributes
	 * @param name the attribute name
	 * @return true if it is an allowed attribute name
	 */
	private boolean isAttributeWelcome(String name)
	{
		return welcomeAttributes.contains(name);
	}

	/**
	 * Check if the attribute is safe, checking either its name and value. 
	 * @param name the attribute name
	 * @param value the attribute value
	 * @return true if it is a safe attribute
	 */
	private boolean isAttributeSafe(String name, String value)
	{
		if (name.length() >= 2 && name.charAt(0) == 'O' && name.charAt(1) == 'N') {
			return false;
		}
		
		if (value.indexOf('\n') > -1 || value.indexOf('\r') > -1 || value.indexOf('\0') > -1) {
			return false;
		}
			
		if (("HREF".equals(name) || "SRC".equals(name))) {
			if (!this.isHrefValid(value)) {
				return false;
			}
		}
		else if ("STYLE".equals(name)) {
			// It is much more a try to not allow constructions
			// like style="background-color: url(javascript:xxxx)" than anything else
			if (value.indexOf('(') > -1) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Checks if a given address is valid
	 * @param href The address to check
	 * @return true if it is valid
	 */
	private boolean isHrefValid(String href) 
	{
		if (SystemGlobals.getBoolValue(ConfigKeys.HTML_LINKS_ALLOW_RELATIVE)
			&& href.length() > 0 
			&& href.charAt(0) == '/') {
			return true;
		}
		
		for (Iterator iter = allowedProtocols.iterator(); iter.hasNext(); ) {
			String protocol = iter.next().toString().toLowerCase();
			
			if (href.startsWith(protocol)) {
				return true;
			}
		}
		
		return false;
	}
}
