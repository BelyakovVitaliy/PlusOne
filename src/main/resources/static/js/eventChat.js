var stompClient = null;
var chatId;

$(document).ready(function () {
	var socket = new SockJS('/onePlusWS');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function (frame) {
		chatId = $("#eventName").attr("value");
		stompClient.subscribe('/topic/event/chat/' + chatId, function (message) {
			getMessage(JSON.parse(message.body));
		});
	});
});

function closeIt() {
	stompClient.disconnect();
}

window.onbeforeunload = closeIt;

function sendMessage() {
	var message = $('#message-area');
	var response = $('#messageHistory');
	var dt = new Date();
	var dateTime = dt.getUTCDay() + "." + dt.getMonth() + "." + dt.getUTCFullYear() + " | " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
	stompClient.send("/event/sendMessage/" + chatId, {}, JSON.stringify({"message": message.val()}));
	response.append("<div class=\"outgoing_msg\">\n" +
		"\t\t<div class=\"sent_msg\">\n" +
		"\t\t<p>" + message[0].value + "</p>\n" +
		"\t<span class=\"time_date\">" + dateTime + "</span> </div>\n" +
		"\t</div>");
	message.val("");
}

function getMessage(message) {
	var bodyMessage = JSON.parse(message.message).message;
	var sender = message.sender;
	var senderName = sender.firstName + ' ' + sender.lastName;
	var senderEmail = sender.email;
	var dt = new Date();
	var dateTime = dt.getUTCDay() + "." + dt.getUTCMonth() + "." + dt.getUTCFullYear() + " | " + dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();
	if (senderEmail !== $("#user-name-label").attr('value')) {
		$('#messageHistory').append("" +
			"<div class=\"incoming_msg\">\n" +
			"\t\t\t\t\t\t<div class=\"incoming_msg_img\"> <img src=\"https://ptetutorials.com/images/user-profile.png\" alt=\"sunil\"> </div>\n" +
			"\t\t\t\t\t\t<div class=\"received_msg\">\n" +
			"\t\t\t\t\t\t\t<div class=\"received_withd_msg\">\n" +
			"\t\t\t\t\t\t\t\t<p>" + bodyMessage + "</p>\n" +
			"\t\t\t\t\t\t<div class=\'messageDetails\'>" +
			"\t\t\t\t\t\t\t\t<span class='\sender_name\'>" + senderName + "</span>\n" +
			"\t\t\t\t\t\t\t\t<span class=\"time_date\">" + dateTime + "</span>\n" +
			"\t\t\t\t\t\t</div>\n" +
			"\t\t\t\t\t\t</div>\n" +
			"\t\t\t\t\t</div>\n" +
			"</div>");
	}
}


$(function () {
	$('.search-bar').on('keyup', function (event) {
		var users = $(".chat_people");
		users.hide();
		users.filter(function (i, e) {
			if (e.innerText.toLowerCase().indexOf($('.search-bar').val().toLowerCase()) !== -1) {
				return true
			}
		}).show()
	});
});