<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/appointment.css">
<link rel="stylesheet" href="./css/layout.css">
<title>アポイントメント画面</title>
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
	
<!--	<h1>アポイントメント画面</h1>-->
<!--	<a href="company?command=CompanyList">企業一覧</a>-->
	
	<main>

        <h2 class="page-title">卒業生アポイントメント</h2>
        <div>
        <div class="apo-menu">

            <div class="menu-item" onclick="location.href='company?command=CompanyList'">
                <img src="img/company.png" alt="企業一覧">
                <span>企業一覧</span>
            </div>

            <div class="menu-item" onclick="location.href='event?command=JoinHistory'">
                <img src="img/history.png" alt="参加一覧/履歴">
                <span>参加一覧/履歴</span>
            </div>

            <div class="menu-item" onclick="location.href='event?command=EventList'">
                <img src="img/event.png" alt="開催一覧">
                <span>開催一覧</span>
            </div>

			<div class="menu-item" onclick="location.href='graduate?command=RegistEmail'">
				<img src="img/user_add.png" alt="連絡先登録"> <span>連絡先登録</span>
			</div>

        </div>
        </div>
    </main>

    <footer>
		<p><small>&copy; 2024 Example Inc.</small></p>
	</footer>
</body>
</html>