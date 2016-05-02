<div class="span4" id="bhl-topic">
    <div class="panel">
        <div class="panel-heading">
            <div class="panel-title">
                <a target="_blank" href="http://biodiversitylibrary.org">Biodiversity heritage library</smaller></a>
                <i class="fa fa-info-circle pull-right hidden"></i>
            </div>
        </div>
        <div class="panel-body">
            <div class="text-center">
                <a target="_blank" href="http://bhl.ala.org.au/"><g:img dir="images/dashboard" file="bhl.png"/></a>
            </div>
            <table class="table table-condensed table-striped table-hover">
                <tbody>
                <tr><td>Pages</td><td class="numberColumn"><span class="count"><db:formatNumber value="${bhlCounts?.pages}"/></span></td>
                </tr>
                <tr><td>Volumes</td><td class="numberColumn"><span class="count"><db:formatNumber value="${bhlCounts?.volumes}"/></span></td>
                </tr>
                <tr><td>Titles</td><td class="numberColumn"><span class="count"><db:formatNumber value="${bhlCounts?.titles}"/></span></td>
                </tr>
                <tr><td>Publishers</td><td class="numberColumn"><span class="count"><db:formatNumber value="${bhlCounts?.publishers}"/></span></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>