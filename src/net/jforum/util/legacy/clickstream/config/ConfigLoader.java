package net.jforum.util.legacy.clickstream.config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.jforum.util.preferences.ConfigKeys;
import net.jforum.util.preferences.SystemGlobals;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *Loads clickstream.xml for JForum.
 * 
 * @author <a href="plightbo@hotmail.com">Patrick Lightbody</a>
 * @author Rafael Steil (little hacks for JForum)
 * @version $Id: ConfigLoader.java,v 1.6 2005/12/18 02:12:52 rafaelsteil Exp $
 */
public class ConfigLoader
{
	private static final Logger log = Logger.getLogger(ConfigLoader.class);

	private ClickstreamConfig config;

	private static ConfigLoader instance = new ConfigLoader();;

	public static ConfigLoader instance()
	{
		return instance;
	}

	private ConfigLoader() {}

	public ClickstreamConfig getConfig()
	{
		if (this.config != null) {
			return this.config;
		}

		synchronized (instance) {
			this.config = new ClickstreamConfig();
	
			try {
				SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
	
				String path = SystemGlobals.getValue(ConfigKeys.CLICKSTREAM_CONFIG);
				
				if (path != null) {
					if (log.isInfoEnabled()) {
						log.info("Loading clickstream config from " + path);
					}
					
					File fileInput = new File(path);
					
					if (fileInput.exists()) {
						parser.parse(fileInput, new ConfigHandler());
					}
					else {
						parser.parse(new InputSource(path), new ConfigHandler());
					}
				}
				return config;
			}
			catch (SAXException e) {
				log.error("Could not parse clickstream XML", e);
				throw new RuntimeException(e.getMessage());
			}
			catch (IOException e) {
				log.error("Could not read clickstream config from stream", e);
				throw new RuntimeException(e.getMessage());
			}
			catch (ParserConfigurationException e) {
				log.fatal("Could not obtain SAX parser", e);
				throw new RuntimeException(e.getMessage());
			}
			catch (RuntimeException e) {
				log.fatal("RuntimeException", e);
				throw e;
			}
			catch (Throwable e) {
				log.fatal("Exception", e);
				throw new RuntimeException(e.getMessage());
			}
		}
	}

	/**
	 * SAX Handler implementation for handling tags in config file and building config objects.
	 */
	private class ConfigHandler extends DefaultHandler
	{
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			if (qName.equals("bot-host")) {
				config.addBotHost(attributes.getValue("name"));
			}
			else if (qName.equals("bot-agent")) {
				config.addBotAgent(attributes.getValue("name"));
			}
		}
	}
}
