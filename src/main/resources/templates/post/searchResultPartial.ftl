<#if (posts?size) == 0>
    <p id="search-result_notfound">За вашим запитом нічого не знайдено ...</p>
</#if>

<#list posts as post>
    <a class="search-result-post" href="/posts/${post.id}">${post.title}</a>
</#list>

