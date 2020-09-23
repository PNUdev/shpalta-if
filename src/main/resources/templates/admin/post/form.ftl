<#include "../include/header.ftl">
<#include "../include/summernote.ftl">

<#assign formSubmissionUrl = post???then('/admin/posts/update/${post.id}', '/admin/posts/new') >

<div class="mx-auto mt-5 p-5 rounded bg-light col-md-9">
    <form method="POST" id="postForm" action="${formSubmissionUrl}">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Заголовок</span>
            </div>
            <input type="text" class="form-control" name="title" value="${(post.title)!}" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Головне зображення</span>
            </div>
            <input type="url" class="form-control" name="pictureUrl" value="${(post.pictureUrl)!}" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <label for="category_selection" class="input-group-text">Категорія</label>
            </div>
            <select data-live-search="false" title="Categories" class="form-control selectpicker" name="category"
                    id="category_selection" required>
                <option disabled selected value> -- Виберіть категорію --</option>
                <#list categories as category>
                    <option value="${category.id}">${category.title}
                    </option>
                </#list>
            </select>
        </div>
        <div class="input-group mb-3">
            <div style="width: 100%">
                <textarea name="content" id="contentEditor"></textarea>
                <script>
                    $('#contentEditor').summernote({
                        lang: 'uk-UA',
                        tabsize: 2,
                        height: 500
                    });
                </script>
            </div>
        </div>
        <div class="p-3">
            <#if post??>
                <div class="row">
                    <div class="pt-3">
                        <button class="btn btn-primary">Оновити</button>
                        <a href="/admin/posts/delete/${post.id}">
                            <div class="btn btn-danger">Видалити</div>
                        </a>
                    </div>
                </div>
            <#else >
                <button class="btn btn-primary">Зберегти</button>
            </#if>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
<#include "../include/footer.ftl">