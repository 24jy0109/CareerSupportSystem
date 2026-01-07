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
<title>参加一覧/履歴</title>
</head>
<body>
	<header class="head-part">
		<div id="h-left">
			<img src="./img/rogo.png" alt="アイコン">
		</div>

		<div class="header-title"
			onclick="location.href='mypage?command=AppointmentMenu'">
			<div class="title-jp">就活サポート</div>
			<div class="title-en">Career Support</div>
		</div>

		<div class="header-user">ようこそ${name}さん</div>
	</header>

	<main>
		<div class="eventlist">
			<div class="eventlist-title">参加一覧</div>
		</div>

		<table>
			<!--			<tr>-->
			<!--				<th>企業名</th>-->
			<!--				<th>イベントID</th>-->
			<!--				<th>状態</th>-->
			<!--			</tr>-->

			<c:forEach var="dto" items="${events}">
				<c:if test="${dto.event.eventProgress == \"ONGOING\"}">
					<tr>
						<td>${dto.event.company.companyName}</td>
						<td><a
							href="event?command=EventDetail&eventId=${dto.event.eventId}">
								開催詳細 </a></td>
						<td><c:choose>
								<c:when test="${dto.joinAvailability}">
							参加
						</c:when>
								<c:otherwise>
							不参加
						</c:otherwise>
							</c:choose></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>



		<div class="eventlist">
			<div class="eventlist-title">参加履歴</div>
		</div>
		<table>
			<!--			<tr>-->
			<!--				<th>企業名</th>-->
			<!--				<th>イベントID</th>-->
			<!--				<th>結果</th>-->
			<!--			</tr>-->



			<c:forEach var="dto" items="${events}">
				<c:if test="${empty dto.event}">
					<div class="errormsg">履歴がありません。</div>
				</c:if>

				<c:if
					test="${dto.event.eventProgress == \"FINISHED\" || dto.event.eventProgress == \"CANCELED\"}">
					<tr>
						<td>${dto.event.company.companyName}</td>
						<td><a
							href="event?command=EventDetail&eventId=${dto.event.eventId}">
								開催詳細 </a></td>
						<td><c:choose>
								<c:when test="${dto.event.eventProgress == \"CANCELED\"}">
							中止
						</c:when>
								<c:when test="${dto.joinAvailability}">
							参加済
						</c:when>
								<c:otherwise>
							不参加
						</c:otherwise>
							</c:choose></td>
					</tr>

				</c:if>

			</c:forEach>
		</table>

	</main>
</body>
</html>