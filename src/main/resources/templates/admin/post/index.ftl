<#include "../include/header.ftl">
<div class="container">
    <@security.authorize access="hasRole('ROLE_WRITER')">
        <a href="/admin/posts/new">
            <div class="btn btn-primary btn-lg btn-block my-4">Додати пост</div>
        </a>
    </@security.authorize>


    <#if !posts.content?has_content >
        <h2 class="text-center">Список постів пустий</h2>
    <#else>
        <#list posts.content?chunk(2) as row>
            <div class="row">
                <#list row as post>
                    <div class="col-6">
                        <div class="row mb-4">
                            <div class="col-6">
                                <div class="row pl-4">
                                    <img class="img-thumbnail" width="250px" height="250px" src="${post.pictureUrl}">
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="row d-flex justify-content-center h-25">
                                    <span class="font-weight-bold text-secondary pr-2">Заголовок:</span><span
                                            class="font-weight-normal">"${post.title}"</span>
                                </div>
                                <div class="row d-flex justify-content-center h-25">
                                    <span class="font-weight-bold text-secondary pr-2">Дата:</span><span
                                            class="font-weight-normal">"${post.createdAt}"</span>
                                </div>
                                <div class="row d-flex justify-content-center h-50">
                                    <a href="/admin/posts/${post.id}" role="button"
                                       class="btn btn-info btn-sm btn-block m-1">Переглянути</a>
                                    <a href="/admin/posts/edit/${post.id}" role="button"
                                       class="btn btn-warning btn-sm m-1 w-40">Редагувати</a>
                                    <a href="/admin/posts/delete/${post.id}" role="button"
                                       class="btn btn-danger btn-sm m-1 w-40">Перемістити в архів</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            </div>
        </#list>
    </#if>
</div>
<#include "../include/footer.ftl">