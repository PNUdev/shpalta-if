<#if error?? >
    <div>${error}</div>
<#else>
    <#list posts.content as post >
        <div class="post">
            <div class="post__preview">
                <img src="${post.pictureUrl}" alt="NOT FOUND!">
            </div>

            <div class="post__info">
                <a href="/posts/${post.id}">
                    <h3 class="post__info__title">${post.title}</h3>
                    <hr style="border-top-color: ${(post.category.colorTheme)!};">
                </a>

                <div class="post__info__bottom">
                    <div>
                        <p>
                            <i class="far fa-calendar-alt"></i>
                            <span class="post__info__bottom__created-at" data-date="${post.createdAt}"></span>
                        </p>

                        <a href="/feed/${post.category.publicUrl}">
                            <i class="fas fa-tag"></i>
                            <span class="post__info__bottom__link">${(post.category.title)!}</span>
                        </a>

                        <a href="/accounts/${post.authorPublicAccount.id}">
                            <i class="fas fa-user-edit"></i>
                            <span class="post__info__bottom__link">${post.authorPublicAccount.getSignature()!}</span>
                        </a>
                    </div>

                    <a class="link-to-post" href="/posts/${post.id}" style="color: ${(post.category.colorTheme)!};">Читати</a>
                </div>

            </div>

        </div>
    </#list>
</#if>

<script>
    if (categoryParam === "" && sortParam === "" && authorPublicAccountId === "") {
        let post = $('.post')[0];
        post.classList.add("post-headliner");
    }

    $(".post__info__bottom__created-at").each((idx, el) => {
        let date = new Date(el.dataset.date);
        let monthName = date.toLocaleString('ukr', {month: 'short'});
        monthName = monthName[0].toUpperCase() + monthName.substring(1);

        el.innerText = date.getDate() + " " + monthName;
    })

    $(".post__info__bottom__created-at_full").each((idx, el) => {
        el.innerText = el.innerText.replace("T", " ");
    })
</script>



