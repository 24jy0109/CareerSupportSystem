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

			<c:if test="${not empty error}">
				<p style="color: red">${error}</p>
			</c:if>

			<input type="hidden" name="command" value="CompanyRegisterNext">
			<input type="text" name="companyName" placeholder="企業名入力"
				value="${param.companyName}">
			<button type="submit">確認</button>
		</form>


	</main>

</body>
</html>