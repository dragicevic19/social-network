let currentUser = null;
let userToShow = null;
let editUser = false;

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
    let centerActiveDiv = $('.about');
    fillInformationsAboutUser();


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
        fillInformationsAboutUser();

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

    addProfilePictureAndName();
    if (currentUser != userToShow) {
        addButtonsForOtherUser();
    }
    else {
        addButtonForEdit();
    }

    $('#saveChangesBtn').click(function () {
        changeInfos(); 
    });

    $('#changePasswordBtn').click(function () {
        $('.about').hide(300);
        $('.passChange').show(300);
    });

    $('#backBtn').click(function () {
        $('.about').show(300);
        $('.passChange').hide(300);
    });

    $('#savePassChangesBtn').click(function () {
        changePassword(); 
    });
}

function addProfilePictureAndName() {
    let picPath = userToShow.profilnaSlika.putanja;
    if (userToShow.profilnaSlika.obrisana) {
        picPath = "pics/avatar.png";
    }
    var profile_pic = $('<img src=' + picPath + ' class="avatar">'); 
    $('.myprofile .profile_pic').append(profile_pic)

    var name = $("<h1></h1>").text(userToShow.ime + ' ' + userToShow.prezime);
    $('.myprofile .name_last_name').append(name);
}

function addButtonForEdit() {
    var editIcon = $('<i class="fas fa-edit edit"></i>');
    $('.myprofile .name_last_name').append(editIcon);

    $('.edit').click(function () {
        editUser = true;
        $('.about_li').click();
        fillInformationsAboutUser();
    });

}

function fillInformationsAboutUser() {
    $('.about').show();
    $('.passChange').hide();
    if (editUser) {
        $('.btnChanges').show(100);
    }
    else {
        $('.btnChanges').hide(100);
    }
    $('input[name="email"]').val(userToShow.email).attr('disabled', !editUser);
    $('input[name="name"]').val(userToShow.ime).attr('disabled', !editUser);
    $('input[name="lastName"]').val(userToShow.prezime).attr('disabled', !editUser);
    var date = new Date(userToShow.datumRodjenja);
    var dan = date.getDate().toString();
    var mesec = (date.getMonth() + 1).toString();
    dan = (dan.length == 1) ? '0' + dan : dan;
    mesec = (mesec.length == 1) ? '0' + mesec : mesec;
    $('input[name="birthday"]').val(date.getFullYear() + '-' + mesec + '-' + dan).attr('disabled', !editUser);
}

function changePassword() {
    let currPass = $('input[name="password"').val();
    let newPass = $('input[name="newPassword"').val();
    let repPass = $('input[name="repPassword"').val();
    let err = false;
    if (!currPass) {
        $('#currPass').text("Enter password");
        err = true;
    }
    else 
        $('#currPass').text("");
    if (!newPass) {
        $('#newPass').text("Enter password");
        err = true;
    }
    else 
        $('#newPass').text("");
    if (!repPass) {
        $('#repPass').text("Enter password");
        err = true;
    }
    else $('#repPass').text('');
    if (err) return;

    if (currPass !== currentUser.lozinka) {
        $('#currPass').text("Password is not correct");
        err = true;
        return;
    }

    if (newPass !== repPass) {
        $('#repPass').text('Passwords do not match!');
        err = true;
        return;
    }
    var data = JSON.stringify({
        korisnickoIme: currentUser.korisnickoIme,
        lozinka: newPass
    });

    $.ajax({
        type: 'PUT',
        url: 'rest/korisnici/changePass',
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "SUCCESS") {
                alert('Password changed successfully!');
                currentUser = response.data;
                userToShow = currentUser;
                onChangedPassword();
            }
        },
        error: function (response) {
            alert('error while changing password');
        }
    });
}

function onChangedPassword() {
    $('input[name="password"').val('');
    $('input[name="newPassword"').val('');
    $('input[name="repPassword"').val('');
    $('#backBtn').click();
}

function changeInfos() {
    let email = $('input[name="email"]').val();
    let name = $('input[name="name"]').val();
    let lastName = $('input[name="lastName"]').val();
    let date = $('input[name="birthday"]').val();
    let err = false;
    if (!email) {
        $('#email').text("Enter E-Mail");
        err = true;
    }
    else 
        $('#email').text("");
    
    if (!name) {
        $('#name').text("Enter Name");
        err = true;
    }
    else 
        $('#name').text("");
    
    if (!lastName) {
        $('#lastName').text("Enter Last Name");
        err = true;
    }
    else $('#lastName').text('');

    if (!date) {
        $('#date').text("Pick a Date");
        err = true;
    }
    else $('#date').text('');

    if (err) return;

    let data = JSON.stringify({
        korisnickoIme: currentUser.korisnickoIme,
        email: email,
        ime: name,
        prezime: lastName,
        datumRodjenja: date
    });

    $.ajax({
        type: "PUT",
        url: "rest/korisnici/update",
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "SUCCESS") {
                alert("Informations are updated successfully!");
                currentUser = response.data;
                userToShow = currentUser;
                window.location = window.location;
            }
            else {
                alert(response.message);
            }
        },
        error: function (response) {
            alert(response.message);
        }
    });
}

// function onInfosChanged() {
//     editUser = false;
//     $('.about_li').click();
//     fillInformationsAboutUser();
// }

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