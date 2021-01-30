<#macro categories categories>
  <div class="col-3">
    <div id="sidebar" class="categories-sidebar">
      <ul>
        <#list categories as category>
          <li>${category.title}</li>
        </#list>
      </ul>
    </div>
  </div>
</#macro>
