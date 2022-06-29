let userToShow = null;

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


}

$("#btnYes").click(function () {
  window.location = "index.html";
});

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
  navSetting();
});