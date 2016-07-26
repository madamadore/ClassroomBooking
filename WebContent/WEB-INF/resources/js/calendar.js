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
			$('#edit_modal').modal('show');
		},
		eventClick : function(calEvent, jsEvent, view) {
			var hasPerm = hasPermissions(calEvent);
			if (hasPerm) {
				setEditModal(calEvent);
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
				$("#formEdit").submit();
			}
		}
	});
});

function areYouSure() {
	var r = confirm("Sei sicuro di voler eliminare questa prenotazione?");
	if (r == true) {
		$("#formDelete").submit();
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

	$('#edit_modal #startTime').val("");
	$('#edit_modal #endTime').val("");

	$('#edit_modal #startDate').val(date.format("DD-MM-YYYY"));
	$('#edit_modal #endDate').val(date.format("DD-MM-YYYY"));
	$('#edit_modal #deleteButton').attr("disabled", "");
	$('#edit_modal #selectAula option:selected').prop("selected", false);
	$('#edit_modal #idPrenotazione').val("");
	$('#edit_modal #idPrenotazioneDelete').val("");
}

function setEditModal(calEvent) {
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
}

function setViewModal(calEvent) {
	$('#view_modal #startTime').text(calEvent.start.format("HH:mm"));
	$('#view_modal #endTime').text(calEvent.end.format("HH:mm"));
	$('#view_modal #startDate').text(calEvent.start.format("DD-MM-YYYY"));
	$('#view_modal #endDate').text(calEvent.end.format("DD-MM-YYYY"));
	$('#view_modal #aula').text(calEvent.classRoom.name);
}

function salvaPrenotazione() {
//	var token = $("input[name='_csrf']").val();
//	var header = "X-CSRF-TOKEN";
//	$(document).ajaxSend(function(e, xhr, options) {
//		xhr.setRequestHeader(header, token);
//	});
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
			console.log(data);
		}
	});
}