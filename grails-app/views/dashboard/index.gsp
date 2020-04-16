<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<%@page expressionCodec="none" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="ala-main"/>
    <meta name="breadcrumb" content="Dashboard"/>
    <title>Dashboard | ${grailsApplication.config.skin.orgNameLong}</title>
    <link href="${grailsApplication.config.skin?.favicon?:'https://www.ala.org.au/wp-content/themes/ala2011/images/favicon.ico'}" rel="shortcut icon" type="image/x-icon"/>
    <gvisualization:apiImport/>
    <asset:stylesheet src="dashboard.css" />
</head>

<div class="dashboard">
    <div id="pageHeader" class="row">
        <div id="pageHeaderLeft" class="col-sm-6 col-md-6">
            <ul class="breadcrumb hide">
                <li><a href="${grailsApplication.config.ala.baseURL ?: 'https://www.ala.org.au'}">Home</a></li>
                <li class="active">
                    Dashboard <i id="show-error-button" data-html="true" data-title="Panel errors" data-trigger="hover" data-content="" class="fa fa-times-circle fa-lg initiallyHidden link"></i>
                </li>
            </ul>
            <p><i class="fa fa-exclamation-circle fa-lg"></i> You can rearrange topics by clicking on the panel headers and dragging them.</p>
        </div>
        <div id="buttonGroup" class="pull-right col-sm-6 col-md-6">
            <div id="headerButtons">
                <a class="btn btn-primary " id="resetLayout"><i class="fa fa-refresh fa-inverse"></i> Reset layout</a>
                <a class="btn btn-primary " id="downloadCsv"><i class="fa fa-download fa-inverse"></i> Download as CSV</a>
                <a class="btn btn-primary " id="showJson"><i class="fa fa-code fa-inverse"></i> Show raw data</a>
            </div>
        </div>
    </div>

    <div id="floatContainer">
        <g:if test="${grailsApplication.config.recordsPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="recordsPanel"/>
        </g:if>


        <g:if test="${grailsApplication.config.datasetsPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="datasetsPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.basisRecordsPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="basisRecordsPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.collectionPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="collectionPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.dateRecordsPanel.enabled == 'true'}">
             <g:include controller="dashboard" action="dateRecordsPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.nslPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="nslPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.spatialPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="spatialPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.statePanel.enabled == 'true'}">
            <g:include controller="dashboard" action="statePanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.identifyLifePanel.enabled == 'true'}">
            <g:include controller="dashboard" action="identifyLifePanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.mostRecordedSpeciesPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="mostRecordedSpeciesPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.typeSpecimensPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="typeSpecimensPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.barcodeOfLifePanel.enabled == 'true'}">
            <g:include controller="dashboard" action="barcodeOfLifePanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.bhlPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="bhlPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.volunteerPortalPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="volunteerPortalPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.conservationStatusPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="conservationStatusPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.recordsByDataProviderPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="recordsByDataProviderPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.recordsByInstitutionPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="recordsByInstitutionPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.occurrenceTreePanel.enabled == 'true'}">
            <g:include controller="dashboard" action="occurrenceTreePanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.recordsByLifeFormPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="recordsByLifeFormPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.recordsAndSpeciesByDecadePanel.enabled == 'true'}">
            <g:include controller="dashboard" action="recordsAndSpeciesByDecadePanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.usageStatisticsPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="usageStatisticsPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.downloadsByReasonPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="downloadsByReasonPanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.downloadsBySourcePanel.enabled == 'true'}">
            <g:include controller="dashboard" action="downloadsBySourcePanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.downloadsByUserTypePanel.enabled == 'true'}">
            <g:include controller="dashboard" action="downloadsByUserTypePanel"/>
        </g:if>

        <g:if test="${grailsApplication.config.speciesImagesPanel.enabled == 'true'}">
            <g:include controller="dashboard" action="speciesImagesPanel"/>
        </g:if>

    </div>
</div>

<asset:javascript src="application.js"/>

<asset:script type="text/javascript">
    var alaWsUrls = {
        collections: '${grailsApplication.config.collectory.baseURL}',
        biocache: '${grailsApplication.config.biocache.baseURL}',
        biocacheUI: '${grailsApplication.config.biocache.webappURL}',
        bie: '${grailsApplication.config.bie.baseURL}',
        bieUI: '${grailsApplication.config.bie.webappURL}',
        app: '${request.contextPath}'
    }

    <g:applyCodec encodeAs="none">
    var panelInfo = ${panelInfo?:'{}'};
    </g:applyCodec>

    $(function() {
        dashboard.init({
            urls: alaWsUrls
        });

    $('#floatContainer > div > div.panel').matchHeight();
    $.fn.matchHeight._maintainScroll = true;
});
</asset:script>
</body>
</html>
