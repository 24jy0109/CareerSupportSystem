<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>開催情報登録確認</title>
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
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
		<div class="page-title">開催情報登録確認</div>
		<c:set var="dto" value="${event}" />
		<c:set var="event" value="${dto.event}" />

		<form id="eventForm" action="event" method="post">

			<!-- hidden 再送信用 -->
			<c:if test="${not empty event.eventId and event.eventId != 0}">
				<input type="hidden" name="eventId" value="${event.eventId}" />
			</c:if>
			<input type="hidden" name="companyId"
				value="${event.company.companyId}" /> <input type="hidden"
				name="eventStartTime" value="${event.eventStartTime}" /> <input
				type="hidden" name="eventEndTime" value="${event.eventEndTime}" />
			<input type="hidden" name="eventPlace" value="${event.eventPlace}" />
			<input type="hidden" name="eventCapacity"
				value="${event.eventCapacity}" /> <input type="hidden"
				name="eventOtherInfo" value="${event.eventOtherInfo}" /> <input
				type="hidden" name="staffId" value="${event.staff.staffId}" />

			<c:forEach var="g" items="${event.joinGraduates}">
				<input type="hidden" name="graduateStudents"
					value="${g.graduateStudentNumber}" />
			</c:forEach>

			<!-- command 用 hidden -->
			<input type="hidden" id="commandField" name="command"
				value="RegistEvent" />

			<div class="detail-row">
				<div class="field-name">企業名</div>
				<div class="detail-company">${event.company.companyName}</div>
			</div>

			<div class="detail-row">
				<div class="field-name">担当職員</div>
				<div class="detail-data">${event.staff.staffName}</div>
			</div>

			<div class="detail-row">
				<div class="field-name">日時</div>
				<div class="detail-data">
					${event.eventStartTime.year}/${event.eventStartTime.monthValue}/${event.eventStartTime.dayOfMonth}
					${event.eventStartTime.hour < 10 ? '0' : ''}${event.eventStartTime.hour}:${event.eventStartTime.minute < 10 ? '0' : ''}${event.eventStartTime.minute}～
					${event.eventEndTime.year}/${event.eventEndTime.monthValue}/${event.eventEndTime.dayOfMonth}
					${event.eventEndTime.hour < 10 ? '0' : ''}${event.eventEndTime.hour}:${event.eventEndTime.minute < 10 ? '0' : ''}${event.eventEndTime.minute}
				</div>
			</div>

			<div class="detail-row">
				<div class="field-name">場所</div>
				<div class="detail-data">${event.eventPlace}</div>
			</div>

			<div class="detail-row">
				<div class="field-name">参加卒業生</div>
				<div>
					<table class="eventdetail-table">
						<c:forEach var="g" items="${dto.event.joinGraduates}">
							<tr>
								<td class="detail-data"><c:if
										test="${not empty g.graduateStudentNumber}">
										<c:set var="enterYear2"
											value="${fn:substring(g.graduateStudentNumber, 0, 2)}" />
										<c:set var="graduateYear"
											value="${enterYear2 + g.course.courseTerm}" />
														${graduateYear}年卒
													</c:if></td>

								<td><div class="detail-data">${g.graduateName}</div></td>
								<td><div class="detail-data">${g.course.courseName}</div></td>
								<td>
									<div class="detail-data">${g.graduateJobCategory}</div>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>

			<div class="detail-row">
				<div class="field-name">定員</div>
				<div class="detail-data">${event.eventCapacity}名</div>
			</div>

			<div class="detail-row">
				<div class="field-name">その他</div>
				<div class="detail-data">
					<td><c:out value="${event.eventOtherInfo}" /></td>
				</div>
			</div>

			<div class="bottom-btn-split">
				<!-- 戻るボタンは JS で command を切り替える -->
				<button type="submit"
					onclick="document.getElementById('commandField').value='RegistEventBack'">
					戻る</button>
				<button type="submit">開催</button>
			</div>
		</form>

	</main>

</body>
</html>