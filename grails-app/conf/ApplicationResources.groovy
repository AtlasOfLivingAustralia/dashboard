// resource bundles
modules = {

    'jquery-migration' {
        dependsOn 'jquery'

        resource url: '/js/jquery-migrate-1.2.1.min.js', disposition: 'head'
    }

    'jquery-ui' {
        dependsOn 'jquery'

        resource url: '/js/jquery-ui-1.11.2.js'
        resource url: '/css/smoothness/jquery-ui-1.11.2.min.css', attrs:[media:'all']
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

    markdown {
        resource url: 'js/markdown.js'
    }
}

