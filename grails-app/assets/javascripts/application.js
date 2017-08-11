// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
// require jquery.js
//= require jquery-ui-1.11.2.js
// require jquery-migrate-1.2.1.min.js
//= require jquery.ui.touch-punch-0.2.3.min.js
//= require jquery.cookie.js
//= require jquery.jstree.js
//= require jquery.matchHeight.js
//= require taxonTree.js
//= require markdown.js
//= require dashboard.js
//= require_tree .
//= require_self

if (typeof jQuery !== 'undefined') {
	(function($) {
		$('#spinner').ajaxStart(function() {
			$(this).fadeIn();
		}).ajaxStop(function() {
			$(this).fadeOut();
		});
	})(jQuery);
}
