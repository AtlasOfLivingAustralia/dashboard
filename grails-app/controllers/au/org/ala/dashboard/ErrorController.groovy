package au.org.ala.dashboard

class ErrorController {

    def index() {
        if (request.exceptions && request.exception.fileName ==~ ".*/dashboard/.*.gsp") {
            render view: 'na'
        } else {
            render view: 'error'
        }
    }
}
