/*
 * Created on 29/10/2006 11:07:46
 */
package net.jforum.wiki.plugins;

import java.util.Map;

import com.ecyrd.jspwiki.WikiContext;
import com.ecyrd.jspwiki.plugin.PluginException;
import com.ecyrd.jspwiki.plugin.WikiPlugin;

/**
 * @author Rafael Steil
 * @version $Id: InformationPlugin.java,v 1.3 2006/11/15 15:46:24 rafaelsteil Exp $
 */
public abstract class InformationPlugin implements WikiPlugin {
	private static final String CMD_LINE = "_cmdline";
	private static final String BODY = "_body";
	private static final String TITLE = "title";
	
	private String icon;
	private String css;
	
	protected InformationPlugin(String icon, String css)
	{
		this.icon = icon;
		this.css = css;
	}

	/**
	 * @see com.ecyrd.jspwiki.plugin.WikiPlugin#execute(com.ecyrd.jspwiki.WikiContext, java.util.Map)
	 */
	public String execute(WikiContext context, Map params) throws PluginException {
		StringBuffer sb = new StringBuffer(256);
		
		String title = (String)params.get(TITLE);
		String text = title == null
			? (String)params.get(CMD_LINE)
			: (String)params.get(BODY);
			
		sb.append("<table class='").append(this.css).append("' cellpadding='5' width='85%'>")
		.append("<tr>")
		.append("<td valign='top' width='16'>")
		.append("<img src='").append(context.getEngine().getBaseURL())
		.append("/images/").append(this.icon).append("'></td>")
		.append("<td>");
		
		if (title != null) {
			sb.append("<b>").append(context.getEngine().textToHTML(context, title)).append("</b><br>");
		}
		
		sb.append("<p>").append(context.getEngine().textToHTML(context, text)).append("</p></td>")
		.append("</tr>")
		.append("</table>");
		
		return sb.toString();
	}
}
