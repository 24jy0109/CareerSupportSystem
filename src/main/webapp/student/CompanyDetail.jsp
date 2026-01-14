<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>企業詳細</title>
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
		<div class="request-top">
			<c:if test="${not empty companies}">
				<!-- List<CompanyDTO> の1件目だけを取得 -->
				<c:set var="companyDTO" value="${companies[0]}" />
				<div class="company">
					<div class="field-name">企業名</div>
					<div class="company-name">${companyDTO.company.companyName}</div>
					<c:choose>
						<c:when test="${companyDTO.isRequest == '申請済み'}">
							<div class="red-msg">申請済</div>
						</c:when>
						<c:otherwise>未申請</c:otherwise>
					</c:choose>
				</div>

				<div class="event">
					<div class="event-info">
						<div class="event-frex">
							<div class="field-name">開催情報</div>
							<c:if test="${not empty companyDTO.company.events}">
								<table class="detail-table">
									<c:forEach var="event" items="${companyDTO.company.events}">
										<tr class="noline">
											<td>
												${event.eventStartTime.year}/${event.eventStartTime.monthValue}/${event.eventStartTime.dayOfMonth}</td>
											<td>${event.eventStartTime.hour < 10 ? '0' : ''}${event.eventStartTime.hour}:${event.eventStartTime.minute < 10 ? '0' : ''}${event.eventStartTime.minute}</td>
											<td>～</td>
											<td>
												${event.eventEndTime.year}/${event.eventEndTime.monthValue}/${event.eventEndTime.dayOfMonth}</td>

											<td>${event.eventEndTime.hour < 10 ? '0' : ''}${event.eventEndTime.hour}:${event.eventEndTime.minute
												< 10 ? '0' : ''}${event.eventEndTime.minute}</td>
											</td>
											<td>${event.eventPlace}</td>


										</tr>
									</c:forEach>
								</table>
							</c:if>
						</div>
						<c:if test="${empty companyDTO.company.events}">
							<div class="red-msg">開催予定のイベントはありません</div>
						</c:if>
					</div>
				</div>


				<c:if test="${not empty companyDTO.company.graduates}">
					<table>
						<thead>
							<tr>
								<th>卒業年次</th>
								<th>学科</th>
								<th>職種</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="grad" items="${companyDTO.company.graduates}">
								<tr>
									<td><c:if test="${not empty  grad.graduateStudentNumber}">
											<c:set var="enterYear2"
												value="${fn:substring(grad.graduateStudentNumber, 0, 2)}" />
											<c:set var="graduateYear"
												value="${enterYear2 + grad.course.courseTerm}" />
														${graduateYear}年卒
												</c:if></td>
									<td>${grad.course.courseName}</td>
									<td>${grad.graduateJobCategory}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:if>
				<c:if test="${empty companyDTO.company.graduates}">
					<div class="red-msg">卒業生情報はありません</div>
				</c:if>
		</div>
		</c:if>

		<c:if test="${empty companies}">
			<div class="red-msg">企業情報が見つかりません。</div>
		</c:if>

		<div class="bottom-btn-split">
			<button type="button"
				onclick="location.href='company?command=CompanyList'">企業一覧に戻る</button>
			<!-- ▼申請ボタン／取り消しボタン -->
			<c:choose>
				<c:when test="${companyDTO.isRequest == '申請済み'}">
					<form action="appointment_request" method="POST"
						style="display: inline;">
						<input type="hidden" name="command" value="CancelRequest">
						<input type="hidden" name="companyId"
							value="${companyDTO.company.companyId}">
						<button type="submit">取消</button>
					</form>
				</c:when>
				<c:otherwise>
					<form action="appointment_request" method="POST"
						style="display: inline;">
						<input type="hidden" name="command" value="ApplyRequest">
						<input type="hidden" name="companyId"
							value="${companyDTO.company.companyId}">
						<button type="submit">申請</button>
					</form>
				</c:otherwise>
			</c:choose>
		</div>
	</main>
	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>
