<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/appointment.css">
<link rel="stylesheet" href="./css/layout.css">
<title>卒業生アポイントメント画面（職員）</title>
</head>
<body>
	<header >
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

		<h2 class="page-title">卒業生アポイントメント</h2>
		<c:if test="${not empty error}">
			<p class="red-msg complete-screen">${error}</p>
		</c:if>
		<div class="apo-menu-staff">

			<div class="menu-item"
				onclick="location.href='company?command=CompanyList'">
				<img src="img/company.png" alt="企業一覧(申請)"> <span>企業一覧（申請）</span>
			</div>

			<div class="menu-item"
				onclick="location.href='event?command=EventList'">
				<img src="img/history.png" alt="開催一覧/履歴"> <span>開催一覧/履歴</span>
			</div>

			<div class="menu-item"
				onclick="location.href='company?command=CompanyRegister'">
				<img src="img/company_register.png" alt="企業登録"> <span>企業登録</span>
			</div>

			<div class="menu-item"
				onclick="location.href='graduate?command=RegistEmail'">
				<img src="img/user_add.png" alt="連絡先登録"> <span>連絡先登録</span>
			</div>

			<div class="menu-item"
				onclick="location.href='answer?command=ScheduleAnswerCheck'">
				<img src="img/schedule_check.png" alt="日程回答確認"> <span>日程回答確認</span>
			</div>

		</div>

	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
	<jsp:include page="/common/flashMessage.jsp" />
</body>
</html>