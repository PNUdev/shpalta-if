<#macro categories categories>
  <div class="categories-sidebar sidebar">
    <ul>
      <#list categories as category>
        <li>
          <a href="/posts/partial?categoryUrl=${category.publicUrl}">${category.title}</a>
          <span class='mt-3' style="background: ${category.colorTheme}"></span>
        </li>
      </#list>
    </ul>
  </div>

  <div class="close-categories d-flex d-md-none justify-content-center align-items-center">
    <i class="fas fa-times"></i>
  </div>
</#macro>
