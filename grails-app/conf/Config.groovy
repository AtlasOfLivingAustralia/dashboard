/* Added to add compatibility with Grails 2.3+ */
// Explicitly enable hot-swap reload agent
grails.reload.enabled = true
// groupId is no longer specified in the BuildConfig.groovy file
grails.project.groupId = "au.org.ala"
/* Added to add compatibility with Grails 2.3+ */

def ENV_NAME = "${appName.toUpperCase()}_CONFIG"
default_config = "/data/${appName}/config/${appName}-config.properties"
if(!grails.config.locations || !(grails.config.locations instanceof List)) {
    grails.config.locations = []
}
if(System.getenv(ENV_NAME) && new File(System.getenv(ENV_NAME)).exists()) {
    println "[${appName}] Including configuration file specified in environment: " + System.getenv(ENV_NAME);
    grails.config.locations.add "file:" + System.getenv(ENV_NAME)
} else if(System.getProperty(ENV_NAME) && new File(System.getProperty(ENV_NAME)).exists()) {
    println "[${appName}] Including configuration file specified on command line: " + System.getProperty(ENV_NAME);
    grails.config.locations.add "file:" + System.getProperty(ENV_NAME)
} else if(new File(default_config).exists()) {
    println "[${appName}] Including default configuration file: " + default_config;
    grails.config.locations.add "file:" + default_config
} else {
    println "[${appName}] No external configuration file defined."
}
println "[${appName}] (*) grails.config.locations = ${grails.config.locations}"

/* External Servers */

/******* ALA standard config ************/
if (!bie.baseURL) {
    bie.baseURL = "http://bie.ala.org.au/"
}
if (!bie.searchPath) {
    bie.searchPath = "/search"
}
if (!biocache.baseURL) {
    biocache.baseURL = "http://biocache.ala.org.au/"
}
if (!spatial.baseURL) {
    spatial.baseURL = "http://spatial.ala.org.au/"
}
if (!ala.baseURL) {
    ala.baseURL = "http://www.ala.org.au"
}
if (!collectory.baseURL) {
    collectory.baseURL = "http://collections.ala.org.au"
}

if (!logger.baseURL) {
    logger.baseURL = "http://logger.ala.org.au"
}
//if(!runWithNoExternalConfig){
//runWithNoExternalConfig = true
//}
//if(!serverName){
//    serverName = 'http://lists.ala.org.au'
//}
//if (!collectory.enableSync) {
//    collectory.enableSync = false
//}
//if (!collectory.baseURL) {
//    collectory.baseURL="http://collections.ala.org.au"
//}
//if (!security.cas.uriFilterPattern ) {
//    security.cas.uriFilterPattern = '/speciesList, /speciesList/.*, /admin, /admin/.*, /speciesListItem/listAuth/.*, /editor, /editor/.*'
//}
//if (!security.cas.authenticateOnlyIfLoggedInPattern) {
//    security.cas.authenticateOnlyIfLoggedInPattern = "/speciesListItem/list,/speciesListItem/list/.*"
//}
//if (!security.cas.casServerName) {
//    security.cas.casServerName = 'https://auth.ala.org.au'
//}
//if (!security.cas.uriExclusionFilterPattern) {
//    ssecurity.cas.uriExclusionFilterPattern = '/images.*,/css.*,/js.*,/speciesList/occurrences/.*,/speciesList/fieldGuide/.*,/ws/speciesList'
//}
//if (!security.cas.loginUrl) {
//    security.cas.loginUrl = 'https://auth.ala.org.au/cas/login'
//}
//if (!security.cas.logoutUrl) {
//    security.cas.logoutUrl = 'https://auth.ala.org.au/cas/logout'
//}
//if (!security.cas.casServerUrlPrefix) {
//    security.cas.casServerUrlPrefix = 'https://auth.ala.org.au/cas'
//}
//if (!security.cas.bypass) {
//    security.cas.bypass = false
//}
//if (!downloadLimit) {
//    downloadLimit = "200"
//}
//if (!biocacheService.baseURL) {
//    biocacheService.baseURL = "http://biocache.ala.org.au/ws"
//}
//if (!headerAndFooter.baseURL ) {
//    headerAndFooter.baseURL = "http://www2.ala.org.au/commonui"
//}
//if (!ala.baseURL) {
//    ala.baseURL = "http://www.ala.org.au"
//}
//if (!bie.baseURL) {
//    bie.baseURL = "http://bie.ala.org.au"
//}
//if (!bieService.baseURL) {
//    bieService.baseURL = "http://bie.ala.org.au/ws"
//}
//if (!biocache.baseURL) {
//    biocache.baseURL = "http://biocache.ala.org.au"
//}
//if (!fieldGuide.baseURL) {
//    fieldGuide.baseURL = "http://fieldguide.ala.org.au"
//}
//if (!bie.searchPath) {
//    bie.searchPath = "/search"
//}
//if (!bie.download) {
//    bie.download = "/data/bie-staging/species-list"
//}
//if (!bie.nameIndexLocation) {
//    bie.nameIndexLocation = "/data/lucene/namematching_v13"
//}
if (!skin.fluidLayout) {
    skin.fluidLayout = true
}
updateUserDetailsOnStartup = false
/******* End of ALA standard config ************/
/******************************************************************************\
 *  APP CONFIG
 \******************************************************************************/
dashboard.data.file = "/data/dashboard/data.json"
csv.temp.dir = "/data/dashboard/csv/"

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [ // the first one is the default format
    all:           '*/*', // 'all' maps to '*' or the first available format in withFormat
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    hal:           ['application/hal+json','application/hal+xml'],
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*', '/fonts/*']
grails.resources.adhoc.includes = ['/images/**', '/css/**', '/js/**', '/plugins/**', '/fonts/**']

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        // filteringCodecForContentType.'text/html' = 'html'
    }
}


grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

logging.dir = (System.getProperty('catalina.base') ? System.getProperty('catalina.base') + '/logs' : '/var/log/tomcat6')
// log4j configuration
log4j = {
// Example of changing the log pattern for the default console
// appender:
    appenders {
        environments {
            production {
                rollingFile name: "tomcatLog", maxFileSize: 102400000, file: logging.dir + "/specieslist.log", threshold: org.apache.log4j.Level.ERROR, layout: pattern(conversionPattern: "%d %-5p [%c{1}] %m%n")
                'null' name: "stacktrace"
            }
            development {
                console name: "stdout", layout: pattern(conversionPattern: "%d %-5p [%c{1}] %m%n"), threshold: org.apache.log4j.Level.DEBUG
            }
            test {
                rollingFile name: "tomcatLog", maxFileSize: 102400000, file: "/tmp/specieslist-test.log", threshold: org.apache.log4j.Level.DEBUG, layout: pattern(conversionPattern: "%d %-5p [%c{1}] %m%n")
                'null' name: "stacktrace"
            }
        }
    }
    root {
// change the root logger to my tomcatLog file
        error 'tomcatLog'
        warn 'tomcatLog'
        additivity = true
    }
    error 'org.codehaus.groovy.grails.web.servlet', // controllers
            'org.codehaus.groovy.grails.web.pages', // GSP
            'org.codehaus.groovy.grails.web.sitemesh', // layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate',
            'org.codehaus.groovy.grails.plugins.orm.auditable',
            'org.mortbay.log', 'org.springframework.webflow',
            'grails.app',
            'org.apache',
            'org',
            'com',
            'au',
            'grails.app',
            'net',
            'grails.util.GrailsUtil',
            'grails.app.service.org.grails.plugin.resource',
            'grails.app.service.org.grails.plugin.resource.ResourceTagLib',
            'grails.app',
            'grails.plugin.springcache',
            'au.org.ala.cas.client',
            'grails.spring.BeanBuilder',
            'grails.plugin.webxml'
    info 'grails.app',
            'au.org.ala.specieslist'
    debug 'grails.app',
            'grails.app.domain',
            'grails.app.controller',
            'grails.app.service',
            'grails.app.tagLib',
            'au.org.ala.specieslist'
}
