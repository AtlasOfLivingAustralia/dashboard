<div class="span4" id="datasets-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">
                <a href="http://collections.ala.org.au/datasets"><span class="count">${datasets.total}</span></a>
                Data sets
                <i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body">
            <table class="table table-condensed table-striped table-hover">
                <tr class="link">
                    <td id="website">Harvested websites</td>
                    <td>
                        <span class="count"><db:formatNumber
                                value="${datasets.groups.website}"/></span>
                    </td>
                </tr>
                <tr class="link">
                    <td id="records">Occurrence record sets</td>
                    <td><span class="count"><db:formatNumber
                            value="${datasets.groups.records}"/></span>
                    </td>
                </tr>
                <tr class="link">
                    <td id="document">Document sets</td>
                    <td><span class="count"><db:formatNumber
                            value="${datasets.groups.document}"/></span>
                    </td>
                </tr>
                <tr class="link">
                    <td id="uploads">Uploaded record sets</td>
                    <td><span class="count"><db:formatNumber
                            value="${datasets.groups.uploads}"/></span>
                    </td>
                </tr>
            </table>

            <p class="paragraph">
                Most recently added dataset is:<br/>
                <a href="${grailsApplication.config.collectory.baseURL}/public/show/${datasets.last.uid}">
                    <em><db:shorten text="${datasets.last.name}" size="66"/></em></a>
            </p>

            <div id="datasets-info" class="info" style="display: none;">
                <p>Much of the content in the Atlas, such as occurrence records, environmental data, images and the
                conservation status of species, comes from data sets provided by collecting institutions, individual
                collectors and community groups.</p>

                <p>The data sets are listed on the Atlas <a
                        href="http://collections.ala.org.au/datasets/">Datasets page</a>.
                They can be searched and browsed by category.</p>

                <p>This infographic shows the number of datasets for the four major categories.</p>
            </div>
        </div>
    </div>
</div>