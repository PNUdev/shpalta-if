<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >

<main class="wrapper">
    <@c.categories categories />


    <ul class="account">
        <a href="/" id="home-btn"><i class="fas fa-caret-left"></i> Повернутись на головну</a>
        <ul class="account-info">
            <li class="account-name">${account.getSignature()}</li>
            <li><span>Ім'я:</span> ${account.name}</li>
            <li><span>Прізвище: </span>${account.surname}</li>
                <span>Опис: </span>
                <p class="account-desc">${(account.description)!}</p>
            </li>

            <a class="account-posts-btn" href="/feed?author=${account.id}">Переглянути всі пости автора</a>
        </ul>

        <li class="accounts-item__img account-img">
            <img src="${(account.profileImageUrl)!}" alt="NOT FOUND!"
                 onerror="this.src='/images/public_account_default.png'">
        </li>

    </ul>

</main>

<script>
    $(".account-created-at").each((idx, el) => {
        el.innerText = el.innerText.replace("T", " ");
    })
</script>

<#include "../admin/include/toastr.ftl" > <!-- Included here to show toast after account update -->
<#include "../include/footer.ftl" >
