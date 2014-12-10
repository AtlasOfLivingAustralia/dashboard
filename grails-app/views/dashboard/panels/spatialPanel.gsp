<div class="span4" id="spatial-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">
                <a href="http://spatial.ala.org.au/layers"><span class="count">${spatialLayers.total}</span></a> Spatial layers
                <i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body">
            <table class="table table-condensed table-striped table-hover">
                <tr><td>Contextual layers</td><td><span class="count">${spatialLayers.groups.contextual}</span></td>
                </tr>
                <tr><td>Environmental/grided layers</td><td><span
                        class="count">${spatialLayers.groups.environmental}</span></td></tr>
            </table>
            <table class="table table-condensed table-striped table-hover">
                <tr><td>Terrestrial layers</td><td><span class="count">${spatialLayers.groups.terrestrial}</span></td>
                </tr>
                <tr><td>Marine layers</td><td><span class="count">${spatialLayers.groups.marine}</span></td></tr>
            </table>

            <div id="moreSpatial" style="display:none;">
                <table class="table table-condensed table-striped table-hover">
                    <g:each in="${spatialLayers.classification}" var="c">
                        <tr><td>${c.key}</td><td><span class="count">${c.value}</span></td></tr>
                    </g:each>
                </table>
            </div>

            <p style="padding-top: 10px;"><span id="moreSpatialLink" class="link">more..</span></p>
        </div>
    </div>
</div>