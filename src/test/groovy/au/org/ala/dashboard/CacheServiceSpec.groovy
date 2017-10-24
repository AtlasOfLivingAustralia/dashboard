package au.org.ala.dashboard

import grails.test.mixin.TestFor
import groovy.time.TimeCategory
import spock.lang.Specification

@TestFor(CacheService)
class CacheServiceSpec extends Specification{

    def setup() {
        //Initialize cache before each test
        service.cache = [:]
    }

    void "test to check that the cache stores a value and won't change until it expires"() {
        given:
            service.get('mykey', {'value'})

        when:
            def value = service.get('mykey', {'other'})

        then:
            value == 'value'
    }

    void "test to check that the cache will refresh the value of an existing entry when it expires"() {
        given:
            service.get('mykey', {'value'})

        when:
            // we make the cache entry to expire
            use (TimeCategory) {
                //It expires in one day
                CacheService.cache.get('mykey').time = CacheService.cache.get('mykey').time - 1.day - 1.minute
            }


            def value = service.get('mykey', {'other'})
            // The new value is refreshed asynchronously so the first attempt will always retrieve the old value
            assert value == 'value'
            // We wait for the refresher thread to finish
            Thread.sleep(2000)
            // We retrieve the updated value which is current and won't expired for at leas a day
            value = service.get('mykey', {'other2'})

        then:
            value == 'other'
    }

    void "test to see if the cache is cleared if value cannot be obtained"() {
        given:
            service.get('mykey', {'value'})

        when:
            // we make the cache entry to expire
            use (TimeCategory) {
                //It expires in one day
                CacheService.cache.get('mykey').time = CacheService.cache.get('mykey').time - 1.day - 1.minute
            }


            def value = service.get('mykey', {null})

            assert value == null
            // We wait for the refresher thread to finish
            Thread.sleep(2000)
            // We retrieve the updated value which is current and won't expired for at leas a day
            value = service.get('mykey', {null})
        then:
            value == null
    }

    void "test to check that the cache won't be refreshed if there is any exception while getting the new value"() {
        given:
        service.get('mykey', {'value'})

        when:
        // we make the cache entry to expire
        use (TimeCategory) {
            //It expires in one day
            CacheService.cache.get('mykey').time = CacheService.cache.get('mykey').time - 1.day - 1.minute
        }


        def value = service.get('mykey', {throw new Exception("whatever")})
        // The new value is refreshed asynchronously so the first attempt will always retrieve the old value
        assert value == 'value'
        // We wait for the refresher thread to finish
        Thread.sleep(2000)
        // We retrieve the updated value which is current and won't expired for at leas a day
        value = service.get('mykey', {null})
        then:
        // The value has not changed
        value == 'value'
    }
}
