package au.org.ala.dashboard

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.converters.JSON

/**
 * Handles caching of service responses (after transforming).
 * Uses passed closures to handle service requests - so remains independent
 * of the source of information.
 * Implements the info source for 'static' counts read from a config file.
 */
class CacheService {

    static cache = [:]

    /**
     * Returns the cached results for the specified key if available and fresh
     * else calls the passed closure to get the results (and cache them).
     * @param key for cache storage
     * @param source closure to retrieve the results if required
     * @param maxAgeInDays the maximum age of the cached results
     * @return the results
     */
    def get(String key, Closure source, int maxAgeInDays = 1) {
        def cached = cache[key]
        def results

        if (cached?.resp && !(new Date().after(cached?.time + maxAgeInDays))) {
            log.info "using cache for ${key}"
            results = cached.resp
        } else if (cached?.resp && new Date().after(cached?.time + maxAgeInDays)) {
            log.info "cached result for ${key} has expired"
            // This prevents new user from refreshing the cache while it is happening in the background
            cached.time = new Date()
            // We trigger the cache refresh for this particular key in a separate thread
            RefreshCacheEntryJob.triggerNow([cache: cache, key: key, source: source])
            // We return the current cached value which probably is not the new one for the current request
            results = cached.resp
        } else {
            try {
                log.debug "retrieving " + key
                results = source.call()
                cache.put key, [resp: results, time: new Date()]
            } catch (e) {
                log.error "There was a problem retrieving the dashboard data for key ${key}: ${e.message}"
            }
        }

        return results
    }

    def clear(key) {
        cache[key]?.resp = null
    }

    def clear() {
        cache = [:]
    }

    /**
     * Info provider based on an external metadata file.
     * Loading any key will load results for all keys in the file.
     * @param key the type of info requested
     * @return
     */
    def loadStaticCacheFromFile(key) {
        log.info 'loading static data from file'
        def json = new File(ConfigurationHolder.config.dashboard.data.file as String).text
        if (json) {
            JSON.parse(json).each { k,v ->
                cache.put k, [resp: v, time: new Date()]
            }
        }
        return cache[key]?.resp
    }
}



