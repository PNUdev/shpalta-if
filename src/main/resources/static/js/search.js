$(document).ready(function() {
  $('#search-bar').on('input', function () {
    $('#search-result').show();

    const title = $('#search-bar').val().trim();

    if (!title) {
      $('#search-result').html("").hide();
      return;
    }
    $.get(
      '/posts/search-result-partial',
      {
        'title': title,
      }, function (response) {
        $('#search-result').html(response);
      })
  });

  $('#search-btn').on("click", function () {
    if ($(this).hasClass('fa-times')) {
      $('#search-result').hide();
    }

    if (window.innerWidth <= 740) $("#menu-btn").toggle();

    $('.header__search').toggleClass('search-header_show');
    $('.search-form').toggleClass('search__show');

    $(this).toggleClass('fa-search');
    $(this).toggleClass('fa-times');
    $('body').toggleClass('search_opacity');
  })

  $("#menu-btn").on("click", function () {
    $(".header-links").toggleClass("header-links_show");
    $(".categories").toggleClass("categories_show");
    $("#search-btn").toggle();

    $(this).toggleClass('fa-search');
    $(this).toggleClass('fa-times');
    $('body').toggleClass('search_opacity');
  })
})
