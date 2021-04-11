<#if error?? >
    <div>${error}</div>
<#else>
    <#list posts.content as post >
        <div class="post">
            <div class="post-img">
                <img src="${post.pictureUrl}" alt="NOT FOUND!">
            </div>

            <div class="post-info">
                <a href="/posts/${post.id}"><h3 class="post-title">${post.title}</h3></a>

                <div class="post-info_bottom">
                    <div>
                        <p>
                            <i class="far fa-calendar-alt"></i>
                            <span class="post__created-at" data-date="${post.createdAt}"></span>
                        </p>

                        <a href="/accounts/${post.authorPublicAccount.id}">
                            <i class="fas fa-user-edit"></i>
                            <span class="post__author">${post.authorPublicAccount.getSignature()!}</span>
                        </a>
                    </div>

                    <a class="post-btn" href="/posts/${post.id}">Читати</a>
                </div>

            </div>

        </div>
    </#list>
</#if>

<script>
    if (categoryParam === "") {
        let post = $('.post:first-child');
        post.addClass("post-headliner");
    }

    $(".post__created-at").each((idx, el) => {
        let date = new Date(el.dataset.date);
        let monthName = date.toLocaleString('ukr', {month: 'short'});
        monthName = monthName[0].toUpperCase() + monthName.substring(1);

        el.innerText = date.getDate() + " " + monthName;
    })

    $(".post__created-at_full").each((idx, el) => {
        el.innerText = el.innerText.replace("T", " ");
    })
</script>



