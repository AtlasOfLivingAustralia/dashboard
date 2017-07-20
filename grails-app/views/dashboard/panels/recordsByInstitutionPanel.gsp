<div class="col-sm-4 col-md-4" id="institutions-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">Records by institution<i class="fa fa-info-circle pull-right hidden"></i></div>
        </div>
        <div class="panel-body">
            <table class="table table-condensed table-striped table-hover">
                <g:each in="${institutions[0..Math.min(6, institutions.size() - 1)]}" var="b">
                    <tr>
                        <td id="${b.uid}" title="${b.name}">
                            <g:if test="${b.uri}"><a href="${b.uri}"><db:shorten text="${b.display}" size="35"/></a></g:if>
                            <g:else><db:shorten text="${b.display}" size="35"/></g:else>
                        </td>
                        <td class="numberColumn"><span class="count">${b.formattedCount}</span></td>
                    </tr>
                </g:each>
            </table>
            <g:if test="${institutions.size() > 7}">
                <div id="moreInstitution" class="initiallyHidden">
                    <table class="table table-condensed table-striped table-hover">
                        <g:each in="${institutions[7..-1]}" var="b">
                            <tr>
                                <td id="${b.uid}" title="${b.name}">
                                    <g:if test="${b.uri}"><a href="${b.uri}"><db:shorten text="${b.display}" size="35"/></a></g:if>
                                    <g:else><db:shorten text="${b.display}" size="35"/></g:else>
                                </td>
                                <td class="numberColumn"><span class="count">${b.formattedCount}</span></td>
                            </tr>
                        </g:each>
                    </table>
                </div>
            </g:if>
            <g:if test="${institutions.size() > 7}">
                <p class="paragraph"><button id="moreInstitutionLink" class="btn btn-default btn-small moreLink">More</button></p>
            </g:if>
        </div>
    </div>
</div>