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
				<div class="eventlist-title">開催一覧</div>
			</div>

			<c:if test="${empty requestScope.events}">
				<div class="errormsg">イベントは存在しません。</div>
			</c:if>

			<!-- ================= 開催一覧（開催中） ================= -->
			<table class="eventlist-table">
				<tr class="eventlist-tr">
					<th>開催日時</th>
					<th>企業名</th>
					<th></th>
					<th>開催状況</th>
					<th>参加人数</th>
				</tr>

				<c:forEach var="dto" items="${requestScope.events}">
					<c:if test="${dto.event.eventProgress == 'ONGOING'}">
						<tr class="eventlist-tr">
							<td>${dto.event.eventStartTime}</td>
							<td>${dto.event.company.companyName}</td>
							<td><a
								href="event?command=EventDetail&eventId=${dto.event.eventId}">
									開催詳細 </a></td>
							<td>${dto.event.eventProgress.label}</td>
							<td>${dto.joinStudentCount}</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>

			<!-- ================= 開催履歴 ================= -->
			<div class="eventlist">
				<div class="eventlist-title">開催履歴</div>
			</div>

			<table class="eventlist-table">
				<tr class="eventlist-tr">
					<th>開催日時</th>
					<th>企業名</th>
					<th></th>
					<th>開催状況</th>
					<th>参加人数</th>
				</tr>

				<c:forEach var="dto" items="${requestScope.events}">
					<c:if
						test="${dto.event.eventProgress == 'FINISHED' || dto.event.eventProgress == 'CANCELED'}">
						<tr class="eventlist-tr">
							<td>${dto.event.eventStartTime}</td>
							<td>${dto.event.company.companyName}</td>
							<td><a
								href="event?command=EventDetail&eventId=${dto.event.eventId}">
									開催詳細 </a></td>

							<c:choose>
								<c:when test="${dto.event.eventProgress == 'CANCELED'}">
									<td class="event-cancel">${dto.event.eventProgress.label}
									</td>
								</c:when>
								<c:otherwise>
									<td>${dto.event.eventProgress.label}</td>
								</c:otherwise>
							</c:choose>

							<td>${dto.joinStudentCount}</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>

		</main>

	</div>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>