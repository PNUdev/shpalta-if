<#include "../include/header.ftl">
<h2 class="text-center my-3">Відгуки</h2>

<#if !feedbacks?has_content >
    <h3 class="text-center">Список відкритих відповідей пустий</h3>
<#else>
    <table class="table table-striped mx-3">
        <tbody>
        <#list feedbacks.getContent() as feedback >
            <tr class="container <#if !(feedback.reviewed)>table-success</#if>">
                <th scope="row">
                    <div class="row">
                        <div class="col-9">
                            <p class="px-3">${feedback.content}</p>
                        </div>
                        <div class="col-3">
                            <div class="row d-flex justify-content-center">
                                <form class="form-inline"
                                      action="/admin/feedbacks/deactivate/${feedback.id}"
                                      method="post">
                                    <div class="d-flex justify-content-center p-1">
                                        <button class="btn btn-warning btn-sm m-1">Видалити</button>
                                        <input type="hidden" name="${_csrf.parameterName}"
                                               value="${_csrf.token}"/>
                                    </div>
                                </form>
                            </div>
                            <div class="row d-flex justify-content-center">
                                <#if feedback.userInfo?has_content>
                                    <p class="float right">Зв'язатись з користувачем:</p>
                                    <p>${feedback.userInfo}</p>
                                </#if>
                            </div>
                        </div>
                    </div>
                </th>
            </tr>
        </#list>
        </tbody>
    </table>
    <div class="row">
        <ul class="pagination mx-auto">
            <#list 1..feedbacks.totalPages as pageNumber>
                <form action="/admin/feedbacks" method="get">
                    <li class="page-item">
                        <button type="submit"
                                <#if pageNumber - 1 == feedbacks.number>style="background-color: gray" </#if>
                                class="page-link">${pageNumber}
                        </button>
                    </li>
                    <input type="hidden" name="page" value="${pageNumber}">
                </form>
            </#list>
        </ul>
    </div>
</#if>
<#include "../include/footer.ftl">