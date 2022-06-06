<#include "../include/header.ftl">

<#assign formSubmissionUrl = headerLink???then('/admin/header-link/update/${headerLink.id}', '/admin/header-link/new') >

<div class="mx-auto mt-5 p-5 rounded bg-light col-md-9">
    <form method="POST" action="${formSubmissionUrl}">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Назва</span>
            </div>
            <input type="text" class="form-control" name="name" value="${(headerLink.name)!}" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Посилання</span>
            </div>
            <input type="text" class="form-control" name="link" value="${(headerLink.link)!}" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Відкривати в новій вкадці</span>
            </div>
            <input type="checkbox" class="form-control" name="openInNewTab"
                   <#if headerLink?? && headerLink.openInNewTab>checked</#if>>
        </div>
        <button class="btn btn-primary">Зберегти</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>

<#include "../include/footer.ftl">
