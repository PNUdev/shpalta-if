<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >
<#include "../include/facebookSocialPlugin.ftl">

<main class="wrapper">
    <@c.categories categories />

    <ul class="post-show-wrapper">
        <li class="post-title">${post.title!}</li>

        <li>
            <ul class="post-stats">
                <li>
                    <a href="/accounts/${post.authorPublicAccount.id}" title="Переглянути профіль автора">
                        <i class="fas fa-user-circle"></i>
                        <span class="post-author">${post.authorPublicAccount.getSignature()!}</span>
                    </a>
                </li>
                <li class="post-category">${(post.category.title)!}</li>
                <li class="post-created-at">${(post.createdAt)!}</li>
            </ul>
        </li>

        <li class="post-img" data-pictureUrl="${(post.pictureUrl)!}"></li>

        <li class="post-content">${(post.content)!}</li>

        <li class="post-fb_like">
            <div class="fb-like"
                 data-href="${appBasePath}/posts/${post.id}"
                 data-width=""
                 data-layout="standard"
                 data-action="like"
                 data-size="large"
            ></div>
        </li>

        <li class="post-fb_comments">
            <div class="fb-comments"
                 data-colorscheme="dark"
                 data-href="${appBasePath}/posts/${post.id}"
                 data-width=""
                 data-numposts="3"
            ></div>
        </li>
    </ul>
</main>

<#include "../include/footer.ftl" >

<script>
    $(".post-created-at").each((idx, el) => {
        el.innerText = el.innerText.replace("T", " | ");
    })

    $(".post-img").each(function () {
        $(this).css("background-image", "url(" + $(this).data("pictureurl") + ")")
    })
</script>