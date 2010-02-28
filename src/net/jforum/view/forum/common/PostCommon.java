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
 * This file creation date: 21/05/2004 - 15:33:36
 * The JForum Project
 * http://www.jforum.net
 */
package net.jforum.view.forum.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jforum.JForumExecutionContext;
import net.jforum.SessionFacade;
import net.jforum.context.RequestContext;
import net.jforum.dao.PostDAO;
import net.jforum.entities.Post;
import net.jforum.entities.Smilie;
import net.jforum.repository.BBCodeRepository;
import net.jforum.repository.PostRepository;
import net.jforum.repository.SecurityRepository;
import net.jforum.repository.SmiliesRepository;
import net.jforum.security.SecurityConstants;
import net.jforum.util.SafeHtml;
import net.jforum.util.bbcode.BBCode;
import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

/**
 * @author Rafael Steil
 * @version $Id: PostCommon.java,v 1.59 2007/09/24 03:29:17 rafaelsteil Exp $
 */
public class PostCommon
{
	public static Post preparePostForDisplay(Post post)
	{
		if (post.getText() == null) {
			return post;
		}
		
		StringBuffer text = new StringBuffer(post.getText());
		
		if (!post.isHtmlEnabled()) {
			ViewCommon.replaceAll(text, "<", "&lt;");
			ViewCommon.replaceAll(text, ">", "&gt;");
		}
		
		// Do not remove the trailing blank space, as it would
		// cause some regular expressions to fail
		ViewCommon.replaceAll(text, "\n", "<br /> ");
		
		SafeHtml safeHtml = new SafeHtml();
		
		post.setText(text.toString());
		post.setText(safeHtml.makeSafe(post.getText()));
		
		processText(post);
		
		post.setText(safeHtml.ensureAllAttributesAreSafe(post.getText()));
		
		return post;
	}
	
	private static void processText(Post post)
	{
		int codeIndex = post.getText().indexOf("[code");
		int codeEndIndex = codeIndex > -1 ? post.getText().indexOf("[/code]") : -1;
		
		boolean hasCodeBlock = false;
		
		if (codeIndex == -1 || codeEndIndex == -1 || codeEndIndex < codeIndex) {
			post.setText(prepareTextForDisplayExceptCodeTag(post.getText().toString(),
				post.isBbCodeEnabled(), post.isSmiliesEnabled()));
		}
		else if (post.isBbCodeEnabled()) {
			hasCodeBlock = true;
			
			int nextStartPos = 0;
			StringBuffer result = new StringBuffer(post.getText().length());
			
			while (codeIndex > -1 && codeEndIndex > -1 && codeEndIndex > codeIndex) {
				codeEndIndex += "[/code]".length();
				
				String nonCodeResult = prepareTextForDisplayExceptCodeTag(post.getText().substring(nextStartPos, codeIndex), 
					post.isBbCodeEnabled(), post.isSmiliesEnabled());
				
				String codeResult = parseCode(post.getText().substring(codeIndex, codeEndIndex));
				
				result.append(nonCodeResult).append(codeResult);
				
				nextStartPos = codeEndIndex;
				codeIndex = post.getText().indexOf("[code", codeEndIndex);
				codeEndIndex = codeIndex > -1 ? post.getText().indexOf("[/code]", codeIndex) : -1;
			}
			
			
			if (nextStartPos > -1) {
				String nonCodeResult = prepareTextForDisplayExceptCodeTag(post.getText().substring(nextStartPos), 
					post.isBbCodeEnabled(), post.isSmiliesEnabled());
				
				result.append(nonCodeResult);
			}
			
			post.setText(result.toString());
		}
		
		if (hasCodeBlock) {
			JForumExecutionContext.getTemplateContext().put("hasCodeBlock", hasCodeBlock);
		}
	}
	
	private static String parseCode(String text)
	{
		for (Iterator iter = BBCodeRepository.getBBCollection().getBbList().iterator(); iter.hasNext();) {
			BBCode bb = (BBCode)iter.next();
			
			if (bb.getTagName().startsWith("code")) {
				Matcher matcher = Pattern.compile(bb.getRegex()).matcher(text);
				StringBuffer sb = new StringBuffer(text);

				while (matcher.find()) {
					StringBuffer lang = null;
					StringBuffer contents = null;
					
					if ("code".equals(bb.getTagName())) {
					    contents = new StringBuffer(matcher.group(1));
					} 
					else {
						lang = new StringBuffer(matcher.group(1));
						contents = new StringBuffer(matcher.group(2));						
					}
					
					ViewCommon.replaceAll(contents, "<br /> ", "\n");

					// XML-like tags
					ViewCommon.replaceAll(contents, "<", "&lt;");
					ViewCommon.replaceAll(contents, ">", "&gt;");
					
					// Note: there is no replacing for spaces and tabs as
					// we are relying on the Javascript SyntaxHighlighter library
					// to do it for us
					
					StringBuffer replace = new StringBuffer(bb.getReplace());
					int index = replace.indexOf("$1");
					
					if ("code".equals(bb.getTagName())) {
						if (index > -1) {
							replace.replace(index, index + 2, contents.toString());
						}

						index = sb.indexOf("[code]");	
					}
					else {
						if (index > -1) {
							replace.replace(index, index + 2, lang.toString());
						}
						
						index = replace.indexOf("$2");

						if (index > -1) {
							replace.replace(index, index + 2, contents.toString());
						}
						
						index = sb.indexOf("[code=");
					} 
					int lastIndex = sb.indexOf("[/code]", index) + "[/code]".length();

					if (lastIndex > index) {
						sb.replace(index, lastIndex, replace.toString());
					}
				}
				
				text = sb.toString();
			}
		}
		
		return text;
	}
	
	public static String prepareTextForDisplayExceptCodeTag(String text, boolean isBBCodeEnabled, boolean isSmilesEnabled)
	{
		if (text == null) {
			return text;
		}
		
		if (isSmilesEnabled) {
			text = processSmilies(new StringBuffer(text));
		}
		
		if (isBBCodeEnabled && text.indexOf('[') > -1 && text.indexOf(']') > -1) {
			for (Iterator iter = BBCodeRepository.getBBCollection().getBbList().iterator(); iter.hasNext();) {
				BBCode bb = (BBCode)iter.next();
				
				if (!bb.getTagName().startsWith("code")) {
					text = text.replaceAll(bb.getRegex(), bb.getReplace());
				}
			}
		}
		
		text = parseDefaultRequiredBBCode(text);
		
		return text;
	}
	
	public static String parseDefaultRequiredBBCode(String text)
	{
		Collection list = BBCodeRepository.getBBCollection().getAlwaysProcessList();
		
		for (Iterator iter = list.iterator(); iter.hasNext(); ) {
			BBCode bb = (BBCode)iter.next();
			text = text.replaceAll(bb.getRegex(), bb.getReplace());
		}
		
		return text;
	}

	/**
	 * Replace the smlies code by the respective URL.
	 * @param text The text to process
	 * @return the parsed text. Note that the StringBuffer you pass as parameter
	 * will already have the right contents, as the replaces are done on the instance
	 */
	public static String processSmilies(StringBuffer text)
	{
		List smilies = SmiliesRepository.getSmilies();
		
		for (Iterator iter = smilies.iterator(); iter.hasNext(); ) {
			Smilie s = (Smilie) iter.next();
			int pos = text.indexOf(s.getCode());
			
			// The counter is used as prevention, in case
			// the while loop turns into an always true 
			// expression, for any reason
			int counter = 0;
			
			while (pos > -1 && counter++ < 300) {
				text.replace(pos, pos + s.getCode().length(), s.getUrl());
				pos = text.indexOf(s.getCode());
			}
		}

		return text.toString();
	}

	public static Post fillPostFromRequest()
	{
		Post p = new Post();
		p.setTime(new Date());

		return fillPostFromRequest(p, false);
	}

	public static Post fillPostFromRequest(Post p, boolean isEdit) 
	{
		RequestContext request = JForumExecutionContext.getRequest();
		
		p.setSubject(request.getParameter("subject"));
		p.setBbCodeEnabled(request.getParameter("disable_bbcode") == null);
		p.setSmiliesEnabled(request.getParameter("disable_smilies") == null);
		p.setSignatureEnabled(request.getParameter("attach_sig") != null);
		
		if (!isEdit) {
			p.setUserIp(request.getRemoteAddr());
			p.setUserId(SessionFacade.getUserSession().getUserId());
		}
		
		boolean htmlEnabled = SecurityRepository.canAccess(SecurityConstants.PERM_HTML_DISABLED, 
			request.getParameter("forum_id"));
		p.setHtmlEnabled(htmlEnabled && request.getParameter("disable_html") == null);

		if (p.isHtmlEnabled()) {
			p.setText(new SafeHtml().makeSafe(request.getParameter("message")));
		}
		else {
			p.setText(request.getParameter("message"));
		}

		return p;
	}
	
	public static boolean canEditPost(Post post)
	{
		return SessionFacade.isLogged()
			&& (post.getUserId() == SessionFacade.getUserSession().getUserId()
			|| SecurityRepository.canAccess(SecurityConstants.PERM_MODERATION_POST_EDIT));
	}

	public static List topicPosts(PostDAO dao, boolean canEdit, int userId, int topicId, int start, int count)
	{
		boolean needPrepare = true;
		List posts;
		
 		if (SystemGlobals.getBoolValue(ConfigKeys.POSTS_CACHE_ENABLED)) {
 			posts = PostRepository.selectAllByTopicByLimit(topicId, start, count);
 			needPrepare = false;
 		}
 		else {
 			posts = dao.selectAllByTopicByLimit(topicId, start, count);
 		}
 		
		List helperList = new ArrayList();

		int anonymousUser = SystemGlobals.getIntValue(ConfigKeys.ANONYMOUS_USER_ID);

		for (Iterator iter = posts.iterator(); iter.hasNext(); ) {
			Post p;
			
			if (needPrepare) {
				p = (Post)iter.next();
			}
			else {
				p = new Post((Post)iter.next());
			}
			
			if (canEdit || (p.getUserId() != anonymousUser && p.getUserId() == userId)) {
				p.setCanEdit(true);
			}

			helperList.add(needPrepare ? PostCommon.preparePostForDisplay(p) : p);
		}

		return helperList;
	}
}
