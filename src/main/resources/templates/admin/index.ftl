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
            <th scope="row">Загалом користувачів</th>
            <td>${telegramDashboard.totalUsersCount}</td>
            <td></td>
        </tr>
        <tr>
            <th scope="row">Підписаних хоча б на одну категорію</td>
            <td>${telegramDashboard.subscribedToAtLeastOneCategory.subscribedUsersCount}</td>
            <td>${telegramDashboard.subscribedToAtLeastOneCategory.percentOfTotalUsersCount?string("#.##")}%</td>
        </tr>
        <#list telegramDashboard.subscriptionsInfos as subscriptionsInfo>
            <tr>
                <td>${subscriptionsInfo.category.title}</td>
                <td>${subscriptionsInfo.userSubscriptionsInfo.subscribedUsersCount}</td>
                <td>${subscriptionsInfo.userSubscriptionsInfo.percentOfTotalUsersCount?string("#.##")}%</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@security.authorize >

<#include "./include/footer.ftl">