<g:if test="${volunteerPortalCounts}">
    <div class="span4" id="bvp-topic">
        <div class="panel">
            <div class="panel-heading">
                <div class="panel-title">
                    <a target="_blank" href="http://volunteer.ala.org.au/">DigiVol <smaller>(Volunteer portal)</smaller></a>
                    <i class="fa fa-info-circle pull-right hidden"></i>
                </div>
            </div>
            <div class="panel-body">
                <table class="table table-condensed table-striped table-hover">
                    <tbody>
                    <tr><td>Specimen labels transcribed</td><td><span class="count"><db:formatNumber
                            value="${volunteerPortalCounts?.specimens}"/></span></td></tr>
                    <tr><td>Fieldnotes pages transcribed</td><td><span class="count"><db:formatNumber
                            value="${volunteerPortalCounts?.fieldnotes}"/></span></td></tr>
                    <tr><td>Volunteers</td><td><span class="count"><db:formatNumber
                            value="${volunteerPortalCounts?.volunteerCount}"/></span></td></tr>
                    <tr><td>Expeditions active</td><td><span class="count"><db:formatNumber
                            value="${volunteerPortalCounts?.activeExpeditionsCount}"/></span></td></tr>
                    <tr><td>Expeditions completed</td><td><span class="count"><db:formatNumber
                            value="${volunteerPortalCounts?.completedExpeditionsCount}"/></span></td></tr>
                    <tr><td>Total expeditions</td><td><span class="count"><db:formatNumber
                            value="${volunteerPortalCounts?.expeditionCount}"/></span></td></tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</g:if>

<g:if test="${false && volunteerPortalCounts}">
    <div class="span4" id="bvp-topic">
        <div class="panel">
            <div class="panel-heading">
                <div class="panel-title"><a target="_blank" href="http://volunteer.ala.org.au/">DigiVol - top volunteers</a><i class="fa fa-info-circle pull-right hidden"></i></div>
            </div>
            <div class="panel-body">
                <table class="click-thru table table-condensed table-striped table-hover">
                    <tbody>
                    <g:each in="${volunteerPortalCounts.topTenVolunteers.take(7)}" var="volunteer">
                        <tr><td>${volunteer[0]}</td><td><span class="count"><db:formatNumber
                                value="${volunteer[1]}"/></span></td></tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</g:if>