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
		eventLimit : true, // allow "more" link when too many
		// events
		dayClick : function(date, jsEvent, view) {
			$('#startTimeDiv').datetimepicker({
				locale : 'it',
				format : 'LT'
			});
			$('#endTimeDiv').datetimepicker({
				locale : 'it',
				format : 'LT'
			});
			$('#startDateDiv').datetimepicker({
				locale : 'it',
				format : 'DD-MM-YYYY'
			});
			$('#endDateDiv').datetimepicker({
				locale : 'it',
				format : 'DD-MM-YYYY'
			});
			var startDate = document.getElementById("startDate");
			startDate.setAttribute("value", date.format("DD-MM-YYYY"));
			var endDate = document.getElementById("endDate");
			endDate.setAttribute("value", date.format("DD-MM-YYYY"));
			$('#new_modal').modal('show');
		},
		eventClick : function(calEvent, jsEvent, view) {

			alert('Event: ' + calEvent.title);
			alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
			alert('View: ' + view.name);
			// change the border color just for fun
			$(this).css('border-color', 'red');
		},
		eventSources : [ {
			url : '/ClassroomBooking/ajax/prenotazioni'
		} ]
	});
});