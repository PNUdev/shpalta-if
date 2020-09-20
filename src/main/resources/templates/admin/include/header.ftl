<#assign  security=JspTaglibs["http://www.springframework.org/security/tags"] />
<html>
<head>
    <#include "./coreDependencies.ftl" >
    <title>Admin</title>
</head>
<body>
<nav class="navbar navbar-dark bg-info">
    <div>

        <@security.authorize access="hasRole('ROLE_ADMIN')">
            <a class="navbar-brand" href="/admin/categories">Категорії</a>
            <a class="navbar-brand" href="/admin/users">Користувачі</a>
        </@security.authorize >

        <!-- ToDo: WRITER should have access to own posts only and should be able to add posts or edit own posts -->
        <!-- ADMIN should not be able to add or edit posts, only see or hide (deactivate) them (since admin doesn't have account) -->
        <a class="navbar-brand" href="/admin/posts">Пости</a>
    </div>
</nav>