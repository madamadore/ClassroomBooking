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
										var name = $(
												"#selectRuoli option:selected")
												.val();
										var giaInserito = $("#rowRuoli input[value="
												+ name + "]");
										if (giaInserito.length > 0) {
											return;
										}

										var description = $(
												"#selectRuoli option:selected")
												.text();
										var rowCount = $("#tableRoles tr").length;

										var input1 = '<input value="' + name
												+ '" id="roles' + rowCount
												+ '.name" name="roles['
												+ rowCount
												+ '].name" type="hidden">';

										var context = '<tr><td>&nbsp;</td><td>'
												+ name
												+ '</td>'
												+ '<td>'
												+ description
												+ '</td>'
												+ '<td><a class="removeRole" id='
												+ rowCount
												+ ' name="removeRole" data-placement="top" title="Elimina Ruolo" href="javascript:void(0);">'
												+ '<span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a></td>'
												+ input1 + '</tr>';

										$("#rowRuoli").append(context);

										$(".removeRole").click(function() {
											$(this).parent().parent().remove();
										});
									});

					$(".removeRole").click(function() {
						$(this).parent().parent().remove();
					});


				});

// al caricamento della pagina parte una chiamata, questa va evitata
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
