<div class="span4" id="lifeform-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Records by life form<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>
        <div class="panel-body">
            <table id="lifeformsTable-small" class="table table-condensed table-striped table-hover">
                <g:set var="rowsToDisplay" value="${6}"/>
                <g:each in="${0..rowsToDisplay}" var="index">
                    <tr class="link">
                        <td>${records[index].label}</td>
                        <td class="numberColumn">${db.formatNumber(value: records[index].count)}</td>
                        <g:if test="${records[index + rowsToDisplay]}">
                        <td>${records[(index + rowsToDisplay) as Integer].label}</td>
                        <td class="numberColumn">${db.formatNumber(value: records[(index + rowsToDisplay) as Integer].count)}</td>
                        </g:if>
                    </tr>
                </g:each>
            </table>
            <table id="lifeformsTable" class="table table-condensed table-striped table-hover" style="display: none;">
                <g:set var="halfCount" value="${(records.size()/2 as Float).trunc() as Integer}"/>
                <g:each in="${0..halfCount-1}" var="index">
                    <tr class="link">
                        <td>${records[index].label}</td>
                        <td class="numberColumn">${db.formatNumber(value: records[index].count)}</td>
                        <g:if test="${records[index + halfCount]}">
                        <td>${records[(index + halfCount) as Integer].label}</td>
                        <td class="numberColumn">${db.formatNumber(value: records[(index + halfCount) as Integer].count)}</td>
                        </g:if>
                    </tr>
                </g:each>
            </table>
            <p class="paragraph"><a href="javascript:void(0);" id="showAllLifeforms" class="btn btn-small">More</a></p>
        </div>
    </div>
</div>