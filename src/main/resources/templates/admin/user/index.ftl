<#include "../include/header.ftl">

<table class="table table-striped">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">Логін</th>
        <th scope="col">Роль</th>
        <th scope="col">Активний</th>
        <th scope="col">Профіль</th>
    </tr>
    </thead>
    <tbody>
    <#list users as user >
        <tr>
            <th scope="row">${user.id}</th>
            <td>${user.login}</td>
            <td>${user.role}</td>
            <td>${user.active?then('активний', 'неактивний')}</td>
            <td>
                <#if user.publicAccount?? >
                    <a href="/accounts/${user.publicAccount.id}"></a>
                </#if>
            </td>
        </tr>
    </#list>
</table>

<#include "../include/toastr.ftl">
<#include "../include/footer.ftl">