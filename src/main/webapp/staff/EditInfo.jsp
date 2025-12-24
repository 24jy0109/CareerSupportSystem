<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>情報編集画面（職員）</title>
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
		<div class="wrapper">
			<main class="content">
				<div class="request-top">
					<div class="company">
						<div class="field-name"></div>
						<div class="company-name">
<!--							<p>${companyDTO}</p>-->
<!--							<p>${companyDTO.company}</p>-->
							<p>${companyDTO.company.companyName}</p>
							<div>
								<a
									href="company?command=CompanyRegisterNext&companyId=${companyDTO.company.companyId}">企業名編集</a>
							</div>
						</div>

					</div>

					<div class="event">
						<div class="event-info">12月16日(水)17時～18時 172教室(本館７階)</div>
					</div>
				</div>

				<p class="errormsg">申請者がいません</p>

				<div class="bottom-btn-left">
					<!-- 戻るボタン　左側 -->
					<button type="button" onclick="location.href='./CompanyList.html'">企業一覧に戻る</button>
				</div>

			</main>
		</div>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>