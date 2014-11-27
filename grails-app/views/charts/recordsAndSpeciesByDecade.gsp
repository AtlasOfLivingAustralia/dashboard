<%@page defaultCodec="none" %>
<div id="decades-chart"></div>
<gvisualization:columnCoreChart
        name="decades"
        dynamicLoading="${true}"
        elementId="decades-chart"
        title=""
        width="500" height="250"
        columns="${columns}"
        data="${data}"
        vAxes="${new Expando(0: new Expando(logScale: true))}"
        chartArea="${new Expando(left: 85,width: '80%', height: '65%')}"
        legend="${new Expando(position: 'bottom', alignment: 'center')}"/>