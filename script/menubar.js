$(document).ready(function(event) {

    // key code definitions
    var KEY_TAB   = 9;
    var KEY_ENTER = 13;
    var KEY_ESC   = 27;
    var KEY_SPACE = 32;
    var KEY_LEFT  = 37;
    var KEY_UP    = 38;
    var KEY_RIGHT = 39;
    var KEY_DOWN  = 40;
	
    var g_closetimer = null; // document timer object
    var g_openMenu = null; // current open menu object
    var g_tabOut = false; // IE bugFix flag. True if tabbing out of menu

    /**
     * hoverOn() is a handler for menu hover events. called on mouseenter
     *
     * @param none
     *
     * @return N/A
     */
    function hoverOn() {
        $(this).css('background-color', 'black');
    }

    /**
     * closeMenuHelper() is a helper function for the mouseover
     * and mouseout event handlers. Also used as callback for
     * the menu close timer.
     *
     * @param none
     *
     * @return N/A
     */
    function closeMenuHelper() {
        
        if (g_openMenu) {
            g_openMenu.hide();
        }
    }

    /**
     * menubar mouseover handler
     */
    $('ul.menubar').children('li').mouseover(function(e) {

        // cancel the menu close timer
        if (g_closetimer) {
            window.clearTimeout(g_closetimer);
            g_closetimer = null;
        }

        // if an item in the open menu has focus, return
        // focus to the menu itself
        if (g_openMenu != null && g_openMenu.find('li').hasClass('focusHighlight') == true) {
            g_openMenu.parent().focus();
        }

        closeMenuHelper();
        g_openMenu = $(this).find('ul').css('display', 'block');

        e.stopPropagation;
        return false;
    }); // end menubar mouseover handler

    /**
     * menubar mouseout handler
     */
    $('ul.menubar').children('li').mouseout(function(e) {

        // set menu close timer. Delay is in milliseconds
        g_closetimer = window.setTimeout(closeMenuHelper, 100);

        e.stopPropagation;
        return false;
    }); // end menubar mouseout handler

    /**
     * menubar click handler
     */
    $('ul.menubar').children('li').click(function(e) {

        var sibs = $(this).siblings(); // stored objects of sibling menus

        // Toggle the menu and set focus
        $(this).children('ul').toggle();

        if ($(this).children('ul').is(':visible')) {

            // remove highlight from menu items
            $(this).find('li').each(function() {
                removeHighlight(this);
            });

            $(this).find('li:first').focus();

        }
        else {
            $(this).focus();
        }
		//if link
		
		var href = $(this).children()[0];
		
        window.location.href = href;
        // remove the highlight from other menus
        $(this).siblings().each(function() {
            removeHighlight(this);
        });

        e.stopPropagation;
        return false;
    }); // end menubar click handler

    /*
     * menubar keydown handler
     */
    $('ul.menubar').children('li').keydown(function(e) {

        var sibs = $(this).siblings(); // stored objects of sibling menus
        var selectedMenu; // currently selected menu

        if (e.altKey || e.ctrlKey || e.shiftKey) {
            // Modifier key pressed: Do not process
            return true;
        }

        switch (e.keyCode) {
            case KEY_TAB: {
                // Make sure the menu is hidden
                $(this).children('ul').hide();

                // remove the menubar highlight
                removeHighlight(this);

                return true;
                break;
            }
            case KEY_ESC: {
                // Make sure the menu is hidden
                $(this).children('ul').hide();

                break;
            }
            case KEY_UP:
            case KEY_DOWN: {

                // Show the menu
                $(this).children('ul').show();

                // Store this menu for the mouseover helper
                g_openMenu = $(this).children('ul');

                // Hide other menus
                sibs.children('ul').hide();

                // remove highlight from menu items
                $(this).find('li').each(function() {
                    removeHighlight(this);
                });

                // set focus on the first submenu item
                $(this).find('li:first').focus();

                break;
            }
			
			case KEY_SPACE:
			case KEY_ENTER: {
				$(this).trigger('click');
			}
			
            case KEY_LEFT: {
                // select the previous menu
                selectedMenu = $(this).prev();

                // if there is no previous menu, select the last one
                if( !selectedMenu.length ) {
                    selectedMenu = $(this).parent().children('li:last');
                } // endif

                // Set focus to new menu
                selectedMenu.focus();

                // Remove the highlight from the current menu
                removeHighlight(this);

                break;
            }
            case KEY_RIGHT: {
                // select the next menu
                selectedMenu = $(this).next();

                // if there is no next menu, select the first one
                if( !selectedMenu.length ) {
                    selectedMenu = $(this).parent().children('li:first');
                } // endif

                // Set focus to new menu
                selectedMenu.focus();
				
                // Remove the highlight from the current menu
                removeHighlight(this);

                break;
            }
            default: {
                return true;
            }
        } // end switch

        e.stopPropagation;
        return false;
    }); // end top menu key handler

    /**
     * menu mouseover handler
     */
    $('ul.menubar ul').children('li').mouseover(function(e) {

        if (g_closetimer) {
            window.clearTimeout(g_closetimer);
            g_closetimer = null;
        }

        e.stopPropagation;
        return false;
    }); // end menu mouseover handler

    /**
     * menu mouseout handler
     */
    $('ul.menubar ul').children('li').mouseout(function(e) {
        var parentMenu = $(this).parent().parent();

        // set menu close timer. Delay is in milliseconds
        g_closetimer = window.setTimeout(closeMenuHelper, 100);

        if($(this).parent().children('li').hasClass('focusHighlight') == true) {
            // An element of the menu this item belongs to has focus, so
            // return the focus to the parent menu item
            parentMenu.focus();
        }

        e.stopPropagation;
        return false;
    }); // end menu mouseout handler

    /*
     * menu click handler
     */
    $('ul.menubar ul').children('li').click(function(e) {

        var menuName = $(this).parent().attr('id');
        var href = $(this).children()[0];
        var menuItem = $(this).attr('id');
        	
        // Set focus to the controlled element
        $('#' + $(this).attr('aria-controls')).focus();

        // Remove highlight from the menu
        $('ul.menubar li').each(function() {
            removeHighlight(this)
        });
	
        // close the menu
        $(this).parent().hide();

        window.location.href = href;

    e.stopPropagation;
        return false;
    }); // end menu click handler

    /*
     * Menu keydown handler
     */
    $('ul.menubar ul').children('li').keydown(function(e) {
            
        var selectedItem; //currently selected menu item
        var parentMenuItem = $(this).parent().parent();

        switch (e.keyCode) {
            case KEY_TAB: {

                // hide the menu
                $(this).parent().hide();

        // remove the highlight from menu item and parent menu item
        removeHighlight(this);
        removeHighlight(parentMenuItem);


        // IE8 causes the focus event to fire for containing objects.
        // Set a flag to prevent the highlight class from being re-applied
        g_tabOut = true;

        return true;
                break;
            }
            case KEY_ESC: {
                $(this).parent().parent().focus();
                $(this).parent().hide();

                return true;
                break;
            }
            case KEY_ENTER:
            case KEY_SPACE:    {

                // trigger submenu click handler
                $(this).trigger('click');

                break;
            }
            case KEY_UP: {
                // select the previous menu item
                selectedItem = $(this).prev();

                // if there is no previous menu item, select the last one
                if( !$(selectedItem).length ) {
                    selectedItem = $(this).parent().children('li:last');
                } // endif

                if ($(selectedItem).attr('role') == 'separator') {
                    // skip seperators -- assumes seperator is not
                    // first menu item
                    selectedItem = $(selectedItem).prev();
                }

                // Set focus to new menu item
                selectedItem.focus();

                // Remove the highlight from the current menu item
                removeHighlight(this);

                break;
            }
            case KEY_DOWN: {
                // select the next menu item
                selectedItem = $(this).next();

                // if there is no next menu item, select the first one
                if( !$(selectedItem).length ) {
                    selectedItem = $(this).parent().children('li:first');
                } // endif

                if ($(selectedItem).attr('role') == 'separator') {
                    // skip seperators -- assumes seperator is not
                    // last menu item
                    selectedItem = $(selectedItem).next();
                }

                // Set focus to new menu item
                selectedItem.focus();

                // Remove the highlight from the current menu item
                removeHighlight(this);

                break;
            }
            case KEY_LEFT: {
                // select the prev menu
                selectedItem = $(this).parents('li').prev();

                // if there is no prev menu, select the last one
                if( !$(selectedItem).length ) {
                    selectedItem = parentMenuItem.parent().children('li:last');
                    
                } // endif

                // Show the new menu and select the first item
                selectedItem.children('ul').show();
                selectedItem.find('li:first').focus();

                // Hide the current menu and remove the highlights
                $(this).parent().hide();
                removeHighlight(this);
                removeHighlight(parentMenuItem);

                break;
            }
            case KEY_RIGHT: {
                // select the next menu
                selectedItem = $(this).parents('li').next();

                // if there is no next menu, select the first one
                if( !$(selectedItem).length ) {
                    selectedItem = parentMenuItem.parent().children('li:first');
                    
                } // endif

                selectedItem.children('ul').show();
                selectedItem.find('li:first').focus();

                // Hide the current menu and remove the highlights
                $(this).parent().hide();
                removeHighlight(this);
                removeHighlight(parentMenuItem);

                break;
            }
            default: {
                return true;
            }
        } // end switch

        e.stopPropagation;
        return false;

    }); // end menu keydown handler

    /**
     * menu keypress event handler (attaches to all menubar and menu items)
     *
     * The Opera browser performs some window commands from the keypress event,
     * not keydown like Firefox, Safari, and IE. This event handler consumes
     * keypresses for relevant keys so that Opera behaves when the user is
     * manipulating the menu with the keyboard.
     */
    $('ul.menubar li').keypress(function(e) {

    switch (e.keyCode) {
        case KEY_UP:
        case KEY_DOWN:
        case KEY_ESC:
		{
		e.preventDefault;
                return false;
            break;
        }
    }
    return true;
    }); // end keypress handler

    /*
     * menu focus handler (attaches to all menubar and menu items)
     */
    $('ul.menubar li').focus(function(e) {

    // Ignore the focus event if the g_tabOut flag is set
    // (IE8 workaround for tab key events)
    // 
    if (g_tabOut == false) {
            $(this).addClass('focusHighlight');
    }
    else {
        // reset the flag
        g_tabOut = false;
    }

        return true;
    }); // end menu focus handler

    /*
     * removeHighlight() function to remove the highlight from a menu item
     *
     * @param (item object) menu item object to remove highlight from
     *
     * @return N/A
     */
    function removeHighlight(item) {
        $(item).removeClass('focusHighlight');
    }; // end removeHighlight function

    /*
     * document click function to close menu if user clicks elsewhere on document
     * Note: click events consumed by the menu will not reach this handler
     */
    $(document).click(function(e) {
        // hide all the menus
        $('ul.menubar ul').hide();

        $('ul.menubar li').each(function () {
            removeHighlight(this);
        });
    }); // end document click handler


}); // end document ready

