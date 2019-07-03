$.getScript("js/common.js", function () {
});

var getMoreLink;

function getMore() {
	if (typeof getMoreLink === 'undefined') {
		return
	}
	disableGetMoreBtn();
	const url = '/rest/event/getMoreFromKudaGo';
	let params = {
		link: getMoreLink
	};
	$.ajax({
		type: "POST",
		url: url,
		data: params,
		success: function (e) {
			enableGetMoreBtn();
			appendEventsFromSearchResult(e);
		},
		error: function (e) {
			enableGetMoreBtn();
			defaultErrorHandler(e)
		}
	});
}

function searchEvent() {
	disableSearchBtn();
	document.getElementById('eventsFromKudaGo').innerHTML = '';
	if (checkForm() === false) {
		return;
	}
	const url = "/rest/event/searchInKudaGo";
	let event = {
		title: $('#searchEventForm input[name = "title"]').val(),
		destination: {
			x: $("#positionX")[0].innerText,
			address: $('#searchEventForm input[name = "destination"]').val(),
			y: $("#positionY")[0].innerText,
			radius: $("#search-dest-radius").val()
		},
		startDate: $('#searchEventForm input[name = "startDate"]').val(),
		endDate: $('#searchEventForm input[name = "endDate"]').val(),
		/*tags : $('#tagBox').tagging( "getTags" )*/
	};
	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(event),
		success: function (e) {
			enableSearchBtn();
			appendEventsFromSearchResult(e)
		},
		error: function (e) {
			enableSearchBtn();
			defaultErrorHandler(e)
		}
	});
}

function createEventFormKudaGo() {
	/*if (checkForm() === false) {
		return
	}*/
	url = "/rest/event/add";
	var startDateParts = $("#innerEventStartDate")[0].innerText.split(/[. :]/);
	var endDateParts = $("#innerEventEndDate")[0].innerText.split(/[. :]/);
	var destination = {x: $("#innerPositionX")[0].innerText, y: $("#innerPositionY")[0].innerText,
		address: $("#innerEventDestination")[0].value};
	let event = {
		picture: $("#innerEventPicture")[0].src,
		title: $("#innerEventTitle")[0].innerText,
		description: $("#innerEventDescription")[0].innerText,
		destination: destination,
		startDate: new Date(startDateParts[2],startDateParts[1]-1,startDateParts[0],startDateParts[3],
			startDateParts[4]).toISOString(),
		endDate: new Date(endDateParts[2],endDateParts[1]-1,endDateParts[0],endDateParts[3],
			endDateParts[4]).toISOString(),
		tags: getTags()
	};
	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(event),
		success: function (e) {
				window.location.href = "/mainBoard";
		},
		error: function (e) {
			defaultErrorHandler(e)
		}
	});
}

function getTags() {
	let tagBlocks = $('#innerEventTagBlock > .tag');
	var tags = [];
	$.each(tagBlocks,function(i,tag) {
		if (tag.innerText.substr(0, 1) !== '#') {
			tags[i] = '#' + tag.innerText
		}else {
			tags[i] = tag.innerText
		}
	});
	return tags;
}

function defaultErrorHandler(e) {
	if (e.responseJSON !== "" && typeof e.responseJSON !=='undefined') {
		alert(e.responseJSON.message)
	} else {
		alert("Произошла ошибка")
	}
}

function appendEventsFromSearchResult(e) {
	var eventList = e.events;
	getMoreLink = e.nextEventsURL;
	if (eventList !== null) {
		if (getMoreLink !== "null") {
			$('#getMore-btn')[0].removeAttribute("hidden");
		} else {
			$('#getMore-btn').attr("hidden", "hidden");
		}
		eventList.forEach(function (event, i, it) {
			var colmd4 = document.createElement("div");
			colmd4.className = "col-md-4";
			var cardmb4shadowsm = document.createElement("div");
			cardmb4shadowsm.className = "card mb-4 shadow-sm";
			cardmb4shadowsm.setAttribute("onclick", "showModal(" + event.id + ")");
			var eventPicture = document.createElement("img");
			eventPicture.className = "card-img-top";
			eventPicture.id = "event-picture-" + event.id;
			if (event.picture !== null) {
				eventPicture.src = event.picture;
			} else {
				eventPicture.src = "/rest/pic/standartEventPicture.png"
			}
			var cardBody = document.createElement("div");
			cardBody.className = "card-body";
			var cardTextTitle = document.createElement("span");
			cardTextTitle.className = "card-text title";
			cardTextTitle.id = "event-title-" + event.id;
			cardTextTitle.innerText = event.title;
			cardBody.appendChild(cardTextTitle);
			var cardTextDescription = document.createElement("span");
			cardTextDescription.className = "card-text description";
			cardTextDescription.id = "event-description-" + event.id;
			cardTextDescription.innerText = event.description;
			cardTextDescription.setAttribute("hidden", "hidden");
			cardBody.appendChild(cardTextDescription);

			if (event.destination !== null) {
				var cardTextDestination = document.createElement("div");
				cardTextDestination.className = "card-text";
				cardTextDestination.id = "event-destination-" + event.id;
				cardTextDestination.innerText = event.destination.address;
				var cardTextDestinationLoc = document.createElement("span");
				cardTextDestinationLoc.hidden = true;
				var cardTextDestinationX = document.createElement("span");
				cardTextDestinationX.id = "positionX-" + event.id;
				cardTextDestinationX.innerText = event.destination.x;
				var cardTextDestinationY = document.createElement("span");
				cardTextDestinationY.id = "positionY-" + event.id;
				cardTextDestinationY.innerText = event.destination.y;
				cardTextDestinationLoc.appendChild(cardTextDestinationX);
				cardTextDestinationLoc.appendChild(cardTextDestinationY);
				cardBody.appendChild(cardTextDestination);
				cardBody.appendChild(cardTextDestinationLoc);
			}
			var dateBlock = document.createElement("div");
			dateBlock.className = "d-flex justify-content-between align-items-center";
			var startDate = document.createElement("small");
			startDate.className = "card-text";
			startDate.id = "event-startDate-" + event.id;
			startDate.value = event.startDate;
			startDate.innerText = new Date(event.startDate).format("dd.mm.yyyy HH:MM");
			dateBlock.appendChild(startDate);
			var endDate = document.createElement("small");
			endDate.className = "card-text";
			endDate.id = "event-endDate-" + event.id;
			endDate.value = event.endDate;
			endDate.innerText = new Date(event.endDate).format("dd.mm.yyyy HH:MM");
			dateBlock.appendChild(endDate);
			cardBody.appendChild(dateBlock);
			var tagBlock = document.createElement("div");
			tagBlock.className = "tag-block";
			tagBlock.id = "tag-block-event-" + event.id;
			event.tags.forEach(function (tag, i, it) {
				var tagEl = document.createElement("span");
				tagEl.className = "tag";
				tagEl.innerText = tag.title;
				tagBlock.appendChild(tagEl);
			});
			cardBody.appendChild(tagBlock);
			cardmb4shadowsm.appendChild(eventPicture);
			cardmb4shadowsm.appendChild(cardBody);
			colmd4.appendChild(cardmb4shadowsm);
			document.getElementById('eventsFromKudaGo').appendChild(colmd4);
		});
	}
	$("#searchEventModal").modal('hide');
}

$(function () {
	$('#searchEventModal').on('hidden.bs.modal', function () {
		$('#searchEventForm input').each(function () {
			$(this).removeClass("is-valid").removeClass("is-invalid");
		});
		enableSearchBtn()
	});
});

function showModal(id) {
	$('#innerEventPicture')[0].setAttribute("src", $('#event-picture-' + id)[0].getAttribute("src"));
	$('#innerEventTitle')[0].innerText = $('#event-title-' + id)[0].innerText;
	$('#innerEventDescription')[0].innerText = $('#event-description-' + id)[0].innerText;
	if(typeof $('#event-destination-' + id) !== 'undefined') {
		$('#innerEventDestination')[0].innerText = $('#event-destination-' + id)[0].innerText;
		$('#innerPositionX')[0].innerText = $('#positionX-' + id)[0].innerText;
		$('#innerPositionY')[0].innerText = $('#positionY-' + id)[0].innerText;
	}
	$('#innerEventStartDate')[0].innerText = $('#event-startDate-' + id)[0].innerText;
	$('#innerEventEndDate')[0].innerText = $('#event-endDate-' + id)[0].innerText;
	$("div[id^=tag-block-event-" + id + "] span").each(function (i, e) {
		$('#innerEventTagBlock').append("<span class='tag'>" + e.innerText + "</span>")
	});
	$('#eventModal').modal('show');

}

$(function () {
	$('#eventModal').on('hidden.bs.modal', function () {
		$('#innerEventTagBlock').empty();
		if (map) {
			$("#showMapIntoCard-btn").click();
		}
	});
});

function checkForm() {
	var formValid = true;
	if (($('#searchEventTitle').val() === "") && ($('#searchEventDestination').val() === "")) {
		$('#searchEventTitle').addClass('is-invalid').removeClass('is-valid');
		$('#searchEventDestination').addClass('is-invalid').removeClass('is-valid');
		formValid = false;
	} else {
		$('#searchEventTitle').addClass('is-valid').removeClass('is-invalid');
		$('#searchEventDestination').addClass('is-valid').removeClass('is-invalid');
	}
	if($('#searchEventStartDate').val() !== "" && $('#searchEventEndDate').val() != "") {
		if ($('#searchEventStartDate').val !== "" && $('#searchEventStartDate').val() == "") {
			$('#searchEventStartDate').siblings(".invalid-feedback")[0].innerText = "Check this field";
			$('#searchEventStartDate').addClass('is-invalid').removeClass('is-valid');
			formValid = false;
		} else {
			$('#searchEventStartDate').addClass('is-valid').removeClass('is-invalid');
		}
		if ($('#searchEventEndDate').val !== "" && $('#searchEventEndDate').val() == "") {
			$('#searchEventEndDate').siblings(".invalid-feedback")[0].innerText = "Check this field";
			$('#searchEventEndDate').addClass('is-invalid').removeClass('is-valid');
			formValid = false;
		} else {
			$('#searchEventEndDate').addClass('is-valid').removeClass('is-invalid');
		}
		if (formValid) {
			if (new Date($('#searchEventEndDate').val()) < new Date($('#searchEventStartDate').val())) {
				formValid = false;
				$('#searchEventEndDate').siblings(".invalid-feedback")[0].innerText = "End date cannot be earlier then start date. Max date is 01.01.2050";
				$('#searchEventEndDate').addClass('is-invalid').removeClass('is-valid');
			} else {
				$('#searchEventEndDate').addClass('is-valid').removeClass('is-invalid');
			}
		}
	}
	if (!formValid) {
		enableSearchBtn();
		return formValid;
	}
}

$(function () {
	$("#searchEventEndDate").blur(function () {
		if ($('#searchEventEndDate').val !== "" && $('#searchEventStartDate').val === "") {
			$('#searchEventStartDate').val(new Date().format("yyyy-mm-dd'T'HH:MM"));
		}
	});
});

function enableSearchBtn() {
	$('#search-spinner').attr("hidden", "hidden");
	$("#searchEvent-btn")[0].removeAttribute("disabled");
}

function disableSearchBtn() {
	$('#search-spinner')[0].removeAttribute('hidden');
	$("#searchEvent-btn").attr("disabled", "disabled");
}

function enableGetMoreBtn() {
	$('#getMore-spinner').attr("hidden", "hidden");
	$("#getMore-btn")[0].removeAttribute("disabled");
}

function disableGetMoreBtn() {
	$("#getMore-btn").attr("disabled", "disabled");
	$("#getMore-spinner")[0].removeAttribute("hidden");
}


ymaps.ready(initModalMap);

let destinIsChanged;

function initModalMap() {
	// Подключаем поисковые подсказки к полю ввода.
	var suggestView = new ymaps.SuggestView('searchEventDestination'),
		map,
		placemark,
		area;

	$("#showMap-btn").click(function () {
		if (map) {
			map.destroy();
			map = null;
		} else {
			geocode($("#searchEventDestination").val());
		}
	});

	$('#searchEventDestination').keypress(function (e) {
		if (e.keyCode === 13 && map) {
			geocode($('#searchEventDestination').val());
		}
	});

	$('#search-dest-radius').keypress(function (e) {
		if (e.keyCode === 13 && map) {
			geocode($('#searchEventDestination').val());
		}
	});

	function geocode(request) {
		// Геокодируем введённые данные.
		ymaps.geocode(request).then(function (res) {
			var obj = res.geoObjects.get(0),
				error;
			destinIsChanged = false;
			if (!obj) {
				showErrorAndDeleteModalMap('Адрес не найден');
			} else {
				showResultOnModalMap(obj);
			}
		}, function (e) {
			console.log(e)
		});
	}

	function showResultOnModalMap(obj) {
		// Удаляем сообщение об ошибке, если найденный адрес совпадает с поисковым запросом.
		$('#searchEventDestination').removeClass('input_error');
		$('#notice').css('display', 'none');
		$("#search-map").css("width", $("#searchEventModal .modal-content").width() - 20);
		var mapContainer = $('#search-map'),
			bounds = obj.properties.get('boundedBy'),
			// Рассчитываем видимую область для текущего положения пользователя.
			mapState = ymaps.util.bounds.getCenterAndZoom(
				bounds,
				[mapContainer.width(), mapContainer.height()]
			),
			// Сохраняем полный адрес для сообщения под картой.
			fullAddress = obj.getAddressLine(),
			// Сохраняем укороченный адрес для подписи метки.
			shortAddress = [obj.getThoroughfare(), obj.getPremiseNumber(), obj.getPremise()].join(' ');
		// Убираем контролы с карты.
		mapState.controls = [];
		// Создаём карту.
		createModalMap(mapState, shortAddress, fullAddress);
		// Выводим сообщение под картой.
	}


	function createModalMap(state, shortAddress, fullAddress) {
		setPostitionAttributes(state.center);
		var radius = $('#search-dest-radius').val();
		if (isRadiusValid(radius) === false) {
			showErrorAndDeleteModalMap("Проверьте поле радиус");
			return false;
		}
		// Если карта еще не была создана, то создадим ее и добавим метку с адресом.
		if (!map) {
			map = new ymaps.Map('search-map', state);
			placemark = new ymaps.Placemark();
			changePlacemarkData(placemark, state, shortAddress, fullAddress);
			map.geoObjects.add(placemark);
			area = new ymaps.Circle([map.getCenter(), radius]);
			map.geoObjects.add(area);
			map.events.add('contextmenu', function (e) {
				// Определяем адрес по координатам (обратное геокодирование).
				let coords = e.get('coords');
				geocode(coords);
			});
			// Если карта есть, то выставляем новый центр карты и меняем данные и позицию метки в соответствии с найденным адресом.
		} else {
			map.setCenter(state.center, state.zoom);
			changePlacemarkData(placemark, state, shortAddress, fullAddress);
			area.geometry.setCoordinates(state.center);
			area.geometry.setRadius(radius);
			$('#searchEventDestination').val(fullAddress);
		}
	}

	function showErrorAndDeleteMap(message) {
		showError(message);
		$('#searchEventDestination').addClass('input_error');
		// Удаляем карту.
		if (map) {
			map.destroy();
		}
		map = null;
	}



	function setPostitionAttributes(center) {
		$("#positionX")[0].innerText = center[0];
		$("#positionY")[0].innerText = center[1];
	}
}

function changePlacemarkData(placemark, state, shortAddress, fullAddress) {
	placemark.geometry.setCoordinates(state.center);
	placemark.properties.set({iconCaption: shortAddress, balloonContent: fullAddress});
}

function showError(message) {
	$('#notice').text(message);
	$('#notice').css('display', 'block');
}

function checkRadius() {
	$('#notice').css('display', 'none');
	if (isRadiusValid() === false) {
		showError("Проверьте поле радиус");
	}
}

function isRadiusValid() {
	var radius = $("#search-dest-radius").val();
	var reg = new RegExp("^\\d*$");
	return !(!reg.test(radius) || radius < 0 || radius > 1000);
}















//Инициализация карты в карточке события

ymaps.ready(init);
let map;

function init() {

	$("#showMapIntoCard-btn").click(function (e) {
		if (e.currentTarget.firstChild.className === 'glyphicon glyphicon-globe') {
			let x = e.clientX,
				y = e.clientY;
			//TODO Лучше???
			$("#destination-map")[0].style.top = (y - 200) + 'px';
			$("#destination-map")[0].style.left = (x - 530) + 'px';
			$("#destination-map")[0].style.height='160px';
			$("#destination-map")[0].style.width='376px';
			let posX = $("#innerPositionX")[0].innerText;
			let posY = $("#innerPositionY")[0].innerText;
			geocode([posX, posY]);
		} else {
			$("#destination-map")[0].style.height='0';
			$("#destination-map")[0].style.width='0';
			if (map) {
				map.destroy();
			}
			map = null;
			e.currentTarget.firstChild.className = 'glyphicon glyphicon-globe'
		}
	});

	function geocode(request) {
		// Геокодируем введённые данные.
		ymaps.geocode(request).then(function (res) {
			$("#showMapIntoCard-btn")[0].firstChild.className = "fa fa-spinner fa-spin";
			var obj = res.geoObjects.get(0),
				error;
			if (!obj) {
				showError('Произошла ошибка');
			} else {
				showResultOnMap(obj);
			}
			$("#showMapIntoCard-btn")[0].firstChild.className = "glyphicon glyphicon-remove-sign";
		}, function (e) {
			console.log(e)
		});
	}

	function showResultOnMap(obj) {
		// Удаляем сообщение об ошибке, если найденный адрес совпадает с поисковым запросом.
		//$('#addEventDestination').removeClass('input_error');
		/*$('#notice').css('display', 'none');*/

		var mapContainer = $('#destination-map'),
			bounds = obj.properties.get('boundedBy'),
			// Рассчитываем видимую область для текущего положения пользователя.
			mapState = ymaps.util.bounds.getCenterAndZoom(
				bounds,
				[mapContainer.width(), mapContainer.height()]
			),
			// Сохраняем полный адрес для сообщения под картой.
			fullAddress = obj.getAddressLine(),
			// Сохраняем укороченный адрес для подписи метки.
			shortAddress = [obj.getThoroughfare(), obj.getPremiseNumber(), obj.getPremise()].join(' ');
		// Убираем контролы с карты.
		mapState.controls = [];
		// Создаём карту.
		createCardMap(mapState, shortAddress, fullAddress);
		// Выводим сообщение под картой.
	}

	function showError(message) {
		alert("message");
		//$('#notice').text(message);
		//	$('#addEventDestination').addClass('input_error');
		//$('#notice').css('display', 'block');
		// Удаляем карту.
		if (map) {
			map.destroy();
		}
	}
}

function createCardMap(state, shortAddress, fullAddress) {
	// Если карта еще не была создана, то создадим ее и добавим метку с адресом.
	if (!map) {
		map = new ymaps.Map('destination-map', state);
		placemark = new ymaps.Placemark();
		changePlacemarkData(placemark, state, shortAddress, fullAddress);
		map.geoObjects.add(placemark);
		// Если карта есть, то выставляем новый центр карты и меняем данные и позицию метки в соответствии с найденным адресом.
	} else {
		map.setCenter(state.center, state.zoom);
		changePlacemarkData(placemark, state, shortAddress, fullAddress);
	}
}


