package au.org.ala.dashboard

import grails.converters.JSON

/**
 * Filter to add JSONP and CORS to JSON responses
 */
class JSONPFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {

            }
            after = { Map model ->

                def ct = response.getContentType()
                //println("content type: "  + ct)
                //println("model: "  + model)
                if(ct?.contains("application/json") && model){
                    response.setHeader('Access-Control-Allow-Origin','*') // CORS header
                    String resp = model as JSON
                    if(params.callback) {
                        resp = params.callback + "(" + resp + ")"
                    }
                    render (contentType: "application/json", text: resp)
                    false
                }
            }
            afterView = { Exception e ->

            }
        }
    }
}
