<#include "../admin/include/header.ftl" >
<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<@security.authentication property='principal.publicAccount' var='principalAccount' />

<div class="row d-flex justify-content-around">
    <div class="col-md-7 mt-5 p-5 rounded bg-light">
        <form method="POST" action="/accounts/update">
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">Ім'я</span>
                </div>
                <input type="text" class="form-control" name="name" value="${principalAccount.name}" required>
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">Прізвище</span>
                </div>
                <input type="text" class="form-control" name="surname" value="${principalAccount.surname}" required>
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">
                        <input type="checkbox" name="pseudonymUsed" id="pseudonym_checkbox" class="mr-2"
                               <#if principalAccount.pseudonymUsed>checked</#if>>
                         <label class="form-check-label" for="pseudonym_checkbox">
                                Використовувати псевдонім
                         </label>

                    </span>
                </div>
                <input type="text" class="form-control" name="pseudonym" value="${principalAccount.pseudonym!}">
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">Зображення акаунту</span>
                </div>
                <input type="text" class="form-control" name="profileImageUrl" id="image-url"
                       value="${(principalAccount.profileImageUrl)!}">
            </div>
            <div class="input-group">
                <div class="input-group-prepend">
                    <span class="input-group-text">Опис</span>
                </div>
                <textarea class="form-control" name="description"
                          rows="10">${(principalAccount.description)!}</textarea>
            </div>

            <div class="p-3">
                <button class="btn btn-primary">Оновити акаунт</button>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

    </div>
    <div class="col-md-4 mt-5 p-5 rounded bg-light">
        <div id="image-preview-block">
            <div class="h4 mb-3">Попередній перегляд зображення</div>
            <img src="" class="mw-100 mh-100" id="image-display" alt="Попередній перегляд зображення">
        </div>
        <div id="image-loading-error">
            <div class="h2">Зображення не знайдено</div>
        </div>
    </div>
</div>

<script>
    const imageUrl = $('#image-url');
    const imageDisplay = $('#image-display');
    const imagePreviewBlock = $('#image-preview-block');
    const imageLoadingErrorBlock = $('#image-loading-error');
    const setImageSrcUrl = function () {
        imageDisplay.attr('src', imageUrl.val())
    };
    $(document).ready(setImageSrcUrl);
    imageUrl.change(setImageSrcUrl);
    imageDisplay
        .on('load', function () {
            imagePreviewBlock.show();
            imageLoadingErrorBlock.hide();
        })
        .on('error', function () {
            imagePreviewBlock.hide();
            imageLoadingErrorBlock.show();
        });
</script>

<#include "../admin/include/footer.ftl" >