/*
 * Created on Mar 15, 2005 1:22:52 PM
 */
package net.jforum.cache;

import org.jboss.cache.Fqn;
import org.jboss.cache.TreeCache;
import org.jboss.cache.TreeCacheListener;
import org.jgroups.View;

/**
 * @author Rafael Steil
 * @version $Id: JBossCacheListener.java,v 1.4 2007/08/01 22:30:06 rafaelsteil Exp $
 */
public class JBossCacheListener implements TreeCacheListener
{
	/**
	 * @see org.jboss.cache.TreeCacheListener#nodeCreated(org.jboss.cache.Fqn)
	 */
	public void nodeCreated(Fqn fqn) {}
	
	/**
	 * @see org.jboss.cache.TreeCacheListener#nodeRemoved(org.jboss.cache.Fqn)
	 */
	public void nodeRemoved(Fqn fqn) {}

	/**
	 * @see org.jboss.cache.TreeCacheListener#nodeLoaded(org.jboss.cache.Fqn)
	 */
	public void nodeLoaded(Fqn fqn) {}

	/**
	 * @see org.jboss.cache.TreeCacheListener#nodeEvicted(org.jboss.cache.Fqn)
	 */
	public void nodeEvicted(Fqn fqn) {}

	/**
	 * @see org.jboss.cache.TreeCacheListener#nodeModified(org.jboss.cache.Fqn)
	 */
	public void nodeModified(Fqn fqn)
	{
		if (CacheEngine.NOTIFICATION.startsWith((String)fqn.get(0))) {
		}
	}

	/**
	 * @see org.jboss.cache.TreeCacheListener#nodeVisited(org.jboss.cache.Fqn)
	 */
	public void nodeVisited(Fqn fqn) {}

	/**
	 * @see org.jboss.cache.TreeCacheListener#cacheStarted(org.jboss.cache.TreeCache)
	 */
	public void cacheStarted(TreeCache cache) {}

	/**
	 * @see org.jboss.cache.TreeCacheListener#cacheStopped(org.jboss.cache.TreeCache)
	 */
	public void cacheStopped(TreeCache cache) {}

	/**
	 * @see org.jboss.cache.TreeCacheListener#viewChange(org.jgroups.View)
	 */
	public void viewChange(View view) {}
}
