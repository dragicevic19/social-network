let currentUser = null;
let userToShow = null;

function navSetting() {
    $('.logout').hide();
    $('.myprofile_li').addClass('active');
    let navActiveLi = $('.myprofile_li');
    let navActiveDiv = $('.myprofile');

    $('.myprofile_li').click(function () {  // dodati da se ovde vraca na svoj
        if (navActiveLi.is(this)) {         // profil ako je bio na necijem
            return;
        }
        $(this).addClass('active');
        $(navActiveLi).removeClass('active');
        navActiveLi = $(this);
        $('.myprofile').show(300);
        $(navActiveDiv).hide(300);
        navActiveDiv = $('.myprofile');
    });

    $('.logout_li').click(function () {
        if (navActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(navActiveLi).removeClass('active');
        navActiveLi = $(this);
        $('.logout').show(300);
        $(navActiveDiv).hide(300);
        navActiveDiv = $('.logout');
    });
}

function leftSettings() {
    $('.friends_li').addClass('active');
    let leftActiveLi = $('.friends_li');
    let leftActiveDiv = $('.friends');
    $('.mutuals').hide();
    if (currentUser == userToShow) {
        return;
    }
    var mutualsLi = $('<li class="mutual_friends_li"></li>').text('Mutual friends');
    $('.left .tabs ul').append(mutualsLi);

    $('.friends_li').click(function () {
        if (leftActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(leftActiveLi).removeClass('active');
        leftActiveLi = $(this);
        $('.friends').show(300);
        $(leftActiveDiv).hide(300);
        leftActiveDiv = $('.friends');
    });

    $('.mutual_friends_li').click(function () {
        if (leftActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(leftActiveLi).removeClass('active');
        leftActiveLi = $(this);
        $('.mutuals').show(300);
        $(leftActiveDiv).hide(300);
        leftActiveDiv = $('.mutuals');
    });
}

function rightSettings() {
    $('.messages').hide();
    $('.requests_li').addClass('active');

    let rightActiveLi = $('.requests_li');
    let rightActiveDiv = $('.requests');

    $('.requests_li').click(function () {
        if (rightActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(rightActiveLi).removeClass('active');
        rightActiveLi = $(this);
        $('.requests').show(300);
        $(rightActiveDiv).hide(300);
        rightActiveDiv = $('.requests');
    });
    $('.messages_li').click(function () {
        if (rightActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(rightActiveLi).removeClass('active');
        rightActiveLi = $(this);
        $('.messages').show(300);
        $(rightActiveDiv).hide(300);
        rightActiveDiv = $('.messages');
    });
}

function centerSettings() {
    $('.posts').hide();
    $('.photos').hide();
    $('.about_li').addClass('active');

    let centerActiveLi = $('.about_li');
    let centerActiveDiv = $('about');

    $('.about_li').click(function () {
        if (centerActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(centerActiveLi).removeClass('active');
        centerActiveLi = $(this);
        $('.about').show(300);
        $(centerActiveDiv).hide(300);
        centerActiveDiv = $('.about');
    });
    $('.posts_li').click(function () {
        if (centerActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(centerActiveLi).removeClass('active');
        centerActiveLi = $(this);
        $('.posts').show(300);
        $(centerActiveDiv).hide(300);
        centerActiveDiv = $('.posts');
    });
    $('.photos_li').click(function () {
        if (centerActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(centerActiveLi).removeClass('active');
        centerActiveLi = $(this);
        $('.photos').show(300);
        $(centerActiveDiv).hide(300);
        centerActiveDiv = $('.photos');
    });
}

$(document).ready(function () {

    $.ajax({
        type: "GET",
        url: "rest/korisnici/loggedIn",
        success: function (response) {
            currentUser = response.data;
            userToShow = currentUser;
            navSetting();
            leftSettings();
            rightSettings();
            centerSettings();
            if (currentUser.uloga == "GOST") {
            }
            else {
            }
        },
        error: function (response) {
            alert("You need to login first!");
            window.location = "index.html";
        }
    });



});