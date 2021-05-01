<#include "../include/header.ftl">

<div class="row d-flex justify-content-around">
    <div class="col-md-9 mt-5 p-5 rounded bg-light">
        <form method="POST">
            <div class="input-group mb-3">
                <div class="input-group">
                    <div class="input-group-prepend">
                        <span class="input-group-text">Текст повідомлення</span>
                    </div>
                    <textarea class="form-control" name="content" rows="5" required></textarea>
                </div>
            </div>
            <div class="form-check mb-3">
                <input class="form-check-input" name="shareForAllUsers" type="checkbox"
                       id="shareForAllUsers">
                <label class="form-check-label" for="shareForAllUsers">
                    Поширити для усіх категорій
                </label>
            </div>
            <div class="input-group mb-3">
                <select data-live-search="false" title="Категорії" class="form-control category-select"
                        name="categoryId"
                        id="categorySelect" required>
                    <option disabled selected value> -- Виберіть категорію --</option>
                    <#list categories as category>
                        <option value="${category.id}">
                            ${category.title}
                        </option>
                    </#list>
                </select>
            </div>
            <div>
                <button class="btn btn-primary btn-block" id="btn-submit">Поширити</button>
            </div>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
</div>

<link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css" rel="stylesheet"/>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>
<script>
    $(document).ready(function () {
        $('.category-select').select2();
        $('#shareForAllUsers').change(function () {
            const categorySelect = $('#categorySelect')
            if (this.checked) {
                categorySelect.val('').trigger('change');
                categorySelect.prop('disabled', true);
                categorySelect.prop('required', false);
            } else {
                categorySelect.prop('disabled', false);
                categorySelect.prop('required', true);
            }
        });
    });
</script>

<#include "../include/footer.ftl">