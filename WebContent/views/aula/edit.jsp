<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="aula" scope="request" class="it.tecnosphera.booking.classroom.model.Aula">
	<jsp:setProperty name="aula" property="*"/>
</jsp:useBean>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Aula</title>

<link href=”bootstrap/css/bootstrap.min.css” rel=”stylesheet” type=”text/css” />
<script type=”text/javascript” src=”bootstrap/js/bootstrap.min.js”></script>

</head>
<body>
    <div class="content">
	<form action="create" method="POST">
		<div>
		   <label for="id">ID:</label>
		   <input type="text" readonly id="id" name="id" value="<%=aula.getId()%>"/>
		</div>
		<div>
		   <label for="name">Nome:</label>
		   <input type="text" id="name" name="name" value="<%=aula.getName()%>" />
		</div>
		<div>
		   <label for="description">Descrizione:</label>
		   <textarea id="description" name="description" 
		   	cols="50" rows="5"><%=aula.getDescription()%></textarea>
		</div>
		<div>
			<button type=”submit” class=”btn btn-default”>Salva</button>
		</div>
	</form>	
    </div>
</body>
</html>