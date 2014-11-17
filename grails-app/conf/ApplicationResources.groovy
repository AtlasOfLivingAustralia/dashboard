// resource bundles
modules = {

    'jquery-ui' {
        dependsOn 'jquery'
        resource url: '/js/jquery-ui-1.8.16.custom.min.js'
    }

    'touch-support' {
        resource url: '/js/jquery.ui.touch-punch-0.2.3.min.js'
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

