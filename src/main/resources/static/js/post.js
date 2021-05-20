$(document).ready(function() {
  $(".post__stats__created-at").each((idx, el) => {
    el.innerText = el.innerText.replace("T", " | ");
  })

  $(".post__preview").each(function () {
    $(this).css("background-image", "url(" + $(this).data("pictureurl") + ")")
  })
})
