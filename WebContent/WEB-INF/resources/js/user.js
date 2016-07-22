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
			
			/*<![CDATA[*/
		        $("#addRuolo").click(function(){
					var ruolo = $("#selectRuoli option:selected").val();
					var descRuolo = $("#selectRuoli option:selected").text();
					var rowCount = $("#tableRoles tr").length;
					
					var input1 = '<input type="hidden"' /*[[th:field="*{userRole[__${']]*/ + rowCount + /*[['}__].user"]]*/ ' value="' + $("#id").val() + '" />';
					var input2 = '<input type="hidden"' /*[[th:field="*{userRole[__${']]*/ + rowCount + /*[['}__].role"]]*/ ' value="' + ruolo + '" />';
					var input3 = '<input type="hidden"' /*[[th:field="*{userRole[__${']]*/ + rowCount + /*[['}__].descrizione"]]*/ ' value="' + descRuolo + '" />';
						
					var context = '<tr><td>&nbsp;</td><td>' + ruolo + '</td>' +
									'<td>' + descRuolo + '</td>' + 
									'<td><a class="removeRole"' /*[[th:field="*{userRole[__${']]*/ + rowCount + /*[['}__].userRoleId}"]]*/ ' name="removeRole" data-placement="top" title="Elimina Ruolo" href="javascript:void(0);">' +
									'<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>' +  
									input1 + input2 + input3 + '</td></tr>';
						
					$("#rowRuoli").append(context);
				});
				
				$(".removeRole").click(function(){
					
					var deleteR = false;
					var token = $("input[name='_csrf']").val();
					var header = "X-CSRF-TOKEN";
			        
					$(document).ajaxSend(
		                function(e, xhr, options) {
		                    xhr.setRequestHeader(header, token);
		                });
		        	$.ajax({
		                    type : "POST",
		                    url : '/ClassroomBooking/admin/user/deleteRole',
		                    data: { user: $('input#id').val(), role: $(".removeRole").attr("id") },
		                    dataType : 'html',
		                    timeout : 10000,
		                    async : false,
		                    success : function(data) {
		                        deleteR = data;
		                        $(".removeRole").parent().parent().remove();
		                    }
		                });
				
				});
	        /*]]>*/
				
				
		});
