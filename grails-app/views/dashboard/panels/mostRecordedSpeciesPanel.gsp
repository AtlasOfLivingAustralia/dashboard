<div class="col-sm-4 col-md-4" id="most-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Most recorded species<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>

        <div class="panel-body">
            <div id="mostRecorded">
                <g:if test="${mostRecorded?.error?.asBoolean()}">
                    <p class="error" title="${basisOfRecord?.reason}">${mostRecorded?.error}</p>
                </g:if>
                <table class="table table-condensed table-striped table-hover link">
                    <g:each in="${mostRecorded.facets}" var="m">
                        <tr class="link"><td id="${m.facet}"><em>${m.name}</em>
                            <g:if test="${m.common}">- ${m.common}</g:if></td>
                            <td><span class="count">${m.formattedCount}</span></td>
                        </tr>
                    </g:each>
                </table>
            </div>
            <g:select from="['all lifeforms', 'Plants', 'Animals', 'Birds', 'Reptiles', 'Arthropods',
                             'Mammals', 'Fish', 'Insects', 'Amphibians', 'Bacteria', 'Fungi']" name="mostSppGroup"/>
            %{--<g:img style="vertical-align:middle;display:none" id="mostLoadingImg" dir="images" file="spinner.gif"/>--}%
            <asset:image style="vertical-align:middle;display:none" id="mostLoadingImg" src="spinner.gif" />
        </div>
    </div>
</div>