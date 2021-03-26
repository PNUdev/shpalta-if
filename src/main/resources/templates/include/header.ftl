<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <link rel="stylesheet" href="/css/header.css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>

    <title>SHPALTA_IF</title>
</head>
<body>

<h1>Header</h1>
<div>
    <form method="get" action="/feed">
        <input id="search-bar" name="title" placeholder="Search post" type="text" autocomplete="off">
        <button type="submit">Search</button>
    </form>
    <div id="search-result"></div>
</div>
    <script>
        $(function () {
            $('#search-bar').on('input', function () {
                const title = $('#search-bar').val().trim();
                if (!title) {
                    $('#search-result').html("");
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
    </script>

</body>
</html>
