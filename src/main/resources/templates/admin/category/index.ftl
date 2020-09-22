<#include "../include/header.ftl">
<div class="container">

    <a href="/admin/categories/new">
        <div class="btn btn-primary btn-lg btn-block my-4">Додати категорію</div>
    </a>

    <#if !categories?has_content >
        <h2 class="text-center">Список категорій пустий</h2>
    <#else>
        <#list categories as category>
            <h3><a href="/admin/categories/edit/${category.id}">${category.title}</a></h3>
        </#list>
    </#if>
</div>

<#include "../include/footer.ftl">