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

			<div class="header-user">ようこそ${name}さん</div>
		</div>
	</header>
	<main>
		<div class="wrapper">
			<main class="content">
				<div class="request-top">
					<c:choose>
						<c:when test="${empty requests}">
							<c:set var="company" value="${showCompany.company}" />
							<div class="company">
								<div class="field-name">企業名</div>
								<div class="company-name">${showCompany.company.companyName}</div>
							</div>
						</c:when>
						<c:otherwise>
							<c:set var="company" value="${requests[0].company}" />
							<div class="company">
								<div class="field-name">企業名</div>
								<div class="company-name">${company.companyName}</div>
							</div>
						</c:otherwise>
					</c:choose>


					<div class="event">
						<div class="event-info">
							<div class="event-frex">
								<div class="field-name">開催情報</div>
								<table class="detail-table">

									<c:if test="${empty company.events}">
										<tr class="noline">
											<div class="errormsg">対象イベントがありません。</div>
										</tr>
									</c:if>

									<c:forEach var="ev" items="${company.events}">
										<tr class="noline">

											<td>${ev.eventStartTime.year}/${ev.eventStartTime.monthValue}/${ev.eventStartTime.dayOfMonth}</td>
											<td>${ev.eventStartTime.hour < 10 ? '0' : ''}${ev.eventStartTime.hour}:${ev.eventStartTime.minute < 10 ? '0' : ''}${ev.eventStartTime.minute}</td>
											<td>～</td>
											<td>${ev.eventEndTime.year}/${ev.eventEndTime.monthValue}/${ev.eventEndTime.dayOfMonth}</td>
											<td>${ev.eventEndTime.hour < 10 ? '0' : ''}${ev.eventEndTime.hour}:${ev.eventEndTime.minute < 10 ? '0' : ''}${ev.eventEndTime.minute}</td>
											<td>${ev.eventPlace}</td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</div>


					</div>
					<c:if test="${empty requests}">
						<div class="errormsg">リクエストした学生はいません。</div>
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
									<td>${req.requestTime.year}/${req.requestTime.monthValue}/${req.requestTime.dayOfMonth}
										${req.requestTime.hour < 10 ? '0' : ''}${req.requestTime.hour}:${req.requestTime.minute < 10 ? '0' : ''}${req.requestTime.minute}
									</td>
								</tr>
							</c:forEach>

						</table>
					</c:if>

					<div class="bottom-btn-left">
						<!-- 戻るボタン　左側 -->
						<button type="button"
							onclick="location.href='company?command=CompanyList'">企業一覧に戻る</button>
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
