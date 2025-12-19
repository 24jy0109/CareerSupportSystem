<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>開催詳細(職員)</title>

<style>
table {
	border-collapse: collapse;
	width: 80%;
	margin: 20px auto;
}

th, td {
	border: 1px solid #333;
	padding: 8px;
}

th {
	background-color: #eee;
	width: 30%;
}

h2 {
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
		<h2>イベント詳細</h2>

		<c:choose>
			<c:when test="${empty event}">
				<p style="text-align: center;">イベント情報が存在しません。</p>
			</c:when>

			<c:otherwise>

				<!-- 基本情報 -->
				<table>
					<tr>
						<th>企業名</th>
						<td>${event.event.company.companyName}</td>
					</tr>

					<tr>
						<th>担当職員</th>
						<td>${event.event.staff.staffName}<br>
							(${event.event.staff.staffEmail})
						</td>
					</tr>

					<tr>
						<th>開催日時</th>
						<td>${event.event.eventStartTime} ～
							${event.event.eventEndTime}</td>
					</tr>

					<tr>
						<th>場所</th>
						<td>${event.event.eventPlace}</td>
					</tr>

					<tr>
						<th>定員</th>
						<td>${event.event.eventCapacity}名</td>
					</tr>

					<tr>
						<th>現在の参加学生数</th>
						<td>${event.joinStudentCount}名</td>
					</tr>
				</table>

				<!-- 参加卒業生 -->
				<h2>参加卒業生一覧</h2>

				<c:choose>
					<c:when test="${empty event.graduates}">
						<p style="text-align: center;">参加卒業生はいません。</p>
					</c:when>

					<c:otherwise>
						<table>
							<tr>
								<th>卒業年</th>
								<th>学科名</th>
								<th>職種</th>
								<th>氏名</th>
								<th>その他</th>
							</tr>

							<c:forEach var="g" items="${event.graduates}">
								<tr>
									<td><c:if test="${not empty g.graduateStudentNumber}">
											<c:set var="enterYear2"
												value="${fn:substring(g.graduateStudentNumber, 0, 2)}" />
											<c:set var="graduateYear"
												value="${enterYear2 + g.course.courseTerm}" />
							        ${graduateYear}年卒
							    </c:if></td>
									<td>${g.course.courseName}</td>
									<td>${g.graduateJobCategory}</td>
									<td>${g.graduateName}</td>
									<td>${g.otherInfo}</td>
								</tr>
							</c:forEach>
						</table>
					</c:otherwise>
				</c:choose>

			</c:otherwise>
		</c:choose>
		<a href="join_student?command=JoinStudentList&eventId=${event.event.eventId}">在校生参加者確認確認</a>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>