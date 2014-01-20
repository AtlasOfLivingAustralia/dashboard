/******************************************************************************\
 *  CONFIG MANAGEMENT
 \******************************************************************************/

def ENV_NAME = "DASHBOARD_CONFIG"
def default_config = "/data/collectory/config/${appName}-config.properties"
if(!grails.config.locations || !(grails.config.locations instanceof List)) {
    grails.config.locations = []
}
if(System.getenv(ENV_NAME) && new File(System.getenv(ENV_NAME)).exists()) {
    println "[DASHBOARD] Including configuration file specified in environment: " + System.getenv(ENV_NAME);
    grails.config.locations = ["file:" + System.getenv(ENV_NAME)]
} else if(System.getProperty(ENV_NAME) && new File(System.getProperty(ENV_NAME)).exists()) {
    println "[DASHBOARD] Including configuration file specified on command line: " + System.getProperty(ENV_NAME);
    grails.config.locations = ["file:" + System.getProperty(ENV_NAME)]
} else if(new File(default_config).exists()) {
    println "[DASHBOARD] Including default configuration file: " + default_config;
    def loc = ["file:" + default_config]
    println ">> loc = " + loc
    grails.config.locations = loc
    println "[DASHBOARD] grails.config.locations = " + grails.config.locations
} else {
    println "[DASHBOARD] No external configuration file defined."
}
println "[DASHBOARD] (*) grails.config.locations = ${grails.config.locations}"

/******************************************************************************\
 *  EXTERNAL SERVERS
\******************************************************************************/
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
/******************************************************************************\
 *  APP CONFIG
 \******************************************************************************/
dashboard.data.file = "/data/dashboard/data.json"
csv.temp.dir = "/data/dashboard/csv/"

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']


// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
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

// enable query caching by default
grails.hibernate.cache.queries = true

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
        //grails.host = "http://woodfired.ala.org.au"
        grails.host = "http://localhost"
        grails.serverURL = "${grails.host}:8083/${appName}"
    }
    test {
        grails.logging.jul.usebridge = false
        grails.host = "ala-testweb1.vm.csiro.au"
        grails.serverURL = "http://${grails.host}:8080/${appName}"
    }
    production {
        grails.logging.jul.usebridge = false
        grails.serverURL = "http://dashboard.ala.org.au"
    }
}

// log4j configuration
// log4j configuration
log4j = {
    appenders {
        environments {
            production {
                rollingFile name: "dashboard-prod",
                    maxFileSize: 104857600,
                    file: "/var/log/tomcat6/dashboard.log",
                    threshold: org.apache.log4j.Level.ERROR,
                    layout: pattern(conversionPattern: "%d [%c{1}]  %m%n")
                rollingFile name: "stacktrace", maxFileSize: 1024, file: "/var/log/tomcat6/dashboard-stacktrace.log"
            }
            development {
                console name: "stdout", layout: pattern(conversionPattern: "%d [%c{1}]  %m%n"), threshold: org.apache.log4j.Level.DEBUG
            }
        }
    }

    root {
        debug  'dashboard-prod'
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
	         'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
	         'org.codehaus.groovy.grails.commons', // core / classloading
	         'org.codehaus.groovy.grails.plugins', // plugins
           'org.springframework.jdbc',
           'org.springframework.transaction',
           'org.codehaus.groovy',
           'org.grails',
           'org.grails.plugin',
           'org.apache',
           'grails.spring',
           'grails.util.GrailsUtil',
           'net.sf.ehcache',
           'grails.app.taglib.org.grails.plugin.resource.ResourceTagLib'

    debug  'ala'
}