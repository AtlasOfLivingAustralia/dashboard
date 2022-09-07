<g:if test="${volunteerPortalCounts}">
    <div class="col-sm-4 col-md-4" id="bvp-topic">
        <div class="panel">
            <div class="panel-heading">
                <div class="panel-title">
                    <a target="_blank" href="https://volunteer.ala.org.au/">DigiVol <smaller>(Volunteer portal)</smaller></a>
                    <i class="fa fa-info-circle pull-right hidden"></i>
                </div>
            </div>
            <div class="panel-body">
                <table class="table table-condensed table-striped table-hover">
                    <tbody>
                        <tr>
                            <td>Specimen labels transcribed</td>
                            <td class="numberColumn"><span class="count"><db:formatNumber value="${volunteerPortalCounts?.specimens}"/></span></td>
                        </tr>
                        <tr>
                            <td>Fieldnotes pages transcribed</td>
                            <td class="numberColumn"><span class="count"><db:formatNumber value="${volunteerPortalCounts?.fieldnotes}"/></span></td>
                        </tr>
                        <tr>
                            <td>Volunteers</td>
                            <td class="numberColumn"><span class="count"><db:formatNumber value="${volunteerPortalCounts?.volunteerCount}"/></span></td>
                        </tr>
                        <tr>
                            <td>Expeditions active</td>
                            <td class="numberColumn"><span class="count"><db:formatNumber value="${volunteerPortalCounts?.activeExpeditionsCount}"/></span></td>
                        </tr>
                        <tr>
                            <td>Expeditions completed</td>
                            <td class="numberColumn"><span class="count"><db:formatNumber value="${volunteerPortalCounts?.completedExpeditionsCount}"/></span></td>
                        </tr>
                        <tr>
                            <td>Total expeditions</td>
                            <td class="numberColumn"><span class="count"><db:formatNumber  value="${volunteerPortalCounts?.expeditionCount}"/></span></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</g:if>
