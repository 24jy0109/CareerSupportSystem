<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
<title>開催情報登録画面（職員）</title>
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

	<c:forEach var="dto" items="${events}">

		<!-- =========================
		     表示用 Event を決定
		     ========================= -->
		<c:choose>
			<c:when test="${not empty inputEvent}">
				<c:set var="event" value="${inputEvent}" />
			</c:when>
			<c:otherwise>
				<c:set var="event" value="${dto.event}" />
			</c:otherwise>
		</c:choose>

		<!-- =========================
		     エラーメッセージ
		     ========================= -->
		<p style="color:red;">${error}</p>

		<div class="registevent-layout">

			<div class="registevent-row">
				<div class="field-name">企業名</div>
				<div class="company-name">${dto.event.company.companyName}</div>
			</div>

			<form action="event?command=RegistEventConfirm" method="post">

				<input type="hidden" name="companyId"
					value="${dto.event.company.companyId}" />

				<!-- =========================
				     日時
				     ========================= -->
				<div class="two-item-set">
					<div class="registevent-row">
						<div class="field-name">開始日時</div>
						<input type="datetime-local" name="eventStartTime"
							class="registevent-input"
							value="${event.eventStartTime != null
								? event.eventStartTime.toString().substring(0,16)
								: ''}" />
					</div>

					<div class="registevent-row">
						<div class="field-name">終了日時</div>
						<input type="datetime-local" name="eventEndTime"
							class="registevent-input"
							value="${event.eventEndTime != null
								? event.eventEndTime.toString().substring(0,16)
								: ''}" />
					</div>
				</div>

				<!-- =========================
				     場所・定員
				     ========================= -->
				<div class="two-item-set">
					<div class="registevent-row">
						<div class="field-name">場所</div>
						<input type="text" name="eventPlace"
							class="registevent-input"
							value="${event.eventPlace}" />
					</div>

					<div class="registevent-row">
						<div class="field-name">定員</div>
						<input type="number" name="eventCapacity"
							class="registevent-input" min="1"
							value="${event.eventCapacity}" />
					</div>
				</div>

				<!-- =========================
				     備考
				     ========================= -->
				<div class="registevent-row">
					<div class="field-name">その他</div>
					<textarea name="eventOtherInfo"
						class="registevent-input"
						rows="3">${event.eventOtherInfo}</textarea>
				</div>

				<!-- =========================
				     担当スタッフ
				     ========================= -->
				<div class="registevent-row">
					<div class="field-name">開催担当者</div>
					<select name="staffId" class="input" required>
						<c:forEach var="st" items="${dto.staffs}">
							<option value="${st.staffId}"
								<c:if test="${event.staff != null && st.staffId == event.staff.staffId}">
									selected
								</c:if>>
								${st.staffName}
							</option>
						</c:forEach>
					</select>
				</div>

				<!-- =========================
				     卒業生選択
				     ========================= -->
				<table class="registevent-table">
					<tr class="registevent-r">
						<th></th>
						<th>卒業年次</th>
						<th>名前</th>
						<th>学科</th>
						<th>職種</th>
						<th>担当者</th>
						<th></th>
					</tr>

					<c:forEach var="g" items="${dto.graduates}">
						<tr class="registevent-r">
							<td>
								<input type="checkbox" name="graduateStudents"
									value="${g.graduateStudentNumber}"
									<c:if test="${event.joinGraduates != null &&
										event.joinGraduates.contains(g)}">
										checked
									</c:if> />
							</td>

							<td>
								<c:set var="enterYear"
									value="${fn:substring(g.graduateStudentNumber, 0, 2)}" />
								${enterYear + g.course.courseTerm}年卒
							</td>

							<td>${g.graduateName}</td>
							<td>${g.course.courseName}</td>
							<td>${g.graduateJobCategory}</td>
							<td>${empty g.staff ? '未割当' : g.staff.staffName}</td>

							<td>
								<button type="button"
									onclick="location.href='event?command=ScheduleArrangeSendForm&graduateStudentNumber=${g.graduateStudentNumber}'">
									開催相談
								</button>
							</td>
						</tr>
					</c:forEach>
				</table>

				<!-- =========================
				     ボタン
				     ========================= -->
				<div class="bottom-btn-split">
					<button type="button"
						onclick="location.href='company?command=CompanyList'">
						戻る
					</button>

					<input type="submit" class="event-btn" value="開催">
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