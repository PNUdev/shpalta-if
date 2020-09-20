<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >

<@c.categories categories />

<ul>
    <#list accounts as account>
        <li>
            <ul>
                <li>${account.name}</li>
                <li>${account.surname}</li>
                <li>${(account.profileImageUrl)!}</li>
                <li>${(account.description)!}</li>
            </ul>
        </li>
    </#list>
</ul>

<#include "../include/footer.ftl" >