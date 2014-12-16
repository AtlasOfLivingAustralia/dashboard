grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolver = "maven"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        mavenLocal()
        mavenRepo ("http://nexus.ala.org.au/content/groups/public/") {
            updatePolicy 'always'
        }
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        // runtime 'mysql:mysql-connector-java:5.1.16'
        runtime 'org.apache.ant:ant:1.7.1'    //you can also use runtime
        runtime 'org.apache.ant:ant-launcher:1.7.1'
        compile 'net.sf.opencsv:opencsv:2.3'
    }

    plugins {
        build ':tomcat:7.0.54'
        build ":release:3.0.1"

        compile ':scaffolding:2.0.3'
        compile ":google-visualization:1.0-SNAPSHOT"
        compile ":font-awesome-resources:4.2.0.0"

        runtime ':resources:1.2.8'
        runtime ":ala-charts:0.2.3"
        runtime (":ala-web-theme:0.8.4") {
            exclude "servlet-api"
            exclude "svn"
            exclude "cache"
            exclude "cache-ehcache"
        }
        runtime ":jquery:1.11.1"

    }
}
