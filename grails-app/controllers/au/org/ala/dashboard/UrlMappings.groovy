class UrlMappings {

	static mappings = {
        
        "/data" (controller: 'dashboard', action: 'data')

        "/clearCache/${key}?" (controller: 'dashboard', action: 'clearCache')

		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller: 'dashboard')
		"500"(controller: 'error')
	}
}
