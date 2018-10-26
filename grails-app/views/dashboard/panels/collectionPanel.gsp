<div class="col-sm-4 col-md-4" id="collections-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title"><a href="${grailsApplication.config.collectory.baseURL}"><span class="count">${collections.total}</span></a>
                Collections<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>
        <div class="panel-body text-center">
            <p class="paragraph">Click on a Collection for details:</p>
            <div id="collectionsByCategory">
                %{--<asset:image src="spinner.gif"/>--}%
                <g:include controller="charts" action="collections"/>
            </div>
        </div>
    </div>
</div>