<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>連絡先登録完了画面（在校生/職員）</title>
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
		<div class="complete-screen">
			<p class="complete-msg">登録完了しました</p>
			<p>
				<img src="./img/complete_regist_job.png">
			</p>
			<p>メールを送信しました。</p>
			<p>変更したい場合はメニュー画面内の連絡先登録より変更してください。</p>
		</div>
		<div class="bottom-btn-left">
			<button type="button"
				onclick="location.href='graduate?command=AppointMenu'">メニュー画面に戻る</button>
		</div>
		</div>

	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>