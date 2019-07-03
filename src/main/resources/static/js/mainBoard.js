$.getScript("js/common.js", function () {
});

var joinToEventBtn;
/*
var stockAddEventPic = "data:image/svg+xml;charset=UTF-8,%3Csvg%20width%3D%22498%22%20height%3D%22225%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20viewBox%3D%220%200%20498%20225%22%20preserveAspectRatio%3D%22none%22%3E%3Cdefs%3E%3Cstyle%20type%3D%22text%2Fcss%22%3E%23holder_1665f66498b%20text%20%7B%20fill%3A%23eceeef%3Bfont-weight%3Abold%3Bfont-family%3AArial%2C%20Helvetica%2C%20Open%20Sans%2C%20sans-serif%2C%20monospace%3Bfont-size%3A25pt%20%7D%20%3C%2Fstyle%3E%3C%2Fdefs%3E%3Cg%20id%3D%22holder_1665f66498b%22%3E%3Crect%20width%3D%22498%22%20height%3D%22225%22%20fill%3D%22%2355595c%22%3E%3C%2Frect%3E%3Cg%3E%3Ctext%20x%3D%22156.421875%22%20y%3D%22123.6%22%3EEventPhoto%3C%2Ftext%3E%3C%2Fg%3E%3C%2Fg%3E%3C%2Fsvg%3E"
*/

$(document).ready(function () {
	feather.replace();
	$('input[class="calendarFilter"]').daterangepicker({
		timePicker: true,
		timePickerIncrement: 10,
		timePicker24Hour: true,
		locale: {
			format: 'DD.MM.YYYY H:mm'
		}
	});
});

function showModal(id) {
	$('#innerEventPicture')[0].setAttribute("src", $('#event-picture-' + id)[0].getAttribute("src"));
	$('#innerEventTitle')[0].innerText = $('#event-title-' + id)[0].innerText;
	$('#innerEventDescription')[0].innerText = $('#event-description-' + id)[0].innerText;
	$('#innerEventDestination')[0].innerText = $('#event-destination-' + id)[0].innerText;
	$('#innerEventStartDate')[0].innerText = $('#event-startDate-' + id)[0].innerText;
	$('#innerEventEndDate')[0].innerText = $('#event-endDate-' + id)[0].innerText;
	$("#innerPositionX").text($("#positionX-" + id)[0].innerText);
	$("#innerPositionY").text($("#positionY-" + id)[0].innerText);
	let ownerUser = $('#my-icon-' + id).attr("value");
	joinToEventBtn = $('#join-event-modal-btn');
	if (typeof ownerUser !== 'undefined') {
		var innerMyIcon = document.createElement("p");
		innerMyIcon.className = "inner-my-icon";
		innerMyIcon.id = "inner-my-icon";
		innerMyIcon.innerText = "MY";
		$("#innerEventPicture").before(innerMyIcon);
		let actionListGroup = document.createElement("div");
		actionListGroup.className = "btn-group dropright";
		actionListGroup.id = "actionListGroup";
		let actionList = document.createElement("button");
		actionList.className = "btn btn-secondary btn-sm dropdown-toggle";
		actionList.id = "actionList";
		actionList.type = "button";
		actionList.setAttribute("data-toggle", "dropdown");
		actionList.innerText = "Actions";
		let dropdownsActions = document.createElement("div");
		dropdownsActions.className = "dropdown-menu";
		dropdownsActions.id = "dropdownsActions";
		let formGroup = document.createElement("div");
		formGroup.className = "form-group";
		let enableChatBtnLabel = document.createElement("label");
		enableChatBtnLabel.className = "form-check-label";
		enableChatBtnLabel.setAttribute("for", "enable-chat-btn");
		enableChatBtnLabel.innerText = "Enable chat";
		let enableChatBtn = document.createElement("input");
		enableChatBtn.className = "form-check-input";
		enableChatBtn.type = "checkbox";
		enableChatBtn.setAttribute("onchange", "changeChatAvailability(" + id + ")");
		enableChatBtn.id = "enable-chat-btn";
		let showEditEventForm = document.createElement("a");
		showEditEventForm.className = "btn btn-primary";
		showEditEventForm.href = "editEvent/" + id;
		showEditEventForm.id = "show-edit-event-form";
		showEditEventForm.innerText = "Edit event";
		let showDelEventDialog = document.createElement("button");
		showDelEventDialog.className = "btn btn-danger";
		showDelEventDialog.setAttribute("onclick", "showDeleteEventDialog(" + id + ")");
		showDelEventDialog.id = "show-del-event-dialog";
		showDelEventDialog.innerText = "Delete event";
		formGroup.appendChild(enableChatBtnLabel);
		formGroup.appendChild(enableChatBtn);
		dropdownsActions.appendChild(formGroup);
		dropdownsActions.appendChild(showEditEventForm);
		dropdownsActions.appendChild(showDelEventDialog);
		actionListGroup.appendChild(actionList);
		actionListGroup.appendChild(dropdownsActions);
		$('#innerEventTitle').after(actionListGroup);


		/*$("#innerEventPicture").before('<p id = "inner-my-icon" class=\"inner-my-icon\">MY</p>');
		$('#innerEventTitle').after("<div class=\"btn-group dropright\" id='actionListGroup'>\n" +
			"\t\t\t\t\t\t\t<button class=\"btn btn-secondary btn-sm dropdown-toggle\" id = 'actionList' type=\"button\" data-toggle=\"dropdown\">\n" +
			"\t\t\t\t\t\t\t\tActions\n" +
			"\t\t\t\t\t\t\t</button>\n" +
			"\t\t\t\t\t\t\t<div class=\"dropdown-menu\" id='dropdownsActions'>\n" +
			"\t\t\t\t\t\t\t<div class=\"form-group\">\n" +
			"\t\t\t\t\t\t\t\t<label class = \"form-check-label\" for=\"enable-chat-btn\">Enable chat</label>\n" +
			"\t\t\t\t\t\t\t\t<input type=\"checkbox\" class=\"form-check-input\" onchange=\"changeChatAvailability(" + id + ")\" id=\"enable-chat-btn\"/>\n" +
			"\t\t\t\t\t\t\t</div>\n" +
			"\t\t\t\t\t\t\t\t<a class=\"btn btn-primary\" href=\"/editEvent/" + id + "\" id=\"show-edit-event-form\">Edit event</a>\n" +
			"\t\t\t\t\t\t\t\t<button class=\"btn btn-danger\" onclick=\"showDeleteEventDialog(" + id + ")\" id=\"show-del-event-dialog\">Delete event</button>\n" +
			"\t\t\t\t\t\t\t</div>\n" +
			"\t\t\t\t\t\t</div>");*/
		joinToEventBtn.hide();
		if ($('#card-event-' + id).attr("ischatenabled") === 'true') {
			$('#enable-chat-btn').attr('checked', 'checked');
			joinToEventBtn.before("<button type=\"submit\" id = \"joinToVKChat-modal-btn\" onclick=\"joinToVKChat(" + id + ")\" class=\"btn btn-primary\" role=\"button\">Join to VK chat</button>")
			joinToEventBtn.before("<button type=\"submit\" id = \"joinToChat-modal-btn\" onclick=\"joinToChat(" + id + ")\" class=\"btn btn-primary\" role=\"button\">Join to chat</button>")
		}
	} else {
		if ($("#card-event-" + id).attr("isUserJoined") === 'false') {
			joinToEventBtn.attr("onClick", "joinToEvent(" + id + ")");
			joinToEventBtn[0].innerText = "+1"
		} else {
			if ($('#card-event-' + id).attr("ischatenabled") === 'true') {
				joinToEventBtn.before("<button type=\"submit\" id = \"joinToVKChat-modal-btn\" onclick=\"joinToVKChat(" + id + ")\" class=\"btn btn-primary\" role=\"button\">Join to VK chat</button>")
				joinToEventBtn.before("<button type=\"submit\" id = \"joinToChat-modal-btn\" onclick=\"joinToChat(" + id + ")\" class=\"btn btn-primary\" role=\"button\">Join to chat</button>")
			}
			joinToEventBtn.attr("onclick", "exitFromEvent(" + id + ")");
			joinToEventBtn[0].innerText = "-1"
		}
	}


	$("div[id^=tag-block-event-" + id + "] span").each(function (i, e) {
		$('#innerEventTagBlock').append("<span class='tag'>" + e.innerText + "</span>")
	});
	if ($('#follower-amount-' + id).length) {
		let innerFollowerAmount = document.createElement("p");
		innerFollowerAmount.className = "inner-follower-amount";
		innerFollowerAmount.innerText = $('#follower-amount-' + id)[0].innerText;
		innerFollowerAmount.setAttribute("onclick", "showJoinedUsers(" + id + ")");
		$('#inner-img-block').prepend(innerFollowerAmount);
	}
	$('#eventModal').modal('show');
}

$(function () {
	$('#eventModal').on('hidden.bs.modal', function () {
		$("#innerPositionX")[0].removeAttribute("positionX");
		$("#innerPositionY")[0].removeAttribute("positionY");
		$("#inner-my-icon").remove();
		$("#actionListGroup").remove();
		$("#joinToVKChat-modal-btn").remove();
		$("#joinToChat-modal-btn").remove();
		$('#innerEventTagBlock').empty();
		$('#join-event-modal-btn').show();
		$(".inner-follower-amount").remove();
		$("#delete-event-btn")[0].removeAttribute("onclick");
		if (map) {
			$("#showMap-btn").click();
		}
	});
});

/*function showEditEventForm(id) {

}*/

/*function showEditEventForm(id) {
	$("#addEventPicture")[0].removeAttribute("style");
	var picture = $('#innerEventPicture').attr("src");
	var title = $('#innerEventTitle')[0].innerText;
	var description = $("#innerEventDescription")[0].innerText;
	var destination = $('#innerEventDestination')[0].innerText;
	var startDate = new Date($('#event-startDate-' + id).attr('value'));
	var endDate = new Date($('#event-endDate-' + id).attr('value'));
	var tags = $("#innerEventTagBlock span");
	$('#addEventModal').attr('eventId',id);
	$('#addEventPicture').attr("src", picture);
	$('#addEventTitle').val(title);
	$('#addEventDescription').val(description);
	$('#addEventDestination').val(destination);
	$('#addEventStartDate').val(startDate.format("dd.mm.yyyy HH:MM"));
	$('#addEventEndDate').val(endDate.format("dd.mm.yyyy HH:MM"));
	var tagsMas = tags.map(function(i,a) {
		return a.innerText;
	});
	$('#add-Event-btn').attr('onclick','updateEvent()');
	$('#tagBox').tagging( "add", tagsMas.toArray());
	$("#eventModal").modal('hide');
	$("#addEventModal").modal('show');
	$('#addEventModal').modal('handleUpdate');
}*/

/*function checkForm() {
	var formValid = true;
	if (typeof pic !== 'undefined') {
		if (pic.size > $("#addEventPicture-btn").attr("max")) {
			alert("Ошибка сохранения фотографии. Файл слишком велик");
			formValid = false;
		}
	}
	$('#addEventModal input').each(function() {
		if (this.checkValidity()) {
			$(this).addClass('is-valid').removeClass('is-invalid');
		} else {
			$(this).addClass('is-invalid').removeClass('is-valid');
			formValid = false;
		}
	});
	if(new Date($('#addEventEndDate').val()) < new Date($('#addEventStartDate').val()) ) {
		$('#addEventStartDate').addClass('is-invalid').removeClass('is-valid');
		$('#addEventEndDate').addClass('is-invalid').removeClass('is-valid');
		formValid = false;
	}
	if (!formValid) {
		return formValid;
	}
}*/

/*$(function () {
	$('#addEventModal').on('hidden.bs.modal', function () {
		$('#addEventPicture')[0].src = stockAddEventPic;
		$('#addEventPicture-btn').val('');
		$('#addEventForm')[0].reset();
		$('#tagBox').tagging("removeAll");
		$('#add-Event-btn').attr('onclick','addNewEvent()');
	});
});

function showAddEventModal() {
	$('#addEventModal').modal('show');
}*/

function showDeleteEventDialog(id) {
	$("#delete-event-btn").attr("onclick", 'deleteEvent(' + id + ')');
	$("#deleteEventModal").modal('show');
}

/*function selectPicture() {
	file = $("#addEventPicture-btn")[0].files[0];
	readURL(this);
	$("#addEventPicture").css({
		width: 'auto',
		maxHeight: screen.height/3.2,
		height: ""
	});
	$('#addEventModal').modal('handleUpdate');
}*/

/*var pic;

function readURL(input) {
	if (input.file) {
		pic = input.file;
		var reader = new FileReader();
		reader.onload = function (e) {
			$('#addEventPicture').attr('src', e.target.result);
		};
		reader.readAsDataURL(input.file);
	}
}

function addNewEvent() {
	if(checkForm() === false) {return}
	url = "/rest/event/add";
	let event = {
		title: $("#addEventTitle")[0].value,
		description: $("#addEventDescription")[0].value,
		destination: $("#addEventDestination")[0].value,
		startDate: $("#addEventStartDate")[0].value,
		endDate: $("#addEventEndDate")[0].value,
		tags: $('#tagBox').tagging( "getTags" )
	};
	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(event),
		success: function (e) {
			sendPicture(e);
			setTimeout(function () {
				location.reload();
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
}*/

/*function sendPicture(id) {
	if(typeof pic === 'undefined' || typeof id === 'undefined') {
		return;
	}
	var dataValue = new FormData();
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
}*/

/*function updateEvent() {
	if(checkForm() === false) {return}
	url = "/rest/event/update";
	let event = {
		id: $('#addEventModal').attr('eventId'),
		title: $("#addEventTitle")[0].value,
		description: $("#addEventDescription")[0].value,
		destination: $("#addEventDestination")[0].value,
		startDate: $("#addEventStartDate")[0].value,
		endDate: $("#addEventEndDate")[0].value,
		tags: $('#tagBox').tagging( "getTags" )
	};
	$.ajax({
		type: "POST",
		url: url,
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(event),
		success: function (e) {
			sendPicture(e);
			setTimeout(function () {
				location.reload();
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
}*/

function changeChatAvailability(id) {
	let params = {
		eventId: id,
		isChatEnable: $("#enable-chat-btn")[0].checked
	};
	url = "/rest/event/changeChatAvailability";
	$.ajax({
		url: url,
		type: 'POST',
		data: params,
		error: function (e) {
			alert(e.responseJSON.message);
			$("#enable-chat-btn")[0].checked = false;
			window.open(e.responseJSON.link)
		}
	});
}


function joinToVKChat(id) {
	let params = {
		eventId: id
	};
	url = "/rest/event/joinToVKChat";
	$.ajax({
		url: url,
		type: 'POST',
		data: params,
		success: function (e) {
			location.href = e
		},
		error: function (e) {
			alert(e.responseText)
		}
	});
}

function joinToChat(id) {
	location.href = "/event/chat/" + id;
}

function joinToEvent(id) {
	let params = {
		eventId: id
	};
	url = "/rest/event/joinToEvent";
	$.ajax({
		url: url,
		type: 'POST',
		data: params,
		success: function (e) {
			joinToEventBtn.attr("onclick", 'exitFromEvent(' + id + ")");
			joinToEventBtn[0].innerText = "-1";
			if ($('#card-event-' + id).attr("ischatenabled") === 'true') {
				joinToEventBtn.before("<button type=\"submit\" id = \"joinToVKChat-modal-btn\" onclick=\"joinToVKChat(" + id + ")\" class=\"btn btn-primary\" role=\"button\">Join to VK chat</button>")
				joinToEventBtn.before("<button type=\"submit\" id = \"joinToChat-modal-btn\" onclick=\"joinToChat(" + id + ")\" class=\"btn btn-primary\" role=\"button\">Join to chat</button>")
			}
		},
		error: function (e) {
			alert(e.responseText)
		}
	});
}

function exitFromEvent(id) {
	let params = {
		eventId: id
	};
	url = "/rest/event/exitFromEvent";
	$.ajax({
		url: url,
		type: 'POST',
		data: params,
		success: function (e) {
			joinToEventBtn.attr("onclick", 'joinToEvent(' + id + ")");
			joinToEventBtn[0].innerText = "+1";
			if ($('#card-event-' + id).attr("ischatenabled") === 'true') {
				$("#joinToVKChat-modal-btn").remove();
				$("#joinToChat-modal-btn").remove();
			}
		},
		error: function (e) {
			alert(e.responseText)
		}
	});
}

function deleteEvent(id) {
	let params = {
		eventId: id
	};
	url = "/rest/event/delete";
	$.ajax({
		url: url,
		type: 'POST',
		data: params,
		success: function (e) {
			location.reload()
		},
		error: function (e) {
			alert(e.responseText)
		}
	});
}

$(function () {
	$('input[class="calendarFilter"]').on('change', function (event) {
		var startDate = new Date($('input[class="calendarFilter"]').data('daterangepicker').startDate);
		var endDate = new Date($('input[class="calendarFilter"]').data('daterangepicker').endDate);
		/*		$(".card").each(function (i,e) {
					var evId = $(e).attr('realId');
					if($("#event-startDate-" + evId).attr("value") < startDate && $("#event-startDate-" + evId).attr("value") > endDate) {
						e.
					}
				})*/
		var cards = $(".card");
		cards.hide();
		cards.filter(function (i, e) {
			var evId = $(e).attr('realId');
			var eventStartDate = new Date($("#event-startDate-" + evId).attr("value"));
			var eventEndDate = new Date($("#event-endDate-" + evId).attr("value"));
			if (!(((eventStartDate < startDate && eventEndDate < startDate) || (eventStartDate > endDate && eventEndDate > endDate)))) {
				return true
			}
		}).show()
	});
});


ymaps.ready(init);
let map;

function init() {

	$("#showMap-btn").click(function (e) {
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
			$("#showMap-btn")[0].firstChild.className = "fa fa-spinner fa-spin";
			var obj = res.geoObjects.get(0),
				error;
			if (!obj) {
				showError('Произошла ошибка');
			} else {
				showResultOnMap(obj);
			}
			$("#showMap-btn")[0].firstChild.className = "glyphicon glyphicon-remove-sign";
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
		createMap(mapState, shortAddress, fullAddress);
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

	function createMap(state, shortAddress, fullAddress) {
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

	function changePlacemarkData(placemark, state, shortAddress, fullAddress) {
		placemark.geometry.setCoordinates(state.center);
		placemark.properties.set({iconCaption: shortAddress, balloonContent: fullAddress});
	}
}

function showJoinedUsers(eventId) {
	if ($(".joinedUsers").length === 0) {
		url = "/rest/event/getJoinedUsers/" + eventId;
		$.ajax({
			type: "GET",
			url: url,
			success: function (e) {
				let ownerUser = $('#my-icon-' + eventId).attr("value");
				if (typeof ownerUser === 'undefined') {
					let owner = {
						firstName: "Виталий",
						lastName: "Беляков",
						email: "belyakov.vit.a@gmail.com",
						isOwner: true
					};
					e.unshift(owner);
				}
				let joinedUsers = document.createElement("div");
				joinedUsers.className = "joinedUsers";
				$.each(e, function (i, user) {
					let userFullName = document.createElement("h5");
					userFullName.innerText = user.firstName + " " + user.lastName;
					let userEmail = document.createElement("span");
					userEmail.className = "joinedUserEmail";
					userEmail.innerText = user.email;
					let userData = document.createElement("div");
					userData.className = "userData";
					userData.appendChild(userFullName);
					userData.appendChild(userEmail);
					if (user.isOwner === true) {
						let ownerSign = document.createElement("i");
						ownerSign.className = "glyphicon glyphicon-certificate";
						ownerSign.setAttribute("aria-hidden", "true");
						userData.appendChild(ownerSign);
					}
					let userImg = document.createElement("img");
					userImg.src = "https://ptetutorials.com/images/user-profile.png";
					let userImgBlock = document.createElement("div");
					userImgBlock.className = "joinedUserImg";
					userImgBlock.appendChild(userImg);
					let joinedUser = document.createElement("div");
					joinedUser.className = "joinedUser";
					joinedUser.appendChild(userImgBlock);
					joinedUser.appendChild(userData);
					joinedUsers.appendChild(joinedUser);
				});
				let buttonPosition = $(".inner-follower-amount")[0].getBoundingClientRect();
				let x = buttonPosition.x,
					y = buttonPosition.y;
				$("#innerEventTitle").append(joinedUsers);
				$(".joinedUsers")[0].style.top = (y + 5) + 'px';
				$(".joinedUsers")[0].style.left = (x - 493) + 'px';
			},
			error: function (e) {
				console.log("Не удалось получить список присоединившихся пользователей к событию № " + eventId);
				console.log(e.responseText);
			}
		});
	} else {
		if (typeof $(".joinedUsers").attr("hidden") !== "undefined") {
			$(".joinedUsers")[0].removeAttribute("hidden");
		} else {
			$(".joinedUsers").attr("hidden", "hidden")
		}
	}
}