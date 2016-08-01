$(document).ready(function(event) {
	var currentDateTime = new Date();
	$('#calendar').fullCalendar({
		header : {
			left : 'prev,next today',
			center : 'title',
			right : 'month,agendaWeek,agendaDay'
		},
		defaultDate : currentDateTime,
		editable : true,
		eventLimit : true, // allow "more" link
		// when too many
		// events
		dayClick : function(date, jsEvent, view) {
			setCreationModal(date);
			$("#edit_modal #errorDiv").css("display", "none");
			$('#edit_modal').modal('show');
		},
		eventClick : function(calEvent, jsEvent, view) {
			var hasPerm = hasPermissions(calEvent);
			if (hasPerm) {
				setEditModal(calEvent);
				$("#edit_modal #errorDiv").css("display", "none");
				$('#edit_modal').modal('show');
			} else {
				setViewModal(calEvent);
				$('#view_modal').modal('show');
			}
		},
		eventSources : [ {
			url : '/ClassroomBooking/ajax/prenotazioni'
		} ],

		eventResize : function(event, delta, revertFunc) {
			var hasPerm = hasPermissions(event);
			if (!hasPerm) {
				revertFunc();
			} else {
				setEditModal(event);
				salvaPrenotazione();
			}
		},
		eventDrop : function(event, delta, revertFunc) {
			var hasPerm = hasPermissions(event);
			if (!hasPerm) {
				revertFunc();
			} else {
				setEditModal(event);
				salvaPrenotazione();
			}
		}
	});
});

function areYouSure() {
	var r = confirm("Sei sicuro di voler eliminare questa prenotazione?");
	if (r == true) {
		var id = $("#edit_modal #idPrenotazione").val();
		deleteEvent(id);
	}
}

function hasPermissions(calEvent) {
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	var hasPerm = false;
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : '/ClassroomBooking/ajax/hasPermissions',
		data : JSON.stringify(calEvent.owner),
		dataType : 'json',
		timeout : 10000,
		async : false,
		success : function(data) {
			hasPerm = data;
		}
	});
	return hasPerm;
}

function setCreationModal(date) {
	$("#edit_modal #modalTitle").text("Crea una nuova prenotazione");
	$('#edit_modal #startTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});

	$('#edit_modal #endTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#edit_modal #startDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});
	$('#edit_modal #endDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});

	$('#edit_modal #startTime').val("09:00");
	$('#edit_modal #startDate').val(date.format("DD-MM-YYYY"));
	updateEndDate();
	
	$('#edit_modal #deleteButton').attr("disabled", "");
	$('#edit_modal #selectAula option:selected').prop("selected", false);
	$('#edit_modal #idPrenotazione').val("");
	$('#edit_modal #idPrenotazioneDelete').val("");
	$("#edit_modal #sendEmailDiv").css("display", "none");
}

function setEditModal(calEvent) {
	$("#edit_modal #modalTitle").text("Modifica prenotazione");
	$('#edit_modal #startTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#edit_modal #endTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#edit_modal #startDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});
	$('#edit_modal #endDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});

	$('#edit_modal #startTime').val(calEvent.start.format("HH:mm"));
	$('#edit_modal #endTime').val(calEvent.end.format("HH:mm"));

	$('#edit_modal #startDate').val(calEvent.start.format("DD-MM-YYYY"));
	$('#edit_modal #endDate').val(calEvent.end.format("DD-MM-YYYY"));
	$('#edit_modal #selectAula option:selected').prop("selected", false);
	$('#edit_modal #aulaN' + calEvent.classRoom.id).prop("selected", true);
	$('#edit_modal #idPrenotazione').val(calEvent.id);
	$('#edit_modal #idPrenotazioneDelete').val(calEvent.id);
	$('#edit_modal #deleteButton').removeAttr("disabled");
	$("#edit_modal #sendEmailDiv").css("display", "unset");
	$('#edit_modal #sendEmail').prop("href", "mailto:" + calEvent.owner.email);
	$('#edit_modal #sendEmail')
			.html(
					calEvent.owner.name
							+ " "
							+ calEvent.owner.cognome
							+ " "
							+ '<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>');
}

function setViewModal(calEvent) {
	$('#view_modal #startTime').text(calEvent.start.format("HH:mm"));
	$('#view_modal #endTime').text(calEvent.end.format("HH:mm"));
	$('#view_modal #startDate').text(calEvent.start.format("DD-MM-YYYY"));
	$('#view_modal #endDate').text(calEvent.end.format("DD-MM-YYYY"));
	$('#view_modal #aula').text(calEvent.classRoom.name);
	$('#view_modal #sendEmail').prop("href", "mailto:" + calEvent.owner.email);
	$('#view_modal #sendEmail')
			.html(
					calEvent.owner.name
							+ " "
							+ calEvent.owner.cognome
							+ " "
							+ '<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>');
}

function salvaPrenotazione() {
	$("#edit_modal #inputPrenotazione").css("display", "none");
	$("#edit_modal #attesa").css("display", "unset");
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	$.ajax({
		type : "POST",
		url : '/ClassroomBooking/ajax/edit',
		data : {
			aula : $("#edit_modal #selectAula option:selected").val(),
			startTime : $("#edit_modal #startTime").val(),
			endTime : $("#edit_modal #endTime").val(),
			startDate : $("#edit_modal #startDate").val(),
			endDate : $("#edit_modal #endDate").val(),
			id : $("#edit_modal #idPrenotazione").val()
		},
		timeout : 10000,
		async : false,
		success : function(data) {
			if (data == "ok") {
				$('#edit_modal').modal('hide');
				$('#calendar').fullCalendar('refetchEvents');
			} else {
				if (data == "login") {
					$("#edit_modal #errorMessage").text(
							"E' necessario effettuare il login");
				} else if (data == "missInput") {
					$("#edit_modal #errorMessage").text(
							"Uno o piu' input mancanti");
				} else if (data == "wrongInput") {
					$("#edit_modal #errorMessage").text(
							"Uno o piu' input sono errati");
				} else if (data == "missingPermission") {
					$("#edit_modal #errorMessage").text("Non hai i permessi");
				}
				$("#edit_modal #errorDiv").css("display", "unset");
			}
		}
	});
	$("#edit_modal #inputPrenotazione").css("display", "unset");
	$("#edit_modal #attesa").css("display", "none");
}

// questo metodo restituisce una prenotazione passando il suo id. L'ho fatto
// perchè pensavo mi servisse, invece ho potuto farne a meno... Ma non lo
// cancello perchè è funzionante e potrebbe ancora servire... Stesso discorso
// vale per il getEvent dell'AjaxController
function getEvent(idPrenotazione) {
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	var event;
	$.ajax({
		type : "POST",
		url : '/ClassroomBooking/ajax/getEvent',
		data : {
			id : idPrenotazione
		},
		timeout : 10000,
		async : false,
		success : function(data) {
			var event = data;
		}
	});
	return event;
}

function deleteEvent(idPrenotazione) {
	$("#edit_modal #inputPrenotazione").css("display", "none");
	$("#edit_modal #attesa").css("display", "unset");
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	$.ajax({
		type : "POST",
		url : '/ClassroomBooking/ajax/delete',
		data : {
			id : idPrenotazione
		},
		timeout : 10000,
		async : false,
		success : function(data) {
			if (data == "ok") {
				$('#edit_modal').modal('hide');
				$('#calendar').fullCalendar('refetchEvents');
			} else {
				if (data == "login") {
					$("#edit_modal #errorMessage").text(
							"E' necessario effettuare il login");
				} else if (data == "missInput") {
					$("#edit_modal #errorMessage").text(
							"Uno o piu' input mancanti");
				} else if (data == "wrongInput") {
					$("#edit_modal #errorMessage").text(
							"Uno o piu' input sono errati");
				} else if (data == "missingPermission") {
					$("#edit_modal #errorMessage").text("Non hai i permessi");
				}
				$("#edit_modal #errorDiv").css("display", "unset");
			}
		}
	});
	$("#edit_modal #inputPrenotazione").css("display", "unset");
	$("#edit_modal #attesa").css("display", "none");
}

function updateEndDate() {
	var ore = $("#edit_modal #startTime").val().substring(0, 2);
	var min = $("#edit_modal #startTime").val().substring(3, 5);
	var giorno = $("#edit_modal #startDate").val().substring(0, 2);
	var mese = $("#edit_modal #startDate").val().substring(3, 5);
	var anno = $("#edit_modal #startDate").val().substring(6, 10);
	var dataMinima = new Date(anno, mese - 1, giorno, +ore + 1, min, 0, 0);
	$('#edit_modal #endTimeDiv').data("DateTimePicker").minDate(dataMinima);
	$('#edit_modal #endDateDiv').data("DateTimePicker").minDate(dataMinima);
}

$('#edit_modal #startTime').blur(function(e) {
	updateEndDate();
});

$('#edit_modal #startDate').blur(function(e) {
	updateEndDate();
});
