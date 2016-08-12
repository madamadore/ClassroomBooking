$(document).ready(function(event) {
	var currentDateTime = new Date();
	$('#calendar').fullCalendar({
		timezone : 'local',
		events : function(start, end, timezone, callback) {
			$("#calendar").css("display", "none");
			$("#attesaPrenotazioni").css("display", "unset");
			$.ajax({
				type : "GET",
				url : 'ajax/prenotazioni',
				dataType : 'json',
				success : function(data) {
					$("#calendar").css("display", "unset");
					$("#attesaPrenotazioni").css("display", "none");
					callback(data);
				}
			})
		},
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
			$("#edit_prenotazione #errorDiv").css("display", "none");
			$('#dialog').modal('show');
		},
		eventClick : function(calEvent, jsEvent, view) {
			var hasPerm = hasPermissions(calEvent);
			if (hasPerm) {
				setEditModal(calEvent);
				$("#edit_prenotazione #errorDiv").css("display", "none");
				$('#dialog').modal('show');
			} else {
				setViewModal(calEvent);
				$('#view_modal').modal('show');
			}
		},
		eventResize : function(event, delta, revertFunc) {
			var hasPerm = hasPermissions(event);
			if (!hasPerm) {
				revertFunc();
			} else {
				setEditModal(event);
				salvaPrenotazione(false);
			}
		},
		eventDrop : function(event, delta, revertFunc) {
			var hasPerm = hasPermissions(event);
			if (!hasPerm) {
				revertFunc();
			} else {
				setEditModal(event);
				salvaPrenotazione(false);
			}
		}
	});

	$("#edit_prenotazione #salvaPrenotazione").click(function() {
		salvaPrenotazione(true);
	})
});

$("#edit_prenotazione #deleteButton").click(function areYouSure() {
	var r = confirm("Sei sicuro di voler eliminare questa prenotazione?");
	if (r == true) {
		var id = $("#edit_prenotazione #idPrenotazione").val();
		deleteEvent(id);
	}
})

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
		url : 'ajax/hasPermissions',
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
	$("#dialog #modalTitle").text("Crea una nuova prenotazione");
	$('#edit_prenotazione #startTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});

	$('#edit_prenotazione #endTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#edit_prenotazione #startDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});
	$('#edit_prenotazione #endDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});
	removeEndDateLimitation();
	$('#edit_prenotazione #startTimeDiv').data("DateTimePicker").date("09:00")
	$('#edit_prenotazione #startDateDiv').data("DateTimePicker").date(
			date.format("DD-MM-YYYY"));
	$('#edit_prenotazione #endTimeDiv').data("DateTimePicker").date("10:00");
	$('#edit_prenotazione #endDateDiv').data("DateTimePicker").date(
			date.format("DD-MM-YYYY"));
	updateEndDate();

	$('#edit_prenotazione #deleteButton').attr("disabled", "");
	$('#edit_prenotazione #selectAula option:selected').prop("selected", false);
	$('#edit_prenotazione #idPrenotazione').val("");
	$('#edit_prenotazione #idPrenotazioneDelete').val("");
	$("#edit_prenotazione #sendEmailDiv").css("display", "none");
}

function setEditModal(calEvent) {
	$("#dialog #modalTitle").text("Modifica prenotazione");
	$('#edit_prenotazione #startTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#edit_prenotazione #endTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#edit_prenotazione #startDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});
	$('#edit_prenotazione #endDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});

	removeEndDateLimitation();
	$('#edit_prenotazione #startTimeDiv').data("DateTimePicker").date(
			calEvent.start.format("HH:mm"));
	$('#edit_prenotazione #endTimeDiv').data("DateTimePicker").date(
			calEvent.end.format("HH:mm"));

	$('#edit_prenotazione #startDateDiv').data("DateTimePicker").date(
			calEvent.start.format("DD-MM-YYYY"));
	$('#edit_prenotazione #endDateDiv').data("DateTimePicker").date(
			calEvent.end.format("DD-MM-YYYY"));

	$('#edit_prenotazione #selectAula option:selected').prop("selected", false);
	$('#edit_prenotazione #aulaN' + calEvent.classRoom.id).prop("selected",
			true);
	$('#edit_prenotazione #idPrenotazione').val(calEvent.id);
	$('#edit_prenotazione #idPrenotazioneDelete').val(calEvent.id);
	$('#edit_prenotazione #deleteButton').removeAttr("disabled");
	$("#edit_prenotazione #sendEmailDiv").css("display", "unset");
	$('#edit_prenotazione #sendEmail').prop("href",
			"mailto:" + calEvent.owner.email);
	$('#edit_prenotazione #sendEmail')
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

function salvaPrenotazione(render) {
	var verificaResult = verificaInput();
	if (verificaResult != 'ok') {
		$("#edit_prenotazione #errorMessage").text(verificaResult);
		$("#edit_prenotazione #errorDiv").css("display", "unset");
		return;
	}
	$("#edit_prenotazione #inputPrenotazione").css("display", "none");
	$("#edit_prenotazione #attesa").css("display", "unset");
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	$.ajax({
		type : "POST",
		url : 'prenotazioni/ajax/save',
		data : {
			aula : $("#edit_prenotazione #selectAula option:selected").val(),
			startTime : $("#edit_prenotazione #startTime").val(),
			endTime : $("#edit_prenotazione #endTime").val(),
			startDate : $("#edit_prenotazione #startDate").val(),
			endDate : $("#edit_prenotazione #endDate").val(),
			id : $("#edit_prenotazione #idPrenotazione").val()
		},
		timeout : 10000,
		dataType : 'json',
		success : function(ajaxResponse) {
			var data = ajaxResponse.result;
			$("#edit_prenotazione #inputPrenotazione").css("display", "unset");
			$("#edit_prenotazione #attesa").css("display", "none");
			if (data == "ok") {
				$('#dialog').modal('hide');
				if (render)
					$('#calendar').fullCalendar('renderEvent',
							ajaxResponse.event);
			} else {
				if (data == "login") {
					$("#edit_prenotazione #errorMessage").text(
							"E' necessario effettuare il login");
				} else if (data == "missInput") {
					$("#edit_prenotazione #errorMessage").text(
							"Uno o piu' input mancanti");
				} else if (data == "wrongInput") {
					$("#edit_prenotazione #errorMessage").text(
							"Uno o piu' input sono errati");
				} else if (data == "missingPermission") {
					$("#edit_prenotazione #errorMessage").text(
							"Non hai i permessi");
				} else if (data == "conflict") {
					$("#edit_prenotazione #errorMessage").text(
							"Prenotazione in conflitto con un'altra");
				}
				$("#edit_prenotazione #errorDiv").css("display", "unset");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$("#edit_prenotazione #inputPrenotazione").css("display", "unset");
			$("#edit_prenotazione #attesa").css("display", "none");
			if (errorThrown == "timeout") {
				$("#edit_prenotazione #errorMessage").text(
						"Il server non risponde");
			} else {
				$("#edit_prenotazione #errorMessage").text(
						"E' necessario effettuare il login");
			}
			$("#edit_prenotazione #errorDiv").css("display", "unset");
		}
	});
}

function deleteEvent(idPrenotazione) {
	$("#edit_prenotazione #inputPrenotazione").css("display", "none");
	$("#edit_prenotazione #attesa").css("display", "unset");
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	$.ajax({
		type : "POST",
		url : 'prenotazioni/ajax/delete',
		data : {
			id : idPrenotazione
		},
		timeout : 10000,
		success : function(data) {
			$("#edit_prenotazione #inputPrenotazione").css("display", "unset");
			$("#edit_prenotazione #attesa").css("display", "none");
			if (data == "ok") {
				$('#dialog').modal('hide');
				$('#calendar').fullCalendar('removeEvents', idPrenotazione);
			} else {
				if (data == "login") {
					$("#edit_prenotazione #errorMessage").text(
							"E' necessario effettuare il login");
				} else if (data == "missInput") {
					$("#edit_prenotazione #errorMessage").text(
							"Uno o piu' input mancanti");
				} else if (data == "wrongInput") {
					$("#edit_prenotazione #errorMessage").text(
							"Uno o piu' input sono errati");
				} else if (data == "missingPermission") {
					$("#edit_prenotazione #errorMessage").text(
							"Non hai i permessi");
				} else if (data == "conflict") {
					$("#edit_prenotazione #errorMessage").text(
							"Prenotazione in conflitto con un'altra");
				}
				$("#edit_prenotazione #errorDiv").css("display", "unset");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$("#edit_prenotazione #inputPrenotazione").css("display", "unset");
			$("#edit_prenotazione #attesa").css("display", "none");
			if (errorThrown == "timeout") {
				$("#edit_prenotazione #errorMessage").text(
						"Il server non risponde");
			} else {
				$("#edit_prenotazione #errorMessage").text(
						"E' necessario effettuare il login");
			}
			$("#edit_prenotazione #errorDiv").css("display", "unset");
		}
	});
}

function updateEndDate() {
	var ore = $("#edit_prenotazione #startTime").val().substring(0, 2);
	var min = $("#edit_prenotazione #startTime").val().substring(3, 5);
	var giorno = $("#edit_prenotazione #startDate").val().substring(0, 2);
	var mese = $("#edit_prenotazione #startDate").val().substring(3, 5);
	var anno = $("#edit_prenotazione #startDate").val().substring(6, 10);
	var dataMinima = new Date(anno, mese - 1, giorno, +ore + 1, min, 0, 0);
	if ($("#edit_prenotazione #startDate").val() != $(
			"#edit_prenotazione #endDate").val()) {
		$('#edit_prenotazione #endTimeDiv').data("DateTimePicker").minDate(
				new Date(0));
	} else {
		$('#edit_prenotazione #endTimeDiv').data("DateTimePicker").minDate(
				dataMinima);
	}
	$('#edit_prenotazione #endDateDiv').data("DateTimePicker").minDate(
			dataMinima);
}

function removeEndDateLimitation() {
	var date = new Date(0);
	$('#edit_prenotazione #endTimeDiv').data("DateTimePicker").minDate(date);
	$('#edit_prenotazione #endDateDiv').data("DateTimePicker").minDate(date);
}

$('#edit_prenotazione #startTime').blur(function(e) {
	updateEndDate();
});

$('#edit_prenotazione #startDate').blur(function(e) {
	updateEndDate();
});

$('#edit_prenotazione #endTime').blur(function(e) {
	updateEndDate();
});

$('#edit_prenotazione #endDate').blur(function(e) {
	updateEndDate();
});

function verificaInput() {
	var aula = $("#edit_prenotazione #selectAula option:selected").val();
	var startTime = $("#edit_prenotazione #startTime").val();
	var endTime = $("#edit_prenotazione #endTime").val();
	var startDate = $("#edit_prenotazione #startDate").val();
	var endDate = $("#edit_prenotazione #endDate").val();
	if (aula == '-1')
		return "Selezionare un'aula";
	if (startTime == "")
		return "Selezionare l'ora d'inizio";
	if (endTime == "")
		return "Selezionare l'ora di fine";
	if (startDate == "")
		return "Selezionare la data d'inizio";
	if (endDate == "")
		return "Selezionare la  data di fine";
	return 'ok';
}