<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>開催一覧/履歴（職員）</title>
<style>
table {
	border-collapse: collapse;
	width: 90%;
	margin: 20px auto;
}

th, td {
	border: 1px solid #333;
	padding: 8px;
	text-align: center;
}

th {
	background-color: #eee;
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

	<h2 style="text-align: center;">イベント一覧</h2>

	<c:choose>
		<c:when test="${empty requestScope.events}">
			<p style="text-align: center;">イベントは存在しません。</p>
		</c:when>

		<c:otherwise>
			<table>
				<tr>
					<th>企業名</th>
					<th>開催詳細</th>
					<th>進行状況</th>
					<th>参加学生数</th>
				</tr>

				<c:forEach var="dto" items="${requestScope.events}">
					<tr>
						<td>${dto.event.company.companyName}</td>
						<td><a href="event?command=EventDetail&eventId=${dto.event.eventId}">開催詳細</a></td>
						<td>${dto.event.eventProgress}</td>
						<td>${dto.joinStudentCount}</td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>


	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>