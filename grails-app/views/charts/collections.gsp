<div id="collections-chart"></div>
<g:if test="${!data}">
    <asset:image src="spinner.gif" />
</g:if>
<gvisualization:pieCoreChart
        name="collections"
        dynamicLoading="${true}"
        elementId="collections-chart"
        title=""
        columns="${columns}"
        data="${data}"
        is3D="${true}"
        legend="none"
        pieSliceText="label" chartArea="${[width: '100%', height: '100%']}"
        pieSliceTextStyle="${[fontSize: '12']}"
        backgroundColor="${[fill: 'transparent']}"
        select="function() {dashboard.charts.collection.showCollection(collections, collections_data)}"
        onmouseover="function() { jQuery('#collections-chart').css('cursor','pointer')}"
        onmouseout="function() { jQuery('#collections-chart').css('cursor','default')}"/>