<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="request" 
	class="it.tecnosphera.booking.classroom.model.User">
	<jsp:setProperty name="user" property="*"/>
</jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User</title>
<link href=”bootstrap/css/bootstrap.min.css” rel=”stylesheet” type=”text/css” />
<script type=”text/javascript” src=”bootstrap/js/bootstrap.min.js”></script>
</head>

<body>
    <div class="content">
	<form action="create" method="POST">
		<div>
		   <label for="id">ID:</label>
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
		</div>				
		<div>
		   <input type="submit" value="Salva" />
		</div>
	</form>	
    </div>
</body>
</html>