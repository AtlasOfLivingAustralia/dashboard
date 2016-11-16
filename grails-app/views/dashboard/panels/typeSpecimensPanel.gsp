<div class="span4" id="typeStatus-topic">
  <div class="panel">
    <div class="panel-heading">
      <div class="panel-title">
        <a href="http://biocache.ala.org.au/occurrences/search?q=type_status:[*%20TO%20*]&fq=-type_status:notatype">
          <span class="count"><db:formatNumber value="${typeCounts.total}"/></span>
        </a>
        Type specimens
        <i class="fa fa-info-circle pull-right hidden"></i>
      </div>
    </div>
    <div class="panel-body">
      <div id="baseTypes">
        <table class="click-thru table table-condensed table-striped table-hover">
          <tbody>
            <tr><td width="70%" id="holotype">Holotypes</td><td class="numberColumn"><span class="count">${g.formatNumber(number: typeCounts.holotype, type: 'number')}</span></td></tr>
            <tr><td id="lectotype">Lectotypes</td><td class="numberColumn"><span class="count">${g.formatNumber(number: typeCounts.lectotype, type: 'number')}</span></td>
            </tr>
            <tr><td id="neotype">Neotypes</td><td class="numberColumn"><span class="count">${g.formatNumber(number: typeCounts.neotype, type: 'number')}</span></td></tr>
            <tr><td id="isotype">Isotypes</td><td class="numberColumn"><span class="count">${g.formatNumber(number: typeCounts.isotype, type: 'number')}</span></td></tr>
          </tbody>
        </table>
        <table class="click-thru table table-condensed table-striped table-hover">
          <tbody>
            <tr><td width="70%">Types with images</td><td class="numberColumn"><span class="count">${g.formatNumber(number: typeCounts.withImage?.total, type: 'number')}</span></td></tr>
          </tbody>
        </table>

      </div>

      <div id="moreTypes" style="display:none;">
        <table class="click-thru table table-condensed table-striped table-hover">
          <g:each in="${typeCounts}" var="c">
            <g:if test="${!(c.key in ['total', 'withImage']) && c.key.length() > 1}">
              <tr><td id="${c.key}">${c.key[0].toUpperCase() + c.key[1..-1]}</td><td><span
                      class="count">${c.value}</span></td></tr>
            </g:if>
          </g:each>
        </table>
        <table class="click-thru table table-condensed table-striped table-hover">
          <g:each in="${typeCounts.withImage}" var="c">
            <g:if test="${c.key != 'total' && c.key.length() > 1}">
              <tr><td id="${'image' + c.key}">${c.key[0].toUpperCase() + c.key[1..-1] + ' with image'}</td><td>
                <a href="http://biocache.ala.org.au/occurrences/search?q=type_status%3A%5B*+TO+*%5D&fq=-type_status%3Anotatype&fq=multimedia:Image"></a>
                <span class="count">${c.value}</span>
              </a>
              </td></tr>
            </g:if>
          </g:each>
        </table>
      </div>

      <p class="paragraph"><button id="moreTypesLink" class="btn btn-small">More</button></p>
    </div>
  </div>
</div>