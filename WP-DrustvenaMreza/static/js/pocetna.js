let ulogovaniKorisnik = null;

$(document).ready(function () {

    $.ajax({
        type: "GET",
        url: "rest/korisnici/loggedIn",
        success: function (korisnik) {
            if (!korisnik)
                alert("Nema ulogovanog greska");
            else {
                ulogovaniKorisnik = korisnik;
                alert(ulogovaniKorisnik.objave);
                $("body").append(ulogovaniKorisnik.korisnickoIme);
                //alert(ulogovaniKorisnik.email);
            }
        }
    });

});