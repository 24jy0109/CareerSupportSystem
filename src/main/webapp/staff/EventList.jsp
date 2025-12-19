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

	<div class="wrapper">
		<main class="content">
			<div class="eventlist">
				<div class="eventlist-title">開催一覧・履歴</div>
			</div>

			<c:choose>
				<c:when test="${empty requestScope.events}">
					<div class="errormsg">
						イベントは存在しません。
						<div>
				</c:when>
				<c:otherwise>
					<table class="eventlist-table">
						<tr class="eventlist-tr">
							<th>企業名</th>
							<th></th>
							<th>開催状況</th>
							<th>参加人数</th>
						</tr>

						<c:forEach var="dto" items="${requestScope.events}">
							<tr class="eventlist-tr">
								<td>${dto.event.company.companyName}</td>
								<td><a
									href="event?command=EventDetail&eventId=${dto.event.eventId}">開催詳細</a></td>
								<td>${dto.event.eventProgress}</td>
								<td>${dto.joinStudentCount}</td>
							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>
		</main>
	</div>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>