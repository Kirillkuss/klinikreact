var protocol = window.location.protocol
var hostname = window.location.hostname;
var port = window.location.port;

function findByWordDoctor() {
    $(document.getElementById("findByWordDoctor")).on( "click",function(){
        var word = $('#wordFound').val();
        if(  word.length  == 0 ){
            $('#errorToast').text( 'Значение поля поиск не может быть пустым' ).show();
            $('#liveToastBtn').click();
        }else{
            $.ajax({
                type: "GET",
                contentType: "application/json; charset=utf-8",
                url: protocol + "//"+ hostname + ":" + port + "/react/doctors/find-fio/{word}{page}{size}",
                data:{ word: word, page: 1, size: 100 } ,
                cache: false,
                success: function( json ) {     
                    var tr=[];
                    for (var i = 0; i < json.length; i++) {
                        var rowNumber = 1 + i;
                        tr.push('<tr>');
                        tr.push('<td>' + rowNumber + '</td>');
                        tr.push('<td>' + json[i].surname + '</td>');
                        tr.push('<td>' + json[i].name + '</td>');
                        tr.push('<td>' + json[i].fullName + '</td>');
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
     * Ленивая загрука
     * @param {*} page - страница 
     * @param {*} size - размер
     */
function lazyDoctors( page, size) {
    $(document).ready(function() {
    $('table tbody').on('mousedown', 'tr', function(e) {
        $(this).addClass('highlight').siblings().removeClass('highlight');
    });
     $.get( protocol + '//'+ hostname + ':' + port +'/react/doctors/lazy/{page}{size}?page='+page+'&size='+size, function(json) {
        var tr=[];
        $('tbody').empty();
        var startIndex = (page - 1) * size + 1;
        for (var i = 0; i < json.length; i++) {
            var rowNumber = startIndex + i;
            tr.push('<tr>');
            tr.push('<td>' + rowNumber + '</td>');
            tr.push('<td>' + json[i].surname + '</td>');
            tr.push('<td>' + json[i].name + '</td>');
            tr.push('<td>' + json[i].fullName + '</td>');
            tr.push('</tr>');
            }
            $('table').append($(tr.join('')));
        });
    });
    };
/**
 * Добавить документ
 */

function AddDoctor() {
    $("#testFormDoctor").submit(function (event) {
        event.preventDefault();
        if (this.checkValidity() === false) {
            return;
        }

        var surname = $('#surname').val();
        var name = $('#name').val();
        var fullName = $('#fullName').val();
        
        $.ajax({
            type: "POST",
            contentType: "application/json; charset=utf-8",
            url: protocol + "//" + hostname + ':' + port + "/react/doctors/add",
            data: JSON.stringify({ surname: surname, name: name, fullName: fullName }),
            cache: false,
            success: function (json) {
                $('#exampleModal').modal('hide');
                $('.modal-backdrop').remove(); 
                $('#lastDoctor').click(); 
            },
            error: function (error) {
                const response = JSON.parse(error.responseText);
                $('#errorToast').text( response.message ).show();
                $('#liveToastBtn').click();
            }
           
        });
    });
}

        function getCountDoctor() {
            return new Promise((resolve, reject) => {
                $.getJSON(protocol + '//' + hostname + ':' + port + '/react/doctors/count')
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

        async function switchTable() {
            let totalDoctors = await getCountDoctor();
            let doctorsPerPage = 15;
            let totalPages = Math.ceil(totalDoctors / doctorsPerPage);
            let currentPage = 1;
            $('#currentPage').text(currentPage);
            updatePageNumbers(); 
    
            function updatePage() {
                $('tbody:even').empty();
                lazyDoctors(currentPage, doctorsPerPage);
                $('#currentPage').text(currentPage);
                updatePageNumbers(); 
            }
    
            function updatePageNumbers() {
                $('#pageNumbers').empty(); 
                let pageNumbers = [];
                pageNumbers.push(1);
                //pageNumbers.push('...');
                
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
                            //pageNumbers.push('...');
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
                        //pageNumbers.push('...');
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


/**
 * Обработчик события при нажатии на enter
 */
$(document).ready(function() {
    $('#findByWordDoctor').on("click", findByWordDoctor());
    $('#wordFound').on('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); 
            document.getElementById("findByWordDoctor").click();
        }
    });
});