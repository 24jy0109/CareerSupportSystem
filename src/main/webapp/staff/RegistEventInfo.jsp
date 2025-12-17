<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>開催情報登録</title>

<style>
table {
	border-collapse: collapse;
	width: 90%;
	margin-top: 20px;
}

th, td {
	border: 1px solid #333;
	padding: 8px;
}

th {
	background: #eee;
}

.btn {
	padding: 6px 12px;
	background: #448aff;
	color: #fff;
	border: none;
	border-radius: 4px;
}

.input {
	padding: 5px;
}

.form-block {
	margin: 10px 0;
}
</style>
</head>

<body>

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

			<h2>イベントID：${event.eventId}</h2>
			<p>企業ID：${event.company.companyId}</p>

			<hr />

			<!-- =========================
 イベント登録フォーム
 ========================= -->
			<form action="event?command=RegistEvent" method="post">

				<input type="hidden" name="companyId"
					value="${event.company.companyId}" />

				<c:if test="${not empty answer}">
					<input type="hidden" name="answerId" value="${answer.answerId}" />
				</c:if>

				<c:if test="${not empty answer}">
					<input type="hidden" name="eventId" value="${answer.event.eventId}" />
				</c:if>

				<div class="form-block">
					<label>開催場所：</label> <input type="text" name="eventPlace"
						class="input" value="${event.eventPlace}" />
				</div>

				<div class="form-block">
					<label>開始日時：</label> <input type="datetime-local"
						name="eventStartTime" class="input" value="${startTime}" />
				</div>

				<div class="form-block">
					<label>終了日時：</label> <input type="datetime-local"
						name="eventEndTime" class="input" value="${endTime}" />
				</div>

				<div class="form-block">
					<label>定員：</label> <input type="number" name="eventCapacity"
						class="input" min="1" value="${event.eventCapacity}" />
				</div>

				<div class="form-block">
					<label>備考：</label>
					<textarea name="eventOtherInfo" class="input" rows="3">${event.eventOtherInfo}</textarea>
				</div>

				<div class="form-block">
					<label>担当スタッフ：</label> <select name="staffId" class="input"
						required>
						<c:forEach var="st" items="${dto.staffs}">
							<option value="${st.staffId}">${st.staffName}</option>
						</c:forEach>
					</select>
				</div>

				<!-- =========================
 卒業生選択
 ========================= -->
				<h3>参加卒業生</h3>

				<table>
					<tr>
						<th>選択</th>
						<th>学籍番号</th>
						<th>氏名</th>
						<th>コース</th>
						<th>学年</th>
						<th>担当スタッフ</th>
					</tr>

					<c:forEach var="g" items="${dto.graduates}">
						<tr>
							<td><input type="checkbox" name="graduateStudents"
								value="${g.graduateStudentNumber}"
								<c:if test="${g.graduateStudentNumber == selectedStudentNumber}">
						       	checked
						       </c:if> />
							</td>
							<td>${g.graduateStudentNumber}</td>
							<td>${g.graduateName}</td>
							<td>${g.course.courseName}</td>
							<td>${g.course.courseTerm}</td>
							<td>
								<p>${empty g.staff ? '未割当' : g.staff.staffName}</p> <a
								href="event?command=ScheduleArrangeSendForm&graduateStudentNumber=${g.graduateStudentNumber}">
									開催相談 </a>
							</td>
						</tr>
					</c:forEach>
				</table>

				<br />
				<button type="submit" class="btn">イベント登録</button>
			</form>

			<hr />
		</c:forEach>

	</main>

</body>
</html>
