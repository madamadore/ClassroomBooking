<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="request" 
	class="it.tecnosphera.booking.classroom.model.Aula">
	<jsp:setProperty name="aula" property="*"/>
</jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User</title>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
    <div class="content">
	<div>
	<form action="create" method="POST">
		<div>
		   <label for="id">ID:</label>
		   <input type="text" readonly id="id" name="id" value="${aula.id}"/>
		</div>
		<div>
		   <label for="name">Nome:</label>
		   <input type="text" id="name" name="name" value="${aula.name}" />
		</div>
		<div>
		   <label for="description">Descrizione:</label>
		   <textarea id="description" name="description" cols="50" rows="5">${aula.description}</textarea>
		</div>
		<div>
		<button class = "btn btn-default" type = "submit">Salva</button>
		</div>
	</form>	
    </div>
</body>
</html>