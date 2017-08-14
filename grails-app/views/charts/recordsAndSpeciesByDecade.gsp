<div id="decades-chart"></div>
<g:if test="${!data}">
    <asset:image src="spinner.gif" />
</g:if>
<gvisualization:columnCoreChart
        name="decades"
        dynamicLoading="${true}"
        elementId="decades-chart"
        title=""
        columns="${columns}"
        data="${data}"
        height="250"
        vAxes="${[0: [logScale: true]]}"
        chartArea="${[left: 85, width: '80%']}"
        legend="${[position: 'top', alignment: 'center']}"
/>