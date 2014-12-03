package au.org.ala.dashboard

class ErrorController {

    def index() {
        if (request.exception && request.exception.fileName ==~ ".*/dashboard/.*.gsp" && !(request.exception.fileName ==~ ".*/dashboard/index.gsp")) {
            render template: 'panelRenderingError', model: [gspFile: request.exception.className]
        } else {
            render view: '/error'
        }
    }
}
