/**
 * contains dashboard dynamic behaviour
 * @type {{urls: {collections: null, biocache: null, bie: null, app: null}, init: Function, setupPanelInfo: Function, sortableFeature: {sortableListSelector: string, sortableListCookieName: string, sortableListCookieExp: number, sortableOriginalOrder: *, init: Function, serializeListOrderToCookie: Function, restoreListOrderFromCookie: Function}, drawLifeformsTable: Function, wireActions: Function, charts: {collection: {showCollection: Function}}}}
 */
var dashboard = {
    urls: {
        collections: null,
        biocache: null,
        biocacheUI: null,
        bie: null,
        bieUI: null,
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
        dashboard.setupRecordsByLifeFormTable();
        dashboard.wireActions();

        // set most recorded data to match the current selection (for back button state)
        $('#mostSppGroup').change();

        initTaxonTree({
            /* base url of the collectory */
            collectionsUrl: dashboard.urls.collections,
            /* base url of the biocache ws*/
            biocacheServicesUrl: dashboard.urls.biocache,
            /* base url of the biocache webapp*/
            biocacheWebappUrl: dashboard.urls.biocacheUI,
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
            $('#showAllLoggerReasons').html($('#loggerReasonBreakdownTable tr.hideableRow:visible').length ? 'More' : 'Less');
            $('#loggerReasonBreakdownTable .hideableRow').slideToggle(300,  function() {
                jQuery.fn.matchHeight._update();
            });
        });

        //sources
        $('#loggerSourceBreakdownTable .hideableRow').hide();
        $('#showAllLoggerSource').on('click touch', function(){
            $('#showAllLoggerSource').html($('#loggerSourceBreakdownTable tr.hideableRow:visible').length ? 'More' : 'Less');
            $('#loggerSourceBreakdownTable .hideableRow').slideToggle(300,  function() {
                jQuery.fn.matchHeight._update();
            });
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
            $('#' + panelId + ' .panel-title i').on('click touchstart',function() {
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
                    $('#floatContainer > div > div.panel').matchHeight(); // adjust heights so we don't get orphaned boxes
                },
                handle: ".panel-heading",
                delay: 300,
                cancel: ".info-icon, .info-icon i"
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

    /**
     *
     */
    setupRecordsByLifeFormTable: function() {
        // add click listener
        $('#lifeformsTable td:nth-child(odd)').click(function () {
            var group = $(this).html();
            document.location.href = dashboard.urls.biocacheUI + "/occurrences/search?q=*:*&fq=species_group:" + group;
        });

        //lifeforms
        $('#showAllLifeforms').click(function(){
            if ($('#lifeformsTable:visible').length) {
                $('#lifeformsTable').hide();
                $('#lifeformsTable-small').show(100, function() {
                    jQuery.fn.matchHeight._update();
                });
            } else {
                $('#lifeformsTable-small').hide();
                $('#lifeformsTable').show(100, function() {
                    jQuery.fn.matchHeight._update();
                });
            }
            $('#showAllLifeforms').html($('#lifeformsTable:visible').length ? 'Less' : 'More');
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
                            (obj.common === null ? "" : (" - " + obj.common)) + "</td><td class='numberColumn'>" +
                            "<span class='count'>" + obj.count + "</span></td></tr>";
                        });
                    }
                    $('#mostRecorded table').html('<tbody>' + html + '</tbody>');
                    jQuery.fn.matchHeight._update(); // adjust heights
                }
            });
        });
        // reset layout
        $('#resetLayout').click(function() {
            $.cookie(dashboard.sortableFeature.sortableListCookieName, dashboard.sortableFeature.sortableOriginalOrder, {expires: dashboard.sortableFeature.sortableListCookieExp, path: "/"});
            dashboard.sortableFeature.restoreListOrderFromCookie();
            $('#floatContainer > div > div.panel').matchHeight(); // adjust heights so we don't get orphaned boxes
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
            var open = ($('#moreBasisLink').html() === 'Less'),
                $extra = $('.moreBasis');
            $('#moreBasisLink').html(open ? 'More' : 'Less');
            if (open) {
                $extra.slideUp(300, function() {
                    jQuery.fn.matchHeight._update();
                });
            } else {
                $extra.slideDown(300,  function() {
                    jQuery.fn.matchHeight._update();
                });
            }
        });
        // more.. in dataProvider topic
        // more.. in institution topic
        $('.moreLink').click(function () {
            var $extra = $(this).parent().parent().find('.initiallyHidden'),
                open = ($(this).html() === 'Less');
            $extra.slideToggle(300,  function() {
                jQuery.fn.matchHeight._update();
            });
            $(this).html(open ? 'More' : 'Less');
        });
        // more.. in spatial topic
        $('#moreSpatialLink').click(function () {
            $('#moreSpatialLink').html($('#moreSpatial:visible').length ? 'More' : 'Less');
            $('#moreSpatial').slideToggle(300,  function() {
                jQuery.fn.matchHeight._update();
            });
        });
        // more.. in type status topic
        $('#moreTypesLink').click(function () {
            $('#moreTypesLink').html($('#moreTypes:visible').length ? 'More' : 'Less');
            $('#baseTypes').slideToggle(300,  function() {
                jQuery.fn.matchHeight._update();
            });
            $('#moreTypes').slideToggle(300,  function() {
                jQuery.fn.matchHeight._update();
            });
        });
        // datasets links
        $('#datasets-topic td:first-child:not(#description)').click(function () {
            var type = $(this).attr('id');
            if (type == 'dataAvailable') {
                document.location.href = dashboard.urls.biocacheUI + "/occurrence/facets?q=*:*&facets=data_resource_uid&flimit=0";
            } else if (type == 'institutions') {
                document.location.href = dashboard.urls.collections + "/ws/institution/count";
            } else if (type == 'collections') {
                document.location.href = dashboard.urls.collections + "/ws/collection/count";
            } else {
                document.location.href = dashboard.urls.collections + "/datasets#filters=resourceType:" + type;
            }
        });
        // basis of record links
        $('#basis-topic td:first-child').click(function () {
            var basis = $(this).attr('id');
            document.location.href = dashboard.urls.biocacheUI + "/occurrences/search?q=*:*&fq=basis_of_record:" + basis.substring(3);
        });
        // type status links
        $('#typeStatus-topic td:first-child').click(function () {
            var id = $(this).attr('id');
            if (id.length > 5 && id.substr(0,5) === 'image') {
                document.location.href = dashboard.urls.biocacheUI + "/occurrences/search?q=*:*&fq=type_status:" +
                id.substr(5) + "&fq=multimedia:Image#imagesView";
            } else {
                document.location.href = dashboard.urls.biocacheUI + "/occurrences/search?q=*:*&fq=type_status:" + id;
            }
        });
        // species links
        $('#most-topic').on('click', 'td:first-child', function (event) {
            var guid = $(event.currentTarget).attr('id');
            document.location.href = dashboard.urls.bieUI + "/species/" + guid;
        });
        // by date links
        $('#date-topic td:first-child').click(function () {
            var id = $(this).attr('id');
            if (id.length > 4) {
                // handle earliest/latest by linking to record via uuid
                document.location.href = dashboard.urls.biocacheUI + "/occurrence/" + id;
            } else {
                // treat as first year of a century
                var startYear = Number(id),
                    endYear = startYear + 99,
                    range = "[" + startYear + "-01-01T00:00:00Z+TO+" + endYear + "-12-31T23:59:59Z]";
                document.location.href = dashboard.urls.biocacheUI + "/occurrences/search?q=*:*&fq=occurrence_year:" + range;
            }
        });
        // info links
        $('.info-link').click(function () {
            $(this).parent().find('div.info').slideToggle(300,  function() {
                jQuery.fn.matchHeight._update();
            });
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
