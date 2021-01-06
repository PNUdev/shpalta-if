<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >

<@c.categories categories />

<!-- ToDo we should have dropdown with possible sort variations;
 applying of sort, that is different then current, should trigger reload of the page with new "sort" query param -->

<#if title??>
    <p>
        Results by title '${title}'
    </p>
</#if>
<div id="feed-main">

</div>

<script>
    // ToDo feel free to rewrite this piece of code in scope of UI implementation
    //  (it's just a demo of desired behaviour) as long as you keep the same approach

    let categoryParam = '${(category)!}';
    let sortParam = '${(sort)!}';
    let title = '${(title)!}';
    let authorPublicAccountId = '${(authorPublicAccountId)!}';

    $(window).on('load scroll', function () {
            if ($(window).scrollTop() + $(window).height() === $(document).height()) {

                let requestUrl = '/posts/partial?page=1'; // ToDo store previous page number and increment it for every new segment

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
                })
            }
        }
    );
</script>

<#include "../include/footer.ftl" >