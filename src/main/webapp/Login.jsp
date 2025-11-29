<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
	<form action="login" method="POST">
		<input type="hidden" name="command" value="Login">
		<button type="submit">Googleでログイン</button>
	</form>
	
	<form action="login" method="POST">
		<input type="hidden" name="command" value="TestStaffLogin">
		<button type="submit">TestStaffLogin</button>
	</form>
</body>
</html>