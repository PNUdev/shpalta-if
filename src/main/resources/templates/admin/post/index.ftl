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
    <div class="container py-3 <#if !active>text-white bg-dark</#if>">
        <#if !posts.content?has_content >
            <h2 class="text-center"><#if active>Список постів пустий<#else>Архів пустий</#if></h2>
        <#else>

            <#list posts.content?chunk(2) as row>
                <div class="row">
                    <#list row as post>
                        <div class="col-6">
                            <div class="row mb-4">
                                <div class="col-6">
                                    <div class="row pl-4 d-flex justify-content-center">
                                        <img class="img-thumbnail" style="height: 150px; width: 200px"
                                             src="${post.pictureUrl}">
                                    </div>
                                </div>
                                <div class="col-6 d-flex align-items-start flex-column">
                                    <div class="row d-flex justify-content-center w-100">
                                        <span class="font-weight-bold text-secondary pr-2">Заголовок:</span><span
                                                class="font-weight-normal">"${post.title}"</span>
                                    </div>
                                    <div class="row d-flex justify-content-center w-100 mt-auto">
                                        <span class="font-weight-bold text-secondary pr-2">Дата:</span><span
                                                class="font-weight-normal">"${post.createdAt}"</span>
                                    </div>
                                    <div class="row d-flex justify-content-center">
                                        <a href="/admin/posts/${post.id}" role="button"
                                           class="btn btn-info btn-sm btn-block m-1">Переглянути</a>
                                        <@security.authorize access="hasRole('ROLE_WRITER')">
                                            <a href="/admin/posts/edit/${post.id}" role="button"
                                               class="btn btn-warning btn-sm m-1 w-40">Редагувати</a>
                                        </@security.authorize>
                                        <a href="/admin/posts/<#if active>deactivate<#else>delete</#if>/${post.id}"
                                           role="button"
                                           class="btn btn-danger btn-sm m-1 w-40">
                                            <#if active>Перемістити в архів<#else>Видалити назавжди</#if>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </#list>
                </div>
            </#list>
        </#if>
    </div>
    <div class="row">
        <ul class="pagination mx-auto">
            <#list 1..posts.totalPages as pageNumber>
                <form action="/admin/posts" method="get">
                    <input type="hidden" name="postFiltersDto" value="${postFilters}">
                    <input type="hidden" name="page" value="${pageNumber -1}">
                    <li class="page-item">
                        <button type="submit"
                                <#if pageNumber - 1 == pageable.pageNumber>style="background-color: gray" </#if>
                                class="page-link">${pageNumber}
                        </button>
                    </li>
                    <#if name??>
                        <input type="hidden" name="name" value="${name}">
                    </#if>
                    <#if active??>
                        <input type="hidden" name="active" value="${active?string("true", "false")}">
                    </#if>
                    <input type="hidden" name="page" value="${pageNumber}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </#list>
        </ul>
    </div>
</div>
<#include "../include/footer.ftl">
