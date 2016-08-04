$(document).ready(function(event) {
	var currentDateTime = new Date();
	$('#calendar').fullCalendar({
		timezone : 'local',
		events : function(start, end, timezone, callback) {
			$("#calendar").css("display", "none");
			$("#attesaPrenotazioni").css("display", "unset");
			$.ajax({
				type : "GET",
				url : '/ClassroomBooking/ajax/prenotazioni',
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
			setCreationModal(date, "edit_prenotazione");
			setCreationModal(date, "edit_lezione");
			$("#dialog #errorDiv").css("display", "none");
			$('#dialog').modal('show');
		},
		eventClick : function(calEvent, jsEvent, view) {
			var hasPerm = hasPermissions(calEvent);
			if (hasPerm) {
				$("#typePrenotazione").css("display", "none");
				if (calEvent.type == "Prenotazione") {
					setEditModal(calEvent, "edit_prenotazione");
					$("#dialog #errorDiv").css("display", "none");
				} else {
					setEditModal(calEvent, "edit_lezione");
					$("#edit_lezione #errorDiv").css("display", "none");
				}
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
				setEditModal(event, "edit_prenotazione");
				setEditModal(event, "edit_lezione");
				salvaPrenotazione(false);
			}
		},
		eventDrop : function(event, delta, revertFunc) {
			var hasPerm = hasPermissions(event);
			if (!hasPerm) {
				revertFunc();
			} else {
				setEditModal(event, "edit_prenotazione");
				setEditModal(event, "edit_lezione");
				salvaPrenotazione(false);
			}
		}
	});

	$('#optPrenotazione').change(function(e) {
		$("#edit_lezione").css("display", "none");
		$("#edit_prenotazione").css("display", "unset");
	});

	$('#optLezione').change(function(e) {
		$("#edit_prenotazione").css("display", "none");
		$("#edit_lezione").css("display", "unset");
	});

	$("#edit_prenotazione #salvaPrenotazione").click(function() {
		salvaPrenotazione(true);
	})

	$("#edit_prenotazione #deleteButton").click(function areYouSure() {
		var r = confirm("Sei sicuro di voler eliminare questa prenotazione?");
		if (r == true) {
			var id = $("#edit_prenotazione #idPrenotazione").val();
			deleteEvent(id);
		}
	})

	$("#edit_lezione #salvaPrenotazione").click(function() {
		salvaLezione(true);
	})

	$("#edit_lezione #deleteButton").click(function areYouSure() {
		var r = confirm("Sei sicuro di voler eliminare questa prenotazione?");
		if (r == true) {
			var id = $("#edit_lezione #idPrenotazione").val();
			deleteEvent(id);
		}
	})
});

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

function setCreationModal(date, modalId) {
	$("#typePrenotazione").css("display", "unset");
	$("#dialog #modalTitle").text("Crea una nuova prenotazione");
	$('#' + modalId + ' #startTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});

	$('#' + modalId + ' #endTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#' + modalId + ' #startDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});
	$('#' + modalId + ' #endDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});

	removeEndDateLimitation(modalId);
	$('#' + modalId + ' #startTimeDiv').data("DateTimePicker").date("09:00")
	$('#' + modalId + ' #startDateDiv').data("DateTimePicker").date(
			date.format("DD-MM-YYYY"));
	$('#' + modalId + ' #endTimeDiv').data("DateTimePicker").date("10:00");
	$('#' + modalId + ' #endDateDiv').data("DateTimePicker").date(
			date.format("DD-MM-YYYY"));
	updateEndDate(modalId);

	$('#' + modalId + ' #deleteButton').attr("disabled", "");
	$('#' + modalId + ' #selectAula option:selected').prop("selected", false);
	$('#' + modalId + ' #idPrenotazione').val("");
	$('#' + modalId + ' #idPrenotazioneDelete').val("");
	$('#' + modalId + ' #sendEmailDiv').css("display", "none");
}

function setEditModal(calEvent, modalId) {
	$("#dialog #modalTitle").text("Modifica prenotazione");
	$('#' + modalId + ' #startTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#' + modalId + ' #endTimeDiv').datetimepicker({
		locale : 'it',
		format : 'LT'
	});
	$('#' + modalId + ' #startDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});
	$('#' + modalId + ' #endDateDiv').datetimepicker({
		locale : 'it',
		format : 'DD-MM-YYYY'
	});

	removeEndDateLimitation(modalId);
	$('#' + modalId + ' #startTimeDiv').data("DateTimePicker").date(
			calEvent.start.format("HH:mm"));
	$('#' + modalId + ' #endTimeDiv').data("DateTimePicker").date(
			calEvent.end.format("HH:mm"));

	$('#' + modalId + ' #startDateDiv').data("DateTimePicker").date(
			calEvent.start.format("DD-MM-YYYY"));
	$('#' + modalId + ' #endDateDiv').data("DateTimePicker").date(
			calEvent.end.format("DD-MM-YYYY"));

	$('#' + modalId + ' #selectAula option:selected').prop("selected", false);
	$('#' + modalId + ' #aulaN' + calEvent.classRoom.id).prop("selected", true);
	$('#' + modalId + ' #idPrenotazione').val(calEvent.id);
	$('#' + modalId + ' #idPrenotazioneDelete').val(calEvent.id);
	$('#' + modalId + ' #deleteButton').removeAttr("disabled");
	$('#' + modalId + ' #sendEmailDiv').css("display", "unset");
	$('#' + modalId + ' #sendEmail').prop("href",
			"mailto:" + calEvent.owner.email);
	$('#' + modalId + ' #sendEmail')
			.html(
					calEvent.owner.name
							+ " "
							+ calEvent.owner.cognome
							+ " "
							+ '<span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>');

	if (calEvent.type == "Lezione") {
		$('#' + modalId + ' #limite').val(calEvent.limite);
		$('#' + modalId + ' #docente').val(calEvent.docente);
		$('#' + modalId + ' #descrizione').val(calEvent.descrizione);
	}
}

function setViewModal(calEvent, modalId) {
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
		$("#dialog #errorMessage").text(verificaResult);
		$("#dialog #errorDiv").css("display", "unset");
		return;
	}
	$("#edit_prenotazione #inputPrenotazione").css("display", "none");
	$("#dialog #attesa").css("display", "unset");
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	$.ajax({
		type : "POST",
		url : '/ClassroomBooking/prenotazioni/ajax/save',
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
			$("#dialog #attesa").css("display", "none");
			if (data == "ok") {
				$('#dialog').modal('hide');
				if (render)
					$('#calendar').fullCalendar('renderEvent',
							ajaxResponse.event);
			} else {
				showErrorMessage(data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$("#edit_prenotazione #inputPrenotazione").css("display", "unset");
			$("#dialog #attesa").css("display", "none");
			if (errorThrown == "timeout") {
				$("#dialog #errorMessage").text("Il server non risponde");
			} else {
				$("#dialog #errorMessage").text(
						"E' necessario effettuare il login");
			}
			$("#dialog #errorDiv").css("display", "unset");
		}
	});
}

function salvaLezione(render) {
	var verificaResult = verificaInputLezione();
	if (verificaResult != 'ok') {
		$("#dialog #errorMessage").text(verificaResult);
		$("#dialog #errorDiv").css("display", "unset");
		return;
	}
	$("#edit_lezione #inputPrenotazione").css("display", "none");
	$("#dialog #attesa").css("display", "unset");
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	$.ajax({
		type : "POST",
		url : '/ClassroomBooking/lezioni/ajaxLezioni/save',
		data : {
			aula : $("#edit_lezione #selectAula option:selected").val(),
			startTime : $("#edit_lezione #startTime").val(),
			endTime : $("#edit_lezione #endTime").val(),
			startDate : $("#edit_lezione #startDate").val(),
			endDate : $("#edit_lezione #endDate").val(),
			id : $("#edit_lezione #idPrenotazione").val(),
			titolo : $("#edit_lezione #titolo").val(),
			limite : $("#edit_lezione #limite").val(),
			docente : $("#edit_lezione #docente").val(),
			descrizione : $("#edit_lezione #descrizione").val()
		},
		timeout : 10000,
		dataType : 'json',
		success : function(ajaxResponse) {
			var data = ajaxResponse.result;
			$("#edit_lezione #inputPrenotazione").css("display", "unset");
			$("#dialog #attesa").css("display", "none");
			if (data == "ok") {
				$('#dialog').modal('hide');
				if (render)
					$('#calendar').fullCalendar('renderEvent',
							ajaxResponse.event);
			} else {
				showErrorMessage(data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$("#edit_lezione #inputPrenotazione").css("display", "unset");
			$("#dialog #attesa").css("display", "none");
			if (errorThrown == "timeout") {
				$("#dialog #errorMessage").text("Il server non risponde");
			} else {
				$("#dialog #errorMessage").text(
						"E' necessario effettuare il login");
			}
			$("#dialog #errorDiv").css("display", "unset");
		}
	});
}

function deleteEvent(idPrenotazione) {
	$("#edit_prenotazione #inputPrenotazione").css("display", "none");
	$("#dialog #attesa").css("display", "unset");
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	$.ajax({
		type : "POST",
		url : '/ClassroomBooking/prenotazioni/ajax/delete',
		data : {
			id : idPrenotazione
		},
		timeout : 10000,
		success : function(data) {
			$("#edit_prenotazione #inputPrenotazione").css("display", "unset");
			$("#dialog #attesa").css("display", "none");
			if (data == "ok") {
				$('#dialog').modal('hide');
				$('#calendar').fullCalendar('removeEvents', idPrenotazione);
			} else {
				showErrorMessage(data);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			$("#edit_prenotazione #inputPrenotazione").css("display", "unset");
			$("#dialog #attesa").css("display", "none");
			if (errorThrown == "timeout") {
				$("#dialog #errorMessage").text("Il server non risponde");
			} else {
				$("#dialog #errorMessage").text(
						"E' necessario effettuare il login");
			}
			$("#dialog #errorDiv").css("display", "unset");
		}
	});
}

function updateEndDate(modalId) {
	var ore = $('#' + modalId + ' #startTime').val().substring(0, 2);
	var min = $('#' + modalId + ' #startTime').val().substring(3, 5);
	var giorno = $('#' + modalId + ' #startDate').val().substring(0, 2);
	var mese = $('#' + modalId + ' #startDate').val().substring(3, 5);
	var anno = $('#' + modalId + ' #startDate').val().substring(6, 10);
	var dataMinima = new Date(anno, mese - 1, giorno, +ore + 1, min, 0, 0);
	if ($('#' + modalId + ' #startDate').val() != $('#' + modalId + ' #endDate')
			.val()) {
		$('#' + modalId + ' #endTimeDiv').data('DateTimePicker').minDate(
				new Date(0));
	} else {
		$('#' + modalId + ' #endTimeDiv').data('DateTimePicker').minDate(
				dataMinima);
	}
	$('#' + modalId + ' #endDateDiv').data('DateTimePicker')
			.minDate(dataMinima);
}

function removeEndDateLimitation(modalId) {
	var date = new Date(0);
	$('#' + modalId + ' #endTimeDiv').data("DateTimePicker").minDate(date);
	$('#' + modalId + ' #endDateDiv').data("DateTimePicker").minDate(date);
}

$('#edit_prenotazione #startTime').blur(function(e) {
	updateEndDate("edit_prenotazione");
});

$('#edit_prenotazione #startDate').blur(function(e) {
	updateEndDate("edit_prenotazione");
});

$('#edit_prenotazione #endTime').blur(function(e) {
	updateEndDate("edit_prenotazione");
});

$('#edit_prenotazione #endDate').blur(function(e) {
	updateEndDate("edit_prenotazione");
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

function showErrorMessage(data) {
	if (data == "login") {
		$("#dialog #errorMessage").text("E' necessario effettuare il login");
	} else if (data == "missInput") {
		$("#dialog #errorMessage").text("Uno o piu' input mancanti");
	} else if (data == "wrongInput") {
		$("#dialog #errorMessage").text("Uno o piu' input sono errati");
	} else if (data == "missingPermission") {
		$("#dialog #errorMessage").text("Non hai i permessi");
	} else if (data == "conflict") {
		$("#dialog #errorMessage").text(
				"Prenotazione in conflitto con un'altra");
	}
	$("#dialog #errorDiv").css("display", "unset");
}

function verificaInputLezione() {
	var aula = $("#edit_lezione #selectAula option:selected").val();
	var startTime = $("#edit_lezione #startTime").val();
	var endTime = $("#edit_lezione #endTime").val();
	var startDate = $("#edit_lezione #startDate").val();
	var endDate = $("#edit_lezione #endDate").val();
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