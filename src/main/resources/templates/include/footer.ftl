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

        <div class="copyright d-flex justify-content-between align-items-center">
          <div class="text">© Copyright 2020 IF Shpalta</div>
          <div class="menu">
            <a href="#" class="mr-2">Твоє здоров'я</a>
            <a href="#" class="mr-2">Івано-Франківськ</a>
            <a href="#" class="mr-2">Політика</a>
            <a href="#">Технології</a>
          </div>
        </div>
      </div>
    </footer>

    <script>
      const searchBox = document.querySelector(".search-box");
      const searchBtn = document.querySelector(".search-icon");
      const cancelBtn = document.querySelector(".cancel-icon");
      const searchInput = document.querySelector("input");
      const searchData = document.querySelector(".search-data");

      searchBtn.onclick =()=>{
        searchBox.classList.add("active");
        searchBtn.classList.add("active");
        searchInput.classList.add("active");
        cancelBtn.classList.add("active");
        searchInput.focus();

        if(searchInput.value != ""){
          var values = searchInput.value;
          searchData.classList.remove("active");
          searchData.innerHTML = "You just typed " + "<span style='font-weight: 500;'>" + values + "</span>";
        }else{
          searchData.textContent = "";
        }
      }
      cancelBtn.onclick =()=>{
        searchBox.classList.remove("active");
        searchBtn.classList.remove("active");
        searchInput.classList.remove("active");
        cancelBtn.classList.remove("active");
        searchData.classList.toggle("active");
        searchInput.value = "";
      }
    </script>
  </body>
</html>
