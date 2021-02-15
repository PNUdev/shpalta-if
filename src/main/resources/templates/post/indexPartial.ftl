<#if error?? >
  <div>${error}</div>
<#else>
  <#list posts.content as post >
    <div class="post d-flex flex-column flex-md-row mb-3">
      <a href="/posts/${post.id}" class="mr-2 h-100">
        <img src="${post.pictureUrl}" class="post__picture">
      </a>

      <div class="caption mt-2 mt-md-0">
        <div class="caption-title pb-2">
          <h3><a href="/posts/${post.id}">${post.title}</a></h3>
        </div>

        <div class="created-at d-flex align-items-center">
          <img src="/images/calendar-alt-solid.svg" alt="" class="mr-2">
          <span class='date'>${post.createdAt}</span>
        </div>
      </div>
    </div>
  </#list>
</#if>
