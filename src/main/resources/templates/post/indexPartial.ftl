<#if error?? >
    <div>${error}</div>
<#else>
    <#list posts.content as post >
        <div>
            ${post.title}
        </div>
    </#list>
</#if>