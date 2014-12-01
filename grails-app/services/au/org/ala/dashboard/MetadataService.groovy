package au.org.ala.dashboard

import grails.converters.JSON
import groovy.json.JsonSlurper
import org.apache.commons.lang.StringUtils

import javax.annotation.PostConstruct
import java.text.SimpleDateFormat

class MetadataService {

    def webService, cacheService, grailsApplication

    String BIO_CACHE_URL

    @PostConstruct
    def init() {
        BIO_CACHE_URL = grailsApplication.config.biocache.baseURL
    }
/**
     * Populates the model for the dashboard view
     * @return
     */
    Map getDashboardModel() {
        [basisOfRecord          : getBasisOfRecord(),
         mostRecorded           : getMostRecordedSpecies('all'),
         totalRecords           : getTotalAndDuplicates(),
//         collections            : getCollectionsByCategory(),
         collections            : null,
         datasets               : getDatasets(),
         dataProviders          : getDataProviders(),
         institutions           : getInstitutions(),
         taxaCounts             : getTaxaCounts(),
         identifyLifeCounts     : getIdentifyLifeCounts(),
         bhlCounts              : getBHLCounts(),
         boldCounts             : getBoldCounts(),
         typeCounts             : getTypeStats(),
         dateStats              : getDateStats(),
         volunteerPortalCounts  : getVolunteerStats(),
         spatialLayers          : getSpatialLayers(),
         stateConservation      : getSpeciesByConservationStatus(),
         loggerTotals           : getLoggerTotals(),
         loggerReasonBreakdown  : getLoggerReasonBreakdown(),
         loggerEmailBreakdown   : getLoggerEmailBreakdown(),
         loggerTemporalBreakdown: getLoggerReasonTemporalBreakdown(),
         imagesBreakdown        : getImagesBreakdown(),
         panelInfo              : getPanelInfo() as JSON
        ]
    }

    /**
     * Uses a cached biocache lookup to return the total number of occurrence records and the number of
     * suspected duplicates.
     * @return map with counts and any errors - [error: <errors>, total: <count>, duplicates: <count>]
     */
    def getTotalAndDuplicates() {
        return cacheService.get('duplicate_status', {
            def raw = biocacheFacetCount('duplicate_status')
            [error: raw.error, total: raw.total, duplicates: raw.facets.find({ it.facet == 'D' }).count]
        })
    }

    /**
     * Uses a cached biocache lookup to return the counts for each basis of record.
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def getBasisOfRecord() {
        return cacheService.get('basis_of_record', {
            biocacheFacetCount('basis_of_record')
        })
    }

    /**
     * Uses a cached biocache lookup to return the counts for the specified facet.
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def getBiocacheFacet(facetName) {
        return cacheService.get(facetName, {
            biocacheFacetCount(facetName)
        })
    }

    /**
     * Get cached data for the 5 most recorded species for the specified group.
     *
     * @param group the lifeform group or text containing 'all'
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def getMostRecordedSpecies(group) {
        cacheService.get('mostRecorded' + group, {
            // get the guids for the 6 most recorded species
            def fq = (group.toLowerCase() =~ 'all' ? '' : "&fq=species_group:${group}")
            def results = biocacheFacetCount('species_guid', "*:*${fq}&flimit=6")

            // look up the name and common names
            def guids = results.facets.collect { it.facet }
            def md = bieBulkLookup(guids)
            results.facets.each {
                def data = md[it.facet]
                if (data) {
                    it.name = data.name
                    it.common = data.common
                }
            }

            return results
        })
    }

    def getVolunteerStats() {
        cacheService.get('volunteerStats', {
            // earliest record
            webService.getJson("http://volunteer.ala.org.au/ws/stats")
        })
    }

    /**
     * Get cached data for the oldest and newest records and counts of
     * records by century.
     * @return map
     */
    def getDateStats() {
        cacheService.get('dateStats', {
            def results = [:]

            // earliest record
            def a = webService.getJson(grailsApplication.config.biocache.baseURL +
                    "/ws/occurrences/search?q=!assertions:invalidCollectionDate&pageSize=1&sort=occurrence_date&facet=off")
            def earliestUuid = a?.occurrences[0]?.uuid
            if (a?.occurrences[0]?.eventDate) {
                def earliest = new Date(a?.occurrences[0]?.eventDate)
                def earliestDate = new SimpleDateFormat("d MMMM yyyy").format(earliest)
                results.earliest = [uuid: earliestUuid, display: earliestDate]
            } else {
                log.error("Earliest date is invalid. See record: " + a?.occurrences[0]?.uuid)
            }

            // latest record
            def b = webService.getJson(grailsApplication.config.biocache.baseURL +
                    "/ws/occurrences/search?q=!assertions:invalidCollectionDate&pageSize=1&sort=occurrence_date&dir=desc&facet=off")
            def latestUuid = b.occurrences[0].uuid
            def latest = new Date(b.occurrences[0].eventDate)
            def latestDate = new SimpleDateFormat("d MMMM yyyy").format(latest)
            results.latest = [uuid: latestUuid, display: latestDate]

            // latest record with image
            def bi = webService.getJson(grailsApplication.config.biocache.baseURL +
                    "/ws/occurrences/search?q=!assertions:invalidCollectionDate%20AND%20occurrence_date:%5B*%20TO%20*%5D&pageSize=1&sort=first_loaded_date&dir=desc&facet=off&fq=multimedia:Image")
            def latestImageUuid = bi.occurrences[0].uuid
            def latestImage = new Date(bi.occurrences[0].eventDate)
            def latestImageDate = new SimpleDateFormat("d MMMM yyyy").format(latestImage)
            results.latestImage = [uuid: latestImageUuid, display: latestImageDate]

            // get counts by century
            [1600, 1700, 1800, 1900, 2000].each {
                def url = grailsApplication.config.biocache.baseURL +
                        "/ws/occurrences/search?q=*:*&pageSize=0&facet=off&fq=occurrence_year:[${it}-01-01T00:00:00Z%20TO%20${it + 99}-12-31T23:59:59Z]"
                def c = webService.getJson(url)
                results['c' + it] = c.totalRecords
            }

            return results
        })
    }

    /**
     * Get cached counts for various type statuses.
     * @return map
     */
    def getTypeStats = {
        return cacheService.get('typeStats', {

            def results = [:]

            // type counts
            def facets = biocacheFacetCount('type_status', '*:*')
            facets.facets.each {
                if (it.facet != 'notatype') {
                    results[it.facet] = it.count
                }
            }
            results.total = results.values().sum { it }

            // type counts with images
            results.withImage = [:]
            facets = biocacheFacetCount('type_status', "*:*&fq=multimedia:Image")
            facets.facets.each {
                if (it.facet != 'notatype') {
                    results.withImage[it.facet] = it.count
                }
            }
            results.withImage.total = results.withImage.values().sum { it }

            return results
        })
    }

    /**
     * Uses multiple biocache searches to return unique species accepted names for each decade.
     * @return a list of maps with the following format [decade: <value>, records: <value>, species: <value>]
     */
    List getSpeciesByDecade() {
        return cacheService.get("speciesByDecade", {

            def baseUrl =
                    "http://biocache.ala.org.au/ws/explore/groups.json?q=*:*&pageSize=10&fq=occurrence_year:"
            //[1750-01-01T00:00:00Z+TO+1760-12-31T23:59:59Z]

            def data = []
            (184..201).each {
                def from, decade
                if (it == 184) {
                    from = '*'
                    decade = 'before 1850'
                } else {
                    from = (it.toString() + '0-01-01T00:00:00Z')
                    decade = it.toString() + '0s'
                }
                def to = (it.toString() + '9-12-31T23:59:59Z')
                def url = baseUrl + '[' + from + '+TO+' + to + ']'
                def json = new URL(url).text
                def result = JSON.parse(json)
                def totals = result.find { it.name == 'ALL_SPECIES' }
                data << [decade: decade, records: totals.count, species: totals.speciesCount]
            }

            return data
        })
    }

    /**
     * Uses a cached collectory lookup to return counts for datasets by type.
     * @return map with total and breakdown by type
     */
    def getDatasets() {
        return cacheService.get('datasets', {
            def action = "Datasets lookup"
            // look it up
            log.info "looking up datasets"
            def resp = null
            def url = grailsApplication.config.collectory.baseURL +
                    "/ws/dataResource/count/resourceType?public=true"

            def conn = new URL(url).openConnection()
            try {
                conn.setConnectTimeout(5000)
                conn.setReadTimeout(50000)
                def json = conn.content.text
                resp = JSON.parse(json)
            } catch (SocketTimeoutException e) {
                log.info "${action} timed out = ${e.toString()}"
                return [error: "${action} timed out", reason: e.message]
            } catch (Exception e) {
                log.info "${action} failed = ${e.toString()}"
                return [error: "${action} failed", reason: e.message]
            }

            // look up most recently added
            def json = new URL("http://collections.ala.org.au/ws/dataResource").text
            def allDRs = new JsonSlurper().parseText(json)
            allDRs.sort { it.uid[2..-1].toInteger() }
            def last = allDRs.last()

            def results = [total: resp.total, groups: resp.groups, last: last]

            return results
        })
    }

    /**
     * Uses a cached spatial services lookup to return counts for layers by type.
     * @return map with total and breakdown by type, domain and classification1
     */
    def getSpatialLayers() {
        return cacheService.get('layers', {
            def action = "Spatial layers lookup"
            log.info "looking up spatial layers"
            // look it up
            def resp = null
            def url = grailsApplication.config.spatial.baseURL +
                    "/layers.json"

            def conn = new URL(url).openConnection()
            try {
                conn.setConnectTimeout(5000)
                conn.setReadTimeout(50000)
                def json = conn.content.text
                resp = JSON.parse(json)
            } catch (SocketTimeoutException e) {
                log.info "${action} timed out = ${e.toString()}"
                return [error: "${action} timed out", reason: e.message]
            } catch (Exception e) {
                log.info "${action} failed = ${e.toString()}"
                return [error: "${action} failed", reason: e.message]
            }

            def environmental = resp.count { it.type == 'Environmental' }
            def contextual = resp.count { it.type == 'Contextual' }
            def terrestrial = resp.count { it.domain == 'Terrestrial' }
            def marine = resp.count { it.domain == 'Marine' || it.classification1 == 'Marine' }

            def results = [total         : resp.size(), groups: [
                    environmental: environmental,
                    contextual   : contextual,
                    terrestrial  : terrestrial,
                    marine       : marine
            ],
                           classification: resp.countBy { it.classification1 }]

            return results
        })
    }

    def getSpeciesByConservationStatus() {
        return cacheService.get('speciesByConservationStatus', {

            def baseUrl = grailsApplication.config.biocache.baseURL +
                    "/ws/explore/groups.json?pageSize=10&q=state_conservation:"

            def data = []
            ['Endangered', 'Near Threatened', 'Least Concern/Unknown', 'Listed under FFG Act', 'Extinct', 'Parent Species (Unofficial)'].each {
                def url = baseUrl + '"' + URLEncoder.encode(it) + '"'
                def json = new URL(url).text
                def result = JSON.parse(json)
                def totals = result.find { it.name == 'ALL_SPECIES' }
                data << [status: it, records: totals.count, species: totals.speciesCount]
            }
            return data
        })
    }

    def getDataProviders() {
        return cacheService.get('dataProviders', {

            // raw list by uid
            def results = getBiocacheFacet('data_provider_uid')?.facets

            // get metadata for name and acronym
            def dpMetadata = JSON.parse(new URL(grailsApplication.config.collectory.baseURL + "/lookup/dataProvider").text)
            def dpMap = [:]
            dpMetadata.each() {
                dpMap.put it.uid, [acronym: it.acronym, name: it.name, uri: it.uri]
            }

            // inject additional metadata
            results.each {
                def md = dpMap[it.facet]
                if (md) {
                    it.name = md.name
                    it.acronym = md.acronym
                    it.uri = grailsApplication.config.collectory.baseURL + '/public/show/' + it.facet
                    it.display = md.acronym ?: md.name
                }
            }

            return results
        })
    }

    def getInstitutions() {
        return cacheService.get('institutions', {

            // raw list by uid
            def results = getBiocacheFacet('institution_uid')?.facets

            // get metadata for name and acronym
            def dpMetadata = JSON.parse(new URL(grailsApplication.config.collectory.baseURL + "/lookup/institution").text)
            def dpMap = [:]
            dpMetadata.each() {
                dpMap.put it.uid, [acronym: it.acronym, name: it.name]
            }

            // inject additional metadata
            results.each {
                def md = dpMap[it.facet]
                if (md) {
                    it.name = md.name
                    it.acronym = md.acronym
                    it.uri = grailsApplication.config.collectory.baseURL + '/public/show/' + it.facet
                    it.display = (md.name.size() > 30 && md.acronym) ? md.acronym : md.name
                }
            }

            return results
        })
    }

    /**
     * Performs a lookup on the biocache for the specified facet and query.
     *
     * @param facetName the facet values to return
     * @param query selects the results set to facet
     * @return map with facets and any errors - [error: <errors>, reason: <reason if error>, facets: <facet values>]
     */
    def biocacheFacetCount(facetName, query = '*:*') {

        def facets = []
        def resp = null
        def url = grailsApplication.config.biocache.baseURL +
                "ws/occurrences/search?q=${query}&pageSize=0&fsort=count&facets=${facetName}"

        log.info "looking up " + facetName + ", URL: " + url

        def conn = new URL(url).openConnection()
        try {
            conn.setConnectTimeout(5000)
            conn.setReadTimeout(50000)
            def json = conn.content.text
            resp = JSON.parse(json)
        } catch (SocketTimeoutException e) {
            log.error "Biocache lookup failed = ${e.toString()}"
            return [error: "Biocache lookup failed", reason: e.message]
        } catch (Exception e) {
            log.error "Biocache lookup failed = ${e.toString()}"
            return [error: "Biocache lookup failed", reason: e.message]
        }

        // hack to workaround biocache bug
        if (facetName == 'decade') {
            facetName = 'occurrence_year'
        }

        /*if (facetName == 'type_status') {
            println resp
        }*/
        // handle no results
        if (!resp || !resp.facetResults) {
            return [error: "Biocache lookup failed", reason: "no data"]
        }
        resp.facetResults.find({ it.fieldName == facetName })?.fieldResult?.each { facet ->
            facets << [display       : humanise(facet.label),
                       facet         : facet.label,
                       formattedCount: format(facet.count as long),
                       count         : facet.count as long
            ]
        }

        return [error: null, facets: facets, total: resp.totalRecords]
    }

    /**
     * Bulk lookup of metadata from the bie using a list of guids.
     * @param list of taxon guids
     * @return metadata for each taxon
     */
    def bieBulkLookup(list) {
        def url = grailsApplication.config.bie.baseURL
        def data = webService.doPost(url,
                "ws/species/guids/bulklookup.json", "", (list as JSON).toString())
        //println "returned from doPost ${data.resp}"
        def results = [:]
        if (!data.error) {
            data.resp.searchDTOList.each { taxon ->
                def name = taxon.name ?: taxon.nameComplete
                results.put taxon.guid, [
                        common: taxon.commonNameSingle,
                        name  : name,
                        image : [largeImageUrl   : taxon.largeImageUrl,
                                 smallImageUrl   : taxon.smallImageUrl,
                                 thumbnailUrl    : taxon.thumbnailUrl,
                                 imageMetadataUrl: taxon.imageMetadataUrl]]
            }
        }
        //println "returned from bie lookup ${results}"
        return results
    }

    /**
     * Get cached data for the
     * @return map
     */
    def getLoggerTotals() {
        cacheService.get('loggerTotals', {
            def results = [:]

            // earliest record
            def totals = webService.getJson(grailsApplication.config.logger.baseURL +
                    "/service/totalsByType").totals

            for (k in totals.keys()) {
                def keyMap = totals[k]
                results[k] = ["events": format(keyMap["events"] as long), "records": format(keyMap["records"] as long)]
            }

            return results
        })
    }

    def getLoggerReasonBreakdown() {
        cacheService.get('loggerReasonBreakdown', {
            def results = []

            // this number includes testing - we need to remove this
            def allTimeReasonBreakdown = webService.getJson(grailsApplication.config.logger.baseURL +
                    "/service/reasonBreakdown?eventId=1002").all

            //order by counts
            def sortedBreakdowns = allTimeReasonBreakdown.reasonBreakdown.sort { -it.value["events"] }

            //but then place "Other", "Unclassified", "Testing" at the bottom & combined
            def other = sortedBreakdowns.get("other")

            def unclassifiedCount = sortedBreakdowns.get("unclassified")
            def testingCount = sortedBreakdowns.get("testing")

            if (unclassifiedCount) {
                other["events"] = other["events"] + unclassifiedCount["events"]
                other["records"] = other["records"] + unclassifiedCount["records"]
            }

            //testing events
            def testingEvents = 0
            def testingRecords = 0

            if (testingCount) {
//                other["events"] = other["events"] + testingCount["events"]
//                other["records"] = other["records"] + testingCount["records"]
                testingEvents = testingCount["events"] as long
                testingRecords = testingCount["records"] as long
            }

            sortedBreakdowns.remove("other")
            sortedBreakdowns.remove("unclassified")
            sortedBreakdowns.remove("testing")
            sortedBreakdowns.put("other", other)

            for (k in sortedBreakdowns.keySet()) {
                def keyMap = sortedBreakdowns[k]
                results.add([StringUtils.capitalize(k), format(keyMap["events"] as long), format(keyMap["records"] as long)])
            }

            results.add(["TOTAL",
                         format((allTimeReasonBreakdown.events as long) - testingEvents),
                         format((allTimeReasonBreakdown.records as long) - testingRecords)]
            )

            return results
        })
    }

    def getLoggerReasonTemporalBreakdown() {
        cacheService.get('loggerReasonBreakdown', {
            def results = []
            // earliest record
            def allTimeReasonBreakdown = webService.getJson(grailsApplication.config.logger.baseURL +
                    "/service/reasonBreakdownMonthly?eventId=1002").all
            return allTimeReasonBreakdown
        })
    }

    def getLoggerEmailBreakdown() {
        cacheService.get('loggerEmailBreakdown', {
            def results = [:]

            // earliest record
            def allTimeEmailBreakdown = webService.getJson(grailsApplication.config.logger.baseURL +
                    "/service/emailBreakdown?eventId=1002").all

            ["edu", "gov", "other", "unspecified"].each {
                def keyMap = allTimeEmailBreakdown.emailBreakdown[it]
                results[it] = ["events": format(keyMap["events"] as long), "records": format(keyMap["records"] as long)]
            }

//            results["total"] = ["events" : format(allTimeEmailBreakdown.events as long), "records" : format(allTimeEmailBreakdown.records as long)]

            return results
        })
    }

    def getImagesBreakdown() {
        cacheService.get('imagesBreakdown', {

            def results = [:]

            def bieUrl = grailsApplication.config.bie.baseURL
            //TODO Why is this not used ans why are all the urls harcoded
            //def biocacheUrl = grailsApplication.config.biocache.baseURL

            def taxaWithImages = webService.getJson("http://biocache.ala.org.au/ws/occurrence/facets?facets=taxon_name&pageSize=0&q=multimedia:Image")[0].count

            def speciesWithImages = webService.getJson("http://biocache.ala.org.au/ws/occurrence/facets?q=multimedia:Image%20AND%20(rank:species%20OR%20rank:subspecies)&facets=taxon_name&pageSize=0")[0].count

            def subspeciesWithImages = webService.getJson("http://biocache.ala.org.au/ws/occurrence/facets?q=multimedia:Image%20AND%20rank:subspecies&facets=taxon_name&pageSize=0")[0].count

            results.put("taxaWithImages", taxaWithImages)
            results.put("speciesWithImages", speciesWithImages)
            results.put("subspeciesWithImages", subspeciesWithImages)

            def vpResources = webService.getJson("http://collections.ala.org.au/ws/dataHub/dh6").memberDataResources
//            log.debug "vpResources = ${vpResources}"
            def resourcesQuery = ""

            if (vpResources) {
                resourcesQuery = "data_resource_uid:("
                vpResources.eachWithIndex() { res, i ->
                    if (i > 0) {
                        resourcesQuery = resourcesQuery + "%20OR%20"
                    }
                    resourcesQuery = resourcesQuery + res.uid
                }
                resourcesQuery = resourcesQuery + ")"
            }

            def taxaURL = "http://biocache.ala.org.au/ws/occurrence/facets?facets=taxon_name&pageSize=0&q=multimedia:Image%20AND%20" + resourcesQuery

            def taxaVPCount = webService.getJson(taxaURL)[0].count
            results.put("taxaWithImagesFromVolunteerPortal", taxaVPCount)

            def taxaCSCount = webService.getJson("http://biocache.ala.org.au/ws/occurrence/facets?facets=taxon_name&pageSize=0&q=multimedia:Image%20AND%20provenance:\"Individual%20sightings\"")[0].count
            results.put("taxaWithImagesFromCS", taxaCSCount)

            def imageTotal = webService.getJson("http://images.ala.org.au/ws/getRepositoryStatistics").imageCount
            results.put("imageTotal", imageTotal)

            return results
        })
    }

    /**
     * Provides a List with the number of records for each state record with the following format:
     * @return [[label: <value>, count: <value<], ...]
     */
    List getStateAndTerritoryRecords() {
        def results = webService.getJson("${BIO_CACHE_URL}${Constants.WebServices.PARTIAL_URL_STATE_TERRITORY_FACETED_RESULTS}")
        ((results.facetResults.find {it.fieldName == 'state'}).fieldResult as ArrayList)
    }


    /* -------------------------------- STATIC LOOKUPS --------------------------------------------*/

    def get = { key ->
        return cacheService.get(key, { cacheService.loadStaticCacheFromFile(key) })
    }

    def getCollectionsByCategory() {
        return get('collections')
    }

    def getRecordsByDate() {
        return get('recordsByDate')
    }

    def getTaxaCounts() {
        return get('taxaCounts')
    }

    def getBHLCounts() {
        return get('bhlCounts')
    }

    def getBoldCounts() {
        return get('boldCounts')
    }

    def getIdentifyLifeCounts() {
        return get('identifyLife')
    }

    def getPanelInfo() {
        return get('info')
    }

    /* -------------------------------- UTILITIES --------------------------------------------*/

    String format(i) {
        if (i >= 1000000000) {
            return String.format("%9.2f", i / 1000000000) + 'B'
        } else if (i >= 1000000) {
            return String.format("%6.2f", i / 1000000) + 'M'
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
                r += c
            }
            // upper preceded by lower - xX
            else if (c in 'A'..'Z' && s[i - 1] in 'a'..'z') {
                // followed by upper - -xXX
                if (s[i + 1] in 'A'..'Z') {
                    r += ' ' + c
                }
                // followed by lower or number - xXx
                else {
                    r += ' ' + c.toLowerCase()
                }
            } else {
                r += c
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
