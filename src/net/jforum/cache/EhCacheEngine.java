/******************************************************************************
 * Sony Online Entertainment
 * Application Engineering
 *
 * Unpublished work Copyright 2005 Sony Online Entertainment Inc.
 * All rights reserved.
 * Created on Oct 11, 2005
 ******************************************************************************/
package net.jforum.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.jforum.util.preferences.SystemGlobals;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
/**
 * The rest of the application seems to make some invalid assumptions about how
 * things are cached.  Those assumptions might be benign, but it is hard to tell
 * without deep testing.  Until this is finishe the JBossCacheEngine should be 
 * configured in a local mode.
 *
 * Created on Oct 11, 2005 
 *
 * @author Jake Fear
 * @version $Id: EhCacheEngine.java,v 1.1 2005/10/14 00:15:54 rafaelsteil Exp $
 */
public class EhCacheEngine implements CacheEngine {

	private static final Logger log = Logger.getLogger(EhCacheEngine.class);
	
	private CacheManager manager;
	
	public void init() {
		try {
			manager = CacheManager.create(SystemGlobals.getValue("ehcache.cache.properties"));
		} catch (CacheException ce) {
			log.error("EhCache could not be initialized", ce);
			throw new RuntimeException(ce);
		}
	}

	public void stop() {
		manager.shutdown();
	}

	public void add(String key, Object value) {
		if (log.isDebugEnabled()) {
			log.debug("Caching " + value + " with key " + key);
		}
		add(DUMMY_FQN, key, value);
	}

	public void add(String fullyQualifiedName, String key, Object value) {
		if (!manager.cacheExists(fullyQualifiedName)) {
			try {
				manager.addCache(fullyQualifiedName);
			} catch (CacheException ce) {
				log.error(ce, ce);
				throw new RuntimeException(ce);
			}
		}
		Cache cache = manager.getCache(fullyQualifiedName);
		
		Element element = new Element(key, (Serializable)value);
		cache.put(element);
	}

	public Object get(String fullyQualifiedName, String key) {
		try {
			if (!manager.cacheExists(fullyQualifiedName)) {
				manager.addCache(fullyQualifiedName);
				return null;
			}
			Cache cache = manager.getCache(fullyQualifiedName);
			Element element = cache.get(key);
			if (element != null) {
				return element.getValue();
			} 
			
			return null;
		} catch (CacheException ce) {
			log.error("EhCache could not be shutdown", ce);
			throw new RuntimeException(ce);
		}
	}

	public Object get(String fullyQualifiedName) {
		if (!manager.cacheExists(fullyQualifiedName)) {
			try {
				manager.addCache(fullyQualifiedName);
			} catch (CacheException ce) {
				log.error("EhCache could not be shutdown", ce);
				throw new RuntimeException(ce);
			}
		}
		Cache cache = manager.getCache(fullyQualifiedName);
		return cache;
	}

	public Collection getValues(String fullyQualifiedName) {
		try {
			if (!manager.cacheExists(fullyQualifiedName)) {
				manager.addCache(fullyQualifiedName);
				return new ArrayList();
			}
			Cache cache = manager.getCache(fullyQualifiedName);
			List values = new ArrayList(cache.getSize());
			List keys = cache.getKeys();
			
			for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
				values.add(cache.get((Serializable)iter.next()));
			}

			return values;
		} catch (CacheException ce) {
			log.error("EhCache could not be shutdown", ce);
			throw new RuntimeException(ce);
		}
	}

	public void remove(String fullyQualifiedName, String key) {
		Cache cache = manager.getCache(fullyQualifiedName);
		
		if (cache != null) {
			cache.remove(key);
		}
	}

	public void remove(String fullyQualifiedName) {
		if (manager.cacheExists(fullyQualifiedName)) {
			manager.removeCache(fullyQualifiedName);
		}
	}

}
