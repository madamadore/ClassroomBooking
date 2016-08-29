$(document).ready(function(event) {
	var currentDateTime = new Date();
	$('#calendar').fullCalendar({
		timezone : 'local',
		events : function(start, end, timezone, callback) {
			$("#calendar").css("display", "none");
			$("#attesaPrenotazioni").css("display", "unset");
			$.ajax({
				type : "GET",
				url : 'prenotazioni/ajax/list',
				dataType : 'json',
				success : function(data) {
					for (var i = 0; i < data.length; i++) {
						if (data[i].type == "Lezione") {
							data[i].color = "#01DF01";
						}
					}
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
			console.log("hasRole = " + hasRole("ROLE_TECHER"));
			setCreationModal(date, "edit_prenotazione");
			if(hasRole("ROLE_TECHER")){
				setCreationModal(date, "edit_lezione");
				$("#dialog #optPrenotazione").prop("checked", true);
			}else{
				$("#dialog #typePrenotazione").css("display", "none");
			}
			$("#dialog #errorDiv").css("display", "none");
			$("#edit_lezione").css("display", "none");
			$("#edit_prenotazione").css("display", "unset");
			$('#dialog').modal('show');
		},
		eventClick : function(calEvent, jsEvent, view) {
			var hasPerm = hasPermissions(calEvent);
			if (hasPerm) {
				$("#dialog #errorDiv").css("display", "none");
				$("#dialog #typePrenotazione").css("display", "none");
				if (calEvent.type == "Prenotazione") {
					setEditModal(calEvent, "edit_prenotazione");
					$("#edit_lezione").css("display", "none");
					$("#edit_prenotazione").css("display", "unset");
				} else {
					setEditModal(calEvent, "edit_lezione");
					$("#edit_lezione #errorDiv").css("display", "none");
					$("#edit_prenotazione").css("display", "none");
					$("#edit_lezione").css("display", "unset");
				}
				$('#dialog').modal('show');
			} else {
				var modalId = "viewPrenotazione_modal";
				if (calEvent.type == "Lezione") {
					modalId = "viewLezione_modal";
				}
				setViewModal(calEvent, modalId);
				$('#' + modalId).modal('show');
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

	$('#edit_lezione #startTime').blur(function(e) {
		updateEndDate("edit_lezione");
	});

	$('#edit_lezione #startDate').blur(function(e) {
		updateEndDate("edit_lezione");
	});

	$('#edit_lezione #endTime').blur(function(e) {
		updateEndDate("edit_lezione");
	});

	$('#edit_lezione #endDate').blur(function(e) {
		updateEndDate("edit_lezione");
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

	$("#edit_lezione #iscriviti").click(function() {
		var idLezione = $("#edit_lezione #idPrenotazione").val();
		iscriviti(idLezione, "edit_lezione");
	})

	$("#edit_lezione #annullaIscrizione").click(function() {
		var idLezione = $("#edit_lezione #idPrenotazione").val();
		annullaIscrizione(idLezione, "edit_lezione");
	})

	$("#edit_lezione #listaIscritti").click(function() {
		$("#dialog").modal('hide');
		$("#registeredDialog").modal('show');
		var idLezione = $("#edit_lezione #idPrenotazione").val();
		getIscritti(idLezione);
	})

	$("#viewLezione_modal #iscriviti").click(function() {
		var idLezione = $("#viewLezione_modal #idLezione").val();
		iscriviti(idLezione, "viewLezione_modal");
	})

	$("#viewLezione_modal #annullaIscrizione").click(function() {
		var idLezione = $("#viewLezione_modal #idLezione").val();
		annullaIscrizione(idLezione, "viewLezione_modal");
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

	date.hour(9);
	date.minute(0);
	$('#' + modalId + ' #startTimeDiv').data("DateTimePicker").date(date);
	$('#' + modalId + ' #startDateDiv').data("DateTimePicker").date(date);
	date.hour(10);
	$('#' + modalId + ' #endTimeDiv').data("DateTimePicker").date(date);
	$('#' + modalId + ' #endDateDiv').data("DateTimePicker").date(date);
	updateEndDate(modalId);

	$('#' + modalId + ' #deleteButton').attr("disabled", "");
	$('#' + modalId + ' #selectAula option:selected').prop("selected", false);
	$('#' + modalId + ' #idPrenotazione').val("");
	$('#' + modalId + ' #idPrenotazioneDelete').val("");
	$('#' + modalId + ' #sendEmailDiv').css("display", "none");

	if (modalId == "edit_lezione") {
		$('#' + modalId + ' #titolo').val("");
		$('#' + modalId + ' #limite').val("");
		$('#' + modalId + ' #docente').val("");
		$('#' + modalId + ' #descrizione').val("");
		$('#' + modalId + ' #listaIscritti').attr("disabled", "");
		$('#' + modalId + ' #iscriviti').attr("disabled", "");
		$('#' + modalId + ' #iscrivitiDiv').css("display", "unset");
		$('#' + modalId + ' #annullaIscrizioneDiv').css("display", "none");
	}
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

	$('#' + modalId + ' #startTimeDiv').data("DateTimePicker").date(
			calEvent.start);
	$('#' + modalId + ' #endTimeDiv').data("DateTimePicker").date(calEvent.end);

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
		$('#' + modalId + ' #titolo').val(calEvent.title);
		$('#' + modalId + ' #limite').val(calEvent.limite);
		$('#' + modalId + ' #docente').val(calEvent.docente);
		$('#' + modalId + ' #descrizione').val(calEvent.descrizione);
		$('#' + modalId + ' #listaIscritti').removeAttr("disabled");
		$('#' + modalId + ' #iscriviti').removeAttr("disabled");
		var iscritto = verificaIscrizione(calEvent.id);
		if (iscritto) {
			$('#' + modalId + ' #iscrivitiDiv').css("display", "none");
			$('#' + modalId + ' #annullaIscrizioneDiv').css("display", "unset");
		} else {
			$('#' + modalId + ' #iscrivitiDiv').css("display", "unset");
			$('#' + modalId + ' #annullaIscrizioneDiv').css("display", "none");
		}
	}
}

function setViewModal(calEvent, modalId) {
	$('#' + modalId + ' #startTime').text(calEvent.start.format("HH:mm"));
	$('#' + modalId + ' #endTime').text(calEvent.end.format("HH:mm"));
	$('#' + modalId + ' #startDate').text(calEvent.start.format("DD-MM-YYYY"));
	$('#' + modalId + ' #endDate').text(calEvent.end.format("DD-MM-YYYY"));
	$('#' + modalId + ' #aula').text(calEvent.classRoom.name);
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
		$('#' + modalId + ' #titolo').text(calEvent.title);
		$('#' + modalId + ' #limite').text(calEvent.limite);
		$('#' + modalId + ' #docente').text(calEvent.docente);
		$('#' + modalId + ' #descrizione').text(calEvent.descrizione);
		$('#' + modalId + ' #idLezione').val(calEvent.id);
		$('#' + modalId + ' #errorDiv').css("display", "none");
		var iscritto = verificaIscrizione(calEvent.id);
		if (iscritto) {
			$('#' + modalId + ' #iscrivitiDiv').css("display", "none");
			$('#' + modalId + ' #annullaIscrizioneDiv').css("display", "unset");
		} else {
			$('#' + modalId + ' #iscrivitiDiv').css("display", "unset");
			$('#' + modalId + ' #annullaIscrizioneDiv').css("display", "none");
		}
	}
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
			$("#dialog #attesa").css("display", "none");
			if (data == "ok") {
				$('#dialog').modal('hide');
				if (render) {
					$('#calendar').fullCalendar('removeEvents',
							ajaxResponse.event.id);
					$('#calendar').fullCalendar('renderEvent',
							ajaxResponse.event);
				}
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
				console.log("Errore qui");
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
		url : 'lezioni/ajaxLezioni/save',
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
				if (render) {
					$('#calendar').fullCalendar('removeEvents',
							ajaxResponse.event.id);
					ajaxResponse.event.color = "#01DF01";
					$('#calendar').fullCalendar('renderEvent',
							ajaxResponse.event);
				}
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
		url : 'prenotazioni/ajax/delete',
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
	var start = "" + anno + mese + giorno + ore + min;
	var end = $('#' + modalId + ' #endDateDiv').data('DateTimePicker').date()
			.format("YYYYMMDD")
			+ $('#' + modalId + ' #endTimeDiv').data('DateTimePicker').date()
					.format("HHmm");
	var unora = 100;
	if (end - start < unora) {
		$('#' + modalId + ' #endTimeDiv').data('DateTimePicker').date(
				dataMinima);
		$('#' + modalId + ' #endDateDiv').data('DateTimePicker').date(
				dataMinima);
	}
}

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
	var titolo = $("#edit_lezione #titolo").val();
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
	if (titolo == "")
		return "Inserire un titolo";
	return 'ok';
}

function iscriviti(idLezione, modalId) {
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	if (modalId == "edit_lezione") {
		$("#edit_lezione #inputPrenotazione").css("display", "none");
		$("#dialog #attesa").css("display", "unset");
	} else {
		$("#" + modalId + " #informazioni").css("display", "none");
		$("#" + modalId + " #attesa").css("display", "unset");
	}
	$.ajax({
		type : "POST",
		url : 'lezioni/ajaxLezioni/iscriviti',
		timeout : 10000,
		data : {
			idLezione : idLezione
		},
		success : function(data) {
			var errorModal = modalId;
			if (modalId == "edit_lezione") {
				$("#edit_lezione #inputPrenotazione").css("display", "unset");
				$("#dialog #attesa").css("display", "none");
				errorModal = "dialog";
			} else {
				$("#" + modalId + " #informazioni").css("display", "unset");
				$("#" + modalId + " #attesa").css("display", "none");
			}
			if (data == "ok") {
				$('#' + modalId + ' #iscrivitiDiv').css("display", "none");
				$('#' + modalId + ' #annullaIscrizioneDiv').css("display",
						"unset");
			} else if (data == "limitExeeded") {
				$("#" + errorModal + " #errorMessage").text(
						"Limite di iscritti raggiunto");
				$("#" + errorModal + " #errorDiv").css("display", "unset");
			} else {
				$("#" + errorModal + " #errorMessage").text(
						"E' necessario effettuare il login");
				$("#" + errorModal + " #errorDiv").css("display", "unset");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			var errorModal = modalId;
			if (modalId == "edit_lezione") {
				$("#edit_lezione #inputPrenotazione").css("display", "unset");
				$("#dialog #attesa").css("display", "none");
				errorModal = "dialog";
			} else {
				$("#" + modalId + " #informazioni").css("display", "unset");
				$("#" + modalId + " #attesa").css("display", "none");
			}
			if (errorThrown == "timeout") {
				$("#" + errorModal + " #errorMessage").text(
						"Il server non risponde");
			} else {
				$("#" + errorModal + " #errorMessage").text(
						"E' necessario effettuare il login");
			}
			$("#" + errorModal + " #errorDiv").css("display", "unset");
		}
	});
}

function annullaIscrizione(idLezione, modalId) {
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	if (modalId == "edit_lezione") {
		$("#edit_lezione #inputPrenotazione").css("display", "none");
		$("#dialog #attesa").css("display", "unset");
	} else {
		$("#" + modalId + " #informazioni").css("display", "none");
		$("#" + modalId + " #attesa").css("display", "unset");
	}
	$.ajax({
		type : "POST",
		url : 'lezioni/ajaxLezioni/annullaIscrizione',
		timeout : 10000,
		data : {
			idLezione : idLezione
		},
		success : function(data) {
			var errorModal = modalId;
			if (modalId == "edit_lezione") {
				$("#edit_lezione #inputPrenotazione").css("display", "unset");
				$("#dialog #attesa").css("display", "none");
				errorModal = "dialog";
			} else {
				$("#" + modalId + " #informazioni").css("display", "unset");
				$("#" + modalId + " #attesa").css("display", "none");
			}
			if (data == "ok") {
				$('#' + modalId + ' #iscrivitiDiv').css("display", "unset");
				$('#' + modalId + ' #annullaIscrizioneDiv').css("display",
						"none");
			} else {
				$("#" + errorModal + " #errorMessage").text(
						"E' necessario effettuare il login");
				$("#" + errorModal + " #errorDiv").css("display", "unset");
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			var errorModal = modalId;
			if (modalId == "edit_lezione") {
				$("#edit_lezione #inputPrenotazione").css("display", "unset");
				$("#dialog #attesa").css("display", "none");
				errorModal = "dialog";
			} else {
				$("#" + modalId + " #informazioni").css("display", "unset");
				$("#" + modalId + " #attesa").css("display", "none");
			}
			if (errorThrown == "timeout") {
				$("#" + errorModal + " #errorMessage").text(
						"Il server non risponde");
			} else {
				$("#" + errorModal + " #errorMessage").text(
						"E' necessario effettuare il login");
			}
			$("#" + errorModal + " #errorDiv").css("display", "unset");
		}
	});
}

function verificaIscrizione(idLezione) {
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	var iscritto = false;
	$.ajax({
		type : "POST",
		url : 'lezioni/ajaxLezioni/verificaIscrizione',
		dataType : 'json',
		data : {
			idLezione : idLezione
		},
		timeout : 10000,
		async : false,
		success : function(data) {
			iscritto = data;
		}
	});
	return iscritto;
}

function getIscritti(idLezione) {
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	var nomi = [];
	$.ajax({
		type : "POST",
		url : 'lezioni/ajaxLezioni/getIscritti',
		dataType : 'json',
		data : {
			idLezione : idLezione
		},
		timeout : 10000,
		async : false,
		success : function(data) {
			nomi = data;
		}
	});
	var htmlNomi = "";
	for (var i = 0; i < nomi.length; i++) {
		htmlNomi = htmlNomi + '<li class="list-group-item">' + nomi[i]
				+ '</li>';
	}
	if (htmlNomi == "") {
		htmlNomi = '<li class="list-group-item">Nessun Iscritto</li>';
	}
	$("#registeredDialog #listaNomi").html(htmlNomi);
}

function hasRole(role) {
	var token = $("input[name='_csrf']").val();
	var header = "X-CSRF-TOKEN";
	$(document).ajaxSend(function(e, xhr, options) {
		xhr.setRequestHeader(header, token);
	});
	var hasRole = false;
	$.ajax({
		type : "POST",
		url : 'admin/user/ajax/hasRole',
		data : {
			role : role
		},
		dataType : 'json',
		timeout : 10000,
		async : false,
		success : function(data) {
			hasRole = data;
		}
	});
	return hasRole;
}
