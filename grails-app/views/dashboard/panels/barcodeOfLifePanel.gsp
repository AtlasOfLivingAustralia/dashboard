<div class="col-sm-4 col-md-4" id="bold-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">
                <a target="_blank" href="http://bold.ala.org.au/"> Barcode of life</a><i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body"> %{--<g:img dir="images/dashboard" file="bold.png"/>--}%
            <h5 class="text-center">DNA barcode data <a target="_blank" href="http://bold.ala.org.au/"><asset:image src="dashboard/bold.png"/></a></h5>
            <table class="table table-condensed table-striped table-hover">
                <tbody>
                <tr><td>Records</td><td class="numberColumn"><span class="count"><db:formatNumber value="${boldCounts?.records}"/></span>
                </td></tr>
                <tr><td>Species</td><td class="numberColumn"><span class="count"><db:formatNumber value="${boldCounts?.species}"/></span>
                </td></tr>
                <tr><td>Countries</td><td class="numberColumn"><span class="count"><db:formatNumber value="${boldCounts?.countries}"/></span>
                </td></tr>
                <tr><td>Institutions</td><td class="numberColumn"><span class="count"><db:formatNumber
                        value="${boldCounts?.institutions}"/></span></td></tr>
                </tbody>
            </table>
        </div>
    </div>
</div>