<div class="span6" id="source-breakdown-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Occurrence downloads by source<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>
        <div class="panel-body">
            <div id="sourceBreakdown">
                <table id="loggerSourceBreakdownTable" class="table table-condensed table-striped table-hover">
                    <g:each in="${loggerSourceBreakdown}" var="r" status="rIdx">
                        <tr id="loggerSourceBreakdown-${r[0] == 'TOTAL' ? 'TOTAL' : rIdx}"
                            class="${rIdx >= 6 && r[0] != 'TOTAL' ? 'hideableRow' : ''} ${r[0] == 'TOTAL' ? 'total-highlight' : ''}">
                            <td>${r[0]}</td>
                            <td class="numberColumn">${r[1]} events</td>
                            <td class="numberColumn">${r[2]} records</td>
                        </tr>
                    </g:each>
                </table>

                <p class="paragraph"><a href="javascript:void(0);" id="showAllLoggerSource" class="btn btn-small">More</a></p>
            </div>
        </div>
    </div>
</div>