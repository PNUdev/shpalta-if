<#include "../include/header.ftl">

<div class="my-3 px-5">
    <a href="/admin/header-link/new">
        <div class="btn btn-lg btn-primary">Нове посилання</div>
    </a>
</div>

<table class="table table-striped">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">Назва</th>
        <th scope="col">Посилання</th>
        <th scope="col">Відкривати в новій вкадці</th>
        <th scope="col"></th>
        <th scope="col"></th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <#list headerLinks as headerLink >
        <tr>
            <th scope="row">${headerLink.id}</th>
            <td>${headerLink.name}</td>
            <td>
                <a href="${headerLink.link}" <#if headerLink.openInNewTab>target="_blank"</#if>>${headerLink.link}</a>
            </td>
            <td>
                <input type="checkbox" class="form-control" name="openInNewTab"
                       <#if headerLink.openInNewTab>checked</#if> disabled>
            </td>
            <td>
                <form action="/admin/header-link/move-top/${headerLink.id}" method="post">
                    <button <#if headerLink?counter==1>disabled</#if>>&#8593;</button>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
                <form action="/admin/header-link/move-bottom/${headerLink.id}" method="post">
                    <button <#if !headerLink?has_next>disabled</#if>>&#8595;</button>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </td>
            <td><a href="/admin/header-link/edit/${headerLink.id}">Оновити посилання</a></td>
            <td>
                <form action="/admin/header-link/delete/${headerLink.id}" method="post">
                    <button class="btn btn-danger">Видалити посилання</button>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </td>
        </tr>
    </#list>
</table>

<#include "../include/footer.ftl">
