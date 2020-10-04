<script>
    // prevent double form submission
    $('button').click(function () {
        // dismiss button disabling for summernote editor
        const summerNoteBtbClassName = 'note-btn'
        if (this.className.includes(summerNoteBtbClassName)) {
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