package au.org.ala.dashboard

class DashboardTagLib {

    static namespace = 'db'
    def metadataService

    /**
     * Formats numbers for humans.
     *
     * @attr value to format
     */
    def formatNumber = { attrs ->
        if (attrs.value) {
            out << metadataService.format(attrs.value as int)
        }
    }

    /**
     * Shortens a string to the specified maximum length.
     *
     * @attr text to shorten
     * @attr size max length
     */
    def shorten = { attrs ->
        def len = attrs.size.toInteger() ?: 40
        if (len && attrs.text.size() > len) {
            out << trimLength(attrs.text, len)
        }
        else {
            out << attrs.text
        }
    }

    /**
     * Trims the passed string to the specified length breaking at word boundaries and adding an ellipsis if trimmed.
     */
    def trimLength = {trimString, stringLength ->

        String concatenateString = "..."
        List separators = [".", " "]

        if (stringLength && (trimString?.length() > stringLength)) {
            trimString = trimString.substring(0, stringLength - concatenateString.length())
            String separator = separators.findAll{trimString.contains(it)}?.min{trimString.lastIndexOf(it)}
            if(separator){
                trimString = trimString.substring(0, trimString.lastIndexOf(separator))
            }
            trimString += concatenateString
        }
        return trimString
    }

}
