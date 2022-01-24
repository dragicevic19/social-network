let ulogovaniKorisnik = null;

$(document).ready(function () {

    $(".requests").hide();
    $(".messages").hide();
    $(".logout").hide();
    $(".myprofile_li").addClass("active");
    let activeDiv = $(".myprofile");
    let activeLi = $(".myprofile_li");

    $(".requests_li").click(function () {
        if (activeLi.is(this)) {
            return;
        }
        $(this).addClass("active");
        $(activeLi).removeClass("active");
        activeLi = $(this);
        $(".requests").show(300);
        $(activeDiv).hide(300);
        activeDiv = $(".requests");
    });

    $(".messages_li").click(function () {
        if (activeLi.is(this)) {
            return;
        }
        $(this).addClass("active");
        $(activeLi).removeClass("active");
        activeLi = $(this);

        $(".messages").show(300);
        $(activeDiv).hide(300);
        activeDiv = $(".messages");
    });

    $(".logout_li").click(function () {
        if (activeLi.is(this)) {
            return;
        }
        $(this).addClass("active");
        $(activeLi).removeClass("active");
        activeLi = $(this);

        $(".logout").show(300);
        $(activeDiv).hide(300);
        activeDiv = $(".logout");
    });

    $(".myprofile_li").click(function () {
        if (activeLi.is(this)) {
            return;
        }
        $(this).addClass("active");
        $(activeLi).removeClass("active");
        activeLi = $(this);

        $(".myprofile").show(300);
        $(activeDiv).hide(300);
        activeDiv = $(".myprofile");
    });


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
                    // $("body").append("You are logged in as guest")
                }
                else {
                    // $("body").append('Welcome user: ' + ulogovaniKorisnik.korisnickoIme);
                }
            }
        },
        error: function (response) {
            alert("You need to login first!");
            window.location = "index.html";
        }
    });

});