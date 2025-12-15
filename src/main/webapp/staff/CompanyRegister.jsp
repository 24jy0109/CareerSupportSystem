<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<!--<link rel="stylesheet" href="./css/appointment.css">-->
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>企業登録画面（職員）</title>
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

			<div class="header-user">ようこそ 24jy0119 さん</div>
		</div>
	</header>
	

	<main>
		<form action="company" method="post">
			<div class="left-screen">
				<span class="leftscreen-title">企業登録</span>
					<c:if test="${not empty error}">
						<p style="color: red">${error}</p>
					</c:if>
				<div class="company-register-flame">
					<table class="registscreen-table">
						<tr>
							<td>
								<div>企業名</div>
							</td>
							<td>
							    <input type="hidden" name="command"value="CompanyRegisterNext"> 
							    <input type="text"name="companyName" placeholder="企業名入力"value="${param.companyName}" class="company-input">
							</td>
						</tr>
					</table>
				</div>
			</div>

			<div class="bottom-btn-right buttom-btn-narrow">
				<button type="submit">確認</button>
			</div>
		</form>

	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>






</body>
</html>