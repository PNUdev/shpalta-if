<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/header.css">
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet" href="/css/footer.css">

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"
            integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
    <script src="https://kit.fontawesome.com/3fe1b74acd.js" crossorigin="anonymous"></script>

    <title>SHPALTA_IF</title>
</head>
<body>

<header class="header">
    <a class="header__logo" href="/"></a>

    <div class="header__search">
        <form method="get" action="/feed" class="search-form">
            <input autofocus id="search-bar" name="title" placeholder="Знайти найкактуальніші події ..." type="text" autocomplete="off">
        </form>

        <i class="fas fa-search" id="search-btn"></i>

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

    $('#search-btn').on("click", function() {
        if($(this).hasClass('fa-times')){
            $('#search-result').hide();
        }

        $('.search-form').toggleClass('search__show');
        $(this).toggleClass('fa-search');
        $(this).toggleClass('fa-times');
        $('body').toggleClass('search_opacity');
    })


</script>

</body>
</html>
