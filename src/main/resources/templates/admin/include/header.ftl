<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
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

    <div class="ml-auto row mr-2">
        <div class="btn-group dropleft">
            <button type="button" class="btn btn-light dropdown-toggle" data-toggle="dropdown" aria-haspopup="true"
                    aria-expanded="false">
                <@security.authentication property="principal.username"/>
            </button>
            <div class="dropdown-menu">
                <@security.authorize access="hasRole('ROLE_WRITER')">
                    <a class="dropdown-item" href="/accounts/edit">
                        Редагувати акаунт
                    </a>
                    <a class="dropdown-item"
                       href="/accounts/<@security.authentication property="principal.publicAccount.id"/>">
                        Переглянути акаунт
                    </a>
                </@security.authorize >
                <a class="dropdown-item" href="/admin/users/update-password">
                    Оновити пароль
                </a>
                <div class="dropdown-divider"></div>
                <form method="POST" action="/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-link dropdown-item">Вийти</button>
                </form>
            </div>
        </div>

    </div>
</nav>