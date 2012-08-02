/*
 * Copyright (C) 2012 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

package au.org.ala.dashboard

import grails.converters.JSON
import au.com.bytecode.opencsv.CSVWriter

class DashboardController {

    def metadataService, cacheService

    /**
     * Show main dashboard page.
     */
    def index = {
        [basisOfRecord: metadataService.getBasisOfRecord(),
         mostRecorded: metadataService.getMostRecordedSpecies('all'),
         totalRecords: metadataService.getTotalAndDuplicates(),
         collections: metadataService.getCollectionsByCategory(),
         datasets: metadataService.getDatasets(),
         dataProviders: metadataService.getDataProviders(),
         institutions: metadataService.getInstitutions(),
         taxaCounts: metadataService.getTaxaCounts(),
         identifyLifeCounts: metadataService.getIdentifyLifeCounts(),
         bhlCounts: metadataService.getBHLCounts(),
         boldCounts: metadataService.getBoldCounts(),
         typeCounts: metadataService.getTypeStats(),
         dateStats: metadataService.getDateStats(),
         volunteerPortalCounts: metadataService.get('volunteerPortalCounts'),
         spatialLayers: metadataService.getSpatialLayers(),
         stateConservation: metadataService.getSpeciesByConservationStatus()]
    }

    def mostRecorded(String group) {
        def facets = metadataService.getMostRecordedSpecies(group)
        render facets as JSON
    }
    
    /**
     * Do logouts through this app so we can invalidate the session.
     *
     * @param casUrl the url for logging out of cas
     * @param appUrl the url to redirect back to after the logout
     */
    def logout = {
        session.invalidate()
        redirect(url:"${params.casUrl}?url=${params.appUrl}")
    }

    def clearCache() {
        if (params.key) {
            cacheService.clear(params.key)
        }
        else {
            cacheService.clear()
        }
        render 'Done.'
    }

    /* ---------------------------- data services ---------------------------------*/

    def decadesAsArray = {
        def results = [['Decade','Records','Species']] +
                metadataService.getSpeciesByDecade().collect {[it.decade, it.records, it.species]}
        render results as JSON
    }

    def downloadAsCsv = {
        /* build files as csv */

        // by decade
        writeCsvFile('by-decade', metadataService.getSpeciesByDecade().collect {
            [it.decade, it.records, it.species] }, ['Decade','Records','Species'])

        // total + dups
        writeCsvFile('total-records', metadataService.getTotalAndDuplicates().findAll({it.key != 'error'}), [])

        // basis of record
        writeCsvFile('basis-of-record', facetCount('basis_of_record'), ['basisOfRecord','number of records'])

        // records by state
        recordsBy('state')

        // records by kingdom
        recordsBy('kingdom')

        recordsBy('state_conservation')

        recordsByOtherName('species_group','lifeform')

        // collections
        writeCsvFile('collections', metadataService.getCollectionsByCategory(), ['category','number of collections'])

        // data providers
        writeCsvFile('data-providers', metadataService.getDataProviders().collectEntries {[it.name, it.count]},
                ['data provider','number of records'])

        // spatial layers
        def md = metadataService.getSpatialLayers()
        def spMap = [Total: md.total] + md.groups + md.classification
        writeCsvFile('spatial-layers', spMap, ['type','number'])

        // datasets
        def ds = metadataService.getDatasets()
        def dsMap = [Total: ds.total] + ds.groups
        writeCsvFile('datasets', dsMap, ['type','number'])

        // records by century
        def rc = metadataService.getDateStats()
        def rcList =
        ['c1600','c1700','c1800','c1900','c2000'].collect { [it, rc[it]] }
        writeCsvFile('records-by-century', rcList, ['century','number'])

        // records for types
        def ty = metadataService.getTypeStats()
        def tyList = ty.collectEntries { k,v ->
            if (k == 'withImage') {
                v.collectEntries { l,w -> [(l + ' (with image)'): w]}
            }
            else {
                ["${k}": v]
            }
        }
        writeCsvFile('type-status', tyList, ['type status', 'number'])

        // taxa counts
        writeCsvFile('names', metadataService.getTaxaCounts(), [])

        // bhl counts
        writeCsvFile('biodiversity-heritage-library', metadataService.getBHLCounts(), [])

        // identify life counts
        writeCsvFile('identify-life', metadataService.getIdentifyLifeCounts(), [])

        // vp counts
        writeCsvFile('biodiversity-volunteer-portal', metadataService.get('volunteerPortalCounts'), [])

        /* zip files */
        new AntBuilder().zip(destfile: '/data/dashboard/zip/dashboard.zip', basedir: '/data/dashboard/csv/',
        includes: '**/*.csv')

        /* render zip */
        response.setHeader("Content-disposition", "attachment; filename=dashboard.zip");
        byte[] zip = new File('/data/dashboard/zip/dashboard.zip').bytes
        response.contentType = "application/zip"
        response.outputStream << zip
    }

    def writeCsvFile(filename, values, header) {
        new File(grailsApplication.config.csv.temp.dir + filename + '.csv').withWriter { out ->
            def csv = new CSVWriter(out/*, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER*/)
            if (header) { csv.writeNext(header as String[]) }
            if (values instanceof Map) {
                values.each { k,v ->
                    csv.writeNext([k,v] as String[])
                }
            }
            else if (values instanceof List) {
                values.each {
                    csv.writeNext(it as String[])
                }
            }
        }
    }

    def recordsByOtherName(String facet, String name) {
        def dashed = name.replaceAll('_','-')
        writeCsvFile('records-by-' + dashed, facetCount(facet), [dashed,'number of records'])
    }

    def recordsBy(String facet) {
        recordsByOtherName(facet, facet)
    }

    def most = { group ->
        def m = [:]
        metadataService.getMostRecordedSpecies(group).facets.each() {
            m.put it.name, [count: it.count, common: it.common, lsid: it.facet]
        }
        m
    }

    def facetCount = { facet ->
        def r = [:]
        metadataService.getBiocacheFacet(facet).facets.each {
            r.put it.display, it.count
        }
        r
    }

    def data() {

        // build output
        def d = [
                totalRecords: metadataService.getTotalAndDuplicates().findAll({it.key != 'error'}),
                basisOfRecord: facetCount('basis_of_record'),
                collections: metadataService.getCollectionsByCategory(),
                datasets: metadataService.getDatasets(),
                recordsByDataProvider:
                        metadataService.getDataProviders().collectEntries {[it.name, it.count]},
                recordsByInstitution:
                        metadataService.getInstitutions().collectEntries {[it.name, it.count]},
                recordsByDate: metadataService.getDateStats(),
                recordsByState: facetCount('state'),
                recordsByKingdom: facetCount('kingdom'),
                recordsByConservationStatus: facetCount('state_conservation'),
                byDecade: metadataService.getSpeciesByDecade(),
                recordsByLifeform: facetCount('species_group'),
                spatialLayers: metadataService.getSpatialLayers(),
                typeCounts: metadataService.getTypeStats(),
                taxaCounts: metadataService.getTaxaCounts(),
                bhlCounts: metadataService.getBHLCounts(),
                volunteerPortalCounts: metadataService.get('volunteerPortalCounts'),
                identifyLifeCounts: metadataService.getIdentifyLifeCounts()]
        ['All','Plants','Mammals','Reptiles','Birds','Animals','Arthropods',
         'Fish','Insects','Amphibians','Bacteria','Fungi'].each {
            d['mostRecorded' + it] = most(it)
        }

        render d as JSON
    }
    
    /* ---------------------------- test actions ---------------------------------*/
    def spatialLayers = {
        render metadataService.getSpatialLayers() as JSON
    }
    def datasets = {
        render metadataService.getDatasets() as JSON
    }

    def metadata = {
        def method = params.id
        def arg = params.arg
        if (arg) {
            render metadataService."$method"(arg) as JSON
        }
        else {
            render metadataService."$method"() as JSON
        }
    }
}
