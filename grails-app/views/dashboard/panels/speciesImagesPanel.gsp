<div class="span4" id="images-breakdown-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">
                Species images&nbsp;&nbsp;
                <span class="count"><db:formatNumber value="${imagesBreakdown["speciesWithImages"]}"/></span>
                <i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body">
            <div id="imagesBreakdown">
                <table class="table table-condensed table-striped table-hover">
                    <tr><td>Taxa with images</td><td>${imagesBreakdown["taxaWithImages"]}</td></tr>
                    <tr><td>Species with images</td><td>${imagesBreakdown["speciesWithImages"]}</td></tr>
                    <tr><td>Subspecies with images</td><td>${imagesBreakdown["subspeciesWithImages"]}</td></tr>
                    <tr><td>Taxa with images from<br/> DigiVol
                    </td><td>${imagesBreakdown["taxaWithImagesFromVolunteerPortal"]}</td></tr>
                    <tr><td>Taxa with images from<br/> citizen science
                    </td><td>${imagesBreakdown["taxaWithImagesFromCS"]}</td></tr>
                    <tr><td>Total number of images</td><td>${imagesBreakdown["imageTotal"]}</td></tr>
                </table>
            </div>
        </div>
    </div>
</div>