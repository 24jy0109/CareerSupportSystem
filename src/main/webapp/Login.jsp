<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/appointment.css">
<link rel="stylesheet" href="./css/layout.css">
<title>Login</title>
</head>
<body>
	<header>
		<div class="head-part">
			<div id="h-left">
				<img src="img/rogo.png" alt="アイコン">
			</div>

			<div class="header-title"
				onclick="location.href='mypage?command=AppointmentMenu'">
				<div class="title-jp">就活サポート</div>
				<div class="title-en">Career Support</div>
			</div>

			<!--			<div class="header-user">ようこそ${name}さん</div>-->
		</div>
	</header>
	<main>
		<div class="page-title">ログイン</div>
		<c:if test="${not empty error}">
			<p class="red-msg">${error}</p>
		</c:if>
		<div class="login-center">
<!--			<div class="login-row"l>-->
				<div class="login-desplay">
<!--					<div>学生はこちら</div>-->
					<form action="login" method="POST">
						<input type="hidden" name="command" value="Login">
						<button type="submit" class="login-button">Googleでログイン</button>
					</form>
				</div>
<!--				<div class="login-desplay">-->
			<!--					<div>職員はこちら</div>-->

				</div>
		<!--			</div>-->
		</div>

		<form action="login" method="POST">
			<input type="hidden" name="command" value="TestStaffLogin">
			<button type="submit" class="login-button">TestStaffLogin</button>
		</form>
	</main>
	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>