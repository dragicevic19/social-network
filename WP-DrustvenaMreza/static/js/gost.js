let userToShow = null;

$(".search-input").keydown(function (event) { 
    if (event.which == 13) { 
        event.preventDefault();
        let query = $('input[name="searchInput"]').val();
        var searchOptions = [];
        $.each($("input[name='searchOption']:checked"), function() {
            searchOptions.push($(this).val());
        });
        var sort =  $("#sortingOption  option:selected").val();

        if (searchOptions.length != 0) { 
            query += '&options=' + searchOptions.join(",");
        }
        $.ajax({
            type: "GET",
            url: "rest/korisnici/search?sort=" + sort + "&query=" + query,
            success: function (response) {          
                if (response.status == "SUCCESS") {
                    $('.searchResult').empty();
                    $(".login").hide(300);
                    $ (".myprofile").hide(300);
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
                        div.append(text);
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

function showUserProfile(username) {
  $.ajax({
      type: "GET",
      url: "rest/korisnici/user?username=" + username,
      success: function (response) {          
          if (response.status == "SUCCESS") {
              userToShow = response.data;
              window.sessionStorage.setItem('userToShow', JSON.stringify(userToShow));
              // window.location = window.location;
              centerSettings();

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

function navSetting() {
    
    $('.myprofile').hide();

    $('.login_li').click(function () {
        $('.login').show(300);
        $(".search-res").hide(300);
        $(".myprofile").hide(300);
    });
}

//  ======================================================= CENTER =======================================================
function centerSettings() {
  $('.login').hide();
  $('.search-res').hide();
  $('.myprofile').show();
  $('.posts').hide();
  $('.photos').hide();
  $('.about_li').addClass('active');

  let centerActiveLi = $('.about_li');
  let centerActiveDiv = $('.about');

  if (userToShow) {
    getPosts(userToShow);
    getSlike(userToShow);
  }

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
        if (checkIfValid()){
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
        if (checkIfValid()){
            $(this).addClass('active');
            $(centerActiveLi).removeClass('active');
            centerActiveLi = $(this);
            $('.photos').show(300);
            $(centerActiveDiv).hide(300);
            centerActiveDiv = $('.photos');
        }
    });
    addProfilePictureAndName();
}

function checkIfValid(){
    if(!userToShow){
        return true;
    }
    if(userToShow.privatan){
        alert("This profile is private!");
        return false;
    }
    return true;
}


$("#btnYes").click(function () {
  window.location = "index.html";
});

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

function addProfilePictureAndName() {
    let picPath = "pics/avatar.png";
    if (userToShow.profilnaSlika) {
        if (!userToShow.profilnaSlika.obrisana) {
            picPath = userToShow.profilnaSlika.putanja;
        }
    }
    var profile_pic = $('<img src=' + picPath + ' class="avatar">'); 
    $('.myprofile .profile_pic').empty();
    $('.myprofile .profile_pic').append(profile_pic)

    var name = $("<h1></h1>").text(userToShow.ime + ' ' + userToShow.prezime);
    $('.myprofile .name_last_name').empty();
    $('.myprofile .name_last_name').append(name);
}


function fillSlikaInformation(objave){

    for (let i = 0; i < objave.length; i++){
        if(!objave[i].obrisana){
            let divRequest = $("<div class='slika' data-index='" + objave[i] +"'></div>");
            let divInfo = $('<div class="info"></div>');
            let divPhoto = $('<div class="photo"></div>');
            if (objave[i].slika) {
                let picPath = objave[i].slika;
                let img = $('<img src=' + picPath + '>');
                divPhoto.append(img);
            }
            let div = $('<div style="margin:20px; text-align:left;"></div>');
            if (objave[i].tekst){
                let text = $('<p class="text-muted">' + objave[i].tekst + '</p>');
                div.append(text);
            }

            let divAction = $('<div class="action"></div>');
            let btnShow = $('<button class="btn btn-primary showBtn" data-index=' + objave[i].id + '>Show</button>');
            divAction.append(btnShow);

            divInfo.append(divPhoto);
            divInfo.append(div);

            divInfo.append(divAction);

            divRequest.append(divInfo);

            $('.photos').append(divRequest);
        }
    }
}


function bindButtonsSlika() {
    let posts = $('.slika');
    for (let i = 0; i < posts.length; i++){
        $(posts[i]).find(".showBtn").click(function () {
            showSlika(this.getAttribute("data-index"));
        });
    }
}


function fillPostInformation(objave){
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
            divAction.append(btnShow);

            divInfo.append(div);
            divInfo.append(divPhoto);

            divInfo.append(divAction);

            divRequest.append(divInfo);

            $('.posts').append(divRequest);
        }
    }
}


function bindButtonsPosts() {
    let posts = $('.post');
    for (let i = 0; i < posts.length; i++){
        $(posts[i]).find(".showBtn").click(function () {
            showPost(this.getAttribute("data-index"));
        });
    }
}


function showPost(postId) {
    $.ajax({
    type: "GET",
    url: "rest/objave/getSpecificObjava?objavaID=" + postId,
    success: function (response) {
        let objava = response.data;
        $('.posts').hide(300);
        showSpecificPost(objava);
        bindButtonsKomment('post');
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
            bindButtonsKomment('pic');
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

    if (objava.slika) {
      let picPath = objava.slika;
      let img = $('<img src=' + picPath + '>');
      divPhoto.append(img);
    }
    
    let div = $('<div class="message-body" style="margin:10px;"></div>');
    if (objava.tekst){
        let text = $('<p class="text-muted">' + objava.tekst + '</p>');
        div.append(text);
    }

    let divAction = $('<div class="action"></div>');
    let btnBack = $('<button class="btn backBtn" data-index='+ objava.id +'>Back</button>');
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


function bindButtonsKomment(type){
    let post = $('.showingPost');
    for (let i = 0; i < post.length; i++){
        $(post[i]).find(".backBtn").click(function () {
            goBackToPosts(type)
        });
    }
}

function goBackToPosts(type){
    if (type === 'pic') {
        $('.posts_li').click();
        $('.photos_li').click();
    }
    else {
        $('.photos_li').click();
        $('.posts_li').click();
    }
}


function fillInformationsAboutUser() {
    $('.about').show();

    $('input[name="name"]').val(userToShow.ime).attr('disabled', true);
    $('input[name="lastName"]').val(userToShow.prezime).attr('disabled', true);
    var date = new Date(userToShow.datumRodjenja);
    var dan = date.getDate().toString();
    var mesec = (date.getMonth() + 1).toString();
    dan = (dan.length == 1) ? '0' + dan : dan;
    mesec = (mesec.length == 1) ? '0' + mesec : mesec;
    $('input[name="birthday"]').val(date.getFullYear() + '-' + mesec + '-' + dan).attr('disabled', true);
}

//  ======================================================= END CENTER =======================================================

$(document).ready(function () {
    userToShow = window.sessionStorage.getItem('userToShow');
    navSetting();
});