<script>
    // prevent double form submission
    $('button').click(function () {
        if (!!document.getElementById("postForm")) {
            return;
        }
        const btn = $(this);
        const form = btn.closest('form');
        if (form[0] && form[0].checkValidity()) {
            setTimeout(function () {
                btn.prop('disabled', true);
            }, 0);
        }
    });
</script>

<#include './toastr.ftl' >
</body>
</html>