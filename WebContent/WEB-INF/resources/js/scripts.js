/*
 * Scripts generici 
 */

	$(document).ready(function(){
			$.validator.addMethod("emailTecno", function(value, element) {
				return (!value.indexOf('@') > -1);
			}, "Inserire solo nome Utente");

			$("#form").validate({
				rules : {
					conf_password : {
						equalTo : "#password"
					},
					emailTecno : {
						emailTecno : true
					}
				}
			});
			
			$("#selectRuoli").change(function(){
				$("#addRuolo").removeClass('hidden');
			});
			
			$("#addRuolo").click(function(){
				var ruolo = $("#selectRuoli option:selected").val();
				var descRuolo = $("#selectRuoli option:selected").text();
				$("#rowRuoli").append('<tr><td>&nbsp;</td><td>' + ruolo + '</td>' +
											'<td>' + descRuolo + '</td>' + 
											'<td><a class="removeRuolo" data-toggle="tooltip" data-placement="top" title="Elimina" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>' +  
											'<input type="hidden" th:name="userRoleId" value="" />' +
											'<input type="hidden" th:name="user" value="' + $("#id").val() + '" />' +
											'<input type="hidden" th:name="role" value="' + ruolo + '" />' +
											'<input type="hidden" th:name="descrizione" value="' + descRuolo + '" />' +
											'</td></tr>');
			});
			
			$(".removeRuolo").click(function(){
				$(this).parent().parent().remove();
			});
			
		});
