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


		<script type="text/javascript">
			function confirmJoin(eventId) {
				if (confirm(
						"このイベントに参加登録します。\n" +
						"システム上から登録後の取り消しはできません。\n" +
						"変更が必要な場合は、担当職員までご連絡ください。\n" +
						"よろしいですか？"
				)) {
					location.href = "event?command=EventJoin&eventId=" + eventId;
				}
				return false;
			}
		
			function confirmNotJoin(eventId) {
				if (confirm(
						"このイベントに不参加で登録します。\n" +
						"システム上から登録後の取り消しはできません。\n" +
						"変更が必要な場合は、担当職員までご連絡ください。\n" +
						"よろしいですか？"
				)) {
					location.href = "event?command=EventNotJoin&eventId=" + eventId;
				}
				return false;
			}
		</script>
	</header>
	<main>
		<div class="page-title">開催詳細</div>
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
										<!--									<div class="detail-data">-->
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


												<!--									</div>-->

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

					</div>
					<div class="detail-row">
						<div class="field-name">その他</div>
						<div class="detail-data">${event.event.eventOtherInfo}</div>
					</div>




					<%-- 参加可否表示 --%>
					<div class="join-status">
						<c:choose>

							<%-- 参加済み --%>
							<c:when test="${event.joinAvailability == true}">

								<c:choose>
									<c:when test="${event.event.eventProgress == 'FINISHED'}">
										<div class="eventdetail-msg">参加済</div>
									</c:when>

									<c:when test="${event.event.eventProgress == 'CANCELED'}">
										<div class="eventdetail-msg">中止</div>
									</c:when>

									<c:otherwise>
										<div class="eventdetail-msg">参加予定</div>
									</c:otherwise>
								</c:choose>

								<div class="bottom-btn-left">
									<div>
										<button type="button" onclick="history.back()">戻る</button>
									</div>
								</div>
							</c:when>

							<%-- 不参加 --%>
							<c:when test="${event.joinAvailability == false}">
								<div class="eventdetail-msg">不参加</div>
								<div class="bottom-btn-left">
									<div>
										<button type="button" onclick="history.back()">戻る</button>
									</div>
								</div>
							</c:when>

							<%-- 未回答 --%>
							<c:otherwise>
								<c:choose>

									<%-- 定員未達 --%>
									<c:when
										test="${event.joinStudentCount < event.event.eventCapacity}">
										<%-- 戻るボタンと参加・不参加ボタン --%>
										<div class="bottom-btn-split">
											<div>
												<button type="button" onclick="history.back()">戻る</button>
											</div>

											<div class="btn-gap">

												<!-- 参加ボタン -->
												<button type="button"
													onclick="return confirmJoin(${event.event.eventId});">
													参加</button>

												<!-- 不参加ボタン：studentRequested == true のときのみ -->
												<c:if test="${event.studentRequested}">
													<button type="button" class="white-btn"
														onclick="return confirmNotJoin(${event.event.eventId});">
														不参加</button>
												</c:if>

											</div>
										</div>
									</c:when>
									<%-- 定員到達 --%>
									<c:otherwise>
										<div class="bottom-btn-left">
											<div>
												<button type="button" onclick="history.back()">戻る</button>
											</div>
										</div>
										<div class="eventdetail-msg">満員です</div>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>



					</div>
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
