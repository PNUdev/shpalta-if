<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >

<main class="wrapper">
    <@c.categories categories />

    <div id="feed-main">
        <div class="feed-main-management">
            <form action="" class="filter" onchange="this.submit()">
                <select name="sort" id="sortBy">
                    <option value="createdAt">За датою публікації</option>
                    <option value="title">За назвою</option>
                    <option value="category">За категорією</option>
                    <option value="authorPublicAccount">За нікнеймом автора</option>
                </select>

                <select name="sort" id="sortDir">
                    <option value="desc">За спаданням</option>
                    <option value="asc">За зростанням</option>
                </select>

                <#if authorPublicAccountId??>
                    <input type="hidden" name="author" value='${(authorPublicAccountId)!}'>
                </#if>

            </form>
        </div>

        <#if title??>
            <p class="feed-main__search-by">
                Результати пошуку за '${title}':
            </p>
        </#if>
    </div>

    <script>
        // ToDo feel free to rewrite this piece of code in scope of UI implementation
        //  (it's just a demo of desired behaviour) as long as you keep the same approach

        const categoryParam = '${(category)!}';
        const sortParam = '${(sort)!}';
        const title = '${(title)!}';
        const authorPublicAccountId = '${(authorPublicAccountId)!}';

        let page = 1;
        let isEnd = false;

        $(window).on('load scroll', function () {
                if (isEnd) {
                    return;
                }
                if ($(window).scrollTop() + $(window).height() >= $(document).height() - 160) {
                    let requestUrl = '/posts/partial?page=' + page;

                    if (categoryParam) {
                        requestUrl += '&categoryUrl=' + categoryParam;
                    }

                    if (sortParam) {
                        requestUrl += '&sort=' + sortParam;
                    }

                    if (title) {
                        requestUrl += '&title=' + title;
                    }

                    if (authorPublicAccountId) {
                        requestUrl += '&authorPublicAccountId=' + authorPublicAccountId;
                    }

                    $.get(requestUrl, response => {
                        $("#feed-main").append(response)
                        isEnd = response.trim().startsWith("<script>")
                    })

                    page++;
                }
            }
        );

        let [sortBy, sortDir] = sortParam.split(",");
        if (sortBy && sortDir) {
            $('#sortBy')[0].value = sortBy;
            $('#sortDir')[0].value = sortDir;
        }

    </script>
</main>
<#include "../include/footer.ftl" >
