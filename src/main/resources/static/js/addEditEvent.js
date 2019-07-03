function selectPicture() {
	file = $("#eventPicture-btn")[0].files[0];
	readURL(this);
	$("#eventPicture").css({
		width: 'auto',
		maxHeight: screen.height / 3.2,
		height: ""
	});
	$('#eventModal').modal('handleUpdate');
}

var pic;

function readURL(input) {
	if (input.file) {
		pic = input.file;
		var reader = new FileReader();
		reader.onload = function (e) {
			$('#eventPicture').attr('src', e.target.result);
		};
		reader.readAsDataURL(input.file);
	}
}

$(document).ready(function () {
	var options = {
		"no-duplicate": true,
		"edit-on-delete": false,
		"no-duplicate-callback": window.alert,
		"no-duplicate-text": "Duplicate tags",
		"forbidden-chars": [",", ".", "_", "?"]
	};
	var tagMas = $("#tagBox-hidden span");
	tagMas.each(function (i, e) {
		tagMas[i] = e.innerText;
	});
	$("#eventDestination").keydown(function () {
		destinIsChanged = true;
	});
	//TODO Можно лучше?
	$("#tagBox").tagging(options);
	$("#tagBox").tagging("removeAll");
	$("#tagBox").tagging("add", tagMas.toArray());
	$("#eventTitle").focus();
});

function addNewEvent() {
	if (checkForm() === false) {
		return
	}
	url = "/rest/event/add";
	var destination = {x: $("#positionX")[0].innerText, y: $("#positionY")[0].innerText, address: $("#eventDestination")[0].value};
	let event = {
		title: $("#eventTitle")[0].value,
		description: $("#eventDescription")[0].value,
		destination: destination,
		startDate: $("#eventStartDate")[0].value,
		endDate: $("#eventEndDate")[0].value,
		tags: getTags()
	};
	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(event),
		success: function (e) {
			sendPicture(e);
			setTimeout(function () {
				window.location.href = "/mainBoard";
			}, 1000);
		},
		error: function (e) {
			if (e.responseText !== "") {
				alert(e.responseText)
			} else {
				alert("Произошла ошибка")
			}
		}
	});
}

function getTags() {
	let tags = $('#tagBox').tagging("getTags");
	tags.forEach(function (e, i, tags) {
		if (e.substr(0, 1) !== '#') {
			tags[i] = '#' + e
		}
	});
	return tags;
}

function sendPicture(id) {
	if (typeof pic === 'undefined' || typeof id === 'undefined') {
		return;
	}
	let dataValue = new FormData();
	dataValue.append("pic", pic);
	dataValue.append("id", id);
	$.ajax({
		url: '/rest/event/addPicture',
		type: 'POST',
		data: dataValue,
		cache: false,
		dataType: 'json',
		enctype: "multipart/form-data",
		processData: false,
		contentType: false,
		success: function (data) {
			location.reload();
		},
		error: function (data) {
			alert(data.responseJSON.message)
		}
	});
}

function updateEvent() {
	if (checkForm() === false) {
		return
	}
	let url = "/rest/event/update";
	var destination = {x: $("#positionX")[0].innerText, y: $("#positionY")[0].innerText, address: $("#eventDestination")[0].value};
	let event = {
		id: $('#eventDetails').attr('eventId'),
		title: $("#eventTitle")[0].value,
		description: $("#eventDescription")[0].value,
		destination: destination,
		startDate: $("#eventStartDate")[0].value,
		endDate: $("#eventEndDate")[0].value,
		tags: getTags()
	};
	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(event),
		success: function (e) {
			sendPicture(e);
			setTimeout(function () {
				window.location.href = "/mainBoard";
			}, 1000);
		},
		error: function (e) {
			if (e.responseText !== "") {
				alert(e.responseText)
			} else {
				alert("Произошла ошибка")
			}
		}
	});
}

function checkForm() {
	var formValid = true;
	if (typeof pic !== 'undefined') {
		if (pic.size > $("#eventPicture-btn").attr("max")) {
			alert("Ошибка сохранения фотографии. Файл слишком велик");
			formValid = false;
		}
	}
	$('#eventDetails input').each(function () {
		if (this.checkValidity()) {
			$(this).addClass('is-valid').removeClass('is-invalid');
		} else {
			$(this).addClass('is-invalid').removeClass('is-valid');
			formValid = false;
		}
	});
	var startDate = new Date($('#eventStartDate').val());
/*	if (startDate < new Date()) {
		$('#eventStartDate').addClass('is-invalid').removeClass('is-valid');
	}*/

	if (new Date($('#eventEndDate').val()) < startDate) {
		$('#eventEndDate').addClass('is-invalid').removeClass('is-valid');
		formValid = false;
	}
	if (($("#notice")[0].style.display !== 'none') || (destinIsChanged === true)) {
		formValid = false;
	}
	if (!formValid) {
		return formValid;
	}
}

$(function () {
	$('[data-toggle="tooltip"]').tooltip();
});

ymaps.ready(init);

let destinIsChanged;

function init() {
	// Подключаем поисковые подсказки к полю ввода.
	var suggestView = new ymaps.SuggestView('eventDestination'),
		map,
		placemark;

	let posX = $("#positionX")[0].innerText;
	let posY = $("#positionY")[0].innerText;
	if ((posY !== "") && (posX !== "") && (typeof posX !== 'undefined') && (typeof posY !== 'undefined')) {
		geocode([posX, posY]);
	}

	$('#eventDestination').keypress(function (e) {
		if (e.keyCode === 13) {
			geocode($('#eventDestination').val());
		}
	});

	$('#eventDestination').focusout(function (e) {
		geocode($('#eventDestination').val());
	});

	function geocode(request) {
		// Геокодируем введённые данные.
		ymaps.geocode(request).then(function (res) {
			var obj = res.geoObjects.get(0),
				error;
			destinIsChanged = false;
			if (!obj) {
				showError('Адрес не найден');
			} else {
				showResultOnMap(obj);
			}
		}, function (e) {
			console.log(e)
		});
	}

	function showResultOnMap(obj) {
		// Удаляем сообщение об ошибке, если найденный адрес совпадает с поисковым запросом.
		$('#eventDestination').removeClass('input_error');
		$('#notice').css('display', 'none');

		var mapContainer = $('#map'),
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
		createMap(mapState, shortAddress, fullAddress);
		// Выводим сообщение под картой.
	}

	function showError(message) {
		$('#notice').text(message);
		$('#eventDestination').addClass('input_error');
		$('#notice').css('display', 'block');
		// Удаляем карту.
		if (map) {
			map.destroy();
		}
		map = null;
	}

	function createMap(state, shortAddress, fullAddress) {
		setPostitionAttributes(state.center);
		// Если карта еще не была создана, то создадим ее и добавим метку с адресом.
		if (!map) {
			map = new ymaps.Map('map', state);
			placemark = new ymaps.Placemark();
			changePlacemarkData(placemark, state, shortAddress, fullAddress);
			map.geoObjects.add(placemark);
			map.events.add('contextmenu', function (e) {
				// Определяем адрес по координатам (обратное геокодирование).
				let coords = e.get('coords');
				geocode(coords);
			});
			// Если карта есть, то выставляем новый центр карты и меняем данные и позицию метки в соответствии с найденным адресом.
		} else {
			map.setCenter(state.center, state.zoom);
			changePlacemarkData(placemark, state, shortAddress, fullAddress);
			$('#eventDestination').val(fullAddress);
		}
	}

	function changePlacemarkData(placemark, state, shortAddress, fullAddress) {
		placemark.geometry.setCoordinates(state.center);
		placemark.properties.set({iconCaption: shortAddress, balloonContent: fullAddress});
	}

	function setPostitionAttributes(center) {
		$("#positionX")[0].innerText = center[0];
		$("#positionY")[0].innerText = center[1];
	}
}
