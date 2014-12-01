package au.org.ala.dashboard

import grails.test.spock.IntegrationSpec
import spock.lang.Shared

class MetadataServiceSpec extends IntegrationSpec {

    @Shared def metadataService

    def "test retrieval of states and territories records"() {
        setup:
            def records = metadataService.getStateAndTerritoryRecords()

        expect:
            records instanceof ArrayList
            records != null && records.size() > 1
    }

    def "test retrieval of records and species by decade"() {
        setup:
            def results = metadataService.getSpeciesByDecade()

        expect:
            results != null
    }
}
