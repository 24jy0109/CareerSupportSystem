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
<title>開催一覧</title>
<!--<style>-->
<!--table {-->
<!--	border-collapse: collapse;-->
<!--	width: 90%;-->
<!--	margin: 20px auto;-->
<!--}-->

<!--th, td {-->
<!--	border: 1px solid #333;-->
<!--	padding: 8px;-->
<!--	text-align: center;-->
<!--}-->

<!--th {-->
<!--	background-color: #eee;-->
<!--}-->

<!--h2 {-->
<!--	text-align: center;-->
<!--}-->
<!--</style>-->
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
		<div>開催詳細から参加登録をしてください。</div>

		<c:choose>
			<c:when test="${empty events}">
				<p style="text-align: center;">現在参加可能なイベントはありません。</p>
			</c:when>

			<c:otherwise>
				<table>
					<tr>
						<th>企業名</th>
						<th>残り定員</th>
						<th>残り定員計算</th>
						<th>詳細</th>
					</tr>

					<c:forEach var="dto" items="${events}">
						<tr>
							<td>${dto.event.company.companyName}</td>

							<td>${dto.event.eventCapacity - dto.joinStudentCount}</td>
							<td>${dto.event.eventCapacity} - ${dto.joinStudentCount}</td>

							<td><a
								href="event?command=EventDetail&eventId=${dto.event.eventId}">
									開催詳細 </a></td>
						</tr>
					</c:forEach>

				</table>
			</c:otherwise>
		</c:choose>

	</main>
</body>
</html>