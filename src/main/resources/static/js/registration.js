function register(url) {
	if (checkForm() === false) {
		return;
	}
	var SN = [];
	var $th = $('#socialNetworks').find('th');
	try {
		$('#socialNetworks').find('tbody tr').each(function (i, tr) {
			var obj = {}, $tds = $(tr).find('td');
			$th.each(function (index, th) {
				if ($(th)[0].localName !== "button" && $tds.eq(index).value === "") {
					var current = document.getElementById("errorText");
					current.textContent = "Заполните пустые поля в таблице 'Cоциальные сети'";
					current.style.color = "red";
					throw new Error("Пустые поля в таблице 'Cоциальные сети'");
				}
				if ($(th).attr('abbr') !== "") {
					if (typeof $tds.eq(index).children().val() !== 'undefined') {
						obj[$(th).attr('abbr')] = $tds.eq(index).children().val();
					}
				}
			});
			SN.push(obj);
		});
	} catch (e) {
		return;
	}
	/*let socialNetworks = [];
	socialNetworks["socialNetworks"] = SN;*/
	let user = {
		firstName: $("#firstName").val(),
		lastName: $("#lastName").val(),
		email: $("#email").val(),
		socialNetworks: SN
	};
	let passwords = {
		oldPassword:$('#password-field-old').val(),
		newPassword:$('#password-field-first').val(),
		repeatPassword:$('#password-field-second').val()
	};
	$.ajax({
		url: url + "?" + jQuery.param(passwords),
		type: 'POST',
		dataType: 'text',
		contentType: "application/json; charset=utf-8",
		data: JSON.stringify(user),
		success: function (e) {
			location.href = e;
		},
		error: function (e) {
			$("#errorText")[0].innerText = JSON.parse(e.responseText).message
		}
	});

}

function checkForm() {
	$('#regForm input').each(function () {
		if (this.checkValidity()) {
			$(this).addClass('is-valid').removeClass('is-invalid');
		} else {
			$(this).addClass('is-invalid').removeClass('is-valid');
		}
	});
	$.each($(".sn-link"), function (i, e) {
		checkSNLink(e);
	});
	$.each($(".password-field:visible"),function (i,e) {
		if($(e).val() !== "") {
			var passFirst = $('#password-field-first');
			var passSecond = $('#password-field-second');
			if(passFirst.parent().attr('hidden') !== "hidden") {
				if (passFirst.val() !== passSecond.val()) {
					passFirst.addClass('is-invalid').removeClass('is-valid');
					passFirst.siblings(".invalid-feedback")[0].innerText = "Passwords are not equal";
					passSecond.addClass('is-invalid').removeClass('is-valid');
					passSecond.siblings(".invalid-feedback")[0].innerText = "Passwords are not equal";
				} else {
					passFirst.addClass('is-valid').removeClass('is-invalid');
					passFirst.siblings(".invalid-feedback")[0].innerText = "This field can't be empty";
					passSecond.addClass('is-valid').removeClass('is-invalid');
					passSecond.siblings(".invalid-feedback")[0].innerText = "This field can't be empty";
				}
			}
		} else {
			$(e).addClass('is-invalid').removeClass('is-valid');
		}
	});
	return (!($(".is-invalid").length > 0));
}

var SNs = [];
$(document).ready(function () {
	$.each($(".socialNetworkType"), function (index, type) {
		SNs.push(type.innerText);
	});
});


/*$(document).on('click', 'td', (function (e) {
	if (e.target.localName !== "td" || e.target.firstElementChild !== null || (e.target.offsetParent.id !== "socialNetworks")) {
		return;
	}
	var t = e.target || e.srcElement;
	var elm_name = t.tagName.toLowerCase();
	if (elm_name === 'input') {
		return false;
	}
	var val = $(t).html();
	var code;
	if(e.target.cellIndex === 1 && e.target.offsetParent.id === "socialNetworks"){
		code = '<input type="text" id="edit" value="' + val + '" />';
	}
	$(t).empty().append(code);
	var newEditElement = inputElem;
	newEditElement.focus();
	newEditElement.blur(function () {
		var val = $(this).val();
		$(this).parent().empty().html(val);
	});
	convertInputToLabel($("#edit"));
}));

function convertInputToLabel(inputElem) {
	var newEditElement = inputElem;
	newEditElement.focus();
	newEditElement.blur(function () {
		var val = $(this).val();
		$(this).parent().empty().html(val);
	});
}*/

function addNewSN() {
	var stop = false;
	$.each(($("#SN-table-body td")), function (i, e) {
		var element = e.firstChild;
		if (element !== null) {
			if ((element.localName == "select" || element.localName == "input") && element.value === "") {
				alert("Заполните пустые строки массива");
				stop = true;
				return false;
			}
		}
	});
	if (stop === true) {
		return
	}
	let optionSNs;
	$.each(SNs, function (i, e) {
		optionSNs = optionSNs + "<option>" + e + "</option>"
	});
	var row = document.createElement("tr");
	var linkCell = document.createElement("td");
	var linkField = document.createElement("input");
	linkField.className = "form-control sn-link";
	linkField.type = "text";
	var linkFieldErrorMessage = document.createElement("div");
	linkFieldErrorMessage.className = "invalid-feedback";
	linkFieldErrorMessage.innerText = "Check this field";
	linkCell.appendChild(linkField);
	linkCell.appendChild(linkFieldErrorMessage);
	var SNTypeCell = document.createElement("td");
	var SNTypeFiled = document.createElement("select");
	SNTypeFiled.className = "form-control social-network-type";
	SNTypeFiled.value = SNs[0];
	SNTypeFiled.innerHTML = optionSNs;
	SNTypeCell.appendChild(SNTypeFiled);
	var deleteCell = document.createElement("td");
	var deleteButton = document.createElement("button");
	deleteButton.setAttribute("onclick", "deleteSocial(this)");
	deleteButton.className = "glyphicon glyphicon-remove";
	deleteCell.appendChild(deleteButton);
	row.appendChild(linkCell);
	row.appendChild(SNTypeCell);
	row.appendChild(deleteCell);
	$("#SN-table-body").append(row);

	$(".sn-link").blur(function () {
		checkSNLink($(this));
	});
	/*let lastSNT;
	$(".social-network-type").click(function () {
		lastSNT = this.value;
	});
	$(".social-network-type").change(function () {
		if(SNs.indexOf(lastSNT) === -1){
			SNs.push(lastSNT);
		}
		SNs.splice($.inArray(this.value, SNs), 1);
		updateSNOptions();
	})*/
	$(".social-network-type").change(function () {
		$(this).closest('td').prev().children().attr("placeholder", $("#" + $(this).val()).attr("holder"));
	});
	$(".social-network-type").ready(function () {
		let lastElement = $(".social-network-type")[$(".social-network-type").length - 1];
		$(lastElement).closest('td').prev().children().attr("placeholder", $("#" + $(lastElement).val()).attr("holder"));
	})
}


function deleteSocial(element) {
	$(element).parent().parent().remove();
	/*let el = $(element).closest('td').prev().children().val();
	if(SNs.indexOf(el) === -1){
		SNs.push(el);
	}
	updateSNOptions();*/
}

/*
function updateSNOptions() {
	if($(".social-network-type").length > 1) {
		$.each($(".social-network-type"), function (i, e) {
			if(SNs.indexOf(e.children().map(function (elem) {
				return elem.value()})) === -1) {
				$(e).empty();
			}
			e.innerHTML = getOptionedSNs();
		})
	}
}

let optionSNs;
function getOptionedSNs() {
	let optionSNs= "<option></option>";
	$.each(SNs,function (i,e) {
		if(e!=="") {
			optionSNs = optionSNs + "<option>" + e + "</option>"
		}
	});
	return optionSNs;
}*/

function checkSNLink(e) {
	let regexp = new RegExp($("#" + $(e).closest('td').next().children(":first").val()).attr("regExp"));
	if (regexp.test($(e).val())) {
		$(e).addClass('is-valid').removeClass('is-invalid');
	} else {
		$(e).addClass('is-invalid').removeClass('is-valid');
	}
}

$(document).ready(function () {
	$.each($(".socialNetworkTypes"), function () {
		if (this.value === this.getAttribute("userSocialType")) {
			this.selected = "true"
		}
	});
	$(".sn-link").blur(function () {
		checkSNLink($(this));
	});
});

function showChangePasswordFields() {
	if ($('#password-field-old').parent().attr("hidden") === 'hidden') {
		$.each($('.password-field'), function (i, e) {
			$(e).parent()[0].removeAttribute("hidden")
		})}
	else{
		$.each($('.password-field'), function (i, e) {
			$(e).parent().attr("hidden", "hidden")
		})
	}
}