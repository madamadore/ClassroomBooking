$(document).ready(
		function(event) {
			var currentDateTime = new Date();
			$('#calendar').fullCalendar(
					{
						header : {
							left : 'prev,next today',
							center : 'title',
							right : 'month,agendaWeek,agendaDay'
						},
						defaultDate : currentDateTime,
						editable : true,
						eventLimit : true, // allow "more" link when too many
						// events
						dayClick : function(date, jsEvent, view) {
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

							$('#edit_modal #startDate').val(
									date.format("DD-MM-YYYY"));
							$('#edit_modal #endDate').val(
									date.format("DD-MM-YYYY"));
							$('#edit_modal').modal('show');
						},
						eventClick : function(calEvent, jsEvent, view) {

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

							$('#edit_modal #startTime').val(
									calEvent.start.format("HH:mm"));
							$('#edit_modal #endTime').val(
									calEvent.end.format("HH:mm"));

							$('#edit_modal #startDate').val(
									calEvent.start.format("DD-MM-YYYY"));
							$('#edit_modal #endDate').val(
									calEvent.end.format("DD-MM-YYYY"));
							$('#edit_modal #selectAula').attr("selected",
									calEvent.classRoom.id);
							$('#edit_modal #idPrenotazione').val(calEvent.id);

							$('#edit_modal').modal('show');
						},
						eventSources : [ {
							url : '/ClassroomBooking/ajax/prenotazioni'
						} ]
					});
		});