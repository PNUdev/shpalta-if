<#macro categories categories>
    <#assign categoriesColor = "#B18282">

    <#if category??>
        <#list categories?filter(categ -> categ.publicUrl = category) as categ>
            <#assign categoriesColor = categ.colorTheme>
        </#list>
    </#if>

    <aside class="categories">
        <h1 class="categories__title">Категорії</h1>

        <ul class="categories-list">
            <li class="categories__item">
                <a href="/feed/">
                    <i style='color:#B18282' class="categories__item_label"></i>
                    <span>Актуальне</span>
                </a>
            </li>

            <#list categories as category>
                <li class="categories__item">
                    <a href="/feed/${category.publicUrl}">
                        <i style='color:${category.colorTheme}' class="categories__item_label"></i>
                        <span>${category.title}</span>
                    </a>
                </li>
            </#list>
        </ul>

    </aside>

    <script>
        (function setCategoriesColor() {
            let color = "${categoriesColor?js_string}"
            $('.categories__title').css({'border-color': color});

            $(".categories__item_label[style='color:" + color + "']").each(function () {
                this.parentElement.style.transform = "translate(1.5em)";
            })
        }())
    </script>

</#macro>
