package au.org.ala.dashboard

import grails.test.spock.IntegrationSpec
import spock.lang.Shared



class MetadataServiceSpec extends IntegrationSpec {

    @Shared def metadataService
    def grailsApplication

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
        results instanceof List
        results.size() > 0
        results[0] instanceof Map
        results[0]['label'] != null
        results[0]['count'] != null
    }

    def "test retrieval of records by life form"() {
        setup:
            def results = metadataService.getRecordsByLifeForm()
        expect:
            results instanceof List
            results.size() > 0
            results[0] instanceof Map
            results[0]['label'] != null
            results[0]['count'] != null
    }

    def "test retrieval of BHL statistics"() {
        setup:

        def stats = metadataService.getBHLCounts()

        expect:

        assert stats.size()  == 3
        assert stats['pages'] != null
        assert stats['titles'] != null
        assert stats['volumes'] != null
    }

    def "test retrieval of BHL fails graciously"() {
        setup:
        grailsApplication.config.bhl.baseURL = "NonExistent"

        when:
        def stats = metadataService.getBHLCounts()

        then:
        assert stats.size() == 0
    }
}
