import au.org.ala.dashboard.MetadataService

import static grails.async.Promises.task


class BootStrap {

    MetadataService metadataService

    def init = { servletContext ->

        log.info "Initializing the Dashboard Metadata Cache asynchronously..."
        def cachePrepopulationTask = task {
            metadataService.getDashboardModel()
        }

        cachePrepopulationTask.onComplete {
            log.info "Dashboard Metadata cache initialized!"
        }

    }

    def destroy = {
    }
}
