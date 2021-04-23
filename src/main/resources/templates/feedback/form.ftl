<#include "../include/header.ftl" >
<form method="POST" action="/feedbacks/create" class="feedback">
    <h2>Залиште відгук.</h2>

    <input type="text" name="userInfo" class="name" placeholder="Ваш email (необов'язково)">
    <textarea aria-label="Відгук / пропозиція" name="content"
              placeholder="Що ви хотіли б запропонувати чи покращити?"></textarea>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <button>Відправити</button>
</form>

<#include "../include/footer.ftl" >