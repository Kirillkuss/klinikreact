var protocol = window.location.protocol
var hostname = window.location.hostname;
var port = window.location.port;
/**
 * Ленивый поиск карт пациентов
 * @param {*} page - страница
 * @param {*} size - размер
 */
function lazyCard( page, size) {
    $(document).ready(function() {
    $('table tbody').on('mousedown', 'tr', function(e) {
        $(this).addClass('highlight').siblings().removeClass('highlight');
    });
    $.getJSON( window.location.protocol + '//'+ hostname + ':' + port +'/react/card-patients/lazy/{page}{size}?page='+page+'&size='+size, function(json) {
        var tr=[];
        $('tbody').empty();
        var startIndex = (page - 1) * size + 1;
        for (var i = 0; i < json.length; i++) {
            var rowNumber = startIndex + i;
            tr.push('<tr>');
            tr.push('<td>' + rowNumber + '</td>');
            tr.push('<td>' + json[i].cardPatient.diagnosis + '</td>');
            if (json[i].cardPatient.allergy === true) {
                tr.push('<td>' + "Да" + '</td>');
            } else {
                tr.push('<td>' + "Нет" + '</td>');
            }
            tr.push('<td>' + json[i].cardPatient.note + '</td>');
            tr.push('<td>' + json[i].cardPatient.сonclusion + '</td>');
            tr.push('<td>' + json[i].patient.surname + ' ' + json[i].patient.name + ' ' + json[i].patient.fullName + '</td>');
            tr.push('<td>' + json[i].patient.phone + '</td>');
            tr.push('<td>' + json[i].patient.address + '</td>');
            tr.push('<td>' + json[i].patient.document.numar + '</td>');
            tr.push('<td>' + json[i].patient.document.snils + '</td>');
            tr.push('<td>' + json[i].patient.document.polis + '</td>');
            tr.push('</tr>');
            }
            $('table').append($(tr.join('')));
        });
    });
};

/**
 * Добавить карту пациента
 */
function AddCardPatient() {
    $("#FormCard").submit( function (event){
        event.preventDefault();
        var idPatient  = $('#idPatient').val();
        var diagnosis  = $('#diagnosis').val();
        var allergy    = $('#allergy').val();
        var note       = $('#note').val();
        var сonclusion = $('#сonclusion').val();

            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                url: window.location.protocol + "//"+ hostname  +":" + port +"/react/card-patients/add?idPatient=" + idPatient,
                data: JSON.stringify ({diagnosis: diagnosis,
                                       allergy: allergy,
                                       note: note,
                                       сonclusion: сonclusion}), 
                cache: false,
                success: function( json ) {
                    $('#exampleModal').modal('hide');
                    $('.modal-backdrop').remove(); 
                    $('#LastCard').click();
                }, error: function ( error ){
                    const response = JSON.parse(error.responseText);
                    $('#errorToast').text( response.message ).show();
                    $('#liveToastBtn').click();
                }
        });
    });
};
/**
 * Поиск карты по параметрам
 */
function findByDocumentCard() {
    $(document.getElementById("findByCardPatient")).on( "click",function(){
        var param = $('#wordParam').val();
        if(  param.length  == 0 ){
            $('#errorToast').text( 'Значение поля поиск не может быть пустым' ).show();
            $('#liveToastBtn').click();
        }else{
            $.ajax({
                type: "GET",
                contentType: "application/json; charset=utf-8",
                url: window.location.protocol +"//"+ hostname +":" + port +"/react/card-patients/{param}",
                data:{ param: param } ,
                cache: false,
                success: function( json ) {
                    var tr=[];
                    for (var i = 0; i < json.length; i++) {
                        var rowNumber = 1 + i;
                        tr.push('<tr>');
                        tr.push('<td>' + rowNumber + '</td>');
                        tr.push('<td>' + json[i].cardPatient.diagnosis + '</td>');
                        if (json[i].cardPatient.allergy === true) {
                            tr.push('<td>' + "Да" + '</td>');
                        } else {
                            tr.push('<td>' + "Нет" + '</td>');
                        }
                        tr.push('<td>' + json[i].cardPatient.note + '</td>');
                        tr.push('<td>' + json[i].cardPatient.сonclusion + '</td>');
                        tr.push('<td>' + json[i].patient.surname + ' ' + json[i].patient.name + ' ' + json[i].patient.fullName + '</td>');
                        tr.push('<td>' + json[i].patient.phone + '</td>');
                        tr.push('<td>' + json[i].patient.address + '</td>');
                        tr.push('<td>' + json[i].patient.document.numar + '</td>');
                        tr.push('<td>' + json[i].patient.document.snils + '</td>');
                        tr.push('<td>' + json[i].patient.document.polis + '</td>');
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

function getCountCard() {
    return new Promise((resolve, reject) => {
        $.getJSON(protocol + '//' + hostname + ':' + port + '/react/card-patients/count')
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
    let totalcards = await getCountCard();
    let cardsPerPage = 9;
    let totalPages = Math.ceil(totalcards / cardsPerPage);
    let currentPage = 1;

    $('#currentPage').text(currentPage);
    updatePageNumbers();

    $(document.getElementById("Previous")).on("click", function () {
        if (currentPage > 1) {
            currentPage--;
            updatePage();
        }
    });

    $(document.getElementById("Next")).on("click", function () {
        if (currentPage < totalPages) {
            currentPage++;
            updatePage();
        }
    });

    $(document.getElementById("last")).on("click", function () {
        currentPage = totalPages;
        updatePage();
    });

    function updatePage() {
        $('tbody:even').empty();
        $('#currentPage').text(currentPage);
        lazyCard(currentPage, cardsPerPage);
        updatePageNumbers();
    }

    function updatePageNumbers() {
        $('#pageNumbers').empty();
        let pageNumbers = [];

        if (totalPages <= 3) {
            for (let page = 1; page <= totalPages; page++) {
                pageNumbers.push(page);
            }
        } else {
            if (currentPage > 2) {
                pageNumbers.push(1);
                if (currentPage > 3) pageNumbers.push('...'); 
            }
            if (currentPage - 1 > 0) pageNumbers.push(currentPage - 1);
            pageNumbers.push(currentPage);
            if (currentPage + 1 <= totalPages) pageNumbers.push(currentPage + 1);
            if (currentPage < totalPages - 1) {
                if (currentPage < totalPages - 2) pageNumbers.push('...'); 
                pageNumbers.push(totalPages);
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
    $('#findByCardPatient').on("click", findByDocumentCard());
    $('#wordParam').on('keypress', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); 
            document.getElementById("findByCardPatient").click();
        }
    });
});