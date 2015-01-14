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
                    <tbody>
                    <tr><td>Taxa with images</td><td class="numberColumn">${g.formatNumber(number: imagesBreakdown["taxaWithImages"], type: 'number')}</td></tr>
                    <tr><td>Species with images</td><td class="numberColumn">${g.formatNumber(number: imagesBreakdown["speciesWithImages"], type: 'number')}</td></tr>
                    <tr><td>Subspecies with images</td><td class="numberColumn">${g.formatNumber(number: imagesBreakdown["subspeciesWithImages"], type: 'number')}</td></tr>
                    <tr><td>Taxa with images from<br/> DigiVol
                    </td><td class="numberColumn">${g.formatNumber(number: imagesBreakdown["taxaWithImagesFromVolunteerPortal"], type: 'number')}</td></tr>
                    <tr><td>Taxa with images from<br/> citizen science
                    </td><td class="numberColumn">${g.formatNumber(number: imagesBreakdown["taxaWithImagesFromCS"], type: 'number')}</td></tr>
                    </tbody>
                    <tfoot>
                    <tr class="total-highlight"><td>Total number of images</td><td class="numberColumn">${g.formatNumber(number: imagesBreakdown["imageTotal"], type: 'number')}</td></tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</div>