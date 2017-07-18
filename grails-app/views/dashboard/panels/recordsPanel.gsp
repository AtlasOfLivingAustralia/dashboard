<div class="col-sm-4 col-md-4" id="records-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Occurrence records<i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body">
            <p id="totalRecords">
                <a href="http://biocache.ala.org.au/occurrences/"><db:addCommas value="${totalRecords.total}"/></a>
            </p>

            <p class="text-center p1-5">records in total.</p>

            <p class="third-paragraph">
                We estimate the number of potential duplicate records to be
                <a href="${grailsApplication.config.biocache.baseURL}/occurrences/search?q=*:*&fq=duplicate_status:D"
                   id="duplicateCount" class="link"><em><db:addCommas value="${totalRecords.duplicates}"/></em></a>.
            </p>
        </div>
        <div class="panel-body panel-info hidden">
        </div>
    </div>
</div>