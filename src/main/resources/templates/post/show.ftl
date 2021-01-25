<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >
<#include "../include/facebookSocialPlugin.ftl">

<@c.categories categories />

<ul>
    <li>Title: ${post.title!}</li>
    <li>Picture url: ${post.pictureUrl!}</li>
    <li>Category: ${(post.category.title)!}</li>
    <li>Date: ${(post.createdAt)!}</li>
    <li>Author: ${post.authorPublicAccount.getSignature()!}</li>
    <li>${(post.content)!}</li>
    <li>
        <div class="fb-like"
             data-href="${appBasePath}/posts/${post.id}"
             data-width=""
             data-layout="standard"
             data-action="like"
             data-size="large"
        ></div>
    </li>
   <li>
       <div class="fb-comments"
             data-colorscheme="dark"
             data-href="${appBasePath}/posts/${post.id}"
             data-width=""
             data-numposts="3"
       ></div>
   </li>
</ul>

<#include "../include/footer.ftl" >
