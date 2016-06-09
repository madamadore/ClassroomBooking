<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script language="Javascript" type="text/javascript">
<!-- 
function testpass(modulo){
  // Verifico che il campo password sia valorizzato in caso contrario
  // avverto dell'errore tramite un Alert
  if (modulo.password.value == ""){
    alert("Errore: inserire una password!")
    modulo.password.focus()
    return false
  }
  // Verifico che le due password siano uguali, in caso contrario avverto
  // dell'errore con un Alert
  if (modulo.password.value != modulo.password_2.value) {
    alert("La password inserita non coincide con la prima!")
    modulo.password.focus()
    modulo.password.select()
    return false
  }
  return true
}
-->
</script>
</head>
<body>
<form method="post" name="modulo" onsubmit="return testpass(this)">
Username scelto:<br />
<input type="text" name="username" size="30"/><br />
Digita qui la password:<br />
<input type="password" name="password" size="30"/><br />
Conferma la password:<br />
<input type="password" name="password_2" size="30" /><br />
<input type="submit" value="Invia" />
</form>

</body>
</html>