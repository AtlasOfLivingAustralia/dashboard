/**
 * contains dashboard dynamic behaviour
 * @type {{urls: {collections: null, biocache: null, bie: null, app: null}, init: Function, setupPanelInfo: Function, sortableFeature: {sortableListSelector: string, sortableListCookieName: string, sortableListCookieExp: number, sortableOriginalOrder: *, init: Function, serializeListOrderToCookie: Function, restoreListOrderFromCookie: Function}, drawLifeformsTable: Function, wireActions: Function, charts: {collection: {showCollection: Function}}}}
 */
var dashboard = {
    urls: {
        collections: null,
        biocache: null,
        bie: null,
        app: null
    },

    panelRenderingErrors: [],

    /**
     * Initialize the dashboard dynamic features
     * @param options
     */
    init: function (options) {
        dashboard.urls = options.urls;
        dashboard.sortableFeature.init();
        dashboard.setupPanelInfo();
        dashboard.drawLifeformsTable();
        dashboard.wireActions();

        // set most recorded data to match the current selection (for back button state)
        $('#mostSppGroup').change();

        initTaxonTree({
            /* base url of the collectory */
            collectionsUrl: dashboard.urls.collections,
            /* base url of the biocache ws*/
            biocacheServicesUrl: dashboard.urls.biocache + 'ws/',
            /* base url of the biocache webapp*/
            biocacheWebappUrl: dashboard.urls.biocache,
            serverUrl: dashboard.urls.app,
            theme: 'classic',
            icons: true,
            title: '',
            /* the id of the div to create the charts in - defaults is 'charts' */
            targetDivId: "tree",
            /* a query to set the scope of the records */
            query: "*:*"
        });

        //reasons
        $('#loggerReasonBreakdownTable .hideableRow').hide();
        $('#showAllLoggerReasons').on('click touch', function(){
            $('#loggerReasonBreakdownTable .hideableRow').toggle('slow');
        });

        if(dashboard.panelRenderingErrors.length > 0) {
            var html = '';
            $.each(dashboard.panelRenderingErrors, function(i, fileName) {
                html += '<li>Error rendering ' + fileName + '</li>';
            });
            $('#show-error-button').attr('data-content', html);
            $('#show-error-button').popover();
            $('#show-error-button').toggleClass('initiallyHidden');
        }
    },

    /**
     * Enables the panels info
     */
    setupPanelInfo: function() {
        $.each(panelInfo, function(panelId, info) {
            $('#' + panelId + ' .panel-title i').removeClass('hidden');
            $('#' + panelId + ' .panel-title i').click(function() {
                $('#' + panelId + ' .panel-body').toggleClass('hidden');
            });
            $('#' + panelId + ' .panel-info').html(markdown.toHTML(info));
        });
    },

    /**
     * Contains the logic required to crete a sortable grid layout
     */
    sortableFeature: {
        sortableListSelector: "#floatContainer",
        sortableListCookieName: "alaDashboardCustomSorting",
        sortableListCookieExp: 10 * 365,
        sortableOriginalOrder: $.map($("#floatContainer > div"), function(val, i) {
            return $(val).attr("id");
        }),

        /**
         *
         */
        init: function() {
            $(dashboard.sortableFeature.sortableListSelector).sortable({
                tolerance: 'pointer',
                cursor: "move",
                update: function(){
                    dashboard.sortableFeature.serializeListOrderToCookie();
                },
                handle: ".panel-heading"
            });

            dashboard.sortableFeature.restoreListOrderFromCookie();
        },

        /**
         *
         */
        serializeListOrderToCookie: function() {
            $.cookie(dashboard.sortableFeature.sortableListCookieName,
                $(dashboard.sortableFeature.sortableListSelector).sortable("toArray"),
                {expires: dashboard.sortableFeature.sortableListCookieExp, path: "/"});
        },

        /**
         *
         */
        restoreListOrderFromCookie: function() {
            var i, previousorder;
            var cookie = $.cookie(dashboard.sortableFeature.sortableListCookieName);
            if (!cookie) { return; }
            previousorder = cookie.split(',');
            for (i = 0; i < previousorder.length; i++) {
                $('#'+previousorder[i]).appendTo($(dashboard.sortableFeature.sortableListSelector));
            }
        }
    },

    // TODO Move this to server side. This is actually hard to watch!!
    drawLifeformsTable: function() {
        $.ajax({
            url: dashboard.urls.biocache + "ws/occurrences/search.json?pageSize=0" +
            "&q=*:*&facets=species_group&flimit=200",
            dataType: 'jsonp',
            success: function(data) {
                // transform facet results list into map keyed on field name (the facet name in the data)
                var facetMap = {};

                $.each(data.facetResults, function(idx, obj) {
                    facetMap[obj.fieldName] = obj.fieldResult;
                });

                var $table = $('#lifeformsTable'),
                    content = "",
                    list = facetMap.species_group,
                    l = list.length,
                    c = Math.floor(l/2);

                for (i = 0; i < c; i++) {

                    var className = '';
                    if(i>5){
                        className = 'hideable';
                    }
                    content += "<tr class='" + className +"'><td>" + list[i].label + "</td>" + "<td>" + format(list[i].count) + "</td>";
                    content += "<td>" + list[c+i].label + "</td>" + "<td>" + format(list[c+i].count) + "</td></tr>";
                }

                $table.html($(content));
                // add click listener
                $('#lifeformsTable td:nth-child(odd)').click(function () {
                    var group = $(this).html();
                    document.location.href = biocacheWebappUrl + "occurrences/search?q=*:*&fq=species_group:" + group;
                });

                //lifeforms
                $('#lifeformsTable .hideable').hide();
                $('#showAllLifeforms').click(function(){
                    $('#lifeformsTable .hideable').toggle('slow');
                });
            }
        });
    },

    wireActions: function() {
        //lifeform dropdown for most recorded spp
        $('#mostSppGroup').change(function () {
            var group = $(this).val();
            // set a spinner
            $('#mostLoadingImg').css('display','inline');
            // get the data in one go and cache the results
            $.ajax({
                url: dashboard.urls.app + "/dashboard/mostRecorded?group=" + group,
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
        $('#resetLayout').click(function() {
            $.cookie(dashboard.sortableFeature.sortableListCookieName, dashboard.sortableFeature.sortableOriginalOrder, {expires: dashboard.sortableFeature.sortableListCookieExp, path: "/"});
            dashboard.sortableFeature.restoreListOrderFromCookie();
        });
        // show json
        $('#showJson').click(function () {
            document.location.href = dashboard.urls.app + "/dashboard/data";
        });
        // download csv
        $('#downloadCsv').click(function () {
            document.location.href = dashboard.urls.app + "/dashboard/downloadAsCsv";
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
            document.location.href = dashboard.urls.collections + "/datasets#filters=resourceType:" + type;
        });
        // basis of record links
        $('#basis-topic td:first-child').click(function () {
            var basis = $(this).attr('id');
            document.location.href = dashboard.urls.biocache + "/occurrences/search?q=*:*&fq=basis_of_record:" + basis.substring(3);
        });
        // type status links
        $('#typeStatus-topic td:first-child').click(function () {
            var id = $(this).attr('id');
            if (id.length > 5 && id.substr(0,5) === 'image') {
                document.location.href = dashboard.urls.biocache + "/occurrences/search?q=*:*&fq=type_status:" +
                id.substr(5) + "&fq=multimedia:Image#imagesView";
            } else {
                document.location.href = dashboard.urls.biocache + "/occurrences/search?q=*:*&fq=type_status:" + id;
            }
        });
        // species links
        $('#most-topic').on('click', 'td:first-child', function (event) {
            var guid = $(event.currentTarget).attr('id');
            document.location.href = dashboard.urls.bie + "/species/" + guid;
        });
        // by date links
        $('#date-topic td:first-child').click(function () {
            var id = $(this).attr('id');
            if (id.length > 4) {
                // handle earliest/latest by linking to record via uuid
                document.location.href = dashboard.urls.biocache + "/occurrence/" + id;
            } else {
                // treat as first year of a century
                var startYear = Number(id),
                    endYear = startYear + 99,
                    range = "[" + startYear + "-01-01T00:00:00Z+TO+" + endYear + "-12-31T23:59:59Z]";
                document.location.href = dashboard.urls.biocache + "/occurrences/search?q=*:*&fq=occurrence_year:" + range;
            }
        });
        // info links
        $('.info-link').click(function () {
            $(this).parent().find('div.info').toggle();
        });
    },

    /**
     * Charts event handlers
     */
    charts: {
        collection: {
            /**
             * Event handler that redirects the user to the URL that corresponds to the selected chart portion
             * @param visualization
             * @param data
             */
            showCollection: function(visualization, data) {
                if(visualization.getSelection() && visualization.getSelection().length > 0) {
                    var slice = data.getValue(visualization.getSelection()[0].row, 0),
                        cat = "";
                    switch (slice) {
                        case 'Plants':
                            cat = 'plants';
                            break;
                        case 'Microbes':
                            cat = 'microbes';
                            break;
                        case 'Insects':
                            cat = 'insects';
                            break;
                        case 'Other fauna':
                            cat = 'fauna';
                            break;
                    }
                    // Redirect to the collection
                    document.location.href = dashboard.urls.collections + "?start=" + cat;
                }
            }
        }
    }
};

/**
 * Formats numbers as human readable. Handles numbers in the millions.
 * @param count the number
 */
function format(count) {
    return addCommas(count);
}
