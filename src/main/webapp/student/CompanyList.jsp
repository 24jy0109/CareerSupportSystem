<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>企業リスト</title>
<style>
body {
	font-family: Arial, sans-serif;
}

table {
	border-collapse: collapse;
	width: 80%;
	margin: 20px auto;
}

th, td {
	border: 1px solid #333;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f0f0f0;
}

.center {
	text-align: center;
}
</style>
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
		<h2 class="center">企業リスト</h2>

		<!-- ▼検索フォーム -->
		<form action="company" method="GET">
			<input type="hidden" name="command" value="CompanyList"> <input
				type="text" name="companyName" placeholder="企業名で検索"
				value="${param.companyName}">
			<button type="submit">検索</button>
		</form>

		<table>
			<thead>
				<tr>
					<th>企業ID</th>
					<th>企業名</th>
					<th>イベント状況</th>
					<th>リクエスト状況</th>
					<th>卒業生人数</th>
					<th>申請/詳細</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="company" items="${companies}">
					<tr>
						<td>${company.company.companyId}</td>
						<td>${company.company.companyName}</td>
						<td>${company.eventProgress}</td>
						<td>${company.isRequest}</td>
						<td>${company.graduateCount}</td>
						<td><a
							href="company?command=CompanyDetail&companyId=${company.company.companyId}">申請/詳細</a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</main>
	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>
