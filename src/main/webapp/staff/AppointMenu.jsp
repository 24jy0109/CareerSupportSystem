<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="../css/header.css">
<link rel="stylesheet" href="../css/appointment.css">
<link rel="stylesheet" href="../css/layout.css">
<title>卒業生アポイントメント画面（職員）</title>
</head>
<body>
	<header>
		<div class="head-part">
			<div id="h-left">
				<img src="../img/rogo.png" alt="アイコン">
			</div>

			<div class="header-title"
				onclick="location.href='./AppointMenu.html'">
				<div class="title-jp">就活サポート</div>
				<div class="title-en">Career Support</div>
			</div>

			<div class="header-user">ようこそ 24jy0119 さん</div>
		</div>
	</header>
	<main>

		<h2 class="page-title">卒業生アポイントメント</h2>

		<div class="apo-menu-staff">

			<div class="menu-item" onclick="location.href='company?command=CompanyList'">
				<img src="../img/company.png" alt="企業一覧(申請)"> <span>企業一覧（申請）</span>
			</div>

			<div class="menu-item" onclick="location.href='./EventList.html'">
				<img src="../img/history.png" alt="開催一覧/履歴"> <span>開催一覧/履歴</span>
			</div>

			<div class="menu-item"
				onclick="location.href='./CompanyRegister.html'">
				<img src="../img/company_register.png" alt="企業登録"> <span>企業登録</span>
			</div>

			<div class="menu-item" onclick="location.href='./GraduateInput.html'">
				<img src="../img/user_add.png" alt="連絡先登録"> <span>連絡先登録</span>
			</div>

			<div class="menu-item"
				onclick="location.href='./ScheduleAnswerCheck.html'">
				<img src="../img/schedule_check.png" alt="日程回答確認"> <span>日程回答確認</span>
			</div>

		</div>

	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>















	<a href="company?command=CompanyList">企業一覧</a>
	<br>
	<a href="company?command=CompanyRegister">企業名登録</a>
	<br>
	<a href="graduate?command=RegistEmail">連絡先登録</a>
</body>
</html>