<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="request" 
	class="it.tecnosphera.booking.classroom.model.User">
	<jsp:setProperty name="user" property="*"/>
</jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script language="Javascript" type="text/javascript">

function testpass(User){
   if (User.password.value == ""){
    alert("Errore: inserire una password!")
    User.password.focus()
    return false
  }

  if (User.password.value != User.conferma_password.value) {
    alert("La password inserita non coincide con la prima!")
    User.password.focus()
    User.password.select()
    return false
  }
  return true
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User</title>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>

<body>
    <div class="content">
	<form action="view.jsp" method="POST" name="User" onsubmit="return testpass(this)">
		<div hidden>
		   <input type="text" readonly id="id" name="id" value="<%=user.getId()%>"/>
		</div>
		<div>
		   <label for="name">Nome:</label>
		   <input type="text" id="name" name="name" value="<%=user.getName()%>" />
		</div>
		<div>
		   <label for="name">Cognome:</label>
		   <input type="text" id="cognome" name="cognome" value="<%=user.getCognome()%>" />
		</div>
		<div>
		   <label for="name">Indirizzo email:</label>
		   <input type="text" id="email" name="email" value="<%=user.getEmail()%>" />
		</div>		
		<div>
		   <label for="name">Password:</label>
		   <input type="password" id="password" name="password" value="<%=user.getPassword()%>" />
		   <input type="password" id="conferma_password" name="conferma_password" value="<%=user.getPassword()%>" />
		</div>
						
		<div>
		   <input type="submit" value="Salva" />
		   <input type="submit" value="Edit" />
		</div>
	</form>	
    </div>
</body>
</html>