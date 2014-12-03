<div class="span4" id="bold-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">
                <a target="_blank" href="http://bold.ala.org.au/"> Barcode of life</a><i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body">
            <h5 class="text-center">DNA barcode data <a target="_blank" href="http://bold.ala.org.au/"><g:img dir="images/dashboard" file="bold.png"/></a></h5>
            <table class="table table-condensed table-striped table-hover">
                <tbody>
                <tr><td>Records</td><td><span class="count"><db:formatNumber value="${boldCounts?.records}"/></span>
                </td></tr>
                <tr><td>Species</td><td><span class="count"><db:formatNumber value="${boldCounts?.species}"/></span>
                </td></tr>
                <tr><td>Countries</td><td><span class="count"><db:formatNumber value="${boldCounts?.countries}"/></span>
                </td></tr>
                <tr><td>Institutions</td><td><span class="count"><db:formatNumber
                        value="${boldCounts?.institutions}"/></span></td></tr>
                </tbody>
            </table>
        </div>
    </div>
</div>