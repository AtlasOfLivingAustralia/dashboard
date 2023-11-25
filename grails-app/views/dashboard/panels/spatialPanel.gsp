<div class="col-sm-4 col-md-4" id="spatial-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">
                <a href="${grailsApplication.config.spatial.baseURL}/layers/index"><span class="count">${spatialLayers.total}</span>
                </a> Spatial layers
                <i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>

        <div class="panel-body">
            <table class="table table-condensed table-striped table-hover">
                <tr>
                    <td width="80%">Contextual layers</td>
                    <td class="numberColumn"><span class="count">${spatialLayers.groups.contextual}</span></td>
                </tr>
                <tr>
                    <td>Environmental/grided layers</td>
                    <td class="numberColumn"><span class="count">${spatialLayers.groups.environmental}</span></td>
                </tr>
            </table>
            <table class="table table-condensed table-striped table-hover">
                <tr>
                    <td width="80%">Terrestrial layers</td>
                    <td class="numberColumn"><span class="count">${spatialLayers.groups.terrestrial}</span></td>
                </tr>
                <tr>
                    <td>Marine layers</td>
                    <td class="numberColumn"><span class="count">${spatialLayers.groups.marine}</span></td>
                </tr>
            </table>

            <div id="moreSpatial" style="display:none;">
                <table class="table table-condensed table-striped table-hover">
                    <g:each in="${spatialLayers.classification}" var="c">
                        <tr><td>${c.key}</td><td class="numberColumn"><span class="count">${c.value}</span></td></tr>
                    </g:each>
                </table>
            </div>

            <p class="paragraph"><button id="moreSpatialLink" class="btn btn-default btn-small">More</button></p>
        </div>
    </div>
</div>
