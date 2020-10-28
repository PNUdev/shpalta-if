<#include "../include/header.ftl">

<div class="mx-auto mt-5 p-5 rounded bg-light col-md-9">
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
                <label for="role_selection" class="input-group-text">Роль</label>
            </div>
            <select data-live-search="false" title="Роль" class="form-control selectpicker" name="role"
                    id="role_selection" required>
                <option value="${roleWriter}" selected>
                    ${roleWriter.getUkrainianName()}
                </option>
                <option value="${roleEditor}">
                    ${roleEditor.getUkrainianName()}
                </option>
            </select>
        </div>
        <script type="text/javascript">
            let role_field = $('#role_selection');
            role_field.on('change', function () {
                if (role_field.val() == "ROLE_WRITER") {
                    $('#name_input').prop('required', true);
                    $('#surname_input').prop('required', true);
                    $('#name_surname_inputs').show();
                } else {
                    $('#name_input').prop('required', false);
                    $('#surname_input').prop('required', false);
                    $('#name_surname_inputs').hide();
                }
            })
        </script>
        <div id="name_surname_inputs">
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">Ім'я</span>
                </div>
                <input type="text" id="name_input" class="form-control" name="name" required>
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend">
                    <span class="input-group-text">Прізвище</span>
                </div>
                <input type="text" id="surname_input" class="form-control" name="surname" required>
            </div>
        </div>
        <button class="btn btn-primary">Додати користувача</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
</div>

<#include "../include/footer.ftl">