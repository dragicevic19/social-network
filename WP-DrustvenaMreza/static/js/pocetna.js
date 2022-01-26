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
//  ======================================================= LEFT =======================================================

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
//  ======================================================= END LEFT =======================================================

//  ======================================================= RIGHT =======================================================
function rightSettings() {
    $('.messages').hide();
    $('.requests_li').addClass('active');

    let rightActiveLi = $('.requests_li');
    let rightActiveDiv = $('.requests');
    getFriendRequests();

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

function fillRequests(friendRequests) {
    for (let i = 0; i < friendRequests.length; i++){
        let divRequest = $("<div class='request' data-index='" + friendRequests[i].korisnickoIme +"'></div>");
        let divInfo = $('<div class="info"></div>');
        let divPhoto = $('<div class="profile-photo"></div>');
        let picPath = "pics/avatar.png";
        if (friendRequests[i].profilnaSlika) {
            if (!friendRequests[i].profilnaSlika.obrisana) {
                picPath = friendRequests[i].profilnaSlika.putanja;
            }
        }
        let img = $('<img src=' + picPath + '>');
        divPhoto.append(img);
        let div = $('<div></div>');
        let text = $('<h4>' + friendRequests[i].ime + ' ' + friendRequests[i].prezime + '</h4>');
        let mutuals = $('<p class="text-muted">' + getNumOfMutualFriends(friendRequests[i]) + '</p>');
        div.append(text);
        div.append(mutuals);

        let divAction = $('<div class="action"></div>');
        let btnAccept = $('<button class="btn btn-primary acceptBtn" data-index=' + friendRequests[i].idZahteva + '>Accept</button>');
        let btnDecline = $('<button class="btn declineBtn" data-index='+ friendRequests[i].idZahteva +'>Decline</button>');
        divAction.append(btnAccept);
        divAction.append(btnDecline);

        divInfo.append(divPhoto);
        divInfo.append(div);
        divInfo.append(divAction);

        divRequest.append(divInfo);

        $('.requests').append(divRequest);
    }
}

function bindButtons() {
    let requests = $('.request');
    for (let i = 0; i < requests.length; i++){
        $(requests[i]).find(".acceptBtn").click(function () {
            acceptRequest($(this).attr('data-index'));
        });
        $(requests[i]).find(".declineBtn").click(function () {
            declineRequest($(this).attr('data-index'));
        });
    }
}

function acceptRequest(reqId) {
    $.ajax({
        type: "GET",
        url: "rest/korisnici/acceptFriendRequest?id=" + reqId,
        success: function (response) {
            window.location = window.location;
        },
        error: function (response) {
            alert("ERROR ACCEPT REQUEST");
        }
    });
}

function declineRequest(reqId) {
    $.ajax({
        type: "GET",
        url: "rest/korisnici/declineFriendRequest?id=" + reqId,
        success: function (response) {
            window.location = window.location;
        },
        error: function (response) {
            alert("ERROR DECLINE REQUEST");
        }
    });
}

function getNumOfMutualFriends(friendRequest) {
    let numOfMutualFriends = 0;
    for (let i = 0; i < currentUser.prijatelji.length; i++){
        for (let j = 0; j < friendRequest.prijateljiPosiljaoca.length; j++){
            if (friendRequest.prijateljiPosiljaoca[j] === currentUser.prijatelji[i]) {
                numOfMutualFriends++;
            }
        }
    }
    let retStr = (numOfMutualFriends != 0) ? numOfMutualFriends.toString() + ' mutual friend' : '';
    if (numOfMutualFriends > 1) retStr += 's';
    return retStr;
}

function getFriendRequests() {
    $.ajax({
        type: "GET",
        url: "rest/korisnici/friendRequests?username=" + currentUser.korisnickoIme,
        success: function (response) {            
            let friendRequests = response.data;
            fillRequests(friendRequests);
            bindButtons();
        },
        error: function (response) {
            alert("error");
        }
    });
}

//  ======================================================= END RIGHT =======================================================
//  ======================================================= CENTER =======================================================
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

    $("#btnYes").click(function () {
        $.ajax({
            type: "GET",
            url: "rest/korisnici/logout",
            success: function (response) {
                if (response.status == "SUCCESS") {
                    currentUser = null;
                    userToShow = null;
                    window.location = 'index.html';
                }
                else {
                    alert("error ovde " + response.message);
                }
            },
            error: function (response) {
                alert(response.message);
            }
        });
    });

    $("#btnNo").click(function () {
        $('.myprofile_li').click();
    });
}

function addProfilePictureAndName() {
    let picPath = "pics/avatar.png";
    if (userToShow.profilnaSlika) {
        if (!userToShow.profilnaSlika.obrisana) {
            picPath = userToShow.profilnaSlika.putanja;
        }
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
//  ======================================================= END CENTER =======================================================

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