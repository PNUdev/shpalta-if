<#import '../include/categories.ftl' as c >
<#include "../include/header.ftl" >
<div class="row">
	<div class="col-3 d-none d-md-block">
		<div class="categories-container">
			<@c.categories categories />
		</div>
	</div>

	<!-- ToDo we should have dropdown with possible sort variations;
	applying of sort, that is different then current, should trigger reload of the page with new "sort" query param -->

	<div class="col-12 col-md-9">
		<img src="images/og.jpg" alt="" class="main-image mb-3">
		<#if title??>
			<h3 class="posts-header">Результати за запитом '${title}'</h3>
		<#else>
			<h3 class="posts-header">Цікаві статті</h3>
		</#if>
		<div id="feed-main">
			<div class="posts"></div>
			<div class="lds-roller d-none"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div>
		</div>
	</div>
</div>

<script>
	const categoryParam = '${(category)!}';
	const sortParam = '${(sort)!}';
	const title = '${(title)!}';
	const authorPublicAccountId = '${(authorPublicAccountId)!}';
	let page = 1;
	let shoudContinue = true;

	const loadPosts = () => {
		let requestUrl = "/posts/partial?page=" + page;
		if (categoryParam.length > 0) 				requestUrl += '&categoryUrl=' + categoryParam;
		if (sortParam.length > 0) 						requestUrl += '&sort=' + sortParam;
		if (title.length > 0) 								requestUrl += '&title=' + title;
		if (authorPublicAccountId.length > 0) requestUrl += '&authorPublicAccountId=' + authorPublicAccountId;

		if (shoudContinue) {
			$('.lds-roller').removeClass("d-none");

			$.get(requestUrl, response => {
				$('.lds-roller').addClass("d-none");
				if (response.length == 0) shoudContinue = false;
				$("#feed-main .posts").append(response)
			})
		}
	}

	$(window).on('load', loadPosts(1));
	$(window).on('scroll', function() {
		if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight && shoudContinue) {
			page += 1;
			loadPosts();
		}
	});
</script>

<#include "../include/footer.ftl" >
