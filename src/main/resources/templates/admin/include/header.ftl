<html>
<head>
    <#include "./coreDependencies.ftl" >
    <title>Admin</title>
</head>
<body>
<nav class="navbar navbar-dark bg-info">
    <div>

        <!-- ToDo: Show this tab only to ADMIN users -->
        <a class="navbar-brand" href="/admin/categories">Категорії</a>

        <!-- ToDo: WRITER should have access to own posts only and should be able to add posts or edit own posts -->
        <!-- ADMIN should not be able to add or edit posts, only see or hide (deactivate) them -->
        <a class="navbar-brand" href="/admin/posts">Пости</a>

        <!-- ToDo: Show this tab only to ADMIN users -->
        <a class="navbar-brand" href="#">Користувачі</a>
    </div>
</nav>