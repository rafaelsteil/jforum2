package net.jforum.util.legacy.clickstream;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.jforum.util.legacy.clickstream.config.ClickstreamConfig;
import net.jforum.util.legacy.clickstream.config.ConfigLoader;

/**
 * Determines if a request is actually a bot or spider.
 * 
 * @author <a href="plightbo@hotmail.com">Patrick Lightbody</a>
 * @author Rafael Steil (little hacks for JForum)
 * @version $Id: BotChecker.java,v 1.6 2005/12/18 02:12:54 rafaelsteil Exp $
 */
public class BotChecker
{
	/**
	 * Checks if we have a bot
	 * @param request the request
	 * @return <code>null</code> if there is no bots in the current request, 
	 * or the bot's name otherwise
	 */
	public static String isBot(HttpServletRequest request)
	{
		if (request.getRequestURI().indexOf("robots.txt") != -1) {
			// there is a specific request for the robots.txt file, so we assume
			// it must be a robot (only robots request robots.txt)
			return "Unknown (asked for robots.txt)";
		}
		
		String userAgent = request.getHeader("User-Agent");
		
		ClickstreamConfig config = ConfigLoader.instance().getConfig();
		
		if (userAgent != null && config != null) {
			List agents = config.getBotAgents();
			
			userAgent = userAgent.toLowerCase();
			
			for (Iterator iterator = agents.iterator(); iterator.hasNext(); ) {
				String agent = (String) iterator.next();
				
				if (agent == null) {
					continue;
				}
				
				if (userAgent.indexOf(agent) != -1) {
					return userAgent;
				}
			}
		}
		
		String remoteHost = request.getRemoteHost(); // requires a DNS lookup
		
		if (remoteHost != null && remoteHost.length() > 0 && remoteHost.charAt(remoteHost.length() - 1) > 64) {
			List hosts = config.getBotHosts();
			
			remoteHost = remoteHost.toLowerCase();
			
			for (Iterator iterator = hosts.iterator(); iterator.hasNext(); ) {
				String host = (String) iterator.next();
				
				if (host == null) {
					continue;
				}
				
				if (remoteHost.indexOf(host) != -1) {
					return remoteHost;
				}
			}
		}

		return null;
	}
}