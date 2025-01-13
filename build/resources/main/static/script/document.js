var protocol = window.location.protocol
var hostname = window.location.hostname;
var port = window.location.port;

/**
 * Поиск документов по параметрам
 */
function findByWordDocument() {
    $(document.getElementById("findByWordDocument")).on( "click",function(){
        var word = $('#wordFound').val();
        if(  word.length  == 0 ){
            $('#errorToast').text( 'Значение поля поиск не может быть пустым' ).show();
            $('#liveToastBtn').click();
        }else{
            $.ajax({
                type: "GET",
                contentType: "application/json; charset=utf-8",
                url: protocol + "//"+ hostname + ':' + port + "/react/documents/find/{word}",
                data:{ word: word } ,
                cache: false,
                success: function( json ) {
                    var tr=[];
                    for (var i = 0; i < json.length; i++) {
                        var rowNumber = 1 + i;
                        tr.push('<tr>');
                        tr.push('<td>' + rowNumber + '</td>');
                        tr.push('<td>' + json[i].typeDocument + '</td>');
                        tr.push('<td>' + json[i].seria + '</td>');
                        tr.push('<td>' + json[i].numar + '</td>');
                        tr.push('<td>' + json[i].snils + '</td>');
                        tr.push('<td>' + json[i].polis + '</td>');
                        tr.push('</tr>');
                    }
                    $('tbody:even').empty();
                    $('table').prepend($(tr.join('')));
                }, error: function ( error ){
                    const response = JSON.parse(error.responseText);
                    $('#errorToast').text( response.message ).show();
                    $('#liveToastBtn').click();
                }
            });
        }

    });	
};

/**
 * Обработчик события при нажатии на enter
 */
$(document).ready(function() {
    $('#findByWordDocument').on("click", findByWordDocument());
    $('#wordFound').on('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); 
            document.getElementById("findByWordDocument").click();
        }
    });
});

/**
 * Добавить документ
 */
function AddDocument() {
    $("#testFormDocument").submit( function (event){
        event.preventDefault();
        var idDocument =  $('#idDocument').val();
        var typeDocument = $('#typeDocument').val();
        var seria = $('#seria').val();
        var numar = $('#numar').val();
        var snils = $('#snils').val();
        var polis = $('#polis').val();
            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                url: "http://" + hostname + ':' + port + "/react/documents/add",
                data: JSON.stringify({typeDocument: typeDocument, seria: seria, numar: numar, snils: snils, polis: polis}),
                cache: false,
                success: function( json ) {
                    $('#exampleModal').modal('hide');
                    $('.modal-backdrop').remove(); 
                    $('#last').click();
                }, error: function ( error ){
                    console.log( error.responseText );
                    const response = JSON.parse(error.responseText);
                    if ( response.code === 500 ){
                        const messageMatch = response.message.match(/'([^']+)'/);
                        $('#errorToast').text(messageMatch[1]).show();
                        $('#liveToastBtn').click();
                    }else{
                        $('#errorToast').text( response.message ).show();
                        $('#liveToastBtn').click();
                    }

                }
            });
        });
    };
    /**
     * Ленивая загрука
     * @param {*} page - страница 
     * @param {*} size - размер
     */
    function lazyDocument( page, size) {
        $(document).ready(function() {
        $('table tbody').on('mousedown', 'tr', function(e) {
            $(this).addClass('highlight').siblings().removeClass('highlight');
        });
        $.getJSON(protocol + '//'+ hostname + ':' + port + '/react/documents/lazy/{page}{size}?page='+page+'&size='+size, function(json) {
            var tr=[];
            $('tbody').empty();
            var startIndex = (page - 1) * size + 1;
            for (var i = 0; i < json.length; i++) {
                var rowNumber = startIndex + i;
                tr.push('<tr>');
                tr.push('<td>' + rowNumber + '</td>');
                tr.push('<td>' + json[i].typeDocument + '</td>');
                tr.push('<td>' + json[i].seria + '</td>');
                tr.push('<td>' + json[i].numar + '</td>');
                tr.push('<td>' + json[i].snils + '</td>');
                tr.push('<td>' + json[i].polis + '</td>');
                tr.push('</tr>');
                }
                $('table').append($(tr.join('')));
            });
        });
    };

    function getCountDocuments() {
        return new Promise((resolve, reject) => {
            $.getJSON(protocol + '//' + hostname + ':' + port + '/react/documents/count')
                .done(function(json) {
                    var count = json;
                    resolve(count); 
                })
                .fail(function(error) {
                    console.error("Ошибка при получении данных:", error);
                    reject(error); 
                });
        });
    }

    /**
     * Нумерация страниц
     */
    async function switchTable() {
        let totalDocuments = await getCountDocuments();
        let documentsPerPage = 15;
        let totalPages = Math.ceil(totalDocuments / documentsPerPage);
        let currentPage = 1;
        $('#currentPage').text(currentPage);
        updatePageNumbers(); 

        function updatePage() {
            $('tbody:even').empty();
            lazyDocument(currentPage, documentsPerPage);
            $('#currentPage').text(currentPage);
            updatePageNumbers(); 
        }

        function updatePageNumbers() {
            $('#pageNumbers').empty(); 
            let pageNumbers = [];
            pageNumbers.push(1);
            pageNumbers.push('...');
            
            if ( currentPage === (totalPages - 1) ){
                pageNumbers.push( currentPage - 2 );
            }
            if (totalPages > 4) {
                for ( let page = Math.max( 2, currentPage - 1 ); page <= Math.min( currentPage + 1, totalPages - 1 ); page++ ) {
                   if( currentPage === totalPages){
                        pageNumbers.push( currentPage - 3 );
                        pageNumbers.push( currentPage - 2 );
                   }
                    pageNumbers.push(page);
                    if( (totalPages - 1) === page ){
                        pageNumbers.push('...');
                    }
                    if( currentPage === 1){
                        pageNumbers.push(currentPage + 2 );
                        pageNumbers.push(currentPage + 3 );
                    }
                }
                if( currentPage === 2){
                    pageNumbers.push(currentPage + 2 );
                }
                if (currentPage < totalPages - 2) {
                    pageNumbers.push('...');
                }
                pageNumbers.push(totalPages);
            } else {
                for (let page = 2; page <= totalPages; page++) {
                    pageNumbers.push(page);
                }
            }
    
            for (const page of pageNumbers) {
                $('#pageNumbers').append(
                    `<a class="btn btn-outline-dark page-btn" href="#" data-page="${page}">${page}</a>`
                );
            }
    
            $('.page-btn').on("click", function () {
                let pageNum = $(this).data("page");
                if (pageNum !== "...") {
                    currentPage = parseInt(pageNum);
                    updatePage();
                }
            });
        }
        await updatePage();
    }
    

    function listEM() {
        $(document).ready(function() {
        $('table tbody').on('mousedown', 'tr', function(e) {
            $(this).addClass('highlight').siblings().removeClass('highlight');
        });
        $.getJSON('http://127.0.0.1:8080/rest/api/entitymanager', function(json) {
            console.log(json.data );
            var tr=[];
            for (var i = 0; i < json.data.length; i++) {
                tr.push('<tr>');
                tr.push('<td>' + json.data[i].idUser + '</td>');
                tr.push('<td>' + json.data[i].name + '</td>');
                tr.push('<td>' + json.data[i].login + '</td>');
                tr.push('<td>' + json.data[i].phone + '</td>');
                tr.push('<td>' + json.data[i].wallet + '</td>');
                tr.push('</tr>');
                }
                $('table').append($(tr.join('')));
            });
        });
    };