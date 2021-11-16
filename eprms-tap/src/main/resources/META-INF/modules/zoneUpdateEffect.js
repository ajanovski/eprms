define([ "jquery", "t5/core/dom", "t5/core/events" ], function($, dom, events) {

	$('*[data-container-type="zone"]').on(events.zone.didUpdate, function() {
		$(this).css("animation", "0.3s linear slidein");
	});

	$('*[data-container-type="zone"]').on("animationend", function() {
		$(this).css("animation", "none");
		$(this).offsetHeight;
		$(this).css("animation", "null");
	});

	//return null;
});
