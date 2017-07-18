<div class="col-sm-4 col-md-4" id="email-breakdown-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Occurrence downloads by user type<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>
        <div class="panel-body">
            <div id="emailBreakdown">
                <table class="table table-condensed table-striped table-hover">
                    <tr><td>Education</td><td>${loggerEmailBreakdown?.edu?.events} events</td><td>${loggerEmailBreakdown?.edu?.records} records</td></tr>
                    <tr><td>Government</td><td>${loggerEmailBreakdown?.gov?.events} events</td><td>${loggerEmailBreakdown?.gov?.records} records</td></tr>
                    <tr><td>Other</td><td>${loggerEmailBreakdown?.other?.events} events</td><td>${loggerEmailBreakdown?.other?.records} records</td></tr>
                    <tr><td>Unspecified</td><td>${loggerEmailBreakdown?.unspecified?.events} events</td><td>${loggerEmailBreakdown?.unspecified?.records} records</td></tr>
                    <tr><td>TOTAL</td><td>${loggerEmailBreakdown?.total?.events} events</td><td>${loggerEmailBreakdown?.total?.records} records</td></tr>
                </table>
            </div>
        </div>
    </div>
</div>