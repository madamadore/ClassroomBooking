$(document)
		.ready(
				function(event) {
					var currentDateTime = new Date();
					$('#calendar')
							.fullCalendar(
									{
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
											$('#edit_modal #startTimeDiv')
													.datetimepicker({
														locale : 'it',
														format : 'LT'
													});
											$('#edit_modal #endTimeDiv')
													.datetimepicker({
														locale : 'it',
														format : 'LT'
													});
											$('#edit_modal #startDateDiv')
													.datetimepicker({
														locale : 'it',
														format : 'DD-MM-YYYY'
													});
											$('#edit_modal #endDateDiv')
													.datetimepicker({
														locale : 'it',
														format : 'DD-MM-YYYY'
													});

											$('#edit_modal #startTime').val("");
											$('#edit_modal #endTime').val("");

											$('#edit_modal #startDate').val(
													date.format("DD-MM-YYYY"));
											$('#edit_modal #endDate').val(
													date.format("DD-MM-YYYY"));
											$('#edit_modal #deleteButton')
													.attr("disabled", "");
											$(
													'#edit_modal #selectAula option:selected')
													.removeAttr("selected");
											$('#edit_modal #idPrenotazione')
													.val("");
											$(
													'#edit_modal #idPrenotazioneDelete')
													.val("");
											$('#edit_modal').modal('show');
										},
										eventClick : function(calEvent,
												jsEvent, view) {
											var hasPermissions = false;

											var token = $("input[name='_csrf']")
													.val();
											var header = "X-CSRF-TOKEN";
											$(document).ajaxSend(
													function(e, xhr, options) {
														xhr.setRequestHeader(
																header, token);
													});

											$
													.ajax({
														type : "POST",
														contentType : "application/json",
														url : '/ClassroomBooking/ajax/hasPermissions',
														data : JSON
																.stringify(calEvent.owner),
														dataType : 'json',
														timeout : 10000,
														async : false,
														success : function(data) {
															hasPermissions = data;
														}
													});

											if (Boolean(hasPermissions)) {
												$('#edit_modal #startTimeDiv')
														.datetimepicker({
															locale : 'it',
															format : 'LT'
														});
												$('#edit_modal #endTimeDiv')
														.datetimepicker({
															locale : 'it',
															format : 'LT'
														});
												$('#edit_modal #startDateDiv')
														.datetimepicker(
																{
																	locale : 'it',
																	format : 'DD-MM-YYYY'
																});
												$('#edit_modal #endDateDiv')
														.datetimepicker(
																{
																	locale : 'it',
																	format : 'DD-MM-YYYY'
																});

												$('#edit_modal #startTime')
														.val(
																calEvent.start
																		.format("HH:mm"));
												$('#edit_modal #endTime')
														.val(
																calEvent.end
																		.format("HH:mm"));

												$('#edit_modal #startDate')
														.val(
																calEvent.start
																		.format("DD-MM-YYYY"));
												$('#edit_modal #endDate')
														.val(
																calEvent.end
																		.format("DD-MM-YYYY"));
												$(
														'#edit_modal #selectAula option:selected')
														.removeAttr("selected");
												$('#edit_modal #aulaN'+ calEvent.classRoom.id)
														.attr("selected", "");
												$('#edit_modal #idPrenotazione')
														.val(calEvent.id);
												$(
														'#edit_modal #idPrenotazioneDelete')
														.val(calEvent.id);
												$('#edit_modal #deleteButton')
														.removeAttr("disabled");
												$('#edit_modal').modal('show');
											} else {
												$('#view_modal #startTime')
														.text(
																calEvent.start
																		.format("HH:mm"));
												$('#view_modal #endTime')
														.text(
																calEvent.end
																		.format("HH:mm"));

												$('#view_modal #startDate')
														.text(
																calEvent.start
																		.format("DD-MM-YYYY"));
												$('#view_modal #endDate')
														.text(
																calEvent.end
																		.format("DD-MM-YYYY"));
												$('#view_modal #aula')
														.text(
																calEvent.classRoom.name);

												$('#view_modal').modal('show');
											}
										},
										eventSources : [ {
											url : '/ClassroomBooking/ajax/prenotazioni'
										} ]
									});
				});