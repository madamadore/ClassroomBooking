$(document).ready(function() {

	$('#calendar').fullCalendar({
		header : {
			left : 'prev,next today',
			center : 'title',
			right : 'month,agendaWeek,agendaDay'
		},
		defaultDate : '2016-06-16',
		editable : true,
		eventLimit : true, // allow "more" link when too many events
		dayClick : function(date, jsEvent, view) {
			$('#startTime').datetimepicker({
				locale : 'it',
				format: 'LT'
			});
			$('#endTime').datetimepicker({
				locale : 'it',
				format: 'LT'
			});
			$('#startDateDiv').datetimepicker({
				locale : 'it',
				format: 'DD-MM-YYYY'
			});
			$('#endDateDiv').datetimepicker({
				locale : 'it',
				format: 'DD-MM-YYYY'
			});
			var startDate = document.getElementById("startDate");
			startDate.setAttribute("placeholder", date.format("DD-MM-YYYY"));
			var endDate = document.getElementById("endDate");
			endDate.setAttribute("placeholder", date.format("DD-MM-YYYY"));
			$('#new_modal').modal('show');
			// alert('Clicked on: ' + date.format());
			// alert('Coordinates: ' + jsEvent.pageX + ','
			// + jsEvent.pageY);
			// alert('Current view: ' + view.name);

			// change the day's background color just for fun
			$(this).css('background-color', 'red');
		},
		eventClick : function(calEvent, jsEvent, view) {

			alert('Event: ' + calEvent.title);
			alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
			alert('View: ' + view.name);
			// change the border color just for fun
			$(this).css('border-color', 'red');
		},
		events : [ {
			title : 'All Day Event',
			start : '2016-06-01'
		}, {
			title : 'Long Event',
			start : '2016-06-07',
			end : '2016-06-10',
			backgroundColor : 'red'
		}, {
			id : 999,
			title : 'Repeating Event',
			start : '2016-06-09T16:00:00'
		}, {
			id : 999,
			title : 'Repeating Event',
			start : '2016-06-16T16:00:00'
		}, {
			title : 'Conference',
			start : '2016-06-11',
			end : '2016-06-13'
		}, {
			title : 'Meeting',
			start : '2016-06-12T10:30:00',
			end : '2016-06-12T12:30:00'
		}, {
			title : 'Lunch',
			start : '2016-06-12T12:00:00'
		}, {
			title : 'Meeting',
			start : '2016-06-12T14:30:00'
		}, {
			title : 'Happy Hour',
			start : '2016-06-12T17:30:00'
		}, {
			title : 'Dinner',
			start : '2016-06-12T20:00:00'
		}, {
			title : 'Birthday Party',
			start : '2016-06-13T07:00:00'
		}, {
			title : 'Click for Google',
			url : 'http://google.com/',
			start : '2016-06-28'
		} ]

	});

});