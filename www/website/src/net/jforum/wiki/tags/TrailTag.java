/*
 * Created on 04/11/2006 10:29:00
 */
package net.jforum.wiki.tags;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import com.ecyrd.jspwiki.WikiContext;
import com.ecyrd.jspwiki.WikiEngine;
import com.ecyrd.jspwiki.tags.WikiTagBase;

/**
 * Implement a "breadcrumb" (most recently visited) trail.
 * This is a slightly changed version of the BreadcrumbsTag of JSPWiki.
 * 
 * @author Ken Liu ken@kenliu.net
 * @version $Id: TrailTag.java,v 1.3 2006/11/15 15:46:26 rafaelsteil Exp $
 */
public class TrailTag extends WikiTagBase {
	private static final String BREADCRUMBTRAIL_KEY = "__Trail";
	
	private int maxPages = 11;
	private String trailSeparator = " | ";
	
	// set to "visit" to show pages that were visited
	private String trailType = "breadcrumb";
	
	/**
	 * @see com.ecyrd.jspwiki.tags.WikiTagBase#doWikiStartTag()
	 */
	public final int doWikiStartTag() throws IOException {
		HttpSession session = pageContext.getSession();
		FixedQueue trail = (FixedQueue) session.getAttribute(BREADCRUMBTRAIL_KEY);
		String page = m_wikiContext.getPage().getName();

		if (trail == null) {
			trail = new FixedQueue(maxPages);
		}

		if (m_wikiContext.getRequestContext().equals(WikiContext.VIEW)) {
			if (trail.isEmpty()) {
				trail.push(page);
			}
			else {
				// Don't add the page to the queue if the page was just refreshed
				if (!((String) trail.getLast()).equals(page)) {
					if (trailType.equalsIgnoreCase("visit")) { 
						if (!trail.contains(page)) {
							trail.push(page);							
						}
					}
					else {
						trail.push(page);
					}
				}
			}
		}

		session.setAttribute(BREADCRUMBTRAIL_KEY, trail); // our trail

		JspWriter out = pageContext.getOut();
		int queueSize = trail.size();
		String linkClass = "wikipage";
		WikiEngine engine = m_wikiContext.getEngine();
		
		int i = 0;
		for (Iterator iter = trail.iterator(); iter.hasNext(); i++) {
			String currentPage = (String)iter.next();
			
			StringBuffer sb = new StringBuffer(128)
				.append("<a class='")
				.append(linkClass)
				.append("' href='")
				.append(engine.getViewURL(currentPage))
				.append("'>");
			
			if (currentPage == page) {
				sb.append("<b>").append(currentPage).append("</b>");
			}
			else {
				sb.append(currentPage);
			}

			sb.append("</a>");

			out.print(sb.toString());

			if (i + 1 < queueSize) {
				out.print(trailSeparator);
			}
		}

		return SKIP_BODY;
	}

	public int getMaxpages() {
		return maxPages;
	}

	public void setMaxpages(int maxpages) {
		maxPages = maxpages + 1;
	}

	public String getSeparator() {
		return trailSeparator;
	}

	public void setSeparator(String separator) {
		trailSeparator = separator;
	}

	public String getTrailType() {
		return trailType;
	}

	public void setTrailType(String type) {
		trailType = type;
	}

	/**
	 * Extends the LinkedList class to provide a fixed-size queue implementation
	 */
	private class FixedQueue extends LinkedList implements Serializable {
		private int size;

		FixedQueue(int size) {
			this.size = size;
		}

		Object push(Object o) {
			add(o);
			
			return this.size() > this.size
				? this.removeFirst()
				: null;
		}
	}
}
