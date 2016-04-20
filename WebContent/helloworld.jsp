<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	Ciao, io sono la pagina <b>Hello world</b>
	
	<%
	String test = request.getParameter("test");
	if ( test != null) {
		out.write("<br />");
		out.write("E stato specificato il parametro Test " + 
				"e vale: " + test);
	} 
	%> 
	
</body>
</html>