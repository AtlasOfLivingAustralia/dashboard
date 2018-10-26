<div class="col-sm-4 col-md-4" id="records-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Occurrence records<i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body">
            <p id="totalRecords">
                <a href="${grailsApplication.config.biocache.webappURL}/occurrences/"><db:addCommas value="${totalRecords.total}"/></a>
            </p>

            <p class="text-center p1-5">records in total.</p>

        </div>
        <div class="panel-body panel-info hidden">
        </div>
    </div>
</div>