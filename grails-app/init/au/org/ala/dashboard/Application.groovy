package au.org.ala.userdetails

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import groovy.util.logging.Slf4j

@Slf4j
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

}