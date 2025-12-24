<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>開催情報登録 確認</title>
<link rel="stylesheet" href="./css/layout.css">
</head>

<body>

<header>
	<h2>開催情報登録（確認）</h2>
</header>

<main>

<c:set var="dto" value="${event}" />
<c:set var="event" value="${dto.event}" />

<form action="event?command=RegistEventExecute" method="post">

	<!-- hidden 再送信用 -->
	<input type="hidden" name="companyId" value="${event.company.companyId}" />
	<input type="hidden" name="eventStartTime" value="${event.eventStartTime}" />
	<input type="hidden" name="eventEndTime" value="${event.eventEndTime}" />
	<input type="hidden" name="eventPlace" value="${event.eventPlace}" />
	<input type="hidden" name="eventCapacity" value="${event.eventCapacity}" />
	<input type="hidden" name="eventOtherInfo" value="${event.eventOtherInfo}" />
	<input type="hidden" name="staffId" value="${event.staff.staffId}" />

	<c:forEach var="g" items="${dto.graduates}">
		<input type="hidden" name="graduateStudents"
		       value="${g.graduateStudentNumber}" />
	</c:forEach>

	<table class="confirm-table">

		<tr>
			<th>企業名</th>
			<td>${event.company.companyName}</td>
		</tr>

		<tr>
			<th>開始日時</th>
			<td>${event.eventStartTime}</td>
		</tr>

		<tr>
			<th>終了日時</th>
			<td>${event.eventEndTime}</td>
		</tr>

		<tr>
			<th>開催場所</th>
			<td>${event.eventPlace}</td>
		</tr>

		<tr>
			<th>定員</th>
			<td>${event.eventCapacity} 名</td>
		</tr>

		<tr>
			<th>その他</th>
			<td><c:out value="${event.eventOtherInfo}" /></td>
		</tr>

		<tr>
			<th>開催担当者</th>
			<td>${event.staff.staffName}</td>
		</tr>

	</table>

	<h3>参加卒業生</h3>

	<table class="confirm-table">
		<tr>
			<th>学籍番号</th>
			<th>氏名</th>
			<th>学科</th>
			<th>職種</th>
		</tr>

		<c:forEach var="g" items="${dto.event.joinGraduates}">
			<tr>
				<td>${g.graduateStudentNumber}</td>
				<td>${g.graduateName}</td>
				<td>${g.course.courseName}</td>
				<td>${g.graduateJobCategory}</td>
			</tr>
		</c:forEach>
	</table>

	<div class="bottom-btn-split">
		<button type="button" onclick="history.back()">戻る</button>
		<button type="submit">確定</button>
	</div>

</form>

</main>

</body>
</html>
