<#include "../include/header.ftl">

<div class="col-md-7 mt-5 p-5 rounded bg-light">
    <form method="POST" action="/admin/users/new">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Ім'я користувача</span>
            </div>
            <input type="text" class="form-control" name="username" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Пароль</span>
            </div>
            <input type="password" class="form-control" name="password" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Повтор паролю</span>
            </div>
            <input type="password" class="form-control" name="repeatedPassword" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Ім'я</span>
            </div>
            <input type="text" class="form-control" name="name" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Прізвище</span>
            </div>
            <input type="text" class="form-control" name="surname" required>
        </div>
        <button class="btn btn-primary">Додати користувача</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>

<#include "../include/toastr.ftl">
<#include "../include/footer.ftl">