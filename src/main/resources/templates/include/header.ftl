<html>
<head>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"
            integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
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
            title = $('#search-bar').val();
            const resultSize = 5;
            $.ajax({
                url: 'http://localhost:8080/posts/search-result-partial',
                type: 'GET',
                data: {
                    'title': title,
                    'size': resultSize
                },
                success: function (response) {
                    console.log(response);
                    $('#search-result').html(response);
                }
            });
        })
    });
</script>
