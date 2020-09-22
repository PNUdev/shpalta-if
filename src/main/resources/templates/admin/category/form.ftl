<#include "../include/header.ftl">

<#assign formSubmissionUrl = category???then('/admin/categories/update/${category.id}', '/admin/categories/new') >
<#assign defaultColorTheme = '#42adf5'>

<div class="mx-auto mt-5 p-5 rounded bg-light col-md-9">
    <form method="POST" action="${formSubmissionUrl}">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Назва</span>
            </div>
            <input type="text" class="form-control" name="title" value="${(category.title)!}" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Колір теми</span>
            </div>
            <input type="color" class="form-control" name="colorTheme"
                   value="${(category.colorTheme)!defaultColorTheme}" required>
        </div>

        <div class="p-3">
            <#if category??>
                <div class="row">
                    <div class="pt-3">
                        <button class="btn btn-primary">Оновити</button>
                        <a href="/admin/categories/delete/${category.id}">
                            <div class="btn btn-danger">Видалити</div>
                        </a>
                    </div>
                </div>
            <#else >
                <button class="btn btn-primary">Додати категорію</button>
            </#if>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>

<#include "../include/footer.ftl">