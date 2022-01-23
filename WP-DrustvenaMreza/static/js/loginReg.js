$(document).ready(function(){
    $(':input')
        .not(':checkbox, :radio, select').val('');

    $(".login").hide();
    $(".register_li").addClass("active");

    $(".login_li").click(function () {
        $(this).addClass("active");
        $(".register_li").removeClass("active");
        $(".login").show(300);
        $(".register").hide(300);
    });

    $(".register_li").click(function () {
        $(this).addClass("active");
        $(".login_li").removeClass("active");
        $(".register").show(300);
        $(".login").hide(300);
    });

    $(".register .btn").click(function () {
        register();
    });

    $(".login .btn").click(function () {
        login();
    });

    $(".btn.guest").click(function () {
        guestLogin();
    });
});

function register() {
    
    let kIme = $('input[name="username"]').val();
    let email = $('input[name="email"]').val();
    let lozinka = $('input[name="password"]').val();
    let ponovljenaLozinka = $('input[name="repPassword"]').val();
    let ime = $('input[name="name"]').val();
    let prezime = $('input[name="lastName"]').val();
    let pol = $('input[name="gender"]:checked').val();
    
    let greska = false;

    if (!kIme) {
        $('input[name="username"]').addClass('error');
        $('input[name="username"]').attr("placeholder", "Enter username");
        greska = true;
    }
    if (!email) {
        $('input[name="email"]').addClass('error');
        $('input[name="email"]').attr("placeholder", "Enter e-mail");
        greska = true;
    }
    if (!lozinka) {
        $('input[name="password"]').addClass('error');
        $('input[name="password"]').attr("placeholder", "Enter password");
        greska = true;
    }
    if (!ponovljenaLozinka) {
        $('input[name="repPassword"]').addClass('error');
        $('input[name="repPassword"]').attr("placeholder", "Re-enter password");
        greska = true;
    }
    if (lozinka != ponovljenaLozinka) {
        $('input[name="repPassword"]').addClass('error');
        $('input[name="repPassword"]').val('');
        $('input[name="repPassword"]').attr("placeholder", "Passwords do not match!");
        greska = true;
    }
    if (!ime) {
        $('input[name="name"]').addClass('error');
        $('input[name="name"]').attr("placeholder", "Enter name");
        greska = true;
    }
    if (!prezime) {
        $('input[name="lastName"]').addClass('error');
        $('input[name="lastName"]').attr("placeholder", "Enter last name");
        greska = true;
    }

    if (greska) return;
    var data = JSON.stringify({
        korisnickoIme: kIme,
        lozinka: lozinka,
        email: email,
        ime: ime,
        prezime: prezime,
        pol: pol,
        uloga: "KORISNIK"
    });

    $.ajax({
        type: "POST",
        url: "/rest/korisnici/register",
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "ERROR") {
                toast(response.message);
            }
            else {
                window.location = "pocetna.html";
            }
        }
    });
}

function login() {
    let korisnickoIme = $('input[name="logKorisnicko"]').val();
    let lozinka = $('input[name="logPass"]').val();
    let greska = false;

    if (!korisnickoIme) {
        $('input[name="logKorisnicko"]').addClass('error');
        $('input[name="logKorisnicko"]').attr("placeholder", "Enter username");
        greska = true;
    }
    if (!lozinka) {
        $('input[name="logPass"]').addClass('error');
        $('input[name="logPass"]').attr("placeholder", "Enter password");
        greska = true;
    }
    if (greska) return;
    
    //var data = $('#loginForm').serialize();
    var data = JSON.stringify({
        korisnickoIme: korisnickoIme,
        lozinka: lozinka,
        uloga: "KORISNIK"
    })
    $.ajax({
        type: "POST",
        url: '/rest/korisnici/login',
        data: data,
        contentType: "application/json",
        success: function (response) {
            if (response.status == "SUCCESS") {
                $('#successLog').text('Successfully logged in!');
                $('#successLog').show().delay(3000).fadeOut();
                window.location = "pocetna.html";
            }
            else {
                $('#successLog').text('The username or password is incorrect');
                $('#successLog').addClass('loginError')
            }
        }
    });
}

function guestLogin() {
    var data = JSON.stringify({
        uloga: "GOST"
    });
    $.ajax({
        type: "POST",
        url: '/rest/korisnici/login',
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "SUCCESS")
                window.location = "pocetna.html";
            else 
                toast("Error");   
        }
    });
}