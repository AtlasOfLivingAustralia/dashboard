<div class="span4" id="basis-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Basis of records<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>
        <div class="panel-body">
            <g:if test="${basisOfRecord.error.asBoolean()}">
                <p class="error" title="${basisOfRecord.reason}">${basisOfRecord.error}</p>
            </g:if>
            <table class="table table-condensed table-striped table-hover">
                <tbody>
                <g:each in="${basisOfRecord.facets[0..Math.min(4, basisOfRecord?.facets?.size() - 1)]}" var="b">
                    <tr class="link">
                        <td id="br-${b.facet}">${b.display}</td>
                        <td class="numberColumn"><span class="count">${b.formattedCount}</span></td>
                    </tr>
                </g:each>
                <g:if test="${basisOfRecord.facets.size() > 5}">
                    <g:each in="${basisOfRecord.facets[5..-1]}" var="b">
                        <tr class="moreBasis link" style="display:none;">
                            <td id="br-${b.facet}">${b.display}</td>
                            <td class="numberColumn"><span class="count">${b.formattedCount}</span></td>
                        </tr>
                    </g:each>
                </g:if>
                </tbody>
            </table>

            <p class="paragraph"><button id="moreBasisLink" class="btn btn-small">More</button></p>
        </div>
    </div>
</div>