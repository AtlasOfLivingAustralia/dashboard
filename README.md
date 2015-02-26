### dashboard   [![Build Status](https://travis-ci.org/AtlasOfLivingAustralia/dashboard.svg?branch=master)](https://travis-ci.org/AtlasOfLivingAustralia/dashboard)
dashboard
=========
## Information

Website URL: http://dashboard.ala.org.au

Provides a quick overview of many metrics for the ALA.

## Main technologies used
 - [Grails framework](https://grails.org/)
 - [The Atlas of Living Australia REST Web Services](http://api.ala.org.au/)
 - [Grails google-visualization](http://grails.org/plugin/google-visualization) plugin
 - [Google Charts](https://developers.google.com/chart/)
 - Other libraries include:
  - [Bootstrap](http://getbootstrap.com/)
  - [jQuery](http://jquery.com/)
  - ...

##Changelog
- Version 1.0 (TBA)
  - Integrate new 2015 look&feel
  - Removed ala-web-them plugin dependency
  - Added ala-bootstrap2:2.0 and ala-auth:1.2 plugin dependencies
- Version 0.3.1 (22/01/2015)
  - Added 'cache headers' and 'yuicompressor' dependencies to be used by the Resources plugin.
  - Fixed log4j configuration
- Version 0.3 (15/01/2015)
  - Changes made based on Peter Doherty feedback.
- Version 0.2 (14/01/2015)
  - Complete overhaul of the project to fix bugs and integrate bootstrap2 look and feel
  - Charts now rendered using the [Grails google-visualization](http://grails.org/plugin/google-visualization) plugin
