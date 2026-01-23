<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>イベント回答フォーム確認画面</title>
</head>

<body>
	<header>
		<div class="head-part">
			<div id="h-left">
				<img src="img/rogo.png" alt="アイコン">
			</div>

			<div class="header-title">
				<div class="title-jp">就活サポート</div>
				<div class="title-en">Career Support</div>
			</div>
		</div>
	</header>

	<main>

		<div class="page-title">参加可否・希望日時確認</div>

		<c:set var="a" value="${answer}" />

		<form action="answer" method="post"
			onsubmit="return showLoadingAndDisableSubmit(this);">

			<!-- command だけで戻る／確定を切替 -->
			<input type="hidden" name="command" value="RegistAnswer"> <input
				type="hidden" name="answerId" value="${a.answerId}"> <input
				type="hidden" name="eventAvailability"
				value="${a.eventAvailability}"> <input type="hidden"
				name="firstChoiceStart" value="${a.firstChoiceStartTime}"> <input
				type="hidden" name="firstChoiceEnd" value="${a.firstChoiceEndTime}">
			<input type="hidden" name="secondChoiceStart"
				value="${a.secondChoiceStartTime}"> <input type="hidden"
				name="secondChoiceEnd" value="${a.secondChoiceEndTime}"> <input
				type="hidden" name="thirdChoiceStart"
				value="${a.thirdChoiceStartTime}"> <input type="hidden"
				name="thirdChoiceEnd" value="${a.thirdChoiceEndTime}">

			<table class="confirm-table">

				<tr>
					<th>参加可否</th>
					<td><c:choose>
							<c:when test="${a.eventAvailability}">
							出席する
						</c:when>
							<c:otherwise>
							出席しない
						</c:otherwise>
						</c:choose></td>
				</tr>

				<c:if test="${a.eventAvailability}">

					<tr>
						<th>第一希望</th>
						<td>${a.firstChoiceStartTime}～ ${a.firstChoiceEndTime}</td>
					</tr>

					<c:if test="${a.secondChoiceStartTime != null}">
						<tr>
							<th>第二希望</th>
							<td>${a.secondChoiceStartTime}～ ${a.secondChoiceEndTime}</td>
						</tr>
					</c:if>

					<c:if test="${a.thirdChoiceStartTime != null}">
						<tr>
							<th>第三希望</th>
							<td>${a.thirdChoiceStartTime}～ ${a.thirdChoiceEndTime}</td>
						</tr>
					</c:if>

				</c:if>

			</table>

			<div class="bottom-btn-split">
				<button type="button"
					onclick="
					this.form.command.value='AnswerBack';
					this.form.submit();
				">
					戻る</button>

				<button type="submit">回答</button>
			</div>

		</form>

	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
	<jsp:include page="/common/loading.jsp" />
</body>
</html>
