<div class="col-sm-4 col-md-4" id="state-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Records by state and territory<i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>

        <div class="panel-body">
            <div id="stateAndTerritoryRecords">
%{--                <g:img dir="images" file="spinner.gif"/>--}%
               %{-- <asset:image src="spinner.gif" alt=""/>--}%
                <g:include controller="charts" action="stateAndTerritoryRecords"/>
            </div>
        </div>
    </div>
</div>