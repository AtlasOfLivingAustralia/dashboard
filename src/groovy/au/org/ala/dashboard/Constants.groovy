package au.org.ala.dashboard

/**
 * Created by rui008 on 26/11/2014.
 */
class Constants {

    class WebServices {

        final static String PARTIAL_URL_STATE_TERRITORY_FACETED_RESULTS = "/ws/occurrences/search.json?pageSize=0&q=*:*&facets=state&flimit=200"

        final static String PARTIAL_URL_RECORDS_BY_LIFE_FORM = "/ws/occurrences/search.json?pageSize=0&q=*:*&facets=species_group&flimit=200"
    }
}
