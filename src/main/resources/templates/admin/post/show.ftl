<#include "../include/header.ftl">

<div class="container">

    <div class="row mt-4 d-flex align-items-center  justify-content-end">
        <@security.authorize access="hasAnyRole('ROLE_WRITER', 'ROLE_EDITOR')">
            <a href="/admin/posts/edit/${post.id}" role="button"
               class="btn btn-warning btn-sm m-1 w-40">Редагувати</a>
        </@security.authorize>
        <#if post.isActive()>
            <a href="/admin/posts/deactivate/${post.id}"
               role="button"
               class="btn btn-danger btn-sm m-1 w-40">
                Перемістити в архів
            </a>
        <#else >
            <@security.authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_WRITER')">
                <a href="/admin/posts/delete/${post.id}"
                   role="button"
                   class="btn btn-danger btn-sm m-1 w-40">
                    Видалити назавжди
                </a>
            </@security.authorize>
        </#if>
    </div>

    <div class="row  border bg-light">
        <div class="col-8">
            <div class="row h-75 d-flex align-items-center  justify-content-center">
                <h2 class="font-weight-bold align-text-middle">${post.title}</h2>
            </div>
            <div class="row h-25 d-flex justify-content-between">
                <span class="pl-3 text-muted">Категорія: ${post.category.title}</span>
                <span class="text-muted">${post.createdAt}</span>
            </div>
        </div>
        <div class="col-4 d-flex justify-content-center align-items-center">
            <img class="img-thumbnail" width="200px" height="200px" src="${post.pictureUrl}">
        </div>
    </div>
    <div class="row mt-4 p-3 border bg-light">
        ${post.content}
    </div>
</div>
<#include "../include/footer.ftl">
