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
        resource url: '/css/dashboard-ie7.css', attrs:[media:'all'], wrapper: {s -> "<!--[if IE 7]> $s <![endif]-->"}
    }

    'jquery.cookie' {
        dependsOn 'jquery'
        resource url: '/js/jquery.cookie.js'
    }
}

