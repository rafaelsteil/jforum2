package net.jforum.util.legacy.clickstream;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.jforum.util.preferences.ConfigKeys;

import org.apache.log4j.Logger;

/**
 * The filter that keeps track of a new entry in the clickstream for <b>every request</b>.
 * 
 * @author <a href="plightbo@hotmail.com">Patrick Lightbody</a>
 * @author Rafael Steil (little hacks for JForum)
 * @version $Id: ClickstreamFilter.java,v 1.6 2007/09/20 16:07:09 rafaelsteil Exp $
 */
public class ClickstreamFilter implements Filter
{
	private static final Logger log = Logger.getLogger(ClickstreamFilter.class);

	/**
	 * Attribute name indicating the filter has been applied to a given request.
	 */
	private final static String FILTER_APPLIED = "_clickstream_filter_applied";

	/**
	 * Processes the given request and/or response.
	 * 
	 * @param request The request
	 * @param response The response
	 * @param chain The processing chain
	 * @throws IOException If an error occurs
	 * @throws ServletException If an error occurs
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		// Ensure that filter is only applied once per request.
		if (request.getAttribute(FILTER_APPLIED) == null) {
			request.setAttribute(FILTER_APPLIED, Boolean.TRUE);
			
			String bot = BotChecker.isBot((HttpServletRequest)request);
			
			if (bot != null && log.isDebugEnabled()) {
				log.debug("Found a bot: " + bot);
			}
			
			request.setAttribute(ConfigKeys.IS_BOT, Boolean.valueOf(bot != null));
		}
		
		// Pass the request on
		chain.doFilter(request, response);
	}

	/**
	 * Initializes this filter.
	 * 
	 * @param filterConfig The filter configuration
	 * @throws ServletException If an error occurs
	 */
	public void init(FilterConfig filterConfig) throws ServletException {}

	/**
	 * Destroys this filter.
	 */
	public void destroy() {}
}