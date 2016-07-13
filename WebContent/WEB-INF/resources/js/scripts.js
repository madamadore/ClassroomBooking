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
				$("#addRuolo").show();
			});
			
			$("#addRuolo").click(function(){
				var ruolo = $("#selectRuoli option:selected").value();
				var descRuolo = $("#selectRuoli option:selected").text();
				$("#rowRuoli").append('<tr><td>&nbsp;</td><td>' + ruolo + '</td>' +
											'<td>' + descRuolo + '</td>' + 
											'<td><a id="removeRuolo" data-toggle="tooltip" data-placement="top" title="Elimina" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>' +  
											'<input type="hidden" th:name="userRoleId" value="" />' +
											'<input type="hidden" th:name="user" th:value="${user.id}" />' +
											'<input type="hidden" th:name="role" value="' + ruolo + '" />' +
											'<input type="hidden" th:name="descrizione" th:value="' + descRuolo + '" />' +
											'</td></tr>');
			});
			
			$("#removeRuolo").click(function(){
				$(this).parent().parent().remove();
			});
			
		});
