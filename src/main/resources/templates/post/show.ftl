<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >
<#include "../include/facebookSocialPlugin.ftl">

<main class="wrapper">
    <@c.categories categories />

    <ul class="post-show-wrapper">
        <li class="post__title">${post.title!}</li>

        <li>
            <ul class="post__stats">
                <li>
                    <a href="/accounts/${post.authorPublicAccount.id}" title="Переглянути профіль автора">
                        <i class="fas fa-user-circle"></i>
                        <span class="post__stats__author">${post.authorPublicAccount.getSignature()!}</span>
                    </a>
                </li>
                <li class="post__stats__category">${(post.category.title)!}</li>
                <li class="post__stats__created-at">${(post.createdAt)!}</li>
            </ul>
        </li>

        <li class="post__preview" data-pictureUrl="${(post.pictureUrl)!}"></li>

        <li class="post__content">${(post.content)!}</li>

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

<script src="/js/post.js"></script>
