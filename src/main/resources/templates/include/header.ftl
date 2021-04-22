<!doctype html>
<html lang="uk">
<head>
    <#include "meta.ftl">

    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/post.css">
    <link rel="stylesheet" href="/css/accounts.css">
    <link rel="stylesheet" href="/css/footer.css">

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"
            integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/3fe1b74acd.js" crossorigin="anonymous"></script>

    <link rel="shortcut icon" href="/images/favicon.ico" type="image/ico">
    <title>Шпальта ІФ</title>
</head>
<body>

<header class="header">
    <a class="header__logo" href="/"></a>

    <div class="header-links header-links_show">
        <a href="/accounts">Наші автори</a>
        <a href="/feedbacks">Зворотній зв'язок</a>
        <a href="https://telegram.im/@shpalta_if_stage_bot" target="_blank">Телеграм бот</a>
    </div>

    <div class="header__search">
        <form method="get" action="/feed" class="search-form">
            <input id="search-bar" name="title" placeholder="Знайти найкактуальніші події ..." type="text"
                   autocomplete="off">
        </form>

        <i class="fas fa-search" id="search-btn"></i>
        <i class="fas fa-bars" id="menu-btn"></i>

        <div id="search-result"></div>
    </div>

</header>

<script>
    $(function () {
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
        })
    });

    $('#search-btn').on("click", function () {
        if ($(this).hasClass('fa-times')) {
            $('#search-result').hide();
        }

        if (window.innerWidth <= 740) {
            $("#menu-btn").toggle();
        }

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

</script>
