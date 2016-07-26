/*
 * Scripts generici 
 */
$(document)
		.ready(
				function() {
					$.validator.addMethod("emailTecno",
							function(value, element) {
								return (!value.indexOf('@') > -1);
							}, "Inserire solo nome Utente");

					$("#selectRuoli").change(function() {
						$("#addRuolo").removeClass('hidden');
					});

					/* <![CDATA[ */
					$("#addRuolo")
							.click(
									function() {
										var ruolo = $(
												"#selectRuoli option:selected")
												.val();
										var descRuolo = $(
												"#selectRuoli option:selected")
												.text();
										var rowCount = $("#tableRoles tr").length;

										var input1 = '<input type="hidden"' /* [[th:field="*{userRole[__${']] */
												+ rowCount
												+ /* [['}__].user"]] */' value="'
												+ $("#id").val() + '" />';
										var input2 = '<input type="hidden"' /* [[th:field="*{userRole[__${']] */
												+ rowCount
												+ /* [['}__].role"]] */' value="'
												+ ruolo + '" />';
										var input3 = '<input type="hidden"' /* [[th:field="*{userRole[__${']] */
												+ rowCount
												+ /* [['}__].descrizione"]] */' value="'
												+ descRuolo + '" />';

										var context = '<tr><td>&nbsp;</td><td>'
												+ ruolo
												+ '</td>'
												+ '<td>'
												+ descRuolo
												+ '</td>'
												+ '<td><a class="removeRole" id=' /* [[th:field="*{userRole[__${']] */
												+ rowCount
												+ /* [['}__].userRoleId}"]] */' name="removeRole" data-placement="top" title="Elimina Ruolo" href="javascript:void(0);">'
												+ '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>'
												+ input1 + input2 + input3
												+ '</td></tr>';

										$("#rowRuoli").append(context);

										$(".removeRole").click(function() {
											$(this).parent().parent().remove();
										});
									});

					$(".removeRole").click(function() {
						$(this).parent().parent().remove();
					});

					/* ]]> */

				});

// al caricamento della pagina questa parte una chiamata, va evitata
var fakeCall = true;
function enablePasswordConfirmation() {

	if (fakeCall) {
		fakeCall = false;
		return;
	}
	console.log("Conferma abilitata");
	var regole = {
		emailTecno : {
			emailTecno : true
		}
	};
	regole.conf_password = {
		equalTo : "#password"
	}
	$("#form").validate({
		rules : regole
	});
}
