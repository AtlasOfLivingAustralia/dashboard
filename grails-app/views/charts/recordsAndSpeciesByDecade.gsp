<div id="decades-chart"></div>
<gvisualization:columnCoreChart
        name="decades"
        dynamicLoading="${true}"
        elementId="decades-chart"
        title=""
        columns="${columns}"
        data="${data}"
        height="250"
        vAxes="${new Expando(0: new Expando(logScale: true))}"
        chartArea="${new Expando(left: 85, width: '80%')}"
        legend="${[position: 'top', alignment: 'center']}"/>