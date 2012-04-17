package au.org.ala.dashboard

import org.codehaus.groovy.grails.commons.ConfigurationHolder
import grails.converters.JSON
import java.text.SimpleDateFormat

class MetadataService {

    def webService

    // the metadata cache holds responses from web service lookups
    // refreshing is time based
    def mdCache = [:] // map of lookup responses - value is [resp: <response>, time: <timestamp>]
    
    // the 'static' data cache holds data read from a config file
    // refreshing is manual (and at startup)
    def staticDataCache = [:] // map of 'static' data read from a config file
    
    /**
     * Uses a cached biocache lookup to return the counts for each basis of record.
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def getBasisOfRecord() {
        return cachedBiocacheFacetCount('basis_of_record')
    }

    /**
     * Uses a cached biocache lookup to return the 5 most recorded species for the specified group.
     *
     * @param group the lifeform group or text containing 'all'
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def getMostRecordedSpecies(group) {
        def cacheName = "mostRecorded"+group
        def cached = mdCache[cacheName]
        if (cached && cached.resp && !(new Date().after(cached.time + 1))) {
            println "using cache for most recorded ${group}"
            return cached.resp
        }

        println "looking up most recorded ${group}"

        // get the guids for the 6 most recorded species
        def fq = (group.toLowerCase() =~ 'all' ? '' : "&fq=species_group:${group}")
        def results = biocacheFacetCount('species_guid', "*:*${fq}&flimit=6")

        // look up the name and common names
        def guids = results.facets.collect {it.facet}
        def md = bieBulkLookup(guids)
        results.facets.each {
            def data = md[it.facet]
            if (data) {
                it.name = data.name
                it.common = data.common
            }
        }

        // store in cache - if there was no error
        if (!results.error) {
            mdCache.put cacheName, [resp: results, time: new Date()]
        }

        return results
    }

    /**
     * Uses a cached biocache lookup to return the oldest and newest records and counts of
     * records by century.
     *
     * @return map
     */
    def getDateStats() {
        def cacheName = "dateStats"
        def cached = mdCache[cacheName]
        if (cached && cached.resp && !(new Date().after(cached.time + 1))) {
            println "using cache for date stats"
            return cached.resp
        }
        println "looking up date stats"
        def results = [:]

        // earliest record
        def a = webService.getJson(ConfigurationHolder.config.biocache.baseURL + "/ws/occurrences/search?q=*:*&pageSize=1&sort=occurrence_date&facet=off")
        def earliestUuid = a.occurrences[0].uuid
        def earliest = new Date(a.occurrences[0].eventDate)
        def earliestDate = new SimpleDateFormat("d MMMM yyyy").format(earliest)
        results.earliest = [uuid: earliestUuid, display: earliestDate]

        // latest record
        def b = webService.getJson(ConfigurationHolder.config.biocache.baseURL + "/ws/occurrences/search?q=*:*&pageSize=1&sort=occurrence_date&dir=desc&facet=off")
        def latestUuid = b.occurrences[0].uuid
        def latest = new Date(b.occurrences[0].eventDate)
        def latestDate = new SimpleDateFormat("d MMMM yyyy").format(latest)
        results.latest = [uuid: latestUuid, display: latestDate]

        // get counts by century
        [1600,1700,1800,1900,2000].each {
            def url = ConfigurationHolder.config.biocache.baseURL +
             "/ws/occurrences/search?q=*:*&pageSize=0&facet=off&fq=occurrence_year:[${it}-01-01T00:00:00Z%20TO%20${it + 99}-12-31T23:59:59Z]"
            def c = webService.getJson(url)
            results['c' + it] = c.totalRecords
        }

        mdCache.put cacheName, [resp: results, time: new Date()]
        return results
    }

    /**
     * Uses a cached biocache lookup to return counts for various type statuses.
     *
     * @return map
     */
    def getTypeStats() {
        def cacheName = "typeStats"
        def cached = mdCache[cacheName]
        if (cached && cached.resp && !(new Date().after(cached.time + 1))) {
            println "using cache for type stats"
            return cached.resp
        }
        println "looking up type stats"

        def results = [:]

        // type counts
        def facets = cachedBiocacheFacetCount('type_status')
        facets.facets.each {
            if (it.facet != 'notatype') {
                results[it.facet] = it.count
            }
        }
        results.total = results.values().sum {it}

        // type counts with images
        results.withImage = [:]
        facets = cachedBiocacheFacetCount('type_status',"*:*&fq=multimedia:Image")
        facets.facets.each {
            if (it.facet != 'notatype') {
                results.withImage[it.facet] = it.count
            }
        }
        results.withImage.total = results.withImage.values().sum {it}

        mdCache.put cacheName, [resp: results, time: new Date()]
        return results
    }

    /**
     * Uses a cached collectory lookup to return counts for datasets by type.
     *
     * @return map with total and breakdown by type
     */
    def getDatasets() {
        def action = "Datasets lookup"
        // check cache
        def cached = mdCache.datasets
        if (cached && cached.resp && !(new Date().after(cached.time + 1))) {
            println "using cache for datasets"
            return cached.resp
        }

        // look it up
        def resp = null
        def url = ConfigurationHolder.config.collectory.baseURL +
                "/ws/dataResource/count/resourceType?public=true"

        def conn = new URL(url).openConnection()
        try {
            conn.setConnectTimeout(5000)
            conn.setReadTimeout(50000)
            def json = conn.content.text
            resp = JSON.parse(json)
        } catch (SocketTimeoutException e) {
            println "${action} timed out = ${e.toString()}"
            return [error: "${action} timed out", reason: e.message]
        } catch (Exception e) {
            println "${action} failed = ${e.toString()}"
            return [error: "${action} failed", reason: e.message]
        }

        def results = [total: resp.total, groups: resp.groups]
        
        // store in cache
        mdCache.put 'datasets', [resp: results, time: new Date()]

        return results
    }

    /**
     * Uses a cached spatial services lookup to return counts for layers by type.
     *
     * @return map with total and breakdown by type, domain and classification1
     */
    def getSpatialLayers() {
        def action = "Spatial layers lookup"
        // check cache
        def cached = mdCache.layers
        if (cached && cached.resp && !(new Date().after(cached.time + 1))) {
            println "using cache for spatial layers"
            return cached.resp
        }

        println "looking up spatial layers"
        // look it up
        def resp = null
        def url = ConfigurationHolder.config.spatial.baseURL +
                "/layers.json"

        def conn = new URL(url).openConnection()
        try {
            conn.setConnectTimeout(5000)
            conn.setReadTimeout(50000)
            def json = conn.content.text
            resp = JSON.parse(json)
        } catch (SocketTimeoutException e) {
            println "${action} timed out = ${e.toString()}"
            return [error: "${action} timed out", reason: e.message]
        } catch (Exception e) {
            println "${action} failed = ${e.toString()}"
            return [error: "${action} failed", reason: e.message]
        }

        def environmental = resp.count { it.type == 'Environmental'}
        def contextual = resp.count { it.type == 'Contextual'}
        def terrestrial = resp.count { it.domain == 'Terrestrial'}
        def marine = resp.count { it.domain == 'Marine' || it.classification1 == 'Marine'}

        def results = [total: resp.size(), groups: [
                environmental: environmental,
                contextual: contextual,
                terrestrial: terrestrial,
                marine: marine
                ],
                classification: resp.countBy {it.classification1}]
        
        // store in cache
        mdCache.put 'layers', [resp: results, time: new Date()]

        return results
    }

    /**
     * Performs a lookup on the biocache for the specified facet against all records.
     *
     * @param facetName the facet values to return
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def cachedBiocacheFacetCount(facetName) {
        return cachedBiocacheFacetCount(facetName, "*:*")
    }

    /**
     * Performs a lookup on the biocache using cached values if available.
     *
     * @param facetName the facet values to return
     * @param query selects the results set to facet
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def cachedBiocacheFacetCount(facetName, query) {
        // build cache name from facet and query
        def cacheName = facetName + query
        // check cache
        def cached = mdCache[cacheName]
        if (cached && cached.resp && !(new Date().after(cached.time + 1))) {
            println "using cache for ${facetName}"
            return cached.resp
        }
        def results = biocacheFacetCount(facetName, query)

        // store in cache - if there was no error
        if (!results.error) {
            mdCache.put cacheName, [resp: results, time: new Date()]
        }

        return results
    }

    /**
     * Performs a lookup on the biocache for the specified facet and query.
     *
     * @param facetName the facet values to return
     * @param query selects the results set to facet
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def biocacheFacetCount(facetName, query) {
        println "looking up " + facetName
        def facets = []
        def resp = null
        def url = ConfigurationHolder.config.biocache.baseURL +
                "/ws/occurrences/search?q=${query}&pageSize=0&facets=${facetName}"

        def conn = new URL(url).openConnection()
        try {
            conn.setConnectTimeout(5000)
            conn.setReadTimeout(50000)
            def json = conn.content.text
            resp = JSON.parse(json)
        } catch (SocketTimeoutException e) {
            println "Biocache lookup failed = ${e.toString()}"
            return [error: "Biocache lookup failed", reason: e.message]
        } catch (Exception e) {
            println "Biocache lookup failed = ${e.toString()}"
            return [error: "Biocache lookup failed", reason: e.message]
        }

        // hack to workaround biocache bug
        if (facetName == 'decade') { facetName = 'occurrence_year'}

        /*if (facetName == 'type_status') {
            println resp
        }*/
        // handle no results
        if (!resp || !resp.facetResults) { return [error: "Biocache lookup failed", reason: "no data"] }
        resp.facetResults.find({ it.fieldName == facetName})?.fieldResult?.each { facet ->
            facets << [display: humanise(facet.label),
                       facet: facet.label,
                       formattedCount: format(facet.count as int),
                       count: facet.count as int
            ]
        }

        return [error: null, facets: facets]
    }

    /**
     * Bulk lookup of metadata from the bie using a list of guids.
     * @param list of taxon guids
     * @return metadata for each taxon
     */
    def bieBulkLookup(list) {
        def url = ConfigurationHolder.config.bie.baseURL
        def data = webService.doPost(url,
                "ws/species/guids/bulklookup.json", "", (list as JSON).toString())
        //println "returned from doPost ${data.resp}"
        def results = [:]
        if (!data.error) {
            data.resp.searchDTOList.each {taxon ->
                def name = taxon.name ?: taxon.nameComplete
                results.put taxon.guid, [
                        common: taxon.commonNameSingle,
                        name: name,
                        image: [largeImageUrl: taxon.largeImageUrl,
                                smallImageUrl: taxon.smallImageUrl,
                                thumbnailUrl: taxon.thumbnailUrl,
                                imageMetadataUrl: taxon.imageMetadataUrl]]
            }
        }
        //println "returned from bie lookup ${results}"
        return results
    }

    /* -------------------------------- STATIC LOOKUPS --------------------------------------------*/
    def loadStaticCacheFromFile() {
        def json = new File(ConfigurationHolder.config.dashboard.data.file).text
        if (json) {
            staticDataCache = JSON.parse(json)
        }
    }

    def getStaticData(key) {
        def res = staticDataCache[key]
        if (!res) {
            loadStaticCacheFromFile()
            res = staticDataCache[key]
        }
        return res
    }

    def getCollectionsByCategory() {
        return getStaticData('collections')
    }

    def getRecordsByDate() {
        return getStaticData('recordsByDate')
    }

    def getTaxaCounts() {
        return getStaticData('taxaCounts')
    }

    def getBHLCounts() {
        return getStaticData('bhlCounts')
    }

    def getBoldCounts() {
        return getStaticData('boldCounts')
    }

    def getIdentifyLifeCounts() {
        return getStaticData('identifyLife')
    }
    /* ---------------------------- CACHE MANAGEMENT -----------------------------------------*/

    def clearCache() {
        clearCache(null)
    }

    def clearCache(key) {
        if (key == null) {
            staticDataCache = [:]
            mdCache = [:]
            println "cleared all cached data"
        }
        else {
            if (staticDataCache.containsKey(key)) {
                staticDataCache.remove(key)
            }
            if (mdCache.containsKey(key)) {
                mdCache.remove(key)
            }
            println "cleared cached data for " + key
        }
    }

    /* -------------------------------- UTILITIES --------------------------------------------*/
    String format(int i) {
        if (i >= 1000000) {
            return String.format("%6.2f", i/1000000) + 'M'
        }
        return addCommas(i)
    }

    /**
     * Inserts commas into a number for display.
     * @param nStr
     */
    String addCommas(nStr) {
        nStr += ''
        def x = nStr.tokenize('.')
        def x1 = x[0]
        def x2 = x.size() > 1 ? '.' + x[1] : ''
        def rgx = /(\d)(?=(\d{3})+$)/
        x1 = x1.replaceAll(rgx, "\$1,");
        return x1 + x2;
    }

    String humanise(s) {
        def r = ''
        def l = s.size() - 1
        s.eachWithIndex { c, i ->
            // first or last
            if (i == 0 || i == l) {
                r +=  c
            }
            // upper preceded by lower - xX
            else if (c in 'A'..'Z' && s[i-1] in 'a'..'z') {
                // followed by upper - -xXX
                if (s[i+1] in 'A'..'Z') {
                    r +=  ' ' + c
                }
                // followed by lower or number - xXx
                else {
                    r +=  ' ' + c.toLowerCase()
                }
            }
            else {
                r +=  c
            }
        }
        return r
        //return s.replaceAll(/([A-Z][a-z]+)/, " \$1".toLowerCase())
        /*return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );*/
    }
}
