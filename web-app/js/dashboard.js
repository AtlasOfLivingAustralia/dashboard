/**
 * Created by IntelliJ IDEA.
 * User: markew
 * Date: 14/03/12
 * Time: 11:27 AM
 */

/*------------------------------------ Biocache facets results -----------------------------------------*/
var biocacheFacets = {

    // config optionally loaded from options
    biocacheServicesUrl: "http://biocache.ala.org.au/ws/",

    // the function to call when facet data is available - loaded from options
    dataLoadedCallback: null,

    // the total num biocache records - loaded from lookup
    totalRecords: 0,

    // the raw data - loaded from lookup
    rawFacetData: null,

    // the facet data sorted into a map (the raw data is used by the charts package - the map is used otherwise)
    facetMap: {},

    // initialisation
    init: function (options) {
        // inject external config
        this.biocacheServicesUrl = options.biocacheServicesUrl || this.biocacheServicesUrl;
        // make this accessible to callbacks
        var that = this;
        // get the data in one go and cache the results
        $.ajax({
            url: urlConcat(this.biocacheServicesUrl, "occurrences/search.json?pageSize=0" +
                    "&q=*:*&facets=state&facets=decade&facets=species_group&flimit=200"),
            dataType: 'jsonp',
            success: function(data) {
                // store raw data
                that.rawFacetData = data;

                // store total
                that.totalRecords = data.totalRecords;

                // transform facet results list into map keyed on field name (the facet name in the data)
                $.each(data.facetResults, function(idx, obj) {
                    that.facetMap[obj.fieldName] = obj.fieldResult;
                });

                // notify listeners that data is loaded
                if (that.dataLoadedCallback !== null) {
                    that.dataLoadedCallback.call(that, that.rawFacetData);
                }
            }
        });
    },

    // returns true if data is loaded
    isReady: function () {
        return this.rawFacetData !== null;
    },

    // sets the listener for data ready event
    onDataLoaded: function (callback) {
        this.dataLoadedCallback = callback;
    }
};

/*------------------------------------ Collections chart -----------------------------------------*/
var collectionsChart = {
    chart: null,
    dataTable: null,
    options: {},
    init: function (counts) {
        this.dataTable = new google.visualization.DataTable();
        this.options = {width: 180, height: 180, pieSliceText: 'label', is3D: true,
                    pieSliceTextStyle: {fontSize: 11.5},
                    chartArea: {top: 8, left: 8, width: "90%", height:"90%"},
                    legend: {position: 'none'},
                    backgroundColor: {fill: 'transparent'}};
        this.dataTable.addColumn('string', 'category');
        this.dataTable.addColumn('number','collections');
        //dataTable.addColumn('string','value');
        this.dataTable.addRow(['Plants', Number(counts.plants)/*, 'plants'*/]);
        this.dataTable.addRow(['Insects', Number(counts.insects)/*, 'ento'*/]);
        this.dataTable.addRow(['Other fauna', Number(counts.otherFauna)/*, 'fauna'*/]);
        this.dataTable.addRow(['Microbes', Number(counts.micro)/*, 'microbes'*/]);

        this.draw();
    },
    draw: function () {
        var that = this;

        this.chart = new google.visualization.PieChart(document.getElementById('collectionsByCategory'));

        google.visualization.events.addListener(this.chart, 'select', function() {
            var slice = that.dataTable.getValue(that.chart.getSelection()[0].row,0),
                    cat = "";
            switch(slice) {
                case 'Plants': cat = 'plants'; break;
                case 'Microbes': cat = 'microbes'; break;
                case 'Insects': cat = 'insects'; break;
                case 'Other fauna': cat = 'fauna'; break;
            }
            document.location.href = "http://collections.ala.org.au?start=" + cat;
        });

        this.chart.draw(this.dataTable, this.options);
    }
};

/*------------------------------------ Decades chart -----------------------------------------*/
var decadesChart = {
    chart: null,
    dataTable: null,
    options: {},
    init: function (dataUrl) {
        var that = this;
        this.options = {
            width:450, height:235,
            hAxis: {},
            legend: {position: "in"},
            chartArea: {top:5, height:"75%", left:"17%", width:"80%"},
            vAxes: {
                0: {logScale: true, minValue: 0, maxValue: 100000000},
                1: {title: "species", log: 120000}
            },
            series: {
                0: {targetAxisIndex: 0},
                1: {targetAxisIndex: 0}
            }
        };
        // get the data
        $.ajax({
            url: dataUrl,
            dataType: 'json',
            success: function(data) {
                that.dataTable = google.visualization.arrayToDataTable(data);
                that.draw();
            }
        });
    },
    draw: function () {
        var that = this;

        // Create and draw the visualization.
        new google.visualization.ColumnChart(document.getElementById('decadeChart')).
            draw(this.dataTable, this.options);
    }
};

/*------------------------------------ Lifeforms chart -----------------------------------------*/
function drawLifeformsTable(biocacheWebappUrl) {
    var $table = $('#lifeformsTable'),
        content = "",
        list = biocacheFacets.facetMap.species_group,
        l = list.length,
        c = Math.floor(l/2);
    if (biocacheFacets.isReady()) {
        for (i = 0; i < c; i++) {
            content += "<tr><td>" + list[i].label + "</td>" + "<td>" + format(list[i].count) + "</td>";
            content += "<td>" + list[c+i].label + "</td>" + "<td>" + format(list[c+i].count) + "</td></tr>";
        }
        /*$.each(biocacheFacets.facetMap.species_group, function(i, obj) {
            if (i % 2 === 0) { content += "<tr>"; }
            content += "<td>" + obj.label + "</td>" + "<td>" + format(obj.count) + "</td>";
            if (i % 2 === 1) { content += "</tr>"; }
        });*/
        $table.html($(content));
        // add click listener
        $('#lifeformsTable td:nth-child(odd)').click(function () {
            var group = $(this).html();
            document.location.href = biocacheWebappUrl + "occurrences/search?q=*:*&fq=species_group:" + group;
        });
    }
}

/*------------------------------------ bind page actions -----------------------------------------*/
function wireActions(serverUrl) {
    //lifeform dropdown for most recorded spp
    $('#mostSppGroup').change(function () {
        var group = $(this).val();
        // set a spinner
        $('#mostLoadingImg').css('display','inline');
        // get the data in one go and cache the results
        $.ajax({
            url: urlConcat(serverUrl, "/dashboard/mostRecorded?group=" + group),
            dataType: 'json',
            success: function(data) {
                // clear the spinner
                $('#mostLoadingImg').css('display','none');
                var html = "";
                if (data.error !== null) {
                    html = '<p class="error" title="' + data.reason + '">' + data.error + '</p>';
                } else {
                    $.each(data.facets, function(i, obj) {
                        html += "<tr><td id='"+ obj.facet + "'><em>" + obj.name + "</em>" +
                                (obj.common === null ? "" : (" - " + obj.common)) + "</td><td>" +
                                "<span class='count'>" + obj.count + "</span></td></tr>";
                    });
                }
                $('#mostRecorded table').html(html);
            }
        });
    });
    // reset layout
    $('#resetLayout').click(restoreOrder);
    // show json
    $('#showJson').click(function () {
        document.location.href = serverUrl + "/dashboard/data";
    });
    // download csv
    $('#downloadCsv').click(function () {
        document.location.href = serverUrl + "/dashboard/downloadAsCsv";
    });
    // more.. in basis topic
    $('#moreBasisLink').click(function () {
        var open = ($('#moreBasisLink').html() === 'less..'),
            $extra = $('#moreBasis');
        $('#moreBasisLink').html(open ? 'more..' : 'less..');
        if (open) {
            $extra.find('td > div').slideUp(300);
        } else {
            $extra.find('td > div').slideDown(300);
        }
    });
    // more.. in dataProvider topic
    // more.. in institution topic
    $('.moreLink').click(function () {
        var $extra = $(this).parent().parent().find('.initiallyHidden'),
            open = ($(this).html() === 'less..');
        $extra.slideToggle(300);
        $(this).html(open ? 'more..' : 'less..');
    });
    // more.. in spatial topic
    $('#moreSpatialLink').click(function () {
        $('#moreSpatialLink').html($('#moreSpatial:visible').length ? 'more..' : 'less..');
        $('#moreSpatial').toggle(300);
    });
    // more.. in type status topic
    $('#moreTypesLink').click(function () {
        $('#moreTypesLink').html($('#moreTypes:visible').length ? 'more..' : 'less..');
        $('#baseTypes').toggle(300);
        $('#moreTypes').toggle(300);
    });
    // datasets links
    $('#datasets-topic td:first-child').click(function () {
        var type = $(this).attr('id');
        document.location.href = collectionsWebappUrl + "/datasets#filters=resourceType:" + type;
    });
    // basis of record links
    $('#basis-topic td:first-child').click(function () {
        var basis = $(this).attr('id');
        document.location.href = biocacheWebappUrl + "/occurrences/search?q=*:*&fq=basis_of_record:" + basis.substring(3);
    });
    // type status links
    $('#typeStatus-topic td:first-child').click(function () {
        var id = $(this).attr('id');
        if (id.length > 5 && id.substr(0,5) === 'image') {
            document.location.href = biocacheWebappUrl + "/occurrences/search?q=*:*&fq=type_status:" +
                    id.substr(5) + "&fq=multimedia:Image#imagesView";
        } else {
            document.location.href = biocacheWebappUrl + "/occurrences/search?q=*:*&fq=type_status:" + id;
        }
    });
    // species links
    $('#most-topic').on('click', 'td:first-child', function (event) {
        var guid = $(event.currentTarget).attr('id');
        document.location.href = bieWebappUrl + "/species/" + guid;
    });
    // by date links
    $('#date-topic td:first-child').click(function () {
        var id = $(this).attr('id');
        if (id.length > 4) {
            // handle earliest/latest by linking to record via uuid
            document.location.href = biocacheWebappUrl + "/occurrence/" + id;
        } else {
            // treat as first year of a century
            var startYear = Number(id),
                endYear = startYear + 99,
                range = "[" + startYear + "-01-01T00:00:00Z+TO+" + endYear + "-12-31T23:59:59Z]";
            document.location.href = biocacheWebappUrl + "/occurrences/search?q=*:*&fq=occurrence_year:" + range;
        }
    });
    // info links
    $('.info-link').click(function () {
        $(this).parent().find('div.info').toggle();
    });
}

function restoreOrder() {
    var $container = $('#floatContainer'),
        $items = $container.find('.link-group'),
        itemCount = $items.length,
        $toMove;

    for (var i = 0; i < itemCount; i++) {
        // check whether the corresponding item is in the correct slot
        if ($($items[i]).attr('tabindex') !== i) {
            // find the right one and cut it
            $toMove = $container.find('.link-group[tabindex=' + i + ']').remove();
            // paste before this item
            $($items[i]).before($toMove);
            // need to refresh the items list from the new positions
            $items = $container.find('.link-group');
        }
    }
    drawFacetCharts(biocacheFacets.rawFacetData);
    collectionsChart.draw();
}

/**
 * Formats numbers as human readable. Handles numbers in the millions.
 * @param count the number
 */
function format(count) {
    /*if (count >= 1000000) {
        return (count/1000000).toFixed(2) + 'M';
    }*/
    return addCommas(count);
}
