package au.org.ala.userdetails

class BootStrap {

    def customObjectMarshallers

    def init = { servletContext ->
        log.info("Running bootstrap queries")

        customObjectMarshallers.register()

        log.info("Done bootstrap queries.")
    }
}