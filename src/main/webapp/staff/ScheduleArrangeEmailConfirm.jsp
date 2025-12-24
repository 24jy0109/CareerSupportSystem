<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>日程調整メール 確認</title>
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
</head>

<body>

	<header>
		<h2>日程調整メール（確認）</h2>
	</header>

	<main>

		<c:set var="mail" value="${email}" />

		<form action="event" method="post">
			<input type="hidden" name="command" value="SendScheduleArrangeEmail">

			<!-- 再送信用 hidden（入力値そのもの） -->
			<input type="hidden" name="graduateStudentNumber"
				value="${mail.graduate.graduateStudentNumber}"> <input
				type="hidden" name="staffId" value="${mail.staff.staffId}">
			<input type="hidden" name="companyId"
				value="${mail.company.companyId}"> <input type="hidden"
				name="mailTitle" value="${mail.email.subject}"> <input
				type="hidden" name="mailBody" value="${mail.inputBody}">

			<table class="confirm-table">

				<tr>
					<th>担当者</th>
					<td>${mail.staff.staffName}</td>
				</tr>

				<tr>
					<th>宛先</th>
					<td>${mail.graduate.graduateName}
						（${mail.graduate.graduateStudentNumber}）</td>
				</tr>

				<tr>
					<th>件名</th>
					<td>${mail.email.subject}</td>
				</tr>

				<tr>
					<th>本文</th>
					<td><pre class="confirm-mail-body">
<c:out value="${mail.email.body}" />
				</pre></td>
				</tr>

			</table>

			<div class="bottom-btn-split">
				<button type="button" onclick="history.back()">戻る</button>
				<button type="submit">送信</button>
			</div>

		</form>

	</main>

</body>
</html>
