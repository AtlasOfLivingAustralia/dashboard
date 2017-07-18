<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<%@page expressionCodec="none" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <meta name="breadcrumb" content="Dashboard"/>
    <title>Dashboard | Atlas of Living Australia</title>
    <gvisualization:apiImport/>
    <r:require modules="dashboard"/>
</head>

<body>

<div class="dashboard">
    <div id="pageHeader" class="row">
        <div id="pageHeaderLeft" class="col-sm-6 col-md-6">
            <ul class="breadcrumb hide">
                <li><a href="http://www.ala.org.au" title="Home">Home</a> </li>
                <li class="active">
                    Dashboard <i id="show-error-button" data-html="true" data-title="Panel errors" data-trigger="hover" data-content="" class="fa fa-times-circle fa-lg initiallyHidden link"></i>
                </li>
            </ul>
            <p><i class="fa fa-exclamation-circle fa-lg"></i> You can rearrange topics by clicking on the panel headers and dragging them.</p>
        </div>
        <div id="buttonGroup" class="pull-right col-sm-6 col-md-6">
            <div>
                <div class="col-sm-3 col-md-3">
                    <a class="btn btn-primary btn-block" id="resetLayout"><i class="fa fa-refresh fa-inverse"></i> Reset layout</a>
                </div>

                <div class="col-sm-3 col-md-3">
                    <a class="btn btn-primary btn-block" id="downloadCsv"><i class="fa fa-download fa-inverse"></i> Download as CSV</a>
                </div>

                <div class="col-sm-3 col-md-3">
                    <a class="btn btn-primary btn-block" id="showJson"><i class="fa fa-code fa-inverse"></i> Show raw data
                    </a>
                </div>
            </div>
        </div>
    </div>

    %{--<span><i class="fa fa-exclamation-circle"></i> HINT -> You can rearrange topics by clicking on the panel headers and dragging them.</span>--}%

    <div id="floatContainer">

        <g:include controller="dashboard" action="recordsPanel"/>

        <g:include controller="dashboard" action="datasetsPanel"/>

        <g:include controller="dashboard" action="basisRecordsPanel"/>

        <g:include controller="dashboard" action="collectionPanel"/>

        <g:include controller="dashboard" action="dateRecordsPanel"/>

        <g:include controller="dashboard" action="nslPanel"/>

        <g:include controller="dashboard" action="spatialPanel"/>

        <g:include controller="dashboard" action="statePanel"/>

        <g:include controller="dashboard" action="identifyLifePanel"/>

        <g:include controller="dashboard" action="mostRecordedSpeciesPanel"/>

        <g:include controller="dashboard" action="typeSpecimensPanel"/>

        <g:include controller="dashboard" action="barcodeOfLifePanel"/>

        <g:include controller="dashboard" action="bhlPanel"/>

        <g:include controller="dashboard" action="volunteerPortalPanel"/>

        <g:include controller="dashboard" action="conservationStatusPanel"/>

        <g:include controller="dashboard" action="recordsByDataProviderPanel"/>

        <g:include controller="dashboard" action="recordsByInstitutionPanel"/>

        <g:include controller="dashboard" action="recordsByLifeFormPanel"/>

        <g:include controller="dashboard" action="recordsAndSpeciesByDecadePanel"/>

        <g:include controller="dashboard" action="occurrenceTreePanel"/>

        %{--<g:include controller="dashboard" action="usageStatisticsPanel"/>--}%

        <g:include controller="dashboard" action="downloadsByReasonPanel"/>

        %{--<g:include controller="dashboard" action="downloadsBySourcePanel"/>--}%

        %{--<g:include controller="dashboard" action="downloadsByUserTypePanel"/>--}%

        <g:include controller="dashboard" action="speciesImagesPanel"/>

    </div>
</div>

<r:script>
    var alaWsUrls = {
        collections: '${grailsApplication.config.collectory.baseURL}',
        biocache: '${grailsApplication.config.biocache.baseURL}',
        bie: '${grailsApplication.config.bie.baseURL}',
        app: '${request.contextPath}'
    }

    <g:applyCodec encodeAs="none">
    var panelInfo = ${panelInfo?:'{}'};
    </g:applyCodec>

    $(function() {
        dashboard.init({
            urls: alaWsUrls
        });

        // Draw charts
        ${g.remoteFunction(controller: 'charts', action: 'stateAndTerritoryRecords', update: 'stateAndTerritoryRecords')}
        ${g.remoteFunction(controller: 'charts', action: 'collections', update: 'collectionsByCategory')}
        ${g.remoteFunction(controller: 'charts', action: 'recordsAndSpeciesByDecade', update: 'recordsAndSpeciesByDecade')}
    });
</r:script>
</body>
</html>