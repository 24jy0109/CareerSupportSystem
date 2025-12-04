<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h2>企業登録（確認）</h2>
	<form action="company" method="POST">
		<input type="hidden" name="command" value="CompanyRegisterConfirm">
		 <input type="text" name="companyName" placeholder="企業名で検索"value="${param.companyName}">
		<button type="submit">登録</button>
	</form>
	

</body>
</html>