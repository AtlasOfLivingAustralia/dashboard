package au.org.ala.dashboard

class ChartsController {

    def MetadataService metadataService

    def collections() {
        Map collections = metadataService.getCollectionsByCategory()
        def columns = [['string', 'category'], ['number', 'collections']]
        def data = [
                ['Plants', collections.plants],
                ['Insects', collections.insects],
                ['Other fauna', collections.otherFauna],
                ['Microbes', collections.micro]
        ]

        [columns: columns, data: data]
    }

    def stateAndTerritoryRecords() {
        List stateAndTerritoryRecords = metadataService.getStateAndTerritoryRecords()
        def columns = [['string', 'state'], ['number', 'records']]
        def data = []
        stateAndTerritoryRecords.each { record ->
            data << [record.label, record.count]
        }

        [columns: columns, data: data]
    }
}
