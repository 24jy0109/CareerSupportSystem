<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>開催情報登録画面（職員）</title>

<!--<style>-->
<!--table {-->
<!--	border-collapse: collapse;-->
<!--	width: 90%;-->
<!--	margin-top: 20px;-->
<!--}-->

<!--th, td {-->
<!--	border: 1px solid #333;-->
<!--	padding: 8px;-->
<!--}-->

<!--th {-->
<!--	background: #eee;-->
<!--}-->

<!--.btn {-->
<!--	padding: 6px 12px;-->
<!--	background: #448aff;-->
<!--	color: #fff;-->
<!--	border: none;-->
<!--	border-radius: 4px;-->
<!--}-->

<!--.input {-->
<!--	padding: 5px;-->
<!--}-->

<!--.form-block {-->
<!--	margin: 10px 0;-->
<!--}-->
<!--</style>-->
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


		<!-- =========================
 Answer から希望時間を決定
 ========================= -->
		<c:set var="startTime" value="" />
		<c:set var="endTime" value="" />
		<c:set var="selectedStudentNumber" value="" />

		<c:if test="${not empty answer}">
			<c:if test="${not empty answer.firstChoiceStartTime}">
				<c:set var="startTime" value="${answer.firstChoiceStartTime}" />
				<c:set var="endTime" value="${answer.firstChoiceEndTime}" />
			</c:if>

			<c:if
				test="${empty startTime and not empty answer.secondChoiceStartTime}">
				<c:set var="startTime" value="${answer.secondChoiceStartTime}" />
				<c:set var="endTime" value="${answer.secondChoiceEndTime}" />
			</c:if>

			<c:if
				test="${empty startTime and not empty answer.thirdChoiceStartTime}">
				<c:set var="startTime" value="${answer.thirdChoiceStartTime}" />
				<c:set var="endTime" value="${answer.thirdChoiceEndTime}" />
			</c:if>

			<c:set var="selectedStudentNumber"
				value="${answer.graduate.graduateStudentNumber}" />
		</c:if>

		<!-- =========================
 Event 表示
 ========================= -->
		<c:forEach var="dto" items="${events}">
			<c:set var="event" value="${dto.event}" />

			<!--			<h2>イベントID：${event.eventId}</h2>-->
			<!--			<p>企業ID：${event.company.companyId}</p>-->

			<!--			<hr />-->


			<!-- =========================
 イベント登録フォーム
 ========================= -->
			<div class="registevent-layout">

				<!-- 入力部分 -->
				<!--項目名と企業名・input・selectを横並びにするdiv -->
				<div class="registevent-row">
					<div class="field-name">企業名</div>
					<div class=company-name>${event.company.companyName}</div>
				</div>
				<form action="event?command=RegistEvent" method="post">

					<input type="hidden" name="companyId"
						value="${event.company.companyId}" />

					<c:if test="${not empty answer}">
						<input type="hidden" name="answerId" value="${answer.answerId}" />
					</c:if>

					<c:if test="${not empty answer}">
						<input type="hidden" name="eventId"
							value="${answer.event.eventId}" />
					</c:if>
					<!--戻るボタンの行先を変える-->
					<c:choose>
						<c:when test="${not empty answer}">
							<c:set var="backUrl" value="answer?command=ScheduleAnswerCheck" />
							<c:set var="backLabel" value="回答一覧に戻る" />
						</c:when>
						<c:otherwise>
							<c:set var="backUrl" value="company?command=CompanyList" />
							<c:set var="backLabel" value="企業一覧に戻る" />
						</c:otherwise>
					</c:choose>

					<!--					<div class="form-block">-->
					<!--						<label>開始日時：</label> <input type="datetime-local"-->
					<!--							name="eventStartTime" class="input" value="${startTime}" />-->
					<!--					</div>-->

					<!--					<div class="form-block">-->
					<!--						<label>終了日時：</label> <input type="datetime-local"-->
					<!--							name="eventEndTime" class="input" value="${endTime}" />-->
					<!--					</div>-->

					<div class="two-item-set">
						<div class="registevent-row">
							<div class="field-name">開始日時</div>
							<input type="datetime-local" name="eventStartTime"
								class="registevent-input" value="${startTime}" />
						</div>
						<div class="registevent-row">
							<div class="field-name">終了日時</div>
							<input type="datetime-local" name="eventEndTime"
								class="registevent-input" value="${endTime}" />
						</div>
					</div>

					<!--					<div class="form-block">-->
					<!--						<label>開催場所：</label> <input type="text" name="eventPlace"-->
					<!--							class="input" value="${event.eventPlace}" />-->
					<!--					</div>-->


					<!--					<div class="form-block">-->
					<!--						<label>定員：</label> <input type="number" name="eventCapacity"-->
					<!--							class="input" min="1" value="${event.eventCapacity}" />-->
					<!--					</div>-->

					<div class="two-item-set">
						<div class="registevent-row">
							<div class="field-name">場所</div>
							<input type="text" name="eventPlace" class="registevent-input"
								value="${event.eventPlace}" />
						</div>
						<div class="registevent-row">
							<div class="field-name">定員</div>
							<input type="number" name="eventCapacity"
								class="registevent-input" min="1" value="${event.eventCapacity}" />
						</div>
					</div>


					<!--					<div class="form-block">-->
					<!--						<label>備考：</label>-->
					<!--						<textarea name="eventOtherInfo" class="input" rows="3">${event.eventOtherInfo}</textarea>-->
					<!--					</div>-->


					<div class="registevent-row">
						<div class="field-name">その他</div>
						<textarea name="eventOtherInfo" class="registevent-input" rows="3">${event.eventOtherInfo}</textarea>
					</div>

					<!--					<div class="form-block">-->
					<!--						<label>担当スタッフ：</label> <select name="staffId" class="input"-->
					<!--							required>-->
					<!--							<c:forEach var="st" items="${dto.staffs}">-->
					<!--								<option value="${st.staffId}">${st.staffName}</option>-->
					<!--							</c:forEach>-->
					<!--						</select>-->
					<!--					</div>-->

					<div class="registevent-row">
						<div class="field-name">開催担当者</div>
						<select name="staffId" class="input" required>
							<c:forEach var="st" items="${dto.staffs}">
								<option value="${st.staffId}">${st.staffName}</option>
							</c:forEach>
						</select>
					</div>

					<!-- =========================
 卒業生選択
 ========================= -->

					<!--					<table>-->
					<!--						<tr>-->
					<!--							<th>選択</th>-->
					<!--							<th>学籍番号</th>-->
					<!--							<th>氏名</th>-->
					<!--							<th>コース</th>-->
					<!--							<th>学年</th>-->
					<!--							<th>担当スタッフ</th>-->
					<!--						</tr>-->

					<!--						<c:forEach var="g" items="${dto.graduates}">-->
					<!--							<tr>-->
					<!--								<td><input type="checkbox" name="graduateStudents"-->
					<!--									value="${g.graduateStudentNumber}"-->
					<!--									<c:if test="${g.graduateStudentNumber == selectedStudentNumber}">-->
					<!--						       	checked-->
					<!--						       </c:if> />-->
					<!--								</td>-->
					<!--								<td>${g.graduateStudentNumber}</td>-->
					<!--								<td>${g.graduateName}</td>-->
					<!--								<td>${g.course.courseName}</td>-->
					<!--								<td>${g.course.courseTerm}</td>-->
					<!--								<td>-->
					<!--									<p>${empty g.staff ? '未割当' : g.staff.staffName}</p> <a-->
					<!--									href="event?command=ScheduleArrangeSendForm&graduateStudentNumber=${g.graduateStudentNumber}">-->
					<!--										開催相談 </a>-->
					<!--								</td>-->
					<!--							</tr>-->
					<!--						</c:forEach>-->
					<!--					</table>-->

					<table class="registevent-table">
						<tr class="registevent-r">
							<th class="registevent-h"></th>
							<th class="registevent-h">卒業年次</th>
							<th class="registevent-h">名前</th>
							<th class="registevent-h">学科</th>
							<th class="registevent-h">職種</th>
							<th class="registevent-h">担当者</th>
							<th class="registevent-h"></th>
						</tr>

						<c:forEach var="g" items="${dto.graduates}">
							<tr class="registevent-r">
								<td><input type="checkbox" name="graduateStudents"
									value="${g.graduateStudentNumber}" name="join-graduate"
									<c:if test="${g.graduateStudentNumber == selectedStudentNumber}">
						       	checked
						       </c:if> />
								</td>
								<td><c:if test="${not empty g.graduateStudentNumber}">
										<c:set var="enterYear2"
											value="${fn:substring(g.graduateStudentNumber, 0, 2)}" />
										<c:set var="graduateYear"
											value="${enterYear2 + g.course.courseTerm}" />
							        ${graduateYear}年卒
							    </c:if></td>
								<td>${g.graduateName}</td>
								<td>${g.course.courseName}</td>
								<td>${g.graduateJobCategory}</td>
								<td>
									<p>${empty g.staff ? '未割当' : g.staff.staffName}</p>
								</td>

								<td>
									<button type="button"
										onclick="location.href='event?command=ScheduleArrangeSendForm&graduateStudentNumber=${g.graduateStudentNumber}'">開催相談</button>
								</td>
							</tr>
						</c:forEach>
					</table>

					<div class="bottom-btn-split">
						<button type="button" onclick="location.href='${backUrl}'">
							${backLabel}</button>

					</div>

				</form>
			</div>

		</c:forEach>

	</main>
	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>
