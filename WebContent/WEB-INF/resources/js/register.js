$.validator.addMethod("emailTecno", function(value, element) {
	return (!value.indexOf('@') > -1);
}, "Inserire solo nome Utente");

$("#form").validate({
	rules : {
		conf_password : {
			equalTo : "#password"
		},

		email : {
			required : true,
			remote : 'uer/ajax/emailValidation'
		}
	},
	messages : {
		email : {
			required : "E' necessario inserire un'email.",
			remote : "Questa email è già stata usata per la registrazione."
		}
	}
});