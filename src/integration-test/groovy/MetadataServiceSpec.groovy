package au.org.ala.dashboard

//import grails.test.spock.IntegrationSpec
import grails.test.mixin.integration.Integration
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Shared
import grails.test.mixin.TestFor
import spock.lang.Specification

@Integration
@TestFor(MetadataService)
class MetadataServiceSpec extends Specification {

   // @Shared def metadataService
    @Autowired
    MetadataService metadataService
    def grailsApplication
//    def cacheService

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

//        expect:
//        results instanceof List
//        results.size() > 0
//        results[0] instanceof Map
//        results[0]['records'] != null
//        results[0]['species'] != null
    }

    def "test retrieval of records by life form"() {
        setup:
            def results = metadataService.getRecordsByLifeForm()
//        expect:
//            results instanceof List
//            results.size() > 0
//            results[0] instanceof Map
//            results[0]['label'] != null
//            results[0]['count'] != null
    }

    def "test retrieval of BHL statistics"() {
        setup:

        def stats = metadataService.getBHLCounts()

//        expect:
//
//        assert stats.size()  == 3
//        assert stats['pages'] != null
//        assert stats['titles'] != null
//        assert stats['volumes'] != null
    }

    def "test retrieval of BHL from cache"() {
        setup:
        grailsApplication.config.bhl.baseURL = "NonExistent"

//        when:
//        def stats = metadataService.getBHLCounts()
//
//        then:
//        assert stats.size() == 3
    }

}
