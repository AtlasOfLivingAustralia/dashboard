package au.org.ala.dashboard

/**
 * Created by rui008 on 26/11/2014.
 */
class Constants {

    class WebServices {

        final static String PARTIAL_URL_STATE_TERRITORY_FACETED_RESULTS = "/ws/occurrences/search.json?pageSize=0&q=*:*&facets=state&flimit=200"

        final static String PARTIAL_URL_RECORDS_BY_LIFE_FORM = "/ws/occurrences/search.json?pageSize=0&q=*:*&facets=species_group&flimit=200"

        final static String PARTIAL_URL_DATE_STATS_EARLIEST_RECORD = "/ws/occurrences/search?q=!assertions:invalidCollectionDate&pageSize=1&sort=occurrence_date&facet=off"

        final static String PARTIAL_URL_DATE_STATS_LATEST_RECORD = "/ws/occurrences/search?q=!assertions:invalidCollectionDate&pageSize=1&sort=occurrence_date&dir=desc&facet=off"

        final static String PARTIAL_URL_DATE_STATS_LATEST_RECORD_WITH_IMAGE = "/ws/occurrences/search?q=!assertions:invalidCollectionDate%20AND%20occurrence_date:%5B*%20TO%20*%5D&pageSize=1&sort=first_loaded_date&dir=desc&facet=off&fq=multimedia:Image"

        final static String PARTIAL_URL_VOLUNTEER_STATS = "/ws/stats"

        final static String PARTIAL_URL_SPECIES_BY_DECADE = "/ws/explore/groups.json?q=*:*&pageSize=10&fq=occurrence_year:"

        final static String PARTIAL_URL_COUNT_DATASETS_BY_TYPE = "/ws/dataResource/count/resourceType?public=true"

        final static String PARTIAL_URL_SPECIES_BY_CONSERVATION_STATUS= "/ws/explore/groups.json?pageSize=10&q=state_conservation:"

        final static String PARTIAL_URL_LOGGER_TOTALS = "/service/totalsByType"

        final static String PARTIAL_URL_LOGGER_REASON_BREAKDOWN = "/service/reasonBreakdown?eventId=1002"

        final static String PARTIAL_URL_LOGGER_SOURCE_BREAKDOWN = "/service/sourceBreakdown?eventId=1002"

        final static String PARTIAL_URL_LOGGER_REASON_TEMPORAL_BREAKDOWN = "/service/reasonBreakdownMonthly?eventId=1002"

        final static String PARTIAL_URL_LOGGER_EMAIL_BREAKDOWN = "/service/emailBreakdown?eventId=1002"

        final static String PARTIAL_URL_TAXA_WITH_IMAGES = "/ws/occurrence/facets?facets=taxon_name&pageSize=0&q=multimedia:Image"

        final static String PARTIAL_URL_SPECIES_WITH_IMAGES = "/ws/occurrence/facets?q=multimedia:Image%20AND%20(rank:species%20OR%20rank:subspecies)&facets=taxon_name&pageSize=0"

        final static String PARTIAL_URL_SUBSPECIES_WITH_IMAGES = "/ws/occurrence/facets?q=multimedia:Image%20AND%20rank:subspecies&facets=taxon_name&pageSize=0"

        final static String PARTIAL_URL_TAXA_VP_COUNT = "/ws/occurrence/facets?facets=taxon_name&pageSize=0&q=multimedia:Image%20AND%20"

        final static String PARTIAL_URL_TAXA_CS_COUNT = "/ws/occurrence/facets?facets=taxon_name&pageSize=0&q=multimedia:Image%20AND%20provenance:\"Individual%20sightings\""

        final static String PARTIAL_URL_IMAGE_TOTAL = "/ws/getRepositoryStatistics"

    }
}
