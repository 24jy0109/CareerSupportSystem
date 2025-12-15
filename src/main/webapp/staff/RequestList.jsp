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
<title>申請者一覧（職員）</title>
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

<!--		<h2>企業リクエスト一覧</h2>-->

<!--		<c:if test="${empty requests}">-->
<!--			<p>リクエストした学生はいません。</p>-->
<!--		</c:if>-->

<!--		<c:if test="${not empty requests}">-->
<!--			 最初の Request から会社情報を取得 -->
<!--			<c:set var="company" value="${requests[0].company}" />-->

<!--			<h3>企業名：${company.companyName}</h3>-->

<!--			 progress=2 のイベント一覧 -->
<!--			<h4>イベント情報</h4>-->

<!--			<c:if test="${empty company.events}">-->
<!--				<p>対象イベントがありません。</p>-->
<!--			</c:if>-->

<!--			<c:forEach var="ev" items="${company.events}">-->
<!--				<div style="margin-bottom: 12px;">-->
<!--					<b>開始 :</b> ${ev.eventStartTime}<br> <b>終了 :</b>-->
<!--					${ev.eventEndTime}<br> <b>場所 :</b> ${ev.eventPlace}-->
<!--				</div>-->
<!--				<hr>-->
<!--			</c:forEach>-->

<!--			 学生リクエスト一覧 -->
<!--			<h3>リクエストした学生一覧</h3>-->

<!--			<table border="1" cellpadding="8">-->
<!--				<tr>-->
<!--					<th>学生番号</th>-->
<!--					<th>氏名</th>-->
<!--					<th>コース名</th>-->
<!--					<th>リクエスト日時</th>-->
<!--				</tr>-->

<!--				<c:forEach var="req" items="${requests}">-->
<!--					<tr>-->
<!--						<td>${req.student.studentNumber}</td>-->
<!--						<td>${req.student.studentName}</td>-->
<!--						<td>${req.student.course.courseName}</td>-->
<!--						<td>${req.requestTime}</td>-->
<!--					</tr>-->
<!--				</c:forEach>-->

<!--			</table>-->
<!--		</c:if>-->
<!--	</main>-->

	<div class="wrapper">
		<main class="content">
			<div class="request-top">
				<c:set var="company" value="${requests[0].company}" />

				<div class="company">
					<div class="field-name">企業名</div>
					<div class="company-name">${company.companyName}</div>
				</div>

				<div class="event">
					<div class="event-info">
						<c:if test="${empty company.events}">
							<p>対象イベントがありません。</p>
						</c:if>

						<c:forEach var="ev" items="${company.events}">
							<div class="event-frex">
								<div>${ev.eventStartTime}～${ev.eventEndTime}</div>
								<div>${ev.eventPlace}</div>
							</div>
						</c:forEach>
					</div>
				</div>


			</div>
			<c:if test="${empty requests}">
				<h3>企業名：${showCompany.company.companyName}</h3>
				<p 　class="errormsg">リクエストした学生はいません。</p>
			</c:if>
			<c:if test="${not empty requests}">

				<table class="request-table">
					<tr class="request-info">
						<th class="request-h">氏名</th>
						<th class="request-h">学科</th>
						<th class="request-h">学籍番号</th>
						<th class="request-h">リクエスト日時</th>
					</tr>

					<c:forEach var="req" items="${requests}">
						<tr class="request-info">
							<td>${req.student.studentName}</td>
							<td>${req.student.course.courseName}</td>
							<td>${req.student.studentNumber}</td>
							<td>${req.requestTime}</td>
						</tr>
					</c:forEach>

				</table>
			</c:if>

			<div class="bottom-btn-left">
				<!-- 戻るボタン　左側 -->
				<button type="button" onclick="location.href='./CompanyList.html'">企業一覧に戻る</button>
			</div>

		</main>
	</div>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>
