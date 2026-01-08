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

			<div class="header-user">ようこそ${name}さん</div>
		</div>
	</header>


	<main>
		<form action="company" method="post">
			<input type="hidden" name="companyId" value="${companyId}">
			<input type="hidden" name="command" value="CompanyRegisterNext">
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
							<td> <input type="text"
								name="companyName" class="company-input" value="${companyName}"
								placeholder="${empty companyName ? '企業名を入力' : ''}"></td>
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