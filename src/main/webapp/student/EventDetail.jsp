<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>開催詳細</title>
<style>
table {
	border-collapse: collapse;
	width: 90%;
	margin: 20px auto;
}

th, td {
	border: 1px solid #333;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #eee;
	width: 30%;
}

h2, h3 {
	text-align: center;
}

.join-status {
	text-align: center;
	font-weight: bold;
	margin-top: 20px;
}
</style>
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
		<table>
			<tr>
				<th>企業名</th>
				<td>${event.event.company.companyName}</td>
			</tr>

			<tr>
				<th>担当職員名</th>
				<td>${event.event.staff.staffName}</td>
			</tr>

			<tr>
				<th>担当職員連絡先</th>
				<td>${event.event.staff.staffEmail}</td>
			</tr>

			<tr>
				<th>開催日時</th>
				<td>${event.event.eventStartTime}～${event.event.eventEndTime}</td>
			</tr>

			<tr>
				<th>開催場所</th>
				<td>${event.event.eventPlace}</td>
			</tr>

			<tr>
				<th>定員数</th>
				<td>${event.event.eventCapacity}名</td>
			</tr>

			<tr>
				<th>現在の参加者数</th>
				<td>${event.joinStudentCount}名</td>
			</tr>

			<tr>
				<th>その他</th>
				<td>${event.event.eventOtherInfo}</td>
			</tr>
		</table>

		<!-- 参加卒業生 -->
		<h3>参加卒業生情報</h3>

		<c:choose>
			<c:when test="${empty event.graduates}">
				<p style="text-align: center;">参加予定の卒業生はいません。</p>
			</c:when>

			<c:otherwise>
				<table>
					<tr>
						<th>名前</th>
						<th>卒業年</th>
						<th>学科</th>
						<th>職種</th>
					</tr>

					<c:forEach var="g" items="${event.graduates}">
						<tr>
							<td>${g.graduateName}</td>
							<td><c:if test="${not empty g.graduateStudentNumber}">
									<c:set var="enterYear2"
										value="${fn:substring(g.graduateStudentNumber, 0, 2)}" />
									<c:set var="graduateYear"
										value="${enterYear2 + g.course.courseTerm}" />
									${graduateYear}年卒
								</c:if></td>
							<td>${g.course.courseName}</td>
							<td>${g.graduateJobCategory}</td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>

		<%-- 参加可否表示 --%>
		<div class="join-status">
			<c:choose>

				<%-- 未回答 --%>
				<c:when test="${event.joinAvailability == null}">

					<c:choose>
						<%-- 定員未達 --%>
						<c:when
							test="${event.joinStudentCount < event.event.eventCapacity}">
							<a href="event?command=EventJoin&eventId=${event.event.eventId}">参加</a>
					&nbsp;|&nbsp;
					<a href="event?command=EventNotJoin&eventId=${event.event.eventId}">不参加</a>
						</c:when>

						<%-- 定員到達 --%>
						<c:otherwise>
					満員です
				</c:otherwise>
					</c:choose>

				</c:when>

				<%-- 参加 --%>
				<c:when test="${event.joinAvailability}">
			参加予定
		</c:when>

				<%-- 不参加 --%>
				<c:otherwise>
			不参加
		</c:otherwise>

			</c:choose>
		</div>
	</main>
</body>
</html>
