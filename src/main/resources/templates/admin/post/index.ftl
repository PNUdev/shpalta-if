<#include "../include/header.ftl">

<#assign formSubmissionUrl = '/admin/posts'>
<#assign active = !postFilters?? || postFilters.isActive()>

<div class="container">
    <@security.authorize access="hasRole('ROLE_WRITER')">
        <a href="/admin/posts/new">
            <div class="btn btn-primary btn-lg btn-block my-4">Додати пост</div>
        </a>
    </@security.authorize>
    <form method="GET" action="${formSubmissionUrl}" id="filter-form">
        <div class="row d-flex justify-content-center mt-3">
            <div class="btn-group btn-group-toggle" data-toggle="buttons">
                <button class="btn <#if active>
                btn-dark disabled<#else>
                btn-outline-dark active</#if>">
                    <input type="radio" name="active" value="true" id="hatchback" autocomplete="off"
                           <#if active>checked</#if>>
                    Пости
                </button>
                <button class="btn <#if !active>
                btn-dark disabled<#else>
                btn-outline-dark active</#if>">
                    <input type="radio" name="active" value="false" autocomplete="off"
                           <#if !active>checked</#if>>
                    Архів
                </button>
            </div>
        </div>
        <div class="row mt-1 bg-light border p-2">
            <div class="col-4">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <label for="category_selection" class="input-group-text">Категорія</label>
                    </div>
                    <select data-live-search="false" class="form-control selectpicker"
                            name="categoryId"
                            id="category_selection">
                        <option disabled selected value> -- Виберіть категорію --</option>
                        <#list categories as category>
                            <option value="${category.id}"
                                    <#if postFilters?? && postFilters.categoryId?? && category.id == postFilters.categoryId>selected</#if>
                            >
                                ${category.title}
                            </option>
                        </#list>
                    </select>
                </div>
                <@security.authorize access="hasRole('ROLE_ADMIN')">
                    <div class="input-group mb-3">
                        <div class="input-group-prepend">
                            <label for="public_account_selection" class="input-group-text">Автор</label>
                        </div>
                        <select data-live-search="false" class="form-control selectpicker"
                                name="authorPublicAccountId"
                                id="public_account_selection">
                            <option disabled selected value> -- Виберіть автора --</option>
                            <#list publicAccounts as publicAccount>
                                <option value="${publicAccount.id}"
                                        <#if postFilters?? && postFilters.authorPublicAccountId?? && publicAccount.id == postFilters.authorPublicAccountId>selected</#if>
                                >
                                    ${publicAccount.name} ${publicAccount.surname}
                                </option>
                            </#list>
                        </select>
                    </div>
                </@security.authorize>
            </div>
            <div class="col-6">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Заголовок</span>
                    </div>
                    <input type="text" class="form-control" name="title" value="${(postFilters.title)!}">
                </div>
            </div>
            <div class="col-2">
                <a href="/admin/posts" class="btn btn-secondary btn-block">Скинути</a>
                <button class="btn btn-primary btn-block">Відфільтрувати</button>
            </div>
        </div>

    </form>
    <script>
        $('#filter-form').submit(function () {
            $(this)
                .find('input[name]')
                .filter(function () {
                    return !this.value;
                })
                .prop('name', '');
        });
    </script>
    <div class="container py-3">
        <#if !posts.content?has_content >
            <h2 class="text-center"><#if active>Список постів пустий<#else>Архів пустий</#if></h2>
        <#else>
            <#list posts.content as post>
                <div class="row border my-1 py-2 <#if !active>text-white bg-dark</#if>">
                    <div class="col-3">
                        <div class="row d-flex justify-content-center">
                            <img class="img-thumbnail" style="height: 120px; width: 200px"
                                 src="${post.pictureUrl}">
                        </div>
                    </div>
                    <div class="col-5 d-flex align-items-start flex-column">
                        <div class="row d-flex justify-content-center w-100">
                            <span class="font-weight-bold text-secondary pr-2">Заголовок:</span><span
                                    class="font-weight-normal">"${post.title}"</span>
                        </div>
                        <div class="row d-flex justify-content-center w-100 mt-auto">
                            <span class="font-weight-bold text-secondary pr-2">Дата:</span><span
                                    class="font-weight-normal">"${post.createdAt}"</span>
                        </div>
                    </div>
                    <div class="col-4">

                        <form action="/admin/posts/activate/${post.id}" method="post">
                            <div class="row d-flex justify-content-center p-1">
                                <a href="/admin/posts/${post.id}" role="button"
                                   class="btn btn-info btn-sm align-middle m-1">Переглянути</a>
                                <#if !active>
                                    <button class="btn btn-success btn-sm m-1">Активувати</button>
                                    <input type="hidden" name="${_csrf.parameterName}"
                                           value="${_csrf.token}"/>
                                </#if>
                            </div>
                        </form>


                        <div class="row d-flex justify-content-center p-1">
                            <@security.authorize access="hasRole('ROLE_WRITER')">

                                <a href="/admin/posts/edit/${post.id}" role="button"
                                   class="btn btn-warning btn-sm m-1 w-40">Редагувати</a>
                            </@security.authorize>

                            <#if active>
                                <a href="/admin/posts/deactivate/${post.id}"
                                   role="button"
                                   class="btn btn-danger btn-sm m-1 w-40">
                                    Перемістити в архів
                                </a>
                            <#else >
                                <a href="/admin/posts/delete/${post.id}"
                                   role="button"
                                   class="btn btn-danger btn-sm m-1 w-40">
                                    Видалити назавжди
                                </a>
                            </#if>
                        </div>

                    </div>
                </div>
            </#list>
        </#if>
    </div>
    <div class="row">
        <ul class="pagination mx-auto">
            <#list 1..posts.totalPages as pageNumber>
                <form action="/admin/posts" method="get">
                    <li class="page-item">
                        <button type="submit"
                                <#if pageNumber - 1 == posts.number>style="background-color: gray" </#if>
                                class="page-link">${pageNumber}
                        </button>
                    </li>
                    <#if active??><input type="hidden" name="active"
                                         value="${postFilters.active?string('true', 'false')}"></#if>
                    <#if postFilters.title??><input type="hidden" name="title" value="${postFilters.title}"></#if>
                    <#if postFilters.categoryId??><input type="hidden" name="categoryId"
                                                         value="${postFilters.categoryId}"></#if>
                    <#if postFilters.authorPublicAccountId??>
                        <input type="hidden" name="authorPublicAccountId" value="${postFilters.authorPublicAccountId}">
                    </#if>
                    <input type="hidden" name="page" value="${pageNumber}">
                </form>
            </#list>
        </ul>
    </div>
</div>
<#include "../include/footer.ftl">
