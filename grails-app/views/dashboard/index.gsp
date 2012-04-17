<%@ page contentType="text/html;charset=UTF-8" import="org.codehaus.groovy.grails.commons.ConfigurationHolder"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="ala3" />
    <title>Data Profile | Atlas of Living Australia</title>
    <link rel="stylesheet" href="${resource(dir:'css',file:'dashboard.css')}"/>
    <link rel="stylesheet" href="${resource(dir:'css/smoothness',file:'jquery-ui-1.8.16.custom.css')}"/>
    <!--[if IE 7]> <link rel="stylesheet" href="${resource(dir:'css',file:'dashboard-ie7.css')}" type="text/css"> <![endif]-->
    <script type="text/javascript" language="javascript" src="http://www.google.com/jsapi"></script>
    <r:require modules="jqueryui, charts, dashboard"/>
</head>
<body class="dashboard fluid">
<div>
    <header id="page-header">
        <div class="inner">
            <nav id="breadcrumb"><ol><li><a href="http://www.ala.org.au">Home</a></li> <li class="last">Dashboard</li></ol></nav>
            <span style="float:left;">HINT: You can rearrange topics by clicking and dragging them.</span>
            <button style="float:right;" type="button" class="link" id="resetLayout">Reset layout</button>
            <button style="float:right;" type="button" class="link" id="showJson">Show raw data</button>
        </div><!--inner-->
    </header>
    <section id="floatContainer">
        <div class='link-group click-thru' tabindex="0" id="datasets-topic">
            <h2><span class="count">${datasets.total}</span>Data sets</h2>
            %{--<g:img class="info-link" dir="images/skin" file="information.png"/>--}%
            <table>
                <tr><td id="website">Harvested websites</td><td><span class="count"><db:formatNumber value="${datasets.groups.website}"/></span></td></tr>
                <tr><td id="records">Occurrence record sets</td><td><span class="count"><db:formatNumber value="${datasets.groups.records}"/></span></td></tr>
                <tr><td id="document">Document sets</td><td><span class="count"><db:formatNumber value="${datasets.groups.document}"/></span></td></tr>
                <tr><td id="uploads">Uploaded record sets</td><td><span class="count"><db:formatNumber value="${datasets.groups.uploads}"/></span></td></tr>
            </table>
            <div id="datasets-info" class="info" style="display: none;">
                <p>Much of the content in the Atlas, such as occurrence records, environmental data, images and the
                conservation status of species, comes from data sets provided by collecting institutions, individual
                collectors and community groups.</p>
                <p>The data sets are listed on the Atlas <a href="http://collections.ala.org.au/datasets/">Datasets page</a>.
                They can be searched and browsed by category.</p>
                <p>This infographic shows the number of datasets for the four major categories.</p>
            </div>
        </div>
        <div class='link-group click-thru' tabindex="1" id="basis-topic">
            <h2>Basis of records</h2>
            <g:if test="${basisOfRecord.error.asBoolean()}"><p class="error" title="${basisOfRecord.reason}">${basisOfRecord.error}</p></g:if>
            <table>
                <g:each in="${basisOfRecord.facets}" var="b">
                    <tr>
                        <td id="br-${b.facet}">${b.display}</td>
                        <td><span class="count">${b.formattedCount}</span></td>
                    </tr>
                </g:each>
            </table>
        </div>
        <div class='link-group' id="collections-topic" tabindex="2">
            <h2><span class="count">${collections.total}</span>Collections</h2>
            <div id="collectionsByCategory"><g:img style="padding: 70px;" dir="images" file="spinner.gif"/> </div>
        </div>
        <div class='link-group click-thru' tabindex="3" id="date-topic">
            <h2>Records by date</h2>
            <table>
                <tr><td id="${dateStats.earliest.uuid}">Earliest record</td><td><span class="count">${dateStats.earliest.display}</span></td>
                <tr><td id="${dateStats.latest.uuid}">Latest record</td><td><span class="count">${dateStats.latest.display}</span></td>
                <tr><td id="1600">1600's</td><td><span class="count"><db:formatNumber value="${dateStats.c1600}"/></span></td>
                <tr><td id="1700">1700's</td><td><span class="count"><db:formatNumber value="${dateStats.c1700}"/></span></td>
                <tr><td id="1800">1800's</td><td><span class="count"><db:formatNumber value="${dateStats.c1800}"/></span></td>
                <tr><td id="1900">1900's</td><td><span class="count"><db:formatNumber value="${dateStats.c1900}"/></span></td>
                <tr><td id="2000">2000's</td><td><span class="count"><db:formatNumber value="${dateStats.c2000}"/></span></td>
            </table>
        </div>
        <div class='link-group' tabindex="4" id="nsl-topic">
            <h2>National Species Lists</h2>
            <table>
                <tbody>
                    <tr><td>Accepted names</td><td><span class="count"><db:formatNumber value="${taxaCounts?.acceptedNames}"/></span></td></tr>
                    <tr><td>Synonyms</td><td><span class="count"><db:formatNumber value="${taxaCounts?.synonymNames}"/></span></td></tr>
                    <tr><td>Species names</td><td><span class="count"><db:formatNumber value="${taxaCounts?.acceptedSpeciesNames}"/></span></td></tr>
                    <tr><td></td><td></td></tr>
                </tbody>
                <tbody>
                    <tr><td style="margin-top:20px;">Species with records</td><td><span class="count"><db:formatNumber value="${taxaCounts?.speciesWithRecords}"/></span></td></tr>
                </tbody>
            </table>
        </div>
        <div class='link-group' tabindex="5" id="spatial-topic">
            <h2><span class="count">${spatialLayers.total}</span>Spatial layers</h2>
            <table>
                <tr><td>Contextual layers</td><td><span class="count">${spatialLayers.groups.contextual}</span></td></tr>
                <tr><td>Environmental/grided layers</td><td><span class="count">${spatialLayers.groups.environmental}</span></td></tr>
            </table>
            <table>
                <tr><td>Terrestrial layers</td><td><span class="count">${spatialLayers.groups.terrestrial}</span></td></tr>
                <tr><td>Marine layers</td><td><span class="count">${spatialLayers.groups.marine}</span></td></tr>
            </table>
            <div id="moreSpatial" style="display:none;">
                <table>
                <g:each in="${spatialLayers.classification}" var="c">
                    <tr><td>${c.key}</td><td><span class="count">${c.value}</span></td></tr>
                </g:each>
                </table>
            </div>
            <p style="padding-top: 10px;"><span id="moreSpatialLink"  class="link">more..</span></p>
        </div>
        <div class='link-group' tabindex="6" id="state-topic">
            <h2>Records by state and territory</h2>
            <div id="stateChart"></div>
        </div>
        <div class='link-group' tabindex="7" id="identifyLife-topic">
            <h2>Identify Life</h2>
            <p><span class="item">Identification keys:</span></p>
            <a target="_blank" href="http://www.identifylife.org/"><g:img dir="images/dashboard" file="identify-life-2.png"/></a>
            <span class="count">${identifyLifeCounts.keys}</span>
        </div>
        <div class='link-group click-thru' tabindex="8" id="most-topic">
            <h2>Most recorded species</h2>
            <div id="mostRecorded">
                <g:if test="${mostRecorded.error.asBoolean()}"><p class="error" title="${basisOfRecord.reason}">${mostRecorded.error}</p></g:if>
                <table>
                    <g:each in="${mostRecorded.facets}" var="m">
                        <tr><td id="${m.facet}"><em>${m.name}</em>
                            <g:if test="${m.common}">- ${m.common}</g:if></td>
                            <td><span class="count">${m.formattedCount}</span> </td>
                        </tr>
                    </g:each>
                </table>
            </div>
            <g:select from="['all lifeforms','Plants','Animals','Birds','Reptiles','Arthropods',
                    'Mammals','Fish','Insects','Amphibians','Bacteria','Fungi']" name="mostSppGroup"/>
            <g:img style="vertical-align:middle;display:none" id="mostLoadingImg" dir="images" file="spinner.gif"/>
        </div>
        <div class='link-group' tabindex="9" id="typeStatus-topic">
            <h2><span class="count"><db:formatNumber value="${typeCounts.total}"/></span>Type specimens</h2>
            <div id="baseTypes">
                <table class="click-thru">
                    <tr><td id="holotype">Holotypes</td><td><span class="count">${typeCounts.holotype}</span></td></tr>
                    <tr><td id="lectotype">Lectotypes</td><td><span class="count">${typeCounts.lectotype}</span></td></tr>
                    <tr><td id="neotype">Neotypes</td><td><span class="count">${typeCounts.neotype}</span></td></tr>
                    <tr><td id="isotype">Isotypes</td><td><span class="count">${typeCounts.isotype}</span></td></tr>
                </table>
                <table>
                    <tr><td>Types with images</td><td><span class="count">${typeCounts.withImage?.total}</span></td></tr>
                </table>
            </div>
            <div id="moreTypes" style="display:none;">
                <table class="click-thru">
                    <g:each in="${typeCounts}" var="c">
                        <g:if test="${!(c.key in ['total','withImage'])}">
                            <tr><td id="${c.key}">${c.key[0].toUpperCase() + c.key[1..-1]}</td><td><span class="count">${c.value}</span></td></tr>
                        </g:if>
                    </g:each>
                </table>
                <table class="click-thru">
                    <g:each in="${typeCounts.withImage}" var="c">
                        <g:if test="${c.key != 'total'}">
                            <tr><td id="${'image'+c.key}">${c.key[0].toUpperCase() + c.key[1..-1] + ' with image'}</td><td><span class="count">${c.value}</span></td></tr>
                        </g:if>
                    </g:each>
                </table>
            </div>
            <p style="padding-top: 10px;"><span id="moreTypesLink"  class="link">more..</span></p>
        </div>
        <div class='link-group' tabindex="10" id="bold-topic">
            <h2>Barcode of life <a target="_blank" href="http://bold.ala.org.au/"><g:img dir="images/dashboard" file="bold.png"/></a></h2>
            <h3>DNA barcode data</h3>
            <table>
                <tbody>
                <tr><td>Records</td><td><span class="count"><db:formatNumber value="${boldCounts?.records}"/></span></td></tr>
                <tr><td>Species</td><td><span class="count"><db:formatNumber value="${boldCounts?.species}"/></span></td></tr>
                <tr><td>Countries</td><td><span class="count"><db:formatNumber value="${boldCounts?.countries}"/></span></td></tr>
                <tr><td>Institutions</td><td><span class="count"><db:formatNumber value="${boldCounts?.institutions}"/></span></td></tr>
                </tbody>
            </table>
        </div>
        <div class='link-group' tabindex="11" id="bhl-topic">
            <h2>Biodiversity heritage library</h2>
            <a target="_blank" href="http://bhl.ala.org.au/"><g:img dir="images/dashboard" file="bhl.png"/></a>
            <table>
                <tbody>
                <tr><td>Pages</td><td><span class="count"><db:formatNumber value="${bhlCounts?.pages}"/></span></td></tr>
                <tr><td>Volumes</td><td><span class="count"><db:formatNumber value="${bhlCounts?.volumes}"/></span></td></tr>
                <tr><td>Titles</td><td><span class="count"><db:formatNumber value="${bhlCounts?.titles}"/></span></td></tr>
                </tbody>
            </table>
        </div>
        <g:if test="${volunteerPortalCounts}">
        <div class='link-group' tabindex="12" id="bvp-topic">
            <h2><a target="_blank" href="http://volunteer.ala.org.au/">Biodiversity volunteer portal</a></h2>
            <table>
                <tbody>
                <tr><td>Specimen labels transcribed</td><td><span class="count"><db:formatNumber value="${volunteerPortalCounts?.recordsTranscribed}"/></span></td></tr>
                <tr><td>Fieldnotes pages transcribed</td><td><span class="count"><db:formatNumber value="${volunteerPortalCounts?.pagesTranscribed}"/></span></td></tr>
                <tr><td>Volunteers</td><td><span class="count"><db:formatNumber value="${volunteerPortalCounts?.volunteers}"/></span></td></tr>
                <tr><td>Projects active</td><td><span class="count"><db:formatNumber value="${volunteerPortalCounts?.activeProjects}"/></span></td></tr>
                <tr><td>Projects completed</td><td><span class="count"><db:formatNumber value="${volunteerPortalCounts?.completedProjects}"/></span></td></tr>
                </tbody>
            </table>
        </div>
        </g:if>
        <div class='link-group click-thru' tabindex="13" id="lifeform-topic">
            <h2>Records by lifeform</h2>
            <table id="lifeformsTable"></table>
        </div>
        <div class='link-group' tabindex="14" id="decade-topic">
            <h2>Records by decade</h2>
            <div id="decadeChart"></div>
        </div>
        <div class='link-group' tabindex="15" id="tree-topic">
            <h2>Taxonomy tree</h2>
            <div id="tree"></div>
        </div>

    </section>
</div><!--close content-->
<r:script>
    function drawVisualization() {
      // Create and populate the data table.
      var data = new google.visualization.DataTable();
      data.addColumn('string', 'Lifeform');
      data.addColumn('number', 'No. Records');
      data.addRows(8);
      data.setCell(0, 0, 'Animals');
      data.setCell(1, 0, 'Birds');
      data.setCell(2, 0, 'Plants');
      data.setCell(3, 0, 'Angiosperms');
      data.setCell(4, 0, 'Dicots');
      data.setCell(5, 0, 'Arthropods');
      data.setCell(6, 0, 'Mammals');
      data.setCell(7, 0, 'Fish');
      data.setCell(0, 1, 23912635);
      data.setCell(1, 1, 19157148);
      data.setCell(2, 1, 5920693);
      data.setCell(3, 1, 3451984);
      data.setCell(4, 1, 2663865);
      data.setCell(5, 1, 1346124);
      data.setCell(6, 1, 937775);
      data.setCell(7, 1, 839419);

      // Create and draw the visualization.
      visualization = new google.visualization.Table(document.getElementById('testChart'));
      visualization.draw(data, {width: "220px", page: 'enable', pageSize: 5});
    }

    var biocacheServicesUrl = "${ConfigurationHolder.config.biocache.baseURL}ws/",
        biocacheWebappUrl = "${ConfigurationHolder.config.biocache.baseURL}",
        bieWebappUrl = "${ConfigurationHolder.config.bie.baseURL}",
        collectionsWebappUrl = "${ConfigurationHolder.config.collectory.baseURL}";
        serverUrl = "${ConfigurationHolder.config.grails.serverURL}"
        stateChartOptions = {
          error: "badQuery",
          query: "*:*",
          charts: ['state'],
          chartsDiv: 'stateChart',
          clickThru: true,
          width: 360,
          height: 180,
          title: "",
          fontSize: 11.5,
          chartArea: {left:0, top:0, width:"100%", height: "100%"},
          state: { legend: {textStyle: {fontSize: 11.3}}, backgroundColor: 'transparent'}
        },
        decadeChartOptions = {
          error: "badQuery",
          query: "*:*",
          charts: ['decade'],
          chartsDiv: 'decadeChart',
          clickThru: true,
          width: 400,
          height: 211,
          chartArea: {left:40, top:0, width:"97%", height: "80%"},
          vAxis: {textPosition: 'in'}
        },
        lifeformChartOptions = {
          error: "badQuery",
          query: "*:*",
          charts: ['species_group'],
          chartsDiv: 'lifeformChart',
          ignore: [],
          clickThru: true,
          width: 300,
          height: 250,
          title: 'By higher-level group',
          chartType: 'table',
          page: 'enable',
          species_group: {
          chartArea: {left:4, width:"30%"}}/*,
          vAxis: {minValue: 0, textPosition:'in'},
          colors: ['#108628'],
          reverseCategories:true,
          hAxis:{slantedTextAngle:60}*/
        },
        taxonomyTreeOptions = {
          /* base url of the collectory */
          collectionsUrl: "${ConfigurationHolder.config.grails.serverURL}",
          /* base url of the biocache ws*/
          biocacheServicesUrl: biocacheServicesUrl,
          /* base url of the biocache webapp*/
          biocacheWebappUrl: biocacheWebappUrl,
          serverUrl: "${ConfigurationHolder.config.grails.serverURL}",
          theme: 'classic',
          icons: true,
          title: '',
          /* the id of the div to create the charts in - defaults is 'charts' */
          targetDivId: "tree",
          /* a query to set the scope of the records */
          query: "*:*"
        };

    google.load("visualization", "1", {packages:["corechart","table"]});
    google.setOnLoadCallback(function() {
        // collections
        collectionsChart.init({
            plants: "${collections.plants}",
            micro: "${collections.micro}",
            insects: "${collections.insects}",
            otherFauna: "${collections.otherFauna}"
        });

        //drawVisualization();

        // biocache charts
        if (biocacheFacets.isReady()) {
            // facet data loaded so draw now
            drawFacetCharts(biocacheFacets.rawFacetData);
        } else {
            // wait til facet data is loaded
            biocacheFacets.onDataLoaded(drawFacetCharts);
        }
    });

    // init sortable
    $(function () {
        $('#floatContainer').sortable({
            stop: function(event, ui) {
                drawFacetCharts(biocacheFacets.rawFacetData);
                collectionsChart.draw();
            },
            tolerance: 'pointer'
        });
    });

    function drawFacetCharts(data) {
        facetChartGroup.drawFacetCharts(data, stateChartOptions);
        facetChartGroup.drawFacetCharts(data, decadeChartOptions);
        facetChartGroup.drawFacetCharts(data, lifeformChartOptions);
        drawLifeformsTable(biocacheWebappUrl);
    }

    wireActions("${ConfigurationHolder.config.grails.serverURL}");

    // set most recorded data to match the current selection (for back button state)
    $('#mostSppGroup').change();

    // tree
    initTaxonTree(taxonomyTreeOptions);

    // init facets then wait til charts are ready
    biocacheFacets.init({biocacheServicesUrl: biocacheServicesUrl});

</r:script>
</body>
</html>