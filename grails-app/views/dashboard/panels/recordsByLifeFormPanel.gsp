<div class="span4" id="lifeform-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Records by life form<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>
        <div class="panel-body">
            <table id="lifeformsTable" class="table table-condensed table-striped table-hover">
                <g:set var="halfCount" value="${(records.size()/2 as Float).trunc() as Integer}"/>
                <g:each in="${0..<halfCount}" var="index">
                    <g:set var="cssClass" value="${index > 5 ? 'hideable' : ''}"/>
                    <tr class="link ${cssClass}">
                        <td>${records[index].label}</td>
                        <td class="numberColumn">${g.formatNumber(number: records[index].count, type: 'number')}</td>
                        <td>${records[(index + halfCount) as Integer].label}</td>
                        <td class="numberColumn">${g.formatNumber(number: records[(index + halfCount) as Integer].count, type: 'number')}</td>
                    </tr>
                </g:each>
            </table>
            <p class="paragraph"><a href="javascript:void(0);" id="showAllLifeforms" class="btn btn-small">More</a></p>
        </div>
    </div>
</div>