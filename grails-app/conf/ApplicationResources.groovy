// resource bundles
modules = {

    jqueryui {
        dependsOn 'jquery'
        resource url: '/js/jquery-ui-1.8.16.custom.min.js'
    }

    dashboard {
        dependsOn 'charts'
        resource url: '/js/dashboard.js'
        resource url: '/css/dashboard.css', attrs:[media:'all']
    }

    'jquery.cookie' {
        dependsOn 'jquery'
        resource url: '/js/jquery.cookie.js'
    }
}

