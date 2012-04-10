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

class DashboardController {

    def metadataService

    /**
     * Show main dashboard page.
     */
    def index = {
        [basisOfRecord: metadataService.getBasisOfRecord(),
         mostRecorded: metadataService.getMostRecordedSpecies('all'),
         collections: metadataService.getCollectionsByCategory(),
         datasets: metadataService.getDatasets(),
         taxaCounts: metadataService.getTaxaCounts(),
         identifyLifeCounts: metadataService.getIdentifyLifeCounts(),
         bhlCounts: metadataService.getBHLCounts(),
         boldCounts: metadataService.getBoldCounts(),
         dateStats: metadataService.getDateStats(),
         volunteerPortalCounts: metadataService.getStaticData('volunteerPortalCounts'),
         spatialLayers: metadataService.getSpatialLayers()]
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
        metadataService.clearCache(params.key)
        render 'Done.'
    }

    /* ---------------------------- data service ---------------------------------*/
    
    def most = { group ->
        def m = [:]
        metadataService.getMostRecordedSpecies(group).facets.each() {
            m.put it.name, [count: it.count, common: it.common, lsid: it.facet]
        }
        m
    }

    def facetCount = { facet ->
        def r = [:]
        metadataService.cachedBiocacheFacetCount(facet).facets.each {
            r.put it.display, it.count
        }
        r
    }

    def data() {

        // records by decade
        def decades = [:]
        def dec = [:]
        metadataService.cachedBiocacheFacetCount('decade').facets.each {
            if (it.facet == 'before') {
                decades.put 'before 1850', it.count
            }
            else {
                dec.put it.facet[0..3] + 's', it.count
            }
        }
        decades << dec

        // build output
        def d = [basisOfRecord: facetCount('basis_of_record'),
                collections: metadataService.getCollectionsByCategory(),
                datasets: metadataService.getDatasets(),
                spatialLayers: metadataService.getSpatialLayers(),
                recordsByDate: metadataService.getRecordsByDate(),
                recordsByState: facetCount('state'),
                recordsByKingdom: facetCount('kingdom'),
                recordsByConservationStatus: facetCount('state_conservation'),
                recordsByDecade: decades,
                recordsByLifeform: facetCount('species_group'),
                taxaCounts: metadataService.getTaxaCounts(),
                bhlCounts: metadataService.getBHLCounts(),
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

}
