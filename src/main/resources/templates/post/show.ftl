<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >

<@c.categories categories />

<ul>
    <li>Title: ${post.title!}</li>
    <li>Picture url: ${post.pictureUrl!}</li>
    <li>Category: ${(post.category.title)!}</li>
    <li>Date: ${(post.createdAt)!}</li>
    <li>Author: ${post.authorPublicAccount.name!} ${post.authorPublicAccount.surname!}</li>
    <li>${(post.content)!}</li>
</ul>

<#include "../include/footer.ftl" >
