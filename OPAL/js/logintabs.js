// Einblenden von Elementen, die nur mit aktiviertem JavaScript sinnvoll und notwendig sind	
$('#login').show();
$('.jshidden').show();

// Hinzufügen des Hilfetextes für den Screenreader
$('<p class="offscreen">Wenn ein Tab ausgewählt ist, können Sie die Pfeiltasten und Enter oder Leertaste benutzen, um den Login zu wechseln.</p>').insertAfter('.tabs');

// nur ausgewählter Tab in Taborder des Browsers, um schneller über Tab-Widget tabben zu können
$('.tabs li').attr('tabindex', '-1');
$('.tabs li.selected').attr('tabindex', '0');

// Ausblenden der Tabpanels
$('.panels div').hide();
$('.panels div').attr('aria-hidden', 'true');

//Einblenden des zum ausgewählten Tab gehörenden Panels
$('#'+$('.tabs li.selected').attr('aria-controls')).show();
$('#'+$('.tabs li.selected').attr('aria-controls')).attr('aria-hidden', 'false');

// Tastatur-Navigation zwischen den Tab-Reitern				
$('.tabs li').keydown(function(event) {
	switch(event.keyCode){
		case 37	:	$(this).prev().focus();
					break;
					
		case 39	:	$(this).next().focus();
					break;
		
		case 32 :	$(this).trigger('click');
					break;
					
		case 13:	$(this).trigger('click');
					break;
	}
});

// Klick auf Tab		
$('.tabs li').click(function(){
	// Hervorhebung des Tabs
	$('.tabs li').removeClass('selected');
	$(this).addClass('selected');
	
	// Tabindex aktualisieren
	$('.tabs li').attr('tabindex', '-1');
	$(this).attr('tabindex', '0');
	
	// Panel anzeigen
	$('.panels div').attr('aria-hidden', 'true');
	$('#'+$(this).attr('aria-controls')).attr('aria-hidden', 'false');
	$('.panels div').hide();
	$('#'+$(this).attr('aria-controls')).show();
	
	// Vertauschen der Reihenfolge, damit der Screenreader auch liest
	$($('.panels div').not($('#'+$(this).attr('aria-controls')))).before($('#'+$(this).attr('aria-controls')));
});