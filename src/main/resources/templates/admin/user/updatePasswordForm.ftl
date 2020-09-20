<#include "../include/header.ftl">

<div class="col-md-7 mt-5 p-5 rounded bg-light">
    <form method="POST" action="/admin/users/update-password">
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Старий пароль</span>
            </div>
            <input type="password" class="form-control" name="oldPassword" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Новий пароль</span>
            </div>
            <input type="password" class="form-control" name="newPassword" required>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend">
                <span class="input-group-text">Повтор нового паролю</span>
            </div>
            <input type="password" class="form-control" name="newPasswordRepeated" required>
        </div>
        <button class="btn btn-primary">Оновити пароль</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>

<#include "../include/toastr.ftl">
<#include "../include/footer.ftl">