<#include "../include/header.ftl">
<div class="container">
    <@security.authorize access="hasRole('ROLE_WRITER')">
        <a href="/admin/posts/new">
            <div class="btn btn-primary btn-lg btn-block my-4">Додати пост</div>
        </a>
    </@security.authorize>


    <#if !posts?has_content >
        <h2 class="text-center">Список постів пустий</h2>
    <#else>
        <#list posts as post>
            <div class="row">
                <div class="col-4">
                    <div class="row">
                        <img src="${post.pictureUrl}">
                    </div>
                </div>
                <div class="col-4">
                    <div class="row">
                        <strong>${post.title}</strong>
                    </div>
                    <div class="row">
                        <strong>${post.create}</strong>
                        <strong>${post.createdAt}</strong>
                    </div>
                </div>
                <div class="col-4">
                    <div class="row">

                    </div>
                </div>
            </div>
        </#list>
    </#if>
</div>
<#include "../include/footer.ftl">