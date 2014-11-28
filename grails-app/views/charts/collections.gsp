<div id="collections-chart"></div>
<gvisualization:pieCoreChart
        name="collections"
        dynamicLoading="${true}"
        elementId="collections-chart"
        title=""
        columns="${columns}"
        data="${data}"
        is3D="${true}"
        legend="${new Expando(position: 'none')}"
        pieSliceText="label" chartArea="${new Expando(width: '95%', height: '90%', left: 20)}"
        pieSliceTextStyle="${new Expando(fontSize: '12')}"
        backgroundColor="${new Expando(fill: 'transparent')}"
        select="function() {dashboard.charts.collection.showCollection(collections, collections_data)}"
        onmouseover="function() { jQuery('#collections-chart').css('cursor','pointer')}"
        onmouseout="function() { jQuery('#collections-chart').css('cursor','default')}"/>