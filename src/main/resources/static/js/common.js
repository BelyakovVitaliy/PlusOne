$(document).ready(function () {
	$('.nav-link').each(function (i, e) {
		if (e.innerText === $('#pageTitle')[0].innerText) {
			e.className = e.className + " active"
		}
	});
});

