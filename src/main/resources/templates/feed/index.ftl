<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >
<div class="row">
	<@c.categories categories />

	<!-- ToDo we should have dropdown with possible sort variations;
	applying of sort, that is different then current, should trigger reload of the page with new "sort" query param -->

	<div class="col-9">
		<img src="images/og.jpg" alt="" class="main-image mb-3">
		<#if title??>
			<h3 class="posts-header">Результати за запитом '${title}'</h3>
		<#else>
			<h3 class="posts-header">Цікаві статті</h3>
		</#if>
		<div id="feed-main">
			<div class="posts row">
				<div class="post col-4 mb-3">
					<a href="#" class="w-100 pb-2"><img src="https://shpalta.media/wp-content/uploads/2021/01/2020-05-18-02.15.04-1-1.jpg" class="thumbnail w-100"></a>

					<div class="caption">
						<div class="caption-title pb-2">
							<h3><a href="#">Скільки мають тривати онлайн-заняття школярів на дистанційці</a></h3>
						</div>
						<div class="created-at d-flex align-items-center">
							<img src="images/calendar-alt-solid.svg" alt="" class="mr-2">
							<span>11.01.2021 20:37</span>
						</div>
					</div>
				</div>

				<div class="post col-4 mb-3">
					<a href="#" class="w-100 pb-2"><img src="https://shpalta.media/wp-content/uploads/2021/01/2020-05-18-02.15.04-1-1.jpg" class="thumbnail w-100"></a>

					<div class="caption">
						<div class="caption-title pb-2">
							<h3><a href="#">Скільки мають тривати онлайн-заняття школярів на дистанційці</a></h3>
						</div>
						<div class="created-at d-flex align-items-center">
							<img src="images/calendar-alt-solid.svg" alt="" class="mr-2">
							<span>11.01.2021 20:37</span>
						</div>
					</div>
				</div>

				<div class="post col-4 mb-3">
					<a href="#" class="w-100 pb-2"><img src="https://shpalta.media/wp-content/uploads/2021/01/2020-05-18-02.15.04-1-1.jpg" class="thumbnail w-100"></a>

					<div class="caption">
						<div class="caption-title pb-2">
							<h3><a href="#">Скільки мають тривати онлайн-заняття школярів на дистанційці</a></h3>
						</div>
						<div class="created-at d-flex align-items-center">
							<img src="images/calendar-alt-solid.svg" alt="" class="mr-2">
							<span>11.01.2021 20:37</span>
						</div>
					</div>
				</div>

				<div class="post col-4 mb-3">
					<a href="#" class="w-100 pb-2"><img src="https://shpalta.media/wp-content/uploads/2021/01/2020-05-18-02.15.04-1-1.jpg" class="thumbnail w-100"></a>

					<div class="caption">
						<div class="caption-title pb-2">
							<h3><a href="#">Скільки мають тривати онлайн-заняття школярів на дистанційці</a></h3>
						</div>
						<div class="created-at d-flex align-items-center">
							<img src="images/calendar-alt-solid.svg" alt="" class="mr-2">
							<span>11.01.2021 20:37</span>
						</div>
					</div>
				</div>

				<div class="post col-4 mb-3">
					<a href="#" class="w-100 pb-2"><img src="https://shpalta.media/wp-content/uploads/2021/01/2020-05-18-02.15.04-1-1.jpg" class="thumbnail w-100"></a>

					<div class="caption">
						<div class="caption-title pb-2">
							<h3><a href="#">Скільки мають тривати онлайн-заняття школярів на дистанційці</a></h3>
						</div>
						<div class="created-at d-flex align-items-center">
							<img src="images/calendar-alt-solid.svg" alt="" class="mr-2">
							<span>11.01.2021 20:37</span>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

	<!-- <script>
		// ToDo feel free to rewrite this piece of code in scope of UI implementation
		//  (it's just a demo of desired behaviour) as long as you keep the same approach
		const categoryParam = '${(category)!}';
		const sortParam = '${(sort)!}';
		const title = '${(title)!}';
		const authorPublicAccountId = '${(authorPublicAccountId)!}';

		$(window).on('load scroll', function () {
				if ($(window).scrollTop() + $(window).height() === $(document).height()) {
					let requestUrl = '/posts/partial?page=1'; // ToDo store previous page number and increment it for every new segment

					if (categoryParam) requestUrl += '&categoryUrl=' + categoryParam;
					if (sortParam) requestUrl += '&sort=' + sortParam;
					if (title) requestUrl += '&title=' + title;
					if (authorPublicAccountId) requestUrl += '&authorPublicAccountId=' + authorPublicAccountId;

					$.get(requestUrl, response => $("#feed-main posts").append(response))
				}
			}
		);
	</script> -->

<#include "../include/footer.ftl" >
