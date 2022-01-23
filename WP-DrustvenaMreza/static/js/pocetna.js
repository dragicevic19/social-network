let ulogovaniKorisnik = null;

$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "rest/korisnici/loggedIn",
        success: function (response) {
            if (response.status == "ERROR"){
                alert("You need to login first");
                window.location = "index.html";
            }
            else {
                ulogovaniKorisnik = response.data;
                if (ulogovaniKorisnik.uloga == "GOST") {
                    $("body").append("You are logged in as guest")
                }
                else {
                    $("body").append('Welcome user: ' + ulogovaniKorisnik.korisnickoIme);
                }
            }
        },
        error: function (response) {
            alert("You need to login first!");
            window.location = "index.html";
        }
    });

});