<#include "../include/header.ftl">

<div class="my-3 px-5">
    <a href="/admin/users/new">
        <div class="btn btn-lg btn-primary">Новий користувач</div>
    </a>
</div>

<table class="table table-striped">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">Логін</th>
        <th scope="col">Роль</th>
        <th scope="col">Активний</th>
        <th scope="col"></th>
        <th scope="col">Профіль</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user >
        <tr>
            <th scope="row">${user.id}</th>
            <td>${user.username}</td>
            <td>${user.role}</td>
            <td>${user.active?then('активний', 'неактивний')}</td>
            <td>
                <#if user.role != 'ROLE_ADMIN'>
                    <#if user.active >
                        <form action="/admin/users/deactivate/${user.id}" method="post">
                            <button class="btn btn-danger">Деактивувати</button>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    <#else >
                        <div class="row">
                            <form action="/admin/users/activate/${user.id}" method="post" class="mr-2">
                                <button class="btn btn-primary">Активувати</button>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </form>
                            <form action="/admin/users/delete/${user.id}" method="post">
                                <button class="btn btn-danger">Видалити акаунт</button>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </form>
                        </div>
                    </#if>
                </#if>
            </td>
            <td>
                <#if user.publicAccount?? >
                    <a href="/accounts/${user.publicAccount.id}">Переглянути акаунт</a>
                </#if>
            </td>
        </tr>
    </#list>
</table>

<#include "../include/footer.ftl">