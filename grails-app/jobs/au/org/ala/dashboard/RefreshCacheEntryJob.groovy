package au.org.ala.dashboard

class RefreshCacheEntryJob {
    static triggers = { }

    def execute(context) {
        try {
            context.cache.put(context.key , [resp: context.source.call(), time: new Date()])
        } catch (e) {
            log.error "There was a problem retrieving the dashboard data for key ${key}: ${e.message}"
        }
    }
}