<div class="col-sm-4 col-md-4" id="datasets-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">
                <a href="http://collections.ala.org.au/datasets"><span class="count">${g.formatNumber(number:datasets.total,format:'###,##0')}</span></a>
                Data sets
                <i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body">
            <table class="table table-condensed table-striped table-hover">
                <tr class="link">
                    <td id="institutions">Institutions</td>
                    <td class="numberColumn"><span class="count"><db:formatNumber
                            value="${datasets.institutionCount}"/></span>
                    </td>
                </tr>
                <tr class="link">
                    <td id="collections">Collections</td>
                    <td class="numberColumn"><span class="count"><db:formatNumber
                            value="${datasets.collectionCount}"/></span>
                    </td>
                </tr>
                <tr class="link">
                    <td id="records">Data Resources</td>
                    <td class="numberColumn"><span class="count"><db:formatNumber
                            value="${datasets.groups.records}"/></span>
                    </td>
                </tr>
                <tr class="link">
                    <td class="text-indent" id="dataAvailable">&#8226; Data Available</td>
                    <td class="numberColumn"><span class="count"><db:formatNumber
                            value="${datasets.dataAvailableCount}"/></span>
                    </td>
                </tr>
                <tr>
                    <td class="text-indent" id="description">&#8226; Description only</td>
                    <td class="numberColumn"><span class="count"><db:formatNumber
                            value="${datasets.descriptionOnlyCount}"/></span>
                    </td>
                </tr>
               <tr class="link">
                    <td id="species-list">Species List sets</td>
                    <td class="numberColumn"><span class="count"><db:formatNumber
                            value="${datasets.groups.'species-list'}"/></span>
                    </td>
               </tr>
               <g:if test="${datasets.groups.document}">
                    <tr class="link">
                        <td id="document">Document sets</td>
                        <td class="numberColumn"><span class="count"><db:formatNumber
                                value="${datasets.groups.document}"/></span>
                        </td>
                    </tr>
                </g:if>
                <g:if test="${datasets.groups.website}">
                    <tr class="link">
                       <td id="website">Harvested websites</td>
                       <td class="numberColumn">
                           <span class="count"><db:formatNumber
                                   value="${datasets.groups.website}"/></span>
                       </td>
                    </tr>
                </g:if>
                <g:if test="${datasets.groups.uploads}">
                    <tr class="link">
                        <td id="uploads">Uploaded record sets</td>
                        <td class="numberColumn"><span class="count"><db:formatNumber
                                value="${datasets.groups.uploads}"/></span>
                        </td>
                    </tr>
                </g:if>
            </table>

            <p class="paragraph">
                Most recently added dataset is:<br/>
            </p>
            <div class="text-center">
                <a href="${grailsApplication.config.collectory.baseURL}/public/show/${datasets.last.uid}">
                    <h4>"<em><db:shorten text="${datasets.last.name}" size="66"/></em>"</h4>
                </a>
            </div>

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