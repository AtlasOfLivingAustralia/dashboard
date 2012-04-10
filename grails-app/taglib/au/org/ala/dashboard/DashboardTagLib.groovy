package au.org.ala.dashboard

class DashboardTagLib {

    static namespace = 'db'
    def metadataService
    
    def formatNumber = { attrs ->
        if (attrs.value) {
            out << metadataService.format(attrs.value as int)
        }
    }
}
