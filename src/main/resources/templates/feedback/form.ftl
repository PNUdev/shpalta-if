<#include "../include/header.ftl" >
<form method="POST" action="/feedbacks/create">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    Що ви хотіли б запропонувати чи покращити?
    <textarea aria-label="Відгук / пропозиція" name="content" required></textarea>
    <input type="text" name="userInfo">(необов'язково)
    <button>Відправити</button>
</form>

<#include "../include/footer.ftl" >