<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="./css/header.css">
<link rel="stylesheet" href="./css/layout.css">
<link rel="stylesheet" href="./css/companylist.css">
<title>日程回答確認画面（職員）</title>

<style>
.pagination {
	text-align: center;
	margin-top: 20px;
}

.pagination button {
	margin: 0 4px;
	padding: 6px 10px;
	border-radius: 4px;
	border: 1px solid #ccc;
	background-color: #fff;
	color: #333;
	cursor: pointer;
}

.pagination button:hover {
	background-color: #f2f2f2;
}

.pagination button.active {
	background-color: #e6f4ea;
	border-color: #009A37;
	color: #006b2e;
	font-weight: 600;
	cursor: default;
}
</style>

<script type="text/javascript">
	function confirmReject(answerId) {
		if (confirm("この回答を拒否します。\n卒業生へ拒否通知メールが送信されますが、よろしいですか？")) {
			location.href = "answer?command=noAnswer&answerId=" + answerId;
		}
		return false;
	}
</script>
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

		<table class="schedule-check-table">
			<tr class="schedule-check-r">
				<th class="schedule-check-h">企業名</th>
				<th class="schedule-check-h">名前</th>
				<th class="schedule-check-h">回答</th>
				<th class="schedule-check-h"></th>
			</tr>

			<c:forEach var="a" items="${answers}">
				<form action="event" method="get" class="answer-row">
					<tr class="schedule-check-r2">
						<td>${a.event.company.companyName}</td>
						<td>${a.graduate.graduateName}</td>
						<td>
							<label>
								<input type="radio" name="choice" value="1" required>
								第1希望
								${a.firstChoiceStartTime.year}/${a.firstChoiceStartTime.monthValue}/${a.firstChoiceStartTime.dayOfMonth}
								${a.firstChoiceStartTime.hour < 10 ? '0' : ''}${a.firstChoiceStartTime.hour}:
								${a.firstChoiceStartTime.minute < 10 ? '0' : ''}${a.firstChoiceStartTime.minute}～
								${a.firstChoiceEndTime.year}/${a.firstChoiceEndTime.monthValue}/${a.firstChoiceEndTime.dayOfMonth}
								${a.firstChoiceEndTime.hour < 10 ? '0' : ''}${a.firstChoiceEndTime.hour}:
								${a.firstChoiceEndTime.minute < 10 ? '0' : ''}${a.firstChoiceEndTime.minute}
							</label>
						</td>
						<td></td>
					</tr>

					<tr class="schedule-check-r3">
						<td></td>
						<td></td>
						<td>
							<label>
								<input type="radio" name="choice" value="2">
								第2希望
								${a.secondChoiceStartTime.year}/${a.secondChoiceStartTime.monthValue}/${a.secondChoiceStartTime.dayOfMonth}
								${a.secondChoiceStartTime.hour < 10 ? '0' : ''}${a.secondChoiceStartTime.hour}:
								${a.secondChoiceStartTime.minute < 10 ? '0' : ''}${a.secondChoiceStartTime.minute}～
								${a.secondChoiceEndTime.year}/${a.secondChoiceEndTime.monthValue}/${a.secondChoiceEndTime.dayOfMonth}
								${a.secondChoiceEndTime.hour < 10 ? '0' : ''}${a.secondChoiceEndTime.hour}:
								${a.secondChoiceEndTime.minute < 10 ? '0' : ''}${a.secondChoiceEndTime.minute}
							</label>
						</td>
						<td>
							<input type="hidden" name="command" value="yesAnswer">
							<input type="hidden" name="answerId" value="${a.answerId}">
							<input type="submit" value="決定" class="decision-button">
							<button type="button" class="cancel-button"
								onclick="return confirmReject(${a.answerId});">
								拒否
							</button>
						</td>
					</tr>

					<tr class="schedule-check-r">
						<td></td>
						<td></td>
						<td>
							<label>
								<input type="radio" name="choice" value="3">
								第3希望
								${a.thirdChoiceStartTime.year}/${a.thirdChoiceStartTime.monthValue}/${a.thirdChoiceStartTime.dayOfMonth}
								${a.thirdChoiceStartTime.hour < 10 ? '0' : ''}${a.thirdChoiceStartTime.hour}:
								${a.thirdChoiceStartTime.minute < 10 ? '0' : ''}${a.thirdChoiceStartTime.minute}～
								${a.thirdChoiceEndTime.year}/${a.thirdChoiceEndTime.monthValue}/${a.thirdChoiceEndTime.dayOfMonth}
								${a.thirdChoiceEndTime.hour < 10 ? '0' : ''}${a.thirdChoiceEndTime.hour}:
								${a.thirdChoiceEndTime.minute < 10 ? '0' : ''}${a.thirdChoiceEndTime.minute}
							</label>
						</td>
						<td></td>
					</tr>
				</form>
			</c:forEach>
		</table>

		<div id="pagination-answer" class="pagination"></div>
	</main>

	<footer>
		<p>
			<small>&copy; 2024 Example Inc.</small>
		</p>
	</footer>

<script>
let activePager = null;

function setupPagination(rowSelector, paginationId, rowsPerPage = 10) {

	const rows = document.querySelectorAll(rowSelector);
	const pagination = document.getElementById(paginationId);

	if (!rows.length) return null;

	const totalPages = Math.ceil(rows.length / rowsPerPage);
	let currentPage = 1;
	const buttons = [];

	function showPage(page) {
		if (page < 1 || page > totalPages) return;

		currentPage = page;
		const start = (page - 1) * rowsPerPage;
		const end = start + rowsPerPage;

		rows.forEach((row, index) => {
			row.style.display = (index >= start && index < end) ? "" : "none";
		});

		buttons.forEach(btn => btn.classList.remove("active"));
		if (buttons[page - 1]) {
			buttons[page - 1].classList.add("active");
		}
	}

	for (let i = 1; i <= totalPages; i++) {
		const btn = document.createElement("button");
		btn.textContent = i;
		btn.onclick = () => {
			activePager = pager;
			showPage(i);
		};
		buttons.push(btn);
		pagination.appendChild(btn);
	}

	showPage(1);

	const pager = {
		next() { showPage(currentPage + 1); },
		prev() { showPage(currentPage - 1); }
	};

	return pager;
}

const answerPager = setupPagination(".answer-row", "pagination-answer", 10);
activePager = answerPager;

document.addEventListener("keydown", function(e) {
	const tag = document.activeElement.tagName;
	if (tag === "INPUT" || tag === "TEXTAREA") return;
	if (!activePager) return;

	if (e.key === "ArrowRight") activePager.next();
	if (e.key === "ArrowLeft") activePager.prev();
});
</script>

</body>
</html>