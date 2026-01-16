<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
<title>開催詳細(職員)</title>

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

		<div class="detail">
			<c:choose>
				<c:when test="${empty event}">
					<div class="errormsg">イベント情報が存在しません。</div>
				</c:when>

				<c:otherwise>
					<div class="detail-row">
						<div class="field-name">企業名</div>
						<div class="detail-company">${event.event.company.companyName}</div>
					</div>

					<div class="detail-row">
						<div class="field-name">担当職員</div>
						<div class="detail-data">${event.event.staff.staffName}</div>

						<div class="field-name">連絡先</div>
						<div class="detail-data">${event.event.staff.staffEmail}</div>
					</div>

					<div class="detail-row">
						<div class="field-name">日時</div>
						<div class="detail-data">
							${event.event.eventStartTime.year}/${event.event.eventStartTime.monthValue}/${event.event.eventStartTime.dayOfMonth}
							${event.event.eventStartTime.hour < 10 ? '0' : ''}${event.event.eventStartTime.hour}:${event.event.eventStartTime.minute < 10 ? '0' : ''}${event.event.eventStartTime.minute}～
							${event.event.eventEndTime.year}/${event.event.eventEndTime.monthValue}/${event.event.eventEndTime.dayOfMonth}
							${event.event.eventEndTime.hour < 10 ? '0' : ''}${event.event.eventEndTime.hour}:${event.event.eventEndTime.minute < 10 ? '0' : ''}${event.event.eventEndTime.minute}
						</div>
					</div>

					<div class="detail-row">
						<div class="field-name">場所</div>
						<div class="detail-data">${event.event.eventPlace}</div>
					</div>

					<div class="detail-row">
						<div class="field-name">参加卒業生</div>
						<div>
							<table class="eventdetail-table">
								<c:choose>
									<c:when test="${empty event.graduates}">
										<tr>
											<td><div class="errormsg">参加卒業生はいません。</div></td>
										</tr>
									</c:when>
									<c:otherwise>
										<c:forEach var="g" items="${event.graduates}">
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
									</c:otherwise>
								</c:choose>
							</table>
						</div>
					</div>

					<div class="detail-row">
						<div class="field-name">定員</div>
						<div class="detail-data">${event.event.eventCapacity}名</div>

						<div class="field-name">参加</div>
						<div class="detail-data">${event.joinStudentCount}名</div>

						<div class="detail-data">
							<c:if test="${event.joinStudentCount > 0}">
								<a
									href="join_student?command=JoinStudentList&eventId=${event.event.eventId}">
									在校生参加者確認 </a>
							</c:if>
						</div>
					</div>

					<div class="detail-row">
						<div class="field-name">その他</div>
						<div class="detail-data">
							<td>${g.otherInfo}</td>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>

		<!--		eventProgressがあったら、開催終了・開催中止ボタンは出さない-->
		<div class="bottom-btn-split">
			<div>
				<button type="button"
					onclick="location.href='event?command=EventList'">戻る</button>
			</div>
			<c:choose>
				<c:when test="${event.event.eventProgress=='ONGOING'}">
					<div class="btn-gap">
						<button type="button"
							onclick="if (confirm('開催終了。\nこの操作は取り消せませんが、よろしいですか？')) { location.href='event?command=EventEnd&eventId=${event.event.eventId}'; }">
							開催終了</button>


						<button type="button"
							onclick="if (confirm('中止します。\nこの操作は取り消せません。\n\nまた、参加予定の在校生・卒業生へ中止通知メールが送信されますが、よろしいですか？')) { location.href='event?command=EventCancel&eventId=${event.event.eventId}'; }"
							class="white-btn">開催中止</button>

					</div>
				</c:when>
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