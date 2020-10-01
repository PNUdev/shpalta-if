<#include "../include/header.ftl" >

<ul>
    <li>Title: ${post.title!}</li>
    <li>Picture url: ${post.pictureUrl!}</li>
    <li>Category: ${(post.category.title)!}</li>
    <li>Date: ${(post.createdAt)!}</li>
    <li>Author: ${post.authorPublicAccount.g!} ${post.authorPublicAccount.name!}</li>
    <li>${(post.content)!}</li>

</ul>

<#include "../include/footer.ftl" >
