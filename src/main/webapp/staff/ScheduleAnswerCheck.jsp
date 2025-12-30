<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
<title>日程回答確認画面（職員）</title>
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
	</header>

	<main>
		<h2 class="page-title">日程回答確認</h2>

		<c:if test="${empty answers}">
			<p class="errormsg answer-errormsg">回答がありません。</p>
		</c:if>
		</tr>

		<table class="schedule-check-table">
			<tr class="schedule-check-r">
				<th class="schedule-check-h">企業名</th>
				<th class="schedule-check-h">名前</th>
				<th class="schedule-check-h">回答</th>
				<th class="schedule-check-h"></th>
			</tr>
			<c:forEach var="a" items="${answers}">
				<form action="event" method="get">
					<tr class="schedule-check-r2">
						<td>${a.event.company.companyName}</td>
						<td>${a.graduate.graduateName}</td>
						<td><label> <input type="radio" name="choice"
								value="1" required> 第1希望
								${a.firstChoiceStartTime.year}/${a.firstChoiceStartTime.monthValue}/${a.firstChoiceStartTime.dayOfMonth}
								${a.firstChoiceStartTime.hour < 10 ? '0' : ''}${a.firstChoiceStartTime.hour}:${a.firstChoiceStartTime.minute < 10 ? '0' : ''}${a.firstChoiceStartTime.minute}～
								${a.firstChoiceEndTime.year}/${a.firstChoiceEndTime.monthValue}/${a.firstChoiceEndTime.dayOfMonth}
								${a.firstChoiceEndTime.hour < 10 ? '0' : ''}${a.firstChoiceEndTime.hour}:${a.firstChoiceEndTime.minute < 10 ? '0' : ''}${a.firstChoiceEndTime.minute}
						</label></td>
						<td></td>
					</tr>

				<tr class="schedule-check-r3">
					<td></td>
					<td></td>
					<td><label> <input type="radio" name="choice"
							value="2"> 第2希望
							${a.secondChoiceStartTime.year}/${a.secondChoiceStartTime.monthValue}/${a.secondChoiceStartTime.dayOfMonth}
							${a.secondChoiceStartTime.hour < 10 ? '0' : ''}${a.secondChoiceStartTime.hour}:${a.secondChoiceStartTime.minute < 10 ? '0' : ''}${a.secondChoiceStartTime.minute}～
							${a.secondChoiceEndTime.year}/${a.secondChoiceEndTime.monthValue}/${a.secondChoiceEndTime.dayOfMonth}
							${a.secondChoiceEndTime.hour < 10 ? '0' : ''}${a.secondChoiceEndTime.hour}:${a.secondChoiceEndTime.minute < 10 ? '0' : ''}${a.secondChoiceEndTime.minute}
					</label></td>
					<td><input type="hidden" name="command" value="yesAnswer">
						<input type="hidden" name="answerId" value="${a.answerId}">
						<input type="submit" value="決定" class="decision-button">
						<button type="button" class="cancel-button"
							onclick="location.href='answer?command=noAnswer&answerId=${a.answerId}'">
							拒否</button></td>
				</tr>

				<tr class="schedule-check-r">
					<td></td>
					<td></td>
					<td><label> <input type="radio" name="choice"
							value="3"> 第3希望
							${a.thirdChoiceStartTime.year}/${a.thirdChoiceStartTime.monthValue}/${a.thirdChoiceStartTime.dayOfMonth}
							${a.thirdChoiceStartTime.hour < 10 ? '0' : ''}${a.thirdChoiceStartTime.hour}:${a.thirdChoiceStartTime.minute < 10 ? '0' : ''}${a.thirdChoiceStartTime.minute}～
							${a.thirdChoiceEndTime.year}/${a.thirdChoiceEndTime.monthValue}/${a.thirdChoiceEndTime.dayOfMonth}
							${a.thirdChoiceEndTime.hour < 10 ? '0' : ''}${a.thirdChoiceEndTime.hour}:${a.thirdChoiceEndTime.minute < 10 ? '0' : ''}${a.thirdChoiceEndTime.minute}

					</label></td>
					<td></td>
				</tr>
				</form>
			</c:forEach>
		</table>
	</main>




	<!--		<h2>回答一覧</h2>-->

	<!--		<c:forEach var="a" items="${answers}">-->
	<!--			<hr>-->

	<!--			<form action="event" method="post">-->

	<!--				<p>Answer ID：${a.answerId}</p>-->
	<!--				<p>企業名：${a.event.company.companyName}</p>-->
	<!--				<p>卒業生名：${a.graduate.graduateName}</p>-->

	<!--				<p>-->
	<!--					<label> <input type="radio" name="choice" value="1" required>-->
	<!--						第1希望： ${a.firstChoiceStartTime} ～ ${a.firstChoiceEndTime}-->
	<!--					</label>-->
	<!--				</p>-->

	<!--				<p>-->
	<!--					<label> <input type="radio" name="choice" value="2">-->
	<!--						第2希望： ${a.secondChoiceStartTime} ～ ${a.secondChoiceEndTime}-->
	<!--					</label>-->
	<!--				</p>-->

	<!--				<p>-->
	<!--					<label> <input type="radio" name="choice" value="3">-->
	<!--						第3希望： ${a.thirdChoiceStartTime} ～ ${a.thirdChoiceEndTime}-->
	<!--					</label>-->
	<!--				</p>-->

	<!--				<input type="hidden" name="command" value="yesAnswer"> <input-->
	<!--					type="hidden" name="answerId" value="${a.answerId}"> <input-->
	<!--					type="submit" value="決定">-->
	<!--			</form>-->

	<!--			 拒否 -->
	<!--			<form action="answer" method="post">-->
	<!--				<input type="hidden" name="command" value="noAnswer"> <input-->
	<!--					type="hidden" name="answerId" value="${a.answerId}"> <input-->
	<!--					type="submit" value="拒否">-->
	<!--			</form>-->

	<!--		</c:forEach>-->

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

</body>
</html>
