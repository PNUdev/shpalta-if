<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >
<#include "../include/facebookSocialPlugin.ftl">

<@c.categories categories />

<main id="wrapper">
    <ul class="post-show-wrapper">
        <li class="post-title">${post.title!}</li>

        <ul class="post-stats">
            <a href="/accounts/${post.authorPublicAccount.id}" title="Переглянути профіль автора">
                <i class="fas fa-user-circle"></i>
                <span class="post-author">${post.authorPublicAccount.getSignature()!}</span>
            </a>
            <li class="post-category">${(post.category.title)!}</li>
            <li class="post-created-at">${(post.createdAt)!}</li>
        </ul>

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
</script>