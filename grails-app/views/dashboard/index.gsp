<%@ page import="grails.converters.JSON" contentType="text/html;charset=UTF-8" %>
<%@page expressionCodec="none" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>Dashboard | Atlas of Living Australia</title>
    <gvisualization:apiImport/>
    <r:require modules="dashboard"/>
</head>

<body>

<div class="dashboard">
    <div id="page-header" class="row-fluid">
        <div class="span6 v-center">
            <div class="page-header-content left">
                <ul class="breadcrumb">
                    <li><a href="http://www.ala.org.au" title="Home">Home</a> <span class="divider"><i class="fa fa-arrow-right fa-inverse"></i></span></li>
                    <li class="active">
                        Dashboard <i id="show-error-button" data-html="true" data-title="Panel errors" data-trigger="hover" data-content="" class="fa fa-times-circle fa-lg initiallyHidden link"></i>
                    </li>
                </ul>
                <span><i class="icon-exclamation-sign icon-white"></i> HINT -> You can rearrange topics by clicking on the panel headers and dragging them.</span>
            </div>
        </div>
        <div class="span6 v-center">
            <div class="page-header-content button-group">
                <div class="span3 offset2">
                    <a class="btn btn-block" id="resetLayout"><i class="fa fa-refresh"></i> Reset layout</a>
                </div>
                <div class="span3">
                    <a class="btn btn-block" id="downloadCsv"><i class="fa fa-download"></i> Download as CSV</a>
                </div>
                <div class="span3">
                    <a class="btn btn-block" id="showJson"><i class="fa fa-code"></i> Show raw data</a>
                </div>
            </div>
        </div>
    </div>

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
    var panelInfo = ${panelInfo};
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