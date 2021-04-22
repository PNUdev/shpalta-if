<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >

<main class="wrapper">
    <@c.categories categories />

    <ul class="accounts">
        <a href="/"><i class="fas fa-caret-left"></i> Повернутись на головну</a>
        <li class="accounts-title"><h2>Користувачі: </h2></li>
        <#list accounts as account>
            <li>
                <ul class="accounts-item">
                    <li class="accounts-item__img">
                        <img src="${(account.profileImageUrl)!}" alt="NOT FOUND!"
                             onerror="this.src='/images/public_account_default.png'">
                    </li>

                    <li>
                        <ul class="accounts-item-info">
                            <li class="accounts-item__name">
                                <a href="/accounts/${account.id}">${account.getSignature()}</a></li>
                            <li class="accounts-item__desc">${((account.description)!)?truncate(80, "...")}</li>
                        </ul>
                    </li>
                </ul>
            </li>
        </#list>
    </ul>

</main>
<#include "../include/footer.ftl" >