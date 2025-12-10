<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>確認画面</title>
</head>
<body>

	<h2>企業登録（確認）</h2>
	<form action="company" method="POST">
		<p>企業名：${param.companyName}</p>
		<input type="hidden" name="companyName" value="${param.companyName}">
		<button type="submit" name="command" value="CompanyRegisterBack">戻る</button>
		<button type="submit" name="command" value="CompanyRegisterConfirm">登録</button>
	</form>


</body>
</html>