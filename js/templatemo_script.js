/*
 * Template Script - NanosLand Dashboard
 * Basado en: http://www.templatemo.com
 */

$(document).ready( function() {

	// Manejo del click en submenús del sidebar
	// Alterna entre abrir y cerrar el submenú de eventos
	$('.templatemo-sidebar-menu li.sub a').click(function(){
		if($(this).parent().hasClass('open')) {
			$(this).parent().removeClass('open');
		} else {
			$(this).parent().addClass('open');
		}
	});

}); // document.ready
