<div class="col-sm-4 col-md-4" id="nsl-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">National Species Lists<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>

        <div class="panel-body">
            <table class="table table-condensed table-striped table-hover">
                <tbody>
                <tr><td>Accepted names</td><td class="numberColumn"><span class="count"><db:formatNumber
                        value="${taxaCounts?.acceptedNames}"/></span></td></tr>
                <tr><td>Synonyms</td><td class="numberColumn"><span class="count"><db:formatNumber
                        value="${taxaCounts?.synonymNames}"/></span></td></tr>
                <tr><td>Species names</td><td class="numberColumn"><span class="count"><db:formatNumber
                        value="${taxaCounts?.acceptedSpeciesNames}"/></span></td></tr>
                <tr><td style="margin-top:20px;">Species with records</td><td class="numberColumn"><span
                        class="count"><db:formatNumber
                            value="${taxaCounts?.speciesWithRecords}"/></span></td></tr>
                </tbody>
            </table>
        </div>
    </div>
</div>