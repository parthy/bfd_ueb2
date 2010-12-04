// JavaScript Document

jQuery('.logintoggler').removeClass("offscreen");
jQuery('.opal').addClass("offscreen");
jQuery('.hochschule .logintoggler').click(function(){
		jQuery('.opal').removeClass("offscreen");
		jQuery('.hochschule').addClass("offscreen");
		jQuery('.opal').focus();
	});
	
jQuery('.opal .logintoggler').click(function(){
		jQuery('.hochschule').removeClass("offscreen");
		jQuery('.opal').addClass("offscreen");
		jQuery('.hochschule').focus();
	});