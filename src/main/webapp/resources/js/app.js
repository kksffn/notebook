(function($) {
    /**
     * INPUT FORM
     * @type {*|jQuery|HTMLElement}
     */

    var form = $('#add-form'),
        input = $('#text'), //v input elementu je takový, který má id="text"
        deadline = $('#datetime'),
        list = $('#item-list');
    var d = new Date($.now());
    d = (d.getDate()+"."+(d.getMonth() + 1)+"."+d.getFullYear()+" "+
        d.getHours()+":"+d.getMinutes()+":"+d.getSeconds());


    input.focus();

    /**
     * SETTINGS
     * object animation
     */
    var animation = {
        startColor: '#00bc8c',
        endColor: list.find('li').css('backgroundColor') || '#303030',
        //Zkus najít backgroundColor pro li elementy v list, pokud nenajdeš, použij #303030
        delay: 200
    }
    form.on('submit', function (event) {    //Pokud uživatel potvrdí formulář
        event.preventDefault();     // nedělej akci, kterou bys normálně dělal :) (neodesílej na novou adresu)

        var request = $.ajax({
            url: form.attr('action'), //Ve formuláři v atributu action máš napsané, co máš dělat :)
            type: 'POST',
            data: form.serialize(), //použij k tomu data z formuláře - vytvoř z nich objekt a ten odešli
            dataType: 'json' //Očekáváme, že se vrátí json
        });

        request.done(function(data) {       //Když je request done,
            if ( data.status === 'success') {   //v data.status je uloženo 'success',
                $.ajax( {url: baseURL}).done(function(html){ //vrať html kód domovské stránky
                    // var newItem = $(html).find('li:last-child');  //a v něm vyhledej poslední li element

                    list.find('#item-0').hide();
                    var newItem = $(html).find('#item-' + data.id); //nebo pomocí json :)

                    newItem.appendTo( list ) //Připoj newItem k seznamu
                        .css({backgroundColor: animation.startColor})
                        .delay(animation.delay)
                        .animate({backgroundColor: animation.endColor});
                    // Nastav hodnotu input na prázdný řetězec, aby v poli nezůstávala odeslaná hodnota!
                    input.val('').focus(); //a dej mu focus
                    deadline.val(d);
                });
            }
        })
    });

    input.on('keypress', function( event) {
        if ( event.which === 13) {   //Pokud je zmáčknutý enter
            form.submit();
            return false; //Zabrání odeslání enter s textem
        }
    });

    /**
     * EDIT FORM
     */
    $('#edit-form').find('#text').select(); //Při načtení edit formuláře se rovnou označí celý text


    /**
     * DELETE FORM
     */
    $('#delete-form').on('submit', function (event) {
        return confirm('Are you sure you want to delete this item?')
    });


}(jQuery));