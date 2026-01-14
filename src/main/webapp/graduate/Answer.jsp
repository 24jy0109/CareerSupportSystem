<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/companylist.css">
<link rel="stylesheet" href="./css/layout.css">
<title>イベント回答フォーム</title>

<script>
	function toggleTimeFields() {
		const checked = document.querySelector('input[name="eventAvailability"]:checked');
		if (!checked) return;

		const isAttend = checked.value === "true";
		const timeArea = document.getElementById("time-area");

		if (isAttend) {
			timeArea.style.display = "block";
		} else {
			timeArea.style.display = "none";
			timeArea.querySelectorAll("input[type='datetime-local']").forEach(input => {
				input.value = "";
			});
		}
	}

	window.onload = toggleTimeFields;
</script>
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
		<%
		String answerId = request.getParameter("answerId");
		%>

		<div class="page-title">参加可否・希望日時フォーム</div>

		<p style="color: red;">${error}</p>

		<form action="answer" method="post">

			<input type="hidden" name="answerId" value="<%=answerId%>"> <input
				type="hidden" name="command" value="AnswerConfirm">

			<!-- 参加可否 -->
			<label>参加可否</label><br>
			<div class="answer-row">
				<input type="radio" name="eventAvailability" value="true"
					<c:if test="${inputAnswer == null || inputAnswer.eventAvailability}">
					checked
				</c:if>
					onchange="toggleTimeFields()"> 出席する <input type="radio"
					name="eventAvailability" value="false"
					<c:if test="${inputAnswer != null && !inputAnswer.eventAvailability}">
					checked
				</c:if>
					onchange="toggleTimeFields()"> 出席しない
			</div>
			<br>

			<div id="time-area">

				<label>日程入力</label>

				<div class="answer-flame">
					<!-- 第一希望 -->
					<div class="answer-row">
						<div>第一希望</div>

						<input type="datetime-local" name="firstChoiceStart"
							value="${inputAnswer != null && inputAnswer.firstChoiceStartTime != null
						? inputAnswer.firstChoiceStartTime.toString().substring(0,16)
						: ''}">～
						<input type="datetime-local" name="firstChoiceEnd"
							value="${inputAnswer != null && inputAnswer.firstChoiceEndTime != null
						? inputAnswer.firstChoiceEndTime.toString().substring(0,16)
						: ''}">

					</div>


					<!-- 第二希望 -->
					<div class="answer-row">
						<div>第二希望</div>
						<input type="datetime-local" name="secondChoiceStart"
							value="${inputAnswer != null && inputAnswer.secondChoiceStartTime != null
						? inputAnswer.secondChoiceStartTime.toString().substring(0,16)
						: ''}">～
						<input type="datetime-local" name="secondChoiceEnd"
							value="${inputAnswer != null && inputAnswer.secondChoiceEndTime != null
						? inputAnswer.secondChoiceEndTime.toString().substring(0,16)
						: ''}">
					</div>

					<!-- 第三希望 -->
					<div class="answer-row">
						<div>第三希望</div>
						<input type="datetime-local" name="thirdChoiceStart"
							value="${inputAnswer != null && inputAnswer.thirdChoiceStartTime != null
						? inputAnswer.thirdChoiceStartTime.toString().substring(0,16)
						: ''}">～
						<input type="datetime-local" name="thirdChoiceEnd"
							value="${inputAnswer != null && inputAnswer.thirdChoiceEndTime != null
						? inputAnswer.thirdChoiceEndTime.toString().substring(0,16)
						: ''}">
					</div>
				</div>

			</div>

			<input type="submit" class="submit-btn" value="回答する">
		</form>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>
</body>
</html>