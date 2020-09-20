<#macro categories categories>

    <!-- ToDo update the view -->
    <aside>
        <ul>
            <#list categories as category>
                <li>${category.title}</li>
            </#list>
        </ul>
    </aside>

</#macro>