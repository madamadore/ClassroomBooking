<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="user" scope="request" 
	class="it.technosphera.booking.classroom.model.User">
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
	<div>
	   ID: <%=user.getId()%>
	</div>
	<div>
	   Nome: <%=user.getName()%>
	</div>
		<div>
	   Cognome: <%=user.getCognome()%>
	</div>
		<div>
	   Email: <%=user.getEmail()%>
	</div>

    </div>
</body>
</html>