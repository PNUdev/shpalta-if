<#include "../include/header.ftl">
<#include "../include/summernote.ftl">

<#assign formSubmissionUrl = post???then('/admin/posts/update/${post.id}', '/admin/posts/new') >

<div class="mx-auto mt-5 p-5 rounded bg-light col-md-9">
    <form method="POST" action="${formSubmissionUrl}">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Заголовок</span>
            </div>
            <input type="text" class="form-control" name="title" value="${(post.title)!}" required>
        </div>
        <div class="row mb-3 d-flex align-items-center">
            <div class="col-9 h-50 input-group d-flex align-items-center">
                <div class="input-group-prepend d-flex align-items-center">
                    <span class="input-group-text">Головне зображення</span>
                </div>
                <input type="url" class="form-control" id="image-url" name="pictureUrl" value="${(post.pictureUrl)!}"
                       required placeholder="Посилання на зображення">
            </div>
            <div class="col-3 h-100">
                <div id="image-preview-block">
                    <img src="" class="mw-100 mh-100" id="image-display" alt="Попередній перегляд зображення">
                </div>
                <div id="image-loading-error">
                    <div class="h4">Зображення не знайдено</div>
                </div>
                <script>
                    let imageUrl = $('#image-url');
                    let imageDisplay = $('#image-display');
                    let imagePreviewBlock = $('#image-preview-block');
                    let imageLoadingErrorBlock = $('#image-loading-error');

                    let setImageSrcUrl = function () {
                        imageDisplay.attr('src', imageUrl.val())
                    };

                    $(document).ready(setImageSrcUrl);
                    imageUrl.change(setImageSrcUrl);

                    imageDisplay
                        .on('load', function () {
                            imagePreviewBlock.show();
                            imageLoadingErrorBlock.hide();
                            $('#btn-submit').attr('disabled', false);
                        })
                        .on('error', function () {
                            imagePreviewBlock.hide();
                            imageLoadingErrorBlock.show();
                            $('#btn-submit').attr('disabled', true);
                        });
                </script>

            </div>
        </div>


        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <label for="category_selection" class="input-group-text">Категорія</label>
            </div>
            <select data-live-search="false" title="Categories" class="form-control selectpicker" name="categoryId"
                    id="category_selection" required>
                <option disabled selected value> -- Виберіть категорію --</option>
                <#list categories as category>
                    <option value="${category.id}"
                            <#if post?? && category.id == post.category.id>selected</#if>>${category.title}
                    </option>
                </#list>
            </select>
        </div>
        <div class="input-group mb-3">
            <div style="width: 100%">
                <textarea name="content" id="postContentEditor"></textarea>
                <script>
                    $('#postContentEditor').summernote({
                        lang: 'uk-UA',
                        tabsize: 2,
                        height: 500,
                        toolbar: [
                            ['style', ['style']],
                            ['font', ['bold', 'underline', 'strikethrough', 'clear']],
                            ['fontname', ['fontname']],
                            ['color', ['color']],
                            ['para', ['ul', 'ol', 'paragraph']],
                            ['table', ['table']],
                            ['insert', ['link', 'picture', 'video']],
                            ['view', ['fullscreen', 'codeview', 'help']],
                        ],
                    });
                    <#if post??>
                    $('#postContentEditor').summernote('code', '${post.content?js_string}');
                    </#if>
                </script>
            </div>
        </div>
        <div>
            <#if post?? && !post.active>
                <div class="row d-flex justify-content-center align-items-center">
                    <input type="checkbox" class="" name="active" id="active-checkbox">Активувати перед збереженням
                </div>
            </#if>
            <button class="btn btn-primary btn-block" id="btn-submit" disabled>Зберегти</button>
        </div>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>
<#include "../include/footer.ftl">
