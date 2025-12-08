<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>アポイントメント画面</title>
</head>
<body>
	<h1>staffアポイントメント画面</h1>
	<a href="company?command=CompanyList">企業一覧</a>
	<br>
	<a href="company?command=CompanyRegister">企業名登録</a>
	<br>
	<form action="graduate?command=RegistEmail" method="post">
	<input type="submit" value="連絡先登録">
	</form>
	
</body>
</html>