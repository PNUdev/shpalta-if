<#include "../include/header.ftl">
<#assign formSubmissionUrl = '/admin/login-attempts'>
<table class="table table-striped">
    <thead>
    <tr>
        <form method="GET" action="${formSubmissionUrl}" id="filter-form">
            <div class="row d-flex justify-content-center my-3">
                <div class="btn-group btn-group-toggle" data-toggle="buttons">
                    <button class="btn <#if !blockedOnly>
                btn-dark disabled<#else>
                btn-outline-dark active</#if>">
                        <input type="radio" name="blockedOnly" value="false" autocomplete="off"
                               <#if !blockedOnly>checked</#if>>
                        Всі спроби
                    </button>
                    <button class="btn <#if blockedOnly>
                btn-dark disabled<#else>
                btn-outline-dark active</#if>">
                        <input type="radio" name="blockedOnly" value="true" id="hatchback" autocomplete="off"
                               <#if blockedOnly>checked</#if>>
                        Заблоковані
                    </button>
                </div>
            </div>
        </form>
    </tr>
    <tr>
        <th scope="col">IP-адрес</th>
        <th scope="col">Дата та час</th>
        <th scope="col">Успішність</th>
        <th scope="col">IP-адрес заблоковано</th>
    </tr>
    </thead>
    <tbody>
    <#list loginAttempts.content as loginAttempt >
        <tr <#if loginAttempt.success>class="text-success"
            <#elseif loginAttempt.ipBlocked>class="text-danger"
            <#else>class="text-warning"</#if>
        >
            <th scope="row">${loginAttempt.ipAddress}</th>
            <td>${loginAttempt.dateTime}</td>
            <td>${loginAttempt.success?then('Успішно', 'Невдало')}</td>
            <td>${loginAttempt.ipBlocked?then('Заблоковано', '')}</td>
        </tr>
    </#list>
</table>
<div class="row">
    <ul class="pagination mx-auto">
        <#list 1..loginAttempts.totalPages as pageNumber>
            <form action="${formSubmissionUrl}" method="get">
                <li class="page-item">
                    <button type="submit"
                            <#if pageNumber - 1 == loginAttempts.number>style="background-color: gray" </#if>
                            class="page-link">${pageNumber}
                    </button>
                </li>
                <input type="hidden" name="page" value="${pageNumber}">
                <#if blockedOnly??><input type="hidden" name="blockedOnly"
                                          value="${blockedOnly?string('true', 'false')}"></#if>
            </form>
        </#list>
    </ul>
</div>

<#include "../include/footer.ftl">