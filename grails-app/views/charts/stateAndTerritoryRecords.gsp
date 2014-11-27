<div id="stateAndTerritoryRecords-chart"></div>
<gvisualization:pieCoreChart
        name="stateAndTerritoryRecords"
        dynamicLoading="${true}"
        elementId="stateAndTerritoryRecords-chart"
        title=""
        width="350" height="200"
        columns="${columns}"
        data="${data}"
        is3D="${true}"
        chartArea="${new Expando(width: '100%', height: '100%')}"
        legendTextStyle="${new Expando(fontSize: '12')}"
        pieSliceTextStyle="${new Expando(fontSize: '12')}"
        backgroundColor="${new Expando(fill: 'transparent')}"/>