<#macro categories categories>

    <!-- ToDo update the view -->
    <aside class="categories">
        <h1>Категорії</h1>
        <ul class="categories-list">
            <#list categories as category>
                <li class="categories_ _item">${category.title}</li>
            </#list>
        </ul>
    </aside>

</#macro>