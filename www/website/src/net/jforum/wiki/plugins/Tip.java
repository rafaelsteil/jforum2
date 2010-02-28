/*
 * Created on 29/10/2006 10:21:56
 */
package net.jforum.wiki.plugins;


/**
 * A plugin for displaying tips on the wiki. 
 * 
 * Usage: 
 * <code>
 * [{Tip
 * Text text text
 * }]
 * </code>
 * 
 * @author Rafael Steil
 * @version $Id: Tip.java,v 1.3 2006/11/15 15:46:24 rafaelsteil Exp $
 */
public class Tip extends InformationPlugin {
	public Tip() {
		super("check.gif", "tipMacro");
	}
}
