<div class="col-sm-4 col-md-4" id="dataProvider-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Records by data provider<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>
        <div class="panel-body">
            <table class="click-thru table table-condensed table-striped table-hover">
                <g:each in="${dataProviders[0..Math.min(6, dataProviders.size() - 1)]}" var="b">
                    <tr>
                        <td id="${b.uid}" title="${b.name}"><a href="${b.uri}">
                            <db:shorten text="${b.display}" size="35"/>
                        </a></td>
                        <td class="numberColumn"><span class="count">${b.formattedCount}</span></td>
                    </tr>
                </g:each>
            </table>
            <g:if test="${dataProviders.size() > 7}">
                <div id="moreDataProvider" class="initiallyHidden">
                    <table class="click-thru table table-condensed table-striped table-hover">
                        <g:each in="${dataProviders[7..-1]}" var="b">
                            <tr>
                                <td id="${b.uid}" title="${b.name}"><a href="${b.uri}">
                                    <db:shorten text="${b.display}" size="35"/>
                                </a></td>
                                <td class="numberColumn"><span class="count">${b.formattedCount}</span></td>
                            </tr>
                        </g:each>
                    </table>
                </div>
            </g:if>
            <g:if test="${dataProviders.size() > 7}">
                <p class="paragraph"><button id="moreDataProviderLink" class="btn btn-small moreLink">More</button></p>
            </g:if>
        </div>
    </div>
</div>