/*
 * Created on 29/10/2006 11:45:47
 */
package net.jforum.wiki.plugins;

import java.util.Map;

import com.ecyrd.jspwiki.WikiContext;
import com.ecyrd.jspwiki.plugin.PluginException;
import com.ecyrd.jspwiki.plugin.WikiPlugin;

/**
 * @author Rafael Steil
 * @version $Id: Highlight.java,v 1.4 2006/11/15 15:46:24 rafaelsteil Exp $
 */
public class Highlight implements WikiPlugin {
	private static final String BODY = "_cmdline";
	
	/**
	 * @see com.ecyrd.jspwiki.plugin.WikiPlugin#execute(com.ecyrd.jspwiki.WikiContext, java.util.Map)
	 */
	public String execute(WikiContext context, Map params) throws PluginException {
		StringBuffer sb = new StringBuffer(256);
		
		String text = (String)params.get(BODY);
		
		if (text == null) {
			text = "";
		}
		
		sb.append("<table class='highlightMacro' cellpadding='1' width='85%'>")
		.append("<tr>")
		.append("<td>");
		sb.append("<p>").append(context.getEngine().textToHTML(context, text)).append("</p></td>")
		.append("</tr>")
		.append("</table>");
		
		return sb.toString();
	}
}
