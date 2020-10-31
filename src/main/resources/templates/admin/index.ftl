<#include "./include/header.ftl">
<h1 class="text-center">Панель адміністратора</h1>

<@security.authorize access="hasRole('ROLE_ADMIN')">
    <table class="table mt-5">
        <thead>
        <tr>
            <th scope="col" colspan="3" class="table-primary text-center">Телеграм підписки</th>
        </tr>
        <tr>
            <th scope="col">Категорія</th>
            <th scope="col">Користувачів підписано</th>
            <th scope="col">% користувачів підписано</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Загалом</td>
            <td>${telegramDashboard.totalUsersCount}</td>
            <td></td>
        </tr>
        <#list telegramDashboard.subscriptionsInfos as subscriptionsInfo>
            <tr>
                <td>${subscriptionsInfo.category.title}</td>
                <td>${subscriptionsInfo.subscribedUsersCount}</td>
                <td>${subscriptionsInfo.percentOfTotalUsersCount?string("#.##")}%</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@security.authorize >

<#include "./include/footer.ftl">