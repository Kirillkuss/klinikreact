(() => {
    let timeLeft; 
    let timerInterval; 
    let inactivityTimer;

    function logout() {
        fetch('/react/logout', {
            method: 'POST',
            credentials: 'include'
        })
        .then(response => {
            if (response.ok) {
                window.location.href = "/react/login"; 
            } else {
                console.error('Ошибка при выходе из системы:', response.statusText);
                alert('Ошибка при выходе. Попробуйте еще раз.');
            }
        })
        .catch(error => {
            console.error('Ошибка сети:', error);
        });
    }

    function startTimer() {
        const timerDisplay = document.getElementById('timer');

        if (timerInterval) {
            clearInterval(timerInterval);
        }

        timeLeft = 10;

        timerInterval = setInterval(() => {
            const minutes = String(Math.floor(timeLeft / 60)).padStart(2, '0');
            const seconds = String(timeLeft % 60).padStart(2, '0');

            timerDisplay.textContent = `${minutes}:${seconds}`;

            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                logout(); 
            } else {
                timeLeft--;
                //console.log("timeLeft >>> " + timeLeft);
            }
        }, 1000);
    }

    function resetTimer() {
        startTimer();
    }

    function resetInactivityTimer() {
        clearTimeout(inactivityTimer); 
        inactivityTimer = setTimeout(logout, 91000); 
        resetTimer();
    }

    window.addEventListener("beforeunload", () => {
        clearInterval(timerInterval);
        clearTimeout(inactivityTimer);
    });

    $(document).ready(function() {
        $(document).off("mousedown", resetInactivityTimer); 
        $(document).off("keypress", resetInactivityTimer);
        
        $(document).on("mousedown", resetInactivityTimer); 
        $(document).on("keypress", resetInactivityTimer); 

        startTimer();
    });
})();

function exit() {
    fetch('/react/logout', {
        method: 'POST',
        credentials: 'include'
    })
    .then(response => {
        if (response.ok) {
            window.location.href = "/react/login"; 
        } else {
            console.error('Ошибка при выходе из системы:', response.statusText);
            alert('Ошибка при выходе. Попробуйте еще раз.');
        }
    })
    .catch(error => {
        console.error('Ошибка сети:', error);
    });
};

$(document).ready(function() {
    $('#swagger').on("click");
        var url = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/react/klinikreact";
        document.getElementById("swagger").href = url;
});