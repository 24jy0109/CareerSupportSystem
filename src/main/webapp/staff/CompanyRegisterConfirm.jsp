<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>企業登録確認画面（職員）</title>
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

	<!--	<h2>企業登録（確認）</h2>-->
	<!--	<form action="company" method="POST">-->
	<!--		<p>企業名：${param.companyName}</p>-->
	<!--		<input type="hidden" name="companyName" value="${param.companyName}">-->
	<!--		<button type="submit" name="command" value="CompanyRegisterBack">戻る</button>-->
	<!--		<button type="submit" name="command" value="CompanyRegisterConfirm">登録</button>-->
	<!--	</form>-->


	<main>
		<form action="company" method="POST">
			<input type="hidden" name="companyId" value="${companyId}">
			<div class="left-screen">
				<span class="leftscreen-title">企業登録</span>
				<div class="company-register-flame">
					<table class="registscreen-table">
						<tr>
							<td>
								<div>企業名</div>
							</td>
							<td>
								<div>${companyName}</div> <input type="hidden"
								name="companyName" value="${companyName}">
							</td>
						</tr>
					</table>

				</div>
			</div>

			<div class="bottom-btn-split">
				<button type="submit" name="command" value="CompanyRegisterBack">戻る</button>
				<button type="submit" name="command" value="CompanyRegisterConfirm">登録</button>
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