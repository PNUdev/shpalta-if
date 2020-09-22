<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >

<@c.categories categories />

<ul>
    <li>${account.name}</li>
    <li>${account.surname}</li>
    <li>${(account.profileImageUrl)!}</li>
    <li>${(account.description)!}</li>
</ul>

<#include "../admin/include/toastr.ftl" > <!-- Included here to show toast after account update -->
<#include "../include/footer.ftl" >