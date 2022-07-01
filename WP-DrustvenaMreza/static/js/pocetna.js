let currentUser = null;
let userToShow = null;
let editUser = false;
let recievedRequests = null;
let sentRequests = null;
let messages = null;

let photo = null;


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
                        if (searchRes[i].uloga === "ADMINISTRATOR") continue;
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
                        let t = (currentUser.korisnickoIme == searchRes[i].korisnickoIme) ? 'You' : getNumOfMutalFriendsUser(searchRes[i]);
                        let mutuals = $('<p class="text-muted">' + t + '</p>');
                        div.append(text);
                        div.append(mutuals);
                
                        divInfo.append(divPhoto);
                        divInfo.append(div);
                
                        divRequest.append(divInfo);
                
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

function bindSearchRes(className) {
    let results = $('.' + className);
    for (let i = 0; i < results.length; i++){
        $(results[i]).find(".searchItem").click(function () {
            showUserProfile($(this).attr('data-index'));
        });
    }
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
//  ======================================================= LEFT =======================================================

function leftSettings() {
    $('.friends_li').addClass('active');
    let leftActiveLi = $('.friends_li');
    let leftActiveDiv = $('.friends');
    $('.mutuals').hide();
    if (currentUser == userToShow) {
        getFriends(currentUser);
        return;
    }
    $('.friends_li').text(userToShow.ime + "'s friends");
    getFriends(userToShow);
    var mutualsLi = $('<li class="mutual_friends_li"></li>').text('Mutual friends');
    $('.left .tabs ul').append(mutualsLi);
    getMutuals(userToShow);

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

function getFriends(user) {
    $.ajax({
        type: "GET",
        url: "rest/korisnici/friends?username=" + user.korisnickoIme,
        success: function (response) {            
            let friends = response.data;
            fillFriends(friends);
            bindFriends('friends');
        },
        error: function (response) {
            alert("error getFriends");
        }
    });
}

function fillFriends(friends) {
    for (let i = 0; i < friends.length; i++){
        let divRequest = $("<div class='friend' data-index='" + friends[i].korisnickoIme +"'></div>");
        let divInfo = $('<div class="info"></div>');
        let divPhoto = $('<div class="profile-photo"></div>');
        let picPath = "pics/avatar.png";
        if (friends[i].profilnaSlika) {
            if (!friends[i].profilnaSlika.obrisana) {
                picPath = friends[i].profilnaSlika.putanja;
            }
        }
        let img = $('<img src=' + picPath + '>');
        divPhoto.append(img);
        let div = $('<div></div>');
        let text = $('<h3>' + friends[i].ime + ' ' + friends[i].prezime + '</h3>');
        div.append(text);

        divInfo.append(divPhoto);
        divInfo.append(div);

        divRequest.append(divInfo);
        $('.friends').append(divRequest);
    }
}

function bindFriends(className) {
    let friends = $('.' + className);
    for (let i = 0; i < friends.length; i++){
        $(friends[i]).find(".friend").click(function () {
            showUserProfile($(this).attr('data-index'));
        });
    }
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

function getMutuals(userToShow) {
    $.ajax({
        type: "GET",
        url: "rest/korisnici/mutualFriends?userOne=" + currentUser.korisnickoIme + "&userTwo=" + userToShow.korisnickoIme,
        success: function (response) {            
            let friends = response.data;
            fillMutualFriends(friends);
            bindFriends('mutuals');
        },
        error: function (response) {
            alert("error getMutuals");
        }
    });
}

function fillMutualFriends(friends) {
    for (let i = 0; i < friends.length; i++){
        let divRequest = $("<div class='friend' data-index='" + friends[i].korisnickoIme +"'></div>");
        let divInfo = $('<div class="info"></div>');
        let divPhoto = $('<div class="profile-photo"></div>');
        let picPath = "pics/avatar.png";
        if (friends[i].profilnaSlika) {
            if (!friends[i].profilnaSlika.obrisana) {
                picPath = friends[i].profilnaSlika.putanja;
            }
        }
        let img = $('<img src=' + picPath + '>');
        divPhoto.append(img);
        let div = $('<div></div>');
        let text = $('<h3>' + friends[i].ime + ' ' + friends[i].prezime + '</h3>');
        div.append(text);

        divInfo.append(divPhoto);
        divInfo.append(div);

        divRequest.append(divInfo);
        $('.mutuals').append(divRequest);
    }
}
//  ======================================================= END LEFT =======================================================

//  ======================================================= RIGHT =======================================================
function rightSettings() {
    $('.requests').hide();
    $('.messages_li').addClass('active');

    let rightActiveLi = $('.messages_li');
    let rightActiveDiv = $('.messages');
    
    fillMessages();
    bindMessages();

    fillRequests();
    bindButtons();

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

function fillRequests() {
    for (let i = 0; i < recievedRequests.length; i++){
        let divRequest = $("<div class='request' data-index='" + recievedRequests[i].korisnickoIme +"'></div>");
        let divInfo = $('<div class="info"></div>');
        let divPhoto = $('<div class="profile-photo"></div>');
        let picPath = "pics/avatar.png";
        if (recievedRequests[i].profilnaSlika) {
            if (!recievedRequests[i].profilnaSlika.obrisana) {
                picPath = recievedRequests[i].profilnaSlika.putanja;
            }
        }
        let img = $('<img src=' + picPath + '>');
        divPhoto.append(img);
        let div = $('<div></div>');
        let text = $('<h4>' + recievedRequests[i].ime + ' ' + recievedRequests[i].prezime + '</h4>');
        let mutuals = $('<p class="text-muted">' + getNumOfMutualFriends(recievedRequests[i]) + '</p>');
        div.append(text);
        div.append(mutuals);

        let divAction = $('<div class="action"></div>');
        let btnAccept = $('<button class="btn btn-primary acceptBtn" data-index=' + recievedRequests[i].idZahteva + '>Accept</button>');
        let btnDecline = $('<button class="btn declineBtn" data-index='+ recievedRequests[i].idZahteva +'>Decline</button>');
        divAction.append(btnAccept);
        divAction.append(btnDecline);

        divInfo.append(divPhoto);
        divInfo.append(div);

        divRequest.append(divInfo);
        divRequest.append(divAction);


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
        let adminClass = (message.withAdmin) ? 'admin' : '';
        let text = $(`<h4 class=${adminClass}>` + user.ime + ' ' + user.prezime + '</h4>');
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

function getNumOfMutalFriendsUser(user) {
    let numOfMutualFriends = 0;
    for (let i = 0; i < currentUser.prijatelji.length; i++){
        for (let j = 0; j < user.prijatelji.length; j++){
            if (user.prijatelji[j] === currentUser.prijatelji[i]) {
                numOfMutualFriends++;
            }
        }
    }
    let retStr = (numOfMutualFriends != 0) ? numOfMutualFriends.toString() + ' mutual friend' : '';
    if (numOfMutualFriends > 1) retStr += 's';
    return retStr;
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
            recievedRequests = response.data[0];
            sentRequests = response.data[1];
            fillRequests();
            bindButtons();
        },
        error: function (response) {
            alert("error getFriendRequests");
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
    getPosts(userToShow);
    getSlike(userToShow);
    
    fillInformationsAboutUser();

    $('.about_li').click(function () {
        if (centerActiveLi.is(this)) {
            return;
        }
        wipePost();
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
        wipePost();
        if (!checkIfValid()){
            
        } else
        {
            $(this).addClass('active');
            $(centerActiveLi).removeClass('active');
            centerActiveLi = $(this);
            $('.posts').show(300);
            $(centerActiveDiv).hide(300);
            centerActiveDiv = $('.posts');
        }
        

    });
    $('.photos_li').click(function () {
        if (centerActiveLi.is(this)) {
            return;
        }
        wipePost();
        if (!checkIfValid()){
            

        } else{
            $(this).addClass('active');
            $(centerActiveLi).removeClass('active');
            centerActiveLi = $(this);
            $('.photos').show(300);
            $(centerActiveDiv).hide(300);
            centerActiveDiv = $('.photos');
        }
        
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

function checkIfValid(){
    let valid = true;
    if(userToShow == currentUser){
        return valid;
    }
    if(!isFriends(userToShow,currentUser)){
        if(userToShow.privatan){
            alert("User isnt your friend and profile is private!");
            valid = false;
        }
    }
    return valid;
}
function wipePost(){
    let element = document.getElementById('showingPosts');
    let element2 = document.getElementById('makingPostsLet');
    if (element){
        element.remove();
    }
    if(element2){
        element2.remove();
    }
    
}
function getPosts(user){
    $.ajax({
        type: "GET",
        url: "rest/objave/getObjave?username=" + user.korisnickoIme,
        success: function (response) {
            let objave = response.data;
            fillPostInformation(objave);
            bindButtonsPosts();

        },
        error: function (response) {
            alert("error in fetching objave");
        }
})
}

function getSlike(user){
    $.ajax({
        type: "GET",
        url: "rest/objave/getSlike?username=" + user.korisnickoIme,
        success: function (response) {
            let objave = response.data;
            fillSlikaInformation(objave);
            bindButtonsSlika();

        },
        error: function (response) {
            alert("error in fetching objave");
        }
})
}

function fillSlikaInformation(objave){
    let btnNewPost = $('<button class="btn makeSlikaBtn" >Make Slika</button>');
    $('.photos').append(btnNewPost);
    for (let i = 0; i < objave.length; i++){
        if(!objave[i].obrisana){
            let divRequest = $("<div class='slika' data-index='" + objave[i] +"'></div>");
            let divInfo = $('<div class="info"></div>');
            let divPhoto = $('<div class="photo"></div>');
            let picPath = "pics/search.ico";
            if (objave[i].slika) {

                picPath = objave[i].slika;

            }
            let img = $('<img src=' + picPath + '>');
            divPhoto.append(img);
            let div = $('<div></div>');
            let text = $('<p class="text-muted">' + objave[i].tekst + '</p>');
            div.append(text);

            let divAction = $('<div class="action"></div>');
            let btnShow = $('<button class="btn btn-primary showBtn" data-index=' + objave[i].id + '>Show</button>');
            let btnDelete = $('<button class="btn deleteBtn" data-index='+ objave[i].id +'>Delete</button>');
            divAction.append(btnShow);
            divAction.append(btnDelete);

            divInfo.append(divPhoto);
            divInfo.append(div);
            divInfo.append(divAction);

            divRequest.append(divInfo);

            $('.photos').append(divRequest);
        }
        
    }
}
function bindButtonsSlika() {
    let postGlobal = $('.photos');
    if (currentUser.korisnickoIme != userToShow.korisnickoIme)
    {
        $('.makeSlikaBtn').hide();
    }
    postGlobal.find(".makeSlikaBtn").click(function() {
        makeSlikaTemp();
        bindMakeSlikaBtn();
    })
    let posts = $('.slika');
    for (let i = 0; i < posts.length; i++){
        $(posts[i]).find(".showBtn").click(function () {

            showSlika(this.getAttribute("data-index"));
        });
        if (currentUser.korisnickoIme != userToShow.korisnickoIme){
            $(".deleteBtn").hide();
        };
        $(posts[i]).find(".deleteBtn").click(function () {
            deletePost(this.getAttribute('data-index'));
        });
    }
}
function fillPostInformation(objave){
    let btnNewPost = $('<button class="btn btn-primary makePostBtn" style="margin-top:10px; width:200px;" >New Post</button>');
    $('.posts').append(btnNewPost);
    for (let i = 0; i < objave.length; i++){
        if(!objave[i].obrisana){
            let divRequest = $("<div class='post' data-index='" + objave[i] +"'></div>");
            let divInfo = $('<div class="info"></div>');
            let divPhoto = $('<div class="photo"></div>');
            if (objave[i].slika) {
                let picPath = objave[i].slika;
                let img = $('<img src=' + picPath + '>');
                divPhoto.append(img);
            }
            let div = $('<div style="margin:20px;"></div>');
            let text = $('<p class="text-muted">' + objave[i].tekst + '</p>');
            div.append(text);

            let divAction = $('<div class="action"></div>');
            let btnShow = $('<button class="btn btn-primary showBtn" data-index=' + objave[i].id + '>Show</button>');
            let btnDelete = $('<button class="btn deleteBtn" data-index='+ objave[i].id +'>Delete</button>');
            divAction.append(btnShow);
            divAction.append(btnDelete);

            divInfo.append(div);
            divInfo.append(divPhoto);

            divInfo.append(divAction);

            divRequest.append(divInfo);

            $('.posts').append(divRequest);
        }
        
    }
}

function bindButtonsPosts() {
    let postGlobal = $('.posts');
    if (currentUser.korisnickoIme != userToShow.korisnickoIme)
    {
        $('.makePostBtn').hide();
    }
    postGlobal.find(".makePostBtn").click(function() {
        makePostTemp();
        bindMakePostBtn();
    })
    let posts = $('.post');
    for (let i = 0; i < posts.length; i++){
        $(posts[i]).find(".showBtn").click(function () {

            showPost(this.getAttribute("data-index"));
        });
        if (currentUser.korisnickoIme != userToShow.korisnickoIme){
            $(".deleteBtn").hide();
        };
        $(posts[i]).find(".deleteBtn").click(function () {
            deletePost(this.getAttribute('data-index'));
        });
    }
}

function makeSlikaTemp(){
    $('.photos').hide(300);
    makePost();
}
function makePostTemp(){
    $('.posts').hide(300);
    makePost();
}
function makePost(){

    let divRequest = $('<div class="makingPost" ></div>');
    let divInfo = $('<div class="info"></div>');
    let divPhoto = $('<input type="file" accept="image/*" class="input_pp" name="picturePath" style="margin-top: 20px; margin-bottom: 20px;">');

    divInfo.append(divPhoto);
    let textField = $('<textarea id="makePostID" name="makePostID" class="makePost" rows="4" cols="40" placeholder="Enter Post">');

    let divAction = $('<div class="action"></div>');
    let btnPost = $('<button class="btn btn-primary postPostBtn" >Post</button>');
    let btnBack = $('<button class="btn backPostBtn" >Back</button>');

    divAction.append(btnPost);
    divAction.append(btnBack);

    divRequest.append(textField);
    divRequest.append(divInfo);
    divRequest.append(divAction);

    let singlePost = $('<div id="makingPostsLet" class="makingPostLet"></div>');

    singlePost.append(divRequest);
    singlePost.addClass('centerPostStyle');
    $('.center_content').append(singlePost);
}

function bindMakeSlikaBtn(){
    let postGlobal = $('.makingPostLet');
    postGlobal.find(".postPostBtn").click(function() {
        postMadeSlika();
    })
    postGlobal.find('.backPostBtn').click(function(){
        goBackToPosts();
    
    })
}

function bindMakePostBtn(){
    let postGlobal = $('.makingPostLet');

    $('input[type="file"]').change(function(e){
        photo = e.target.files[0];
    });

    postGlobal.find(".postPostBtn").click(function() {
        postMadePost();
    })
    postGlobal.find('.backPostBtn').click(function(){
        goBackToPosts();
    
    })
}

function postMadeSlika(){
    let postText = $('textarea#makePostID').val();
    let greska = false;

    

    let postPic = $('input[name="picturePath"').val();
    if (!postPic) {
        greska = true;
        alert("Picture cant be empty!");
    }

    if (greska) return;

    let kIme = currentUser.korisnickoIme;

    var data = JSON.stringify({
        slika: postPic,
        tekst: postText,
        korsinickoIme: kIme,
    });

    $.ajax({
        type: "POST",
        url: "/rest/komentari/newSlika",
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "ERROR") {
                alert("Failed to make post");
                window.location = window.location;
            }
            else {
                alert("Post posted!")
                window.location = window.location;
            }
        }
    });
}

function postMadePost(){
    let postText = $('textarea#makePostID').val();
    let greska = false;

    if (!postText) {
      greska = true;
      alert("Post text cant be empty!");
    }

    if (greska) return;

    if (photo == null) {
        photo = '/';
        savePostToServer();
    }
    else {
        let formData = new FormData();
        formData.append("file", photo);
        formData.append("upload_preset", "upload");
        try{
          $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "https://api.cloudinary.com/v1_1/bookerapp/image/upload",
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 800000,
            success: function (response){
              photo = response.url;
              savePostToServer(photo);
            }
          })
        } catch (err) {
          console.log(err);
        }
    }
  }

function savePostToServer() {
  let kIme = currentUser.korisnickoIme;
  let postText = $('textarea#makePostID').val();

  var data = JSON.stringify({
      slika: photo,
      tekst: postText,
      korsinickoIme: kIme,
  });

  $.ajax({
      type: "POST",
      url: "/rest/komentari/newPost",
      data: data,
      contentType: 'application/json',
      success: function (response) {
          if (response.status == "ERROR") {
            photo = null;
              alert("Failed to make post");
              window.location = window.location;
          }
          else {
            photo = null;

              alert("Post posted!")
              window.location = window.location;
          }
      }
  });
}

function deletePost(objavaID)
{
    let kObjava = objavaID;
    var data = JSON.stringify({

        id: kObjava,
    });
    $.ajax({
        type: "PUT",
        url: "rest/objave/delete",
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "SUCCESS") {
                alert("Post deleted succesfully!");
                window.location = window.location;
            }
            else {
                alert(response.message);
            }
        },
        error: function (response) {
            console.log(data);

        }
    });
}
function showPost(postId) {
        $.ajax({
        type: "GET",
        url: "rest/objave/getSpecificObjava?objavaID=" + postId,
        success: function (response) {
            let objava = response.data;
            $('.posts').hide(300);
            showSpecificPost(objava);
            bindButtonsKomment();
        },
        error: function (response) {
            alert("error in fetching objava");
        }
})
}

function showSlika(postId) {
        $.ajax({
        type: "GET",
        url: "rest/objave/getSpecificObjava?objavaID=" + postId,
        success: function (response) {
            let objava = response.data;
            $('.photos').hide(300);
            showSpecificPost(objava);
            bindButtonsKomment();
        },
        error: function (response) {
            alert("error in fetching objava");
        }
})
}

function showSpecificPost(objava){
    let divRequest = $("<div class='singlePost' data-index='" + objava +"'></div>");
    let divInfo = $('<div class="info"></div>');
    let divPhoto = $('<div class="photo"></div>');
    // let picPath = "pics/search.ico";
    if (objava.slika) {

      let picPath = objava.slika;
      let img = $('<img src=' + picPath + '>');

      divPhoto.append(img);
    }
    
    let div = $('<div class="message-body" style="margin:10px;"></div>');
    let text = $('<p class="text-muted">' + objava.tekst + '</p>');
    div.append(text);

    let divAction = $('<div class="action"></div>');
    let textField = $('<input type="text" id="makeCommentID" name="makeCommentID" class="makeComment" data-index=' + objava.id + ' placeholder="Enter Comment" style="height:40px; width:500px; margin-right:20px;">');
    let btnPost = $('<button class="btn btn-primary postBtn" data-index='+ objava.id +'>Post</button>');
    let btnBack = $('<button class="btn backBtn" data-index='+ objava.id +'>Back</button>');
    divAction.append(textField);
    divAction.append(btnPost);
    divAction.append(btnBack);

    
    let divKomentari = $('<div class="comments" style="margin:20px;"></div>');
    for (let i = 0; i < objava.komentari.length; i++){

      if (objava.komentari[i]){
        if (!objava.komentari[i].obrisan)
        {   
          let divKomentarS = $('<div class="message"></div>');
          let divWrapper = $('<div class="wrappers"></div>');
          let divPhoto = $('<div class="profile-photo"></div>');
          let profilePicPath = objava.komentari[i].korisnik.profilnaSlika.putanja;
          let profile_pic = $('<img src=' + profilePicPath + '>');
          
          divPhoto.append(profile_pic);

          let div = $('<div class="message-body"></div>');
          let text = $('<h4>' + objava.komentari[i].korisnik.ime + ' ' + objava.komentari[i].korisnik.prezime + '</h4>');
          let messagePara = $('<p class="text-muted">' + objava.komentari[i].tekst + '</p>');
          div.append(text);
          div.append(messagePara);
          divWrapper.append(divPhoto);
          divWrapper.append(div);
          divKomentarS.append(divWrapper);

          let divKommentDlt = $('<button class="btn deleteKommentBtn" data-index=' + objava.komentari[i].korisnik.korisnickoIme + ' data-koment-id=' + objava.komentari[i].id + '>Delete</button>');
          divKomentarS.append(divKommentDlt);
          divKomentari.append(divKomentarS);
        }
      }
    }

    divInfo.append(div);
    divInfo.append(divPhoto);
    divInfo.append(divAction);
    divInfo.append(divKomentari);

    divRequest.append(divInfo);

    let singlePost = $('<div id="showingPosts" class="showingPost" data-index=' + objava.id +'></div>');

    singlePost.append(divRequest);
    singlePost.addClass('centerPostStyle');
    $('.center_content').append(singlePost);
}

function bindButtonsKomment(){
    let post = $('.showingPost');

    for (let i = 0; i < post.length; i++){
        $(post[i]).find(".postBtn").click(function (){

            postComment(post[i].getAttribute("data-index"));
        });
        if (currentUser.korisnickoIme != userToShow.korisnickoIme){
            
        };
        $(post[i]).find(".backBtn").click(function () {
            goBackToPosts();
        });
        let deleteButtons = $('.message');
        for (let y = 0; y < deleteButtons.length; y++){

            let theButton = $(deleteButtons[y]).find(".deleteKommentBtn");
            theButton.click(function() {
                 deleteComment(post[i].getAttribute("data-index"), theButton.data('koment-id'));
            });

            if (theButton.data('index') != currentUser.korisnickoIme){
                 theButton.hide();
            }
        }
    }
}

function deleteComment(objavaID, kommID)
{
    let kObjava = objavaID;
    let kID = kommID
    var data = JSON.stringify({

        objavaID: kObjava,
        id: kommID,
    });
    $.ajax({
        type: "PUT",
        url: "rest/komentari/delete",
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "SUCCESS") {
                alert("Comment deleted successfully");
                window.location = window.location;
            }
            else {
                alert(response.message);
            }
        },
        error: function (response) {
            console.log(data);
            alert("smolPP");
        }
    });
}
function postComment(objavaID){
    let kIme = $('#makeCommentID').val();
    let greska = false;

    if (!kIme) {
        greska = true;
        alert("Comment cant be empty");
    }

    if (greska) return;

    let kUser = currentUser.korisnickoIme;
    let kObjava = objavaID;
    
    var data = JSON.stringify({
        tekst: kIme,
        korisnik: kUser,
        objavaID: kObjava,
    });

    $.ajax({
        type: "POST",
        url: "/rest/komentari/newComment",
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "ERROR") {
                alert("Failed to make comment");
                window.location = window.location;
            }
            else {
                window.location = window.location;
            }
        }
    });
}

function goBackToPosts(){
    window.location = window.location;
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
    if (!isFriends(userToShow, currentUser)) {
        if (!isUserInSentRequests(userToShow) && !isUserInRecievedRequests(userToShow)) {
            var addFriendIcon = $('<i class="fas fa-user-plus add"></i>'); 
            $('.myprofile .name_last_name').append(addFriendIcon); 
            
            $('.add').unbind().click(function () {
                sendFriendRequest();
            });
        }
        else {
            var requestPending = $('<i class="fas fa-user-clock pending"> Pending...</i>');
            $('.myprofile .name_last_name').append(requestPending);
        }
    }
    else {
        var removeFriendIcon = $('<i class="fas fa-user-minus remove"></i>');
        var sendMsgIcon = $('<i class="fas fa-envelope messageIcon"></i>');
        $('.myprofile .name_last_name').append(removeFriendIcon);
        $('.myprofile .name_last_name').append(sendMsgIcon);

        $('.remove').unbind().click(function () {
            removeFriend();
        });
        $('.messageIcon').unbind().click(function () {
            goToMessages(userToShow.korisnickoIme);
        });
    }
}

function isUserInSentRequests() {
    if (!sentRequests) return false;
    for (let i = 0; i < sentRequests.length; i++){
        if (sentRequests[i].korisnickoIme == userToShow.korisnickoIme) {
            return true;
        }
    }
    return false;
}

function isUserInRecievedRequests() {
    if(!recievedRequests) return false;
    for (let i = 0; i < recievedRequests.length; i++){
        if (recievedRequests[i].korisnickoIme == userToShow.korisnickoIme) {
            return true;
        }
    }
    return false;
}

function isFriends(showUser, mainUser) {
    for (let i = 0; i < mainUser.prijatelji.length; i++){
        if (mainUser.prijatelji[i] == showUser.korisnickoIme) {
            return true;
        }
    }
    return false;
}

function sendFriendRequest() {
    data = JSON.stringify({
        id: '',
        posiljalac: {
            korisnickoIme: currentUser.korisnickoIme
        },
        primalac: {
            korisnickoIme: userToShow.korisnickoIme
        },
        status: 'NA_CEKANJU'
    });
    $.ajax({
        type: "POST",
        url: "rest/zahtevi/",
        data: data,
        contentType: 'application/json',
        success: function (response) {
            if (response.status == "SUCCESS") {
                window.location = window.location;
            }
            else {
                alert("error sendFriendRequest " + response.message);
            }
        },
        error: function (response) {
            alert(response.message);
        }
    });
}

function removeFriend() {
    $.ajax({
        type: "GET",
        url: "rest/korisnici/removeFriend?userOne=" + currentUser.korisnickoIme + "&userTwo=" + userToShow.korisnickoIme,
        success: function (response) {
            if (response.status == "SUCCESS") {
                window.location = window.location;
            }
            else {
                alert("error remove friends: " + response.message);
            }
        },
        error: function (response) {
            alert(response.message);
        }
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

            if (currentUser.uloga == "ADMINISTRATOR"){
                window.location = "pocetnaAdmin.html";
            }
            else if (currentUser.uloga !== "KORISNIK"){
                window.location = "index.html";
            }

            navSetting();
            leftSettings();
            rightSettings();
            centerSettings();
        },
        error: function (response) {
            alert("You need to login first!");
            window.location = "index.html";
        }
    });
});