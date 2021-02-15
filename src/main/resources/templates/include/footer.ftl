      </div>
    </div>

    <footer class="mt-5">
      <div class="triangle"></div>

      <div class="footer-container">
        <div class="socials d-flex justify-content-center align-items-center my-5">
          <a href="#" class="button" target="_blank">
            <div class="icon"><i class="fab fa-facebook-f"></i></div>
            <span>Facebook</span>
          </a>

          <a href="#" class="button" target="_blank">
            <div class="icon"><i class="fab fa-instagram"></i></div>
            <span>Instagram</span>
          </a>

          <a href="#" class="button" target="_blank">
            <div class="icon"><i class="fab fa-youtube"></i></div>
            <span>YouTube</span>
          </a>

          <div class="clearfix"></div>
        </div>

        <div class="copyright d-flex justify-content-center align-items-center">
          <div class="text">© Copyright 2020 IF Shpalta</div>
        </div>
      </div>
    </footer>

    <script>
      const submitForm = () => {
        if (searchInput.value != "") {
          let value = searchInput.value;

          searchData.classList.remove("d-none");
          searchData.innerHTML = "<div class='type-result'>Ви написали " + "<span class='keyword'>" + value + "</span></div>";
        } else {
          searchData.textContent = "";
        }

        documnet.querySelector("form#search-form").submit();
      }
    </script>

    <script>
      $(".show-categories").on("click", function() {
        $(".categories-mobile").addClass("active");
        $("body").addClass('no-scroll');
      });

      $(".close-categories").on("click", function() {
        $(".categories-mobile").removeClass("active");
        $("body").removeClass('no-scroll');
      });

      $(".search-icon").on("click", function() {
        if($(this).hasClass("active")) {
          $("form#search-form").submit();
        }

        $(".backdrop, .search-box, .search-icon").addClass("active");
        $("input.search-input").focus();
      })

      $("input.search-input").on("input", function() {
        const title = $(this).val().trim();

        if (!title) {
          $('#search-result').html("");
          $("#search-result").addClass("d-none");

          return;
        } else {
          $.get('/posts/search-result-partial', {'title': title}, function(response) {
            let link = $(response);
            let truncatedTitle = link.text().substring(0, 35).trim(this);
            link.text(truncatedTitle + (link.text().length > 35 ? '...' : ''));

            $('#search-result').html(link);
            $("#search-result").removeClass("d-none");
          });
        }
      })

      $(".cancel-icon, .backdrop").on("click", function() {
        $(".backdrop, .search-box, .search-icon").removeClass("active");
        $(".search-data").addClass("d-none");
        $("input.search-input").val("");
      });
    </script>

    <script>
      let topLimit = $('.categories-container .sidebar').offset().top;

      $(window).scroll(function() {
        if (topLimit <= $(window).scrollTop()) {
          $('.categories-container').addClass('sticky');
          console.log(topLimit, $(window).scrollTop() )
        } else {
          $('.categories-container').removeClass('sticky');
        }
      });
    </script>
  </body>
</html>
