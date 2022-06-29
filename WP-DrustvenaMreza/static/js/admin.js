let currentUser = null;
let userToShow = null;
let editUser = false;
let recievedRequests = null;
let sentRequests = null;
let messages = null;

function goToMessages(chatWith){
    window.sessionStorage.setItem('chatWith', chatWith);
    window.location = "chat.html";
}

$(".search-input").keydown(function (event) { 
    if (event.which == 13) { 
        event.preventDefault();
        let query = $('input[name="searchInput"]').val();
        var searchOptions = [];
        $.each($("input[name='searchOption']:checked"), function() {
            searchOptions.push($(this).val());
        });
        if (searchOptions.length != 0) { 
            query += '&options=' + searchOptions.join(",");
        }
        $.ajax({
            type: "GET",
            url: "rest/korisnici/search?query=" + query,
            success: function (response) {          
                if (response.status == "SUCCESS") {
                    $('.searchResult').empty();
                    $(".myprofile").hide(300);  // probaj dok si na logout li da search
                    $(".logout").hide(300);
                    $(".search-res").show(300);
                    const searchRes = response.data;
                    if (searchRes.length == 0){
                        let divNoRes = $("<div class='nores'><h2>No results</h2></div>")
                        $('.searchResult').append(divNoRes);
                        return;
                    }
                    
                    for (let i = 0; i < searchRes.length; i++){
                        let divRequest = $("<div class='searchItem' data-index='" + searchRes[i].korisnickoIme +"'></div>");
                        let divInfo = $('<div class="info"></div>');
                        let divPhoto = $('<div class="profile-photo"></div>');
                        let picPath = "pics/avatar.png";
                        if (searchRes[i].profilnaSlika) {
                            if (!searchRes[i].profilnaSlika.obrisana) {
                                picPath = searchRes[i].profilnaSlika.putanja;
                            }
                        }
                        let img = $('<img src=' + picPath + '>');
                        divPhoto.append(img);
                        let div = $('<div></div>');
                        let text = $('<h4>' + searchRes[i].ime + ' ' + searchRes[i].prezime + '</h4>');
                        div.append(text);
                        
                        let divAction = $('<div class="action"></div>');
                        if (searchRes[i].uloga !== "ADMINISTRATOR") {
                          let btnBlock;
                          if (searchRes[i].blokiran) 
                            btnBlock = $('<button class="btn btn-primary unblockBtn" data-index=' + searchRes[i].korisnickoIme + '>Unblock</button>');
                          else 
                            btnBlock = $('<button class="btn blockBtn" data-index=' + searchRes[i].korisnickoIme + '>Block</button>');

                          divAction.append(btnBlock);
                        }
                
                        divInfo.append(divPhoto);
                        divInfo.append(div);
                
                        divRequest.append(divInfo);
                        divRequest.append(divAction);

                
                        $('.searchResult').append(divRequest);
                    }

                    bindSearchRes('searchResult');
                }
                else {
                    alert(response.message);
                }
            },
            error: function (response) {
                alert("error show user profile");
            }
        });
    } 
});

function bindSearchRes(className) { // ovako mozda isto za poruke?
    let results = $('.' + className);
    for (let i = 0; i < results.length; i++){
        $(results[i]).find(".unblockBtn").click(function () {
          unblockUser($(this).attr('data-index'));
          $(this).removeClass('unblockBtn');
          $(this).addClass('blockBtn');
          $('.search-input').trigger({ type: 'keydown', which: 13 });

        });
        $(results[i]).find(".blockBtn").click(function () {
          blockUser($(this).attr('data-index'));
          $(this).removeClass('blockBtn');
          $(this).addClass('unblockBtn');
          $('.search-input').trigger({ type: 'keydown', which: 13 });

        });
        $(results[i]).find(".searchItem").click(function (e) {
            let classList = $(e.target).attr("class");
            let classArr = classList.split(/\s+/);
            if (classArr.includes("unblockBtn") || classArr.includes("blockBtn")){
              return; // ne radi nista
            }
            showUserProfile($(this).attr('data-index'));
        });
    }
}

function unblockUser(username) {
  $.ajax({
    type: "PUT",
    url: "rest/korisnici/unblock?username=" + username,
    success: function (response) {
        // window.location = window.location; ??
    },
    error: function (response) {
        alert("ERROR ACCEPT REQUEST");
    }
  });
}

function blockUser(username) {
  $.ajax({
    type: "PUT",
    url: "rest/korisnici/block?username=" + username,
    success: function (response) {
        // window.location = window.location; ??
    },
    error: function (response) {
        alert("ERROR ACCEPT REQUEST");
    }
  });
}

function navSetting() {
    $('.logout').hide();
    if (userToShow == currentUser) {
        $('.myprofile_li').addClass('active');
    }
    let navActiveLi = $('.myprofile_li');
    let navActiveDiv = $('.myprofile');

    $('.myprofile_li').click(function () {  
        if (userToShow != currentUser) {
            window.sessionStorage.setItem('userToShow', '');
            window.location = window.location;
        }
        if (navActiveLi.is(this)) {
            return;
        }
        $(this).addClass('active');
        $(navActiveLi).removeClass('active');
        navActiveLi = $(this);
        $('.myprofile').show(300);
        $('.serach-res').hide(300);
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
        $(".search-res").hide(300);
        $(navActiveDiv).hide(300);
        navActiveDiv = $('.logout');
    });
}


function showUserProfile(username) {
    $.ajax({
        type: "GET",
        url: "rest/korisnici/user?username=" + username,
        success: function (response) {          
            if (response.status == "SUCCESS") {
                userToShow = response.data;
                window.sessionStorage.setItem('userToShow', JSON.stringify(userToShow));
                window.location = window.location;
            }
            else {
                alert(response.message);
            }
        },
        error: function (response) {
            alert("error show user profile");
        }
    });
}

//  ======================================================= RIGHT =======================================================
function rightSettings() {
    fillMessages();
    bindMessages();
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

function fillMessages() {
    for (const message of messages){
        const user = (message.posiljalac.korisnickoIme === currentUser.korisnickoIme) ? message.primalac : message.posiljalac;
        let divMessage = $("<div class='message' data-index='" + user.korisnickoIme +"'></div>");
        let divPhoto = $('<div class="profile-photo"></div>');
        let picPath = "pics/avatar.png";
        if (user.profilnaSlika) {
            if (!user.profilnaSlika.obrisana) {
                picPath = user.profilnaSlika.putanja;
            }
        }
        let img = $('<img src=' + picPath + '>');
        divPhoto.append(img);
        let div = $('<div class="message-body"></div>');
        let text = $('<h4>' + user.ime + ' ' + user.prezime + '</h4>');
        let you = (message.posiljalac.korisnickoIme === currentUser.korisnickoIme) ? 'You: ' : '';
        console.log(message.posiljalac);
        let messagePara = $('<p class="text-muted">' + you + message.poslednjaPoruka + '</p>');
        div.append(text);
        div.append(messagePara);

        divMessage.append(divPhoto);
        divMessage.append(div);

        $('.messages').append(divMessage);
    }
}

function bindMessages() {
    let messages = $('.message');
    for (let i = 0; i < messages.length; i++){
        $(messages[i]).click(function () {
            goToMessages($(this).attr('data-index'));
        });
    }
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
    if (currentUser.korisnickoIme != userToShow.korisnickoIme) {
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
                    alert("error logout " + response.message);
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

function addButtonsForOtherUser() {

    var sendMsgIcon = $('<i class="fas fa-envelope messageIcon"></i>');
    $('.myprofile .name_last_name').append(sendMsgIcon);

    $('.messageIcon').unbind().click(function () {
        goToMessages(userToShow.korisnickoIme);
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
    $('.input_field.email').attr('hidden', currentUser.korisnickoIme != userToShow.korisnickoIme);
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
                editUser = false;
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
            recievedRequests = response.data.poslatiIPrimljeniZahtevi[0];
            sentRequests = response.data.poslatiIPrimljeniZahtevi[1];
            messages = response.data.poruke;
            window.sessionStorage.setItem('currentUser', currentUser.korisnickoIme);
            userToShow = window.sessionStorage.getItem('userToShow');
            if (!userToShow) {
                userToShow = currentUser;
            }
            else {
                userToShow = JSON.parse(userToShow);
            }

            if (currentUser.uloga !== "ADMINISTRATOR") {
              alert('You are not administrator!');
              window.location = "index.html";
            }
            else {
              navSetting();
              rightSettings();
              centerSettings();          
            }
        },
        error: function (response) {
            alert("You need to login first!");
            window.location = "index.html";
        }
    });
});