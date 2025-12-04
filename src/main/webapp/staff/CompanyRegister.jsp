<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<main>
	<h1>企業登録</h1>
	<form action="company" method="post">
	<input type="hidden" name="command" value="CompanyRegisterConfirm">
	<input type="text" name="companyName" placeholder="企業目入力">
	<button type="submit">確認</button>
	</form>
	
	
	</main>

</body>
</html>